package com.raiyansoft.sweetsapp.ui.fragments.main.category

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
import com.google.android.material.snackbar.Snackbar
import com.raiyansoft.sweetsapp.R
import com.raiyansoft.sweetsapp.adapters.CategoryProductAdapter
import com.raiyansoft.sweetsapp.databinding.FragmentOccasionBinding
import com.raiyansoft.sweetsapp.models.product.AddCart
import com.raiyansoft.sweetsapp.ui.dialogs.CartAddBottomSheet
import com.raiyansoft.sweetsapp.ui.viewmodel.occasion.OccasionViewModel
import com.raiyansoft.sweetsapp.ui.viewmodel.product.ProductViewModel
import com.raiyansoft.sweetsapp.util.Commons
import com.raiyansoft.sweetsapp.util.OnScrollListener

class OccasionFragment : Fragment() {

    private var binding: FragmentOccasionBinding? = null
    private lateinit var adapter: CategoryProductAdapter
    private var currentPage = 1
    private var totalAvailablePages = 1
    private var loading = false

    private val occasionViewModel by lazy {
        ViewModelProvider(this)[OccasionViewModel::class.java]
    }

    private val productViewModel by lazy {
        ViewModelProvider(this)[ProductViewModel::class.java]
    }

    private val token by lazy {
        Commons.getSharedPreferences(requireContext()).getString(
            Commons.SERVER_TOKEN, ""
        )
    }

    private val dialog = CartAddBottomSheet(
        {
            findNavController().navigate(R.id.action_occasionFragment_to_cartFragment)
        },
        {}
    )

    private var catId = 0

    private var query = ""
    private var add = true

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
        binding = FragmentOccasionBinding.inflate(layoutInflater)
        fillProducts()
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        doInitialization()
    }

    private fun doInitialization() {
        observeCartAdd()
        adapter = CategoryProductAdapter(
            { product ->
                // open product page
                val action = OccasionFragmentDirections.actionOccasionFragmentToProductFragment()
                action.productId = product.id
                findNavController().navigate(action)
            },
            { product ->
                // add to cart
                if (token == "") {
                    Commons.showLoginDialog(requireActivity())
                } else {
                    if (add) {
                        add = false
                        val addCart = AddCart()
                        productViewModel.addToCart(product.id, addCart)

                    }
                }
            },
            { shopID ->
                // open store page
//                val action = CategoryFragmentDirections.actionCategoryFragmentToShopFragment(null)
//                findNavController().navigate(action)
            }
        )
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
        binding!!.imgFilter.setOnClickListener {
            findNavController().navigate(R.id.action_occasionFragment_to_storesFilterFragment)
        }
        binding!!.edSearch.addTextChangedListener {
            query = binding!!.edSearch.text.toString().trim()
            adapter.data.clear()
            adapter.notifyDataSetChanged()
            currentPage = 1
            totalAvailablePages = 1
        }
    }

    private fun observeCartAdd() {
        productViewModel.dataAdd.observe(viewLifecycleOwner,
            {
                if (it.status == 200) {
                    if (!dialog.isAdded) {
                        Log.e("TAG", "doInitialization: not added")
                        dialog.show(
                            requireActivity().supportFragmentManager,
                            "Cart Dialog"
                        )
                    } else {
                        Log.e("TAG", "doInitialization: added")
                    }
                } else {
                    Snackbar.make(requireView(), it.message, Snackbar.LENGTH_SHORT)
                        .show()
                }
                add = true
            })
    }

    private fun fillProducts() {
        occasionViewModel.dataOccasion.observe(viewLifecycleOwner,
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
        occasionViewModel.getOccasionProducts(catId, currentPage, query)

    }

    private val onScrollListener = OnScrollListener {
        if (!binding!!.rcCategoryProducts.canScrollVertically(1)) {
            if (currentPage <= totalAvailablePages) {
                getData()
            }
        }
    }

}