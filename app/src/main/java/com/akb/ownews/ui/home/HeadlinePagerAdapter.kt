package com.akb.ownews.ui.home

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import javax.inject.Inject

class HeadlinePagerAdapter @Inject constructor(manager: Fragment) : FragmentStateAdapter(manager) {

    private val fragments = listOf<Fragment>(HeadlineFragment.newInstance())

    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment = fragments[position]

}