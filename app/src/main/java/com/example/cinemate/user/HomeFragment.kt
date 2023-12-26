package com.example.cinemate.user

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridView
import android.widget.TextView
import com.example.cinemate.R

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

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
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        val gridView = view.findViewById<GridView>(R.id.gridView)
        val text: TextView = view.findViewById(R.id.editUsername)

        val value = arguments?.getString(EXT_USN)
        text.text = "Your Username: $value"

        // Set OnItemClickListener for gridView
        gridView.setOnItemClickListener { parent, view, position, id ->
            // Handle item click here
            startActivity(Intent(requireContext(), DetailsActivity::class.java))
        }
        return view
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
        const val EXT_USN = "EXT_USERNAME" // Define EXT_USN here
        // Membuat instance fragment dengan parameter username
        @JvmStatic
        fun newInstance(param1: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(EXT_USN, param1)
                }
            }
    }
}