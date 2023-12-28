package com.example.cinemate.user

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.cinemate.R
import com.example.cinemate.database.MovieDatabase
import com.example.cinemate.database.MovieEntity
import com.example.cinemate.databinding.ActivityDetailsBinding
import com.example.cinemate.model.Movie
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class DetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailsBinding
    private val databaseReference = FirebaseDatabase.getInstance().getReference("Images")
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Mendapatkan instance FirebaseAuth
        auth = FirebaseAuth.getInstance()

        // Mendapatkan pengguna saat ini
        val currentUser = auth.currentUser

        // Mendapatkan kunci film dari intent
        val key = intent.extras?.getString("key")

        // Mengatur fungsi onClickListener untuk tombol kembali
        with(binding) {
            backButton.setOnClickListener {
                finish()
            }
        }
        // Mendapatkan instance dari MovieDatabase
        val db = MovieDatabase.getDatabase(this)

        // Mengecek apakah film sudah ada di dalam daftar favorit
        val movieFav = db.movieDao()?.getDataByKey(key!!)
        var isFav = movieFav!=null

        // Mengatur gambar ikon favorit berdasarkan status favorit
        if(isFav){
            Glide.with(this).load(R.drawable.baseline_favorite_24).into(binding.btnFav)
        }

        // Mendapatkan data film dari Firebase Realtime Database
        databaseReference.child(key!!).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val movie = snapshot.getValue(Movie::class.java)
                with(binding){
                    // Memuat gambar film menggunakan Glide
                    if (movie != null) {
                        Glide.with(this@DetailsActivity).load(movie.imageUrl).into(poster)
                        judul.text=movie.title
                        sinopsis.text=movie.caption

                        // Mengatur onClickListener untuk tombol favorit
                        btnFav.setOnClickListener{
                            // Menambah atau menghapus film dari daftar favorit
                            if(isFav){
                                // Jika sudah favorit, hapus dari database lokal dan ubah ikon favorit
                                db.movieDao()?.deleteMovie(key)
                                Glide.with(this@DetailsActivity).load(R.drawable.baseline_favorite_border_24).into(binding.btnFav)
                            }else{
                                // Jika belum favorit, tambahkan ke database lokal dan ubah ikon favorit
                                db.movieDao()?.insertMovie(MovieEntity(key,currentUser!!.uid,movie.imageUrl,movie.title))
                                Glide.with(this@DetailsActivity).load(R.drawable.baseline_favorite_24).into(binding.btnFav)
                            }
                            // Mengubah status favorit
                            isFav=!isFav
                        }
                    }

                }
            }
            override fun onCancelled(error: DatabaseError) {
                // Menampilkan pesan kesalahan jika terjadi kesalahan saat mengambil data dari Firebase
                Toast.makeText(this@DetailsActivity,
                    error.message, Toast.LENGTH_SHORT).show()
            }
        })
    }
}