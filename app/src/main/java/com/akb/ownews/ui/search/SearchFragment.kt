package com.akb.ownews.ui.search

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import com.akb.ownews.R
import com.akb.ownews.data.model.SourcesModel
import com.akb.ownews.data.remote.Result
import com.akb.ownews.databinding.FragmentSearchBinding
import com.akb.ownews.ui.PagingLoadStateAdapter
import com.akb.ownews.utils.Const
import com.akb.ownews.utils.emptyString
import com.akb.ownews.utils.hideKeyboard
import com.akb.ownews.utils.livevent.EventObserver
import com.akb.ownews.utils.navController
import com.akb.ownews.utils.navigateOrNull
import com.akb.ownews.utils.textOrNull
import com.akb.ownews.utils.viewBinding
import com.akb.ownews.viewmodel.MainViewModel
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class SearchFragment : Fragment(R.layout.fragment_search) {

    val binding by viewBinding<FragmentSearchBinding>()
    private val viewModel by activityViewModels<MainViewModel>()

    private val searchAdapter = SearchPagingAdapter()
    private var sourcesModel = SourcesModel()

    private var search = emptyString
    private var country = emptyString
    private var category = emptyString
    private var sources = emptyString

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvSearch.apply {
            isNestedScrollingEnabled = true
            adapter = searchAdapter.withLoadStateFooter(
                PagingLoadStateAdapter { searchAdapter.retry() }
            )
        }

        country = viewModel.country

        initViewModel()
        initViewModelCallback()
        initPagingAdapter()
        initAdapterListener()
        initSearchImeOption()
        initSearchCallback()
        initOnClick()

    }

    private fun initAdapterListener() {
        searchAdapter.setOnItemClickListener {
            if(view != null) {
                navController.navigateOrNull(
                    SearchFragmentDirections.actionDetailNews(Gson().toJson(it))
                )
            }
        }
        searchAdapter.setOnShareClickListener {
            initShare(it)
        }
    }

    private fun initViewModel() {
        viewModel.requestSource(viewModel.country)
    }

    private fun initViewModelCallback() {
        initSourceCallback()
        initFilterCategoryCallback()
        initFilterSourceCallback()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initSearchCallback() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.requestHeadlinePaging(
                country = country,
                category = category,
                sources = sources,
                search = search
            ).observe(viewLifecycleOwner) { result ->
                searchAdapter.submitData(lifecycle, result)
                searchAdapter.notifyDataSetChanged()
            }
        }
    }

    private fun initSourceCallback() {
        viewModel.source.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Loading -> {}
                is Result.Success -> {
                    sourcesModel = SourcesModel(result.results.orEmpty())
                }

                is Result.Error<*> -> {}
                else -> {}
            }
        }
    }

    private fun initFilterCategoryCallback() {
        viewModel.filterCategory.observe(viewLifecycleOwner, EventObserver { result ->
            if (result.state == true) {
                country = viewModel.country
                sources = emptyString
                category = result.category?.slug.toString()
                binding.tvCategory.textOrNull = if (category != emptyString) category
                else binding.root.context.getString(R.string.category)
                binding.tvSource.textOrNull = if (sources != emptyString) sources
                else binding.root.context.getString(R.string.source)
                initSearchCallback()

                viewModel.setCategoryNothing()
            }
        })
    }

    private fun initFilterSourceCallback() {
        viewModel.filterSource.observe(viewLifecycleOwner, EventObserver { result ->
            if (result.state == true) {
                country = emptyString
                category = emptyString
                sources = result.source?.id.toString()
                binding.tvCategory.textOrNull = if (category != emptyString) category
                else binding.root.context.getString(R.string.category)
                binding.tvSource.textOrNull = if (sources != emptyString) sources
                else binding.root.context.getString(R.string.source)
                initSearchCallback()

                viewModel.setSourceNothing()
            }
        })
    }

    private fun initPagingAdapter() {
        searchAdapter.addLoadStateListener { loadState ->
            when (loadState.source.refresh) {
                is LoadState.Loading -> {
                    binding.rvSearch.isVisible = false
                    binding.shimmerSearch.isVisible = true
                    binding.clEmptySearch.isVisible = false
                }

                is LoadState.NotLoading -> {
                    binding.rvSearch.isVisible = true
                    binding.shimmerSearch.isVisible = false
                    binding.clEmptySearch.isVisible = false
                }

                is LoadState.Error -> {
                    binding.rvSearch.isVisible = false
                    binding.shimmerSearch.isVisible = false
                    binding.clEmptySearch.isVisible = true
                }
            }

            if (loadState.append.endOfPaginationReached) {
                if (searchAdapter.itemCount < 1) {
                    binding.rvSearch.isVisible = false
                    binding.shimmerSearch.isVisible = false
                    binding.clEmptySearch.isVisible = true
                }
            }
        }
    }

    private fun initSearchImeOption() {
        binding.etSearch.apply {
            clearFocus()
            setOnEditorActionListener(onImeSearchClicked)
        }
    }

    private fun searchEvent(v: TextView, keyword: String) {
        activity?.hideKeyboard(v)
        setCondition(keyword, country, category, sources)
        initSearchCallback()
    }

    private fun setCondition(
        searchText: String,
        countryText: String,
        categoryText: String,
        sourcesText: String
    ) {
        search = searchText
        country = countryText
        category = categoryText
        sources = sourcesText
    }

    private fun initShare(url: String) {
        val intent = Intent()
        intent.action = Intent.ACTION_SEND
        intent.putExtra(Intent.EXTRA_TEXT, context?.getString(R.string.share_template, url))
        intent.type = Const.textPlain
        startActivity(Intent.createChooser(intent, context?.getString(R.string.share_to)))
    }

    private fun initOnClick() {
        binding.apply {
            cvCategory.setOnClickListener(onClickCallback)
            cvSource.setOnClickListener(onClickCallback)
        }
    }

    private val onClickCallback = View.OnClickListener { view ->
        when (view) {
            binding.cvCategory -> {
                navController.navigateOrNull(
                    SearchFragmentDirections.actionFilterCategory(category)
                )
            }

            binding.cvSource -> {
                if (sourcesModel.sources?.firstOrNull()?.name != null) {
                    navController.navigateOrNull(
                        SearchFragmentDirections.actionFilterSource(
                            Gson().toJson(sourcesModel),
                            sources
                        )
                    )
                }
            }
        }
    }

    private val onImeSearchClicked = TextView.OnEditorActionListener { v, actionId, _ ->
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            val searchText = binding.etSearch.text.toString()
            searchEvent(v, searchText)

            return@OnEditorActionListener true
        }
        false
    }

}