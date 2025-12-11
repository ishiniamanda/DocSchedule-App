package com.nibm.docschedule.data.repository

import com.google.firebase.database.FirebaseDatabase
import com.nibm.docschedule.data.model.CategoryModel

class CategoryRepository {

    private val db = FirebaseDatabase.getInstance()

    fun loadCategories(onResult: (List<CategoryModel>) -> Unit) {
        val ref = db.getReference("Category")

        ref.get().addOnSuccessListener { snapshot ->
            val list = mutableListOf<CategoryModel>()

            snapshot.children.forEach { child ->
                val item = child.getValue(CategoryModel::class.java)
                if (item != null) list.add(item)
            }

            onResult(list)
        }
    }
}
