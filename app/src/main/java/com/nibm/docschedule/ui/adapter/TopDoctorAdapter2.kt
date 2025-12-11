package com.nibm.docschedule.ui.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.request.RequestOptions
import com.nibm.docschedule.data.model.DoctorsModel
import com.nibm.docschedule.databinding.ViewholderTopDoctor2Binding
import com.nibm.docschedule.ui.details.DetailActivity

class TopDoctorAdapter2(val items: MutableList<DoctorsModel>) :
    RecyclerView.Adapter<TopDoctorAdapter2.ViewHolder>() {

    private var context: Context?=null

    class ViewHolder(val binding: ViewholderTopDoctor2Binding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context   // ❌ FIXED: `context` was val and null before
        val binding = ViewholderTopDoctor2Binding.inflate(
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
        holder.binding.ratingBar2.rating=items[position].Rating.toFloat()
        holder.binding.scoreTxt.text=items[position].Rating.toString()
        holder.binding.degreeTxt.text="Professional Doctor"


        Glide.with(holder.itemView.context)
            .load(items[position].Picture)
            .apply { RequestOptions().transform(CenterCrop()) }
            .into(holder.binding.img)

        holder.binding.makeBtn.setOnClickListener {
            val intent= Intent(context, DetailActivity::class.java)
            intent.putExtra("object",items[position])
            context?.startActivity(intent)
        }
    }

    override fun getItemCount(): Int =items.size
}