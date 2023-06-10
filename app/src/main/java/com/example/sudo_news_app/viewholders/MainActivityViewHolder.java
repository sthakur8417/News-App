package com.example.sudo_news_app.viewholders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sudo_news_app.R;

public class MainActivityViewHolder extends RecyclerView.ViewHolder{

    public TextView id_header, id_writer, id_date, id_count_of, id_min;
    public ImageView id_photo_news;

    public MainActivityViewHolder(@NonNull View itemView) {
        super(itemView);
        id_header = itemView.findViewById(R.id.id_header);
        id_writer = itemView.findViewById(R.id.id_writer);
        id_date = itemView.findViewById(R.id.id_date);
        id_count_of = itemView.findViewById(R.id.id_count_of);
        id_min = itemView.findViewById(R.id.id_min);
        id_photo_news = itemView.findViewById(R.id.id_photo_news);
    }
}
