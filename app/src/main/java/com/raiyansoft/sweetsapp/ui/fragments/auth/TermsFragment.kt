package com.raiyansoft.sweetsapp.ui.fragments.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.raiyansoft.sweetsapp.databinding.FragmentTermsBinding
import com.raiyansoft.sweetsapp.ui.viewmodel.navDrawer.TermsViewModel

class TermsFragment : Fragment() {

    private lateinit var binding: FragmentTermsBinding

    private val viewModel by lazy {
        ViewModelProvider(this)[TermsViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTermsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        doInitialization()
    }

    private fun doInitialization() {
        binding.imgBack.setOnClickListener {
            findNavController().navigateUp()
        }
        viewModel.getTerms()
        viewModel.dataTerms.observe(viewLifecycleOwner,
            {
                if(it.status == 200){
                    binding.tvTerms.text = it.data.terms
                }else{
                    Snackbar.make(requireView(), it.message, Snackbar.LENGTH_SHORT).show()
                    findNavController().navigateUp()
                }
            })
    }

}