package com.raiyansoft.sweetsapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.raiyansoft.sweetsapp.R
import com.raiyansoft.sweetsapp.databinding.ItemSummaryBinding
import com.raiyansoft.sweetsapp.models.cart.Product

class SummaryAdapter : RecyclerView.Adapter<SummaryAdapter.ViewHolder>() {

    val data = ArrayList<Product>()

    inner class ViewHolder(private val binding : ItemSummaryBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(product : Product) {
            binding.product = product
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding : ItemSummaryBinding = DataBindingUtil.inflate(inflater, R.layout.item_summary, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }
}