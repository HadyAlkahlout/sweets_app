package com.raiyansoft.sweetsapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.raiyansoft.sweetsapp.R
import com.raiyansoft.sweetsapp.databinding.ItemDialogRegionBinding
import com.raiyansoft.sweetsapp.models.cities.Region

class RegionAdapter(val onClick : (region: Region) -> Unit) : RecyclerView.Adapter<RegionAdapter.ViewHolder>() {

    val data = ArrayList<Region>()

    inner class ViewHolder(private val binding : ItemDialogRegionBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(region : Region) {
            binding.region = region
            binding.tvRegion.setOnClickListener {
                onClick(region)
            }
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding : ItemDialogRegionBinding = DataBindingUtil.inflate(inflater, R.layout.item_dialog_region, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }
}