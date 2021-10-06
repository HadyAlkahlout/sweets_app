package com.raiyansoft.sweetsapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.raiyansoft.sweetsapp.R
import com.raiyansoft.sweetsapp.databinding.ItemReviewBinding
import com.raiyansoft.sweetsapp.models.reviews.Rate

class RatesAdapter : RecyclerView.Adapter<RatesAdapter.ViewHolder>() {

    val data = ArrayList<Rate>()

    inner class ViewHolder(private val binding : ItemReviewBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(rate : Rate) {
            binding.rate = rate
            binding.rbMyRate.rating = rate.rate.toFloat()
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding : ItemReviewBinding = DataBindingUtil.inflate(inflater, R.layout.item_review, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }
}