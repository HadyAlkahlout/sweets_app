package com.raiyansoft.sweetsapp.ui.fragments.main.drawerNav

import android.os.Bundle
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
import com.raiyansoft.sweetsapp.adapters.FavouriteAdapter
import com.raiyansoft.sweetsapp.databinding.FragmentFavouriteBinding
import com.raiyansoft.sweetsapp.ui.viewmodel.navDrawer.FavouriteViewModel

class FavouriteFragment : Fragment() {

    private var binding: FragmentFavouriteBinding? = null
    private lateinit var adapter: FavouriteAdapter

    private val viewModel by lazy {
        ViewModelProvider(this)[FavouriteViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getData()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavouriteBinding.inflate(layoutInflater)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        doInitialization()
    }

    private fun doInitialization() {
        binding!!.imgBack.setOnClickListener {
            findNavController().navigateUp()
        }
        adapter = FavouriteAdapter { product ->
            val action = FavouriteFragmentDirections.actionFavouriteFragmentToProductFragment()
            action.productId = product.id
            findNavController().navigate(action)
        }
        binding!!.rcFavourite.adapter = adapter
        binding!!.rcFavourite.layoutManager = LinearLayoutManager(requireContext())
        binding!!.rcFavourite.startAnimation(
            AnimationUtils.loadAnimation(
                requireContext(),
                R.anim.recyclerview_animation
            )
        )
        fillFavouriteData()
        binding!!.swipeLayout.setOnRefreshListener {
            adapter.data.clear()
            adapter.notifyDataSetChanged()
            getData()
        }
    }

    private fun fillFavouriteData() {
        viewModel.dataFavourite.observe(viewLifecycleOwner,
            {
                if (it.status == 200) {
                    adapter.data.addAll(it.data)
                    adapter.notifyDataSetChanged()
                    binding!!.swipeLayout.isRefreshing = false
                } else {
                    Snackbar.make(requireView(), it.message, Snackbar.LENGTH_SHORT).show()
                    findNavController().navigateUp()
                }
            })
    }

    private fun getData() {
        if (binding != null) {
            binding!!.swipeLayout.isRefreshing = true
        }
        viewModel.getFav()
    }
}