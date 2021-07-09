package com.raiyansoft.sweetsapp.adapters

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.raiyansoft.sweetsapp.R
import com.raiyansoft.sweetsapp.databinding.ItemHomeBannerBinding
import com.raiyansoft.sweetsapp.models.home.Ad

class HomeBannerAdapter(val activity: Activity, val onClick: (ad : Ad) -> Unit) :
    RecyclerView.Adapter<HomeBannerAdapter.ViewHolder>() {

    var data = ArrayList<Ad>()

    inner class ViewHolder(var view: ItemHomeBannerBinding) :
        RecyclerView.ViewHolder(view.root) {
        fun bind(ad: Ad) {
            view.ad = ad
            view.imgBanner.setOnClickListener {
                onClick(ad)
            }
            view.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemHomeBannerBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_home_banner,
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