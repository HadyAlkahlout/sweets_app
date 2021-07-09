package com.raiyansoft.sweetsapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.raiyansoft.sweetsapp.R
import com.raiyansoft.sweetsapp.databinding.ItemOrderProductBinding
import com.raiyansoft.sweetsapp.models.order.OrderProduct

class OrderProductAdapter : RecyclerView.Adapter<OrderProductAdapter.ViewHolder>() {

    val data = ArrayList<OrderProduct>()

    inner class ViewHolder(private val binding : ItemOrderProductBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(product : OrderProduct) {
            binding.product = product
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding : ItemOrderProductBinding = DataBindingUtil.inflate(inflater, R.layout.item_order_product, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }
}