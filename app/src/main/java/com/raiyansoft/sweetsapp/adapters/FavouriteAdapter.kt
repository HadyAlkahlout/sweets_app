package com.raiyansoft.sweetsapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.raiyansoft.sweetsapp.R
import com.raiyansoft.sweetsapp.databinding.ItemFavouriteBinding
import com.raiyansoft.sweetsapp.models.favorite.FavProduct
import com.raiyansoft.sweetsapp.models.product.Product

class FavouriteAdapter(val onClick: (product: FavProduct) -> Unit ) : RecyclerView.Adapter<FavouriteAdapter.ViewHolder>() {

    val data = ArrayList<FavProduct>()

    inner class ViewHolder(private val binding : ItemFavouriteBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(product: FavProduct) {
            binding.product = product
            binding.productLayout.setOnClickListener {
                onClick(product)
            }
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding : ItemFavouriteBinding = DataBindingUtil.inflate(inflater, R.layout.item_favourite, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }
}