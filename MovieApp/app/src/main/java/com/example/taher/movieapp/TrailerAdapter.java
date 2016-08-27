package com.example.taher.movieapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by taher on 12/08/16.
 */
public class TrailerAdapter  extends BaseAdapter {

    private ArrayList<String> data = null;
    private Context context = null;
    private int layoutResourceId;

    public TrailerAdapter(ArrayList<String> data, Context context, int layoutResourceId) {
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
            holder.trailerNumber = (TextView)row.findViewById(R.id.trailerNumber);
            row.setTag(holder);
        }
        else
        {
            holder = (ItemHolder)row.getTag();
        }

        String txt = "Trailer " + (position + 1);

        holder.trailerNumber.setText(txt);

        row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            //open video using youtupe app by key at data array list
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + data.get(position))));

            }
        });

        return row;
    }

    public static class ItemHolder
    {
        TextView trailerNumber;
    }
}
