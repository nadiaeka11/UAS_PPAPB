package com.example.cinemate.admin

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.cinemate.R
import com.example.cinemate.databinding.ActivityUploadBinding
import com.example.cinemate.model.Movie
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.OnProgressListener
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask


class UploadActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUploadBinding

    private var submitAdd: Button? = null
    private var addPoster: ImageView? = null
    private var addTitle: EditText? = null
    private var addDesc: EditText? = null
    var progressBar: ProgressBar? = null
    private var imageUri: Uri? = null
    private val databaseReference = FirebaseDatabase.getInstance().getReference("Images")
    private val storageReference: StorageReference = FirebaseStorage.getInstance().getReference()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inisialisasi elemen-elemen antarmuka pengguna
        submitAdd = findViewById<Button>(R.id.submitAdd)
        addTitle = findViewById<EditText>(R.id.addTitle)
        addDesc = findViewById<EditText>(R.id.addDesc)
        addPoster = findViewById<ImageView>(R.id.addPoster)
        progressBar = findViewById<ProgressBar>(R.id.progressBar)
        progressBar?.visibility = View.INVISIBLE

        // Membuat launcher untuk memilih gambar dari penyimpanan
        val activityResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult(),
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                imageUri = data?.data
                addPoster?.setImageURI(imageUri)
            } else {
                Toast.makeText(this@UploadActivity, "No Image Selected", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        // Menetapkan OnClickListener untuk pemilihan gambar
        addPoster?.setOnClickListener{
            val photoPicker = Intent()
            photoPicker.action = Intent.ACTION_GET_CONTENT
            photoPicker.type = "image/*"
            activityResultLauncher.launch(photoPicker)
        }
        // Menetapkan OnClickListener untuk tombol submit
        submitAdd?.setOnClickListener{
            // Memastikan URI gambar tidak null sebelum mengunggah
            if (imageUri != null) {
                uploadToFirebase(imageUri!!)
            } else {
                Toast.makeText(this@UploadActivity, "Please select image", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    // Fungsi untuk mengunggah gambar ke penyimpanan Firebase dan data ke database Firebase
    private fun uploadToFirebase(uri: Uri) {
        val title = addTitle?.text.toString()
        val caption = addDesc?.text.toString()

        // Mendapatkan referensi penyimpanan Firebase untuk gambar
        val imageReference: StorageReference = storageReference.child(
            System.currentTimeMillis().toString() + "." + getFileExtension(uri)
        )
        // Menampilkan ProgressBar selama pengunggahan
        progressBar?.visibility = View.VISIBLE

        // Mengunggah gambar ke penyimpanan Firebase
        imageReference.putFile(uri).addOnSuccessListener {
            // Jika pengunggahan gambar berhasil, mendapatkan URL gambar dari penyimpanan Firebase
            imageReference.downloadUrl.addOnSuccessListener { downloadUri ->

                // Mendapatkan kunci unik untuk data film dalam database
                val key = databaseReference.push().key

                // Memastikan kunci tidak null sebelum menambahkan data ke database
                if (key != null) {
                    val dataClass = Movie(key,downloadUri.toString(), title, caption)
                    databaseReference.child(key).setValue(dataClass)
                }

                // Menyembunyikan ProgressBar setelah pengunggahan berhasil
                progressBar?.visibility = View.INVISIBLE

                // Menampilkan pesan sukses
                Toast.makeText(this@UploadActivity, "Uploaded", Toast.LENGTH_SHORT).show()

                // Kembali ke halaman AdminActivity setelah pengunggahan berhasil
                val intent = Intent(
                    this@UploadActivity,
                    AdminActivity::class.java
                )
                startActivity(intent)
                finish()
            }
        }.addOnProgressListener {
            // Menampilkan ProgressBar selama proses pengunggahan
            progressBar?.visibility = View.VISIBLE }.addOnFailureListener {
            // Menyembunyikan ProgressBar dan menampilkan pesan kesalahan jika pengunggahan gagal
            progressBar?.visibility = View.INVISIBLE
            Toast.makeText(this@UploadActivity, "Failed", Toast.LENGTH_SHORT).show()
        }
    }

    // Fungsi untuk mendapatkan ekstensi file dari Uri gambar
    private fun getFileExtension(fileUri: Uri): String? {
        val contentResolver = contentResolver
        val mime = MimeTypeMap.getSingleton()
        return mime.getExtensionFromMimeType(contentResolver.getType(fileUri))
    }
}