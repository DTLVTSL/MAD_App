package com.example.mad.lab2;

import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;


public class MainActivity extends AppCompatActivity {

    ListView ListView;
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference mDatabaseReference = database.getReference();
    //Stiben
    Button CloSes;

    final ArrayList group_list = new ArrayList();
    int update_count=0;
    TextView total_debit;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("MAD APP");

        /////////////Stiben Abr29
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.add_group);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), new_group_activity.class);
                startActivity(i);

            }
        });



       //Stiben
        final FloatingActionButton update = (FloatingActionButton) findViewById(R.id.update_button);

        update.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                finish();
                startActivity(getIntent());

            }
        });



       //Stiben
        CloSes = (Button) findViewById(R.id.clos_ses);
        CloSes.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainActivity.this, login_new_user.class));

            }
        });
        //

        //daniel1

        ////PENDIENTES
        ///SACAR LA LISTA DE GRUPOS Q SE TIENEN

        Firebase.setAndroidContext(this);
        FirebaseAuth mAuth;
        mAuth = FirebaseAuth.getInstance();
        final String userID=mAuth.getCurrentUser().getUid();

        Firebase firebase = new Firebase(Config.FIREBASE_URL).child("Users").child(userID).child("groups");

        final HashMap<String,HashMap<String,String>> data_items = new HashMap<>();
        firebase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {


                group_list.clear();
                String groupID;
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {

                    groupID=postSnapshot.child("groupID").getValue().toString();

                    group_list.add(groupID);

                }



                update_count=update_count+1; //
                if (update_count>1) {

                    //update.setVisibility(View.VISIBLE);
                    finish();
                    startActivity(getIntent());
                    //Toast.makeText(getApplicationContext(),"Your group list has changed", Toast.LENGTH_SHORT).show();
                    //Toast.makeText(getApplicationContext(),getString(R.string.group_changed), Toast.LENGTH_SHORT).show();
                }

            }


            @Override
            public void onCancelled(FirebaseError error) {
            }

        });


        final ArrayList<groups_class> data_groups2 = new ArrayList<groups_class>();
        final ArrayList<doubts_class> data_doubts= new ArrayList<doubts_class>();


        final groups_adapter adapter = new groups_adapter(this, R.layout.listview_groups_row, data_groups2,data_doubts);

        // MOSTRAR LOS GRUPOS Q SE SACARON DE LA LISTA EN EL LIST
        Firebase.setAndroidContext(this);
        firebase = new Firebase(Config.FIREBASE_URL).child("Groups");

        firebase.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override

            public void onDataChange(DataSnapshot snapshot) {
                data_groups2.clear();
                data_doubts.clear();


                float all_doubts=0;

                for (DataSnapshot postSnapshot : snapshot.getChildren()) {



                    //Log.d("168: ", group_list.toString() +" "+ postSnapshot.getKey().toString()+ ""+(group_list.contains(postSnapshot.getKey().toString())));
                    if (group_list.contains(postSnapshot.getKey().toString())) {

                        HashMap<String, Object> Items_2 = (HashMap<String, Object>) postSnapshot.child("Items").getValue();
                        String noti = postSnapshot.child("noti").getValue().toString();
                        int icon = postSnapshot.child("icon").getValue(int.class);

                        String title = postSnapshot.child("title").getValue().toString();
                        String groupID=postSnapshot.child("groupID").getValue().toString();
                        groups_class group2 = new groups_class(groupID,noti, icon, title, Items_2);


                        //TOTAL PRICE OF GROUP

                        HashMap<String,String> items_price = (HashMap<String,String>) postSnapshot.child("Items").getValue();
                        float total_price=0;

                        boolean same_currency=false;
                        float contador_currency=0;
                        String temporal_currency=""; //voy a contar todos los iguales, 1==2, 2==3, 4==5 ... y cada acierto se suma en contador currency, si es igual a contador items entonces todos tienen el mismo currency
                        float contador_items=0;

                        for (DataSnapshot postSnapshot2 : postSnapshot.child("Items").getChildren()) {
                            total_price=total_price+Float.parseFloat(postSnapshot2.child("price").getValue().toString());

                            if(Objects.equals(temporal_currency, postSnapshot2.child("currency").getValue().toString())){
                                contador_currency=contador_currency+1;
                            }
                            temporal_currency=postSnapshot2.child("currency").getValue().toString();
                            contador_items=contador_items+1;
                        }

                        if((contador_items-1)==contador_currency){same_currency=true;}


                        //TOTAL MEMBERS

                        float total_members=postSnapshot.child("members").getChildrenCount();;

                        float divided_price=0;
                        if (total_members!=0){
                         divided_price=total_price/total_members;}




                        //check if I had paid

                        boolean paid;
                        try {
                            paid = Boolean.parseBoolean(postSnapshot.child("members").child(userID).child("paid").getValue().toString());
                        }catch (Exception e){paid=false;}

                        if(paid==false){
                            all_doubts=all_doubts+divided_price;

                        }
                        //paid_boolean.add(paid);

                        data_groups2.add(group2);
                        //data_doubts.add(new doubts_class(total_price,divided_price));

                        if (same_currency) {

                            data_doubts.add(new doubts_class(total_price,divided_price,temporal_currency,paid));
                        }
                        else {data_doubts.add(new doubts_class(total_price,divided_price,paid));}



                        ListView = (ListView) findViewById(R.id.group_list);
                        ListView.setAdapter(adapter);


                    }



                }

                total_debit= (TextView) findViewById(R.id.total_debit);
                total_debit.setText(String.valueOf(all_doubts));
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });



        //daniel1 fin


            //Creating the adapter
        ListView = (ListView) findViewById(R.id.group_list);


        View header = (View) getLayoutInflater().inflate(R.layout.list_header_row, null);
        ListView.addHeaderView(header);
        ListView.setAdapter(adapter);


        ListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView v = (TextView) view.findViewById(R.id.text1);
                TextView groupID =(TextView) view.findViewById(R.id.groupID);
                try {
                    Intent i = new Intent(view.getContext(), Items_activity.class);
                    i.putExtra("GroupID", groupID.getText());
                    i.putExtra("GroupName", v.getText());
                    startActivity(i);
                }catch (Exception e){

                }


            }
        });



        // MOSTRAR EL USERID

        Firebase.setAndroidContext(this);
        //daniel2 fin
        //Stiben
        mAuth = FirebaseAuth.getInstance();

        TextView userID_tv = (TextView) findViewById(R.id.userID);
        //userID_tv.setText(getString("Welcome: " + mAuth.getCurrentUser().getEmail());
        userID_tv.setText(getString(R.string.welcome) + mAuth.getCurrentUser().getEmail());
        //userID_tv.setVisibility(View.GONE);
    }


    boolean doubleBackToExitPressedOnce = false;
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            ActivityCompat.finishAffinity(MainActivity.this);
            finish();
            return;

        }

        this.doubleBackToExitPressedOnce = true;
        //Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
        Toast.makeText(this, getString(R.string.back), Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

}


