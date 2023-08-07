package com.akb.ownews.ui.onboarding

import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.akb.ownews.R
import com.akb.ownews.data.model.OnBoardingModel
import com.akb.ownews.utils.Const
import com.google.gson.Gson

class OnBoardingPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    private val pagesContent = arrayOf(
        OnBoardingModel(
            R.drawable.ic_on_boarding_1,
            R.string.on_boarding_title_1,
            R.string.on_boarding_desc_1
        ),
        OnBoardingModel(
            R.drawable.ic_on_boarding_2,
            R.string.on_boarding_title_2,
            R.string.on_boarding_desc_2
        ),
        OnBoardingModel(
            R.drawable.ic_on_boarding_3,
            R.string.on_boarding_title_3,
            R.string.on_boarding_desc_3
        )
    )

    override fun getItemCount(): Int = pagesContent.size

    override fun createFragment(position: Int): Fragment = OnBoardingFragment().apply {
        arguments = bundleOf(Const.ON_BOARDING_DATA to Gson().toJson(pagesContent[position]))
    }

}