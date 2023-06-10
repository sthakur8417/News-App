package com.example.sudo_news_app.adapters;

import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sudo_news_app.MainActivity;
import com.example.sudo_news_app.R;
import com.example.sudo_news_app.imagesHandler.ImageHandler;
import com.example.sudo_news_app.models.NewsModel;
import com.example.sudo_news_app.viewholders.MainActivityViewHolder;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;

public class MainActivityAdapter extends RecyclerView.Adapter<MainActivityViewHolder> {

    MainActivity mainActivity;
    ArrayList<NewsModel> newsModelArrayList;
    String publishedDate;
    DateTimeFormatter timeOnly;
    DateTimeFormatter dateTimeOnly;
    LocalDateTime ldt;
    TemporalAccessor temporalAccessor;

    public MainActivityAdapter(MainActivity mainActivity){
        this.mainActivity = mainActivity;
    }

    @NonNull
    @Override
    public MainActivityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewHolder = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_adapter_app, parent, false);
        viewHolder.setOnClickListener((View.OnClickListener) mainActivity);
        return new MainActivityViewHolder(viewHolder);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull MainActivityViewHolder holder, int position) {

        holder.id_header.setText(newsModelArrayList.get(position).getTitle_newsModel());
        holder.id_writer.setText(newsModelArrayList.get(position).getAuthor_newsModel());
        holder.id_min.setText(newsModelArrayList.get(position).getDesc_newsModel());
        String count=(position + 1)+" of "+newsModelArrayList.size();
        holder.id_count_of.setText(count);
        publishedDate = dateFormaterFunction(newsModelArrayList.get(position).getTime_newsModel());
        holder.id_date.setText(publishedDate);

        if (!newsModelArrayList.get(position).getUrlImage_newsModel().equals("null")) {
            holder.id_photo_news.setImageResource(R.drawable.loading);
            new ImageHandler(holder.id_photo_news, mainActivity).execute(newsModelArrayList.get(position).getUrlImage_newsModel());
        }
    }

    @Override
    public int getItemCount() {
        return newsModelArrayList.size();
    }

    public void updateNewsModelList(ArrayList<NewsModel> newsModelArrayList){
        this.newsModelArrayList = newsModelArrayList;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String dateFormaterFunction(String date) {
        String local_date = "";
        local_date = tryDateISOInstant(date);

        if(local_date.isEmpty()) {
            local_date = tryDateISOOffset(date);
        }
        return local_date;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private String tryDateISOOffset(String date) {
        String local_date;
        try {
            timeOnly = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
            temporalAccessor = timeOnly.parse(date);

            dateTimeOnly  = DateTimeFormatter.ofPattern("LLL dd, yyyy kk:mm");
            ldt = LocalDateTime.ofInstant(Instant.from(temporalAccessor), ZoneId.systemDefault());
            local_date = ldt.format(dateTimeOnly);

            return local_date;
        } catch (Exception e) {
            Log.d("MainActivityAdapter","Issue with tryDateISOOffset"+e.getLocalizedMessage());
            return "";
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private String tryDateISOInstant(String date) {
        String local_date;
        try {
            timeOnly= DateTimeFormatter.ISO_INSTANT;
            temporalAccessor = timeOnly.parse(date);

            dateTimeOnly  = DateTimeFormatter.ofPattern("LLL dd, yyyy kk:mm");
            ldt = LocalDateTime.ofInstant(Instant.from(temporalAccessor), ZoneId.systemDefault());
            local_date = ldt.format(dateTimeOnly);

            return local_date;
        } catch (Exception e) {
            Log.d("MainActivityAdapter","Issue with tryDateISOInstant"+e.getLocalizedMessage());
            return "";
        }
    }
}
