package com.example.taher.movieapp.Tasks.HTTPTasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by taher on 13/08/16.
 */
public class Task extends AsyncTask<String, Integer, String> {

    private ProgressBar bar;
    private OnPostExecute onPostExecute;
    private final Context context;

    public Task(Context context, OnPostExecute onPostExecute){
        this.context = context;
        this.onPostExecute = onPostExecute;
    }

    public void setProgressBar(ProgressBar bar) {
        this.bar = bar;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        if (bar != null)
            bar.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);

        if (this.bar != null)
            bar.setProgress(values[0]);

    }

    // params[0] is url
    @Override
    protected String doInBackground(String... params) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        String jsonStr = null;

        try {
            String baseUrl = params[0];

            URL url = new URL(baseUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            if (inputStream == null) return null;

            reader = new BufferedReader(new InputStreamReader(inputStream));

            StringBuffer buffer = new StringBuffer();
            String line;

            while ((line = reader.readLine()) != null)
                buffer.append(line + "\n");


            if (buffer.length() == 0) return null;

            jsonStr = buffer.toString();

        }
        catch (IOException e) {

            jsonStr = null;
            Log.e("TASK EXCEPTION",e.toString());

        } finally {

            if (urlConnection != null)
                urlConnection.disconnect();

            if (reader != null)
                try {
                    reader.close();
                } catch (final IOException e) {

                }

        }
        return jsonStr;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        onPostExecute.onPostExecute(s);
        if (bar != null)
            bar.setVisibility(View.INVISIBLE);
    }
}
