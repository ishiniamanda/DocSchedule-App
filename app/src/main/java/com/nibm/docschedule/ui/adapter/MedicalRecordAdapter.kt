package com.nibm.docschedule.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nibm.docschedule.R
import com.nibm.docschedule.data.model.MedicalRecordModel

class MedicalRecordAdapter(
    private val recordList: List<MedicalRecordModel>
) : RecyclerView.Adapter<MedicalRecordAdapter.RecordViewHolder>() {

    inner class RecordViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivRecordImage: ImageView = itemView.findViewById(R.id.ivRecordImage)
        val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        val tvType: TextView = itemView.findViewById(R.id.tvType)
        val tvDate: TextView = itemView.findViewById(R.id.tvDate)
        val tvUser: TextView = itemView.findViewById(R.id.tvUser)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecordViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_medical_record, parent, false)
        return RecordViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecordViewHolder, position: Int) {
        val record = recordList[position]

        holder.tvTitle.text = record.title
        holder.tvType.text = record.type
        holder.tvDate.text = record.date
        holder.tvUser.text = record.user

        Glide.with(holder.itemView.context)
            .load(record.imageUrl) // ✅ LOAD STRING URL DIRECTLY
            .placeholder(R.drawable.ic_launcher_foreground)
            .error(R.drawable.ic_launcher_foreground)
            .into(holder.ivRecordImage)
    }

    override fun getItemCount(): Int = recordList.size
}
