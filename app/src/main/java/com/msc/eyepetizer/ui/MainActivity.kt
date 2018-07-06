package com.msc.eyepetizer.ui

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.content.ContextCompat
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.msc.eyepetizer.R
import com.msc.libcommon.adapter.FragmentAdapter
import com.msc.libcommon.base.ARouterPath
import com.msc.libcommon.base.BaseActivity
import com.msc.libcommon.base.BaseFragment
import kotlinx.android.synthetic.main.activity_main.*
import java.util.ArrayList

@Route(path = ARouterPath.MAIN_ACT)
class MainActivity : BaseActivity() {

    private val mFragments = ArrayList<BaseFragment>()
    private var mAdapter: FragmentAdapter? = null

    private var onNavigationItemSelectedListener: BottomNavigationView.OnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        val i = item.itemId
        when (i) {
            R.id.navigation_home -> {
                container_pager.currentItem = 0
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_subscription ->{
                container_pager.currentItem = 1
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notification -> {
                container_pager.currentItem = 2
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_me -> {
                container_pager.currentItem = 3
                return@OnNavigationItemSelectedListener true
            }
            else -> false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setStatusBarLightMode(this, ContextCompat.getColor(this, R.color.white))

        setContentView(R.layout.activity_main)
        navigation.enableAnimation(false)
        navigation.enableItemShiftingMode(false)
        navigation.enableShiftingMode(false)
        navigation.setIconsMarginTop(2)
        navigation.setIconSize(44f, 44f)
        navigation.setTextSize(8f)
        navigation.onNavigationItemSelectedListener = onNavigationItemSelectedListener

        rootView = constraintLayout

        container_pager.offscreenPageLimit = 4

        val fragmentHome = ARouter.getInstance().build(ARouterPath.HOME_PAGE_FGT).navigation() as BaseFragment
        val fragmentSubscription = ARouter.getInstance().build(ARouterPath.SUBSCRIPTION_FGT).navigation() as BaseFragment
        val fragmentNotification = ARouter.getInstance().build(ARouterPath.NOTIFICATION_FGT).navigation() as BaseFragment
        val fragmentMe = ARouter.getInstance().build(ARouterPath.ME_FGT).navigation() as BaseFragment

        mFragments.add(fragmentHome)
        mFragments.add(fragmentSubscription)
        mFragments.add(fragmentNotification)
        mFragments.add(fragmentMe)

        mAdapter = FragmentAdapter(supportFragmentManager, mFragments)
        container_pager.adapter = mAdapter

    }

}
