package com.akb.ownews.ui.onboarding

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.akb.ownews.R
import com.akb.ownews.data.model.OnBoardingModel
import com.akb.ownews.databinding.FragmentOnBoardingBinding
import com.akb.ownews.utils.Const
import com.akb.ownews.utils.loadImage
import com.akb.ownews.utils.textOrNull
import com.akb.ownews.utils.viewBinding
import com.google.gson.Gson

class OnBoardingFragment : Fragment(R.layout.fragment_on_boarding) {

    private val binding by viewBinding<FragmentOnBoardingBinding>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val data = Gson().fromJson(arguments?.getString(Const.ON_BOARDING_DATA), OnBoardingModel::class.java)

        binding.ivOnBoarding.loadImage(data.image)
        binding.tvTitle.textOrNull = context?.resources?.getString(data.title)
        binding.tvDesc.textOrNull = context?.resources?.getString(data.description)

    }

}