package com.example.mad.lab2;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Log;
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
public class groups_adapter extends ArrayAdapter<groups_class> {
    Context context;
    private ArrayList<groups_class> data;
    int layoutResourceId;

    private ArrayList<doubts_class> data_doubts;



    //Constructor
    public groups_adapter(Context context, int layoutResourceId, ArrayList<groups_class> data, ArrayList<doubts_class> doubts){
        super(context, layoutResourceId, data);

        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.data = data;
        this.data_doubts=doubts;

        //this.currency=currency;

    }

    // Create the view
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View row = convertView;
        groups_holder holder = null;

        if (row == null){
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new groups_holder();
            holder.image = (ImageView) row.findViewById(R.id.ima1);
            holder.text = (TextView) row.findViewById(R.id.text1);
            holder.textnot = (TextView) row.findViewById(R.id.text2);

            /////velez april 30
            holder.groupID=(TextView) row.findViewById(R.id.groupID);
            holder.divided_doubt = (TextView) row.findViewById(R.id.divided_doubt);
            holder.total_doubt= (TextView) row.findViewById(R.id.total_doubt);
            holder.currency=(TextView) row.findViewById(R.id.debit_currency);
            /////velez april 30 end

            holder.paid_boolean= (TextView) row.findViewById(R.id.group_paid);

            row.setTag(holder);
        }else{
            holder = (groups_holder)row.getTag();
        }

        //groups_class pos_gro = data[position];
        groups_class group = data.get(position);
        holder.text.setText(group.getTitle());
        holder.textnot.setText(group.getNoti());

        /////velez april 30
        holder.groupID.setText(group.getGroupID());
        doubts_class doubt =data_doubts.get(position);
        holder.divided_doubt.setText(doubt.getDivided());
        holder.total_doubt.setText(doubt.getTotal());
        holder.currency.setText(doubt.getCurrency());
        /////velez april 30 end


        if (doubt.getPaid()==true) {
            holder.paid_boolean.setText("PAID");
            holder.paid_boolean.setTextColor(Color.parseColor("#009900"));
            holder.paid_boolean.setBackgroundColor(Color.parseColor("#ffffff"));
        }
        else {
            holder.paid_boolean.setText("");
            //holder.paid_boolean.setTextColor(Color.parseColor("#009900"));
            //holder.paid_boolean.setBackgroundColor(Color.parseColor("#ffffff"));
        }

       /* holder.image.setImageResource(group.icon);*/
        Bitmap bitmap = BitmapFactory.decodeFile(String.valueOf(group.getIcon()));
        if(bitmap != null)
            holder.image.setImageResource(group.icon);
        else{
            holder.image.setImageResource(R.drawable.bills);
        }


        //In order to return the view
        return row;
    }

    //Keep the data ir order to be alble to work with them
    static class groups_holder{
        ImageView image;
        TextView text;
        TextView textnot;

        TextView groupID;
        TextView total_doubt;
        TextView divided_doubt;
        TextView currency;

        TextView paid_boolean;

    }

    @Override
    public boolean areAllItemsEnabled()
    {
        return true;
    }

    @Override
    public boolean isEnabled(int arg0)
    {
        return true;
    }

}
