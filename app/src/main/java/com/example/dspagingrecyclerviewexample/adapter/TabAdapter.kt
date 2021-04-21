package com.example.dspagingrecyclerviewexample.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import java.util.*
/**
 * Project : DSPagingRecyclerViewExample
 * Created by Umesh Jangid on 21,April,2021
 * Dotsquares Limited,
 * Jaipur Rajasthan, India.
 */
class TabAdapter internal constructor(fm: FragmentManager?) : FragmentStatePagerAdapter(
    fm!!
) {
    private val mFragmentList: MutableList<Fragment> = ArrayList()
    private val mFragmentTitleList: MutableList<String> = ArrayList()
    override fun getItem(position: Int): Fragment {
        return mFragmentList[position]
    }

    fun addFragment(fragment: Fragment, title: String) {
        mFragmentList.add(fragment)
        mFragmentTitleList.add(title)
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return mFragmentTitleList[position]
    }

    override fun getCount(): Int {
        return mFragmentList.size
    }
}