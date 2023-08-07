package com.akb.ownews.ui.onboarding

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.akb.ownews.R
import com.akb.ownews.databinding.FragmentOnBoardingContainerBinding
import com.akb.ownews.ui.main.MainActivity
import com.akb.ownews.utils.viewBinding
import com.google.android.material.tabs.TabLayoutMediator

class OnBoardingContainerFragment : Fragment(R.layout.fragment_on_boarding_container) {

    private val binding by viewBinding<FragmentOnBoardingContainerBinding>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.vpOnBoarding.apply {
            adapter = OnBoardingPagerAdapter(this@OnBoardingContainerFragment)
            TabLayoutMediator(
                binding.tlOnBoarding,
                this
            ) { tab, position ->
            }.attach()
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)

                    initSetBtnPrevious()
                    initSetBtnSkip()
                }
            })
        }

        initSetBtnPrevious()
        initSetBtnSkip()

        initOnClick()

    }


    private fun initSetBtnPrevious() {
        binding.btnPrevious.isVisible = binding.vpOnBoarding.currentItem != 0
    }

    private fun initSetBtnSkip() {
        binding.btnSkip.isVisible = binding.vpOnBoarding.currentItem != 2
    }

    private fun initOnClick() {
        binding.btnPrevious.setOnClickListener(onClickCallback)
        binding.btnNext.setOnClickListener(onClickCallback)
        binding.btnSkip.setOnClickListener(onClickCallback)
    }

    private val onClickCallback = View.OnClickListener { view ->
        when (view) {
            binding.btnPrevious -> {
                binding.vpOnBoarding.setCurrentItem(
                    binding.vpOnBoarding.currentItem.minus(1),
                    true
                )

                initSetBtnPrevious()
                initSetBtnSkip()
            }
            binding.btnNext -> {
                if (binding.vpOnBoarding.currentItem == 2) {
                    startActivity(Intent(requireContext(), MainActivity::class.java))
                } else binding.vpOnBoarding.setCurrentItem(
                    binding.vpOnBoarding.currentItem.plus(1),
                    true
                )

                initSetBtnPrevious()
                initSetBtnSkip()
            }
            binding.btnSkip -> {
                binding.vpOnBoarding.setCurrentItem(2, true)
                initSetBtnPrevious()
                initSetBtnSkip()
            }
        }
    }

}