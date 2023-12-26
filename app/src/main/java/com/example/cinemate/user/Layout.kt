package com.example.cinemate.user

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.GridView
import androidx.fragment.app.Fragment
import com.example.cinemate.MyAdapter
import com.example.cinemate.R
import com.example.cinemate.databinding.ActivityLayoutBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Layout : AppCompatActivity() {
    private lateinit var binding: ActivityLayoutBinding
    private lateinit var gridView: GridView
    private var dataList: ArrayList<DataClass> = ArrayList()
    private lateinit var adapter: MyAdapter
    private val databaseReference: DatabaseReference = FirebaseDatabase.getInstance().getReference("Images")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Menghubungkan layout ActivityHomeBinding dengan kode Kotlin
        binding = ActivityLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragment(HomeFragment())

        // Menampilkan data username di elemen editUsername pada layout
        with(binding) {
            bottomNavbar.setOnItemSelectedListener() {
                // Menangani pemilihan item pada BottomNavigationView
                when (it.itemId) {
                    R.id.itemHome -> replaceFragment(HomeFragment())
                    R.id.itemFav -> replaceFragment(FavFragment())
                    R.id.itemAccount -> replaceFragment(AccountFragment())
                    else -> {}
                }
                true
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
    // Fungsi untuk menggantikan fragmen yang ditampilkan di dalam tampilan dengan id "frameLayout"
    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        // Menggantikan fragmen saat ini dengan fragmen yang baru
        fragmentTransaction.replace(R.id.frameLayout, fragment)
        // Menyelesaikan transaksi fragmen
        fragmentTransaction.commit()
    }
}