package com.example.cinemate

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.cinemate.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    //Mendeklarasi variabel binding untuk mengikat elemen tampilan pada layout activity_main
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //menginisialisasi variabel binding. Metode inflate digunakan untuk menghubungkan layout XML activity_main.xml dengan kode Kotlin, sehingga dapat mengakses elemen-elemen tampilan yang ada di dalamnya.
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding){
            //menginisialisasi handler klik untuk elemen dengan ID login
            loginButton.setOnClickListener {
                val username = username.text.toString().trim()
                val password = password.text.toString().trim()

                val intentFirst = Intent(this@LoginActivity, Layout::class.java)
                intentFirst.putExtra(Layout.EXT_USN, username)
                intentFirst.putExtra(Layout.EXT_PASS, password)
                startActivity(intentFirst)
                }
            }
        }
    }