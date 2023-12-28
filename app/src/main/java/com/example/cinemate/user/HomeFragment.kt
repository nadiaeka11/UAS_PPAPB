package com.example.cinemate.user

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.example.cinemate.databinding.FragmentHomeBinding
import com.example.cinemate.model.Movie
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class HomeFragment : Fragment() {
    private lateinit var binding:FragmentHomeBinding
    private lateinit var auth: FirebaseAuth
    private val databaseReference = FirebaseDatabase.getInstance().getReference("Images")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Mendapatkan instance FirebaseAuth
        auth = FirebaseAuth.getInstance()
        // Mendapatkan pengguna saat ini
        val currentUser = auth.currentUser

        // Mengatur tata letak tampilan fragment
        binding = FragmentHomeBinding.inflate(inflater,container,false)

        // Membuat instance adapter untuk RecyclerView
        val adapter = MovieAdapter(object :MovieAdapter.OnItemClickCallback{
            override fun onItemClicked(data: Movie) {
                // Membuka DetailsActivity saat item di RecyclerView diklik
                startActivity(Intent(requireContext(),DetailsActivity::class.java).putExtra("key",data.key))
            }
        })

        // Mengatur teks pada TextView untuk menampilkan nama pengguna saat ini
        binding.editUsername.text=currentUser?.displayName
        // Mengatur tata letak dan adapter untuk RecyclerView
        binding.gridView.layoutManager = GridLayoutManager(requireContext(),2)
        binding.gridView.adapter = adapter

        // Mendengarkan perubahan pada database Firebase untuk mendapatkan daftar film
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // Mengonversi DataSnapshot menjadi daftar film dalam bentuk model Movie
                val list = ArrayList<Movie>()
                for (dataSnapshot in snapshot.children) {
                    dataSnapshot.getValue(Movie::class.java)?.let { list.add(it) }
                }
                // Menetapkan daftar film pada adapter untuk ditampilkan di RecyclerView
                adapter.setMovies(list)
            }
            override fun onCancelled(error: DatabaseError) {
                // Menampilkan pesan kesalahan jika terjadi kesalahan pada database
                Toast.makeText(requireContext(),
                    error.message, Toast.LENGTH_SHORT).show()
            }
        })
        return binding.root
    }
}