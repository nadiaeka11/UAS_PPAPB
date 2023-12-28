package com.example.cinemate.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movie_table")
data class MovieEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val key: String,
    @ColumnInfo(name = "uid")
    val uid: String,
    @ColumnInfo(name = "image")
    val imageURL: String,
    @ColumnInfo(name = "title")
    val title: String
)