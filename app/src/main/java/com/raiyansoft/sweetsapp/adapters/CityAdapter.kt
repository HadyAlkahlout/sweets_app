package com.raiyansoft.sweetsapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.raiyansoft.sweetsapp.R
import com.raiyansoft.sweetsapp.databinding.ItemCityBinding
import com.raiyansoft.sweetsapp.models.cities.City
import com.raiyansoft.sweetsapp.models.cities.Region

class CityAdapter(val context: Context, val onClick : (region: Region) -> Unit) : RecyclerView.Adapter<CityAdapter.ViewHolder>() {

    val data = ArrayList<City>()

    inner class ViewHolder(private val binding : ItemCityBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(city : City) {
            binding.city = city
            val adapter = RegionAdapter{
                onClick(it)
            }
            binding.rcRegions.layoutManager = LinearLayoutManager(context)
            binding.rcRegions.adapter = adapter
            adapter.data.addAll(city.regions)
            adapter.notifyDataSetChanged()
            binding.tvTitle.setOnClickListener {
                if (binding.rcRegions.isVisible){
                    binding.rcRegions.visibility = View.GONE
                } else {
                    binding.rcRegions.visibility = View.VISIBLE
                }
            }
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding : ItemCityBinding = DataBindingUtil.inflate(inflater, R.layout.item_city, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }
}