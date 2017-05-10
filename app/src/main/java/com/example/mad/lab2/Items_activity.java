package com.example.mad.lab2;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.auth.api.Auth;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;

import static com.example.mad.lab2.R.id.total_debit;

public class Items_activity extends AppCompatActivity {

    android.widget.ListView ListView;
    private RecyclerView mRecyclerView;
    private StaggeredGridLayoutManager mLayoutManager;
    private String GroupID;
    private String GroupName;
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference mDatabaseReference = database.getReference();

    TextView total_debit;
    TextView divided_debit;
    TextView paid_text;

    Button paid_button;
    boolean paid=false;

    float total_price=0;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.items_activity);



        Bundle bundle = getIntent().getExtras();
        GroupID =bundle.getString("GroupID");
        GroupName=bundle.getString("GroupName");

        setTitle(getString(R.string.items_into)+" "+GroupName);

        final ArrayList<items_class> data_items = new ArrayList<items_class>();
        final items_adapter adapter = new items_adapter(this, R.layout.listview_items_row, data_items);
        //The items_class are set in the list view progressively, not all of them at the same time




        ////////////////////////////////FIREBASE ATTEMPT///////////
        Firebase.setAndroidContext(this);
        Firebase firebase = new Firebase(Config.FIREBASE_URL).child("Groups").child(GroupID).child("Items");
        //firebase = new Firebase(FIREBASE_URL).child(FIREBASE_CHILD);

        firebase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                data_items.clear();

                for (DataSnapshot postSnapshot : snapshot.getChildren()) {


                    //Log.d("AndroidBash", "item_activity: " + postSnapshot.toString());
                    Log.d("AndroidBash", "item_activity: " + postSnapshot.child("price").getValue().toString());

                    items_class item= postSnapshot.getValue(items_class.class);
                    item.setIcon(2130837589);   //HABIA ERROR POR EL NUMERO ENVIADO EN LA IMAGEN
                    data_items.add(item);
                    //data_items2.add(new items_class("zzzzz", "5", "$",R.drawable.bills));

                    ListView = (ListView) findViewById(R.id.lista2);
                    ListView.setAdapter(adapter);

                    total_price=total_price+Float.parseFloat((String) postSnapshot.child("price").getValue());


                }

                total_debit= (TextView) findViewById(R.id.total_debit_items);
                total_debit.setText(String.valueOf(total_price));

            }

            @Override
            public void onCancelled(FirebaseError error) {
            }
        });


        //////// get members
        Firebase firebase2 = new Firebase(Config.FIREBASE_URL).child("Groups").child(GroupID);
        //firebase = new Firebase(FIREBASE_URL).child(FIREBASE_CHILD);

        firebase2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                float total_members=snapshot.child("members").getChildrenCount();

                divided_debit= (TextView) findViewById(R.id.divided_debit_items);
                divided_debit.setText(String.valueOf(total_price/total_members));
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                float total_members=1;
            }
        });

        Firebase.setAndroidContext(this);
        FirebaseAuth mAuth;
        mAuth = FirebaseAuth.getInstance();
        final String userID=mAuth.getCurrentUser().getUid();

        Firebase firebase3 = new Firebase(Config.FIREBASE_URL).child("Groups").child(GroupID).child("members").child(userID);
        //firebase = new Firebase(FIREBASE_URL).child(FIREBASE_CHILD);

        firebase3.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                paid= (boolean) snapshot.child("paid").getValue();

                paid_text= (TextView) findViewById(R.id.Paid_items);
                if (total_price>0) {
                    if (paid == true) {
                        paid_text.setText(getString(R.string.You_have_already_paid));
                        paid_text.setTextColor(Color.parseColor("#009900"));
                    } else {

                        paid_text.setText(getString(R.string.you_have_not_paid_this_debit));
                        paid_text.setTextColor(Color.parseColor("#ff0000"));
                    }
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                float total_members=1;
            }


        });

        paid_button= (Button) findViewById(R.id.paid_button);

        paid_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Firebase firebase4 = new Firebase(Config.FIREBASE_URL).child("Groups").child(GroupID).child("members").child(userID);
                //firebase = new Firebase(FIREBASE_URL).child(FIREBASE_CHILD);

                firebase4.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        //boolean paid= (boolean) snapshot.child("paid").getValue();

                        if (paid == true) {
                            firebase4.child("paid").setValue(false);
                        } else {
                            firebase4.child("paid").setValue(true);
                            //Toast.makeText(Items_activity.this, "You have paid",Toast.LENGTH_LONG);
                        }

                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {
                        float total_members=1;
                    }


                });




            }
        });


        ///////////////////////////////////////
        //Creating the adapter
        //items_adapter adapter = new items_adapter(this, R.layout.listview_items_row, data_items2);
        ListView = (ListView) findViewById(R.id.lista2);


        View header = (View) getLayoutInflater().inflate(R.layout.list_header_row,null);

        //Add the header to the list
        ListView.addHeaderView(header);
        ListView.setAdapter(adapter);
