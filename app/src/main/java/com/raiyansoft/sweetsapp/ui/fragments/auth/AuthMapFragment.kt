package com.raiyansoft.sweetsapp.ui.fragments.auth

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar
import com.raiyansoft.sweetsapp.R
import com.raiyansoft.sweetsapp.databinding.FragmentAuthMapBinding
import com.raiyansoft.sweetsapp.ui.activities.MainActivity
import com.raiyansoft.sweetsapp.ui.dialogs.CitiesDialog
import com.raiyansoft.sweetsapp.ui.viewmodel.auth.AuthViewModel
import com.raiyansoft.sweetsapp.util.Commons
import java.util.*


class AuthMapFragment : Fragment() {

    private lateinit var binding: FragmentAuthMapBinding
    private var currentLocation = MutableLiveData<Location>()
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private val REQUEST_CODE = 101

    private lateinit var manager: LocationManager

    private var lat = 0.0
    private var long = 0.0
    private var address = ""
    private var areaID = 0

    private val viewModel by lazy {
        ViewModelProvider(this)[AuthViewModel::class.java]
    }

    private val callback = OnMapReadyCallback { googleMap ->
        fetchLastLocation()

        val markerOptions = MarkerOptions()
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_location))
        val kuwait = LatLng(29.3117, 47.4818)
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(kuwait, 8f))

        val mapSettings = googleMap!!.uiSettings
        mapSettings.isMyLocationButtonEnabled = true

        currentLocation.observe(this, { location ->
            val latLong = LatLng(location!!.latitude, location.longitude)
            markerOptions.position(latLong).title(getString(R.string.map_here))

            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLong, 8f))
            googleMap.addMarker(markerOptions)
            lat = location.latitude
            long = location.longitude
        })


        googleMap.setOnMapClickListener { location ->

            googleMap.clear()
            markerOptions.position(location)
            googleMap.addMarker(markerOptions)
            lat = location.latitude
            long = location.longitude

        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAuthMapBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
        doInitialization()
    }

    private fun doInitialization() {
        binding.isLogin = false
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(
            requireActivity()
        )
        manager = requireContext().getSystemService(FragmentActivity.LOCATION_SERVICE) as LocationManager
        binding.btnAddLocation.setOnClickListener {
            setLocation()
        }
        binding.tvAddresses.setOnClickListener {
            val dialog = CitiesDialog{
                binding.tvAddresses.text = it.name
                address = it.name
                areaID = it.id
            }
            dialog.show(requireActivity().supportFragmentManager, "Cities Dialog")
        }
    }

    private fun setLocation() {
        if (lat == 0.0 || long == 0.0){
            Snackbar.make(requireView(), getString(R.string.location_ditict), Snackbar.LENGTH_SHORT).show()
        } else if(binding.tvAddresses.text.toString() == getString(R.string.address)) {
            binding.tvAddresses.background = ContextCompat.getDrawable(requireContext(), R.drawable.edittext_error_background)
        } else {
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle(getString(R.string.warning))
            builder.setMessage(getString(R.string.are_sure))
            builder.setPositiveButton(getString(R.string.yes)) { _, _ ->
                binding.isLogin = true
                val location = com.raiyansoft.sweetsapp.models.address.Location(address, lat, long, areaID)
                viewModel.saveLocation(location)
                viewModel.dataLocation.observe(viewLifecycleOwner,
                    { response ->
                        if (response != null) {
                            if (response.status == 200) {
                                binding.isLogin = false
                                Commons.getSharedEditor(requireContext()).putBoolean(
                                    Commons.LOGIN,
                                    true
                                ).apply()
                                startActivity(Intent(requireContext(), MainActivity::class.java))
                                requireActivity().finish()
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

    private fun fetchLastLocation() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_CODE)
            return
        }
        val task = fusedLocationProviderClient.lastLocation
        task.addOnSuccessListener {
            if (it != null) {
                currentLocation.postValue(it)
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    fetchLastLocation()
                }
            }
        }
    }

}