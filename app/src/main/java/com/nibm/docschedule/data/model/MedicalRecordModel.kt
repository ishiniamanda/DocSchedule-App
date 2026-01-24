package com.nibm.docschedule.data.model

data class MedicalRecordModel(
    var id: String = "",
    var title: String = "",
    var type: String = "",
    var date: String = "",
    var user: String = "",
    val imageUrl: String = ""
)
