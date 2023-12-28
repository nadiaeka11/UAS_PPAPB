package com.example.cinemate.user

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.cinemate.LoginActivity
import com.example.cinemate.R
import com.google.firebase.auth.FirebaseAuth

class AccountFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val fragmentInflater = inflater.inflate(R.layout.fragment_account, container, false)

        // Mendapatkan referensi ke TextView untuk logout
        val logout = fragmentInflater.findViewById<TextView>(R.id.logoutText)

        // Mendapatkan instance dari FirebaseAuth
        auth = FirebaseAuth.getInstance()

        // Menambahkan listener untuk logout
        logout.setOnClickListener{
            // Melakukan logout dari akun Firebase
            auth.signOut()
            // Memulai aktivitas LoginActivity setelah logout
            startActivity(Intent(requireContext(), LoginActivity::class.java))
            // Menutup aktivitas saat ini
            requireActivity().finish()
        }

        // Mengembalikan tata letak fragment yang telah dibuat
        return fragmentInflater
    }
}