package com.msc.libcommon.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter

import com.msc.libcommon.base.BaseFragment

/**
 *
 * Fragments适配器
 * @name ResourcePagerAdapter
 */
class FragmentAdapter(fm: FragmentManager, private val mFragments: List<BaseFragment>?) : FragmentStatePagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return mFragments!![position]
    }

    override fun getCount(): Int {
        return mFragments?.size ?: 0
    }

    override fun getItemPosition(`object`: Any): Int {
        return super.getItemPosition(`object`)
    }
}