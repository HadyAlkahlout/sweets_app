package com.raiyansoft.sweetsapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.raiyansoft.sweetsapp.R
import com.raiyansoft.sweetsapp.databinding.ItemDialogAddressBinding
import com.raiyansoft.sweetsapp.models.address.UpdateAddressResponse

class MyAddressesAdapter(val onClick : (address: UpdateAddressResponse) -> Unit) : RecyclerView.Adapter<MyAddressesAdapter.ViewHolder>() {

    val data = ArrayList<UpdateAddressResponse>()

    inner class ViewHolder(private val binding : ItemDialogAddressBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(address : UpdateAddressResponse) {
            binding.address = address
            binding.tvAddress.setOnClickListener {
                onClick(address)
            }
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding : ItemDialogAddressBinding = DataBindingUtil.inflate(inflater, R.layout.item_dialog_address, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }
}