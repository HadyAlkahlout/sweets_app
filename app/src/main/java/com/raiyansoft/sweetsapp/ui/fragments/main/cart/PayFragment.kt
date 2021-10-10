package com.raiyansoft.sweetsapp.ui.fragments.main.cart

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.raiyansoft.sweetsapp.R
import com.raiyansoft.sweetsapp.databinding.FragmentPayBinding
import com.raiyansoft.sweetsapp.models.cart.SubmitCart
import com.raiyansoft.sweetsapp.ui.activities.PayActivity
import com.raiyansoft.sweetsapp.ui.viewmodel.cart.CartViewModel
import com.raiyansoft.sweetsapp.util.Commons

class PayFragment : Fragment() {

    private lateinit var binding: FragmentPayBinding

    private val viewModel by lazy {
        ViewModelProvider(this)[CartViewModel::class.java]
    }

    private lateinit var submitCart: SubmitCart

    private var pay = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPayBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        doInitialization()
    }

    private fun doInitialization() {
        if (arguments != null) {
            val args = PayFragmentArgs.fromBundle(requireArguments())
            binding.tvTotal.text = "${getString(R.string.dinar)} ${args.total}"
        }
        binding.imgBack.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.tvPay.setOnClickListener {
            var payID = 0
            when {
                binding.rbKnet.isChecked -> {
                    payID = 1
                }
                binding.rbVisa.isChecked -> {
                    payID = 2
                }
                binding.rbCash.isChecked -> {
                    payID = 3
                }
                else -> {
                    binding.layoutPay.background = ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.edittext_error_background
                    )
                }
            }
            goPay(payID)
        }
    }

    private fun goPay(id: Int) {
        if (pay) {
            pay = false
            Commons.showLoadingDialog(requireActivity())
            when (id) {
                1 -> {
                    submitCart = SubmitCart("knet")
                }
                2 -> {
                    submitCart = SubmitCart("visaMaster")
                }
                3 -> {
                    submitCart = SubmitCart("cash_on_delivery")
                }
            }
            viewModel.submitCart(submitCart)
            viewModel.dataSubmitCart.observe(viewLifecycleOwner,
                {
                    if (it != null) {
                        if (it.status == 200) {
                            // Do Payment
                            if (id == 3) {
                                Commons.dismissLoadingDialog()
                                Snackbar.make(
                                    requireView(),
                                    getString(R.string.order_completed),
                                    Snackbar.LENGTH_SHORT
                                ).show()
                                findNavController().navigate(R.id.action_payFragment_to_homeFragment)
                            } else {
                                val intent = Intent(requireContext(), PayActivity::class.java)
                                intent.putExtra("payURL", it.data)
                                startActivity(intent)
                                requireActivity().finish()
                            }
                        } else {
                            Snackbar.make(requireView(), it.message, Snackbar.LENGTH_SHORT).show()
                        }
                        pay = true
                    }
                })
        }
    }

}