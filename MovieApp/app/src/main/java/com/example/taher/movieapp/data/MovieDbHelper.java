package com.example.taher.movieapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.taher.movieapp.data.MovieContract.*;

import java.sql.SQLException;

/**
 * Created by taher on 17/08/16.
 */
public class MovieDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 5;

    static final String DATABASE_NAME = "movie.db";

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String SQL_CREATE_FAVORITE_MOVIES_TABLE =  "CREATE TABLE " + FavoriteMoviesEntry.TABLE_NAME + " (" +
                FavoriteMoviesEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +

                FavoriteMoviesEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL, " +
                FavoriteMoviesEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                FavoriteMoviesEntry.COLUMN_DESCRIPTION + " TEXT NOT NULL, " +
                FavoriteMoviesEntry.COLUMN_DATE + " TEXT NOT NULL, " +
                FavoriteMoviesEntry.COLUMN_IMAGE_URL + " TEXT NOT NULL, " +
                FavoriteMoviesEntry.COLUMN_RATE + " TEXT NOT NULL" +
                " );";

        final String SQL_CREATE_TRAILERS_TABLE = "CREATE TABLE " + TrailersMovieEntry.TABLE_NAME + " (" +
                TrailersMovieEntry._ID + " INTEGER PRIMARY KEY, " +
                TrailersMovieEntry.COLUMN_KEY + " TEXT NOT NULL, " +
                TrailersMovieEntry.COLUMN_MOVIE_KEY + " INTEGER NOT NULL, " +

                " FOREIGN KEY (" + TrailersMovieEntry.COLUMN_MOVIE_KEY + ") REFERENCES " +
                FavoriteMoviesEntry.TABLE_NAME + " (" + FavoriteMoviesEntry._ID + "));";

        final String SQL_CREATE_REVIEWS_TABLE = "CREATE TABLE " + ReviewsMovieEntry.TABLE_NAME + " (" +
                ReviewsMovieEntry._ID + " INTEGER PRIMARY KEY," +
                ReviewsMovieEntry.COLUMN_Name + " TEXT NOT NULL, " +
                ReviewsMovieEntry.COLUMN_DESCRIPTION + " TEXT NOT NULL, " +
                ReviewsMovieEntry.COLUMN_MOVIE_KEY + " INTEGER NOT NULL, " +

                " FOREIGN KEY (" + ReviewsMovieEntry.COLUMN_MOVIE_KEY + ") REFERENCES " +
                FavoriteMoviesEntry.TABLE_NAME + " (" + FavoriteMoviesEntry._ID + "));";

        try{
            db.execSQL(SQL_CREATE_FAVORITE_MOVIES_TABLE);
            db.execSQL(SQL_CREATE_TRAILERS_TABLE);
            db.execSQL(SQL_CREATE_REVIEWS_TABLE);
        } catch (Exception e) {
            Log.v("MovieDbHelper OnCreate", e.toString());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TrailersMovieEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + ReviewsMovieEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + FavoriteMoviesEntry.TABLE_NAME);
        onCreate(db);

    }
}
