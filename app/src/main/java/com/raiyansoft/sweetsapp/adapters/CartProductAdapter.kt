package com.raiyansoft.sweetsapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.raiyansoft.sweetsapp.R
import com.raiyansoft.sweetsapp.databinding.ItemCartProductBinding
import com.raiyansoft.sweetsapp.models.cart.Product

class CartProductAdapter(
    val onCartDelete: (id: Int) -> Unit,
    val onChangeQuantity: (id: Int, newGty: Int) -> Unit
) :
    RecyclerView.Adapter<CartProductAdapter.ViewHolder>() {


    val diffCallback = object : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }
    val differ = AsyncListDiffer(this, diffCallback)

    var data: List<Product>
        get() = differ.currentList
        set(value) = differ.submitList(value)

    inner class ViewHolder(private val binding: ItemCartProductBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(product: Product) {
            binding.product = product
            binding.imgCartRemove.setOnClickListener {
                if (product.qty > 1) {
                    onChangeQuantity(product.id, (product.qty - 1))
                } else {
                    onCartDelete(product.id)
                }
            }
            binding.imgCartAdd.setOnClickListener {
                onChangeQuantity(product.id, (product.qty + 1))
            }

            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding: ItemCartProductBinding =
            DataBindingUtil.inflate(inflater, R.layout.item_cart_product, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }
}