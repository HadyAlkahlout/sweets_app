package com.raiyansoft.sweetsapp.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.raiyansoft.sweetsapp.R
import com.raiyansoft.sweetsapp.databinding.ItemStoreCategoryBinding

class StoreCategoryAdapter(val onClick: (position : Int) -> Unit) :
    RecyclerView.Adapter<StoreCategoryAdapter.ViewHolder>() {

    var data = ArrayList<String>()

    inner class ViewHolder(var view: ItemStoreCategoryBinding) :
        RecyclerView.ViewHolder(view.root) {
        fun bind(category: String) {
            view.category = category
            view.root.setOnClickListener {
                Log.e("TAG", "bind: click tv")
                onClick(data.indexOf(category))
            }
            view.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemStoreCategoryBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_store_category,
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