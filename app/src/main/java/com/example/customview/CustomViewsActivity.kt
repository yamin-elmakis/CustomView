package com.example.customview

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.customview.fragments.AnimatedFragment
import com.example.customview.fragments.BasicFragment
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_custom_views.*

class CustomViewsActivity : AppCompatActivity() {

    private var mSectionsPagerAdapter: SectionsPagerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_custom_views)

        setSupportActionBar(toolbar)
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
    }

    inner class SectionsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        val size: Int = Types.values().size

        override fun getItem(position: Int): Fragment {
            return when (Types.values()[position]) {
                Types.BASIC -> BasicFragment.newInstance()
                Types.ANIMATED -> AnimatedFragment.newInstance()
            }
        }

        override fun getCount(): Int {
            return size
        }
    }

    enum class Types { BASIC, ANIMATED }
}
