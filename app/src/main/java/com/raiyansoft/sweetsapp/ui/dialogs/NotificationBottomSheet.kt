package com.raiyansoft.sweetsapp.ui.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.raiyansoft.sweetsapp.databinding.DialogNotificationBottomsheerBinding

class NotificationBottomSheet(val onMove: () -> Unit) : BottomSheetDialogFragment() {

    private lateinit var binding: DialogNotificationBottomsheerBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogNotificationBottomsheerBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        doInitialization()
    }

    private fun doInitialization() {
        binding.btnEnable.setOnClickListener {
           // Enable Notification
            onMove()
            dismiss()
        }
        binding.tvSkip.setOnClickListener {
            onMove()
            dismiss()
        }
    }
}