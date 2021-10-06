package com.raiyansoft.sweetsapp.ui.fragments.main.category

import android.os.Bundle
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
import com.raiyansoft.sweetsapp.databinding.FragmentCategoryBinding
import com.raiyansoft.sweetsapp.ui.viewmodel.category.CategoryViewModel
import com.raiyansoft.sweetsapp.util.OnScrollListener

class CategoryFragment : Fragment() {

    private var binding: FragmentCategoryBinding? = null
    private lateinit var adapter: ShopAdapter
    private var currentPage = 1
    private var totalAvailablePages = 1
    private var loading = false

    private val viewModel by lazy {
        ViewModelProvider(this)[CategoryViewModel::class.java]
    }

    private var catId = 0

    private var query = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            val args = CategoryFragmentArgs.fromBundle(requireArguments())
            catId = args.categoryId
        } else {
            findNavController().navigateUp()
        }
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
        binding = FragmentCategoryBinding.inflate(layoutInflater)
        fillProducts()
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        doInitialization()
    }

    private fun doInitialization() {
        adapter = ShopAdapter { store ->
            val action = CategoryFragmentDirections.actionCategoryFragmentToShopFragment(store)
            findNavController().navigate(action)
        }
        binding!!.rcCategoryProducts.adapter = adapter
        binding!!.rcCategoryProducts.addOnScrollListener(onScrollListener)
        binding!!.rcCategoryProducts.startAnimation(
            AnimationUtils.loadAnimation(
                requireContext(),
                R.anim.recyclerview_animation
            )
        )
        binding!!.swipeLayout.setOnRefreshListener {
            adapter.data.clear()
            adapter.notifyDataSetChanged()
            currentPage = 1
            totalAvailablePages = 1
        }
        binding!!.imgBack.setOnClickListener {
            findNavController().navigateUp()
        }
        binding!!.edSearch.addTextChangedListener {
            query = binding!!.edSearch.text.toString().trim()
            adapter.data.clear()
            adapter.notifyDataSetChanged()
            currentPage = 1
            totalAvailablePages = 1
        }
    }

    private fun fillProducts() {
        viewModel.dataCategory.observe(viewLifecycleOwner,
            {
                if (it != null) {
                    if (it.data.data.isEmpty() && currentPage == 1) {
                        binding!!.tvEmpty.visibility = View.VISIBLE
                    } else {
                        totalAvailablePages = it.data.paginate.total_pages
                        adapter.data.addAll(it.data.data)
                        adapter.notifyDataSetChanged()
                        currentPage += 1
                    }
                    loading = false
                    binding!!.swipeLayout.isRefreshing = false
                } else {
                    binding!!.tvEmpty.visibility = View.VISIBLE
                }
            }
        )
        binding!!.swipeLayout.isRefreshing = false
    }

    private fun getData() {
        loading = true
        if (binding != null) {
            binding!!.swipeLayout.isRefreshing = true
        }
        viewModel.getCategoryProducts(catId, currentPage, query)
    }

    private val onScrollListener = OnScrollListener {
        if (!binding!!.rcCategoryProducts.canScrollVertically(1)) {
            if (currentPage <= totalAvailablePages) {
                getData()
            }
        }
    }

}