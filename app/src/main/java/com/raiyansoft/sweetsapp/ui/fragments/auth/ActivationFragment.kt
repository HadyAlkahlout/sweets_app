package com.raiyansoft.sweetsapp.ui.fragments.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.messaging.FirebaseMessaging
import com.raiyansoft.sweetsapp.R
import com.raiyansoft.sweetsapp.databinding.FragmentActivationBinding
import com.raiyansoft.sweetsapp.models.verification.Verification
import com.raiyansoft.sweetsapp.ui.activities.MainActivity
import com.raiyansoft.sweetsapp.ui.dialogs.NotificationBottomSheet
import com.raiyansoft.sweetsapp.ui.viewmodel.auth.AuthViewModel
import com.raiyansoft.sweetsapp.util.Commons
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ActivationFragment : Fragment() {

    private lateinit var binding: FragmentActivationBinding

    private val viewModel by lazy {
        ViewModelProvider(this)[AuthViewModel::class.java]
    }

    private var phone = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentActivationBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        doInitialization()
    }

    private fun doInitialization() {
        binding.isLogin = false
        if (arguments != null) {
            val args = ActivationFragmentArgs.fromBundle(requireArguments())
            phone = args.phone
        }
        binding.btnLogin.setOnClickListener {
            if (binding.edCode.text.isEmpty()) {
                binding.edCode.background = ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.edittext_error_background
                )
            } else {
                binding.edCode.background =
                    ContextCompat.getDrawable(requireContext(), R.drawable.edittext_background)
                activate(binding.edCode.text.toString().trim())
            }
        }
    }

    private fun activate(code: String) {
        binding.isLogin = true
        // Activate
        val verification = Verification(phone, code)
        viewModel.activateAccount(verification)
        viewModel.dataActive.observe(viewLifecycleOwner,
            { response ->
                if (response != null) {
                    if (response.status == 200) {
                        Commons.getSharedEditor(requireContext())
                            .putString(Commons.SERVER_TOKEN, "bearer ${response.data.token}")
                            .apply()
                        Commons.getSharedEditor(requireContext())
                            .putInt(Commons.USER_ID, response.data.id).apply()
                        Commons.getSharedEditor(requireContext())
                            .putString(Commons.USERNAME, "${response.data.name}").apply()
                        Commons.getSharedEditor(requireContext())
                            .putString(Commons.USER_PHONE, "${response.data.phone}").apply()
                        Commons.getSharedEditor(requireContext())
                            .putString(Commons.USER_EMAIL, "${response.data.email}").apply()

                        val bottomSheet = NotificationBottomSheet(
                            {
                                getFCMToken()
                                finishView()
                            },
                            {
                                finishView()
                            })
                        bottomSheet.show(
                            requireActivity().supportFragmentManager,
                            "Bottom Sheet"
                        )
                    } else {
                        Snackbar.make(requireView(), response.message, Snackbar.LENGTH_SHORT).show()
                    }
                }
            }
        )

    }

    private fun getFCMToken() {
        FirebaseMessaging.getInstance().subscribeToTopic("allUser").addOnSuccessListener {
            Log.e("app", "getFCMToken: subscribing the topic done successfully")
        }
    }

    private fun finishView() {
        Commons.getSharedEditor(requireContext()).putBoolean(
            Commons.LOGIN,
            true
        ).apply()
        startActivity(Intent(requireContext(), MainActivity::class.java))
        requireActivity().finish()
    }

}