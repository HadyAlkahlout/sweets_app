package com.raiyansoft.sweetsapp.ui.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import com.raiyansoft.sweetsapp.R
import com.raiyansoft.sweetsapp.databinding.BottomSheetRateBinding
import com.raiyansoft.sweetsapp.models.store.UserRate
import com.raiyansoft.sweetsapp.ui.viewmodel.review.ReviewViewModel

class RateBottomSheet(private val orderID: Int) :
    BottomSheetDialogFragment() {

    private lateinit var binding: BottomSheetRateBinding

    private val viewModel by lazy {
        ViewModelProvider(this)[ReviewViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BottomSheetRateBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handleResponse()
        binding.tvAddReview.setOnClickListener {
            if (binding.edMessage.text.isEmpty()) {
                if (binding.rbMyRate.rating > 1f) {
                    Snackbar.make(
                        requireView(),
                        getString(R.string.rate_review_error),
                        Snackbar.LENGTH_SHORT
                    ).show()
                } else {
                    binding.tvAddReview.isEnabled = false
                    val userRate = UserRate(
                        binding.rbMyRate.rating.toDouble(),
                        binding.edMessage.text.toString()
                    )
                    viewModel.sendRate(orderID, userRate)
                }
            } else {
                Snackbar.make(
                    requireView(),
                    getString(R.string.rate_massage_error),
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }

        binding.tvCancel.setOnClickListener {
            dialog!!.dismiss()
        }

    }

    private fun handleResponse() {
        viewModel.dataSend.observe(viewLifecycleOwner,
            { response ->
                if (response.status == 200) {
                    Snackbar.make(
                        requireView(),
                        getString(R.string.rate_done),
                        Snackbar.LENGTH_SHORT
                    ).show()
                    dialog!!.dismiss()
                }
                binding.tvAddReview.isEnabled = true
            })
    }

}