package com.example.mad.lab2;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by daniel on 04/04/17.
 */
/*
public class items_adapter extends ArrayAdapter<items_class> {
    Context context;
    items_class data[] = null;
    int layoutResourceId;

    //Constructor final
    public items_adapter(Context context, int layoutResourceId, items_class[] data){
        super(context, layoutResourceId, data);

        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.data = data;
    }

    // Create the view
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View row = convertView;
        items_adapter.items_holder holder = null;

        if (row == null){
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new items_adapter.items_holder();
            holder.image = (ImageView) row.findViewById(R.id.items_ima1);
            holder.name = (TextView) row.findViewById(R.id.items_name);
            holder.alert = (TextView) row.findViewById(R.id.items_alert);
            holder.price = (TextView) row.findViewById(R.id.items_price);
            holder.currency = (TextView) row.findViewById(R.id.items_currency);


            //holder.textnot = (TextView) row.findViewById(R.id.text2);
            row.setTag(holder);
        }else{
            holder = (items_adapter.items_holder)row.getTag();
        }

        items_class pos_gro = data[position];
        holder.name.setText(pos_gro.name);
        holder.alert.setText(pos_gro.alert);
        holder.price.setText(pos_gro.price);
        holder.image.setImageResource(pos_gro.icon);
        holder.currency.setText((pos_gro.currency));



        //In order to return the view
        return row;
    }

    //Keep the data ir order to be alble to work with them
    static class items_holder{
        ImageView image;
        TextView name;
        TextView alert;
        TextView price;
        TextView currency;

    }

}


*/



public class items_adapter extends ArrayAdapter<items_class> {
    Context context;
    private ArrayList<items_class> data;
    int layoutResourceId;

    //Constructor final ArrayList<items_class> data_items2= new ArrayList<items_class>();
    public items_adapter(Context context, int layoutResourceId, ArrayList<items_class> data){
        super(context, layoutResourceId, data);

        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.data=data;
    }

    // Create the view
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View row = convertView;
        items_holder holder = null;

        if (row == null){
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new items_holder();
            holder.image = (ImageView) row.findViewById(R.id.items_ima1);
            holder.name = (TextView) row.findViewById(R.id.items_name);
            holder.alert = (TextView) row.findViewById(R.id.items_alert);
            holder.price = (TextView) row.findViewById(R.id.items_price);
            holder.currency = (TextView) row.findViewById(R.id.items_currency);


            //holder.textnot = (TextView) row.findViewById(R.id.text2);
            row.setTag(holder);
        }else{
            holder = (items_holder)row.getTag();
        }

        items_class pos_gro = data.get(position);
        holder.name.setText(pos_gro.name);
        holder.alert.setText(pos_gro.alert);
        holder.price.setText(pos_gro.price);
        holder.image.setImageResource(pos_gro.icon);
        holder.currency.setText((pos_gro.currency));



        //In order to return the view
        return row;
    }

    //Keep the data ir order to be alble to work with them
    static class items_holder{
        ImageView image;
        TextView name;
        TextView alert;
        TextView price;
        TextView currency;

    }

}
