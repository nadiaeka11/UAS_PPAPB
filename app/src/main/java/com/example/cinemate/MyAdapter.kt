package com.example.cinemate

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cinemate.user.DataClass

class MyAdapter(private val context: Context, private val dataList: ArrayList<DataClass>) :
    RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.movies_item, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        Glide.with(context).load(dataList[position].imageURL).into(holder.recyclerImage)
        holder.recyclerTitle.text = dataList[position].caption
        Glide.with(context).load(dataList[position].imageURL).into(holder.recyclerFav)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val recyclerImage: ImageView = itemView.findViewById(R.id.gridPoster)
        val recyclerTitle: TextView = itemView.findViewById(R.id.gridTitle)
        val recyclerFav: ImageView = itemView.findViewById(R.id.likeBtn)
    }
}