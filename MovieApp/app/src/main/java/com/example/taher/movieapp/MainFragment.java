package com.example.taher.movieapp;

import android.support.v4.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.taher.movieapp.Tasks.DBTasks.DBOnPostExecute;
import com.example.taher.movieapp.Tasks.DBTasks.DBTask;
import com.example.taher.movieapp.Tasks.HTTPTasks.OnPostExecute;
import com.example.taher.movieapp.Tasks.HTTPTasks.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by taher on 20/08/16.
 */
public class MainFragment extends Fragment {

    private GridView gridView;
    private ArrayList<MovieItem> data;
    private GridAdapter adapter;
    private ProgressBar progressBar;
    private String sortedType;

    private void updateData(){

        String url = getString(R.string.BASE_URL)+sortedType+"?api_key="+getString(R.string.API_KEY);

        Task fetchMoviesTask = new Task(getActivity(), new OnPostExecute() {
            // input parameter is a json string
            @Override
            public void onPostExecute(String input) {
                //Log.v("Data for API : ", input);

                setData(input);
                gridView.setAdapter(adapter);
                if (MainActivity.isTwoFragment())
                    MainActivity.replaceMovieDetails(data.get(0));
            }
        });

        fetchMoviesTask.setProgressBar(progressBar);
        fetchMoviesTask.execute(url);

        if (fetchMoviesTask.isCancelled()) {

            Toast.makeText(getActivity(), "Connection Error", Toast.LENGTH_LONG).show();
        }
    }

    private void throwMsg(){
        new AlertDialog.Builder(getActivity())
                .setTitle("Error")
                .setMessage("Error at internet connection !")
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void setData(String json) {

        try {

            data.clear();

            final String MOVIES_ID = "id";
            final String MOVIES_LIST = "results";
            final String MOVIE_POSTER = "poster_path";
            final String MOVIE_DESCRIPTION = "overview";
            final String MOVIE_DATE = "release_date";
            final String MOVIE_TITLE = "original_title";
            final String MOVIE_RATE = "vote_count";

            JSONObject mainJ = new JSONObject(json);
            JSONArray moviesArray = mainJ.getJSONArray(MOVIES_LIST);

            for (int i = 0; i < moviesArray.length(); i++) {
                JSONObject movie = moviesArray.getJSONObject(i);
                MovieItem item = null;

                //id
                if (movie.has(MOVIES_ID)) {
                    item = new MovieItem(movie.getInt(MOVIES_ID),getActivity());

                    //title
                    if (movie.has(MOVIE_TITLE))
                        item.setTitle(movie.getString(MOVIE_TITLE));

                    //description
                    if (movie.has(MOVIE_DESCRIPTION))
                        item.setDescription(movie.getString(MOVIE_DESCRIPTION));

                    //rate
                    if (movie.has(MOVIE_RATE))
                        item.setRate(movie.getInt(MOVIE_RATE)+"");

                    //image
                    if (movie.has(MOVIE_POSTER))
                        item.setImageUrl(getString(R.string.Img_PATH) + movie.getString(MOVIE_POSTER));

                    //date
                    if (movie.has(MOVIE_DATE)) {
                        String[] temp = movie.getString(MOVIE_DATE).split("-");
                        item.setDate(temp[0]);
                    }

                    //duration ????????????????????
                    //item.setDuration(120);
                    //Log.v("item " + i, item.getImageUrl());
                    data.add(item);
                }
            }
        } catch (JSONException e) {
            throwMsg();
            e.printStackTrace();

        } catch (Exception e) {
            throwMsg();

        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("itemPosition", gridView.getFirstVisiblePosition());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.main_fragment, container, false);

        sortedType = "popular";
        progressBar = (ProgressBar) rootView.findViewById(R.id.progressbar);
        gridView = (GridView) rootView.findViewById(R.id.mainList);
        data = new ArrayList<MovieItem>();
        MainActivity.favoriteMovies = new ArrayList<MovieItem>();

        updateData();

        adapter = new GridAdapter(data, getActivity(), R.layout.poster);

        if (savedInstanceState != null) {
            gridView.setSelection(savedInstanceState.getInt("itemPosition",0));

        }

        DBTask task = new DBTask(getContext(), new DBOnPostExecute() {
            @Override
            public void onPostExecute(ArrayList<MovieItem> input) {
                MainActivity.favoriteMovies.addAll(input);
            }
        });
        task.execute();

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.popular){
            sortedType = "popular";
            updateData();
        } else if (id == R.id.rate) {
            sortedType = "top_rated";
            updateData();
        } else if (id == R.id.favoriteMovies) {
            data.clear();
            data.addAll(MainActivity.favoriteMovies);
            gridView.setAdapter(adapter);
            if (MainActivity.isTwoFragment())
            MainActivity.replaceMovieDetails(MainActivity.favoriteMovies.get(0));
        }

        return super.onOptionsItemSelected(item);
    }
}
