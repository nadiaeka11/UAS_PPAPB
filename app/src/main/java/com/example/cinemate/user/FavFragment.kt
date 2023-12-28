package com.example.cinemate.user

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.example.cinemate.database.MovieDatabase
import com.example.cinemate.databinding.FragmentFavBinding
import com.example.cinemate.databinding.FragmentHomeBinding
import com.example.cinemate.model.Movie
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class FavFragment : Fragment() {
    private lateinit var binding: FragmentFavBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var db:MovieDatabase
    private lateinit var user:FirebaseUser
    private lateinit var adapter:MovieAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFavBinding.inflate(inflater,container,false)
        // Mendapatkan instance FirebaseAuth
        auth = FirebaseAuth.getInstance()
        // Mendapatkan pengguna saat ini
        user = auth.currentUser!!
        // Mendapatkan instance dari MovieDatabase
        db = MovieDatabase.getDatabase(requireContext())

        // Membuat instance adapter untuk RecyclerView
        adapter = MovieAdapter(object :MovieAdapter.OnItemClickCallback{
            override fun onItemClicked(data: Movie) {
                // Membuka DetailsActivity saat item di RecyclerView diklik
                startActivity(Intent(requireContext(),DetailsActivity::class.java).putExtra("key",data.key))
            }
        })
        // Mengatur tata letak dan adapter untuk RecyclerView
        binding.gridView.layoutManager = GridLayoutManager(requireContext(),2)
        binding.gridView.adapter = adapter

        return binding.root
    }

    override fun onStart() {
        super.onStart()

        // Mendapatkan daftar film favorit dari database lokal
        val list=ArrayList<Movie>()
        val listEntity = db.movieDao()?.getMovies(user.uid)

        // Mengonversi daftar MovieEntity menjadi daftar Movie
        if (listEntity != null) {
            for(me in listEntity){
                list.add(Movie(key = me.key, imageUrl = me.imageURL, title = me.title))
            }
        }
        // Mengatur daftar film pada adapter untuk ditampilkan di RecyclerView
        adapter.setMovies(list)
    }
}