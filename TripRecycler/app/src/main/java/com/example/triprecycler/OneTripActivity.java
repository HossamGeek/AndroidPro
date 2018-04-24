package com.example.triprecycler;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.location.places.ui.SupportPlaceAutocompleteFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.util.Calendar;

public class OneTripActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener,GoogleApiClient.ConnectionCallbacks{

//    static{
//        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
//    }

    private FirebaseAuth firebaseAuth;
    public  static  final String MY_PREFS_NAME="File";

    Intent incomingIntent;
    EditText tripName;
    EditText tripNote;
    Button addTrip;
    DatabaseReference reference;

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
        setContentView(R.layout.activity_one_trip);

/*
        Intent sr = new Intent(this,MyService.class);
        startService(sr);*/

        SharedPreferences sharedPreferences =  getSharedPreferences(MY_PREFS_NAME, 0);

        final   String EmailUsr= sharedPreferences.getString("email", "");

        firebaseAuth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference("DataBase").child("Trips");

        tripName = findViewById(R.id.one_tripName);
        tripNote = findViewById(R.id.one_tripNote);
        addTrip = findViewById(R.id.one_addTrip);

        //autocomplete textview
        from = findViewById(R.id.one_place_autocomplete_fragment_from);
        to = findViewById(R.id.one_place_autocomplete_fragment_to);
        to.setThreshold(4);
        from.setThreshold(4);

        mGoogleApiClient = new GoogleApiClient.Builder(OneTripActivity.this)
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
//                getFragmentManager().findFragmentById(R.id.one_place_autocomplete_fragment_from);
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
//                Toast.makeText(OneTripActivity.this,"An error occurred: " + status,Toast.LENGTH_SHORT).show();
//                //Log.i(TAG, "An error occurred: " + status);
//            }
//        });

        //Destination
//        final PlaceAutocompleteFragment autocompleteFragmentto = (PlaceAutocompleteFragment)
//                getFragmentManager().findFragmentById(R.id.one_place_autocomplete_fragment_to);
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
//                Toast.makeText(OneTripActivity.this,"An error occurred: " + status,Toast.LENGTH_SHORT).show();
//                //Log.i(TAG, "An error occurred: " + status);
//            }
//        });

        //Date And Time

        mDateDisplay = (TextView) findViewById(R.id.one_showMyDate);
        mPickDate = (Button) findViewById(R.id.one_myDatePickerButton);

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
        mTimeDisplay = (TextView) findViewById(R.id.one_showMyTime);
        mPickTime =  findViewById(R.id.one_myTimePickerButton);

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


        addTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!TextUtils.isEmpty(mDateDisplay.getText().toString()) &&
                        !TextUtils.isEmpty(mTimeDisplay.getText().toString()) &&
                        !TextUtils.isEmpty(tripNote.getText().toString()) &&
                        !TextUtils.isEmpty(tripName.getText().toString()) &&
                        !TextUtils.isEmpty(from.getText().toString()) &&
                        !TextUtils.isEmpty(to.getText().toString())
                        ){


                    String tripstatus = "upcoming";
                    String key = reference.push().getKey();


                    Trip NewTrip = new Trip(tripName.getText().toString(),from.getText().toString(),to.getText().toString(),mDateDisplay.getText().toString(),
                            mTimeDisplay.getText().toString(), tripNote.getText().toString(),key,EmailUsr,tripstatus);

                    reference.push().setValue(NewTrip);
                    Toast.makeText(OneTripActivity.this,"trip add succes",Toast.LENGTH_SHORT).show();

//                    setAlarm();
//                    autocompleteFragmentfrom.setText("");
//                    autocompleteFragmentto.setText("");
                    finish();
                    Intent intent = new Intent(OneTripActivity.this,MainActivity.class);
                    startActivity(intent);


                }else {
                    Toast.makeText(OneTripActivity.this,"All Fields Are Required",Toast.LENGTH_SHORT).show();                }

            }
        });





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

//    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback
//            = new ResultCallback<PlaceBuffer>() {
//        @Override
//        public void onResult(PlaceBuffer places) {
//            if (!places.getStatus().isSuccess()) {
//                Log.e(TAG, "Place query did not complete. Error: " +
//                        places.getStatus().toString());
//                return;
//            }
//            // Selecting the first object buffer.
//            final Place place = places.get(0);
//            CharSequence attributions = places.getAttributions();
//
//        }
//    };


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
                DatePickerDialog dialog =  new DatePickerDialog(this,
                        mDateSetListener,
                        mYear, mMonth, mDay);

                ///set update date

                dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                return  dialog;

            case TIME_DIALOG_ID:
                TimePickerDialog times =  new TimePickerDialog(this,mTimeSetListener,mHour,mMin,true);

                return times;

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
    private void setAlarm() {

        Calendar cal = Calendar.getInstance();
        cal.set(mYear,
                mMonth,
                mDay,
                mHour,
                mMin,
                00);
        Log.v("sss", cal.toString());

        Intent intent = new Intent(getBaseContext(), AlarmReceiver.class);
        intent.putExtra("note", tripNote.getText().toString());
        intent.putExtra("from", from.getText().toString());
        intent.putExtra("to", to.getText().toString());
        final int alarmID = (int) System.currentTimeMillis();
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                getBaseContext(),
                alarmID,
                intent,
                PendingIntent.FLAG_CANCEL_CURRENT);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
    }

}
