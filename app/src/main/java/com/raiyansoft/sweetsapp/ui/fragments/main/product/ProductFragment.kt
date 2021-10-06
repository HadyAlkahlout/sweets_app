package com.raiyansoft.sweetsapp.ui.fragments.main.product

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.snackbar.Snackbar
import com.raiyansoft.sweetsapp.R
import com.raiyansoft.sweetsapp.adapters.HomeProductAdapter
import com.raiyansoft.sweetsapp.adapters.OptionsMultiAdapter
import com.raiyansoft.sweetsapp.adapters.OptionsOnceAdapter
import com.raiyansoft.sweetsapp.adapters.ProductGalleryAdapter
import com.raiyansoft.sweetsapp.databinding.FragmentProductBinding
import com.raiyansoft.sweetsapp.models.cart.PassOption
import com.raiyansoft.sweetsapp.models.product.AddCart
import com.raiyansoft.sweetsapp.models.product.Fav
import com.raiyansoft.sweetsapp.ui.dialogs.CartAddBottomSheet
import com.raiyansoft.sweetsapp.ui.viewmodel.product.ProductViewModel
import com.raiyansoft.sweetsapp.util.Commons
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody

class ProductFragment : Fragment() {

    private lateinit var binding: FragmentProductBinding
    private lateinit var adapter: HomeProductAdapter
    private lateinit var bannerAdapter: ProductGalleryAdapter
    private lateinit var optionsOnceAdapter: OptionsOnceAdapter
    private lateinit var optionsMultiAdapter: OptionsMultiAdapter

    private val viewModel by lazy {
        ViewModelProvider(this)[ProductViewModel::class.java]
    }

    private val dialog = CartAddBottomSheet(
        {
            findNavController().navigate(R.id.action_productFragment_to_cartFragment)
        },
        {
            findNavController().navigateUp()
        }
    )

    private var token = ""

    private var productId = 0
    private var fav = true
    private var qut = 1

