package com.raiyansoft.sweetsapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.raiyansoft.sweetsapp.R
import com.raiyansoft.sweetsapp.databinding.ItemStoreProductBinding
import com.raiyansoft.sweetsapp.models.product.Product
import kotlin.math.nextUp

class StoreProductAdapter(val onClick: (product : Product) -> Unit, val addCart: (product : Product) -> Unit) : RecyclerView.Adapter<StoreProductAdapter.ViewHolder>() {

    val data = ArrayList<Product>()

    inner class ViewHolder(private val binding : ItemStoreProductBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(product : Product) {
            binding.product = product
            binding.tvPrice.text = product.price.toString()
            binding.itemOrder.setOnClickListener {
                onClick(product)
            }
            binding.tvAddCart.setOnClickListener {
                addCart(product)
            }
            if (product.offer == 0.0){
                binding.tvOff.visibility = View.GONE
            }else{
                binding.tvOff.visibility = View.VISIBLE
                binding.tvOff.text = "${product.offer}% OFF"
            }
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding : ItemStoreProductBinding = DataBindingUtil.inflate(inflater, R.layout.item_store_product, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }
}