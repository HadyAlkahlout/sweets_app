package com.raiyansoft.sweetsapp.ui.fragments.main.drawerNav

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.raiyansoft.sweetsapp.R
import com.raiyansoft.sweetsapp.databinding.FragmentCallBinding
import com.raiyansoft.sweetsapp.models.appInfo.ContactUs
import com.raiyansoft.sweetsapp.ui.viewmodel.navDrawer.ContactViewModel

class CallFragment : Fragment() {

    private lateinit var binding: FragmentCallBinding

    private val viewModel by lazy {
        ViewModelProvider(this)[ContactViewModel::class.java]
    }

    var whatsNum = ""
    var instagramLink = ""
    var twitterLink = ""
    var facebookLink = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCallBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        doInitialization()
    }

    private fun doInitialization() {
        binding.btnSend.setOnClickListener {
            if (binding.edReason.text.isEmpty()){
                binding.edReason.background = ContextCompat.getDrawable(requireContext(), R.drawable.edittext_error_background)
            } else if (binding.edMessage.text.isEmpty()){
                binding.edReason.background = ContextCompat.getDrawable(requireContext(), R.drawable.edittext_background)
                binding.edMessage.background = ContextCompat.getDrawable(requireContext(), R.drawable.edittext_error_background)
            } else {
                binding.edReason.background = ContextCompat.getDrawable(requireContext(), R.drawable.edittext_background)
                binding.edMessage.background = ContextCompat.getDrawable(requireContext(), R.drawable.edittext_background)
                binding.pbLoading.visibility = View.VISIBLE
                sendMessage(binding.edReason.text.toString().trim(), binding.edMessage.text.toString().trim())
            }
        }
        getPagesLinks()
        val i = Intent(Intent.ACTION_VIEW)
        binding.imgBack.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.imgWhatsUp.setOnClickListener {
            val url = "https://api.whatsapp.com/send?phone=$whatsNum"
            i.data = Uri.parse(url)
            startActivity(i)
        }
        binding.imgInstagram.setOnClickListener {
            if (instagramLink.startsWith("http")){
                i.data = Uri.parse(instagramLink)
            } else {
                val url = "https://www.$instagramLink"
                i.data = Uri.parse(url)
            }
            startActivity(i)
        }
        binding.imgTwitter.setOnClickListener {
            if (instagramLink.startsWith("http")){
                i.data = Uri.parse(twitterLink)
            } else {
                val url = "https://www.$twitterLink"
                i.data = Uri.parse(url)
            }
            startActivity(i)
        }
        binding.imgChat.setOnClickListener {
            if (instagramLink.startsWith("http")){
                i.data = Uri.parse(facebookLink)
            } else {
                val url = "https://www.$facebookLink"
                i.data = Uri.parse(url)
            }
            startActivity(i)
        }
    }

    private fun getPagesLinks() {
        viewModel.getPages()
        viewModel.dataPages.observe(viewLifecycleOwner,
            {response ->
                if (response != null){
                    whatsNum = response.data.whatsapp
                    instagramLink = response.data.insta
                    twitterLink = response.data.twitter
                    facebookLink = response.data.fb
                }

            })
    }

    private fun sendMessage(reason: String, message: String) {
        val contact = ContactUs(reason, message)
        viewModel.contactUs(contact)
        viewModel.dataContact.observe(viewLifecycleOwner,
            {response ->
                if (response != null){
                    binding.pbLoading.visibility = View.GONE
                    if (response.status == 200){
                        Snackbar.make(requireView(), getString(R.string.call_sent), Snackbar.LENGTH_SHORT).show()
                        findNavController().navigateUp()
                    } else {
                        Snackbar.make(requireView(), response.message, Snackbar.LENGTH_SHORT).show()
                    }
                }
            })
    }

}