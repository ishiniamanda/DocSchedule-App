package com.nibm.docschedule.ui.appointment

import androidx.lifecycle.ViewModel
import com.nibm.docschedule.data.model.AppointmentModel
import com.nibm.docschedule.data.repository.AppointmentRepository

class MakeAppointmentViewModel : ViewModel() {

    private val repository = AppointmentRepository()

    fun bookAppointment(
        appointment: AppointmentModel,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        repository.bookAppointment(appointment, onSuccess, onFailure)
    }
}
