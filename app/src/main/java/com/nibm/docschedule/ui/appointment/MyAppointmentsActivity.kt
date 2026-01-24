package com.nibm.docschedule.ui.appointment

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.nibm.docschedule.data.model.AppointmentModel
import com.nibm.docschedule.databinding.ActivityMyAppointmentsBinding
import com.nibm.docschedule.ui.adapter.AppointmentAdapter

class MyAppointmentsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMyAppointmentsBinding
    private val appointmentList = mutableListOf<AppointmentModel>()
    private lateinit var adapter: AppointmentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyAppointmentsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Make sure you import AppointmentAdapter from the same package
        adapter = AppointmentAdapter(
            appointments = appointmentList,
            onCancel = { cancelAppointment(it) },
            onReschedule = { rescheduleAppointment(it) }
        )

        binding.appointmentsRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.appointmentsRecyclerView.adapter = adapter

        loadAppointments()
    }

    private fun loadAppointments() {
        val uid = FirebaseAuth.getInstance().uid ?: return
        val ref = FirebaseDatabase.getInstance().getReference("appointments")

        ref.orderByChild("userId").equalTo(uid)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    appointmentList.clear()
                    snapshot.children.forEach {
                        val appointment = it.getValue(AppointmentModel::class.java)
                        appointment?.let { appointmentList.add(it) }
                    }
                    adapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {}
            })
    }

    private fun cancelAppointment(appointment: AppointmentModel) {
        val ref = FirebaseDatabase.getInstance().getReference("appointments")
        ref.child(appointment.appointmentId).removeValue()
            .addOnSuccessListener {
                Toast.makeText(this, "Appointment Cancelled", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to cancel", Toast.LENGTH_SHORT).show()
            }
    }

    private fun rescheduleAppointment(appointment: AppointmentModel) {
        val intent = Intent(this, MakeAppointmentActivity::class.java)
        intent.putExtra("appointment", appointment) // Pass existing appointment
        startActivity(intent)
    }
}
