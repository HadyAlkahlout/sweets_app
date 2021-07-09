package com.raiyansoft.sweetsapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.raiyansoft.sweetsapp.R
import com.raiyansoft.sweetsapp.databinding.ItemHomeStoreBinding
import com.raiyansoft.sweetsapp.models.home.Store

class HomeStoreAdapter(val onClick: (store : Store) -> Unit ) : RecyclerView.Adapter<HomeStoreAdapter.ViewHolder>() {

    val data = ArrayList<Store>()

    inner class ViewHolder(private val binding : ItemHomeStoreBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(store : Store) {
            binding.store = store
            binding.imgStore.setOnClickListener {
                onClick(store)
            }
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding : ItemHomeStoreBinding = DataBindingUtil.inflate(inflater, R.layout.item_home_store, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }
}