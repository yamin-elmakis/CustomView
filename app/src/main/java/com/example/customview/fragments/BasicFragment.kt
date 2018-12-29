package com.example.customview.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.customview.R
import kotlinx.android.synthetic.main.fragment_basic_views.*

class BasicFragment : Fragment() {

    companion object {
        fun newInstance(): BasicFragment {
            val fragment = BasicFragment()
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_basic_views, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        b_plus.setOnClickListener {
            custom_view.increase()
        }
        b_minus.setOnClickListener {
            custom_view.decrease()
        }
    }
}