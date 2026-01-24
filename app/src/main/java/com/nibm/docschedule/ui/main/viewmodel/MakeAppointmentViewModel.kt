package com.nibm.docschedule.ui.main.viewmodel

import androidx.lifecycle.ViewModel
import com.google.firebase.database.FirebaseDatabase
import com.nibm.docschedule.data.model.AppointmentModel

class MakeAppointmentViewModel : ViewModel() {

    fun bookAppointment(
        appointment: AppointmentModel,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val ref = FirebaseDatabase.getInstance().getReference("appointments")
        val id = ref.push().key ?: return onError("Invalid ID")

        appointment.appointmentId = id

        ref.child(id).setValue(appointment)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onError(it.message ?: "Failed") }
    }

    fun updateAppointment(
        appointment: AppointmentModel,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        FirebaseDatabase.getInstance()
            .getReference("appointments")
            .child(appointment.appointmentId)
            .setValue(appointment)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onError(it.message ?: "Failed") }
    }
}
