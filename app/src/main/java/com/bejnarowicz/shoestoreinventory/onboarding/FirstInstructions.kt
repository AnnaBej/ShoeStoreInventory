package com.bejnarowicz.shoestoreinventory.onboarding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.bejnarowicz.shoestoreinventory.R
import kotlinx.android.synthetic.main.fragment_first_instructions.view.*

class FirstInstructions : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_first_instructions, container, false)

        val viewPager = activity?.findViewById<ViewPager2>(R.id.view_pager)
        view.button_next_first.setOnClickListener {
            viewPager?.currentItem = 2
        }
        view.button_back_first.setOnClickListener {
            viewPager?.currentItem = 0
        }

        return view
    }


}