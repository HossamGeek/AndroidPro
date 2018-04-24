package com.example.triprecycler;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.sql.DatabaseMetaData;
import java.util.Calendar;
import java.util.List;

public class EditActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener,GoogleApiClient.ConnectionCallbacks {


    private FirebaseAuth firebaseAuth;
    public  static  final String MY_PREFS_NAME="File";

    Intent incomingIntent;
    EditText tripName;
    EditText tripNote;
    Button addTrip;
    DatabaseReference reference;

    //check box for done trips

    CheckBox checkBox;

    // autocomplete textview

    private AutoCompleteTextView from;
    private AutoCompleteTextView to;
    private static final int GOOGLE_API_CLIENT_ID = 0;
    private GoogleApiClient mGoogleApiClient;
    private PlaceArrayAdapter mPlaceArrayAdapter;
    private static final LatLngBounds BOUNDS_MOUNTAIN_VIEW = new LatLngBounds(
            new LatLng(37.398160, -122.180831), new LatLng(37.430610, -121.972090));

    //date picker

    private int mYear;
    private int mMonth;
    private int mDay;

    private TextView mDateDisplay;
    private Button mPickDate;

    static final int DATE_DIALOG_ID = 0;


    //time picker

    private int mHour;
    private int mMin;
    private int mSec;


    private TextView mTimeDisplay;
    private Button mPickTime;

    static final int TIME_DIALOG_ID = 1;

    private String destination;
    private String station;
    @RequiresApi(api = Build.VERSION_CODES.N)


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        tripName = findViewById(R.id.edit_tripName);
        tripNote = findViewById(R.id.edit_tripNote);
        addTrip = findViewById(R.id.saveTrip);
        checkBox = findViewById(R.id.checkTrip);

        //incomingIntent = getIntent();
        SharedPreferences sharedPreferences =  getSharedPreferences(MY_PREFS_NAME, 0);

        final   String EmailUsr= sharedPreferences.getString("email", "");


        FirebaseApp.initializeApp(this);
        firebaseAuth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference("DataBase").child("Trips");



        //autocomplete textview
        from = findViewById(R.id.edit_tripFrom);
        to = findViewById(R.id.edit_tripTo);
        to.setThreshold(4);
        from.setThreshold(4);

        mGoogleApiClient = new GoogleApiClient.Builder(EditActivity.this)
                .addApi(Places.GEO_DATA_API)
                .enableAutoManage(this,GOOGLE_API_CLIENT_ID,this)
                .addConnectionCallbacks(this)
                .build();
        from.setOnItemClickListener(mAutocompleteClickListener);
        to.setOnItemClickListener(mAutocompleteClickListener);
        mPlaceArrayAdapter = new PlaceArrayAdapter(this, android.R.layout.simple_list_item_1,
                BOUNDS_MOUNTAIN_VIEW, null);
        from.setAdapter(mPlaceArrayAdapter);
        to.setAdapter(mPlaceArrayAdapter);



        //Autocomplete Fields
        //Station
//        final PlaceAutocompleteFragment autocompleteFragmentfrom = (PlaceAutocompleteFragment)
//                getFragmentManager().findFragmentById(R.id.edit_tripFrom);
//
//        autocompleteFragmentfrom.setOnPlaceSelectedListener(new PlaceSelectionListener() {
//            @Override
//            public void onPlaceSelected(Place place) {
//                station = place.getName().toString();
//                //Log.i(TAG, "Place: " + place.getName().toString());
//            }
//
//            @Override
//            public void onError(Status status) {
//                Toast.makeText(EditActivity.this,"An error occurred: " + status,Toast.LENGTH_SHORT).show();
//                //Log.i(TAG, "An error occurred: " + status);
//            }
//        });

        //Destination
//        final PlaceAutocompleteFragment autocompleteFragmentto = (PlaceAutocompleteFragment)
//                getFragmentManager().findFragmentById(R.id.edit_tripTo);
//
//        autocompleteFragmentto.setOnPlaceSelectedListener(new PlaceSelectionListener() {
//            @Override
//            public void onPlaceSelected(Place place) {
//                destination =  place.getName().toString();
//                //Log.i(TAG, "Place: " + place.getName().toString());
//            }
//
//            @Override
//            public void onError(Status status) {
//                Toast.makeText(EditActivity.this,"An error occurred: " + status,Toast.LENGTH_SHORT).show();
//                //Log.i(TAG, "An error occurred: " + status);
//            }
//        });


        //Date And Time

        mDateDisplay = (TextView) findViewById(R.id.edit_showMyDate);
        mPickDate = (Button) findViewById(R.id.edit_myDatePickerButton);

