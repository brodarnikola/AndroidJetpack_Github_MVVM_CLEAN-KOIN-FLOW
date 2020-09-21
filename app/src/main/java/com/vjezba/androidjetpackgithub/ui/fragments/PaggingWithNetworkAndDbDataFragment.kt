package com.vjezba.androidjetpackgithub.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.vjezba.androidjetpackgithub.R
import com.vjezba.androidjetpackgithub.databinding.FragmentPaggingNetworkAndDbDataBinding
import com.vjezba.androidjetpackgithub.viewmodels.PaggingWithNetworkAndDbViewModel
import kotlinx.android.synthetic.main.activity_languages_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class PaggingWithNetworkAndDbDataFragment : Fragment() {

    private val viewModel : PaggingWithNetworkAndDbViewModel by viewModel()
    private val args: PaggingWithNetworkAndDbDataFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentPaggingNetworkAndDbDataBinding.inflate(inflater, container, false)
        context ?: return binding.root

        activity?.toolbar?.title = getString(R.string.gallery_title) + ": " + args.languageName


        activity?.speedDial?.visibility = View.GONE
        return binding.root
    }

    override fun onResume() {
        super.onResume()


    }


}