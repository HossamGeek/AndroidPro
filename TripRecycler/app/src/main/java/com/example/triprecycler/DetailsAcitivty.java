package com.example.triprecycler;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class DetailsAcitivty extends AppCompatActivity implements MyAdapter.OnItemClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Intent receivedIntent = getIntent();
        final Trip trip = receivedIntent.getParcelableExtra("tripObject");
        ((TextView)findViewById(R.id.tripName)).setText(trip.getTripName());
        ((TextView)findViewById(R.id.tripFrom)).setText(trip.getTripStation());
        ((TextView)findViewById(R.id.tripTo)).setText(trip.getTripDestination());
        ((TextView)findViewById(R.id.showMyDate)).setText(trip.getStartDate());
        ((TextView)findViewById(R.id.showMyTime)).setText(trip.getStartTime());
        ((TextView)findViewById(R.id.tripNote)).setText(trip.getTripNote());

        ((Button)findViewById(R.id.startTrip)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uri = "http://maps.google.com/maps?saddr="+trip.getTripStation()+"&daddr="+trip.getTripDestination();
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                intent.setPackage("com.google.android.apps.maps");
                startActivity(intent);
            }
        });
    }

    @Override
    public void onItemClick(int postition) {

    }

}
