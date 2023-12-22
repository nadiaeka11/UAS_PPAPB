package com.example.cinemate

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.cinemate.databinding.ActivityDetailsBinding

class DetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Code to handle the click event on the "Get Ticket" button
        with(binding) {
            backButton.setOnClickListener {
                val intentDetails = Intent(this@DetailsActivity, HomeFragment::class.java)
                startActivity(intentDetails)
            }
        }
    }
}