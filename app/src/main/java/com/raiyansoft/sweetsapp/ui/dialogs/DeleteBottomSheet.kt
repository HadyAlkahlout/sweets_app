package com.raiyansoft.sweetsapp.ui.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.raiyansoft.sweetsapp.databinding.DialogDeleteBottomsheerBinding

class DeleteBottomSheet(val delete: () -> Unit) : BottomSheetDialogFragment() {

    private lateinit var binding: DialogDeleteBottomsheerBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogDeleteBottomsheerBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        doInitialization()
    }

    private fun doInitialization() {
        binding.tvYes.setOnClickListener {
           // Enable Notification
            delete()
            dismiss()
        }
        binding.tvNo.setOnClickListener {
            dismiss()
        }
    }
}