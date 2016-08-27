package com.example.taher.movieapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by taher on 13/08/16.
 */
public class ReviewsAdapter extends BaseAdapter {

    private ArrayList<ReviewItem> data;
    private Context context = null;
    private int layoutResourceId;

    public ReviewsAdapter(ArrayList<ReviewItem> data, Context context, int layoutResourceId) {
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
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ItemHolder holder;

        if(row == null) {
            row = LayoutInflater.from(context).inflate(layoutResourceId, parent, false);
            holder = new ItemHolder();
            holder.name = (TextView)row.findViewById(R.id.reviewer);
            holder.review = (TextView)row.findViewById(R.id.reviewTxt);
            row.setTag(holder);
        } else {
            holder = (ItemHolder)row.getTag();
        }

        holder.name.setText(data.get(position).getName());
        holder.review.setText(data.get(position).getReview());

        return row;
    }

    public static class ItemHolder
    {
        TextView name;
        TextView review;
    }
}
