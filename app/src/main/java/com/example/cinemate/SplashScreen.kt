package com.example.cinemate

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.cinemate.R

class SplashScreen : AppCompatActivity() {
    // Pada oncreate, splash screen akan menunggu selama 3 detik, kemudian berpindah ke LoginActivity.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_screen)

        supportActionBar?.hide()

        // Handler untuk menunda tampilan LoginActivity selama 3000 milidetik (3 detik)
        Handler().postDelayed({
            val intent = Intent(this@SplashScreen, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }, 3000)
    }
}