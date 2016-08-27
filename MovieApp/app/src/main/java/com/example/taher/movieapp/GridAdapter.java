package com.example.taher.movieapp;

import android.app.Activity;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by taher on 30/07/16.
 */
public class GridAdapter extends BaseAdapter {

    private ArrayList<MovieItem> data = null;
    private Context context = null;
    private int layoutResourceId;

    public GridAdapter(ArrayList<MovieItem> data, Context context, int layoutResourceId) {
        this.data = data;
        this.context = context;
        this.layoutResourceId = layoutResourceId;
    }


    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ItemHolder holder = null;

        if(row == null)
        {
            row = LayoutInflater.from(context).inflate(layoutResourceId, parent, false);
            holder = new ItemHolder();
            holder.img = (ImageView)row.findViewById(R.id.posterImg);
            row.setTag(holder);
        }
        else
        {
            holder = (ItemHolder)row.getTag();
        }

        MovieItem item = data.get(position);

        //set image use Picasso
        Picasso.with(context)
                .load(item.getImageUrl())
                .placeholder(R.drawable.loading)
                .into(holder.img);

        row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log.v("this item", data.get(position).getTitle());
                if (MainActivity.isTwoFragment()) {
                    MainActivity.replaceMovieDetails(data.get(position));
                } else {
                    context.startActivity(MovieDetail.getDetailMovieIntent(context, data.get(position)));
                }
            }
        });

        return row;
    }

    public static class ItemHolder
    {
        ImageView img;
    }

}
