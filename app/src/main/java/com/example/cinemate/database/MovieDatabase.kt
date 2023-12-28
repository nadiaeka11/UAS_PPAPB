package com.example.cinemate.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase

// Kelas abstrak yang mewakili Room Database dan menyediakan instance dari MovieDao
@Database(entities = [MovieEntity::class], version = 1, exportSchema = false)
abstract class MovieDatabase : RoomDatabase() {

    // Metode abstrak untuk mendapatkan objek DAO (Data Access Object) untuk entitas MovieEntity
    abstract fun movieDao(): MovieDao?

    companion object {
        @Volatile
        private var INSTANCE: MovieDatabase? = null

        // Metode untuk mendapatkan instance dari MovieDatabase
        fun getDatabase(context: Context): MovieDatabase {
            // Memeriksa apakah INSTANCE sudah terinisialisasi
            return INSTANCE ?: synchronized(this) {
                val instance = databaseBuilder(
                    context.applicationContext,
                    MovieDatabase::class.java,
                    "movie_database"
                )
                    // Mengizinkan penggunaan kueri di thread utama (harap digunakan dengan hati-hati)
                    .allowMainThreadQueries()
                    .build()

                // Menetapkan instance ke INSTANCE agar tidak dibuat ulang
                INSTANCE = instance
                instance
            }
        }
    }
}