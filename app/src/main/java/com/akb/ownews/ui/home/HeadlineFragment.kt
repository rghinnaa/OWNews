package com.akb.ownews.ui.home

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.akb.ownews.R
import com.akb.ownews.databinding.FragmentHeadlineBinding
import com.akb.ownews.ui.PagingLoadStateAdapter
import com.akb.ownews.utils.Const
import com.akb.ownews.utils.navController
import com.akb.ownews.utils.navigateOrNull
import com.akb.ownews.utils.viewBinding
import com.akb.ownews.viewmodel.MainViewModel
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HeadlineFragment : Fragment(R.layout.fragment_headline) {

    private val viewModel by activityViewModels<MainViewModel>()
    private val binding by viewBinding<FragmentHeadlineBinding>()

    private val newsAdapter = NewsPagingAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAdapterListener()
        initHeadlineCallback()

    }

    private fun initAdapterListener() {
        newsAdapter.setOnItemClickListener {
            navController.navigateOrNull(
                HomeFragmentDirections.actionDetailNews(Gson().toJson(it))
            )
        }
        newsAdapter.setOnShareClickListener {
            initShare(it)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initHeadlineCallback() {
        binding.rvNews.apply {
            adapter = newsAdapter.withLoadStateFooter(
                PagingLoadStateAdapter { newsAdapter.retry() }
            )
        }

        parentFragment?.viewLifecycleOwner?.let {
            viewModel.requestHeadlinePaging(country = viewModel.country).observe(it) { result ->
                newsAdapter.submitData(it.lifecycle, result)
                newsAdapter.notifyDataSetChanged()
            }
        }
    }

    private fun initShare(url: String) {
        val intent = Intent()
        intent.action = Intent.ACTION_SEND
        intent.putExtra(Intent.EXTRA_TEXT, context?.getString(R.string.share_template, url))
        intent.type = Const.textPlain
        startActivity(Intent.createChooser(intent, context?.getString(R.string.share_to)))
    }

    companion object {
        fun newInstance() = HeadlineFragment().apply {
            arguments = Bundle()
        }
    }

}