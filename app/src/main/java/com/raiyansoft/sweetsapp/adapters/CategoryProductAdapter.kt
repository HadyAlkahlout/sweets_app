package com.raiyansoft.sweetsapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.raiyansoft.sweetsapp.R
import com.raiyansoft.sweetsapp.databinding.ItemCategoryProductBinding
import com.raiyansoft.sweetsapp.models.home.Store
import com.raiyansoft.sweetsapp.models.product.Product

class CategoryProductAdapter(val onClick: (product : Product) -> Unit, val addCart: (product : Product) -> Unit, val openMenu: (shopID : Int) -> Unit) : RecyclerView.Adapter<CategoryProductAdapter.ViewHolder>() {

    val data = ArrayList<Product>()

    inner class ViewHolder(private val binding : ItemCategoryProductBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(product : Product) {
            binding.product = product
            binding.itemProduct.setOnClickListener {
                onClick(product)
            }
            binding.tvAddCart.setOnClickListener {
                addCart(product)
            }
            binding.tvMenu.setOnClickListener {
                openMenu(product.store_id)
            }
            if (product.offer == 0.0){
                binding.tvOff.visibility = View.GONE
            } else {
                binding.tvOff.text = "${product.offer}% OFF"
            }
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding : ItemCategoryProductBinding = DataBindingUtil.inflate(inflater, R.layout.item_category_product, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }
}