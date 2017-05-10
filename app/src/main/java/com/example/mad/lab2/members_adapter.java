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
 * Created by daniel on 01/04/17.
 */
public class members_adapter extends ArrayAdapter<User> {
    Context context;
    int layoutResourceId;
    private ArrayList<User> data;

    //Constructor
    public members_adapter(Context context, int layoutResourceId, ArrayList<User> data){
        super(context,layoutResourceId,data);
//        super(context, layoutResourceId, data);

        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.data = data;
    }

    // Create the view
    public View getView(int position, View convertView, ViewGroup parent){
        View row = convertView;
        members_holder holder = null;

        if (row == null){
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new members_holder();
            holder.image = (ImageView) row.findViewById(R.id.member_image);
            holder.name = (TextView) row.findViewById(R.id.member_name);
            holder.number= (TextView) row.findViewById(R.id.member_number);


            row.setTag(holder);
        }else{
            holder = (members_holder)row.getTag();
        }

        User member = data.get(position);
        holder.name.setText(member.getName());
        holder.number.setText(member.getPhoneNumber().toString());
        holder.image.setImageResource(member.getPhoto());




        //In order to return the view
        return row;
    }

    //Keep the data ir order to be alble to work with them
    static class members_holder{
        ImageView image;
        TextView name;
        TextView number;


    }

}