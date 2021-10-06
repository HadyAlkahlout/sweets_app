package com.raiyansoft.sweetsapp.ui.fragments.main.home

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.raiyansoft.sweetsapp.R
import com.raiyansoft.sweetsapp.adapters.HomeBannerAdapter
import com.raiyansoft.sweetsapp.adapters.HomeCategoryAdapter
import com.raiyansoft.sweetsapp.adapters.HomeProductAdapter
import com.raiyansoft.sweetsapp.adapters.HomeStoreAdapter
import com.raiyansoft.sweetsapp.databinding.FragmentHomeBinding
import com.raiyansoft.sweetsapp.models.address.Location
import com.raiyansoft.sweetsapp.models.token.DeleteToken
import com.raiyansoft.sweetsapp.ui.activities.AuthActivity
import com.raiyansoft.sweetsapp.ui.activities.MainActivity
import com.raiyansoft.sweetsapp.ui.dialogs.MyAddressesDialog
import com.raiyansoft.sweetsapp.ui.viewmodel.auth.AuthViewModel
import com.raiyansoft.sweetsapp.ui.viewmodel.auth.TokenViewModel
import com.raiyansoft.sweetsapp.ui.viewmodel.cart.CartViewModel
import com.raiyansoft.sweetsapp.ui.viewmodel.home.HomeViewModel
import com.raiyansoft.sweetsapp.util.Commons

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var storeAdapter: HomeStoreAdapter
    private lateinit var bannerAdapter: HomeBannerAdapter
    private lateinit var occasionAdapter: HomeCategoryAdapter
    private lateinit var departmentAdapter: HomeCategoryAdapter
    private lateinit var newAdapter: HomeProductAdapter
    private lateinit var trendAdapter: HomeProductAdapter

    private val viewModel by lazy {
        ViewModelProvider(this)[HomeViewModel::class.java]
    }

    private val authViewModel by lazy {
        ViewModelProvider(this)[AuthViewModel::class.java]
    }

    private val tokenViewModel by lazy {
        ViewModelProvider(this)[TokenViewModel::class.java]
    }

    private val cartViewModel by lazy {
        ViewModelProvider(this)[CartViewModel::class.java]
    }

    private val locationViewModel by lazy {
        ViewModelProvider(this)[AuthViewModel::class.java]
    }

    private var token = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        doInitialization()
    }

    override fun onResume() {
        super.onResume()
        if (token != "") {
            cartViewModel.getCart()
        }
    }

    private fun doInitialization() {
        token = Commons.getSharedPreferences(requireContext()).getString(Commons.SERVER_TOKEN, "")!!
        sendLocation()
        val openOrder =
            Commons.getSharedPreferences(requireContext()).getBoolean(Commons.OPEN_ORDER, false)
        if (openOrder) {
            val orderId = Commons.getSharedPreferences(requireContext()).getInt(Commons.ORDER_ID, 0)
            val action = HomeFragmentDirections.actionHomeFragmentToOrderFragment()
            action.orderId = orderId
            findNavController().navigate(action)
        }
        setupDrawerNavigation()
        Log.e("TAG", "doInitialization: $token")
        binding.tvAddresses.setOnClickListener {
            if (token == "") {
                Commons.showLoginDialog(requireActivity())
            } else {
                val dialog = MyAddressesDialog {

                    val location = Location(
                        it.address,
                        it.lat.toDouble(),
                        it.lng.toDouble(),
                        it.area_id.toInt()
                    )

                    val gson = Gson()
                    val json = gson.toJson(location)
                    Commons.getSharedEditor(requireContext())
                        .putString(Commons.KEY_MY_LOCATION, json)
                        .apply()

                    binding.tvAddresses.text = it.address
                    viewModel.regetHome(it.area_id.toInt())
                }
                dialog.show(requireActivity().supportFragmentManager, "Addresses Dialog")
            }
        }

        setupStore()
        setupBanner()
        setupOccasion()
        setupDepartment()
        setupNew()
        setupTrend()
        fillHomeData()

        val gson = Gson()
        val json: String = Commons.getSharedPreferences(requireContext())
            .getString(Commons.KEY_MY_LOCATION, "")!!
        val location: Location = gson.fromJson(json, Location::class.java)
        binding.tvAddresses.text = location.address

        binding.imgSearch.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_shopsFragment)
        }
        binding.layoutOccasion.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToCategoriesFragment()
            action.categoryType = 0
            findNavController().navigate(action)
        }
        binding.layoutDepartment.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToCategoriesFragment()
            action.categoryType = 1
            findNavController().navigate(action)
        }
        binding.navStore.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_shopsFragment)
        }
        binding.navCart.setOnClickListener {
            if (token == "") {
                Commons.showLoginDialog(requireActivity())
            } else {
                findNavController().navigate(R.id.action_homeFragment_to_cartFragment)
            }
        }
        binding.layoutLogout.setOnClickListener {
            if (token == "") {
                Commons.showLoginDialog(requireActivity())
            } else {
                val builder = AlertDialog.Builder(requireContext())
                builder.setTitle(getString(R.string.warning))
                builder.setMessage(getString(R.string.are_sure))
                builder.setPositiveButton(getString(R.string.yes)) { _, _ ->
                    authViewModel.leaveAccount()
                    authViewModel.dataLogout.observe(viewLifecycleOwner,
                        { response ->
                            if (response != null) {
                                if (response.status == 200) {
                                    val deleteToken = DeleteToken(
                                        Commons.getSharedPreferences(requireContext())
                                            .getString(Commons.DEVICE_TOKEN, "")!!
                                    )
                                    Commons.getSharedEditor(requireContext())
                                        .clear().apply()
                                    tokenViewModel.deleteToken(deleteToken)
                                    tokenViewModel.dataDelete.observe(viewLifecycleOwner,
                                        {
                                            if (it.status == 200) {
                                                startActivity(
                                                    Intent(
                                                        requireContext(),
                                                        AuthActivity::class.java
                                                    )
                                                )
                                                requireActivity().finish()
                                            } else {
                                                Snackbar.make(
                                                    requireView(),
                                                    it.message,
                                                    Snackbar.LENGTH_SHORT
                                                ).show()
                                            }
                                        })
                                } else {
                                    Snackbar.make(
                                        requireView(),
                                        response.message,
                                        Snackbar.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        }
                    )
                }
                builder.setNegativeButton(getString(R.string.no)) { view, _ -> view.dismiss() }
                val dialog = builder.create()
                dialog.show()
            }
        }
        setupCartBadge()
    }

    private fun sendLocation() {
        val isSentLoc = Commons.getSharedPreferences(requireContext()).getBoolean(Commons.IS_SENT_LOC, false)
        if (!isSentLoc) {
            locationViewModel.saveLocation(Commons.getLocation(requireContext()))
            locationViewModel.dataLocation.observe(viewLifecycleOwner, {
                Log.e("EEE location",it.toString())
            })
            Commons.getSharedEditor(requireContext()).putBoolean(Commons.IS_SENT_LOC, true).apply()
        }
    }

    private fun setupDrawerNavigation() {
        binding.imgMenu.setOnClickListener {
            if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                binding.drawerLayout.closeDrawer(GravityCompat.START)
            } else {
                binding.drawerLayout.openDrawer(GravityCompat.START)
            }
        }
        binding.navigationView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.itemProfile -> {
                    if (token == "") {
                        Commons.showLoginDialog(requireActivity())
                    } else {
                        findNavController().navigate(R.id.action_homeFragment_to_profileFragment)
                    }
                }
                R.id.itemOrders -> {
                    if (token == "") {
                        Commons.showLoginDialog(requireActivity())
                    } else {
                        findNavController().navigate(R.id.action_homeFragment_to_ordersFragment)
                    }
                }
                R.id.itemFavourite -> {
                    if (token == "") {
                        Commons.showLoginDialog(requireActivity())
                    } else {
                        findNavController().navigate(R.id.action_homeFragment_to_favouriteFragment)
                    }
                }
                R.id.itemAddresses -> {
                    if (token == "") {
                        Commons.showLoginDialog(requireActivity())
                    } else {
                        findNavController().navigate(R.id.action_homeFragment_to_addressesFragment)
                    }
                }
                R.id.itemAbout -> {
                    findNavController().navigate(R.id.action_homeFragment_to_aboutFragment)
                }
                R.id.itemCall -> {
                    if (token == "") {
                        Commons.showLoginDialog(requireActivity())
                    } else {
                        findNavController().navigate(R.id.action_homeFragment_to_callFragment)
                    }
                }
                R.id.itemLang -> {
                    when (Commons.getSharedPreferences(requireContext())
                        .getString(Commons.LANGUAGE, "ar")) {
                        "ar" -> {
                            Commons.getSharedEditor(requireContext())
                                .putString(Commons.LANGUAGE, "en").apply()
                        }
                        "en" -> {
                            Commons.getSharedEditor(requireContext())
                                .putString(Commons.LANGUAGE, "ar").apply()
                        }
                    }
                    requireActivity().startActivity(
                        Intent(
                            requireContext(),
                            MainActivity::class.java
                        )
                    )
                    requireActivity().finish()
                }
            }
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            true
        }
    }

    private fun setupCartBadge() {
        cartViewModel.dataCart.observe(viewLifecycleOwner,
            {
                if (it != null) {
                    if (it.status == 200) {
                        if (it.data.items.isNotEmpty()) {
                            var num = 0
                            it.data.items.forEach { item ->
                                num += item.products.size
                            }
                            binding.tvCartItems.text = num.toString()
                            binding.cardBadge.visibility = View.VISIBLE
                        }
                    }
                }
            })
    }

    private fun setupStore() {
        storeAdapter = HomeStoreAdapter { store ->
            val action = HomeFragmentDirections.actionHomeFragmentToShopFragment(store)
            findNavController().navigate(action)
        }
        binding.rcHomeSores.adapter = storeAdapter
        binding.rcHomeSores.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
    }

    private fun setupOccasion() {
        occasionAdapter = HomeCategoryAdapter { category ->
            // Go to category page
            val action = HomeFragmentDirections.actionHomeFragmentToOccasionFragment()
            action.categoryId = category.id
            findNavController().navigate(action)
        }
        binding.rcHomeOccasion.adapter = occasionAdapter
        binding.rcHomeOccasion.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
    }

    private fun setupDepartment() {
        departmentAdapter = HomeCategoryAdapter { category ->
            // Go to category page
            val action = HomeFragmentDirections.actionHomeFragmentToCategoryFragment()
            action.categoryId = category.id
            findNavController().navigate(action)
        }
        binding.rcHomeDepartment.adapter = departmentAdapter
        binding.rcHomeDepartment.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
    }

    private fun setupNew() {
        newAdapter = HomeProductAdapter { product ->
            // Go to product page
            val action = HomeFragmentDirections.actionHomeFragmentToProductFragment()
            action.productId = product.id
            findNavController().navigate(action)
        }
        binding.rcHomeNew.adapter = newAdapter
        binding.rcHomeNew.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
    }

    private fun setupTrend() {
        trendAdapter = HomeProductAdapter { product ->
            // Go to product page
            val action = HomeFragmentDirections.actionHomeFragmentToProductFragment()
            action.productId = product.id
            findNavController().navigate(action)
        }
        binding.rcHomeTrend.adapter = trendAdapter
        binding.rcHomeTrend.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
    }

    private fun setupBanner() {
        setBannerItems()
    }

    private fun setBannerItems() {
        bannerAdapter = HomeBannerAdapter(requireActivity()) { ad ->
            Log.e("TAG", "setBannerItems: ${ad.type}")
            Log.e("TAG", "setBannerItems: ${ad.type_id}")
            when (ad.type) {
                "external" -> {
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse(ad.external_url)
                    requireActivity().startActivity(intent)
                }
                "store" -> {
//                    val action = HomeFragmentDirections.actionHomeFragmentToShopFragment(null)
//                    findNavController().navigate(action)
                }
                "product" -> {
                    val action = HomeFragmentDirections.actionHomeFragmentToProductFragment()
                    action.productId = ad.type_id
                    findNavController().navigate(action)
                }
            }
        }
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

    private fun fillHomeData() {
        viewModel.dataHome.observe(viewLifecycleOwner,
            {
                if (it != null) {

                    if (it.data.stores.isEmpty()) {
                        binding.rcHomeSores.visibility = View.GONE
                    } else {
                        binding.rcHomeSores.visibility = View.VISIBLE
                    }
                    storeAdapter.data.addAll(it.data.stores)
                    storeAdapter.notifyDataSetChanged()

                    if (it.data.ads.isEmpty()) {
                        binding.banner.visibility = View.GONE
                    } else {
                        binding.banner.visibility = View.VISIBLE
                    }
                    bannerAdapter.data.addAll(it.data.ads)
                    bannerAdapter.notifyDataSetChanged()

                    setupIndicators()
                    setCurrentIndicator(0)

                    if (it.data.occasions.isEmpty()) {
                        binding.rcHomeOccasion.visibility = View.GONE
                        binding.layoutOccasion.visibility = View.GONE
                    } else {
                        binding.rcHomeOccasion.visibility = View.VISIBLE
                        binding.layoutOccasion.visibility = View.VISIBLE
                    }
                    occasionAdapter.data.addAll(it.data.occasions)
                    occasionAdapter.notifyDataSetChanged()

                    if (it.data.categories.isEmpty()) {
                        binding.rcHomeDepartment.visibility = View.GONE
                        binding.layoutDepartment.visibility = View.GONE
                    } else {
                        binding.rcHomeDepartment.visibility = View.VISIBLE
                        binding.layoutDepartment.visibility = View.VISIBLE
                    }
                    departmentAdapter.data.addAll(it.data.categories)
                    departmentAdapter.notifyDataSetChanged()

                    if (it.data.new_products.isEmpty()) {
                        binding.rcHomeNew.visibility = View.GONE
                        binding.layoutCatNew.visibility = View.GONE
                    } else {
                        binding.rcHomeNew.visibility = View.VISIBLE
                        binding.layoutCatNew.visibility = View.VISIBLE
                    }
                    newAdapter.data.addAll(it.data.new_products)
                    newAdapter.notifyDataSetChanged()

                    if (it.data.most_sales.isEmpty()) {
                        binding.rcHomeTrend.visibility = View.GONE
                        binding.layoutCatTrend.visibility = View.GONE
                    } else {
                        binding.rcHomeTrend.visibility = View.VISIBLE
                        binding.layoutCatTrend.visibility = View.VISIBLE
                    }
                    trendAdapter.data.addAll(it.data.most_sales)
                    trendAdapter.notifyDataSetChanged()
                }
            }
        )
    }

}