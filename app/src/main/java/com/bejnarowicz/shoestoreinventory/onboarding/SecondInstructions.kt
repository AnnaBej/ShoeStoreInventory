package com.bejnarowicz.shoestoreinventory.onboarding

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.bejnarowicz.shoestoreinventory.R
import kotlinx.android.synthetic.main.fragment_second_instructions.view.*

class SecondInstructions : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_second_instructions, container, false)

        val viewPager = activity?.findViewById<ViewPager2>(R.id.view_pager)
        view.button_finish_second.setOnClickListener {
            findNavController().navigate(R.id.action_viewPagerFragment_to_mainViewFragment)
            onBoardingFinished()
        }
        view.button_back_second.setOnClickListener {
            viewPager?.currentItem = 1
        }

        return view
    }

//marking that onBoarding has been completed so the user won't see it again
    private fun onBoardingFinished(){
        val sharedPref = requireActivity().getSharedPreferences("onBoarding", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putBoolean("Finished", true)
        editor.apply()
    }

}