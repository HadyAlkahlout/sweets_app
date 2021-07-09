package com.raiyansoft.sweetsapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.raiyansoft.sweetsapp.R
import com.raiyansoft.sweetsapp.databinding.ItemShopBinding
import com.raiyansoft.sweetsapp.models.home.Store

class ShopAdapter(val onClick: (store : Store) -> Unit ) : RecyclerView.Adapter<ShopAdapter.ViewHolder>() {

    val data = ArrayList<Store>()

    inner class ViewHolder(private val binding : ItemShopBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(store : Store) {
            binding.store = store
            binding.imgBanner
            binding.itemStore.setOnClickListener {
                onClick(store)
            }
            if (store.offer == 0.0){
                binding.tvOff.visibility = View.GONE
            } else {
                binding.tvOff.text = "${store.offer}% OFF"
            }
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding : ItemShopBinding = DataBindingUtil.inflate(inflater, R.layout.item_shop, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }
}