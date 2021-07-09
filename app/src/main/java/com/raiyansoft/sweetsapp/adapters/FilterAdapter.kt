package com.raiyansoft.sweetsapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.raiyansoft.sweetsapp.R
import com.raiyansoft.sweetsapp.databinding.ItemFilterBinding
import com.raiyansoft.sweetsapp.models.filter.Data

class FilterAdapter : RecyclerView.Adapter<FilterAdapter.ViewHolder>() {

    val data = ArrayList<Data>()

    inner class ViewHolder(private val binding: ItemFilterBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(filter: Data) {
            binding.data = filter
            binding.cbSelected.setOnCheckedChangeListener { _, isChecked ->
                filter.selected = isChecked
            }
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding: ItemFilterBinding =
            DataBindingUtil.inflate(inflater, R.layout.item_filter, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }
}