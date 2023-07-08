package com.cmc.recipe.ui.fragment

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class RefrigeratorViewAdapter(fm: FragmentManager,
                              lifecycle: Lifecycle, private val fragmentList: ArrayList<Fragment>) : FragmentStateAdapter(fm,lifecycle){

    override fun getItemCount(): Int = fragmentList.size

    override fun createFragment(position: Int): Fragment {
        return fragmentList[position]
    }
}