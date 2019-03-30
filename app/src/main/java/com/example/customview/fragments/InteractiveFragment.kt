package com.example.customview.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.customview.R
import com.example.customview.views.interactive.ColorChangeListener
import kotlinx.android.synthetic.main.fragment_interactive.*

class InteractiveFragment : Fragment(), ColorChangeListener {

    companion object {
        fun newInstance(): InteractiveFragment {
            val fragment = InteractiveFragment()
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_interactive, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        interactive_color_picker.colorChangeListener  = this
    }

    override fun onColorChanged(color: Int) {
        line.setBackgroundColor(color)
    }
}