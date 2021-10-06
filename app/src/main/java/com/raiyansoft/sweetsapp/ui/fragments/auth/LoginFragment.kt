package com.raiyansoft.sweetsapp.ui.fragments.auth

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.raiyansoft.sweetsapp.R
import com.raiyansoft.sweetsapp.databinding.FragmentLoginBinding
import com.raiyansoft.sweetsapp.models.login.Login
import com.raiyansoft.sweetsapp.ui.activities.MainActivity
import com.raiyansoft.sweetsapp.ui.viewmodel.auth.AuthViewModel

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding

    private val viewModel by lazy {
        ViewModelProvider(this)[AuthViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(layoutInflater)
        onBackPressed()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        doInitialization()
    }

    private fun doInitialization() {
        binding.isLogin = false
        binding.btnSkip.setOnClickListener {
            startActivity(Intent(requireContext(), MainActivity::class.java))
            requireActivity().finish()
        }
        binding.btnLogin.setOnClickListener {
            if (binding.edName.text.isEmpty()) {
                binding.edName.background = ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.edittext_error_background
                )
            } else if (binding.edPhone.text.isEmpty()) {
                binding.edName.background =
                    ContextCompat.getDrawable(requireContext(), R.drawable.edittext_background)
                binding.edPhone.background = ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.edittext_error_background
                )
            }
//            else if (binding.edPhone.text.toString().length != 8) {
//                binding.edPhone.background = ContextCompat.getDrawable(
//                    requireContext(),
//                    R.drawable.edittext_error_background
//                )
//            }
            else {
                binding.edName.background =
                    ContextCompat.getDrawable(requireContext(), R.drawable.edittext_background)
                binding.edPhone.background =
                    ContextCompat.getDrawable(requireContext(), R.drawable.edittext_background)
                val phone = "00965${binding.edPhone.text.toString().trim()}"
                doLogin(binding.edName.text.toString().trim(), phone)
            }
        }
        binding.tvTerm.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_termsFragment)
        }
    }

    private fun doLogin(name: String, phone: String) {
        if (binding.rbAgree.isChecked) {
            binding.isLogin = true
            val login = Login(name, phone)
            viewModel.mackAccount(login)
            viewModel.dataLogin.observe(viewLifecycleOwner,
                { response ->
                    if (response != null) {
                        if (response.status == 200) {
                            binding.isLogin = false
                            val action =
                                LoginFragmentDirections.actionLoginFragmentToActivationFragment(
                                    phone
                                )
                            findNavController().navigate(action)
                        } else {
                            Snackbar.make(requireView(), response.message, Snackbar.LENGTH_SHORT)
                                .show()
                        }
                    }
                }
            )
        } else {
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle(getString(R.string.login_error_title))
            builder.setMessage(getString(R.string.login_error_body))
            builder.setPositiveButton(getString(R.string.ok)) { view, _ -> view.dismiss() }
            val dialog = builder.create()
            dialog.show()
        }
    }

    private fun onBackPressed() {
        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val navController = findNavController()
                navController.popBackStack(R.id.authMapFragment, true)
                navController.navigateUp()
            }
        }
        // ADD LIFECYCLE OWNER
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            onBackPressedCallback
        )
    }

}
