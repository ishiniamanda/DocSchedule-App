package com.nibm.docschedule.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.request.RequestOptions
import com.nibm.docschedule.Activity.DetailActivity
import com.nibm.docschedule.Domain.DoctorsModel
import com.nibm.docschedule.databinding.ViewholderTopDoctorBinding

class TopDoctorAdapter(val items: MutableList<DoctorsModel>) :
    RecyclerView.Adapter<TopDoctorAdapter.ViewHolder>() {

    private var context: Context?=null

    class ViewHolder(val binding: ViewholderTopDoctorBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context   // ❌ FIXED: `context` was val and null before
        val binding = ViewholderTopDoctorBinding.inflate(
            LayoutInflater.from(context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.nameTxt.text = items[position].Name
        holder.binding.specialTxt.text = items[position].Special
        holder.binding.scoreTxt.text = items[position].Rating.toString()
        holder.binding.yearTxt.text = items[position].Expriense.toString() + " Year"

        Glide.with(holder.itemView.context)
            .load(items[position].Picture)
            .apply { RequestOptions().transform(CenterCrop()) }
            .into(holder.binding.img)

        holder.itemView.setOnClickListener {
            val intent= Intent(context, DetailActivity::class.java)
            intent.putExtra("object",items[position])
            context?.startActivity(intent)
        }
    }

    override fun getItemCount(): Int =items.size
}
