package com.raiyansoft.sweetsapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.raiyansoft.sweetsapp.R
import com.raiyansoft.sweetsapp.databinding.ItemHomeCategoryBinding
import com.raiyansoft.sweetsapp.models.home.Category

class HomeCategoryAdapter(val onClick: (category: Category) -> Unit ) : RecyclerView.Adapter<HomeCategoryAdapter.ViewHolder>() {

    val data = ArrayList<Category>()

    inner class ViewHolder(private val binding : ItemHomeCategoryBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(category: Category) {
            binding.category = category
            binding.categoryLayout.setOnClickListener {
                onClick(category)
            }
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding : ItemHomeCategoryBinding = DataBindingUtil.inflate(inflater, R.layout.item_home_category, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }
}