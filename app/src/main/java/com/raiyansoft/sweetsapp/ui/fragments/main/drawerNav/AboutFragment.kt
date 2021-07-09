package com.raiyansoft.sweetsapp.ui.fragments.main.drawerNav

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.raiyansoft.sweetsapp.databinding.FragmentAboutBinding
import com.raiyansoft.sweetsapp.ui.viewmodel.navDrawer.AboutViewModel

class AboutFragment : Fragment() {

    private lateinit var binding: FragmentAboutBinding

    private val viewModel by lazy {
        ViewModelProvider(this)[AboutViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAboutBinding.inflate(layoutInflater)
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
        viewModel.getAbout()
        viewModel.dataAbout.observe(viewLifecycleOwner,
            {
                if(it.status == 200){
                    binding.tvAbout.text = it.data.about_us
                }else{
                    Snackbar.make(requireView(), it.message, Snackbar.LENGTH_SHORT).show()
                    findNavController().navigateUp()
                }
            })
    }

}