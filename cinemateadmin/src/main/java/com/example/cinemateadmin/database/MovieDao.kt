package com.example.cinemateadmin.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

// Data Access Object (DAO) untuk operasi-operasi database terkait Note
@Dao
interface MovieDao {
    // Menyisipkan data movie ke dalam database
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertMovie(movie: MovieEntity)

    // Memperbarui catatan dalam database
    @Update
    fun updateMovie(movie: MovieEntity)

    // Menghapus catatan dari database
    @Delete
    fun deleteAllMovies(movie: MovieEntity)

    // Mengambil semua catatan dari database secara live (menggunakan LiveData)
    @get:Query("SELECT * from movie_table ORDER BY id ASC")
    val getAllMovies: LiveData<List<MovieEntity>>
}