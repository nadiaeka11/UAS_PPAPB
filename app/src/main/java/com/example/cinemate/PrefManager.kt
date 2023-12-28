package com.example.cinemate

import android.content.Context
import android.content.SharedPreferences

class  PrefManager private constructor(context: Context) {
    private val sharedPreferences: SharedPreferences
    companion object {
        private const val PREFS_FILENAME = "AuthAppPrefs"
        private const val KEY_LEVEL = "level"
        private const val ADMIN = "admin"
        private const val PUBLIC = "public"

        // Objek yang digunakan untuk menerapkan pola Singleton pada kelas PrefManager
        @Volatile
        private var instance: PrefManager? = null

        // Metode untuk mendapatkan instance dari PrefManager dengan menerapkan pola Singleton
        fun getInstance(context: Context): PrefManager {
            return instance ?: synchronized(this) {
                instance ?: PrefManager(context.applicationContext).also {
                    instance = it
                }
            }
        }
    }
    // Inisialisasi kelas PrefManager dan mendapatkan instance SharedPreferences
    init {
        sharedPreferences = context.getSharedPreferences(PREFS_FILENAME,
            Context.MODE_PRIVATE)
    }
    // Metode untuk mengatur status pengguna sebagai admin
    fun setAsAdmin() {
        val editor = sharedPreferences.edit()
        editor.putString(KEY_LEVEL, ADMIN)
        editor.apply()
    }
    // Metode untuk mengatur status pengguna sebagai pengguna umum
    fun setAsPublic() {
        val editor = sharedPreferences.edit()
        editor.putString(KEY_LEVEL, PUBLIC)
        editor.apply()
    }

    // Metode untuk memeriksa apakah pengguna saat ini adalah admin
    fun isAdmin(): Boolean {
        return sharedPreferences.getString(KEY_LEVEL,"").equals(ADMIN)
    }

    // Metode untuk memeriksa apakah pengguna saat ini adalah pengguna umum
    fun isPublic(): Boolean {
        return sharedPreferences.getString(KEY_LEVEL,"").equals(PUBLIC)
    }

    // Metode untuk menghapus semua data yang disimpan dalam SharedPreferences
    fun clear() {
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
    }
}