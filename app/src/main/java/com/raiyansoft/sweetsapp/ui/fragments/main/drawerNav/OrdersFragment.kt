package com.raiyansoft.sweetsapp.ui.fragments.main.drawerNav

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.raiyansoft.sweetsapp.R
import com.raiyansoft.sweetsapp.adapters.OrdersAdapter
import com.raiyansoft.sweetsapp.databinding.FragmentOrdersBinding
import com.raiyansoft.sweetsapp.ui.viewmodel.order.OrderViewModel
import com.raiyansoft.sweetsapp.util.OnScrollListener

class OrdersFragment : Fragment() {

    private var binding: FragmentOrdersBinding? = null
    private lateinit var adapter: OrdersAdapter
    private var type = 1

    private val viewModel by lazy {
        ViewModelProvider(this)[OrderViewModel::class.java]
    }
    private var currentPage = 1
    private var totalAvailablePages = 1
    private var loading = false

    override fun onResume() {
        super.onResume()
        adapter.data.clear()
        adapter.notifyDataSetChanged()
        currentPage = 1
        totalAvailablePages = 1
        getData()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOrdersBinding.inflate(layoutInflater)
        getOrders()
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        doInitialization()
    }

    private fun doInitialization() {

        binding!!.imgBack.setOnClickListener{
            findNavController().navigateUp()
        }

        binding!!.tvCurrent.setOnClickListener {
            if (type == 2){
                type = 1
                binding!!.tvPrevious.background = ContextCompat.getDrawable(requireContext(), R.drawable.background_simple_button)
                binding!!.tvPrevious.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorTextPrimary))
                binding!!.tvCurrent.background = ContextCompat.getDrawable(requireContext(), R.drawable.background_button)
                binding!!.tvCurrent.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                adapter.data.clear()
                adapter.notifyDataSetChanged()
                currentPage = 1
                totalAvailablePages = 1
            }
        }

        binding!!.tvPrevious.setOnClickListener {
            if (type == 1){
                type = 2
                binding!!.tvCurrent.background = ContextCompat.getDrawable(requireContext(), R.drawable.background_simple_button)
                binding!!.tvCurrent.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorTextPrimary))
                binding!!.tvPrevious.background = ContextCompat.getDrawable(requireContext(), R.drawable.background_button)
                binding!!.tvPrevious.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                adapter.data.clear()
                adapter.notifyDataSetChanged()
                currentPage = 1
                totalAvailablePages = 1
            }
        }

        adapter = OrdersAdapter(requireActivity()) { order ->
            val action = OrdersFragmentDirections.actionOrdersFragmentToOrderFragment()
            action.orderId = order.id
            action.open = 0
            findNavController().navigate(action)
        }

        binding!!.rcOrders.adapter = adapter
        binding!!.rcOrders.layoutManager = LinearLayoutManager(requireContext())
        binding!!.rcOrders.startAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.recyclerview_animation))
        binding!!.rcOrders.addOnScrollListener(onScrollListener)

        binding!!.swipeLayout.setOnRefreshListener {
            adapter.data.clear()
            adapter.notifyDataSetChanged()
            currentPage = 1
            totalAvailablePages = 1
        }
    }

    private fun getOrders() {
        viewModel.dataOrders.observe(viewLifecycleOwner,
            {
                if (it != null) {
                    totalAvailablePages = it.data.paginate.total_pages
                    adapter.data.addAll(it.data.data)
                    adapter.notifyDataSetChanged()
                    loading = false
                    binding!!.swipeLayout.isRefreshing = false
                    currentPage += 1
                }
            }
        )
    }

    private fun getData() {
        loading = true
        if (binding != null){
            binding!!.swipeLayout.isRefreshing = true
        }
        var typeName = ""
        if (type == 1){
            typeName = "current"
        } else {
            typeName = "prev"
        }
        viewModel.getOrders(currentPage, typeName)
    }

    private val onScrollListener = OnScrollListener {
        if (!binding!!.rcOrders.canScrollVertically(1)) {
            if (currentPage <= totalAvailablePages) {
                getData()
            }
        }
    }

}