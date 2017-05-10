package com.example.mad.lab2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.firebase.auth.FirebaseAuth;

public class new_group_activity extends AppCompatActivity {

    private EditText new_group_name;
    private EditText new_group_noti;
    private Button new_group_save;
    private Button but_cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_group);
        setTitle(getString(R.string.new_group));

        new_group_name = (EditText) findViewById(R.id.new_group);
        new_group_noti = (EditText) findViewById(R.id.new_group_noti);
        new_group_save = (Button) findViewById(R.id.new_group_save);
        but_cancel = (Button) findViewById(R.id.new_group_cancell);

        Firebase.setAndroidContext(this);

        but_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel_new_group();
            }
        });

        //Click Listener for button
        new_group_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseAuth mAuth = FirebaseAuth.getInstance();

                final String UserID = mAuth.getCurrentUser().getUid();

                //Getting values to store
                final String group_name = new_group_name.getText().toString().trim();
                final String group_noti = new_group_noti.getText().toString().trim();

                Firebase ref = new Firebase(Config.FIREBASE_URL).child("Groups");

                Boolean val = validateForm(group_name); //Validate data

                if (val){

                groups_class new_group = new groups_class();
                //Adding values
                new_group.setTitle(group_name);
                new_group.setNoti(group_noti);
                new_group.setIcon(0);

                new_group.setGroupID(ref.push().getKey());

                ref.child(new_group.getGroupID()).setValue(new_group);

                //Add memeber
                member_class member_group = new member_class(UserID);

                Firebase refmem = new Firebase(Config.FIREBASE_URL).child("Groups").child(new_group.getGroupID()).child("members").child(UserID);
                refmem.setValue(member_group);

                //New group in the label of each user
                Firebase reff = new Firebase(Config.FIREBASE_URL).child("Users").child(UserID).child("groups");
                String key = reff.push().getKey().toString();
                reff.child(key).setValue(new group_name_class(group_name, new_group.getGroupID()));

                Intent i = new Intent(new_group_activity.this, MainActivity.class);
                i.putExtra("group_name", group_name);
                startActivity(i);
                finish();
            }
            }
        });
    }

    //Click Listener for button
    public void cancel_new_group () {
        Intent i = new Intent(new_group_activity.this, MainActivity.class);
        i.putExtra("group_name", "Cancelled");
        startActivity(i);
        finish();
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

    private boolean validateForm(String g_name) {
        boolean valid = true;

        if (TextUtils.isEmpty(g_name)) {
            new_group_name.setError(getString(R.string.error));
            valid = false;
        } else {
            new_group_name.setError(null);
        }


        return valid;
    }

}
