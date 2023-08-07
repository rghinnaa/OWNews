package com.akb.ownews.ui.search.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.navArgs
import com.akb.ownews.R
import com.akb.ownews.data.model.FilterCategoryModel
import com.akb.ownews.databinding.FragmentFilterCategoryBinding
import com.akb.ownews.ui.home.CategoryAdapter
import com.akb.ownews.utils.DataCollection
import com.akb.ownews.utils.viewBinding
import com.akb.ownews.viewmodel.MainViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FilterCategoryFragment : BottomSheetDialogFragment() {

    private val binding by viewBinding<FragmentFilterCategoryBinding>()
    private val viewModel by activityViewModels<MainViewModel>()
    private val args by navArgs<FilterCategoryFragmentArgs>()

    private val categoryAdapter = FilterCategoryAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        return LayoutInflater.from(context).inflate(
            R.layout.fragment_filter_category,
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
        binding.rvFilterCategory.adapter = categoryAdapter

        categoryAdapter.differ.submitList(DataCollection.category)
        categoryAdapter.setChosen(args.categoryFilter.orEmpty())

        initAdapterListener()
        initOnClick()
    }

    private fun initAdapterListener() {
        categoryAdapter.setOnItemClickListener {
            viewModel.setCategory(FilterCategoryModel(it, true))
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