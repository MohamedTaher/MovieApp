package com.example.taher.movieapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.taher.movieapp.Tasks.DBTasks.DBOnPostExecute;
import com.example.taher.movieapp.Tasks.DBTasks.DBTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by taher on 20/08/16.
 */
public class MovieDetailFragment extends Fragment {

    private MovieItem item;

    private TextView title;
    private TextView description;
    private TextView rate;
    //private TextView duration;
    private TextView year;
    private ImageView poster;
    private Button favorite;
    private ListView trailers;
    private ListView reviews;

    public MovieItem getItem() {
        return item;
    }

    public void setItem(MovieItem item) {
        this.item = item;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.movie_detail_fragment, container, false);


        if (item != null) {
            title = (TextView) rootView.findViewById(R.id.movieTitle);
            description = (TextView) rootView.findViewById(R.id.description);
            rate = (TextView) rootView.findViewById(R.id.rate);
            //duration = (TextView) findViewById(R.id.duration);
            year = (TextView) rootView.findViewById(R.id.year);
            poster = (ImageView) rootView.findViewById(R.id.movieImage);
            favorite = (Button) rootView.findViewById(R.id.favorite);
            trailers = (ListView) rootView.findViewById(R.id.trailersListView);
            reviews = (ListView) rootView.findViewById(R.id.reviewersListView);

            // ( +"" ) for if this field of data not in json object
            title.setText(item.getTitle() + "");
            description.setText(item.getDescription() + "");
            rate.setText(item.getRate() + "");
            //duration.setText(item.getDuration()+"");
            year.setText(item.getDate() + "");

            Picasso.with(rootView.getContext())
                    .load(item.getImageUrl())
                    .into(poster);

            favorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (!favorite.getText().equals("Favorite")) {
                        DBTask task = new DBTask(getContext(), new DBOnPostExecute() {
                            @Override
                            public void onPostExecute(ArrayList<MovieItem> input) {

                            }
                        });
                        task.execute(item);
                        MainActivity.favoriteMovies.add(item);
                        favorite.setText("Favorite");
                    }

                }
            });

            TrailerAdapter adapter = new TrailerAdapter(item.getTrailers(), getActivity(), R.layout.trailer);
            trailers.setAdapter(adapter);

            ReviewsAdapter reviewsAdapter = new ReviewsAdapter(item.getReviews(), getActivity(), R.layout.review);
            reviews.setAdapter(reviewsAdapter);

            for (int i = 0; i < MainActivity.favoriteMovies.size(); i++) {
                if (item.getId() == MainActivity.favoriteMovies.get(i).getId()) {
                    favorite.setText("Favorite");
                    break;
                }
            }
        }

        return rootView;
    }
}
