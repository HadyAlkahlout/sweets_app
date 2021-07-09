package com.raiyansoft.sweetsapp.ui.fragments.main.shop

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
import com.raiyansoft.sweetsapp.adapters.StoreCategoryAdapter
import com.raiyansoft.sweetsapp.adapters.StoreCategoryProductsAdapter
import com.raiyansoft.sweetsapp.databinding.FragmentShopBinding
import com.raiyansoft.sweetsapp.models.home.Store
import com.raiyansoft.sweetsapp.models.product.AddCart
import com.raiyansoft.sweetsapp.ui.dialogs.CartAddBottomSheet
import com.raiyansoft.sweetsapp.ui.viewmodel.product.ProductViewModel
import com.raiyansoft.sweetsapp.ui.viewmodel.store.StoreViewModel
import com.raiyansoft.sweetsapp.util.Commons

class ShopFragment : Fragment() {

    private lateinit var binding: FragmentShopBinding
    private lateinit var categoryAdapter: StoreCategoryAdapter
    private lateinit var productAdapter: StoreCategoryProductsAdapter

    private lateinit var categories: ArrayList<String>

    private val viewModel by lazy {
        ViewModelProvider(this)[StoreViewModel::class.java]
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
            findNavController().navigate(R.id.action_shopFragment_to_cartFragment)
        },
        {

        }
    )

    private var shopId = 0
    private var add = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            val args = ShopFragmentArgs.fromBundle(requireArguments())
            shopId = args.shopId
        }
        getData()
        getStoreData()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentShopBinding.inflate(layoutInflater)
        fillProducts()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        doInitialization()
    }

    private fun doInitialization() {
        observeCartAdd()
        binding.imgBack.setOnClickListener {
            findNavController().navigateUp()
        }
        categories = ArrayList()
        categoryAdapter = StoreCategoryAdapter { position ->
            Log.e("TAG", "doInitialization: $position")
            binding.rcProducts.smoothScrollToPosition(position)
        }
        productAdapter = StoreCategoryProductsAdapter(
            requireContext(),
            { product ->
                // go to product page
                val action = ShopFragmentDirections.actionShopFragmentToProductFragment()
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
            }
        )
        binding.rcCategories.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rcCategories.adapter = categoryAdapter
        binding.rcProducts.adapter = productAdapter
        binding.rcProducts.startAnimation(
            AnimationUtils.loadAnimation(
                requireContext(),
                R.anim.recyclerview_animation
            )
        )
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

    private fun fillCategories() {
        categoryAdapter.data.clear()
        categoryAdapter.data.addAll(categories)
        categoryAdapter.notifyDataSetChanged()
    }

    private fun fillProducts() {
        viewModel.dataStore.observe(viewLifecycleOwner,
            {
                if (it != null) {
                    if (it.data.isNotEmpty()){
                        productAdapter.data.clear()
                        productAdapter.data.addAll(it.data)
                        categories.clear()
                        productAdapter.notifyDataSetChanged()
                        for (i in it.data) {
                            categories.add(i.category_name)
                        }
                        fillCategories()
                    }else {
                        binding.tvEmpty.visibility = View.VISIBLE
                        binding.rcProducts.visibility = View.GONE
                        binding.rcCategories.visibility = View.GONE
                    }
                }else {
                    binding.tvEmpty.visibility = View.VISIBLE
                    binding.rcProducts.visibility = View.GONE
                    binding.rcCategories.visibility = View.GONE
                }
            }
        )
        viewModel.dataStoreData.observe(viewLifecycleOwner,
            {
                if (it != null) {
                    binding.store = it.data.store_data
                }
            }
        )
    }

    private fun getData() {
        viewModel.getStoreProducts(shopId)
    }

    private fun getStoreData() {
        viewModel.getStoreData(shopId)
    }

}