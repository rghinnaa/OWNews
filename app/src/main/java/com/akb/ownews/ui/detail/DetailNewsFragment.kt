package com.akb.ownews.ui.detail

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.akb.ownews.R
import com.akb.ownews.data.local.entity.Bookmark
import com.akb.ownews.data.model.ArticleModel
import com.akb.ownews.databinding.FragmentDetailNewsBinding
import com.akb.ownews.ui.home.HomeFragmentDirections
import com.akb.ownews.utils.Const.textPlain
import com.akb.ownews.utils.dateFormatter
import com.akb.ownews.utils.emptyString
import com.akb.ownews.utils.loadImage
import com.akb.ownews.utils.navController
import com.akb.ownews.utils.navigateOrNull
import com.akb.ownews.utils.textOrNull
import com.akb.ownews.utils.viewBinding
import com.akb.ownews.viewmodel.MainViewModel
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import java.time.Instant

@AndroidEntryPoint
class DetailNewsFragment : Fragment(R.layout.fragment_detail_news) {

    private val binding by viewBinding<FragmentDetailNewsBinding>()
    private val viewModel by viewModels<MainViewModel>()
    private val args by navArgs<DetailNewsFragmentArgs>()

    private val formatDate = "dd MMMM, yyyy"
    private var items = ArticleModel()
    private var itemBookmark = Bookmark()
    private var bookmarkActive = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val data = Gson().fromJson(args.data, ArticleModel::class.java)

        initData(data)
        initBookmarkCallback()
        initOnClick()

    }

    private fun initData(item: ArticleModel) {
        binding.apply {
            items = item

            ivNews.loadImage(item.urlToImage)
            tvTitle.textOrNull = item.title
            tvWriter.textOrNull = item.author
            tvSource.textOrNull = item.source?.name
            tvNews.textOrNull = item.description

            val instant = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Instant.parse(item.publishedAt)
            } else {
                TODO("VERSION.SDK_INT < O")
            }

            val date = java.util.Date.from(instant)

            tvDate.textOrNull = dateFormatter(formatDate).format(date)

            viewModel.getBookmark()
        }
    }

    private fun initBookmarkCallback() {
        viewModel.bookmark.observe(viewLifecycleOwner) { result ->
            result.forEach {
                if(it.title == items.title) {
                    itemBookmark = it
                    bookmarkActive = true
                    binding.ibBookmark.loadImage(R.drawable.ic_bookmark_news_active)
                }
            }
        }
    }

    private fun initShare() {
        val intent = Intent()
        intent.action = Intent.ACTION_SEND
        intent.putExtra(Intent.EXTRA_TEXT, context?.getString(R.string.share_template, items.url))
        intent.type = textPlain
        startActivity(Intent.createChooser(intent, context?.getString(R.string.share_to)))
    }

    private fun initOnClick() {
        binding.apply {
            ibBack.setOnClickListener(onClickCallback)
            btnRead.setOnClickListener(onClickCallback)
            ibShare.setOnClickListener(onClickCallback)
            ibBookmark.setOnClickListener(onClickCallback)
        }
    }

    private val onClickCallback = View.OnClickListener { view ->
        when (view) {
            binding.ibBack -> {
                navController.navigateUp()
            }
            binding.btnRead -> {
                navController.navigateOrNull(
                    DetailNewsFragmentDirections.actionNews(items.url.toString())
                )
            }
            binding.ibShare -> {
                initShare()
            }
            binding.ibBookmark -> {
                if(bookmarkActive) {
                    bookmarkActive = false
                    viewModel.deleteBookmark(itemBookmark.id)
                    binding.ibBookmark.loadImage(R.drawable.ic_bookmark_news)
                } else {
                    bookmarkActive = true
                    viewModel.insertBookmark(
                        Bookmark(
                            source = items.source?.name,
                            title = items.title,
                            description = items.description,
                            author = items.author,
                            urlToImage = items.urlToImage,
                            url = items.url,
                            publishedAt = items.publishedAt,
                            content = items.content
                        )
                    )
                    binding.ibBookmark.loadImage(R.drawable.ic_bookmark_news_active)
                }
            }
        }
    }

}