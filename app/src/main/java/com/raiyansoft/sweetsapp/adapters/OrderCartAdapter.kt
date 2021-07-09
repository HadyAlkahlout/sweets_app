package com.raiyansoft.sweetsapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.raiyansoft.sweetsapp.R
import com.raiyansoft.sweetsapp.databinding.ItemCartOrderBinding
import com.raiyansoft.sweetsapp.models.order.OrderItem

class OrderCartAdapter(val context: Context) :
    RecyclerView.Adapter<OrderCartAdapter.ViewHolder>() {

    var data = ArrayList<OrderItem>()

    inner class ViewHolder(var view: ItemCartOrderBinding) :
        RecyclerView.ViewHolder(view.root) {
        fun bind(item: OrderItem) {
            view.item = item
            val adapter = OrderProductAdapter()
            view.rcProducts.layoutManager = LinearLayoutManager(context)
            view.rcProducts.adapter = adapter
            adapter.data.addAll(item.products)
            adapter.notifyDataSetChanged()
            view.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemCartOrderBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_cart_order,
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }

}