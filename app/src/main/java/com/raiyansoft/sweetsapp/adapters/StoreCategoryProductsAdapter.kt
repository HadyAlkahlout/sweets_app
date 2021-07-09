package com.raiyansoft.sweetsapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.raiyansoft.sweetsapp.R
import com.raiyansoft.sweetsapp.databinding.ItemStoreCategoryProductsBinding
import com.raiyansoft.sweetsapp.models.product.Product
import com.raiyansoft.sweetsapp.models.store.StoreCategory

class StoreCategoryProductsAdapter(val context: Context, val onClick: (product : Product) -> Unit, val addCart: (product : Product) -> Unit) :
    RecyclerView.Adapter<StoreCategoryProductsAdapter.ViewHolder>() {

    var data = ArrayList<StoreCategory>()

    inner class ViewHolder(var view: ItemStoreCategoryProductsBinding) :
        RecyclerView.ViewHolder(view.root) {
        fun bind(category: StoreCategory) {
            view.category = category
            val adapter = StoreProductAdapter(
                { product ->
                    onClick(product)
                }, { product ->
                    // add to cart
                    addCart(product)
                }
            )
            view.rcProducts.layoutManager = LinearLayoutManager(context)
            view.rcProducts.startAnimation(AnimationUtils.loadAnimation(context, R.anim.recyclerview_animation))
            view.rcProducts.adapter = adapter
            adapter.data.addAll(category.products)
            adapter.notifyDataSetChanged()
            view.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemStoreCategoryProductsBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_store_category_products,
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