package com.vjezba.androidjetpackgithub.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.vjezba.androidjetpackgithub.databinding.FragmentDagger2GithubBinding
import com.vjezba.androidjetpackgithub.ui.activities.LanguagesActivity
import com.vjezba.androidjetpackgithub.ui.dialogs.KeyWordDialog
import com.vjezba.androidjetpackgithub.ui.dialogs.LanguageDialog
import kotlinx.android.synthetic.main.activity_languages_main.*

class Dagger2GithubFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentDagger2GithubBinding.inflate(inflater, container, false)

        (activity as AppCompatActivity?)?.setSupportActionBar(activity?.toolbar)
        activity?.speedDial?.visibility = View.GONE

        binding.btnLanguage.setOnClickListener {
            val languageDialog =
                LanguageDialog()
            languageDialog.show(
                (requireContext() as LanguagesActivity).supportFragmentManager, ""
            )
        }

        binding.btnKeyWord.setOnClickListener {
            val keyWordDialog =
                KeyWordDialog()
            keyWordDialog.show(
                (requireContext() as LanguagesActivity).supportFragmentManager, ""
            )
        }

        return binding.root
    }

}