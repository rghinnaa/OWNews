package com.akb.ownews.ui.bookmark

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.akb.ownews.R
import com.akb.ownews.data.model.ArticleModel
import com.akb.ownews.databinding.FragmentBookmarkBinding
import com.akb.ownews.utils.emptyString
import com.akb.ownews.utils.hideKeyboard
import com.akb.ownews.utils.navController
import com.akb.ownews.utils.navigateOrNull
import com.akb.ownews.utils.viewBinding
import com.akb.ownews.viewmodel.MainViewModel
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BookmarkFragment : Fragment(R.layout.fragment_bookmark) {

    private val binding by viewBinding<FragmentBookmarkBinding>()
    private val viewModel by viewModels<MainViewModel>()

    private val bookmarkAdapter = BookmarkAdapter()
    private var search = emptyString

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initViewModel()
        initBookmarkCallback()
        initSearchBookmarkCallback()

    }

    private fun initView() {
        binding.rvBookmark.adapter = bookmarkAdapter

        initSearchImeOption()
        iniAdapterListener()
        initOnClick()
    }

    private fun iniAdapterListener() {
        bookmarkAdapter.setOnBookmarkClickListener {
            viewModel.deleteBookmark(it)
            viewModel.getBookmark()
        }
        bookmarkAdapter.setOnItemClickListener {
            val data = ArticleModel(
                ArticleModel.Source(null, it.source),
                it.author,
                it.title,
                it.description,
                it.url,
                it.urlToImage,
                it.publishedAt,
                it.content
            )
            navController.navigateOrNull(
                BookmarkFragmentDirections.actionDetailNews(Gson().toJson(data))
            )
        }
    }

    private fun initViewModel() {
        viewModel.getBookmark()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initBookmarkCallback() {
        viewModel.bookmark.observe(viewLifecycleOwner) { result ->
            if(result.isNotEmpty()) {
                binding.rvBookmark.isVisible = true
                binding.clEmptyBookmark.isVisible = false

                bookmarkAdapter.differ.submitList(result)
                bookmarkAdapter.notifyDataSetChanged()
            } else {
                binding.rvBookmark.isVisible = false
                binding.clEmptyBookmark.isVisible = true
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initSearchBookmarkCallback() {
        viewModel.searchBookmark.observe(viewLifecycleOwner) { result ->
            bookmarkAdapter.differ.submitList(result)
            bookmarkAdapter.notifyDataSetChanged()
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
        search = keyword
        if(search == emptyString) viewModel.getBookmark()
        else viewModel.searchBookmark("%$search%")
    }

    private fun initOnClick() {
        binding.apply {
        }
    }

    private val onClickCallback = View.OnClickListener { view ->
        when (view) {

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