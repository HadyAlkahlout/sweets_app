package com.raiyansoft.sweetsapp.ui.fragments.main.drawerNav

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.raiyansoft.sweetsapp.R
import com.raiyansoft.sweetsapp.adapters.OrderCartAdapter
import com.raiyansoft.sweetsapp.databinding.FragmentOrderBinding
import com.raiyansoft.sweetsapp.ui.dialogs.CancelBottomSheet
import com.raiyansoft.sweetsapp.ui.dialogs.RateBottomSheet
import com.raiyansoft.sweetsapp.ui.viewmodel.order.OrderViewModel
import com.raiyansoft.sweetsapp.util.Commons

class OrderFragment : Fragment() {

    private lateinit var binding: FragmentOrderBinding
    private lateinit var adapter: OrderCartAdapter

    private var orderId = 0
    private var open = 1

    private val viewModel by lazy {
        ViewModelProvider(this)[OrderViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOrderBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        doInitialization()
    }

    private fun doInitialization() {
        if (arguments != null) {
            val args = OrderFragmentArgs.fromBundle(requireArguments())
            orderId = args.orderId
            open = args.open
        }
        viewModel.getOrder(orderId)
        binding.imgBack.setOnClickListener {
            when (open) {
                1 -> {
                    findNavController().navigate(R.id.action_orderFragment_to_homeFragment)
                }
                0 -> {
                    findNavController().navigateUp()
                }
            }
        }
        Commons.getSharedEditor(requireContext()).putBoolean(Commons.OPEN_ORDER, false).apply()
        Commons.getSharedEditor(requireContext()).putInt(Commons.ORDER_ID, 0).apply()
        adapter = OrderCartAdapter(requireContext())
        binding.rcOrder.adapter = adapter
        binding.rcOrder.startAnimation(
            AnimationUtils.loadAnimation(
                requireContext(),
                R.anim.recyclerview_animation
            )
        )
        binding.rcOrder.layoutManager = LinearLayoutManager(requireContext())
        fillOrderData()
        binding.btnCancel.setOnClickListener {
            val dialog = CancelBottomSheet {

            }
            dialog.show(requireActivity().supportFragmentManager, "Cancel Dialog")
        }
        binding.tvReview.visibility = View.GONE
        binding.tvReview.setOnClickListener {
            val bottomSheet = RateBottomSheet(orderId)
            bottomSheet.show(requireActivity().supportFragmentManager, "Rate Bottom Sheet")
        }
        binding.btnRepeat.setOnClickListener {

        }
    }

    private fun fillOrderData() {
        viewModel.dataOrder.observe(viewLifecycleOwner,
            {
                Log.e("TAG", "fillOrderData: $it")
                if (it != null) {
                    if (it.status == 200) {
                        binding.order = it.data
                        val total = it.data.total + it.data.delivery_fees.toInt()
                        binding.tvTotal.text = "$total"
                        adapter.data.addAll(it.data.items)
                        adapter.notifyDataSetChanged()
                        binding.isFinished = false
                        when (it.data.status) {
                            "opened" -> {
                                binding.tvOrderStatus.setTextColor(Color.parseColor("#FFAB2A"))
                                binding.tvOrderStatus.text = getString(R.string.status_opened)
                            }
                            "submitted" -> {
                                binding.tvOrderStatus.setTextColor(Color.parseColor("#FFAB2A"))
                                binding.tvOrderStatus.text = getString(R.string.status_submitted)
                            }
                            "accepted" -> {
                                binding.tvOrderStatus.setTextColor(Color.parseColor("#FFAB2A"))
                                binding.tvOrderStatus.text = getString(R.string.status_accepted)
                            }
                            "prepared" -> {
                                binding.tvOrderStatus.setTextColor(Color.parseColor("#FFAB2A"))
                                binding.tvOrderStatus.text = getString(R.string.status_prepared)
                            }
                            "on_delivery" -> {
                                binding.tvOrderStatus.setTextColor(Color.parseColor("#F48B85"))
                                binding.tvOrderStatus.text = getString(R.string.status_on_delivery)
                            }
                            "delivered" -> {
                                binding.tvOrderStatus.setTextColor(Color.parseColor("#A7D4C8"))
                                binding.tvOrderStatus.text = getString(R.string.status_delivered)
                                binding.tvReview.visibility = View.VISIBLE
                            }
                            "canceled" -> {
                                binding.tvOrderStatus.setTextColor(Color.parseColor("#FF5A50"))
                                binding.tvOrderStatus.text = getString(R.string.status_canceled)
                            }
                        }

                    } else {
                        Snackbar.make(requireView(), it.message, Snackbar.LENGTH_SHORT).show()
                        findNavController().navigateUp()
                    }
                }
            })
    }
}