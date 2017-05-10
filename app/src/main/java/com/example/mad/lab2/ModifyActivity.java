package com.example.mad.lab2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;

import java.util.HashMap;
import java.util.Map;

public class ModifyActivity extends AppCompatActivity {

    private EditText mod_group_name;
    private EditText mod_group_noti;
    private EditText mod_group_icon;

    private Button mod_group_save;
    private Button but_cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mod);

        mod_group_name = (EditText) findViewById(R.id.mod_group_name);
        mod_group_noti = (EditText) findViewById(R.id.mod_group_noti);
        mod_group_icon = (EditText) findViewById(R.id.mod_group_icon);
        mod_group_save = (Button) findViewById(R.id.mod_group_save);
        but_cancel = (Button) findViewById(R.id.mod_group_cancell);

        Firebase.setAndroidContext(this);

        //access to the group id
        Bundle bundle = getIntent().getExtras();

        String GroupName=bundle.getString("GroupName");

        setTitle(getString(R.string.modify)+" "+GroupName);


        final String grouptomod = bundle.getString("GroupID");

        but_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel_new_group();
            }
        });

        //Click Listener for button
        mod_group_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseAuth mAuth = FirebaseAuth.getInstance();

                final String name = mod_group_name.getText().toString().trim();
                final String icon = mod_group_icon.getText().toString().trim();
                final String noti = mod_group_noti.getText().toString().trim();

                final Firebase refmod = new Firebase(Config.FIREBASE_URL).child("Groups").child(grouptomod);
                Map<String, Object> taskMap = new HashMap<String, Object>();

                //Integer flag = 1;

                Boolean val = validateForm(name); //Validate data

                if (val) {

                    if (name.isEmpty()) {   //EditText is empty
                        //taskMap.put("title", "Vacio");
                        //flag = 0;
                    } else {   //EditText is not empty
                        taskMap.put("title", name);
                    }

                    if (icon.isEmpty()) {   //EditText is empty
                        //taskMap.put("title", "Vacio");
                    } else {   //EditText is not empty
                        taskMap.put("icon", icon);
                    }

                    if (noti.isEmpty()) {   //EditText is empty
                        //taskMap.put("title", "Vacio");
                    } else {   //EditText is not empty
                        taskMap.put("noti", noti);
                    }

                    //Update new data in the label "Groups"
                    refmod.updateChildren(taskMap);

                    //if (flag == 1) {
                    if (false) {
                        //Update the new name of the group in each user
                        final Firebase regseemem = refmod.child("members");
                        regseemem.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot snapshot) {

                                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                                    String groupID = postSnapshot.child("userID").getValue().toString();

                                    Firebase refgrouse = new Firebase(Config.FIREBASE_URL).child("Users").child(groupID);
                                    Firebase refgrouse2 = refgrouse.child("groups").child(grouptomod).child("group");
                                    refgrouse2.setValue(name);
                                }
                            }

                            @Override
                            public void onCancelled(FirebaseError error) {
                            }

                        });
                    }

                    Intent i = new Intent(ModifyActivity.this, MainActivity.class);
                    i.putExtra("group_name", name);
                    startActivity(i);
                    finish();
                }
            }
        });
        
    }

    //Click Listener for button
    public void cancel_new_group() {
        Intent i = new Intent(ModifyActivity.this, MainActivity.class);
        i.putExtra("group_name", "Cancelled");
        startActivity(i);
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
            mod_group_name.setError(getString(R.string.error));
            valid = false;
        } else {
            mod_group_name.setError(null);
        }


        return valid;
    }

}
