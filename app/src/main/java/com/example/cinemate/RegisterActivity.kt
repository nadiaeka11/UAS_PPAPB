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
import com.google.firebase.auth.UserProfileChangeRequest

class RegisterActivity : AppCompatActivity() {
    // Mendeklarasi variabel binding untuk mengikat elemen tampilan pada layout activity_main
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var prefManager: PrefManager
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Menginisialisasi variabel binding. Metode inflate digunakan untuk menghubungkan layout XML activity_main.xml dengan kode Kotlin,
        // sehingga dapat mengakses elemen-elemen tampilan yang ada di dalamnya.
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        with(binding){
            // Menginisialisasi handler klik untuk elemen dengan ID regButton
            regButton.setOnClickListener {
                val email = emailReg.text.toString().trim()
                val username = usernameReg.text.toString().trim()
                val password = passwordReg.text.toString().trim()
                val confirmPassword = passwordConfirm.text.toString()

                if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                    Toast.makeText(
                        this@RegisterActivity,
                        "Mohon isi semua data",
                        Toast.LENGTH_SHORT).show()
                } else if (password != confirmPassword) {
                    Toast.makeText(this@RegisterActivity, "Password tidak sama",
                        Toast.LENGTH_SHORT).show()
                } else {
                    progressBar?.visibility = View.VISIBLE
                    auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this@RegisterActivity) { task ->
                            progressBar?.visibility = View.INVISIBLE
                            if (task.isSuccessful) {
                                val user = auth.currentUser
                                val profileUpdates = UserProfileChangeRequest.Builder()
                                    .setDisplayName(username)
                                    .build()
                                user?.updateProfile(profileUpdates)
                                    ?.addOnCompleteListener { profileUpdateTask ->
                                        progressBar?.visibility = View.INVISIBLE
                                        if (profileUpdateTask.isSuccessful) {
                                            Toast.makeText(
                                                this@RegisterActivity, "Register Successfull",
                                                Toast.LENGTH_SHORT).show()
                                            prefManager.setAsPublic();
                                            startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                                            finish()
                                        } else {
                                            Toast.makeText(
                                                this@RegisterActivity, "Register Failed ${profileUpdateTask.exception?.message}",
                                                Toast.LENGTH_SHORT).show()
                                        }
                                    }
                            } else {
                                Toast.makeText(
                                    this@RegisterActivity, "Register failed: ${task.exception?.message}",
                                    Toast.LENGTH_SHORT).show()
                            }
                        }
                }
            }
            // Menginisialisasi handler klik untuk elemen dengan ID loginNow
            loginNow.setOnClickListener{
                startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                finish()
            }
        }
    }
}