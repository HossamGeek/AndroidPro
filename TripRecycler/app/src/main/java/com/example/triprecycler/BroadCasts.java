package com.example.triprecycler;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Hossa on 4/12/2018.
 */

public class BroadCasts extends BroadcastReceiver {



    DatabaseReference reference;
    public  static  final String MY_PREFS_NAME="File";

    private int mHour;
    private int mMin;
    private int mYear;
    private int mMonth;
    private int mDay;

    //    private List<String> dbTime;

    public static final String ACTION_STOP = "stop";
    Intent incoming;
    private boolean stop=false;
    @RequiresApi(api = Build.VERSION_CODES.N)





    @Override
    public void onReceive(final Context context, final Intent intent) {




//        final List<Trip> trips = new ArrayList<>();



        final String EmailUser = intent.getStringExtra("email");

        Log.v("DatadbDate", "Hello");



        final List<String> dbTime = new ArrayList<>();

        final Handler handler = new Handler();

        final Boolean[] Tags = {true};
/*

        SharedPreferences sharedPreferences;
        sharedPreferences = getSharedPreferences(MY_PREFS_NAME, 0);

        final   String EmailUsr= sharedPreferences.getString("email", "");
*/

        final Runnable r = new Runnable() {


            public void run() {




                handler.postDelayed(this, 60000);

                reference= FirebaseDatabase.getInstance().getReference("DataBase").child("Trips");


                // get the current date
                final Calendar c=Calendar.getInstance();
                mYear=c.get(Calendar.YEAR);
                mMonth=c.get(Calendar.MONTH);
                mDay=c.get(Calendar.DAY_OF_MONTH);







                // get the current date
                final Calendar mcurrentTime=Calendar.getInstance();
                mHour=mcurrentTime.get(Calendar.HOUR_OF_DAY);
                mMin=mcurrentTime.get(Calendar.MINUTE);






                final SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy", Locale.getDefault());

                final Calendar calendar1 = Calendar.getInstance();
                final Calendar calendar2 = Calendar.getInstance();


                StringBuilder curDate = new StringBuilder();
                final StringBuilder curTime = new StringBuilder();

                //current date

                curDate.append(mMonth+1).append("-");
                curDate.append(mDay).append("-");
                curDate.append(mYear);


                //current time
                curTime.append(mHour+ 2).append(":");
                curTime.append(mMin);



                final String strCurDate = curDate.toString();
                final String strCurTime = curTime.toString();


                try {
                    Date date1 = dateFormat.parse(strCurDate);
                    calendar1.setTime(date1);
                } catch (ParseException e) {
                    e.printStackTrace();
                }






                Log.v("DatadbDate", strCurDate);

                Log.v("DatadbDate", strCurTime);



                Query queryusr = reference.orderByChild("emailuser").equalTo(EmailUser);



                queryusr.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {


                            List<Trip> trips  =   new ArrayList<>();


                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                Trip model1 = snapshot.getValue(Trip.class);


                                trips.add(model1);



                            }



                            //start condition


                            for (int i = 0; i < trips.size(); i++) {

                                String statustrip = "upcoming";

                                String statusDb = trips.get(i).getTripStatus();

                                String strDbDate = trips.get(i).getStartDate();

                                String strDbTime = trips.get(i).getStartTime();

                                try {
                                    Date date2 = dateFormat.parse(strDbDate);

                                    calendar2.setTime(date2);


                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }


                                if(statustrip.compareTo(statusDb) == 0
                                        && calendar2.compareTo(calendar1) == 0
                                        && strCurTime.compareTo(strDbTime) == 0){


                                    Log.v("DatadbDate", "Good");



                                    final String startDes = trips.get(i).getTripDestination();

                                    final String EndDes = trips.get(i).getTripStation();
                                    final String Note = trips.get(i).getTripNote();



                                    Intent alert = new Intent(context,DialogServer.class);
                                    alert.putExtra("from",startDes);
                                    alert.putExtra("to",EndDes);
                                    alert.putExtra("note",Note);
                                    alert.setFlags(intent.FLAG_ACTIVITY_NEW_TASK);


                                    context.startActivity(alert);


                                }else{
                                    Log.v("DatadbDate", "not data");

                                }


                            }


                            //End condition


                        }
                    }


                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });






            }



        };





        handler.postDelayed(r, 10000);






    }




}
