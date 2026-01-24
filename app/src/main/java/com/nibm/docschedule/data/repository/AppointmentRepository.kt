package com.nibm.docschedule.data.repository

import com.google.firebase.database.FirebaseDatabase
import com.nibm.docschedule.data.model.AppointmentModel

class AppointmentRepository {

    private val db =
        FirebaseDatabase.getInstance().getReference("appointments")

    // ✅ Book appointment
    fun bookAppointment(
        appointment: AppointmentModel,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        val id = db.push().key
        if (id == null) {
            onFailure("Failed to generate appointment ID")
            return
        }

        db.child(id)
            .setValue(appointment.copy())
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFailure(it.message ?: "Error") }
    }

    // ✅ Cancel appointment
    fun cancelAppointment(
        appointmentId: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        db.child(appointmentId)
            .child("status")
            .setValue("CANCELLED")
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFailure(it.message ?: "Error") }
    }

    // ✅ Reschedule appointment
    fun rescheduleAppointment(
        appointmentId: String,
        newDate: String,
        newTime: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        val updates = mapOf(
            "date" to newDate,
            "time" to newTime,
            "timestamp" to System.currentTimeMillis()
        )

        db.child(appointmentId)
            .updateChildren(updates)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFailure(it.message ?: "Error") }
    }
}
