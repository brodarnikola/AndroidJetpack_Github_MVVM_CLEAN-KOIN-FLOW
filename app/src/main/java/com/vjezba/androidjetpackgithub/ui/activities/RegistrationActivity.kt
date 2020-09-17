package com.vjezba.androidjetpackgithub.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.vjezba.androidjetpackgithub.R
import com.vjezba.androidjetpackgithub.ui.fragments.EnterDetailsFragment
import com.vjezba.androidjetpackgithub.ui.fragments.TermsAndConditionsFragment
import com.vjezba.androidjetpackgithub.viewmodels.GalleryViewModel
import com.vjezba.androidjetpackgithub.viewmodels.RegistrationViewModel
import com.vjezba.domain.repository.UserManager
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class RegistrationActivity : AppCompatActivity() {

    val userManager: UserManager by inject()
    val registrationViewModel : RegistrationViewModel = RegistrationViewModel( userManager )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_holder, EnterDetailsFragment())
            .commit()
    }

    /**
     * Callback from EnterDetailsFragment when username and password has been entered
     */
    fun onDetailsEntered() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_holder, TermsAndConditionsFragment())
            .addToBackStack(TermsAndConditionsFragment::class.java.simpleName)
            .commit()
    }

    /**
     * Callback from T&CsFragment when TCs have been accepted
     */
    fun onTermsAndConditionsAccepted() {
        registrationViewModel.registerUser()
        startActivity(Intent(this, LanguagesActivity::class.java))
        finish()
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
        } else {
            super.onBackPressed()
        }
    }
}