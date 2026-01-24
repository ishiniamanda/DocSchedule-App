package com.nibm.docschedule.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nibm.docschedule.R
import com.nibm.docschedule.data.model.AppointmentModel

class AppointmentAdapter(
    private val appointments: List<AppointmentModel>,
    private val onCancel: (AppointmentModel) -> Unit,
    private val onReschedule: (AppointmentModel) -> Unit
) : RecyclerView.Adapter<AppointmentAdapter.AppointmentViewHolder>() {

    inner class AppointmentViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val doctorName: TextView = view.findViewById(R.id.doctorNameTxt)
        val doctorSpecial: TextView = view.findViewById(R.id.doctorSpecialTxt)
        val dateTime: TextView = view.findViewById(R.id.dateTimeTxt)
        val cancelBtn: Button = view.findViewById(R.id.cancelBtn)
        val rescheduleBtn: Button = view.findViewById(R.id.rescheduleBtn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppointmentViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_appointment, parent, false)
        return AppointmentViewHolder(view)
    }

    override fun onBindViewHolder(holder: AppointmentViewHolder, position: Int) {
        val appointment = appointments[position]

        holder.doctorName.text = appointment.doctorName
        holder.doctorSpecial.text = appointment.doctorSpecial
        holder.dateTime.text = "${appointment.date} - ${appointment.time}"

        // ✅ Cancel appointment
        holder.cancelBtn.setOnClickListener {
            onCancel(appointment)
        }

        // ✅ Reschedule appointment (PASS SAME OBJECT)
        holder.rescheduleBtn.setOnClickListener {
            onReschedule(appointment)
        }
    }

    override fun getItemCount(): Int = appointments.size
}
