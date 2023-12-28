package com.example.cinemate.user

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.cinemate.databinding.MoviesItemBinding
import com.example.cinemate.model.Movie

class MovieAdapter(val onItemClickCallback: OnItemClickCallback =
    object : OnItemClickCallback {override fun onItemClicked(data: Movie) {}})
    : RecyclerView.Adapter<MovieAdapter.UserViewHolder>() {

    // Daftar film yang akan ditampilkan di RecyclerView
    private val movies = ArrayList<Movie>()

    // Metode untuk mengatur daftar film yang akan ditampilkan di RecyclerView
    fun setMovies(movies: ArrayList<Movie>){
        this.movies.clear()
        this.movies.addAll(movies)
        notifyDataSetChanged()
    }

    // Kelas ViewHolder yang merepresentasikan setiap elemen tampilan item film di RecyclerView
    inner class UserViewHolder(private val bind: MoviesItemBinding) : RecyclerView.ViewHolder(bind.root){
        // Metode untuk mengikat data film ke elemen tampilan ViewHolder
        fun bind(movie: Movie){
            bind.apply {
                // Menggunakan Glide untuk memuat gambar poster film dari URL
                Glide.with(itemView)
                    .load(movie.imageUrl)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .centerCrop()
                    .into(gridPoster)
                gridTitle.text=movie.title
                // Menangani klik pada elemen tampilan item untuk memberi tahu pemanggil
                root.setOnClickListener {
                    onItemClickCallback?.onItemClicked(movie)
                }
            }
        }
    }

    // Metode untuk membuat ViewHolder saat RecyclerView membutuhkannya
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        // Membuat ViewHolder menggunakan layout MoviesItemBinding
        val view = MoviesItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder((view))
    }

    // Metode untuk mendapatkan jumlah total item dalam RecyclerView
    override fun getItemCount(): Int = movies.size

    // Metode untuk mengikat data film ke ViewHolder pada posisi tertentu
    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(movies[position])
    }

    // Interface untuk mendefinisikan callback ketika item film diklik
    interface OnItemClickCallback{
        fun onItemClicked(data: Movie)
    }
}