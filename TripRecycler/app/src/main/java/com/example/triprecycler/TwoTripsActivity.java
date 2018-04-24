package com.example.triprecycler;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class TwoTripsActivity extends AppCompatActivity implements View.OnClickListener{

    private FirebaseAuth firebaseAuth;
    public  static  final String MY_PREFS_NAME="File";

    Intent incomingIntent;
    EditText tripName;
    EditText tripNote;
    EditText tripNoteTrip2;
    Button addTrip;
    DatabaseReference reference;



    //date picker

    private int mYear;
    private int mMonth;
    private int mDay;

    private TextView mDateDisplay;
    private TextView mDateDisplayTrip2;

    private Button mPickDate;
    private Button mPickDateTrip2;

    static final int DATE_DIALOG_ID = 0;
    static final int DATE_DIALOG_ID_Trip2 = 3;


    //time picker

    private int mHour;
    private int mMin;
    private int mSec;


    private TextView mTimeDisplay;
    private TextView mTimeDisplayTrip2;

    private Button mPickTime;
    private Button mPickTimeTrip2;

    static final int TIME_DIALOG_ID = 1;
    static final int TIME_DIALOG_ID_Trip2 = 2;

    private String destination;
    private String station;

    private String destinationTrip2;

    private String stationTrip2;


    @RequiresApi(api = Build.VERSION_CODES.N)


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two_trips);


        SharedPreferences sharedPreferences =  getSharedPreferences(MY_PREFS_NAME, 0);

        final  String EmailUsr= sharedPreferences.getString("email", "");
        firebaseAuth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference("DataBase").child("Trips");

        //trip name is the same
        tripName = findViewById(R.id.two_tripName);

        // notes for th
        tripNote = findViewById(R.id.two_tripNote);
        tripNoteTrip2 = findViewById(R.id.two_tripNote_trip2);

        addTrip = findViewById(R.id.two_addTrip);

        //Autocomplete Fields trip 1
        //Station

        final PlaceAutocompleteFragment autocompleteFragmentfrom = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.two_place_autocomplete_fragment_from);

        autocompleteFragmentfrom.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                station = place.getName().toString();
                //Log.i(TAG, "Place: " + place.getName().toString());
            }

            @Override
            public void onError(Status status) {
                Toast.makeText(TwoTripsActivity.this,"An error occurred: " + status,Toast.LENGTH_SHORT).show();
                //Log.i(TAG, "An error occurred: " + status);
            }
        });

        //Destination
        final PlaceAutocompleteFragment autocompleteFragmentto = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.two_place_autocomplete_fragment_to);

        autocompleteFragmentto.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                destination =  place.getName().toString();
                //Log.i(TAG, "Place: " + place.getName().toString());
            }

            @Override
            public void onError(Status status) {
                Toast.makeText(TwoTripsActivity.this,"An error occurred: " + status,Toast.LENGTH_SHORT).show();
                //Log.i(TAG, "An error occurred: " + status);
            }
        });

        //Autocomplete Fields trip 2
        //Station

        final PlaceAutocompleteFragment autocompleteFragmentfromtrip2 = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.two_place_autocomplete_fragment_from_2);

        autocompleteFragmentfromtrip2.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                stationTrip2 = place.getName().toString();
                //Log.i(TAG, "Place: " + place.getName().toString());
            }

            @Override
            public void onError(Status status) {
                Toast.makeText(TwoTripsActivity.this,"An error occurred: " + status,Toast.LENGTH_SHORT).show();
                //Log.i(TAG, "An error occurred: " + status);
            }
        });

        //Destination
        final PlaceAutocompleteFragment autocompleteFragmenttotrip2 = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.two_place_autocomplete_fragment_to_2 );

        autocompleteFragmenttotrip2.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                destinationTrip2 =  place.getName().toString();
                //Log.i(TAG, "Place: " + place.getName().toString());
            }

            @Override
            public void onError(Status status) {
                Toast.makeText(TwoTripsActivity.this,"An error occurred: " + status,Toast.LENGTH_SHORT).show();
                //Log.i(TAG, "An error occurred: " + status);
            }
        });

        //Date And Time

        mDateDisplay = (TextView) findViewById(R.id.two_showMyDate);

        mPickDate = (Button) findViewById(R.id.two_myDatePickerButton);

        mDateDisplayTrip2 = (TextView) findViewById(R.id.two_showMyDate_trip2);

        mPickDateTrip2 = (Button) findViewById(R.id.two_myDatePickerButton_trip2);

        //Date picker

        mPickDate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDialog(DATE_DIALOG_ID);
            }
        });

        mPickDateTrip2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDialog(DATE_DIALOG_ID_Trip2);
            }
        });

        // get the current date
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        updateDisplay();
        updateDisplayTrip2();

        //time picker
        mTimeDisplay = (TextView) findViewById(R.id.two_showMyTime);

        mPickTime =  findViewById(R.id.two_myTimePickerButton);

        mTimeDisplayTrip2 = (TextView) findViewById(R.id.two_showMyTime_trip2);

        mPickTimeTrip2 =  findViewById(R.id.two_myTimePickerButton_trip2);


        mPickTime.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDialog(TIME_DIALOG_ID);
            }
        });

        mPickTimeTrip2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDialog(TIME_DIALOG_ID_Trip2);
            }
        });

        // get the current date
        final Calendar mcurrentTime = Calendar.getInstance();
        mHour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        mMin = mcurrentTime.get(Calendar.MINUTE);

        // display the current date
        updateDisplayTime();
        updateDisplayTimeTrip2();

        addTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!TextUtils.isEmpty(mDateDisplay.getText().toString()) &&
                        !TextUtils.isEmpty(mDateDisplayTrip2.getText().toString()) &&
                        !TextUtils.isEmpty(mTimeDisplayTrip2.getText().toString()) &&
                        !TextUtils.isEmpty(mTimeDisplay.getText().toString()) &&
                        !TextUtils.isEmpty(tripNote.getText().toString()) &&
                        !TextUtils.isEmpty(tripNoteTrip2.getText().toString()) &&
                        !TextUtils.isEmpty(tripName.getText().toString()) &&
                        !TextUtils.isEmpty(autocompleteFragmentfrom.getContext().toString()) &&
                        !TextUtils.isEmpty(autocompleteFragmentfromtrip2.getContext().toString()) &&
                        !TextUtils.isEmpty(autocompleteFragmenttotrip2.getContext().toString()) &&
                        !TextUtils.isEmpty(autocompleteFragmentto.getContext().toString())
                        ){


                    String key = reference.push().getKey();
                    String tripstatus = "upcoming";


                    Trip NewTrip = new Trip(tripName.getText().toString(),station,destination,mDateDisplay.getText().toString(),
                            mTimeDisplay.getText().toString(), tripNote.getText().toString(),key,EmailUsr,tripstatus);



                    //trip2


                    String key2 = reference.push().getKey();


                    Trip NewTrip2 = new Trip(tripName.getText().toString(),stationTrip2,destinationTrip2,mDateDisplayTrip2.getText().toString(),
                            mTimeDisplayTrip2.getText().toString(), tripNoteTrip2.getText().toString(),key2,EmailUsr,tripstatus);



                    reference.push().setValue(NewTrip);

                    reference.push().setValue(NewTrip2);

                    Toast.makeText(TwoTripsActivity.this,"All Fields succes",Toast.LENGTH_SHORT).show();


                    tripName.setText("");
                    tripNote.setText("");
                    tripNoteTrip2.setText("");

                    mTimeDisplay.setText("");
                    mDateDisplay.setText("");

                    mTimeDisplayTrip2.setText("");
                    mDateDisplayTrip2.setText("");

                    autocompleteFragmentfrom.setText("");
                    autocompleteFragmentto.setText("");


                    autocompleteFragmentfromtrip2.setText("");
                    autocompleteFragmenttotrip2.setText("");

                    finish();
                    Intent intent = new Intent(TwoTripsActivity.this,MainActivity.class);
                    startActivity(intent);



                }else {
                    Toast.makeText(TwoTripsActivity.this,"All Fields Are Required",Toast.LENGTH_SHORT).show();                }

            }
        });
    }


    private void updateDisplay() {
        this.mDateDisplay.setText(
                new StringBuilder()
                        // Month is 0 based so add 1
                        .append(mMonth + 1).append("-")
                        .append(mDay).append("-")
                        .append(mYear).append(" "));

    }

    private void updateDisplayTrip2() {
        this.mDateDisplayTrip2.setText(
                new StringBuilder()
                        // Month is 0 based so add 1
                        .append(mMonth + 1).append("-")
                        .append(mDay).append("-")
                        .append(mYear).append(" "));
    }
    private void updateDisplayTime() {
        this.mTimeDisplay.setText(
                new StringBuilder()
                        // Month is 0 based so add 1
                        .append(mHour).append(":")
                        .append(mMin));
    }

    private void updateDisplayTimeTrip2() {
        this.mTimeDisplayTrip2.setText(
                new StringBuilder()
                        // Month is 0 based so add 1
                        .append(mHour).append(":")
                        .append(mMin));

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

    private TimePickerDialog.OnTimeSetListener mTimeSetListenerTrip2 =
            new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int hour, int minite) {
                    mHour = hour;
                    mMin = minite;
                    updateDisplayTimeTrip2();
                }


            };

    private DatePickerDialog.OnDateSetListener mDateSetListenerTrip2 =
            new DatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker view, int year,
                                      int monthOfYear, int dayOfMonth) {
                    mYear = year;
                    mMonth = monthOfYear;
                    mDay = dayOfMonth;
                    updateDisplayTrip2();


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
                return  times;

            case DATE_DIALOG_ID_Trip2:
                DatePickerDialog dialog2 =  new DatePickerDialog(this,
                        mDateSetListenerTrip2,
                        mYear, mMonth, mDay);

                ///set update date

                dialog2.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                return  dialog2;

            case TIME_DIALOG_ID_Trip2:
                TimePickerDialog times2 =  new TimePickerDialog(this,mTimeSetListenerTrip2,mHour,mMin,true);
                return  times2;

        }
        return null;
    }



    @Override
    public void onClick(View v) {

    }
}
