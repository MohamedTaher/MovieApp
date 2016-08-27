package com.example.taher.movieapp.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by taher on 17/08/16.
 */
public class MovieContract {

    public static final String CONTENT_AUTHORITY = "com.example.taher.movieapp";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_TRAILERS = "trailers";
    public static final String PATH_REVIEWS = "reviews";
    public static final String PATH_FAVORITES = "favorite_movies";

    //Defines the content of the Trailers Table
    public static final class TrailersMovieEntry implements BaseColumns{

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_TRAILERS).build();

        public static final Uri CONTENT_URI_SELECT =
                BASE_CONTENT_URI.buildUpon()
                        .appendPath(PATH_TRAILERS)
                        .appendPath("*")
                        .build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TRAILERS;



        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TRAILERS;

        public static final String TABLE_NAME = "trailers";
        public static final String COLUMN_MOVIE_KEY = "movieId";
        public static final String COLUMN_KEY = "key";

        public static Uri buildTrailersUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static String getMovieIDFromUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }
    }

    //Defines the content of the Reviews Table
    public static final class ReviewsMovieEntry implements BaseColumns{

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_REVIEWS).build();

        public static final Uri CONTENT_URI_SELECT =
                BASE_CONTENT_URI.buildUpon()
                        .appendPath(PATH_REVIEWS)
                        .appendPath("*")
                        .build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_REVIEWS;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_REVIEWS;

        public static final String TABLE_NAME = "reviews";
        public static final String COLUMN_MOVIE_KEY = "movieId";

        public static final String COLUMN_Name = "name";
        public static final String COLUMN_DESCRIPTION = "description";

        public static Uri buildReviewsUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static String getMovieIDFromUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }
    }

    //Defines the content of the Favorite Movies Table
    public static final class FavoriteMoviesEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAVORITES).build();

        public static final Uri CONTENT_URI_SELECT =
                BASE_CONTENT_URI.buildUpon()
                        .appendPath(PATH_FAVORITES)
                        .appendPath("*")
                        .build();


        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FAVORITES;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FAVORITES;


        public static final String TABLE_NAME = "favorite_movies";

        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_IMAGE_URL = "image_url";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_RATE = "rate";
        //public static final String COLUMN_TRAILERS_ID = "trailers_id";
        //public static final String COLUMN_REVIEWS_ID = "reviews_id";

        public static Uri buildFavoriteMoviesUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        /*public static String getMovieIDFromUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }*/
    }


}
