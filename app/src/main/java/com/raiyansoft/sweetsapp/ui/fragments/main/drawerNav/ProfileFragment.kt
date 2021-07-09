package com.raiyansoft.sweetsapp.ui.fragments.main.drawerNav

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.raiyansoft.sweetsapp.R
import com.raiyansoft.sweetsapp.databinding.FragmentProfileBinding
import com.raiyansoft.sweetsapp.models.profile.UpdateProfile
import com.raiyansoft.sweetsapp.ui.viewmodel.navDrawer.ProfileViewModel
import com.raiyansoft.sweetsapp.util.Commons

class ProfileFragment : Fragment(), DatePickerDialog.OnDateSetListener {

    private lateinit var binding: FragmentProfileBinding

    private val viewModel by lazy {
        ViewModelProvider(this)[ProfileViewModel::class.java]
    }

    private val dateDialog by lazy {
        com.raiyansoft.sweetsapp.ui.dialogs.DatePickerDialog(this)
    }

    private var name = ""
    private var sex = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        doInitialization()
    }

    private fun doInitialization() {
        binding.isLoading = false
        binding.imgBack.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.edDateOfBirth.setOnClickListener {
            dateDialog.show(requireActivity().supportFragmentManager, "Date Dialog")
        }
        binding.edPhone.isEnabled = false
        fillUserData()
        observeProfile()
        binding.btnSave.setOnClickListener {
            when {
                binding.edFirstName.text.isEmpty() -> {
                    binding.edFirstName.background = ContextCompat.getDrawable(requireContext(), R.drawable.edittext_error_background)
                }
                binding.edDateOfBirth.text == getString(R.string.birth_day) -> {
                    binding.edFirstName.background = ContextCompat.getDrawable(requireContext(), R.drawable.edittext_background)
                    binding.edDateOfBirth.background = ContextCompat.getDrawable(requireContext(), R.drawable.edittext_error_background)
                }
                binding.edEmail.text.isEmpty() -> {
                    binding.edFirstName.background = ContextCompat.getDrawable(requireContext(), R.drawable.edittext_background)
                    binding.edDateOfBirth.background = ContextCompat.getDrawable(requireContext(), R.drawable.edittext_background)
                    binding.edEmail.background = ContextCompat.getDrawable(requireContext(), R.drawable.edittext_error_background)
                }
                binding.spGender.selectedItemPosition == 0 -> {
                    binding.edFirstName.background = ContextCompat.getDrawable(requireContext(), R.drawable.edittext_background)
                    binding.edDateOfBirth.background = ContextCompat.getDrawable(requireContext(), R.drawable.edittext_background)
                    binding.edEmail.background = ContextCompat.getDrawable(requireContext(), R.drawable.edittext_background)
                    binding.spGender.background = ContextCompat.getDrawable(requireContext(), R.drawable.edittext_error_background)
                }
                else -> {
                    binding.edFirstName.background = ContextCompat.getDrawable(requireContext(), R.drawable.edittext_background)
                    binding.edDateOfBirth.background = ContextCompat.getDrawable(requireContext(), R.drawable.edittext_background)
                    binding.edEmail.background = ContextCompat.getDrawable(requireContext(), R.drawable.edittext_background)
                    binding.spGender.background = ContextCompat.getDrawable(requireContext(), R.drawable.edittext_background)
                    saveUserData()
                }
            }
        }
    }

    private fun fillUserData() {
        val name = Commons.getSharedPreferences(requireContext()).getString(
            Commons.USERNAME, "")!!
        val phone = Commons.getSharedPreferences(requireContext()).getString(
            Commons.USER_PHONE, "")!!
        val email = Commons.getSharedPreferences(requireContext()).getString(
            Commons.USER_EMAIL, "")!!
        val sex = Commons.getSharedPreferences(requireContext()).getInt(
            Commons.USER_SEX, 0)
        val birth = Commons.getSharedPreferences(requireContext()).getString(
            Commons.USER_BIRTH_DATE, "")!!
        binding.edFirstName.setText(name)
        if (email != "null"){
            binding.edEmail.setText(email)
        }
        if (phone != ""){
            binding.edPhone.setText(phone)
        }
        if (birth != "null"){
            binding.edDateOfBirth.text = birth
        }
        if (sex == 1){
            binding.spGender.setSelection(1)
        } else if (sex == 2){
            binding.spGender.setSelection(2)
        }
    }

    private fun saveUserData() {
        binding.isLoading = true
        name = "${binding.edFirstName.text}"
        sex = if (binding.spGender.selectedItemPosition == 1){
            "male"
        } else {
            "female"
        }
        val update = UpdateProfile(
            name,
            binding.edPhone.text.toString(),
            binding.edEmail.text.toString(),
            sex,
            binding.edDateOfBirth.text.toString(),
        )
        viewModel.updateProfile(update)
    }

    private fun observeProfile() {
        viewModel.dataProfile.observe(viewLifecycleOwner,
            {response ->
                if (response != null){
                    if (response.status == 200){
                        Snackbar.make(requireView(), getString(R.string.profile_update), Snackbar.LENGTH_SHORT).show()
                        Commons.getSharedEditor(requireContext()).putString(
                            Commons.USERNAME, name).apply()
                        Commons.getSharedEditor(requireContext()).putString(
                            Commons.USER_PHONE, binding.edPhone.text.toString()).apply()
                        Commons.getSharedEditor(requireContext()).putString(
                            Commons.USER_EMAIL, binding.edEmail.text.toString()).apply()
                        if (binding.spGender.selectedItemPosition == 1){
                            Commons.getSharedEditor(requireContext()).putInt(
                                Commons.USER_SEX, 1).apply()
                        } else {
                            Commons.getSharedEditor(requireContext()).putInt(
                                Commons.USER_SEX, 2).apply()
                        }
                        Commons.getSharedEditor(requireContext()).putString(
                            Commons.USER_BIRTH_DATE, binding.edDateOfBirth.text.toString()).apply()
                        findNavController().navigateUp()
                    }else{
                        Snackbar.make(requireView(), response.message, Snackbar.LENGTH_SHORT).show()
                    }
                    binding.isLoading = false
                }
            })
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        binding.edDateOfBirth.text = "$dayOfMonth/${month + 1}/$year"
        dateDialog.dismiss()
    }

}