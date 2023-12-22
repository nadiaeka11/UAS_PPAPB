package com.example.cinemateadmin

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cinemateadmin.databinding.RecyclerItemBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {
    private lateinit var binding: RecyclerItemBinding
    private var fab: FloatingActionButton? = null
    private var recyclerView: RecyclerView? = null
    private var dataList: ArrayList<DataClass?>? = null
    private var adapter: MyAdapter? = null
    private val databaseReference = FirebaseDatabase.getInstance().getReference("Images")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = RecyclerItemBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fab = findViewById(R.id.fab)
        recyclerView = findViewById(R.id.recyclerView)

        recyclerView?.setHasFixedSize(true)
        recyclerView?.layoutManager = LinearLayoutManager(this)
        dataList = ArrayList()
        adapter = MyAdapter(this, dataList)
        recyclerView?.adapter = adapter

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (dataSnapshot in snapshot.children) {
                    val dataClass = dataSnapshot.getValue(DataClass::class.java)
                    dataList?.add(dataClass)
                }
                adapter?.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {}
        })

        adapter?.setOnItemClickListener(object : MyAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                // Handle item click here, if needed
            }
        })

        binding.edtButton.setOnClickListener {
            val intent = Intent(this@MainActivity, UpdateActivity::class.java)
            startActivity(intent)
        }

        binding.delButton.setOnClickListener {
            // Handle delete button click here
            if (adapter?.itemCount ?: 0 > 0) {
                val position = 0 // You may change this to the desired position
                dataList?.get(position)?.let { data ->
                    deleteData(data.imageURL) // Assuming phone is a unique identifier
                }
            } else {
                Toast.makeText(this@MainActivity, "No data to delete", Toast.LENGTH_SHORT).show()
            }
        }

        fab?.setOnClickListener {
            val intent = Intent(this@MainActivity, UploadActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun deleteData(imageURL: String) {
        val database = FirebaseDatabase.getInstance().getReference("Images")
        database.orderByChild("imageURL").equalTo(imageURL)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (dataSnapshot in snapshot.children) {
                        dataSnapshot.ref.removeValue()
                    }
                    Toast.makeText(this@MainActivity, "Deleted", Toast.LENGTH_SHORT).show()
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@MainActivity, "Unable to delete", Toast.LENGTH_SHORT).show()
                }
            })
    }
}