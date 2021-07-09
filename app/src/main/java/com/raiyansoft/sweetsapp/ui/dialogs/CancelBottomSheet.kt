package com.raiyansoft.sweetsapp.ui.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.raiyansoft.sweetsapp.databinding.DialogCancelBottomsheerBinding
import com.raiyansoft.sweetsapp.databinding.DialogDeleteBottomsheerBinding

class CancelBottomSheet(val cancel: () -> Unit) : BottomSheetDialogFragment() {

    private lateinit var binding: DialogCancelBottomsheerBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogCancelBottomsheerBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        doInitialization()
    }

    private fun doInitialization() {
        binding.tvYes.setOnClickListener {
           // Cancel Order
            cancel()
            dismiss()
        }
        binding.tvNo.setOnClickListener {
            dismiss()
        }
    }
}