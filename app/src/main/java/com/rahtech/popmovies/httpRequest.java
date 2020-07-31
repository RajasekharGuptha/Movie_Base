package com.rahtech.popmovies;

import android.os.AsyncTask;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class httpRequest extends AsyncTask<URL,String,String> {

    @Override
    protected String doInBackground(URL[] url) {

        HttpURLConnection httpURLConnection=null;
        InputStream inputStream = null;
        try {
            httpURLConnection= (HttpURLConnection) url[0].openConnection();
            inputStream = httpURLConnection.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String output=null;
        Scanner scanner= null;
        if (inputStream != null) {
            scanner = new Scanner(inputStream);
            scanner.useDelimiter("\\A");
            if(scanner.hasNext()){
                output = scanner.next();
            }
        }
        if (httpURLConnection != null) {
            httpURLConnection.disconnect();
        }

        return output;
    }

}
