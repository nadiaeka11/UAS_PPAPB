package com.example.cinemate.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MovieDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertMovie(movie: MovieEntity)

    @Query("DELETE FROM movie_table WHERE id = :key")
    fun deleteMovie(key: String)

    @Query("SELECT * from movie_table WHERE uid = :uid")
    fun getMovies(uid:String): List<MovieEntity>

    @Query("SELECT * FROM movie_table WHERE id = :key")
    fun getDataByKey(key: String): MovieEntity
}