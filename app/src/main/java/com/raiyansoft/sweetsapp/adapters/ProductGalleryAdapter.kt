package com.raiyansoft.sweetsapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.raiyansoft.sweetsapp.R
import com.raiyansoft.sweetsapp.databinding.ItemProductBannerBinding
import com.raiyansoft.sweetsapp.models.product.Gallery

class ProductGalleryAdapter :
    RecyclerView.Adapter<ProductGalleryAdapter.ViewHolder>() {

    var data = ArrayList<Gallery>()

    inner class ViewHolder(var view: ItemProductBannerBinding) :
        RecyclerView.ViewHolder(view.root) {
        fun bind(gallery: Gallery) {
            view.banner = gallery
            view.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemProductBannerBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_product_banner,
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