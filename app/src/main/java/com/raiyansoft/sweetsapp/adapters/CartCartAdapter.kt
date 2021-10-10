package com.raiyansoft.sweetsapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.raiyansoft.sweetsapp.R
import com.raiyansoft.sweetsapp.databinding.ItemCartCartBinding
import com.raiyansoft.sweetsapp.models.cart.Item
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil

class CartCartAdapter(
    val context: Context,
    val onCartDelete: (id: Int) -> Unit,
    val onChangeQuantity: (id: Int, newGty: Int) -> Unit
) : RecyclerView.Adapter<CartCartAdapter.ViewHolder>() {

    val diffCallback = object : DiffUtil.ItemCallback<Item>() {
        override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }

        override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }
    val differ = AsyncListDiffer(this, diffCallback)

    var data: List<Item>
        get() = differ.currentList
        set(value) = differ.submitList(value)


    inner class ViewHolder(var view: ItemCartCartBinding) :
        RecyclerView.ViewHolder(view.root) {
        fun bind(item: Item) {
            view.item = item
            val adapter = CartProductAdapter(
                { id ->
                    onCartDelete(id)
                },
                { id, newGty ->
                    onChangeQuantity(id, newGty)
                }
            )
            view.rcProducts.layoutManager = LinearLayoutManager(context)
            view.rcProducts.adapter = adapter
            adapter.data = item.products
            view.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemCartCartBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_cart_cart,
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