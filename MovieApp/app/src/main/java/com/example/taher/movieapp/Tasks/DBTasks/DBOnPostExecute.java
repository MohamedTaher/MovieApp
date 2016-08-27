package com.example.taher.movieapp.Tasks.DBTasks;

import com.example.taher.movieapp.MovieItem;

import java.util.ArrayList;

/**
 * Created by taher on 25/08/16.
 */
public interface DBOnPostExecute {
    void onPostExecute(ArrayList<MovieItem> input);
}
