package com.raiyansoft.sweetsapp.ui.fragments.main.shop

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.raiyansoft.sweetsapp.adapters.RatesAdapter
import com.raiyansoft.sweetsapp.databinding.FragmentReviewBinding
import com.raiyansoft.sweetsapp.models.home.Store
import com.raiyansoft.sweetsapp.ui.viewmodel.review.ReviewViewModel

class ReviewFragment : Fragment() {

    private lateinit var binding: FragmentReviewBinding
    private lateinit var adapter: RatesAdapter

    private lateinit var shop: Store

    private val viewModel by lazy {
        ViewModelProvider(this)[ReviewViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentReviewBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        doInitialization()
    }

    private fun doInitialization() {

        if (arguments != null) {
            val args = ReviewFragmentArgs.fromBundle(requireArguments())
            binding.store = args.shop
            shop = args.shop
        } else {
            findNavController().navigateUp()
        }
        viewModel.getRates(shop.id)

        binding.imgBack.setOnClickListener {
            findNavController().navigateUp()
        }

        adapter = RatesAdapter()
        binding.rcRates.adapter = adapter
        fillRates()

    }

    private fun fillRates() {
        viewModel.dataReview.observe(viewLifecycleOwner,
            { response ->
                if (response != null) {
                    adapter.data.addAll(response.data.rates)
                    adapter.notifyDataSetChanged()
                }
            })
    }
}