package com.raiyansoft.sweetsapp.adapters

import android.app.Activity
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.raiyansoft.sweetsapp.R
import com.raiyansoft.sweetsapp.databinding.ItemOrderBinding
import com.raiyansoft.sweetsapp.models.order.PrevOrder

class OrdersAdapter(val activity: Activity, val onClick: (order : PrevOrder) -> Unit ) : RecyclerView.Adapter<OrdersAdapter.ViewHolder>() {

    val data = ArrayList<PrevOrder>()

    inner class ViewHolder(private val binding : ItemOrderBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(order : PrevOrder) {
            binding.order = order
            binding.itemOrder.setOnClickListener {
                onClick(order)
            }
            when(order.status) {
                "opened" -> {
                    binding.tvStatus.setTextColor(Color.parseColor("#FFAB2A"))
                    binding.tvStatus.text = activity.getString(R.string.status_opened)
                }
                "submitted" -> {
                    binding.tvStatus.setTextColor(Color.parseColor("#FFAB2A"))
                    binding.tvStatus.text = activity.getString(R.string.status_submitted)
                }
                "accepted" -> {
                    binding.tvStatus.setTextColor(Color.parseColor("#FFAB2A"))
                    binding.tvStatus.text = activity.getString(R.string.status_accepted)
                }
                "prepared" -> {
                    binding.tvStatus.setTextColor(Color.parseColor("#FFAB2A"))
                    binding.tvStatus.text = activity.getString(R.string.status_prepared)
                }
                "on_delivery" -> {
                    binding.tvStatus.setTextColor(Color.parseColor("#F48B85"))
                    binding.tvStatus.text = activity.getString(R.string.status_on_delivery)
                }
                "delivered" -> {
                    binding.tvStatus.setTextColor(Color.parseColor("#A7D4C8"))
                    binding.tvStatus.text = activity.getString(R.string.status_delivered)
                }
                "canceled" -> {
                    binding.tvStatus.setTextColor(Color.parseColor("#FF5A50"))
                    binding.tvStatus.text = activity.getString(R.string.status_canceled)
                }
            }
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding : ItemOrderBinding = DataBindingUtil.inflate(inflater, R.layout.item_order, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }
}