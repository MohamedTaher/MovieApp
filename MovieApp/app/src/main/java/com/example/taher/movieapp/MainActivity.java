package com.example.taher.movieapp;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static ArrayList<MovieItem> favoriteMovies;
    private static boolean twoFragment;
    private static FragmentManager fragmentManager;

    public static FragmentManager getMainFragmentManager() {
        return fragmentManager;
    }

    public static boolean isTwoFragment() {
        return twoFragment;
    }

    public static Intent getMainIntent(Context context){
        return new Intent(context, MainActivity.class);
    }

    public static void replaceMovieDetails(MovieItem item){
        MovieDetailFragment fragment = new MovieDetailFragment();
        fragment.setItem(item);

        FragmentManager fm = MainActivity.getMainFragmentManager();
        fm.beginTransaction()
                .replace(R.id.movie_detail_container,fragment)
                .commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        if (findViewById(R.id.movie_detail_container) != null) { // tablet view
            twoFragment = true;

            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.movie_detail_container, new MovieDetailFragment())
                        .commit();
            }

            fragmentManager = getSupportFragmentManager();
        } else {
            twoFragment = false;
        }

    }

}
