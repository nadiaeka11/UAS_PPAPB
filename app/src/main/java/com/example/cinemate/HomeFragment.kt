package com.example.cinemate

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Menerima argumen yang dikirimkan saat membuat instance fragment
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Menghubungkan layout fragment_home.xml dengan kode Kotlin
        val fragmentInflater = inflater.inflate(R.layout.fragment_home, container, false)
        val card = fragmentInflater.findViewById<ImageView>(R.id.poster3)

        // Mendapatkan nilai username dari argumen, atau mengambil nilai default jika null
        val username = arguments?.getString(Layout.EXT_USN) ?: "DefaultUsername"

        // Menetapkan nilai username ke elemen TextView pada layout
        val usernameTextView = fragmentInflater.findViewById<TextView>(R.id.editUsername)
        usernameTextView.text = username

        // Menambahkan event listener pada elemen card (dapat disesuaikan)
        card.setOnClickListener() {
            // Memulai intent untuk berpindah ke DetailsActivity
            startActivity(Intent(requireContext(), DetailsActivity::class.java))
        }

        return fragmentInflater
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // Membuat instance fragment dengan parameter username
        @JvmStatic
        fun newInstance(param1: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                }
            }
    }
}