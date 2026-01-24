package com.nibm.docschedule.ui.records

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.nibm.docschedule.R
import com.nibm.docschedule.data.model.MedicalRecordModel
import com.nibm.docschedule.ui.adapter.MedicalRecordAdapter

class RecordsActivity : AppCompatActivity() {

    private val list = mutableListOf<MedicalRecordModel>()
    private lateinit var adapter: MedicalRecordAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_records) // ✅ This is now the activity layout with RecyclerView

        val recyclerViewRecords = findViewById<RecyclerView>(R.id.recyclerViewRecords)
        val fabAddRecord = findViewById<FloatingActionButton>(R.id.fabAddRecord)

        adapter = MedicalRecordAdapter(list)
        recyclerViewRecords.layoutManager = LinearLayoutManager(this)
        recyclerViewRecords.adapter = adapter

        fabAddRecord.setOnClickListener {
            startActivity(Intent(this, AddRecordActivity::class.java))
        }

        loadRecords()
    }

    private fun loadRecords() {
        val userId = FirebaseAuth.getInstance().currentUser!!.uid

        FirebaseDatabase.getInstance()
            .getReference("MedicalRecords")
            .child(userId)
            .addValueEventListener(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {
                    list.clear()
                    for (child in snapshot.children) {
                        val record = child.getValue(MedicalRecordModel::class.java)
                        if (record != null) list.add(record)
                    }
                    adapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {}
            })
    }
}
