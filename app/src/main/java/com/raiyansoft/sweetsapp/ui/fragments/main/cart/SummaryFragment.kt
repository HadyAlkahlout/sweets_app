package com.raiyansoft.sweetsapp.ui.fragments.main.cart

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.raiyansoft.sweetsapp.R
import com.raiyansoft.sweetsapp.adapters.SummaryAdapter
import com.raiyansoft.sweetsapp.databinding.FragmentSummaryBinding
import com.raiyansoft.sweetsapp.models.cart.Product
import com.raiyansoft.sweetsapp.models.cart.SubmitDetails
import com.raiyansoft.sweetsapp.ui.dialogs.MyAddressesDialog
import com.raiyansoft.sweetsapp.ui.viewmodel.cart.CartViewModel
import com.raiyansoft.sweetsapp.util.Commons
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.log

class SummaryFragment : Fragment(), TimePickerDialog.OnTimeSetListener,
    DatePickerDialog.OnDateSetListener {

    private lateinit var binding: FragmentSummaryBinding
    private lateinit var adapter: SummaryAdapter

    private val viewModel by lazy {
        ViewModelProvider(this)[CartViewModel::class.java]
    }

    private val dateDialog by lazy {
        com.raiyansoft.sweetsapp.ui.dialogs.DatePickerDialog(this)
    }

    private val timeDialog by lazy {
        com.raiyansoft.sweetsapp.ui.dialogs.TimePickerDialog(this)
    }

    private val builder by lazy {
        AlertDialog.Builder(requireContext())
    }
    private val dialog by lazy {
        builder.create()
    }

    private var areaID = 0
    private var time = ""
    private var date = ""
    private var total = 0.0f
    private var pay = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSummaryBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        doInitialization()
    }

    private fun doInitialization() {
        adapter = SummaryAdapter()
        binding.rcItems.layoutManager = LinearLayoutManager(requireContext())
        binding.rcItems.adapter = adapter
        binding.imgBack.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.tvPay.setOnClickListener {
            if (binding.tvAddresses.text.toString() == getString(R.string.address)) {
                binding.layoutAddress.background = ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.edittext_error_background
                )
            } else {

                builder.setTitle(getString(R.string.warning))
                builder.setMessage(getString(R.string.are_sure))
                builder.setPositiveButton(getString(R.string.ok)) { _, _ -> goPay() }
                builder.setNegativeButton(getString(R.string.cancel)) { d, _ -> d.dismiss() }
                builder.setCancelable(false)
                dialog.show()

            }
        }
        binding.layoutDate.setOnClickListener {
            dateDialog.show(requireActivity().supportFragmentManager, "Date Dialog")
        }
        binding.layoutTime.setOnClickListener {
            timeDialog.show(requireActivity().supportFragmentManager, "time picker")
        }
        binding.tvAddresses.setOnClickListener {
            val dialog = MyAddressesDialog {
                binding.tvAddresses.text = it.address
                areaID = it.id
            }
            dialog.show(requireActivity().supportFragmentManager, "Addresses Dialog")
        }
        getCart()
    }

    private fun goPay() {
        if (pay) {
            Commons.showLoadingDialog(requireActivity())
            pay = false
            val submitDetails = SubmitDetails(areaID, date, time)
            viewModel.submitDetails(submitDetails)
            viewModel.dataSubmit.observe(viewLifecycleOwner,
                {
                    if (it != null) {
                        if (it.status == 200) {
                            Commons.dismissLoadingDialog()
                            val action =
                                SummaryFragmentDirections.actionSummaryFragmentToPayFragment()
                            action.total = total
                            findNavController().navigate(action)
                        } else {
                            Snackbar.make(requireView(), it.message, Snackbar.LENGTH_SHORT).show()
                        }
                        pay = true
                    }
                })
        }
    }

    private fun getCart() {
        viewModel.getCart()
        viewModel.dataCart.observe(viewLifecycleOwner,
            {
                if (it != null) {
                    if (it.status == 200) {
                        binding.cart = it.data
                        total = it.data.total.toFloat() + it.data.total_delivery_fees.toFloat()
                        binding.tvTotal.text = "$total"
                        val data = ArrayList<Product>()
                        for (i in it.data.items) {
                            for (x in i.products) {
                                data.add(x)
                            }
                        }
                        adapter.data.addAll(data)
                        adapter.notifyDataSetChanged()
                    } else {
                        Snackbar.make(requireView(), it.message, Snackbar.LENGTH_SHORT).show()
                        findNavController().navigateUp()
                    }
                } else {
                    Log.e("TAG", "getCart: null")
                }
            })
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        binding.tvTime.text = "$hourOfDay:$minute"
        time = "$hourOfDay:$minute"
        Log.e("date", "onTimeSet: $time")
        timeDialog.dismiss()
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        binding.tvDate.text = "$dayOfMonth/${month + 1}/$year"
        date = "$dayOfMonth/${month + 1}/$year"
        val sdf = SimpleDateFormat("dd/MM/yyyy")
        val currentDate = sdf.format(Date())
        if (currentDate != date) {
            binding.tvTime.text = "08:00"
            time = "8:00"
        } else {
            binding.tvDate.text = getString(R.string.date_hint)
            binding.tvTime.text = getString(R.string.time_hint)
            date = ""
            time = ""
        }
        dateDialog.dismiss()
    }

}