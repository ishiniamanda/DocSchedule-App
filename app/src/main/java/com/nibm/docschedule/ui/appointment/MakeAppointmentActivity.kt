package com.nibm.docschedule.ui.appointment

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.nibm.docschedule.data.model.AppointmentModel
import com.nibm.docschedule.data.model.DoctorsModel
import com.nibm.docschedule.databinding.ActivityMakeAppointmentBinding
import com.nibm.docschedule.worker.AppointmentReminderWorker
import java.util.*
import java.util.concurrent.TimeUnit

class MakeAppointmentActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMakeAppointmentBinding

    private lateinit var doctor: DoctorsModel
    private var selectedDate = ""
    private var selectedTime = ""

    private var appointmentId: String? = null
    private var isReschedule = false

    private val timeSlots = listOf(
        "09:00 AM","10:00 AM","11:00 AM",
        "12:00 PM","02:00 PM","03:00 PM",
        "04:00 PM","05:00 PM","06:00 PM","07:00 PM"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMakeAppointmentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // GET DATA FROM INTENT
        doctor = intent.getParcelableExtra("doctor")!!
        appointmentId = intent.getStringExtra("appointmentId")
        isReschedule = appointmentId != null

        setupDoctor()
        setupCalendar()

        // LOAD EXISTING APPOINTMENT IF RESCHEDULE
        if (isReschedule) loadExistingAppointment()

        // CONFIRM BUTTON
        binding.confirmBtn.setOnClickListener {
            if (isReschedule) rescheduleAppointment() else bookAppointment()
        }

        // VIEW MY APPOINTMENTS BUTTON
        binding.viewAppointmentsBtn.setOnClickListener {
            startActivity(Intent(this, MyAppointmentsActivity::class.java))
        }
    }

    private fun setupDoctor() {
        binding.doctorNameTxt.text = doctor.Name
        binding.doctorSpecialTxt.text = doctor.Special
        Glide.with(this).load(doctor.Picture).into(binding.doctorImg)
    }

    private fun setupCalendar() {
        val today = Calendar.getInstance()
        binding.calendarView.minDate = today.timeInMillis

        binding.calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            selectedDate = String.format("%02d-%02d-%04d", dayOfMonth, month + 1, year)
            loadAvailableTimeSlots()
        }
    }

    // ---------------- LOAD AVAILABLE TIME SLOTS ----------------
    private fun loadAvailableTimeSlots() {
        if (selectedDate.isEmpty()) return

        val ref: Query = FirebaseDatabase.getInstance()
            .getReference("appointments")
            .orderByChild("doctorId")
            .equalTo(doctor.Id.toDouble()) // Firebase stores numbers as Double

        ref.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                val bookedTimes = mutableSetOf<String>()

                for (child in snapshot.children) {
                    val appt = child.getValue(AppointmentModel::class.java)
                    if (appt != null && appt.date == selectedDate) {
                        if (isReschedule && appt.appointmentId == appointmentId) continue
                        bookedTimes.add(appt.time)
                    }
                }

                showAvailableSlots(bookedTimes)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@MakeAppointmentActivity, "Failed to load slots", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun showAvailableSlots(bookedTimes: Set<String>) {
        binding.timeSlotGrid.removeAllViews()
        selectedTime = ""

        timeSlots.forEach { time ->
            if (!bookedTimes.contains(time)) {
                val btn = Button(this)
                btn.text = time
                btn.setOnClickListener {
                    highlightSelected(btn)
                    selectedTime = time
                }
                binding.timeSlotGrid.addView(btn)
            }
        }

        if (binding.timeSlotGrid.childCount == 0) {
            Toast.makeText(this, "No available time slots", Toast.LENGTH_SHORT).show()
        }
    }

    private fun highlightSelected(selectedBtn: Button) {
        for (i in 0 until binding.timeSlotGrid.childCount) {
            val btn = binding.timeSlotGrid.getChildAt(i) as Button
            btn.alpha = 1f
        }
        selectedBtn.alpha = 0.5f
    }

    private fun bookAppointment() {
        if (selectedDate.isEmpty() || selectedTime.isEmpty()) {
            Toast.makeText(this, "Select date and time", Toast.LENGTH_SHORT).show()
            return
        }

        val ref = FirebaseDatabase.getInstance().getReference("appointments")
        val key = ref.push().key!!

        val appointment = AppointmentModel(
            appointmentId = key,
            doctorId = doctor.Id,
            doctorName = doctor.Name,
            userId = FirebaseAuth.getInstance().uid ?: "",
            date = selectedDate,
            time = selectedTime,
            status = "BOOKED"
        )

        ref.child(key).setValue(appointment)
            .addOnSuccessListener {
                Toast.makeText(this, "Appointment Booked", Toast.LENGTH_LONG).show()

                // Schedule reminder 5 minutes before appointment
                scheduleReminderWorker(selectedDate, selectedTime, doctor.Name)

                startActivity(Intent(this, MyAppointmentsActivity::class.java))
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Booking failed", Toast.LENGTH_SHORT).show()
            }
    }

    private fun loadExistingAppointment() {
        FirebaseDatabase.getInstance()
            .getReference("appointments")
            .child(appointmentId!!)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val appt = snapshot.getValue(AppointmentModel::class.java)
                    appt?.let {
                        selectedDate = it.date
                        selectedTime = it.time
                        binding.confirmBtn.text = "Reschedule Appointment"
                        loadAvailableTimeSlots()
                    }
                }

                override fun onCancelled(error: DatabaseError) {}
            })
    }

    private fun rescheduleAppointment() {
        if (selectedDate.isEmpty() || selectedTime.isEmpty()) {
            Toast.makeText(this, "Select date and time", Toast.LENGTH_SHORT).show()
            return
        }

        val updates = mapOf(
            "date" to selectedDate,
            "time" to selectedTime,
            "status" to "RESCHEDULED"
        )

        FirebaseDatabase.getInstance()
            .getReference("appointments")
            .child(appointmentId!!)
            .updateChildren(updates)
            .addOnSuccessListener {
                Toast.makeText(this, "Appointment Rescheduled", Toast.LENGTH_LONG).show()

                // Schedule reminder 5 minutes before appointment
                scheduleReminderWorker(selectedDate, selectedTime, doctor.Name)

                startActivity(Intent(this, MyAppointmentsActivity::class.java))
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Reschedule failed", Toast.LENGTH_SHORT).show()
            }
    }

    // ---------------- WORKMANAGER REMINDER ----------------
    private fun scheduleReminderWorker(date: String, time: String, doctorName: String) {
        try {
            val calendar = Calendar.getInstance()
            val parts = date.split("-")
            val day = parts[0].toInt()
            val month = parts[1].toInt() - 1
            val year = parts[2].toInt()

            val timeParts = time.split(":| ".toRegex())
            var hour = timeParts[0].toInt()
            val minute = timeParts[1].toInt()
            val amPm = timeParts[2]

            if (amPm.equals("PM", true) && hour != 12) hour += 12
            if (amPm.equals("AM", true) && hour == 12) hour = 0

            calendar.set(year, month, day, hour, minute, 0)
            calendar.add(Calendar.MINUTE, -5) // 5-minute reminder

            val delay = calendar.timeInMillis - System.currentTimeMillis()
            if (delay <= 0) return // past time, skip

            val data = Data.Builder()
                .putString("doctorName", doctorName)
                .putString("date", date)
                .putString("time", time)
                .build()

            val workRequest = OneTimeWorkRequestBuilder<AppointmentReminderWorker>()
                .setInitialDelay(delay, TimeUnit.MILLISECONDS)
                .setInputData(data)
                .build()

            WorkManager.getInstance(this).enqueue(workRequest)

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
