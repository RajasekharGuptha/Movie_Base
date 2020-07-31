package com.rahtech.popmovies;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements adapter.listItemClickListener,trendtvadapter.onTvClickListener {

    RecyclerView moviesRecyclerView;
    RecyclerView seriesRecyclerView;
    RecyclerView upcomingRecyclerView;
    adapter adapter;
    adapter upcomingadapter;
    trendtvadapter seriesadapter;
    ArrayList<details> detailsArrayList;
    ArrayList<details> seriesDeatilsArrayList;
    ArrayList<details> upcoming;
    String url="https://api.themoviedb.org/3/movie/popular?api_key=<api_key>&language=en-US&page=1";
    String seriesurl="https://api.themoviedb.org/3/tv/popular?api_key=<api_key>&language=en-US&page=1";
    String upcomingurl="https://api.themoviedb.org/3/movie/upcoming?api_key=<api_key>&language=en-US&page=1";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        moviesRecyclerView = findViewById(R.id.moviesList);
        seriesRecyclerView = findViewById(R.id.seriesList);
        upcomingRecyclerView=findViewById(R.id.upcomingMoviesList);
        detailsArrayList= new ArrayList<>();
        seriesDeatilsArrayList= new ArrayList<>();
        upcoming= new ArrayList<>();
        moviesRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        upcomingRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        seriesRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));

        SharedPreferences sharedPreferences=getSharedPreferences(getPackageName(),MODE_PRIVATE);
        String offlineData=sharedPreferences.getString("movies",null);
        String offlineData2=sharedPreferences.getString("series",null);
        String offlineData3=sharedPreferences.getString("upcoming",null);
        if(haveNetworkConnection()) {
            detailsArrayList=new getData().getDataFromUrl(url,"movie");
            seriesDeatilsArrayList=new getData().getDataFromUrl(seriesurl,"tv");
            upcoming=new getData().getDataFromUrl(upcomingurl,"movie");
            Gson gson = new Gson();
            String popmovies = gson.toJson(detailsArrayList);
            sharedPreferences.edit().putString("movies", popmovies).apply();
            sharedPreferences.edit().putString("series", popmovies).apply();
            sharedPreferences.edit().putString("upcoming", popmovies).apply();

        }
        else{
            if(offlineData2!= null && offlineData != null&& offlineData3 != null){
                Gson gson=new Gson();
                Type type= new TypeToken<ArrayList<details>>(){}.getType();
                detailsArrayList=gson.fromJson(offlineData,type);
                seriesDeatilsArrayList=gson.fromJson(offlineData2,type);
            }

        }

        adapter = new adapter(this, detailsArrayList,this);
        seriesadapter = new trendtvadapter(this,seriesDeatilsArrayList,this);
        upcomingadapter=new adapter(this,upcoming,this);
        moviesRecyclerView.setAdapter(adapter);
        seriesRecyclerView.setAdapter(seriesadapter);
        upcomingRecyclerView.setAdapter(upcomingadapter);

    }


    @Override
    public void onListItemClick(int clickInt) {

        Gson gson = new Gson();
        String selectedMovie = gson.toJson(detailsArrayList.get(clickInt));
        Intent detailsIntent = new Intent(this,viewDetailsActivity.class);
        detailsIntent.putExtra("movie",selectedMovie);
        detailsIntent.putExtra("type","movie");
        startActivity(detailsIntent);

    }

    @Override
    public void onTvClick(int clickInt) {
        Gson gson = new Gson();
        String selectedMovie = gson.toJson(seriesDeatilsArrayList.get(clickInt));
        Intent detailsIntent = new Intent(this,viewDetailsActivity.class);
        detailsIntent.putExtra("tv",selectedMovie);
        detailsIntent.putExtra("type","tv");
        startActivity(detailsIntent);

    }

    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = new NetworkInfo[0];
        if (cm != null) {
            netInfo = cm.getAllNetworkInfo();
        }
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }

}