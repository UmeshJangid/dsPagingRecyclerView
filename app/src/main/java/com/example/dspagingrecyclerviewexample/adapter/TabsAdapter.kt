package com.example.dspagingrecyclerviewexample.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

/**
 * Project : DSPagingRecyclerViewExample
 * Created by Umesh Jangid on 20,April,2021
 * Dotsquares Limited,
 * Jaipur Rajasthan, India.
 */

class TabsAdapter(fm: FragmentManager?, var mNumOfTabs: Int) : FragmentStatePagerAdapter(
    fm!!
) {
    override fun getCount(): Int {
        return mNumOfTabs
    }

    override fun getItem(position: Int): Fragment {
        TODO("Not yet implemented")
    }

    /*  override fun getItem(position: Int): Fragment {
          return when (position) {
              0 ->                 //HomeFragment home = new HomeFragment();
                  // return home;
                  null
              1 ->                 // AboutFragment about = new AboutFragment();
                  // return about;
                  null
              2 ->                 // ContactFragment contact = new ContactFragment();
                  // return contact;
                  null
              else -> null
          }
      }*/
}