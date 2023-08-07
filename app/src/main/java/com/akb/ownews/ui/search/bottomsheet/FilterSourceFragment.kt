package com.akb.ownews.ui.search.bottomsheet

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.navArgs
import com.akb.ownews.R
import com.akb.ownews.data.model.ArticleModel
import com.akb.ownews.data.model.FilterSourceModel
import com.akb.ownews.data.model.SourcesModel
import com.akb.ownews.data.remote.Result
import com.akb.ownews.databinding.FragmentFilterSourceBinding
import com.akb.ownews.utils.viewBinding
import com.akb.ownews.viewmodel.MainViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FilterSourceFragment : BottomSheetDialogFragment() {

    private val binding by viewBinding<FragmentFilterSourceBinding>()
    private val viewModel by activityViewModels<MainViewModel>()
    private val args by navArgs<FilterSourceFragmentArgs>()

    private val sourceAdapter = FilterSourceAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        return LayoutInflater.from(context).inflate(
            R.layout.fragment_filter_source,
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
        val data = Gson().fromJson(args.sources, SourcesModel::class.java)

        binding.rvFilterSource.adapter = sourceAdapter
        sourceAdapter.differ.submitList(data.sources)
        sourceAdapter.setChosen(args.sourceFilter.toString())

        initAdapterListener()
        initOnClick()
    }

    private fun initAdapterListener() {
        sourceAdapter.setOnItemClickListener {
            Log.e("yyy", "apakah? $it")
            viewModel.setSource(FilterSourceModel(it, true))
            dismiss()
        }
    }

    private fun initOnClick() {
        binding.apply {
            binding.ibClose.setOnClickListener(onClickCallback)
        }
    }

    private val onClickCallback = View.OnClickListener { view ->
        when (view) {
            binding.ibClose -> dismiss()
        }
    }
}