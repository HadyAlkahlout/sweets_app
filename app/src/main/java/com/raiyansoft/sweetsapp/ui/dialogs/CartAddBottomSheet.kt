package com.raiyansoft.sweetsapp.ui.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.raiyansoft.sweetsapp.databinding.DialogCartAddBinding

class CartAddBottomSheet(val goCart: () -> Unit, val shopping: () -> Unit) : BottomSheetDialogFragment() {

    private lateinit var binding: DialogCartAddBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogCartAddBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        doInitialization()
    }

    private fun doInitialization() {
        binding.tvGoCart.setOnClickListener {
            goCart()
            dismiss()
        }
        binding.tvStay.setOnClickListener {
            shopping()
            dismiss()
        }
    }
}