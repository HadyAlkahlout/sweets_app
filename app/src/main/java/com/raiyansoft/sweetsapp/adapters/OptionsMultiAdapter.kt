package com.raiyansoft.sweetsapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.raiyansoft.sweetsapp.R
import com.raiyansoft.sweetsapp.databinding.ItemProductOptionBinding
import com.raiyansoft.sweetsapp.models.product.Option

class OptionsMultiAdapter(
    val onSelect: (isSelected: Boolean, price: Double) -> Unit,
) : RecyclerView.Adapter<OptionsMultiAdapter.ViewHolder>() {

    val data = ArrayList<Option>()

    inner class ViewHolder(private val binding: ItemProductOptionBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(option: Option) {
            binding.option = option
            binding.clOptions.setOnClickListener {
                option.isSelected = !option.isSelected
                onSelect(option.isSelected, option.price.toDouble())
                notifyDataSetChanged()
            }
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding: ItemProductOptionBinding =
            DataBindingUtil.inflate(inflater, R.layout.item_product_option, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }
}