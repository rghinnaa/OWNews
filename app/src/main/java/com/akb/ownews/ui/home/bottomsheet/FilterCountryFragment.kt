package com.akb.ownews.ui.home.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.akb.ownews.R
import com.akb.ownews.data.model.FilterCountryModel
import com.akb.ownews.databinding.FragmentFilterCountryBinding
import com.akb.ownews.utils.DataCollection
import com.akb.ownews.utils.viewBinding
import com.akb.ownews.viewmodel.MainViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FilterCountryFragment : BottomSheetDialogFragment() {

    private val binding by viewBinding<FragmentFilterCountryBinding>()
    private val viewModel by activityViewModels<MainViewModel>()
    private val args by navArgs<FilterCountryFragmentArgs>()

    private val countryAdapter = FilterCountryAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        return LayoutInflater.from(context).inflate(
            R.layout.fragment_filter_country,
            container,
            false
        )
    }

    override fun getTheme() = R.style.BottomSheetTheme

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()

    }

    private fun initView() {
        binding.rvFilterCountry.adapter = countryAdapter

        countryAdapter.differ.submitList(DataCollection.country)
        countryAdapter.setChosen(args.countryFilter.orEmpty())

        initAdapterListener()
        initOnClick()
    }

    private fun initAdapterListener() {
        countryAdapter.setOnItemClickListener {
            viewModel.setCountry(FilterCountryModel(it, true))
            dismiss()
        }
    }

    private fun initOnClick() {
        binding.apply {
            ibClose.setOnClickListener(onClickCallback)
        }
    }

    private val onClickCallback = View.OnClickListener { view ->
        when (view) {
            binding.ibClose -> dismiss()
        }
    }

}