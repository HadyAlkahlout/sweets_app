package com.raiyansoft.sweetsapp.ui.fragments.main.filter

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
import com.raiyansoft.sweetsapp.adapters.CategoryProductAdapter
import com.raiyansoft.sweetsapp.databinding.FragmentFilterResultBinding
import com.raiyansoft.sweetsapp.models.product.AddCart
import com.raiyansoft.sweetsapp.ui.dialogs.CartAddBottomSheet
import com.raiyansoft.sweetsapp.ui.viewmodel.filter.FilterViewModel
import com.raiyansoft.sweetsapp.ui.viewmodel.product.ProductViewModel
import com.raiyansoft.sweetsapp.util.Commons
import com.raiyansoft.sweetsapp.util.OnScrollListener
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import java.util.*

class FilterResultFragment : Fragment() {

    private lateinit var binding: FragmentFilterResultBinding
    private lateinit var adapter: CategoryProductAdapter

    private val productViewModel by lazy {
        ViewModelProvider(this)[ProductViewModel::class.java]
    }

    private val viewModel by lazy {
        ViewModelProvider(this)[FilterViewModel::class.java]
    }

    private val token by lazy {
        Commons.getSharedPreferences(requireContext()).getString(
            Commons.SERVER_TOKEN, ""
        )
    }

    private val dialog = CartAddBottomSheet(
        {
            findNavController().navigate(R.id.action_filterResultFragment_to_cartFragment)
        },
        {}
    )

    private var currentPage = 1
    private var totalAvailablePages = 1

    private lateinit var categoryId: IntArray
    private lateinit var occasionId: IntArray
    private lateinit var preparationId: IntArray
    private var offers = ""
    private var add = true

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
        binding = FragmentFilterResultBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        doInitialization()
    }

    private fun doInitialization() {
        if (arguments != null){
            val args = FilterResultFragmentArgs.fromBundle(requireArguments())
            categoryId = args.categoryId
            occasionId = args.occasionId
            preparationId = args.preparationId
            offers = args.offers

            Log.e("TAG", "doInitialization Categories: ${categoryId.contentToString()}")
            Log.e("TAG", "doInitialization occasionId: ${occasionId.contentToString()}")
            Log.e("TAG", "doInitialization preparationId: ${preparationId.contentToString()}")
            Log.e("TAG", "doInitialization offers: $offers")

        }
        binding.imgBack.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.imgRefresh.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.refreshLayout.setOnRefreshListener {
            currentPage = 1
            totalAvailablePages = 1
            adapter.data.clear()
            adapter.notifyDataSetChanged()
        }
        adapter = CategoryProductAdapter(
            { product ->
                // open product page
                val action =
                    FilterResultFragmentDirections.actionFilterResultFragmentToProductFragment()
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
                        Log.e("TAG", "addToCart: ${product.id}")
                        Log.e("TAG", "addToCart: inter add")
                        productViewModel.addToCart(product.id, addCart)

                    }
                }
            },
            { shopID ->
                // open store page
                val action =
                    FilterResultFragmentDirections.actionFilterResultFragmentToShopFragment()
                action.shopId = shopID
                findNavController().navigate(action)
            }
        )
        binding.rcProducts.adapter = adapter
        binding.rcProducts.layoutManager = LinearLayoutManager(requireContext())
        binding.rcProducts.startAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.recyclerview_animation))
        binding.rcProducts.addOnScrollListener(onScrollListener)

        observeCartAdd()
        fillProducts()
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
                    }else{
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
        viewModel.dataFilterProducts.observe(viewLifecycleOwner,
            {
                if (it != null){
                    if (it.status == 200){
                        if (it.data.data.isEmpty() && currentPage == 1){
                            binding.tvEmpty.visibility = View.VISIBLE
                        } else {
                            totalAvailablePages = it.data.paginate.total_pages
                            adapter.data.addAll(it.data.data)
                            adapter.notifyDataSetChanged()
                            currentPage += 1
                        }
                        binding.refreshLayout.isRefreshing = false
                    } else {
                        Snackbar.make(requireView(), it.message, Snackbar.LENGTH_SHORT).show()
                        findNavController().navigateUp()
                    }
                } else {
                    binding.refreshLayout.isRefreshing = false
                    binding.tvEmpty.visibility = View.VISIBLE
                }
            })
    }

    private val onScrollListener = OnScrollListener {
        if (!binding.rcProducts.canScrollVertically(1)) {
            if (currentPage <= totalAvailablePages) {
                getData()
            }
        }
    }

    private fun getData() {
        binding.refreshLayout.isRefreshing = true

        val map: MutableMap<String, RequestBody> = HashMap()

        for ((i, cat) in categoryId.withIndex())  {
            map["category[$i]"] = toRequestBody(cat.toString())
        }

        for ((i, occ) in occasionId.withIndex())  {
            map["occasion[$i]"] = toRequestBody(occ.toString())
        }

        for ((i, prep) in preparationId.withIndex())  {
            map["preparation[$i]"] = toRequestBody(prep.toString())
        }

        map["offers"] = toRequestBody(offers)

        Log.e("TAG", "getData map: $map")

        viewModel.getFilterProducts(currentPage, map)
    }

    fun toRequestBody(value: String): RequestBody {
        return RequestBody.create("text/plain".toMediaTypeOrNull(), value)
    }

}