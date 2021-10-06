package com.raiyansoft.sweetsapp.ui.fragments.main.drawerNav

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
import com.raiyansoft.sweetsapp.adapters.AddressAdapter
import com.raiyansoft.sweetsapp.databinding.FragmentAddressesBinding
import com.raiyansoft.sweetsapp.ui.viewmodel.auth.AuthViewModel
import com.raiyansoft.sweetsapp.ui.viewmodel.navDrawer.AddressViewModel
import com.raiyansoft.sweetsapp.util.Commons

class AddressesFragment : Fragment() {

    private var binding: FragmentAddressesBinding? = null
    private lateinit var adapter: AddressAdapter

    private val viewModel by lazy {
        ViewModelProvider(this)[AddressViewModel::class.java]
    }

    override fun onResume() {
        super.onResume()
        adapter.data.clear()
        adapter.notifyDataSetChanged()
        getData()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddressesBinding.inflate(layoutInflater)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        doInitialization()
    }

    private fun doInitialization() {
        val action = AddressesFragmentDirections.actionAddressesFragmentToAddAddressFragment()
        binding!!.imgBack.setOnClickListener {
            findNavController().navigateUp()
        }
        fillAddressesData()
        binding!!.tvAddAddress.setOnClickListener {
            action.edit = false
            findNavController().navigate(action)
        }
        adapter = AddressAdapter { address ->
            action.edit = true
            action.address = address
            findNavController().navigate(action)
        }
        binding!!.rcAddresses.adapter = adapter
        binding!!.rcAddresses.layoutManager = LinearLayoutManager(requireContext())
        binding!!.rcAddresses.startAnimation(
            AnimationUtils.loadAnimation(
                requireContext(),
                R.anim.recyclerview_animation
            )
        )
        binding!!.swipeLayout.setOnRefreshListener {
            getData()
        }
    }

    private fun fillAddressesData() {
        viewModel.dataAddress.observe(viewLifecycleOwner,
            {
                if (it.status == 200) {
                    adapter.data.clear()
                    adapter.notifyDataSetChanged()
                    Log.e("TAG", "fillAddressesData: ${it.data}")
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
        viewModel.getAddress()
    }

}