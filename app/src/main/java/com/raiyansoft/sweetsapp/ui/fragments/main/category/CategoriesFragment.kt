package com.raiyansoft.sweetsapp.ui.fragments.main.category

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.raiyansoft.sweetsapp.R
import com.raiyansoft.sweetsapp.adapters.CategoryAdapter
import com.raiyansoft.sweetsapp.databinding.FragmentCategoriesBinding
import com.raiyansoft.sweetsapp.ui.viewmodel.category.CategoryViewModel
import com.raiyansoft.sweetsapp.ui.viewmodel.occasion.OccasionViewModel
import com.raiyansoft.sweetsapp.util.OnScrollListener

class CategoriesFragment : Fragment() {

    private var binding: FragmentCategoriesBinding? = null
    private lateinit var adapter: CategoryAdapter
    private var currentPage = 1
    private var totalAvailablePages = 1
    private var loading = false

    private val viewModel by lazy {
        ViewModelProvider(this)[CategoryViewModel::class.java]
    }

    private val occasionViewModel by lazy {
        ViewModelProvider(this)[OccasionViewModel::class.java]
    }

    private var catType = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            val args = CategoriesFragmentArgs.fromBundle(requireArguments())
            catType = args.categoryType
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
        binding = FragmentCategoriesBinding.inflate(layoutInflater)
        doInitialization()
        return binding!!.root
    }

    private fun doInitialization() {
        binding!!.imgBack.setOnClickListener {
            findNavController().navigateUp()
        }
        customizeContent()
        fillCategories()
        adapter = CategoryAdapter { category ->

            if (catType == 1) {
                val action =
                    CategoriesFragmentDirections.actionCategoriesFragmentToCategoryFragment()
                action.categoryId = category.id
                findNavController().navigate(action)
            } else {
                val action =
                    CategoriesFragmentDirections.actionCategoriesFragmentToOccasionFragment()
                action.categoryId = category.id
                findNavController().navigate(action)
            }

        }

        binding!!.rcCategories.adapter = adapter
        binding!!.rcCategories.startAnimation(
            AnimationUtils.loadAnimation(
                requireContext(),
                R.anim.recyclerview_animation
            )
        )
        binding!!.rcCategories.addOnScrollListener(onScrollListener)
        binding!!.swipeLayout.setOnRefreshListener {
            adapter.data.clear()
            adapter.notifyDataSetChanged()
            currentPage = 1
            totalAvailablePages = 1
        }
    }

    private fun fillCategories() {
        if (catType == 0) {
            occasionViewModel.dataOccasions.observe(viewLifecycleOwner,
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
        } else {
            viewModel.dataCategories.observe(viewLifecycleOwner,
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
        binding!!.swipeLayout.isRefreshing = false
    }

    private fun getData() {
        loading = true
        if (binding != null) {
            binding!!.swipeLayout.isRefreshing = true
        }
        if (catType == 0) {
            occasionViewModel.getOccasions(currentPage)
        } else {
            viewModel.getCategories(currentPage)
        }
    }

    private val onScrollListener = OnScrollListener {
        if (!binding!!.rcCategories.canScrollVertically(1)) {
            if (currentPage <= totalAvailablePages) {
                getData()
            }
        }
    }

    private fun customizeContent() {
        if (catType == 0) {
            binding!!.tvTitle.text = getString(R.string.occasion_cat)
        } else {
            binding!!.tvTitle.text = getString(R.string.department_cat)
        }
    }

}