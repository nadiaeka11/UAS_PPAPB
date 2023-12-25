package com.example.cinemate

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.example.cinemate.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    //Mendeklarasi variabel binding untuk mengikat elemen tampilan pada layout activity_main
    private lateinit var binding: ActivityLoginBinding
    private lateinit var prefManager: PrefManager
    private lateinit var auth: FirebaseAuth
    private lateinit var progressBar: ProgressBar
    private lateinit var textView: TextView

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if (currentUser != null) {
            startActivity(Intent(this@LoginActivity, Layout::class.java))
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //menginisialisasi variabel binding. Metode inflate digunakan untuk menghubungkan layout XML activity_main.xml dengan kode Kotlin, sehingga dapat mengakses elemen-elemen tampilan yang ada di dalamnya.
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prefManager = PrefManager.getInstance(this)

        auth = FirebaseAuth.getInstance()
        progressBar = findViewById<ProgressBar>(R.id.progressBar)
        textView = findViewById(R.id.registerNow)

        with(binding){
            //menginisialisasi handler klik untuk elemen dengan ID login
            loginButton.setOnClickListener {
                val username = edtUsername.text.toString().trim()
                val password = edtPassword.text.toString().trim()
                progressBar?.visibility = View.INVISIBLE

                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(
                        this@LoginActivity,
                        "Mohon isi semua data", Toast.LENGTH_SHORT).show()
                } else {
                    if (isValidUsernamePassword()) {
                        prefManager.setLoggedIn(true)
                        checkLoginStatus()
                    } else {
                        Toast.makeText(this@LoginActivity,
                            "Username atau password salah", Toast.LENGTH_SHORT).show()
                    }
                }

                auth.signInWithEmailAndPassword(username, password)
                    .addOnCompleteListener(this@LoginActivity) { task ->
                        progressBar?.visibility = View.INVISIBLE
                        if (task.isSuccessful) {
                            Toast.makeText(
                                this@LoginActivity, "Login Sucessful: ${task.exception?.message}",
                                Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this@LoginActivity, Layout::class.java))
                            finish()
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(
                                this@LoginActivity, "Authentication failed: ${task.exception?.message}",
                                Toast.LENGTH_SHORT).show()
                        }
                    }
            }
            registerNow.setOnClickListener {
                startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
            }
        }
    }
    private fun isValidUsernamePassword(): Boolean{
        val username = prefManager.getUsername()
        val password = prefManager.getPassword()
        val inputUsername = binding.edtUsername.text.toString()
        val inputPassword = binding.edtPassword.text.toString()
        return username == inputUsername && password == inputPassword
    }
    private fun checkLoginStatus() {
        val isLoggedIn = prefManager.isLoggedIn()
        if (isLoggedIn) {
            Toast.makeText(this@LoginActivity, "Login Sucessful",
                Toast.LENGTH_SHORT).show()
            startActivity(Intent(this@LoginActivity, Layout::class.java))
            finish()
        } else {
            Toast.makeText(this@LoginActivity, "Login gagal",
                Toast.LENGTH_SHORT).show()
        }
    }
}