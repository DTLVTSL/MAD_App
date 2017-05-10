package com.example.mad.lab2;

import android.content.ContentProvider;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class members_activity extends AppCompatActivity {

    android.widget.ListView ListView;

    final ArrayList member_list = new ArrayList();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.members);

        //The items_class are set in the list view progressively, not all of them at the same time


        Bundle bundle = getIntent().getExtras();
        String group_id=bundle.getString("group_id");
        String group_name=bundle.getString("GroupName");
        setTitle(getString(R.string.members_into)+" "+group_name);


        //Toast.makeText(getApplicationContext(),group_name, Toast.LENGTH_SHORT).show();


        //DANIEL
        Firebase firebase = new Firebase(Config.FIREBASE_URL).child("Groups").child(group_id).child("members");
        firebase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Log.d("MEMBERS snapchot: ", snapshot.toString());
                member_list.clear();
                String groupID;
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Log.d("MEMBERS postsnapchot: ", postSnapshot.getKey().toString());
                    //groupID=postSnapshot.child("groupID").getValue().toString();

                    member_list.add(postSnapshot.getKey().toString());
                    Log.d("MEMBERS MEMBER_LIST:  ", member_list.toString());


                }

                //Log.d("GROUP LIST: ", group_list.toString());



            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        //sacar members de la lista anterior
        final ArrayList<User> data_members= new ArrayList<>();

        final members_adapter adapter2 = new members_adapter(this, R.layout.listview_members_row, data_members);

        // MOSTRAR LOS GRUPOS Q SE SACARON DE LA LISTA EN EL LIST
        Firebase.setAndroidContext(this);
        firebase = new Firebase(Config.FIREBASE_URL).child("Users");

        //final members_adapter finalAdapter = adapter;
        firebase.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override

            public void onDataChange(DataSnapshot snapshot) {
                data_members.clear();

                for (DataSnapshot postSnapshot : snapshot.getChildren()) {


                    //Log.d("MEMBERS 2: ", member_list.toString());
                    //Log.d("MEMBERS 2: ", postSnapshot.getKey().toString());
                    //Log.d("MEMBERS 2: ", String.valueOf(member_list.contains(postSnapshot.getKey().toString())));


                    if (member_list.contains(postSnapshot.getKey().toString())) {
                        Log.d("MEMBERS 3: ", postSnapshot.getValue().toString());



                        HashMap<String, Object> Items_2 = (HashMap<String, Object>) postSnapshot.child("Items").getValue();
                        String id = postSnapshot.getKey().toString();
                        int photo = postSnapshot.child("photo").getValue(int.class);

                        String name = postSnapshot.child("name").getValue().toString();
                        Log.d("MEMBERS 3: ", name);
                        String number=postSnapshot.child("phoneNumber").getValue().toString();

                        User member = new User(id,name,number,photo);
                        data_members.add(member);

                        Log.d("MEMBERS 4: ", data_members.toString());

                        ListView = (ListView) findViewById(R.id.member_list);
                        ListView.setAdapter(adapter2);



                    }
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        //sacar member de la lista anterior end

            //DANIEL END




        //Creating the adapter
        ListView = (ListView) findViewById(R.id.member_list);


        View header = (View) getLayoutInflater().inflate(R.layout.list_header_row, null);
        ListView.addHeaderView(header);
        ListView.setAdapter(adapter2);

        ListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView v= (TextView)view.findViewById(R.id.member_number);
                String telephone = "0000";
                try{
                    telephone = (String) v.getText();
                } catch (Exception e){
                    telephone="no number registered";
                }

                final String finalTelephone = telephone;
                new AlertDialog.Builder(members_activity.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Calling")
                        //.setTitle(getString(R.string.leaving))
                        .setMessage("do you want to call?")
                        //.setMessage(getString(R.string.leave_sure))
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                        {

                            Intent call=new Intent(Intent.ACTION_DIAL,Uri.parse("tel:"+ finalTelephone));


                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    startActivity(call);
                                } catch (Exception e){
                                    Toast.makeText(getApplicationContext(),"we cant call now", Toast.LENGTH_SHORT).show();
                                }

                            }
                        })
                        .setNegativeButton("No", null)
                        .show();


                //Toast.makeText(getApplicationContext(),v.getText(), Toast.LENGTH_SHORT).show();
            }
        });

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