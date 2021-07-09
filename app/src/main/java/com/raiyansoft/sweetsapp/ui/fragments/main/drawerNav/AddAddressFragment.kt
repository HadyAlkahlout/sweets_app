package com.raiyansoft.sweetsapp.ui.fragments.main.drawerNav

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.raiyansoft.sweetsapp.R
import com.raiyansoft.sweetsapp.databinding.FragmentAddAddressBinding
import com.raiyansoft.sweetsapp.models.address.CreateLocation
import com.raiyansoft.sweetsapp.models.address.UpdateAddressResponse
import com.raiyansoft.sweetsapp.ui.dialogs.CitiesDialog
import com.raiyansoft.sweetsapp.ui.dialogs.DeleteBottomSheet
import com.raiyansoft.sweetsapp.ui.dialogs.MapDialog
import com.raiyansoft.sweetsapp.ui.viewmodel.auth.AuthViewModel
import com.raiyansoft.sweetsapp.ui.viewmodel.navDrawer.AddressViewModel

class AddAddressFragment : Fragment() {

    private lateinit var binding: FragmentAddAddressBinding
    private var edit = false
    private var addressObj: UpdateAddressResponse? = null

    private val viewModel by lazy {
        ViewModelProvider(this)[AddressViewModel::class.java]
    }

    private val authViewModel by lazy {
        ViewModelProvider(this)[AuthViewModel::class.java]
    }

    private var address = ""
    private var areaID = 0
    private var lat = 0.0
    private var long = 0.0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddAddressBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        doInitialization()
    }

    private fun doInitialization() {
        binding.imgBack.setOnClickListener {
            findNavController().navigateUp()
        }

        if (arguments != null) {
            val args = AddAddressFragmentArgs.fromBundle(requireArguments())
            if (args.edit) {
                edit = args.edit
                addressObj = args.address
                address = addressObj!!.address
                areaID = addressObj!!.area_id.toInt()
                lat = addressObj!!.lat.toDouble()
                long = addressObj!!.lng.toDouble()
                binding.isEdit = true
                fillAddress()
            }
        }
        binding.btnSave.setOnClickListener {
            if (binding.tvAddresses.text == getText(R.string.address)) {
                binding.tvAddresses.background = ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.edittext_error_background
                )
            } else if (lat == 0.0) {
                binding.tvAddLocation.background = ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.edittext_error_background
                )
            } else {
                if (edit) {
                    updateLocation()
                } else {
                    saveAddress()
                }
            }
        }

        binding.tvDelete.setOnClickListener {
            val bottomSheet = DeleteBottomSheet {
                viewModel.deleteAddress(addressObj!!.id)
                viewModel.dataDelete.observe(viewLifecycleOwner,
                    {
                        if (it.status == 200) {
                            Snackbar.make(requireView(), it.data, Snackbar.LENGTH_SHORT)
                                .show()
                            findNavController().navigateUp()
                        } else {
                            Snackbar.make(requireView(), it.message, Snackbar.LENGTH_SHORT)
                                .show()
                        }
                    })
            }
            bottomSheet.show(requireActivity().supportFragmentManager, "Bottom Sheet")
        }

        binding.tvAddresses.setOnClickListener {
            val dialog = CitiesDialog {
                binding.tvAddresses.text = it.name
                address = it.name
                areaID = it.id
            }
            dialog.show(requireActivity().supportFragmentManager, "Cities Dialog")
        }

        binding.tvAddLocation.setOnClickListener {
            val dialog = MapDialog(lat, long) { lat, long ->
                this.lat = lat
                this.long = long
            }
            dialog.show(requireActivity().supportFragmentManager, "Map")
        }

    }

    private fun fillAddress() {
        binding.tvAddresses.text = addressObj!!.address
        binding.edPiece.setText(addressObj!!.part)
        binding.edStreet.setText(addressObj!!.street)
        binding.edAvenue.setText(addressObj!!.avenue)
        binding.edHouse.setText(addressObj!!.house_num)
        binding.edFloor.setText(addressObj!!.floor_num)
        binding.edAdditional.setText(addressObj!!.extra_address_note)
    }

    private fun saveAddress() {
        binding.pbLoad.visibility = View.VISIBLE
        val location = CreateLocation(
            address,
            lat.toString(),
            long.toString(),
            areaID.toString(),
            binding.edPiece.text.toString(),
            binding.edStreet.text.toString(),
            binding.edAvenue.text.toString(),
            binding.edHouse.text.toString(),
            binding.edFloor.text.toString(),
            binding.edAdditional.text.toString()
        )
        authViewModel.saveLocation(location)
        authViewModel.dataLocation.observe(viewLifecycleOwner,
            { response ->
                if (response != null) {
                    if (response.status == 200) {
                        Snackbar.make(
                            requireView(),
                            getString(R.string.update_success),
                            Snackbar.LENGTH_SHORT
                        ).show()
                        findNavController().navigateUp()
                    } else {
                        Snackbar.make(requireView(), response.message, Snackbar.LENGTH_SHORT).show()
                    }
                }
            }
        )
    }

    private fun updateLocation() {
        binding.pbLoad.visibility = View.VISIBLE
        Log.e("TAG", "updateLocation: enter update")
        val location = CreateLocation(
            address,
            lat.toString(),
            long.toString(),
            areaID.toString(),
            binding.edPiece.text.toString(),
            binding.edStreet.text.toString(),
            binding.edAvenue.text.toString(),
            binding.edHouse.text.toString(),
            binding.edFloor.text.toString(),
            binding.edAdditional.text.toString()
        )
        Log.e("TAG", "updateLocation: address id : ${addressObj!!.id}")
        viewModel.updateAddress(addressObj!!.id, location)
        viewModel.dataUpdate.observe(viewLifecycleOwner,
            { response ->
                if (response != null) {
                    if (response.status == 200) {
                        Snackbar.make(
                            requireView(),
                            getString(R.string.update_success),
                            Snackbar.LENGTH_SHORT
                        ).show()
                        findNavController().navigateUp()
                    } else {
                        Snackbar.make(requireView(), response.message, Snackbar.LENGTH_SHORT).show()
                    }
                }
            }
        )
    }

}