package com.nibm.docschedule.data.repository

import com.google.firebase.database.FirebaseDatabase
import com.nibm.docschedule.data.model.DoctorsModel

class DoctorsRepository {

    private val db = FirebaseDatabase.getInstance()

    fun loadDoctors(onResult: (List<DoctorsModel>) -> Unit) {
        val ref = db.getReference("Doctors")

        ref.get().addOnSuccessListener { snapshot ->
            val list = mutableListOf<DoctorsModel>()

            snapshot.children.forEach { child ->
                val item = child.getValue(DoctorsModel::class.java)
                if (item != null) list.add(item)
            }

            onResult(list)
        }
    }
}
