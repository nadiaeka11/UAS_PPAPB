package com.example.cinemate

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.cinemate.databinding.ActivityLayoutBinding

class Layout : AppCompatActivity() {
    private lateinit var binding: ActivityLayoutBinding
    companion object {
        const val EXT_USN = "ext_usn"
        const val EXT_PASS = "ext_pass"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Menghubungkan layout XML activity_layout.xml dengan kode Kotlin
        binding = ActivityLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Mendapatkan data username dari intent
        val username = intent.getStringExtra(EXT_USN)

        // Membuat bundle untuk mengirim data ke fragment
        val bundle = Bundle()
        bundle.putString(EXT_USN, username)

        // Membuat instance HomeFragment
        val homeFragment = HomeFragment()
        // Menetapkan argument bundle ke HomeFragment
        homeFragment.arguments = bundle

        // Membuat instance AccountFragment
        val accountFragment = AccountFragment()
        // Menetapkan argument bundle ke AccountFragment
        accountFragment.arguments = bundle

        // Menampilkan HomeFragment saat pertama kali aplikasi dibuka
        replaceFragment(homeFragment)

        // Mengatur event listener untuk BottomNavigationView
        with(binding) {
            bottomNavbar.setOnItemSelectedListener() {
                // Menangani pemilihan item pada BottomNavigationView
                when(it.itemId){
                    R.id.itemHome -> replaceFragment(HomeFragment())
                    R.id.itemAccount -> replaceFragment(AccountFragment())
                    else -> {}
                }
                true
            }
        }
    }

    // Fungsi untuk menggantikan fragmen yang ditampilkan di dalam tampilan dengan id "frameLayout"
    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        // Menggantikan fragmen saat ini dengan fragmen yang baru
        fragmentTransaction.replace(R.id.frameLayout, fragment)
        // Menyelesaikan transaksi fragmen
        fragmentTransaction.commit()
    }
}