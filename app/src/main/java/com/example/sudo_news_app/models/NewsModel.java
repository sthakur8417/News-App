package com.example.sudo_news_app.models;

import android.content.res.Resources;

import com.example.sudo_news_app.MainActivity;
import com.example.sudo_news_app.R;

import java.util.HashMap;
import java.util.Map;

public class NewsModel {

    String desc_newsModel, time_newsModel, newsUrl_newsModel, urlImage_newsModel, name_newsModel;
    String id_newsModel, category_newsModel, title_newsModel, author_newsModel;
    private Map<String, Integer> colors_hashMap;

    public int getColors_hashMap(String type) {
        return colors_hashMap.get(type);
    }

    public void setColors_hashMap(MainActivity mainActivity) {
        colors_hashMap = new HashMap<>();
        colors_hashMap.put("business", mainActivity.getResources().getColor(R.color.colorArr1));
        colors_hashMap.put("general", mainActivity.getResources().getColor(R.color.colorArr2));
        colors_hashMap.put("entertainment", mainActivity.getResources().getColor(R.color.colorArr3));
        colors_hashMap.put("health", mainActivity.getResources().getColor(R.color.colorArr4));
        colors_hashMap.put("technology", mainActivity.getResources().getColor(R.color.colorArr5));
        colors_hashMap.put("science", mainActivity.getResources().getColor(R.color.colorArr7));
        colors_hashMap.put("sports", mainActivity.getResources().getColor(R.color.purple_500));
    }

    public String getDesc_newsModel() {
        return desc_newsModel;
    }

    public void setDesc_newsModel(String desc_newsModel) {
        this.desc_newsModel = desc_newsModel;
    }

    public String getTime_newsModel() {
        return time_newsModel;
    }

    public void setTime_newsModel(String time_newsModel) {
        this.time_newsModel = time_newsModel;
    }

    public String getNewsUrl_newsModel() {
        return newsUrl_newsModel;
    }

    public void setNewsUrl_newsModel(String newsUrl_newsModel) {
        this.newsUrl_newsModel = newsUrl_newsModel;
    }

    public String getUrlImage_newsModel() {
        return urlImage_newsModel;
    }

    public void setUrlImage_newsModel(String urlImage_newsModel) {
        this.urlImage_newsModel = urlImage_newsModel;
    }

    public String getName_newsModel() {
        return name_newsModel;
    }

    public void setName_newsModel(String name_newsModel) {
        this.name_newsModel = name_newsModel;
    }

    public String getId_newsModel() {
        return id_newsModel;
    }

    public void setId_newsModel(String id_newsModel) {
        this.id_newsModel = id_newsModel;
    }

    public String getCategory_newsModel() {
        return category_newsModel;
    }

    public void setCategory_newsModel(String category_newsModel) {
        this.category_newsModel = category_newsModel;
    }

    public String getTitle_newsModel() {
        return title_newsModel;
    }

    public void setTitle_newsModel(String title_newsModel) {
        this.title_newsModel = title_newsModel;
    }

    public String getAuthor_newsModel() {
        return author_newsModel;
    }

    public void setAuthor_newsModel(String author_newsModel) {
        this.author_newsModel = author_newsModel;
    }

    @Override
    public String toString() {
        return "NewsModel{" +
                "title='" + title_newsModel + '\'' +
                ", desc='" + desc_newsModel + '\'' +
                ", time='" + time_newsModel + '\'' +
                ", author='" + author_newsModel + '\'' +
                ", urlImage='" + urlImage_newsModel + '\'' +
                '}';
    }

}
