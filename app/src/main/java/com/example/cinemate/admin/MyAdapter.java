package com.example.cinemate.admin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cinemate.R;
import com.example.cinemate.model.Movie;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    // Interface untuk menangani klik pada item RecyclerView
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    // Interface untuk menangani klik pada tombol hapus item RecyclerView
    public interface OnDeleteClickListener {
        void onDeleteClick(int position);
    }

    private Context context;
    private ArrayList<Movie> dataList;
    private OnItemClickListener itemClickListener;
    private OnDeleteClickListener deleteClickListener;

    // Konstruktor untuk MyAdapter
    public MyAdapter(Context context, ArrayList<Movie> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    // Setter untuk OnItemClickListener
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.itemClickListener = listener;
    }

    // Setter untuk OnDeleteClickListener
    public void setOnDeleteClickListener(OnDeleteClickListener listener) {
        this.deleteClickListener = listener;
    }

    // ViewHolder yang berisi elemen-elemen UI dari setiap item RecyclerView
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView recyclerImage;

        // Konstruktor untuk MyViewHolder
        public MyViewHolder(@NonNull View itemView, final OnItemClickListener itemClickListener,
                            final OnDeleteClickListener deleteClickListener) {
            super(itemView);
            recyclerImage = itemView.findViewById(R.id.recyclerImage);

            // Mendapatkan referensi tombol edit dan tombol hapus
            Button edtButton = itemView.findViewById(R.id.edtButton);
            Button delButton = itemView.findViewById(R.id.delButton);

            // Menambahkan OnClickListener untuk tombol edit
            edtButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (itemClickListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            itemClickListener.onItemClick(position);
                        }
                    }
                }
            });

            // Menambahkan OnClickListener untuk tombol hapus
            delButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (deleteClickListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            deleteClickListener.onDeleteClick(position);
                        }
                    }
                }
            });
        }
    }

    // Membuat ViewHolder baru
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item, parent, false);
        return new MyViewHolder(v, itemClickListener, deleteClickListener);
    }

    // Mengikat data ke ViewHolder
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Glide.with(context).load(dataList.get(position).getImageUrl()).into(holder.recyclerImage);
    }

    // Mendapatkan jumlah item dalam dataset
    @Override
    public int getItemCount() {
        return dataList.size();
    }
}