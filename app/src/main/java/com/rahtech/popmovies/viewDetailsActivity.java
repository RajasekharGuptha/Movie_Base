package com.rahtech.popmovies;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import static com.rahtech.popmovies.R.color.colorPrimaryDark;

public class viewDetailsActivity extends AppCompatActivity implements adapter.listItemClickListener, trendtvadapter.onTvClickListener {

    ImageView backdropPath;
    TextView title;
    TextView description;
    TextView rating;
    TextView adult;
    TextView genre;
    TextView watchnow;

    String movilink = null;
    StringBuilder genres;
    String genresString;
    String type;

    Type typeToken;

    ArrayList<details> similarMovies;
    adapter similarMovAdapter;
    RecyclerView similarmoviesRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_details);

        // initialize
        similarMovies = new ArrayList<>();
        backdropPath = findViewById(R.id.backdrop_image);
        title = findViewById(R.id.movietitle);
        description = findViewById(R.id.moviedescription);
        rating = findViewById(R.id.rating);
        adult = findViewById(R.id.adult);
        genre = findViewById(R.id.genre);
        watchnow = findViewById(R.id.watchnow);
        similarmoviesRecyclerView = findViewById(R.id.similarmoviesList);
        similarmoviesRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));


        typeToken = new TypeToken<details>() {
        }.getType();
        genres = new StringBuilder();

        //data from intent
        type = getIntent().getStringExtra("type");
        final details movie = new Gson().fromJson(getIntent().getStringExtra(type), typeToken);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        if (movie != null) {
            if (movie.getBackdrop_path() == null) {

                Picasso.get().load("https://"+getString(R.string.image_base_string) + movie.getPoster_path())
                        .resize(displayMetrics.widthPixels, 300).into(backdropPath);
            } else {
                Picasso.get().load("https://"+getString(R.string.image_base_string) + movie.getBackdrop_path()).into(backdropPath);
            }
            try {
                String jsonFormatData;
                if (type.equals("movie"))
                    jsonFormatData = new httpRequest().execute(new URL(getString(R.string.base_url) + "/movie/" + movie.getId() + "?api_key=" + getString(R.string.api_key))).get();
                else
                    jsonFormatData = new httpRequest().execute(new URL(getString(R.string.base_url) + "/tv/" + movie.getId() + "?api_key=" + getString(R.string.api_key))).get();
                JSONObject jsonObject = new JSONObject(jsonFormatData);
                if (jsonObject.has("homepage")) {
                    movilink = jsonObject.getString("homepage");
                    JSONArray genreArray = jsonObject.getJSONArray("genres");
                    if (genreArray.length() != 0) {
                        for (int i = 0; i < genreArray.length(); i++) {
                            JSONObject genreObj = (JSONObject) genreArray.get(i);
                            genres.append(genreObj.getString("name"));
                            genres.append(",");
                        }
                    }
                    if (genres.length() != 0)
                        genresString = genres.substring(0, genres.length() - 1);
                }
            } catch (ExecutionException | InterruptedException | MalformedURLException | JSONException e) {
                e.printStackTrace();
            }
            title.setText(movie.getTitle());
            description.setText(movie.getOverview());
            rating.setText(String.format("rating:%s/10", movie.getVote_average()));
            adult.setText(String.format("18+:%s\t", movie.isAdult()));
            genre.setText(genresString);
            if (movilink != null || movilink.equals("")) {
                watchnow.setTextColor(getResources().getColor(colorPrimaryDark));
                watchnow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Uri webpage = Uri.parse(movilink);
                        Intent watchMovieIntent = new Intent(Intent.ACTION_VIEW, webpage);
                        if (watchMovieIntent.resolveActivity(getPackageManager()) != null) {
                            startActivity(watchMovieIntent);
                        }

                    }
                });
            } else {
                watchnow.setVisibility(View.INVISIBLE);
            }

        }
        String similarURL = null;
        String recommendationURL = null;
        if (movie != null) {

            if (type.equals("movie")) {
                similarURL = getString(R.string.base_url) + "/movie/" + movie.getId() + "/similar" + "?api_key=" + getString(R.string.api_key);
                recommendationURL = getString(R.string.base_url) + "/movie/" + movie.getId() + "/recommendations" + "?api_key=" + getString(R.string.api_key);
            }else {
                similarURL = getString(R.string.base_url) + "/tv/" + movie.getId() + "/similar" + "?api_key=" + getString(R.string.api_key);
                recommendationURL = getString(R.string.base_url) + "/tv/" + movie.getId() + "/recommendations" + "?api_key=" + getString(R.string.api_key);
            }
        }

        similarMovies=new getData().getDataFromUrl(similarURL,type);
        if (similarMovies.size()==0){
            similarMovies=new getData().getDataFromUrl(recommendationURL,type);
        }
        if(similarMovies.size()==0){
            similarMovies=new MainActivity().detailsArrayList;
        }
        if (type.equals("movie")) {
            adapter similarMovAdapter = new adapter(this, similarMovies, this);
            similarmoviesRecyclerView.setAdapter(similarMovAdapter);
        } else {
            trendtvadapter similaradapter = new trendtvadapter(this, similarMovies, this);
            similarmoviesRecyclerView.setAdapter(similaradapter);
        }

    }

    @Override
    public void onListItemClick(int clickInt) {
        Gson gson = new Gson();
        String selectedMovie = gson.toJson(similarMovies.get(clickInt));
        Intent detailsIntent = new Intent(this,viewDetailsActivity.class);
        detailsIntent.putExtra("movie",selectedMovie);
        detailsIntent.putExtra("type","movie");
        finish();
        startActivity(detailsIntent);

    }

    @Override
    public void onTvClick(int clickInt) {
        Gson gson = new Gson();
        String selectedMovie = gson.toJson(similarMovies.get(clickInt));
        Intent detailsIntent = new Intent(this,viewDetailsActivity.class);
        detailsIntent.putExtra("tv",selectedMovie);
        detailsIntent.putExtra("type","tv");
        finish();
        startActivity(detailsIntent);

    }
}