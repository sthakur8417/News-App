package com.example.sudo_news_app;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.sudo_news_app.adapters.MainActivityAdapter;
import com.example.sudo_news_app.models.NewsModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    final String apiUrl_sources = "https://newsapi.org/v2/top-headlines/sources";
    final String apiUrl_articles = "https://newsapi.org/v2/top-headlines";
    final String apiKey ="d29671b45612474db5230b0539e220b2";
    final String headKey = "User-Agent";
    final String headVal = "News-App";

    ArrayAdapter<String> arrayAdapter;
    MainActivityAdapter mainActivityAdapter;

    DrawerLayout drawer_layout;
    ListView listView;
    ActionBarDrawerToggle actionBarDrawerToggle;
    RecyclerView recyclerView;

    NewsModel newsColorModel;
    NewsModel[] newsSourceArray;
    String[] sourcesArray;

    ArrayList<NewsModel> articlesList;

    private static List<NewsModel> newsArticleList = new ArrayList<>();
    private static List<NewsModel> newsList = new ArrayList<>();

    private Menu menu_topics;

    Uri.Builder builder;
    Response.Listener<JSONObject> objectListener;
    Response.ErrorListener errorListener;
    JsonObjectRequest objectRequest;


    Map<Integer, String> hashMapFilter = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawer_layout = findViewById(R.id.id_main_layout);
        listView = findViewById(R.id.id_orientation);
        recyclerView = findViewById(R.id.id_news_app);
        hashMapFilter.put(1, "all");
        newsColorModel = new NewsModel();
        newsColorModel.setColors_hashMap(this);

        getDataFromApi();

        listView.setOnItemClickListener((parent, view, position, id) -> getArticleItemData(position));
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer_layout, R.string.navigation_open, R.string.navigation_close);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);getSupportActionBar().setHomeButtonEnabled(true);
        }
    }

    private void getDataFromApi() {
        RequestQueue queueSource = Volley.newRequestQueue(this);
        builder = Uri.parse(apiUrl_sources).buildUpon();
        builder.appendQueryParameter("apiKey", apiKey);

        String str_url= builder.build().toString();

        objectListener= response -> parseResponseToJson(response.toString());
        errorListener= error -> updateSourceNewsData(null);

        objectRequest= new JsonObjectRequest(Request.Method.GET, str_url, null,objectListener,errorListener) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put(headKey, headVal);
                return headers;
            }
        };
        queueSource.add(objectRequest);
    }

    private void parseResponseToJson(String responseStr) {
        try {
            JSONArray JSONArray_sources = new JSONObject(responseStr).getJSONArray("sources");

            for(int x = 0; x < JSONArray_sources.length(); x++) {

                NewsModel temp_newsModel = new NewsModel();
                temp_newsModel.setId_newsModel(((JSONObject) JSONArray_sources.get(x)).getString("id"));
                temp_newsModel.setName_newsModel(((JSONObject) JSONArray_sources.get(x)).getString("name"));
                temp_newsModel.setCategory_newsModel(((JSONObject) JSONArray_sources.get(x)).getString("category"));
                newsList.add(temp_newsModel);
            }
            updateSourceNewsData(newsList);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateSourceNewsData(List<NewsModel> newsList){
        if(!newsList.isEmpty()) {
            newsArticleList = new ArrayList<>(newsList);
            sourcesArray = new String[newsList.size()];

            for (int x = 0; x < sourcesArray.length; x++) {
                sourcesArray[x] = newsList.get(x).getName_newsModel();
            }
            this.setTitle("News Gateway " + "(" + sourcesArray.length + ")");

            setListViewAdapter(sourcesArray,newsList);

            NewsModel[] newsModelData = new NewsModel[newsList.size()];
            for (int x = 0; x < newsList.size(); x++) {
                newsModelData[x] = newsList.get(x);
            }
            this.newsSourceArray = newsModelData;
            setDataToMenu(newsModelData);
        }
    }

    private void setListViewAdapter(String[] sourcesArray, List<NewsModel> newsList) {
        arrayAdapter = new ArrayAdapter<String>(this, R.layout.layout_drawer, sourcesArray) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View getView = super.getView(position, convertView, parent);

                ((TextView) getView
                        .findViewById(R.id.layout_drawer))
                        .setTextColor(newsColorModel.getColors_hashMap(newsList.get(position).getCategory_newsModel()));
                return getView;
            }
        };
        listView.setAdapter(arrayAdapter);
    }

    @SuppressLint("ResourceAsColor")
    private void setDataToMenu(NewsModel[] newsModelData) {
        Arrays.stream(newsModelData).map(NewsModel::getCategory_newsModel).distinct().forEach((topicString) -> {
            SpannableString menuString;
            menuString = new SpannableString(topicString);
            menuString.setSpan(new ForegroundColorSpan(newsColorModel.getColors_hashMap(topicString)), 0, menuString.length(), 0);
            menu_topics.add(1, 0, 0, menuString);
        });
    }

    private void getArticleItemData(int position) {

        this.setTitle(newsArticleList.get(position).getName_newsModel());
        getDataFromApiArticles(newsArticleList.get(position).getId_newsModel());

        findViewById(R.id.id_activity).setBackgroundColor(Color.parseColor("#ffffff"));
        drawer_layout.closeDrawer(listView);
    }

    private void getDataFromApiArticles(String id_newsModel){

        RequestQueue queueArticles = Volley.newRequestQueue(this);

        builder = Uri.parse(apiUrl_articles).buildUpon();
        builder.appendQueryParameter("sources", id_newsModel);
        builder.appendQueryParameter("apiKey", apiKey);
        String str_url = builder.build().toString();

        objectListener = response -> parseResponseToJsonArticles(response.toString());
        errorListener = error -> updateArticlesNewsData(null);

        objectRequest = new JsonObjectRequest(Request.Method.GET, str_url, null,objectListener,errorListener) {
            @Override
            public Map<String, String> getHeaders()  {
                Map<String, String> headers = new HashMap<>();
                headers.put(headKey, headVal);
                return headers;
            }
        };
        queueArticles.add(objectRequest);
    }

    private void parseResponseToJsonArticles(String response) {
        articlesList = new ArrayList<>();
        try {
            JSONArray newsArticles = new JSONObject(response).getJSONArray("articles");

            for(int x = 0; x < newsArticles.length(); x++) {

                NewsModel temp_articleModel = new NewsModel();
                temp_articleModel.setTitle_newsModel(((JSONObject) newsArticles.get(x)).getString("title"));
                temp_articleModel.setAuthor_newsModel(((JSONObject) newsArticles.get(x)).getString("author"));
                temp_articleModel.setDesc_newsModel(((JSONObject) newsArticles.get(x)).getString("description"));
                temp_articleModel.setTime_newsModel(((JSONObject) newsArticles.get(x)).getString("publishedAt"));
                temp_articleModel.setUrlImage_newsModel(((JSONObject) newsArticles.get(x)).getString("urlToImage"));
                temp_articleModel.setNewsUrl_newsModel(((JSONObject) newsArticles.get(x)).getString("url"));
                articlesList.add(temp_articleModel);
            }
            updateArticlesNewsData(articlesList);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void updateArticlesNewsData(ArrayList<NewsModel> articlesList) {
        if(!articlesList.isEmpty()) {
            this.articlesList = articlesList;
            mainActivityAdapter = new MainActivityAdapter(this);

            mainActivityAdapter.updateNewsModelList(articlesList);
            recyclerView.setAdapter(mainActivityAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu_topics = menu;
        menu_topics.add(1, 0, 0, "all");
        return true;
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem menuItemitem) {

        if (actionBarDrawerToggle.onOptionsItemSelected(menuItemitem)) {
            Log.d("MainActivity", "onOptionsItemSelected: Issue with the DrawerToggle " + menuItemitem);
            return true;
        }
        updateArticleData(menuItemitem.getTitle().toString(), menuItemitem.getGroupId());
        return super.onOptionsItemSelected(menuItemitem);
    }

    private void updateArticleData(String menuItemTitle, int menuItemId) {
        if(menuItemId!=0)
        {
            String previousMapValue = hashMapFilter.get(menuItemId);
            hashMapFilter.put(menuItemId, menuItemTitle);

            NewsModel[] filteredNewsSources;
            filteredNewsSources = Arrays.stream(newsSourceArray)
                    .filter(source -> (source.getCategory_newsModel().
                            equals(hashMapFilter.get(1)) || hashMapFilter.get(1).equals("all")))
                    .toArray(NewsModel[]::new);

            newsArticleList = new ArrayList<>();
            for (int x = 0; x < filteredNewsSources.length; x++) {
                newsArticleList.add(filteredNewsSources[x]);
            }

            String[] nameSourceArray = Arrays.stream(filteredNewsSources).map(NewsModel::getName_newsModel).toArray(String[]::new);

            if (nameSourceArray.length == 0) {
                new AlertDialog.Builder(this)
                        .setTitle("Sources not Found")
                        .setMessage("No news sources can be found")
                        .setPositiveButton("OK", (dialog, which) -> hashMapFilter.put(menuItemId, previousMapValue)).show();

            } else {

                sourcesArray = nameSourceArray;
                setArticleListViewAdapter(sourcesArray, filteredNewsSources);
                listView.setAdapter(arrayAdapter);
                arrayAdapter.notifyDataSetChanged();
                setTitle("News Gateway (" + sourcesArray.length + ")");
            }
        }
    }

    private void setArticleListViewAdapter(String[] sourcesArray, NewsModel[] filteredNewsSources) {
        arrayAdapter = new ArrayAdapter<String>(this, R.layout.layout_drawer, sourcesArray) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View getView = super.getView(position, convertView, parent);

                ((TextView) getView
                        .findViewById(R.id.layout_drawer))
                        .setTextColor(newsColorModel.getColors_hashMap(filteredNewsSources[position].getCategory_newsModel()));
                return getView;
            }
        };
    }


    @Override
    public void onClick(View view) {
        int position;
        position = recyclerView.getChildAdapterPosition(view);
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(articlesList.get(position).getNewsUrl_newsModel())));
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        actionBarDrawerToggle.onConfigurationChanged(newConfig);
    }
}