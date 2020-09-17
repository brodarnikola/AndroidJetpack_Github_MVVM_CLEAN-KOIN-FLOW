package com.vjezba.androidjetpackgithub.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.vjezba.androidjetpackgithub.R
import com.vjezba.androidjetpackgithub.viewmodels.Dagger2GithubViewModel
import kotlinx.android.synthetic.main.activity_languages_main.*

class Dagger2GithubFragment : Fragment() {

    private lateinit var dagger2GithubViewModel: Dagger2GithubViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dagger2GithubViewModel =
            ViewModelProviders.of(this).get(Dagger2GithubViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_dagger2_github, container, false)
        val textView: TextView = root.findViewById(R.id.text_slideshow)
        dagger2GithubViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })

        activity?.speedDial?.visibility = View.GONE
        return root
    }
}