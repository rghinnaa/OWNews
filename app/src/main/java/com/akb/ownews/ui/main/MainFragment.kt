package com.akb.ownews.ui.main

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.akb.ownews.R
import com.akb.ownews.databinding.FragmentMainBinding
import com.akb.ownews.utils.BackButtonBehaviour
import com.akb.ownews.utils.setupWithNavController
import com.akb.ownews.utils.textOrNull
import com.akb.ownews.utils.viewBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : Fragment(R.layout.fragment_main) {

    private val bottomNavSelectedItemIdKey = "BOTTOM_NAV_SELECTED_ITEM_ID_KEY"
    private var bottomNavSelectedItemId = R.id.home

    private val binding by viewBinding<FragmentMainBinding>()

    private val resToolbarId = mutableListOf(
        R.id.home_fragment,
        R.id.search_fragment,
        R.id.bookmark_fragment,
        R.id.detail_news_fragment,
        R.id.news_fragment,
        R.id.filter_category_fragment,
        R.id.filter_source_fragment,
        R.id.filter_country_fragment
    )
    private val resNavigationId = mutableListOf(
        R.id.detail_news_fragment,
        R.id.news_fragment,
        R.id.filter_category_fragment,
        R.id.filter_source_fragment,
        R.id.filter_country_fragment
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        savedInstanceState?.let {
            bottomNavSelectedItemId = it.getInt(bottomNavSelectedItemIdKey, bottomNavSelectedItemId)
        }

        setUpBottomNavBar()
    }

    private fun setUpBottomNavBar() {
        binding.bottomNavigation.selectedItemId = bottomNavSelectedItemId
        binding.bottomNavigation.itemIconTintList = null

        val navGraphIds = listOf(
            R.navigation.nav_home_graph,
            R.navigation.nav_search_graph,
            R.navigation.nav_bookmark_graph
        )

        val controller = binding.bottomNavigation.setupWithNavController(
            fragmentManager = childFragmentManager,
            navGraphIds = navGraphIds,
            backButtonBehaviour = BackButtonBehaviour.POP_HOST_FRAGMENT,
            containerId = binding.navHostContainer.id,
            firstItemId = R.id.home,
            intent = requireActivity().intent
        )

        controller.observe(viewLifecycleOwner) { navController ->
            NavigationUI.setupWithNavController(binding.toolbar, navController)
            binding.tvToolbar.textOrNull = binding.toolbar.title
            bottomNavSelectedItemId = navController.graph.id

            navController.addOnDestinationChangedListener { _, destination, _ ->
                binding.tvToolbar.isVisible = !resToolbarId.any { resId ->
                    destination.id == resId
                }

                binding.bottomNavigation.isVisible = !resNavigationId.any { resId ->
                    destination.id == resId
                }
            }
        }
    }

    companion object {
        val Fragment?.parentNavigation: BottomNavigationView?
            get() {
                val fragment = if (this?.parentFragment is NavHostFragment) {
                    (parentFragment as? NavHostFragment)?.parentFragment as? MainFragment
                } else null

                return try {
                    fragment?.binding?.bottomNavigation
                } catch (e: Exception) {
                    null
                }
            }
    }

}