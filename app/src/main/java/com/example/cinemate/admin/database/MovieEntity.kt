package com.example.cinemate.admin.database

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

// Mendefinisikan entitas Note untuk digunakan oleh Room Database
@Entity(tableName = "movie_table")
data class MovieEntity(
    // ID merupakan primary key yang akan di-generate secara otomatis
    @PrimaryKey(autoGenerate = true)
    @NonNull
    val id: Int=0,

    // Kolom date untuk menyimpan tanggal catatan
    @ColumnInfo(name = "image")
    val imageURL: String,

    // Kolom title untuk menyimpan judul catatan
    @ColumnInfo(name = "title")
    val title: String,

    // Kolom description untuk menyimpan deskripsi catatan
    @ColumnInfo(name = "description")
    val description: String
    )