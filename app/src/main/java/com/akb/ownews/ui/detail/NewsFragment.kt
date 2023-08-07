package com.akb.ownews.ui.detail

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.akb.ownews.R
import com.akb.ownews.databinding.FragmentNewsBinding
import com.akb.ownews.utils.navController
import com.akb.ownews.utils.viewBinding

class NewsFragment : Fragment(R.layout.fragment_news) {

    private val binding by viewBinding<FragmentNewsBinding>()
    private val args by navArgs<NewsFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()

    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initView() {
        binding.apply {
            wvNews.webViewClient = WebViewClient()
            wvNews.loadUrl(args.url)
            wvNews.settings.javaScriptEnabled = true
            wvNews.settings.setSupportZoom(true)

            wvNews.setOnKeyListener(object : View.OnKeyListener {
                override fun onKey(v: View?, keyCode: Int, event: KeyEvent?): Boolean {
                    if (keyCode == KeyEvent.KEYCODE_BACK && wvNews.canGoBack()) {
                        wvNews.goBack()
                        return true
                    }
                    return false
                }
            })

            ibBack.setOnClickListener {
                navController.navigateUp()
            }
        }
    }

}