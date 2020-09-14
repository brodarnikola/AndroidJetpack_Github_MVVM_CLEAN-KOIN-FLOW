package com.vjezba.androidjetpackgithub.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.vjezba.androidjetpackgithub.R

class SlideShowActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_proba)
    }

    override fun onBackPressed() {

        val intent = Intent(this, LanguagesActivity::class.java)
        startActivity(intent)
        finish()
        super.onBackPressed()
    }
}