package com.example.cinemate

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.example.cinemate.databinding.ActivityRegisterBinding
import com.example.cinemate.user.HomeFragment
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : AppCompatActivity() {
    //Mendeklarasi variabel binding untuk mengikat elemen tampilan pada layout activity_main
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var prefManager: PrefManager
    private lateinit var auth: FirebaseAuth
    private lateinit var progressBar: ProgressBar
    private lateinit var textView: TextView

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if (currentUser != null) {
            startActivity(Intent(this@RegisterActivity, HomeFragment::class.java))
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //menginisialisasi variabel binding. Metode inflate digunakan untuk menghubungkan layout XML activity_main.xml dengan kode Kotlin, sehingga dapat mengakses elemen-elemen tampilan yang ada di dalamnya.
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        prefManager = PrefManager.getInstance(this)

        auth = FirebaseAuth.getInstance()
        progressBar = findViewById<ProgressBar>(R.id.progressBar)
        textView = findViewById(R.id.loginNow)

        with(binding){
            //menginisialisasi handler klik untuk elemen dengan ID login
            regButton.setOnClickListener {
                val email = emailReg.text.toString().trim()
                val username = usernameReg.text.toString().trim()
                val password = passwordReg.text.toString().trim()
                val confirmPassword = passwordConfirm.text.toString()
                progressBar?.visibility = View.INVISIBLE

                if (username.isEmpty() || password.isEmpty() ||
                    confirmPassword.isEmpty()) {
                    Toast.makeText(
                        this@RegisterActivity, "Mohon isi semua data",
                        Toast.LENGTH_SHORT).show()
                } else if (password != confirmPassword) {
                    Toast.makeText(this@RegisterActivity, "Password tidak sama",
                        Toast.LENGTH_SHORT).show()
                } else {
                    prefManager.saveEmail(email)
                    prefManager.saveUsername(username)
                    prefManager.savePassword(password)
                    prefManager.setLoggedIn(true)
                    checkLoginStatus()
                }

                auth.createUserWithEmailAndPassword(username, password)
                    .addOnCompleteListener(this@RegisterActivity) { task ->
                        progressBar?.visibility = View.INVISIBLE
                        if (task.isSuccessful) {
                            prefManager.setLoggedIn(true)
                            Toast.makeText(
                                this@RegisterActivity, "Account created: ${task.exception?.message}",
                                Toast.LENGTH_SHORT).show()
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(
                                this@RegisterActivity, "Authentication failed: ${task.exception?.message}",
                                Toast.LENGTH_SHORT).show()
                        }
                    }
            }
            textView.setOnClickListener{
                startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
            }
        }
    }
    private fun checkLoginStatus() {
        val isLoggedIn = prefManager.isLoggedIn()
        if (isLoggedIn) {
            Toast.makeText(this@RegisterActivity, "Registrasi berhasil",
                Toast.LENGTH_SHORT).show()
            startActivity(Intent(this@RegisterActivity, HomeFragment::class.java))
            finish()
        } else {
            Toast.makeText(this@RegisterActivity, "Registrasi gagal",
                Toast.LENGTH_SHORT).show()
        }

    }
}