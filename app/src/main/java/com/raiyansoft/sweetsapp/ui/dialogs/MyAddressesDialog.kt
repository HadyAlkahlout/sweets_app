package com.raiyansoft.sweetsapp.ui.dialogs

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.animation.AnimationUtils
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.raiyansoft.sweetsapp.R
import com.raiyansoft.sweetsapp.adapters.MyAddressesAdapter
import com.raiyansoft.sweetsapp.databinding.DialogMyAddressesBinding
import com.raiyansoft.sweetsapp.models.address.UpdateAddressResponse
import com.raiyansoft.sweetsapp.ui.viewmodel.auth.AuthViewModel
import com.raiyansoft.sweetsapp.ui.viewmodel.navDrawer.AddressViewModel
import com.raiyansoft.sweetsapp.util.Commons

class MyAddressesDialog(val onClick: (address: UpdateAddressResponse) -> Unit) :
    DialogFragment() {

    private lateinit var binding: DialogMyAddressesBinding
    private lateinit var adapter: MyAddressesAdapter

    private val addressViewModel by lazy {
        ViewModelProvider(this)[AddressViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogMyAddressesBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        doInitialization()
    }

    private fun doInitialization() {
        adapter = MyAddressesAdapter{
            onClick(it)
            dismiss()
        }
        binding.rcMyAddresses.layoutManager = LinearLayoutManager(requireContext())
        binding.rcMyAddresses.adapter = adapter
        binding.rcMyAddresses.startAnimation(
            AnimationUtils.loadAnimation(
                requireContext(),
                R.anim.recyclerview_animation
            )
        )
        fillAddresses()
    }

    private fun fillAddresses() {
        addressViewModel.getAddress()
        addressViewModel.dataAddress.observe(viewLifecycleOwner,
            {
                if (it.status == 200) {
                    Log.e("TAG", "fillAddresses: ${it.data}")
                    adapter.data.clear()
                    adapter.data.addAll(it.data)
                    adapter.notifyDataSetChanged()
                } else {
                    Snackbar.make(requireView(), it.message, Snackbar.LENGTH_SHORT).show()
                }
            })
    }

    override fun onStart() {
        super.onStart()
        dialog!!.window!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
    }

}