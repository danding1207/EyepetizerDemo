package com.msc.eyepetizer

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import com.msc.libcommon.base.BaseActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : BaseActivity() {

    private var onNavigationItemSelectedListener: BottomNavigationView.OnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        val i = item.itemId
        when (i) {
            R.id.navigation_home -> //            mPager.setCurrentItem(0)
                return@OnNavigationItemSelectedListener true
            R.id.navigation_subscription -> //            mPager.setCurrentItem(1)
                return@OnNavigationItemSelectedListener true
            R.id.navigation_notification -> //            mPager.setCurrentItem(2)
                return@OnNavigationItemSelectedListener true
            R.id.navigation_me -> //            mPager.setCurrentItem(2)
                return@OnNavigationItemSelectedListener true
            else -> false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navigation.enableAnimation(false)
        navigation.enableItemShiftingMode(false)
        navigation.enableShiftingMode(false)
        navigation.setIconsMarginTop(2)
        navigation.setIconSize(44f,44f)
        navigation.setTextSize(8f)
        navigation.onNavigationItemSelectedListener = onNavigationItemSelectedListener
    }







}
