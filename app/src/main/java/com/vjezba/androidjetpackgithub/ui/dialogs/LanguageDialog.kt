package com.vjezba.androidjetpackgithub.ui.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.vjezba.androidjetpackgithub.R
import kotlinx.android.synthetic.main.dialog_select_language.view.*

class LanguageDialog : DialogFragment() {

        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            return inflater.inflate(R.layout.dialog_select_language, container, false)
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)
            setupClickListeners(view)
        }

        override fun onStart() {
            super.onStart()
            dialog?.window?.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT
            )
        }

        private fun setupClickListeners(view: View) {
            view.radioCPlusPLus.setOnClickListener {
                dismiss()
            }
            view.radioJava.setOnClickListener {
                dismiss()
            }
            view.radioKotlin.setOnClickListener {
                dismiss()
            }
            view.radioSwift.setOnClickListener {
                dismiss()
            }
            view.btnCancel.setOnClickListener {
                dismiss()
            }
        }
}