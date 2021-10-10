package com.raiyansoft.sweetsapp.ui.dialogs

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.raiyansoft.sweetsapp.databinding.DialogLoadingBinding

class IndeterminateProgressDialog : DialogFragment() {

    private lateinit var mBinding: DialogLoadingBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = DialogLoadingBinding.inflate(inflater, container, false)
        isCancelable = false
        return mBinding.root
    }

    override fun onStart() {
        super.onStart()
        dialog!!.window!!.setLayout(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        onDestroy()
    }
}