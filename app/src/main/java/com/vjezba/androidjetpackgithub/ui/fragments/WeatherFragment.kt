package com.vjezba.androidjetpackgithub.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.vjezba.androidjetpackgithub.databinding.FragmentWeatherBinding
import com.vjezba.androidjetpackgithub.viewmodels.SlideshowViewModel
import com.vjezba.androidjetpackgithub.viewmodels.WeatherViewModel
import kotlinx.android.synthetic.main.activity_languages_main.*
import kotlinx.android.synthetic.main.fragment_weather.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

private const val UPDATE_PERIOD = 10000L

class WeatherFragment : Fragment() {

    private lateinit var slideshowViewModel: SlideshowViewModel


    private val viewModel : WeatherViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentWeatherBinding.inflate(inflater, container, false)
        context ?: return binding.root

        viewModel.incrementNumberAutomaticallyByOne.observe(viewLifecycleOwner, Observer { currentNumber ->
            tvNumberIncreaseAutomatically.text = "" + currentNumber
        })

        viewModel.incrementNumberManuallyByOne.observe(viewLifecycleOwner, Observer { currentNumber ->
            tvNumberIncreaseManually.text = "" + currentNumber
        })


        activity?.speedDial?.visibility = View.GONE
        return binding.root
    }

    override fun onResume() {
        super.onResume()

        btnIncreaseNumber.setOnClickListener {
            viewModel.incrementManuallyByOne()
        }

        lifecycleScope.launch {
            while (true) {
                try {
                    handleUpdate()
                } catch (ex: Exception) {
                    Log.v("ERROR","Periodic remote-update failed...", ex)
                }
                delay(UPDATE_PERIOD)
            }
        }
    }

    private fun handleUpdate() {
        viewModel.incrementAutomaticallyByOne()
    }

}