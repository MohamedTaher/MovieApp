package com.example.taher.movieapp;

import android.content.Context;

import com.example.taher.movieapp.Tasks.HTTPTasks.OnPostExecute;
import com.example.taher.movieapp.Tasks.HTTPTasks.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by taher on 30/07/16.
 */
public class MovieItem implements Serializable {
    private int id;
    private String imageUrl;
    private String title;
    private String description;
    private String date;
    //private int duration;
    private String rate;
    private ArrayList<String> trailers;
    private ArrayList<ReviewItem> reviews;



    public MovieItem(int id, Context context){

        this.id = id;

        trailers = new ArrayList<String>();
        reviews = new ArrayList<ReviewItem>();

        String url = context.getString(R.string.BASE_URL) + id + "/videos" + "?api_key=" + context.getString(R.string.API_KEY);
        Task fetchMovieTrailers = new Task(context, new OnPostExecute() {
            @Override
            public void onPostExecute(String input) {
                // must remove
                if (input == null)
                    return;

                try {
                    final String TRAILERS_LIST = "results";
                    final String TRAILER_KEY = "key";

                    JSONObject result = new JSONObject(input);
                    JSONArray trailersArr = result.getJSONArray(TRAILERS_LIST);

                    for (int i = 0; i < trailersArr.length(); i++){

                        JSONObject trailer = trailersArr.getJSONObject(i);

                        if(trailer.has(TRAILER_KEY))
                            trailers.add(trailer.getString(TRAILER_KEY));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        fetchMovieTrailers.execute(url);

        url = context.getString(R.string.BASE_URL) + id + "/reviews" + "?api_key=" + context.getString(R.string.API_KEY);
        final Task fetchMovieReviews = new Task(context, new OnPostExecute() {
            @Override
            public void onPostExecute(String input) {

                // must remove
                if (input == null)
                    return;

                try {
                    final String REVIEWS_LIST = "results";
                    final String REVIEW_AUTHOR = "author";
                    final String REVIEW_CONTENT = "content";

                    JSONObject result = new JSONObject(input);
                    JSONArray reviewsArr = result.getJSONArray(REVIEWS_LIST);

                    for (int i = 0; i < reviewsArr.length(); i++){

                        JSONObject review = reviewsArr.getJSONObject(i);

                        if(review.has(REVIEW_AUTHOR) && review.has(REVIEW_CONTENT))
                            reviews.add(new ReviewItem(review.getString(REVIEW_AUTHOR)
                                    , review.getString(REVIEW_CONTENT)));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
        fetchMovieReviews.execute(url);

    }

    public MovieItem(String date, String description, int id, String imageUrl, String rate, String title) {
        this.date = date;
        this.description = description;
        this.id = id;
        this.imageUrl = imageUrl;
        this.rate = rate;
        this.title = title;
    }

    public MovieItem(String imageUrl, String title, String description, String data, String rate) {
        this.imageUrl = imageUrl;
        this.title = title;
        this.description = description;
        this.date = data;
        //this.duration = duration;
        this.rate = rate;
    }

    //for testing
    public MovieItem(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    /*public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
    */

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public ArrayList<ReviewItem> getReviews() {
        return reviews;
    }

    public void setReviews(ArrayList<ReviewItem> reviews) {
        this.reviews = reviews;
    }

    public ArrayList<String> getTrailers() {
        return trailers;
    }

    public void setTrailers(ArrayList<String> trailers) {
        this.trailers = trailers;
    }
}
