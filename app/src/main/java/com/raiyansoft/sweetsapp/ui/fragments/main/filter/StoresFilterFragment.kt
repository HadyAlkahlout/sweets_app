package com.raiyansoft.sweetsapp.ui.fragments.main.filter

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.raiyansoft.sweetsapp.R
import com.raiyansoft.sweetsapp.adapters.FilterAdapter
import com.raiyansoft.sweetsapp.databinding.FragmentStoresFilterBinding
import com.raiyansoft.sweetsapp.ui.viewmodel.filter.FilterViewModel

class StoresFilterFragment : Fragment() {

    private lateinit var binding: FragmentStoresFilterBinding
    private lateinit var categoryAdapter: FilterAdapter
    private lateinit var occasionAdapter: FilterAdapter
    private lateinit var timeAdapter: FilterAdapter

    private val viewModel by lazy {
        ViewModelProvider(this)[FilterViewModel::class.java]
    }

    private var offers = ""
    private val category = ArrayList<Int>()
    private val occasion = ArrayList<Int>()
    private val preparation = ArrayList<Int>()

    private var categoryId : IntArray? = null
    private var occasionId : IntArray? = null
    private var preparationId : IntArray? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getData()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStoresFilterBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        doInitialization()
    }

    override fun onResume() {
        super.onResume()
        offers = ""
        category.clear()
        occasion.clear()
        preparation.clear()
        categoryId = null
        occasionId = null
        preparationId = null
        for (i in categoryAdapter.data) {
            i.selected = false
        }
        for (i in occasionAdapter.data) {
            i.selected = false
        }
        for (i in timeAdapter.data) {
            i.selected = false
        }
    }

    private fun doInitialization() {
        binding.imgBack.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.tvDone.setOnClickListener {
            sendFilter()
        }

        categoryAdapter = FilterAdapter()
        binding.rcCategory.layoutManager = LinearLayoutManager(requireContext())
        binding.rcCategory.adapter = categoryAdapter
        occasionAdapter = FilterAdapter()
        binding.rcOccasion.layoutManager = LinearLayoutManager(requireContext())
        binding.rcOccasion.adapter = occasionAdapter
        timeAdapter = FilterAdapter()
        binding.rcTime.layoutManager = LinearLayoutManager(requireContext())
        binding.rcTime.adapter = timeAdapter

        binding.cardCategory.setOnClickListener {
            if (binding.rcCategory.isVisible) {
                binding.rcCategory.visibility = View.GONE
            } else {
                binding.rcCategory.visibility = View.VISIBLE
            }
        }
        binding.cardOccasion.setOnClickListener {
            if (binding.rcOccasion.isVisible) {
                binding.rcOccasion.visibility = View.GONE
            } else {
                binding.rcOccasion.visibility = View.VISIBLE
            }
        }
        binding.cardTime.setOnClickListener {
            if (binding.rcTime.isVisible) {
                binding.rcTime.visibility = View.GONE
            } else {
                binding.rcTime.visibility = View.VISIBLE
            }
        }
        binding.cardOffers.setOnClickListener {
            if (binding.rbProductsType.isVisible) {
                binding.rbProductsType.visibility = View.GONE
            } else {
                binding.rbProductsType.visibility = View.VISIBLE
            }
        }

        fillFilterData()
    }

    private fun fillFilterData() {
        viewModel.dataFilters.observe(viewLifecycleOwner,
            {
                if (it != null) {
                    if (it.status == 200) {
                        categoryAdapter.data.addAll(it.data.categories)
                        categoryAdapter.notifyDataSetChanged()
                        occasionAdapter.data.addAll(it.data.occasions)
                        occasionAdapter.notifyDataSetChanged()
                        timeAdapter.data.addAll(it.data.preparations)
                        timeAdapter.notifyDataSetChanged()
                    } else {
                        Snackbar.make(requireView(), it.message, Snackbar.LENGTH_SHORT).show()
                        findNavController().navigateUp()
                    }
                }
            })
    }

    private fun sendFilter() {
        if (binding.rbOffer.isChecked) {
            offers = "products_with_offers"
        }
        for (i in categoryAdapter.data) {
            if (i.selected) {
                category.add(i.id)
            }
        }
        for (i in occasionAdapter.data) {
            if (i.selected) {
                occasion.add(i.id)
            }
        }
        for (i in timeAdapter.data) {
            if (i.selected) {
                preparation.add(i.id)
            }
        }

        categoryId = IntArray(category.size)
        occasionId = IntArray(occasion.size)
        preparationId = IntArray(preparation.size)
        for ((i, v) in category.withIndex()) {
            categoryId!![i] = v
        }
        for ((i, v) in occasion.withIndex()) {
            occasionId!![i] = v
        }
        for ((i, v) in preparation.withIndex()) {
            preparationId!![i] = v
        }

        val action =
            StoresFilterFragmentDirections.actionStoresFilterFragmentToFilterResultFragment(
                categoryId!!,
                occasionId!!,
                preparationId!!
            )
        action.offers = offers
        findNavController().navigate(action)
    }

    private fun getData() {
        viewModel.getFilters()
    }

}