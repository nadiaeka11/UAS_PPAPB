package com.example.cinemate.admin

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.cinemate.databinding.ActivityUpdateBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class UpdateActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUpdateBinding
    private lateinit var database: DatabaseReference
    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set OnClickListener untuk pemilihan gambar
        binding.edtPoster.setOnClickListener {
            // Tambahkan logika pemilihan gambar di sini
        }

        // Set OnClickListener untuk tombol submit
        binding.submitEdt.setOnClickListener {
            val edtTitle = binding.edtTitle.text.toString()
            val edtDesc = binding.edtDesc.text.toString()

            // Memastikan URI gambar tidak null sebelum melakukan pembaruan
            if (imageUri != null) {
                updateData(imageUri!!, edtTitle, edtDesc)
            } else {
                Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show()
            }
        }
    }
    // Fungsi untuk memperbarui data ke database Firebase
    private fun updateData(posterUri: Uri, title: String, description: String) {
        database = FirebaseDatabase.getInstance().getReference("Images")

        // Menyiapkan data baru yang akan diperbarui
        val newData = mapOf(
            "title" to title,
            "description" to description,
            "image" to posterUri.toString()
        )

        // Nama node dalam database yang berisi data film
        val moviesData = "moviesData"

        // Memperbarui data dalam database
        database.child(moviesData).updateChildren(newData).addOnSuccessListener {
            // Mengosongkan input setelah pembaruan berhasil
            binding.edtTitle.text.clear()
            binding.edtDesc.text.clear()
            Toast.makeText(this,"Successfully Updated",Toast.LENGTH_SHORT).show()
        }.addOnFailureListener{
            // Menampilkan pesan kesalahan jika pembaruan gagal
            Toast.makeText(this,"Failed to Update",Toast.LENGTH_SHORT).show()
        }
    }
}