class doubts_class {
    //SE TUBO QUE HACER ESTA CLASE PARA ENVIAR LOS PARAMETROS DE DEUDAS TOTALES Y DIVIDIDOS, PQ SI
    // SE PONIAN EN LA CLASE GRUPOS SE SUBIRIAN TAMBIEN CON ESOS VALORES A LA
    // BASE DE DATOS Y NO QUEREMOS ESO

    public float total;
    public float divided;
    public String currency;
    public boolean paid;

    public doubts_class(){
        super();
    }

    public doubts_class( float total,float divided,String currency, boolean paid){
        super();
        this.total = total;
        this.divided = divided;
        this.currency=currency;
        this.paid=paid;
    }
    public doubts_class( float total,float divided,boolean paid){
        super();
        this.total = total;
        this.divided = divided;
        this.currency="";
        this.paid=paid;
    }

    public void setTotal( float total){this.total=total;}
    public String getTotal(){return String.valueOf(this.total);}

    public void setDivided( float d){this.divided=d;}
    public String getDivided(){return String.valueOf(this.divided);}

    public void setCurrency( String d){this.currency=d;}
    public String getCurrency(){return String.valueOf(this.currency);}

    public void setPaid(boolean p){this.paid=p;}
    public boolean getPaid(){return this.paid;}

}
