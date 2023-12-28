package com.example.cinemate.admin

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cinemate.R
import com.example.cinemate.databinding.ActivityAdminBinding
import com.example.cinemate.model.Movie
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AdminActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdminBinding

    // Mendeklarasikan variabel fab, recyclerView, dan adapter
    private var fab: FloatingActionButton? = null
    private var recyclerView: RecyclerView? = null
    private var adapter: MyAdapter? = null
    private val databaseReference = FirebaseDatabase.getInstance().getReference("Images")

    // Mendeklarasikan objek databaseReference untuk mengakses Firebase Realtime Database
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Menginisialisasi fab, recyclerView, dan adapter
        fab = findViewById(R.id.fabAddMovie)

        recyclerView = findViewById(R.id.rv_movie)
        recyclerView?.setHasFixedSize(true)
        recyclerView?.layoutManager = LinearLayoutManager(this)

        val dataList = ArrayList<Movie>()
        adapter = MyAdapter(this, dataList)
        recyclerView?.adapter = adapter

        // Menambahkan ValueEventListener ke databaseReference untuk mendapatkan data dari Firebase Realtime Database
        databaseReference.addValueEventListener(object : ValueEventListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                // Mengosongkan dataList
                dataList!!.clear()

                // Mengambil data dari setiap child dalam snapshot dan menambahkannya ke dataList
                for (dataSnapshot in snapshot.children) {
                    val dataClass = dataSnapshot.getValue(Movie::class.java)
                    dataClass?.let { dataList.add(it) }
                }
                // Memberitahu adapter bahwa data telah berubah
                adapter?.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                // Menampilkan pesan kesalahan jika terjadi error
                Toast.makeText(this@AdminActivity,
                    error.message, Toast.LENGTH_SHORT).show()
            }
        })

        // Menambahkan OnClickListener untuk item pada RecyclerView
        adapter?.setOnItemClickListener {
            val intent = Intent(this@AdminActivity, UpdateActivity::class.java)
            startActivity(intent)
        }

        // Menambahkan OnClickListener untuk tombol hapus pada item RecyclerView
        adapter?.setOnDeleteClickListener { position ->
            deleteData(dataList[position].imageUrl)
        }

        // Menambahkan OnClickListener untuk FAB (Floating Action Button) untuk menambahkan film baru
        fab?.setOnClickListener {
            val intent = Intent(this@AdminActivity, UploadActivity::class.java)
            startActivity(intent)
        }

        // Mengatur judul ActionBar
        supportActionBar?.let {
            it.title = "List Movie"
        }
    }

    // Fungsi untuk menghapus data berdasarkan imageURL
    private fun deleteData(imageURL: String) {
        val database = FirebaseDatabase.getInstance().getReference("Images")

        // Mencari data dengan imageURL tertentu dan menghapusnya
        database.orderByChild("imageURL").equalTo(imageURL)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (dataSnapshot in snapshot.children) {
                        dataSnapshot.ref.removeValue()
                    }
                    Toast.makeText(this@AdminActivity, "Deleted", Toast.LENGTH_SHORT).show()
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@AdminActivity, "Unable to delete", Toast.LENGTH_SHORT).show()
                }
            })
    }
}