        //Date picker
        mPickDate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDialog(DATE_DIALOG_ID);
            }
        });

        // get the current date
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        // display the current date
        updateDisplay();

        //time picker
        mTimeDisplay = (TextView) findViewById(R.id.edit_showMyTime);
        mPickTime = (Button) findViewById(R.id.edit_myTimePickerButton);

        mPickTime.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDialog(TIME_DIALOG_ID);
            }
        });

        // get the current date
        final Calendar mcurrentTime = Calendar.getInstance();
        mHour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        mMin = mcurrentTime.get(Calendar.MINUTE);





        // display the current date
        updateDisplayTime();




        final String tripId = getIntent().getStringExtra("id");
        Toast.makeText(this, tripId, Toast.LENGTH_SHORT).show();



        addTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!TextUtils.isEmpty(mDateDisplay.getText().toString())
                && !TextUtils.isEmpty(mTimeDisplay.getText().toString())
                && !TextUtils.isEmpty(tripName.getText().toString())
                && !TextUtils.isEmpty(tripNote.getText().toString())
                && !TextUtils.isEmpty(from.getText().toString())
                && !TextUtils.isEmpty(to.getText().toString())){

                    Query query = reference.orderByChild("tripId").equalTo(tripId);


                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                // dataSnapshot is the "issue" node with all children with id 0
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                                    // do something with the individual "issues"

                                    snapshot.getRef().child("startDate").setValue(mDateDisplay.getText().toString());
                                    snapshot.getRef().child("startTime").setValue(mTimeDisplay.getText().toString());
                                    snapshot.getRef().child("tripDestination").setValue(to.getText().toString());
                                    snapshot.getRef().child("tripName").setValue(tripName.getText().toString());
                                    snapshot.getRef().child("tripNote").setValue(tripNote.getText().toString());
                                    snapshot.getRef().child("tripStation").setValue(from.getText().toString());
                                    if (checkBox.isChecked()){
                                        snapshot.getRef().child("tripStatus").setValue("done");
                                    }else{
                                        snapshot.getRef().child("tripStatus").setValue("upcoming");
                                    }
                                    Toast.makeText(EditActivity.this,"database ",Toast.LENGTH_SHORT).show();


                                }
                            }
                            finish();
                            Intent intent = new Intent(EditActivity.this,MainActivity.class);
                            startActivity(intent);

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });




                    Toast.makeText(EditActivity.this,"All Fields succes",Toast.LENGTH_SHORT).show();



                }else {
                    Toast.makeText(EditActivity.this,"All Fields Are Required",Toast.LENGTH_SHORT).show();                }

            }
        });

        Query query = reference.orderByChild("tripId").equalTo(tripId);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // dataSnapshot is the "issue" node with all children with id 0
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                        // do something with the individual "issues"

                        Trip model1 = snapshot.getValue(Trip.class);


                        tripName.setText(model1.getTripName());
                        tripNote.setText(model1.getTripNote());
                        mTimeDisplay.setText(model1.getStartTime());
                        mDateDisplay.setText(model1.getStartDate());

                        from.setText(model1.getTripStation());
                        to.setText(model1.getTripDestination());
                        Log.v("DataobName",model1.getTripName());


                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

//         reference.addValueEventListener(new ValueEventListener() {
//             @Override
//             public void onDataChange(DataSnapshot dataSnapshot) {
//                 for (DataSnapshot snapshot: dataSnapshot.getChildren()){
//                     Trip trip = snapshot.getValue(Trip.class);
//                     //Log.i("IDDD",trip.getTripId());
//
//
//                 }
//             }
//
//             @Override
//             public void onCancelled(DatabaseError databaseError) {
//
//             }
//         });

    }


    private AdapterView.OnItemClickListener mAutocompleteClickListener
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            final PlaceArrayAdapter.PlaceAutocomplete item = mPlaceArrayAdapter.getItem(position);
            final String placeId = String.valueOf(item.placeId);
            //Log.i(TAG, "Selected: " + item.description);
            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(mGoogleApiClient, placeId);
            //placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
            //Log.i(TAG, "Fetching details for ID: " + item.placeId);
        }
    };

    private void updateDisplayTime() {
        this.mTimeDisplay.setText(
                new StringBuilder()
                        // Month is 0 based so add 1
                        .append(mHour).append(":")
                        .append(mMin));
    }

    private void updateDisplay() {
        this.mDateDisplay.setText(
                new StringBuilder()
                        // Month is 0 based so add 1
                        .append(mMonth + 1).append("-")
                        .append(mDay).append("-")
                        .append(mYear).append(" "));
    }

    private TimePickerDialog.OnTimeSetListener mTimeSetListener =
            new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int hour, int minite) {
                    mHour = hour;
                    mMin = minite;
                    updateDisplayTime();
                }


            };

    private DatePickerDialog.OnDateSetListener mDateSetListener =
            new DatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker view, int year,
                                      int monthOfYear, int dayOfMonth) {
                    mYear = year;
                    mMonth = monthOfYear;
                    mDay = dayOfMonth;
                    updateDisplay();
                }
            };

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG_ID:
                return new DatePickerDialog(this,
                        mDateSetListener,
                        mYear, mMonth, mDay);



            case TIME_DIALOG_ID:
                return new TimePickerDialog(this,mTimeSetListener,mHour,mMin,true);


        }
        return null;
    }


    @Override
    public void onClick(View v) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mPlaceArrayAdapter.setGoogleApiClient(mGoogleApiClient);
    }

    @Override
    public void onConnectionSuspended(int i) {
        mPlaceArrayAdapter.setGoogleApiClient(null);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this,
                "Google Places API connection failed with error code:" +
                        connectionResult.getErrorCode(),
                Toast.LENGTH_LONG).show();

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
