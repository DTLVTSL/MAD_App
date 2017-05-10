package com.example.mad.lab2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by root on 10/05/17.
 */

public class ModifyItemActivity extends AppCompatActivity {

    private EditText Mod_item_name;
    private EditText Mod_item_price;
    private EditText Mod_item_currency;
    private EditText Mod_item_alert;
    private Button Mod_item_save;
    private Button but_cancel;
    private Button delete_item;

    private String GroupID;
    private String GroupName;
    private String ItemName;
    private String Item_price;
    private String Item_currency;
    private String Item_alert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mod_item);

        Mod_item_name = (EditText) findViewById(R.id.mod_item_name);
        Mod_item_price = (EditText) findViewById(R.id.mod_item_price);
        //new_item_currency = (EditText) findViewById(R.id.new_item_currency);
        Mod_item_alert = (EditText) findViewById(R.id.mod_item_alert);
        Mod_item_save = (Button) findViewById(R.id.mod_item_save);
        but_cancel = (Button) findViewById(R.id.mod_item_cancell);

        delete_item= (Button) findViewById(R.id.delete_item);

        //Spiner
        final Spinner spinner_cur = (Spinner) findViewById(R.id.mod_spin_item_currency);
        ArrayAdapter<CharSequence> adap = ArrayAdapter.createFromResource(this,
                R.array.currencies_array, android.R.layout.simple_spinner_item);
        adap.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_cur.setAdapter(adap);

        Firebase.setAndroidContext(this);

        //access to the group id
        Bundle bundle = getIntent().getExtras();
        GroupID =bundle.getString("GroupID");
        GroupName=bundle.getString("GroupName");
        ItemName =bundle.getString("ItemName");
        Item_price=bundle.getString("Item_price");
        Item_currency=bundle.getString("Item_currency");
        Item_alert=bundle.getString("Item_alert");


        Mod_item_name.setText(ItemName);
        Mod_item_price.setText(Item_price);
        //new_item_currency = (EditText) findViewById(R.id.new_item_currency);
        Mod_item_alert.setText(Item_alert);


        ////spinner try
        String compareValue = Item_currency;
        if (!compareValue.equals(null)) {
            int spinnerPosition = adap.getPosition(compareValue);
            spinner_cur.setSelection(spinnerPosition);
        }

        ////spinner try end

        setTitle(getString(R.string.modify)+" "+ItemName);


        final String grouptomod = bundle.getString("GroupID");

        but_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel_new_group();
            }
        });

        //Click Listener for button
        Mod_item_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //FirebaseAuth mAuth = FirebaseAuth.getInstance();

                Firebase ref = new Firebase(Config.FIREBASE_URL).child("Groups").child(GroupID).child("Items");

                final String name = Mod_item_name.getText().toString().trim();
                final String price = Mod_item_price.getText().toString().trim();
                final String alert = Mod_item_alert.getText().toString().trim();
                String currency = spinner_cur.getSelectedItem().toString().trim();

                Boolean val = validateForm(name, price); //Validate data

                if (val == true) {
                    //Creating Person object
                    items_class new_item = new items_class();

                    //Adding values
                    new_item.setName(name);
                    new_item.setPrice(price);
                    new_item.setCurrency(currency);
                    new_item.setAlert(alert);

                    //Storing values to firebase
                    ref.child(ItemName).setValue(new_item);

                   /* Intent i = new Intent(ModifyItemActivity.this, Items_activity.class);
                    i.putExtra("GroupID", GroupID);
                    i.putExtra("GroupName", GroupName);
                    startActivity(i);
                    finish();*/
                    finish();
                }


            }
        });

        delete_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Firebase ref = new Firebase(Config.FIREBASE_URL).child("Groups").child(GroupID).child("Items").child(ItemName);
                ref.removeValue();
                finish();
            }
        });

    }



    //Click Listener for button
    public void cancel_new_group() {
        //Intent i = new Intent(ModifyItemActivity.this, MainActivity.class);
        //i.putExtra("group_name", "Cancelled");
        //startActivity(i);
        finish();
    }


    private boolean validateForm(String name, String price) {
        boolean valid = true;

        if (TextUtils.isEmpty(name)) {
            Mod_item_name.setError(getString(R.string.error));
            valid = false;
        } else {
            Mod_item_name.setError(null);
        }

        if (TextUtils.isEmpty(price)) {
            Mod_item_price.setError(getString(R.string.error));
            valid = false;
        } else {
            Mod_item_price.setError(null);

        }

        return valid;
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK))
        {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}