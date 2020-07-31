package com.rahtech.popmovies;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class getData {

    public ArrayList<details> getDataFromUrl(String url,String type){
        String output=null;
        ArrayList<details>  detailsArrayList=new ArrayList<>();
        try {
            output = new httpRequest().execute(new URL(url)).get();
        } catch (ExecutionException | InterruptedException | MalformedURLException e) {
            e.printStackTrace();
        }

        JSONArray results = null;
        try {
            JSONObject fulloutput = null;
            if (output != null) {
                fulloutput = new JSONObject(output);
                results = fulloutput.getJSONArray("results");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < 20; i++) {
            JSONObject temp = null;
            try {
                if (results != null && results.length()>0) {
                    if (type.equals("movie")) {
                        temp = (JSONObject) results.get(i);
                        details details_temp = new details(
                                temp.getString("title"), temp.getInt("id"), temp.getInt("vote_average")
                                , temp.getString("poster_path"), temp.getString("backdrop_path")
                                , null, temp.getString("overview")
                                , temp.getString("release_date"), temp.getBoolean("adult"));

                        detailsArrayList.add(details_temp);
                    }
                    else{
                        temp = (JSONObject) results.get(i);
                        details details_temp = new details(
                                temp.getString("name"), temp.getInt("id"), temp.getInt("vote_average")
                                , temp.getString("poster_path"), temp.getString("backdrop_path")
                                , null, temp.getString("overview")
                                , temp.getString("first_air_date"), false);

                        detailsArrayList.add(details_temp);

                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return detailsArrayList;

    }

}
