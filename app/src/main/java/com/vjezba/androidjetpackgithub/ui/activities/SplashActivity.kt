package com.vjezba.androidjetpackgithub.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.vjezba.androidjetpackgithub.R
import com.vjezba.domain.repository.UserManager
import org.koin.android.ext.android.inject

class SplashActivity : AppCompatActivity() {

    val userManager: UserManager by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        if( !userManager.isUserLoggedIn() ) {
            if( !userManager.isUserRegistered() ) {
                startActivity(Intent(this, RegistrationActivity::class.java))
                finish()
            }
            else {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }
        else {
            startActivity(Intent(this, LanguagesActivity::class.java))
            finish()
        }

    }

}