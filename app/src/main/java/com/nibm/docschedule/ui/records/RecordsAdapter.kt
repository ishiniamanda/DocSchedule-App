package com.nibm.docschedule.ui.records

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nibm.docschedule.R
import com.nibm.docschedule.data.model.MedicalRecordModel

class RecordsAdapter(
    private val list: List<MedicalRecordModel>
) : RecyclerView.Adapter<RecordsAdapter.RecordViewHolder>() {

    class RecordViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTitle: TextView = view.findViewById(R.id.tvTitle)
        val tvType: TextView = view.findViewById(R.id.tvType)
        val tvDate: TextView = view.findViewById(R.id.tvDate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecordViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_medical_record, parent, false)
        return RecordViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecordViewHolder, position: Int) {
        val record = list[position]
        holder.tvTitle.text = record.title
        holder.tvType.text = record.type
        holder.tvDate.text = record.date
    }

    override fun getItemCount(): Int = list.size
}
