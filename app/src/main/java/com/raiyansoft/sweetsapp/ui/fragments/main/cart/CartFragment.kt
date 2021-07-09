package com.raiyansoft.sweetsapp.ui.fragments.main.cart

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.raiyansoft.sweetsapp.R
import com.raiyansoft.sweetsapp.adapters.CartCartAdapter
import com.raiyansoft.sweetsapp.databinding.FragmentCartBinding
import com.raiyansoft.sweetsapp.models.cart.CartResponse
import com.raiyansoft.sweetsapp.ui.viewmodel.cart.CartViewModel

class CartFragment : Fragment() {

    private var binding: FragmentCartBinding? = null
    private lateinit var adapter: CartCartAdapter

    private val viewModel by lazy {
        ViewModelProvider(this)[CartViewModel::class.java]
    }

    private lateinit var cart : CartResponse

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getData()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCartBinding.inflate(layoutInflater)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        doInitialization()
    }

    private fun doInitialization() {
        binding!!.isEmpty = true
        adapter = CartCartAdapter(requireContext()
        ) { it ->
            binding!!.refreshLayout.isRefreshing = true
            viewModel.removeFromCart(it)
            viewModel.dataRemove.observe(viewLifecycleOwner,
                {
                    if (it != null){
                        if (it.status == 200){
                            Snackbar.make(requireView(), it.data, Snackbar.LENGTH_SHORT).show()
                            adapter.data.clear()
                            adapter.notifyDataSetChanged()
                            getData()
                            binding!!.refreshLayout.isRefreshing = false
                        } else {
                            Snackbar.make(requireView(), it.message, Snackbar.LENGTH_SHORT).show()
                        }
                    }
                })
        }
        binding!!.rcCart.layoutManager = LinearLayoutManager(requireContext())
        binding!!.rcCart.adapter = adapter
        binding!!.rcCart.startAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.recyclerview_animation))
        getCart()
        binding!!.refreshLayout.setOnRefreshListener {
            adapter.data.clear()
            adapter.notifyDataSetChanged()
            getData()
        }
        binding!!.tvPay.setOnClickListener {
            val action = CartFragmentDirections.actionCartFragmentToSummaryFragment()
            findNavController().navigate(action)
        }
        binding!!.imgBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun getCart() {
        viewModel.dataCart.observe(viewLifecycleOwner,
            {
                if (it != null) {
                    if (it.status == 200) {
                        binding!!.isEmpty = false
                        cart = it.data
                        binding!!.cart = it.data
                        val total = it.data.total + it.data.total_delivery_fees
                        binding!!.tvTotal.text = "$total"
                        adapter.data.addAll(it.data.items)
                        adapter.notifyDataSetChanged()
                        binding!!.refreshLayout.isRefreshing = false
                    } else {
                        Snackbar.make(requireView(), it.message, Snackbar.LENGTH_SHORT).show()
                        findNavController().navigateUp()
                    }
                }
            })
        binding!!.refreshLayout.isRefreshing = false
    }

    private fun getData() {
        if (binding != null){
            binding!!.refreshLayout.isRefreshing = true
        }
        viewModel.getCart()
    }

}