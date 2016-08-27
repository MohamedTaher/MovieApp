package com.example.taher.movieapp.Tasks.DBTasks;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import com.example.taher.movieapp.MovieItem;
import com.example.taher.movieapp.ReviewItem;
import com.example.taher.movieapp.data.MovieContract;

import java.util.ArrayList;

/**
 * Created by taher on 25/08/16.
 */
public class DBTask extends AsyncTask<MovieItem, Void, ArrayList<MovieItem>> {

    private Context context;

    private DBOnPostExecute dbOnPostExecute;

    private ArrayList<String> selectTrailers(long movieId) {
        ArrayList<String> trailers = new ArrayList<String>();

        Cursor trailerCursor = context.getContentResolver().query(
                MovieContract.TrailersMovieEntry.CONTENT_URI_SELECT,
                new String[]{MovieContract.TrailersMovieEntry._ID
                        , MovieContract.TrailersMovieEntry.COLUMN_MOVIE_KEY
                        , MovieContract.TrailersMovieEntry.COLUMN_KEY},
                MovieContract.TrailersMovieEntry.COLUMN_MOVIE_KEY + " = ?",
                new String[]{movieId+""},
                null
        );

        if (trailerCursor.moveToFirst()){
            do{
                //Log.v("trailer ====> ", trailerCursor.getString(trailerCursor.getColumnIndex(MovieContract.TrailersMovieEntry.COLUMN_MOVIE_KEY)));
                trailers.add(trailerCursor.getString(trailerCursor.getColumnIndex(MovieContract.TrailersMovieEntry.COLUMN_KEY)));

            }while(trailerCursor.moveToNext());
        }
        trailerCursor.close();
        return  trailers;
    }

    private ArrayList<ReviewItem> selectReviews(long movieId) {
        ArrayList<ReviewItem> reviews = new ArrayList<ReviewItem>();

        Cursor reviewCursor = context.getContentResolver().query(
                MovieContract.ReviewsMovieEntry.CONTENT_URI_SELECT,
                new String[]{MovieContract.ReviewsMovieEntry._ID
                        , MovieContract.ReviewsMovieEntry.COLUMN_MOVIE_KEY
                        , MovieContract.ReviewsMovieEntry.COLUMN_Name
                        , MovieContract.ReviewsMovieEntry.COLUMN_DESCRIPTION},
                MovieContract.ReviewsMovieEntry.COLUMN_MOVIE_KEY + " = ?",
                new String[]{movieId+""},
                null
        );

        if (reviewCursor.moveToFirst()){
            do{
                //Log.v("review ====> ", reviewCursor.getString(reviewCursor.getColumnIndex(MovieContract.ReviewsMovieEntry.COLUMN_MOVIE_KEY)));
                reviews.add(new ReviewItem(
                        reviewCursor.getString(reviewCursor.getColumnIndex(MovieContract.ReviewsMovieEntry.COLUMN_Name)),
                        reviewCursor.getString(reviewCursor.getColumnIndex(MovieContract.ReviewsMovieEntry.COLUMN_DESCRIPTION))));

            }while(reviewCursor.moveToNext());
        }

        reviewCursor.close();
        return  reviews;
    }

    private long addTrailer(String key, long movieID){

        long trailerId;

        Cursor trailerCursor = context.getContentResolver().query(
                MovieContract.TrailersMovieEntry.CONTENT_URI ,
                new String[] {MovieContract.TrailersMovieEntry._ID} ,
                MovieContract.TrailersMovieEntry.COLUMN_KEY + " = ?",
                new String []{key},
                null
        );

        if (trailerCursor.moveToFirst()) {
            int trailerIdIndex = trailerCursor.getColumnIndex(MovieContract.TrailersMovieEntry._ID);
            trailerId = trailerCursor.getLong(trailerIdIndex);
        } else {
            ContentValues trailerValues = new ContentValues();
            trailerValues.put(MovieContract.TrailersMovieEntry.COLUMN_KEY, key);
            trailerValues.put(MovieContract.TrailersMovieEntry.COLUMN_MOVIE_KEY, movieID);

            Uri insertedUri = context.getContentResolver().insert(
                    MovieContract.TrailersMovieEntry.CONTENT_URI,
                    trailerValues
            );

            trailerId = ContentUris.parseId(insertedUri);

        }

        //Log.v("TRAILER ID =====>", trailerId + " " + movieID);

        trailerCursor.close();

        return trailerId;
    }

    private long addReview(String name, String des, long movieID){

        long reviewId;

        Cursor reviewCursor = context.getContentResolver().query(
                MovieContract.ReviewsMovieEntry.CONTENT_URI,
                new String[]{MovieContract.ReviewsMovieEntry._ID},
                MovieContract.ReviewsMovieEntry.COLUMN_DESCRIPTION + " = ?",
                new String[]{des},
                null
        );

        if (reviewCursor.moveToFirst()) {
            int reviewIdIndex = reviewCursor.getColumnIndex(MovieContract.ReviewsMovieEntry._ID);
            reviewId = reviewCursor.getLong(reviewIdIndex);
        } else {
            ContentValues reviewValues = new ContentValues();
            reviewValues.put(MovieContract.ReviewsMovieEntry.COLUMN_Name, name);
            reviewValues.put(MovieContract.ReviewsMovieEntry.COLUMN_DESCRIPTION, des);
            reviewValues.put(MovieContract.TrailersMovieEntry.COLUMN_MOVIE_KEY, movieID);


            Uri insertedUri = context.getContentResolver().insert(
                    MovieContract.ReviewsMovieEntry.CONTENT_URI,
                    reviewValues
            );

            reviewId = ContentUris.parseId(insertedUri);
        }

        reviewCursor.close();


        //Log.v("REVIEW ID ======>", reviewId + " " + movieID);

        return reviewId;
    }

