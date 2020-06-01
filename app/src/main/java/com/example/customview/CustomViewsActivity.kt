package com.example.customview

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.customview.fragments.AnimatedFragment
import com.example.customview.fragments.BasicFragment
import com.example.customview.fragments.InteractiveFragment
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_custom_views.*
import kotlinx.android.synthetic.main.activity_custom_views.view.*

class CustomViewsActivity : AppCompatActivity() {

    private var mSectionsPagerAdapter: SectionsPagerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_custom_views)

        mSectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)

        // Set up the ViewPager with the sections adapter.
        container.adapter = mSectionsPagerAdapter

        Types.values().forEach {
            val tab = tabs.newTab();
            tab.text = it.name
            tabs.addTab(tab)
        }

        container.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabs))
        tabs.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(container))
        container.setCurrentItem(1, false)
    }

    inner class SectionsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

        val size: Int = Types.values().size

        override fun getItem(position: Int): Fragment {
            return when (Types.values()[position]) {
                Types.BASIC -> BasicFragment.newInstance()
                Types.ANIMATED -> AnimatedFragment.newInstance()
                Types.INTERACTIVE -> InteractiveFragment.newInstance()
            }
        }

        override fun getCount(): Int {
            return size
        }
    }

    enum class Types { BASIC, ANIMATED, INTERACTIVE }
}

class NonSwipeableViewPager constructor(context: Context, attrs: AttributeSet?) :
        ViewPager(context, attrs) {

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        // Never allow swiping to switch between pages
        return false
    }

    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        // Never allow swiping to switch between pages
        return false
    }
}