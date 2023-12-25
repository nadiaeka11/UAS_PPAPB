package com.example.cinemate

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.GridView
import com.example.cinemate.databinding.ActivityHomeBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var gridView: GridView
    private var dataList: ArrayList<DataClass> = ArrayList()
    private lateinit var adapter: MyAdapter
    private val databaseReference: DatabaseReference = FirebaseDatabase.getInstance().getReference("Images")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Menghubungkan layout ActivityHomeBinding dengan kode Kotlin
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Mengambil data yang dikirim dari LoginActivity
        val usn = intent.getStringExtra(Layout.EXT_USN)

        // Menampilkan data username di elemen editUsername pada layout
        with(binding) {
            editUsername.text = usn

            // Menambahkan event listener pada elemen poster3 (dapat disesuaikan)
            gridView.setOnClickListener {
                // Membuat Intent untuk berpindah ke DetailsActivity
                val intentView = Intent(this@HomeActivity, DetailsActivity::class.java)
                // Menjalankan Intent untuk berpindah ke DetailsActivity
                startActivity(intentView)
            }
        }

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                dataList.clear()
                for (dataSnapshot in snapshot.children) {
                    val dataClass = dataSnapshot.getValue(DataClass::class.java)
                    dataList.add(dataClass!!)
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle onCancelled event
            }
        })
    }
}