    private long addMovie (MovieItem item) {

        long movieId;

        Cursor movieCursor = context.getContentResolver().query(
                MovieContract.FavoriteMoviesEntry.CONTENT_URI,
                new String[]{MovieContract.FavoriteMoviesEntry._ID},
                MovieContract.FavoriteMoviesEntry.COLUMN_MOVIE_ID + " = ?",
                new String[]{item.getId() + ""},
                null
        );

        if (movieCursor.moveToFirst()) {
            int movieIdIndex = movieCursor.getColumnIndex(MovieContract.FavoriteMoviesEntry._ID);
            movieId = movieCursor.getLong(movieIdIndex);
        } else {
            ContentValues movieValues = new ContentValues();

            movieValues.put(MovieContract.FavoriteMoviesEntry.COLUMN_MOVIE_ID, item.getId());
            movieValues.put(MovieContract.FavoriteMoviesEntry.COLUMN_DATE, item.getDate());
            movieValues.put(MovieContract.FavoriteMoviesEntry.COLUMN_DESCRIPTION, item.getDescription());
            movieValues.put(MovieContract.FavoriteMoviesEntry.COLUMN_TITLE, item.getTitle());
            movieValues.put(MovieContract.FavoriteMoviesEntry.COLUMN_RATE, item.getRate() + "");
            movieValues.put(MovieContract.FavoriteMoviesEntry.COLUMN_IMAGE_URL, item.getImageUrl());

            Uri insertedUri = context.getContentResolver().insert(
                    MovieContract.FavoriteMoviesEntry.CONTENT_URI,
                    movieValues
            );

            movieId = ContentUris.parseId(insertedUri);
        }

        movieCursor.close();

        //Log.v("MOVIE ID ====>", movieId+"");

        return movieId;
    }

    private void addFavoriteMovie(MovieItem item){

        long movieId = addMovie(item);

        ArrayList<String> trailers = item.getTrailers();
        for (int i = 0; i < trailers.size(); i++)
            addTrailer(trailers.get(i), movieId);

        ArrayList<ReviewItem> reviews = item.getReviews();
        for (int i = 0; i < reviews.size(); i++)
            addReview(reviews.get(i).getName(), reviews.get(i).getReview(), movieId);
    }

    private ArrayList<MovieItem> selectFavoriteMovies(){

        ArrayList<MovieItem> favoriteMovies = new ArrayList<MovieItem>();

        Cursor moviesCursor = context.getContentResolver().query(
                MovieContract.FavoriteMoviesEntry.CONTENT_URI_SELECT,
                new String[]{MovieContract.FavoriteMoviesEntry._ID
                        , MovieContract.FavoriteMoviesEntry.COLUMN_MOVIE_ID
                        , MovieContract.FavoriteMoviesEntry.COLUMN_DATE
                        , MovieContract.FavoriteMoviesEntry.COLUMN_DESCRIPTION
                        , MovieContract.FavoriteMoviesEntry.COLUMN_IMAGE_URL
                        , MovieContract.FavoriteMoviesEntry.COLUMN_RATE
                        , MovieContract.FavoriteMoviesEntry.COLUMN_TITLE},
                null,
                null,
                null
        );

        if (moviesCursor.moveToFirst()){
            do{
                //Log.v("MOVIE_ID ====> ", moviesCursor.getString(moviesCursor.getColumnIndex(MovieContract.FavoriteMoviesEntry.COLUMN_MOVIE_ID)));

                MovieItem item = new MovieItem(
                        moviesCursor.getString(moviesCursor.getColumnIndex(MovieContract.FavoriteMoviesEntry.COLUMN_DATE)),
                        moviesCursor.getString(moviesCursor.getColumnIndex(MovieContract.FavoriteMoviesEntry.COLUMN_DESCRIPTION)),
                        Integer.parseInt(moviesCursor.getString(moviesCursor.getColumnIndex(MovieContract.FavoriteMoviesEntry.COLUMN_MOVIE_ID))),
                        moviesCursor.getString(moviesCursor.getColumnIndex(MovieContract.FavoriteMoviesEntry.COLUMN_IMAGE_URL)),
                        moviesCursor.getString(moviesCursor.getColumnIndex(MovieContract.FavoriteMoviesEntry.COLUMN_RATE)),
                        moviesCursor.getString(moviesCursor.getColumnIndex(MovieContract.FavoriteMoviesEntry.COLUMN_TITLE))
                );


                long movieId = Long.parseLong(moviesCursor.getString(moviesCursor.getColumnIndex(MovieContract.FavoriteMoviesEntry._ID)));

                item.setTrailers(selectTrailers(movieId));
                item.setReviews(selectReviews(movieId));

                favoriteMovies.add(item);

            }while(moviesCursor.moveToNext());
        }
        moviesCursor.close();


        return favoriteMovies;
    }

    @Override
    protected ArrayList<MovieItem> doInBackground(MovieItem... params) {

        if (params.length == 0)
            return selectFavoriteMovies();

        addFavoriteMovie(params[0]);
        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<MovieItem> movieItems) {
        super.onPostExecute(movieItems);

        dbOnPostExecute.onPostExecute(movieItems);
    }

    public DBTask(Context context, DBOnPostExecute dbOnPostExecute) {
        this.context = context;
        this.dbOnPostExecute = dbOnPostExecute;
    }
}