    private var dedicateMessage = ""
    private var dedicatePrice = 0.0
    private var total = 0.0
    private var add = true
    private var favorite = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProductBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        doInitialization()
    }

    private fun doInitialization() {
        if (arguments != null) {
            val args = ProductFragmentArgs.fromBundle(requireArguments())
            productId = args.productId
            Log.e("TAG", "doInitialization: $productId")
        }
        token = Commons.getSharedPreferences(requireContext()).getString(
            Commons.SERVER_TOKEN, ""
        )!!
        setupBanner()
        fillProductData(productId)
        observeData()
        binding.imgBack.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.tvAddCart.setOnClickListener {
            if (binding.cbWrite.isChecked) {
                if (binding.edMessage.text.isEmpty()) {
                    binding.edMessage.background = ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.edittext_error_background
                    )
                } else {
                    addToCart(productId)
                }
            } else {
                addToCart(productId)
            }
        }
        binding.imgFav.setOnClickListener {
            if (fav) {
                fav = false
                addToFav()
            } else {
                fav = true
                addToFav()
            }
        }

        observeFav()
        observeCartAdd()

        binding.tvIncrease.setOnClickListener {
            ++qut
            binding.tvQuantity.text = "$qut"
        }
        binding.tvReduce.setOnClickListener {
            if (qut > 1) {
                --qut
                binding.tvQuantity.text = "$qut"
            }
        }

        adapter = HomeProductAdapter { product ->
            adapter.data.clear()
            adapter.notifyDataSetChanged()
            bannerAdapter.data.clear()
            bannerAdapter.notifyDataSetChanged()
            binding.indicatorsContainer.removeAllViews()
            fillProductData(product.id)
        }
        binding.rcSimilarProducts.adapter = adapter
        binding.rcSimilarProducts.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        optionsOnceAdapter = OptionsOnceAdapter { isSelected, price ->
            if (isSelected) {
                binding.tvPrice.text =
                    (binding.tvPrice.text.toString().toDouble() + price).toString()
            } else {
                binding.tvPrice.text =
                    (binding.tvPrice.text.toString().toDouble() - price).toString()
            }
        }
        binding.rcOnceOptions.adapter = optionsOnceAdapter

        optionsMultiAdapter = OptionsMultiAdapter { isSelected, price ->
            if (isSelected) {
                binding.tvPrice.text =
                    (binding.tvPrice.text.toString().toDouble() + price).toString()
            } else {
                binding.tvPrice.text =
                    (binding.tvPrice.text.toString().toDouble() - price).toString()
            }
        }
        binding.rcMultiOptions.adapter = optionsMultiAdapter

        binding.cbWrite.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.edMessage.visibility = View.VISIBLE
                binding.tvIncrease.visibility = View.GONE
                binding.tvReduce.visibility = View.GONE
                qut = 1
                binding.tvQuantity.text = "$qut"
                val price = total + dedicatePrice
                binding.tvPrice.text = "$price"
            } else {
                binding.edMessage.visibility = View.GONE
                binding.tvIncrease.visibility = View.VISIBLE
                binding.tvReduce.visibility = View.VISIBLE
                binding.tvPrice.text = "$total"
            }
        }
    }

    private fun addToFav() {
        if (token == "") {
            Commons.showLoginDialog(requireActivity())
        } else {
            if (favorite) {
                favorite = false
                val id = Fav(productId)
                viewModel.setFav(id)
            }
        }
    }

    private fun observeFav() {
        viewModel.dataFav.observe(viewLifecycleOwner,
            { response ->
                if (response != null) {
                    if (response.status == 200) {
                        if (fav) {
                            binding.imgFav.setImageDrawable(
                                ContextCompat.getDrawable(
                                    requireContext(),
                                    R.drawable.ic_fav_active
                                )
                            )
                        } else {
                            binding.imgFav.setImageDrawable(
                                ContextCompat.getDrawable(
                                    requireContext(),
                                    R.drawable.ic_fav_non
                                )
                            )
                        }
                    } else {
                        Snackbar.make(requireView(), response.message, Snackbar.LENGTH_SHORT).show()
                    }
                }
                favorite = true
            })
    }

    private fun addToCart(id: Int) {
        // add item to cart
        if (token == "") {
            Commons.showLoginDialog(requireActivity())
        } else {
            if (add) {
                add = false
                if (binding.layoutDedicate.isVisible) {
                    dedicateMessage = binding.edMessage.text.toString().trim()
                    dedicatePrice = binding.tvGift.text.toString().toDouble()
                }
                val additions = ArrayList<PassOption>()
                var passOption = PassOption()
                optionsOnceAdapter.data.forEach { option ->
                    if (option.isSelected){
                        passOption = PassOption(
                            option.id,
                            option.price.toDouble()
                        )
                        additions.add(passOption)
                    }
                }
                optionsMultiAdapter.data.forEach { option ->
                    if (option.isSelected){
                        passOption = PassOption(
                            option.id,
                            option.price.toDouble()
                        )
                        additions.add(passOption)
                    }
                }
                val addCart = AddCart(dedicateMessage, dedicatePrice, qut, additions)
                viewModel.addToCart(id, addCart)

            }
        }
    }

    private fun observeCartAdd() {
        viewModel.dataAdd.observe(viewLifecycleOwner,
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

    private fun fillProductData(id: Int) {
        if (token == "") {
            Log.e("TAG", "fillProductData: without token")
            viewModel.getProduct(id)
        } else {
            Log.e("TAG", "fillProductData: with token")
            viewModel.getProduct(token, id)
        }
    }

    private fun observeData() {
        viewModel.dataProduct.observe(viewLifecycleOwner,
            { response ->
                if (response != null) {
                    if (response.status == 200) {
                        binding.product = response.data
                        bannerAdapter.data.addAll(response.data.gallery)
                        bannerAdapter.notifyDataSetChanged()

                        if (response.data.additions != null){
                            if (response.data.additions.once.isEmpty()){
                                binding.llOnce.visibility = View.GONE
                                binding.viewAdditionsDev.visibility = View.GONE
                            }
                            if (response.data.additions.multiple.isEmpty()){
                                binding.llMulti.visibility = View.GONE
                                binding.viewAdditionsDev.visibility = View.GONE
                            }
                            optionsOnceAdapter.data.addAll(response.data.additions.once)
                            optionsOnceAdapter.notifyDataSetChanged()
                            optionsMultiAdapter.data.addAll(response.data.additions.multiple)
                            optionsMultiAdapter.notifyDataSetChanged()
                        } else {
                            binding.llAdditions.visibility = View.GONE
                        }

                        total = response.data.price

                        setupIndicators()
                        setCurrentIndicator(0)
                        Log.e("TAG", "HomeProductAdapterHomeProductAdapter: ${adapter.data}")
                        Log.e("TAG", "HomeProductAdapterHomeProductAdapter: ${bannerAdapter.data}")
                        if (response.data.favourite == 1) {
                            binding.imgFav.setImageDrawable(
                                ContextCompat.getDrawable(
                                    requireContext(),
                                    R.drawable.ic_fav_active
                                )
                            )
                            fav = true
                        } else {
                            binding.imgFav.setImageDrawable(
                                ContextCompat.getDrawable(
                                    requireContext(),
                                    R.drawable.ic_fav_non
                                )
                            )
                            fav = false
                        }
                        if (response.data.have_dedicate == 0) {
                            binding.layoutDedicate.visibility = View.GONE
                        } else {
                            binding.tvGift.text = response.data.dedicate_price
                            dedicatePrice = response.data.dedicate_price.toDouble()
                        }
                        if (response.data.offer < 1) {
                            binding.tvOff.visibility = View.GONE
                        } else {
                            binding.tvOff.visibility = View.VISIBLE
                            binding.tvOff.text = "${response.data.offer}% OFF"
                        }
                        adapter.data.addAll(response.data.similier_products)
                        adapter.notifyDataSetChanged()
                    } else {
                        Snackbar.make(requireView(), response.message, Snackbar.LENGTH_SHORT).show()
                        findNavController().navigateUp()
                    }
                }
            })
    }

    private fun setupBanner() {
        setBannerItems()
    }

    private fun setBannerItems() {
        bannerAdapter = ProductGalleryAdapter()
        binding.banner.adapter = bannerAdapter
        binding.banner.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                setCurrentIndicator(position)
            }
        })
        (binding.banner.getChildAt(0) as RecyclerView).overScrollMode =
            RecyclerView.OVER_SCROLL_NEVER
    }

    private fun setupIndicators() {
        val indicators = arrayOfNulls<ImageView>(bannerAdapter.itemCount)
        val layoutParams: LinearLayout.LayoutParams =
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        layoutParams.setMargins(8, 0, 8, 0)
        for (i in indicators.indices) {
            indicators[i] = ImageView(requireContext())
            indicators[i]?.let {
                it.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.background_inactive_indicator
                    )
                )
                it.layoutParams = layoutParams
                binding.indicatorsContainer.addView(it)
            }
        }
    }

    private fun setCurrentIndicator(position: Int) {
        val childCount = binding.indicatorsContainer.childCount
        for (i in 0 until childCount) {
            val imageView = binding.indicatorsContainer.getChildAt(i) as ImageView
            if (i == position) {
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.background_active_indicator
                    )
                )
            } else {
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.background_inactive_indicator
                    )
                )
            }
        }
    }

}