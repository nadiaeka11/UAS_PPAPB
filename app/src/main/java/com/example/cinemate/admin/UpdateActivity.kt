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

        binding.edtPoster.setOnClickListener {
            // Tambahkan logika pemilihan gambar di sini
        }

        binding.submitEdt.setOnClickListener {
            val edtTitle = binding.edtTitle.text.toString()
            val edtDesc = binding.edtDesc.text.toString()

            if (imageUri != null) {
                updateData(imageUri!!, edtTitle, edtDesc)
            } else {
                Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun updateData(posterUri: Uri, title: String, description: String) {
        database = FirebaseDatabase.getInstance().getReference("Images")
        val newData = mapOf(
            "title" to title,
            "description" to description,
            "image" to posterUri.toString()
        )

        val moviesData = "moviesData"

        database.child(moviesData).updateChildren(newData).addOnSuccessListener {
            binding.edtTitle.text.clear()
            binding.edtDesc.text.clear()
            Toast.makeText(this,"Successfully Updated",Toast.LENGTH_SHORT).show()
        }.addOnFailureListener{
            Toast.makeText(this,"Failed to Update",Toast.LENGTH_SHORT).show()
        }
    }
}