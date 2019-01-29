package com.example.customview.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.customview.R
import kotlinx.android.synthetic.main.fragment_animated_views.*

class AnimatedFragment : Fragment() {

    companion object {
        fun newInstance(): AnimatedFragment {
            val fragment = AnimatedFragment()
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_animated_views, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        main_arc.setDestAngle(160, 1300)
    }

}