package com.example.cinemateadmin

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
import com.example.cinemateadmin.databinding.ActivityUploadBinding
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

        submitAdd = findViewById<Button>(R.id.submitAdd)
        addTitle = findViewById<EditText>(R.id.addTitle)
        addDesc = findViewById<EditText>(R.id.addDesc)
        addPoster = findViewById<ImageView>(R.id.addPoster)
        progressBar = findViewById<ProgressBar>(R.id.progressBar)
        progressBar?.visibility = View.INVISIBLE

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

        addPoster?.setOnClickListener{
            val photoPicker = Intent()
            photoPicker.action = Intent.ACTION_GET_CONTENT
            photoPicker.type = "image/*"
            activityResultLauncher.launch(photoPicker)
        }
        submitAdd?.setOnClickListener{
            if (imageUri != null) {
                uploadToFirebase(imageUri!!)
            } else {
                Toast.makeText(this@UploadActivity, "Please select image", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    //Outside onCreate
    private fun uploadToFirebase(uri: Uri) {
        val title = addTitle?.text.toString()
        val caption = addDesc?.text.toString()
        val imageReference: StorageReference = storageReference.child(
            System.currentTimeMillis().toString() + "." + getFileExtension(uri)
        )

        imageReference.putFile(uri).addOnSuccessListener { taskSnapshot ->
            imageReference.downloadUrl.addOnSuccessListener { downloadUri ->
                val dataClass = DataClass(downloadUri.toString(), title, caption)
                val key = databaseReference.push().key
                if (key != null) {
                    databaseReference.child(key).setValue(dataClass)
                }
                progressBar?.visibility = View.INVISIBLE
                Toast.makeText(this@UploadActivity, "Uploaded", Toast.LENGTH_SHORT).show()
                val intent = Intent(
                    this@UploadActivity,
                    MainActivity::class.java
                )
                startActivity(intent)
                finish()
            }
        }.addOnProgressListener(object : OnProgressListener<UploadTask.TaskSnapshot?> {
            override fun onProgress(snapshot: UploadTask.TaskSnapshot) {
                progressBar?.visibility = View.VISIBLE
            }
        }).addOnFailureListener {
            progressBar?.visibility = View.INVISIBLE
            Toast.makeText(this@UploadActivity, "Failed", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getFileExtension(fileUri: Uri): String? {
        val contentResolver = contentResolver
        val mime = MimeTypeMap.getSingleton()
        return mime.getExtensionFromMimeType(contentResolver.getType(fileUri))
    }
}