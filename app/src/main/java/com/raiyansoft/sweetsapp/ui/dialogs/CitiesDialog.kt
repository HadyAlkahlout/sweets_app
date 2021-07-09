package com.raiyansoft.sweetsapp.ui.dialogs

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.raiyansoft.sweetsapp.R
import com.raiyansoft.sweetsapp.adapters.CityAdapter
import com.raiyansoft.sweetsapp.databinding.DialogCitiesBinding
import com.raiyansoft.sweetsapp.databinding.DialogMyAddressesBinding
import com.raiyansoft.sweetsapp.models.cities.Region
import com.raiyansoft.sweetsapp.ui.viewmodel.areas.AreaViewModel

class CitiesDialog(val onClick : (region : Region) -> Unit) :
    DialogFragment() {

    private lateinit var binding: DialogCitiesBinding
    private lateinit var adapter: CityAdapter

    private val viewModel by lazy {
        ViewModelProvider(this)[AreaViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogCitiesBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        doInitialization()
    }

    private fun doInitialization() {
        adapter = CityAdapter(requireContext()){
            onClick(it)
            dismiss()
        }
        binding.rcCities.layoutManager = LinearLayoutManager(requireContext())
        binding.rcCities.adapter = adapter
        binding.rcCities.startAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.recyclerview_animation))
        fillAddresses()
    }

    private fun fillAddresses() {
        Log.e("TAG", "Entered fillAddresses")
        viewModel.getAreas()
        viewModel.dataAreas.observe(viewLifecycleOwner,
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

}