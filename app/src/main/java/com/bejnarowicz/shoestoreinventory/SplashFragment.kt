package com.bejnarowicz.shoestoreinventory

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController


class SplashFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        Handler(Looper.getMainLooper()).postDelayed({
            if (onBoardingFinished()){
                //if a returning user
                findNavController().navigate(R.id.action_splashFragment_to_mainViewFragment)
            }else{
                //if running the app from the first time
                findNavController().navigate(R.id.action_splashFragment_to_loginFragment)
            }
        }, 2000)

        (requireActivity() as AppCompatActivity).supportActionBar?.hide()

        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    //checking either to show the onBoarding screens to the user
    private fun onBoardingFinished(): Boolean {
        val sharedPref = requireActivity().getSharedPreferences("onBoarding", Context.MODE_PRIVATE)
        return sharedPref.getBoolean("Finished", false)
    }

}