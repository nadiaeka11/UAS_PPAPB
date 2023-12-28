package com.example.cinemate

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.cinemate.admin.AdminActivity
import com.example.cinemate.databinding.ActivityLoginBinding
import com.example.cinemate.user.Layout
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    // Mendeklarasikan variabel binding untuk mengikat elemen tampilan pada layout activity_main
    private lateinit var binding: ActivityLoginBinding
    private lateinit var prefManager: PrefManager
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        prefManager = PrefManager.getInstance(this)

        // Memeriksa apakah pengguna sudah masuk, jika iya, langsung alihkan ke tampilan utama
        val currentUser = auth.currentUser
        if (currentUser != null) {
            startActivity(Intent(this@LoginActivity, Layout::class.java))
            finish()
        }

        // Menginisialisasi variabel binding. Metode inflate digunakan untuk menghubungkan layout XML activity_main.xml dengan kode Kotlin, sehingga dapat mengakses elemen-elemen tampilan yang ada di dalamnya.
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding){
            // Menginisialisasi handler klik untuk elemen dengan ID login
            loginButton.setOnClickListener {
                val email = edtEmail.text.toString().trim()
                val password = edtPassword.text.toString().trim()

                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(
                        this@LoginActivity,
                        "Mohon isi semua data", Toast.LENGTH_SHORT).show()
                } else {
                    // Menampilkan ProgressBar selama proses login
                    progressBar.visibility = View.VISIBLE
                    auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this@LoginActivity) { task ->
                            // Menyembunyikan ProgressBar setelah proses login selesai
                            progressBar.visibility = View.INVISIBLE
                            if (task.isSuccessful) {
                                // Jika login berhasil, periksa apakah pengguna adalah admin atau pengguna umum
                                if(email == "admincinemate@gmail.com"){
                                    prefManager.setAsAdmin()
                                    startActivity(Intent(this@LoginActivity, AdminActivity::class.java))
                                    finish()
                                }else{
                                    prefManager.setAsPublic()
                                    startActivity(Intent(this@LoginActivity, Layout::class.java))
                                    finish()
                                }
                            } else {
                                // Jika login gagal, tampilkan pesan kesalahan
                                Toast.makeText(
                                    this@LoginActivity, "Authentication failed: ${task.exception?.message}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                }
            }
            // Menginisialisasi handler klik untuk elemen dengan ID registerNow
            registerNow.setOnClickListener {
                // Pindah ke halaman registrasi jika pengguna belum memiliki akun
                startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
            }
        }
    }
}