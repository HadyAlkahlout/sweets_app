package com.raiyansoft.sweetsapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.raiyansoft.sweetsapp.R
import com.raiyansoft.sweetsapp.databinding.ItemHomeProductBinding
import com.raiyansoft.sweetsapp.models.home.NewProduct

class HomeProductAdapter(val onClick: (product: NewProduct) -> Unit ) : RecyclerView.Adapter<HomeProductAdapter.ViewHolder>() {

    val data = ArrayList<NewProduct>()

    inner class ViewHolder(private val binding : ItemHomeProductBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(product: NewProduct) {
            binding.product = product
            binding.productLayout.setOnClickListener {
                onClick(product)
            }
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding : ItemHomeProductBinding = DataBindingUtil.inflate(inflater, R.layout.item_home_product, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }
}