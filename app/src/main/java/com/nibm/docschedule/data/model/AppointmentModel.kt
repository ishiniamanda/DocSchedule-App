package com.nibm.docschedule.data.model

import java.io.Serializable

data class AppointmentModel(
    var appointmentId: String = "",
    var doctorId: Int = 0,
    var doctorName: String = "",
    var doctorSpecial: String = "",
    var userId: String = "",
    var date: String = "",
    var time: String = "",
    var status: String = "BOOKED",
    var timestamp: Long = System.currentTimeMillis()
) : Serializable


