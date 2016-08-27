package com.example.taher.movieapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MovieDetail extends AppCompatActivity {

    public static Intent getDetailMovieIntent(Context context, MovieItem item){
        return new Intent(context, MovieDetail.class)
                .putExtra("item", item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        Intent intent = getIntent();
        MovieDetailFragment fragment = new MovieDetailFragment();
        fragment.setItem((MovieItem) intent.getSerializableExtra("item"));

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.movie_detail_container, fragment)
                    .commit();
        }


    }
}