// si se toca un item
        ListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                try {
                    TextView v = (TextView) view.findViewById(R.id.items_name);
                    final String item_name = (String) v.getText();

                    TextView v2 = (TextView) view.findViewById(R.id.items_price);
                    final String item_price = (String) v2.getText();

                    TextView v3 = (TextView) view.findViewById(R.id.items_currency);
                    final String item_currency = (String) v3.getText();

                    TextView v4 = (TextView) view.findViewById(R.id.items_alert);
                    final String item_alert = (String) v4.getText();

                    new AlertDialog.Builder(Items_activity.this)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setTitle(getString(R.string.Modify_Item))
                            //.setTitle(getString(R.string.leaving))
                            .setMessage(getString(R.string.do_you_want_to_modify_it))
                            //.setMessage(getString(R.string.leave_sure))
                            .setPositiveButton(getString(R.string.Yes), new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //Toast.makeText(getApplicationContext(), item_name, Toast.LENGTH_SHORT).show();

                                    //Stiben
                                    Intent i = new Intent(Items_activity.this, ModifyItemActivity.class);
                                    i.putExtra("GroupID", GroupID);
                                    i.putExtra("GroupName", GroupName);
                                    i.putExtra("ItemName", item_name);
                                    i.putExtra("Item_price", item_price);
                                    i.putExtra("Item_currency", item_currency);
                                    i.putExtra("Item_alert", item_alert);
                                    startActivity(i);
                                    //////End Stiben
                                }
                            })
                            .setNegativeButton("No", null)
                            .show();

                }catch (Exception e){
                    Log.d("ja","ja");
                }

                    return false;

            }
        });



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_item);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getApplicationContext(),"Funciona", Toast.LENGTH_SHORT).show();

                Intent i = new Intent(view.getContext(), new_item_activity.class);
                i.putExtra("group_id",GroupID);
                i.putExtra("group_name",GroupName);

                startActivity(i);

            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items_class to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_seeIndividuals) {
            //return true;
            //Toast.makeText(getApplicationContext(),item.getTitle(), Toast.LENGTH_SHORT).show();

            Intent i = new Intent(this, members_activity.class);
            i.putExtra("group_id",GroupID);
            i.putExtra("GroupName",GroupName);
            startActivity(i);
            return true;

        }

        if (id == R.id.action_ModifyGroup) {

            Intent i = new Intent(this, ModifyActivity.class);
            i.putExtra("GroupID",GroupID);
            i.putExtra("GroupName",GroupName);
            startActivity(i);
            return true;

        }

        if (id == R.id.action_invitePeople) {
            Intent i = new Intent(this, Invite_Activity.class);
            i.putExtra("GroupID",GroupID);
            i.putExtra("GroupName",GroupName);
            startActivity(i);
            return true;

        }

        if (id == R.id.action_leaveGroup) {
            final boolean[] group_deleted = {false};
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    //.setTitle("Leaving Group")
                    .setTitle(getString(R.string.leaving))
                    //.setMessage("Are you sure you want to leave this group?")
                    .setMessage(getString(R.string.leave_sure))
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //Toast.makeText(getApplicationContext(),"pues estoy embarazada", Toast.LENGTH_SHORT).show();
                            //finish();

                            final FirebaseAuth mAuth = FirebaseAuth.getInstance();


                           ////////////////////7 ELIMINAR SI NO HAY MIEMBROS

                            final Firebase firebase = new Firebase(Config.FIREBASE_URL).child("Groups");

                            firebase.addListenerForSingleValueEvent(new ValueEventListener() {
                                                               @Override
                                                               public void onDataChange(DataSnapshot dataSnapshot) {
                                                                   int members= (int) dataSnapshot.child(GroupID).child("members").getChildrenCount();
                                                                   if (members==1){

                                                                           firebase.child(GroupID).removeValue();
                                                                           group_deleted[0] = true;

                                                                   }
                                                                   else{
                                                                       group_deleted[0] =false;
                                                                   }
                                                               }

                                                               @Override
                                                               public void onCancelled(FirebaseError firebaseError) {

                                                               }
                                                           });
                            //////////////// ELIMINAR SI NO HAY MIEMBROS END


                            ////////delete from group members


                               DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                               //ref.child("Groups").child("members").orderByChild("userID").equalTo(mAuth.getCurrentUser().getUid());
                            if (group_deleted[0] ==false) {
                               ref.child("Groups").child(GroupID).child("members").orderByChild("userID").equalTo(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
                                   //
                                   @Override
                                   public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {


                                       for (com.google.firebase.database.DataSnapshot ref : dataSnapshot.getChildren()) {
                                           ref.getRef().removeValue();


                                       }
                                   }

                                   @Override
                                   public void onCancelled(DatabaseError databaseError) {

                                   }
                               });
                            }
                            ////////delete from group members-end


                            ///delete from user groups INSIDE USER
                            //DatabaseReference ref_userg = FirebaseDatabase.getInstance().getReference();
                            ref.child("Users").child(mAuth.getCurrentUser().getUid()).child("groups").orderByChild("groupID").equalTo(GroupID).addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
                                @Override
                                public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {

                                    for (com.google.firebase.database.DataSnapshot ref: dataSnapshot.getChildren()) {
                                        ref.getRef().removeValue();

                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }

                            });
                            group_deleted[0]=false;
                            finish();
                            ///delete from user groups-end



                        }

                    })
                    .setNegativeButton("No", null)
                    .show();

        }
        return super.onOptionsItemSelected(item);
        //Toast.makeText(getApplicationContext(),"perro", Toast.LENGTH_SHORT).show();
        //return true;
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


class member_class{

    public String userID;
    public boolean paid;

    public member_class(String userID) {
        this.userID=userID;
        paid=false;
    }

    void setPaid(boolean x){        this.paid=x;    }
    boolean getPaid(){return this.paid;}

    void setUserID(String x){        this.userID=x;    }
    String getUserID(){return this.userID;}



}

