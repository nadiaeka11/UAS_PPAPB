package com.example.cinemate

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.cinemate.admin.AdminActivity
import com.example.cinemate.databinding.ActivityLoginBinding
import com.example.cinemate.user.HomeFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class LoginActivity : AppCompatActivity() {
    //Mendeklarasi variabel binding untuk mengikat elemen tampilan pada layout activity_main
    private lateinit var binding: ActivityLoginBinding
    private lateinit var prefManager: PrefManager
    private lateinit var auth: FirebaseAuth
    private lateinit var progressBar: ProgressBar
    private lateinit var textView: TextView
    private var user: FirebaseUser? = null

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if (currentUser != null) {
            startActivity(Intent(this@LoginActivity, HomeFragment::class.java))
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        //menginisialisasi variabel binding. Metode inflate digunakan untuk menghubungkan layout XML activity_main.xml dengan kode Kotlin, sehingga dapat mengakses elemen-elemen tampilan yang ada di dalamnya.
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val name: EditText = findViewById(R.id.edtUsername)

        prefManager = PrefManager.getInstance(this)

        user = auth.currentUser
        progressBar = findViewById<ProgressBar>(R.id.progressBar)
        textView = findViewById(R.id.registerNow)

        // Mencari tombol dengan ID "loginbtn" dalam tampilan fragment.
        val loginButton = binding.loginButton

        with(binding){
            //menginisialisasi handler klik untuk elemen dengan ID login
            loginButton.setOnClickListener {
                val fragment = HomeFragment()
                val username = edtUsername.text.toString().trim()
                val password = edtPassword.text.toString().trim()
                val bundle = Bundle()
                bundle.putString("name", username)
                fragment.arguments = bundle
                supportFragmentManager.beginTransaction().add(R.id.editUsername,fragment).commit()

                progressBar?.visibility = View.INVISIBLE

                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(
                        this@LoginActivity,
                        "Mohon isi semua data", Toast.LENGTH_SHORT).show()
                } else {
                    if (isValidUsernamePassword()) {
                        prefManager.setLoggedIn(true)
                        checkLoginStatus(username)
                    } else {
                        Toast.makeText(this@LoginActivity,
                            "Username atau password salah", Toast.LENGTH_SHORT).show()
                    }
                }

                auth.signInWithEmailAndPassword(username, password)
                    .addOnCompleteListener(this@LoginActivity) { task ->
                        progressBar?.visibility = View.INVISIBLE
                        if (task.isSuccessful) {
                            prefManager.setLoggedIn(true)
                            val user = auth.currentUser
                            // Update the condition to check if the user is an admin
                            if (isAdmin(user)) {
                                Toast.makeText(
                                    this@LoginActivity,
                                    "Login as Admin Sucessful: ${task.exception?.message}",
                                    Toast.LENGTH_SHORT
                                ).show()
                                startActivity(Intent(this@LoginActivity, AdminActivity::class.java))
                                finish()
                            } else {
                                // Pass the username to HomeFragment
                                val homeFragment = HomeFragment.newInstance(username)
                                replaceFragment(homeFragment)
                            }
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(
                                this@LoginActivity, "Authentication failed: ${task.exception?.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            }
            registerNow.setOnClickListener {
                startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
            }
        }
    }

    private fun isAdmin(user: FirebaseUser?): Boolean {
        // Implementasi logika pengecekan admin sesuai kebutuhan aplikasi Anda
        return user!= null && user.email == "admincinemate@gmail.com"
    }

    private fun isValidUsernamePassword(): Boolean{
        val username = prefManager.getUsername()
        val password = prefManager.getPassword()
        val inputUsername = binding.edtUsername.text.toString()
        val inputPassword = binding.edtPassword.text.toString()
        return username == inputUsername && password == inputPassword
    }
    private fun checkLoginStatus(username: String) {
        val isLoggedIn = prefManager.isLoggedIn()
        if (isLoggedIn) {
            Toast.makeText(this@LoginActivity, "Login Sucessful",
                Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this@LoginActivity, "Login gagal",
                Toast.LENGTH_SHORT).show()
        }
    }
    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.frameLayout, fragment)
            .addToBackStack(null)
            .commit()
    }
}