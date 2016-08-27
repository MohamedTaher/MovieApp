package com.example.taher.movieapp.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import com.example.taher.movieapp.data.MovieContract.*;
import android.support.annotation.Nullable;

/**
 * Created by taher on 17/08/16.
 */
public class MovieProvider extends ContentProvider {

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private MovieDbHelper mOpenHelper;

    static final int MOVIES = 100;
    static final int TRAILER_BY_MOVIE_ID = 101;
    static final int REVIEW_BY_MOVIE_ID = 102;
    static final int TRAILERS = 103;
    static final int REVIEWS = 104;
    static final int ALL_MOVIES = 105;

    private static final SQLiteQueryBuilder sTrailerByMovieQueryBuilder;
    static{
        sTrailerByMovieQueryBuilder = new SQLiteQueryBuilder();

        //This is an inner join which looks like
        //weather INNER JOIN location ON weather.location_id = location._id
        sTrailerByMovieQueryBuilder.setTables(
                TrailersMovieEntry.TABLE_NAME /*+ " INNER JOIN " +
                        FavoriteMoviesEntry.TABLE_NAME +
                        " ON " + TrailersMovieEntry.TABLE_NAME +
                        "." + TrailersMovieEntry.COLUMN_MOVIE_KEY +
                        " = " + FavoriteMoviesEntry.TABLE_NAME +
                        "." + FavoriteMoviesEntry._ID*/
        );

    }

    private static final SQLiteQueryBuilder sReviewByMovieQueryBuilder;
    static{
        sReviewByMovieQueryBuilder = new SQLiteQueryBuilder();

        //This is an inner join which looks like
        //weather INNER JOIN location ON weather.location_id = location._id
        sReviewByMovieQueryBuilder.setTables(
                ReviewsMovieEntry.TABLE_NAME /*+ " INNER JOIN " +
                        FavoriteMoviesEntry.TABLE_NAME +
                        " ON " + ReviewsMovieEntry.TABLE_NAME +
                        "." + ReviewsMovieEntry.COLUMN_MOVIE_KEY +
                        " = " + FavoriteMoviesEntry.TABLE_NAME +
                        "." + FavoriteMoviesEntry._ID*/
        );

    }
/////////////////////////////////////////////////////////////////////////////////////
    private static final SQLiteQueryBuilder sMoviesQueryBuilder;
    static{
        sMoviesQueryBuilder = new SQLiteQueryBuilder();

        sMoviesQueryBuilder.setTables(
                FavoriteMoviesEntry.TABLE_NAME
        );

    }
//////////////////////////////////////////////////////////////////////////////////////


    //favorite_movies.movie_id = ?
    private static final String sMovieID =
            FavoriteMoviesEntry.TABLE_NAME +
            "." + FavoriteMoviesEntry.COLUMN_MOVIE_ID + " = ? ";


    //trailers.movieId = ?
    private static final String sTrailerMovieId =
            TrailersMovieEntry.TABLE_NAME +
                    "." + TrailersMovieEntry.COLUMN_MOVIE_KEY + " = ? ";

    //trailers.movieId = ?
    private static final String sReviewMovieId =
            ReviewsMovieEntry.TABLE_NAME +
                    "." + ReviewsMovieEntry.COLUMN_MOVIE_KEY + " = ? ";


    private Cursor getTrailerByMovieId(Uri uri, String[] projection, String sortOrder, String movieId){
        //String movieId = TrailersMovieEntry.getMovieIDFromUri(uri);

        return sTrailerByMovieQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                sTrailerMovieId,
                new String[] {movieId},
                null,
                null,
                sortOrder
        );
    }

    private Cursor getReviewByMovieId(Uri uri, String[] projection, String sortOrder, String movieId){
        //String movieId = ReviewsMovieEntry.getMovieIDFromUri(uri);

        return sReviewByMovieQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                sReviewMovieId,
                new String[] {movieId},
                null,
                null,
                sortOrder
        );
    }
//////////////////////////////////////////////////////////////////////////////////////////////
    private Cursor getAllMovies (Uri uri, String[] projection, String sortOrder){

        return sMoviesQueryBuilder.query(
                mOpenHelper.getReadableDatabase(),
                projection,
                null,
                null,
                null,
                null,
                sortOrder
        );
    }
    ////////////////////////////////////////////////////////////////////////////////

    static UriMatcher buildUriMatcher() {

        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MovieContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, MovieContract.PATH_FAVORITES, MOVIES);
        matcher.addURI(authority, MovieContract.PATH_FAVORITES + "/*", ALL_MOVIES);

        matcher.addURI(authority, MovieContract.PATH_TRAILERS, TRAILERS);
        matcher.addURI(authority, MovieContract.PATH_TRAILERS + "/*", TRAILER_BY_MOVIE_ID);

        matcher.addURI(authority, MovieContract.PATH_REVIEWS, REVIEWS);
        matcher.addURI(authority, MovieContract.PATH_REVIEWS + "/*", REVIEW_BY_MOVIE_ID);



        return matcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new MovieDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        Cursor retCursor;
        switch (sUriMatcher.match(uri)) {

            case TRAILER_BY_MOVIE_ID:{
                String movieId = selectionArgs[0];
                retCursor = getTrailerByMovieId(uri, projection, sortOrder, movieId);
                break;
            }

            case REVIEW_BY_MOVIE_ID: {
                String movieId = selectionArgs[0];
                retCursor = getReviewByMovieId(uri, projection, sortOrder, movieId);
                break;
            }
            case ALL_MOVIES:
                retCursor = getAllMovies(uri, projection, sortOrder);
                break;

            case MOVIES:
                retCursor = mOpenHelper.getReadableDatabase().query(
                        FavoriteMoviesEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;

            case TRAILERS:
                retCursor = mOpenHelper.getReadableDatabase().query(
                        TrailersMovieEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;

            case REVIEWS:
                retCursor = mOpenHelper.getReadableDatabase().query(
                        ReviewsMovieEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {

        final int match = sUriMatcher.match(uri);

        switch (match) {
            case TRAILER_BY_MOVIE_ID:
                return TrailersMovieEntry.CONTENT_TYPE;

            case REVIEW_BY_MOVIE_ID:
                return ReviewsMovieEntry.CONTENT_TYPE;

            case MOVIES:
                return FavoriteMoviesEntry.CONTENT_TYPE;

            case TRAILERS:
                return TrailersMovieEntry.CONTENT_TYPE;

            case REVIEWS:
                return ReviewsMovieEntry.CONTENT_TYPE;

            case ALL_MOVIES:
                return FavoriteMoviesEntry.CONTENT_TYPE;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match){
            case MOVIES: {
                long _id = db.insert(FavoriteMoviesEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = FavoriteMoviesEntry.buildFavoriteMoviesUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }

            case TRAILERS: {
                long _id = db.insert(TrailersMovieEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = TrailersMovieEntry.buildTrailersUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }

            case REVIEWS: {
                long _id = db.insert(ReviewsMovieEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = ReviewsMovieEntry.buildReviewsUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
