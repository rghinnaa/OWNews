package com.akb.ownews.ui.home

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.akb.ownews.R
import com.akb.ownews.data.model.ArticleModel
import com.akb.ownews.data.model.FilterCategoryModel
import com.akb.ownews.data.remote.Result
import com.akb.ownews.databinding.FragmentHomeBinding
import com.akb.ownews.ui.main.MainFragment.Companion.parentNavigation
import com.akb.ownews.utils.DataCollection
import com.akb.ownews.utils.dateFormatter
import com.akb.ownews.utils.emptyString
import com.akb.ownews.utils.livevent.EventObserver
import com.akb.ownews.utils.loadImage
import com.akb.ownews.utils.navController
import com.akb.ownews.utils.navigateOrNull
import com.akb.ownews.utils.textOrNull
import com.akb.ownews.utils.viewBinding
import com.akb.ownews.viewmodel.MainViewModel
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.Calendar
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    private val binding by viewBinding<FragmentHomeBinding>()
    private val viewModel by activityViewModels<MainViewModel>()

    private val categoryAdapter = CategoryAdapter()
    private var item = ArticleModel()

    private val formatDate = "dd MMMM, yyyy"
    private var country = "us"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initViewModel()
        initViewModelCallback()
        initPagerPagination()
        initOnClick()

    }

    private fun initView() {
        country = viewModel.country
        binding.tvCountry.textOrNull = if(country != "us") country
        else binding.root.context.getString(R.string.country)

        val date = getCurrentDateTime()
        val currentTime = date.toString("MMMM dd")
        binding.tvDay.textOrNull = "Today, $currentTime"

        initAdapter()
        initAdapterListener()
    }

    private fun initViewModel() {
        viewModel.requestTrending(country)
    }

    private fun initViewModelCallback() {
        initTrendingCallback()
        initFilterCountryCallback()
    }

    private fun initAdapter() {
        binding.rvCategory.adapter = categoryAdapter

        categoryAdapter.differ.submitList(DataCollection.category)
    }

    private fun initAdapterListener() {
        categoryAdapter.setOnItemClickListener {
            viewModel.setCategory(FilterCategoryModel(it, true))
            parentNavigation?.selectedItemId = R.id.search
        }
    }

    private fun initPagerPagination() {
        binding.vpHeadlinePagination.adapter = HeadlinePagerAdapter(this@HomeFragment)
    }

    private fun initTrendingCallback() {
        viewModel.trending.observe(viewLifecycleOwner) { result ->
            when(result) {
                is Result.Loading -> {
                    binding.shimmerHome.isVisible = true
                    binding.clHome.isVisible = false
                }
                is Result.Success -> {
                    binding.shimmerHome.isVisible = false
                    binding.clHome.isVisible = true

                    item = result.results?.firstOrNull() ?: ArticleModel()

                    binding.ivTrending.loadImage(item.urlToImage)
                    binding.tvTrendingTitle.textOrNull = item.title
                    binding.tvTrendingSource.textOrNull = item.source?.name

                    val instant = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        Instant.parse(item.publishedAt)
                    } else {
                        TODO("VERSION.SDK_INT < O")
                    }

                    val date = java.util.Date.from(instant)

                    binding.tvTrendingDate.textOrNull = dateFormatter(formatDate).format(date)
                }
                is Result.Error<*> -> {}
                else -> {}
            }
        }
    }

    private fun initFilterCountryCallback() {
        viewModel.filterCountry.observe(viewLifecycleOwner, EventObserver { result ->
            if(result.state == true) {
                country = result.country.toString()
                viewModel.country = country
                binding.tvCountry.textOrNull = country
                initViewModel()
                initPagerPagination()

                viewModel.setCountryNothing()
            }
        })
    }

    private fun Date.toString(format: String, locale: Locale = Locale.getDefault()): String {
        val formatter = SimpleDateFormat(format, locale)
        return formatter.format(this)
    }

    private fun getCurrentDateTime(): Date {
        return Calendar.getInstance().time
    }

    private fun initOnClick() {
        binding.apply {
            ivTrending.setOnClickListener(onClickCallback)
            cvCountry.setOnClickListener(onClickCallback)
        }
    }

    private val onClickCallback = View.OnClickListener { view ->
        when (view) {
            binding.ivTrending -> {
                navController.navigateOrNull(
                    HomeFragmentDirections.actionDetailNews(Gson().toJson(item))
                )
            }
            binding.cvCountry -> {
                navController.navigateOrNull(
                    HomeFragmentDirections.actionFilterCountry(country)
                )
            }
        }
    }

}