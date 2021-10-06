package com.raiyansoft.sweetsapp.ui.dialogs

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.app.ActivityCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.raiyansoft.sweetsapp.R
import com.raiyansoft.sweetsapp.databinding.DialogMapBinding

class MapDialog(
    var lat: Double = 0.0,
    var long: Double = 0.0,
    val onPick: (lat: Double, long: Double) -> Unit
) :
    DialogFragment() {

    private lateinit var binding: DialogMapBinding
    private var currentLocation = MutableLiveData<Location>()
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private val REQUEST_CODE = 101

    private lateinit var manager: LocationManager

    private val callback = OnMapReadyCallback { googleMap ->
        fetchLastLocation()

        val markerOptions = MarkerOptions()
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_location))

        val mapSettings = googleMap!!.uiSettings
        mapSettings.isMyLocationButtonEnabled = true

        if (lat != 0.0){
            val latLong = LatLng(lat, long)
            markerOptions.position(latLong)
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLong, 8f))
            googleMap.addMarker(markerOptions)
            lat = latLong.latitude
            long = latLong.longitude
            binding.btnAddLocation.text = getString(R.string.update_location)
        } else {
            fetchLastLocation()
            currentLocation.observe(this, { location ->

                val latLong = LatLng(location!!.latitude, location.longitude)
                markerOptions.position(latLong).title(getString(R.string.map_here))

                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLong, 8f))
                googleMap.addMarker(markerOptions)
                lat = location.latitude
                long = location.longitude
            })
        }

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
        binding = DialogMapBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
        doInitialization()
    }

    private fun doInitialization() {
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())
        manager =
            requireContext().getSystemService(FragmentActivity.LOCATION_SERVICE) as LocationManager
        binding.btnAddLocation.setOnClickListener {
            setLocation()
        }
    }

    private fun setLocation() {
        onPick(lat, long)
        dismiss()
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

    override fun onStart() {
        super.onStart()
        dialog!!.window!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
    }

}