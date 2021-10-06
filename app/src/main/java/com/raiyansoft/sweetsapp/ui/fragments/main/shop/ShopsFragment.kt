package com.raiyansoft.sweetsapp.ui.fragments.main.shop

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.raiyansoft.sweetsapp.R
import com.raiyansoft.sweetsapp.adapters.ShopAdapter
import com.raiyansoft.sweetsapp.databinding.FragmentShopsBinding
import com.raiyansoft.sweetsapp.ui.viewmodel.store.StoreViewModel
import com.raiyansoft.sweetsapp.util.OnScrollListener

class ShopsFragment : Fragment() {

    private var binding: FragmentShopsBinding? = null
    private lateinit var adapter: ShopAdapter
    private var currentPage = 1
    private var totalAvailablePages = 1
    private var loading = false

    private var query = ""

    private val viewModel by lazy {
        ViewModelProvider(this)[StoreViewModel::class.java]
    }

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
        binding = FragmentShopsBinding.inflate(layoutInflater)
        fillShops()
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        doInitialization()
    }

    private fun doInitialization() {
        adapter = ShopAdapter { store ->
            val action = ShopsFragmentDirections.actionShopsFragmentToShopFragment(store)
            findNavController().navigate(action)
        }
        binding!!.rcShops.adapter = adapter
        binding!!.rcShops.addOnScrollListener(onScrollListener)
        binding!!.rcShops.startAnimation(
            AnimationUtils.loadAnimation(
                requireContext(),
                R.anim.recyclerview_animation
            )
        )
        binding!!.edSearch.addTextChangedListener {
            binding!!.imageSearch.visibility = View.GONE
            query = binding!!.edSearch.text.toString().trim()
            adapter.data.clear()
            adapter.notifyDataSetChanged()
            currentPage = 1
            totalAvailablePages = 1
        }
        binding!!.imgBack.setOnClickListener {
            findNavController().navigateUp()
        }
        binding!!.imgFilter.setOnClickListener {
            findNavController().navigate(R.id.action_shopsFragment_to_storesFilterFragment)
        }
        binding!!.swipeLayout.setOnRefreshListener {
            adapter.data.clear()
            adapter.notifyDataSetChanged()
            currentPage = 1
            totalAvailablePages = 1
        }
    }

    private fun fillShops() {
        viewModel.dataStores.observe(viewLifecycleOwner,
            {
                if (it != null) {
                    Log.e("TAG", "fillShops: data ${it.data.data}")
                    currentPage += 1
                    totalAvailablePages = it.data.paginate.total_pages
                    adapter.data.addAll(it.data.data)
                    adapter.notifyDataSetChanged()
                    loading = false
                    binding!!.swipeLayout.isRefreshing = false
                } else {
                    binding!!.swipeLayout.isRefreshing = false
                    binding!!.tvEmpty.visibility = View.VISIBLE
                }
            }
        )
    }

    private fun getData() {
        loading = true
        if (binding != null) {
            binding!!.swipeLayout.isRefreshing = true
        }
        viewModel.getStores(currentPage, query)
    }

    private val onScrollListener = OnScrollListener {
        if (!binding!!.rcShops.canScrollVertically(1)) {
            if (currentPage <= totalAvailablePages) {
                Log.e("TAG", "onScroll: getData")
                getData()
            }
        }
    }

}