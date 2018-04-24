package com.example.triprecycler;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MyAdapter.OnItemClickListener {

    public static final String MY_PREFS_NAME = "File";


    FloatingActionButton fabPlus , fabOneWay , fabTwoWay;
    Animation fabOpen , fabClose , fabClockwise , fabAntiClockwise;
    Boolean isOpen = false;


    RecyclerView recyclerView;
    MyAdapter adapter;
    List<Trip> listItems;
    List<Trip> tripList;

    static{
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
    //--------add firebase --------
    private FirebaseAuth firebaseAuth;
    DatabaseReference reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences sharedPreferences =  getSharedPreferences(MY_PREFS_NAME, 0);
        final String emailUser = sharedPreferences.getString("email","");

        Intent sr = new Intent(MainActivity.this,MyService.class);
        //sr.putExtra("email",emailUser);

        startService(sr);


        Intent intent = new Intent(getBaseContext(), BroadCasts.class);
        intent.putExtra("email",emailUser);
        final int alarmID = (int) System.currentTimeMillis();
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                getBaseContext(),
                alarmID,
                intent,
                PendingIntent.FLAG_CANCEL_CURRENT);





//        Intent sr = new Intent(this,MyService.class);
//        startService(sr);
        //---------- firebase ------------

        FirebaseApp.initializeApp(this);
        firebaseAuth = FirebaseAuth.getInstance();

        reference = FirebaseDatabase.getInstance().getReference("DataBase").child("Trips");
        Query query = reference.orderByChild("emailuser").equalTo(emailUser);
        reference.keepSynced(true);

        listItems = new ArrayList<>();
        tripList = new ArrayList<Trip>();


        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                    Trip trip = snapshot.getValue(Trip.class);
                    tripList.add(trip);

                }
                //Log.i("Size",""+tripList.size());
                for (int i =0; i < tripList.size(); i++){
                    //listItems.add(new RecyclerItem("Trip "+(i+1),"2018-04-06"));
                    String status = "upcoming";
                    String dbStatus = tripList.get(i).getTripStatus();
                    if (status.compareTo(dbStatus) == 0){
                        listItems.add(new Trip(tripList.get(i).getTripId(),tripList.get(i).getTripName(),tripList.get(i).getStartDate()));
                        Log.i("Name",tripList.get(i).getTripName());
                        Log.i("Name",tripList.get(i).getTripStatus());
                    }
                }
                recyclerView = findViewById(R.id.recyclerView);
                //recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this,2));
                // data from firebase will be here ....

                adapter = new MyAdapter(listItems,MainActivity.this);
                recyclerView.setAdapter(adapter);

                adapter.setOnItemClickListener(MainActivity.this);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
//                    Trip trip = snapshot.getValue(Trip.class);
//                    tripList.add(trip);
//
//                }
//                //Log.i("Size",""+tripList.size());
//                for (int i =0; i < tripList.size(); i++){
//                    //listItems.add(new RecyclerItem("Trip "+(i+1),"2018-04-06"));
//                    listItems.add(new RecyclerItem(tripList.get(i).getTripName(),tripList.get(i).getStartDate()));
//                }
//                recyclerView = findViewById(R.id.recyclerView);
//                recyclerView.setHasFixedSize(true);
//                recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this,2));
//                // data from firebase will be here ....
//
//                adapter = new MyAdapter(tripList,MainActivity.this);
//                recyclerView.setAdapter(adapter);
//
//                adapter.setOnItemClickListener(MainActivity.this);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });



        //-----------code fab start
        fabPlus = findViewById(R.id.fab_plus);
        fabOneWay = findViewById(R.id.fab_one);
        fabTwoWay = findViewById(R.id.fab_two);
        fabOpen = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_open);
        fabClose = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_close);
        fabClockwise = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_clockwise);
        fabAntiClockwise = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_anticlockwise);
        fabPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isOpen){
                    fabOneWay.startAnimation(fabClose);
                    fabTwoWay.startAnimation(fabClose);
                    fabPlus.startAnimation(fabAntiClockwise);
                    fabOneWay.setClickable(false);
                    fabTwoWay.setClickable(false);
                    isOpen = false;

                }else {
                    fabOneWay.startAnimation(fabOpen);
                    fabTwoWay.startAnimation(fabOpen);
                    fabPlus.startAnimation(fabClockwise);
                    fabOneWay.setClickable(true);
                    fabTwoWay.setClickable(true);
                    isOpen = true;
                }
            }
        });
        fabOneWay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toOneTripIntent = new Intent(MainActivity.this,OneTripActivity.class);
                startActivity(toOneTripIntent);
            }
        });
        fabTwoWay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toTwoTripIntent = new Intent(MainActivity.this,TwoTripsActivity.class);
                startActivity(toTwoTripIntent);
            }
        });



        //---------------------- end fab code

    }

    @Override
    public void onItemClick(int postition) {
        Intent detailIntent = new Intent(this,DetailsAcitivty.class);
        detailIntent.putExtra("tripObject",  tripList.get(postition));
        startActivity(detailIntent);

//        Intent editIntent = new Intent(this,EditActivity.class);
//        editIntent.putExtra("tripId",  tripList.get(postition).getTripId());
        //startActivity(editIntent);
        //RecyclerItem clickedItem = listItems.get(postition);
       // Log.i("TAGGG",listItems.get(postition).getTripTitle());

        Toast.makeText(this, ""+listItems.get(postition).getTripName(), Toast.LENGTH_SHORT).show();
//

    }

}
