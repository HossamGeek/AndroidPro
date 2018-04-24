package com.example.triprecycler;

import android.app.Dialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.database.DatabaseReference;

/**
 * Created by Hossa on 4/12/2018.
 */

public class DialogServer extends AppCompatActivity {

    DatabaseReference reference;
    public  static  final String MY_PREFS_NAME="File";
    MediaPlayer ringTone;


    Intent incomingIntent;


    Dialog myDialog;
    Button later,start,close;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alert_dialog);

        this.setFinishOnTouchOutside(false);
        later = findViewById(R.id.later);
        start=findViewById(R.id.start);
        close=findViewById(R.id.close);


        ringTone = new MediaPlayer();

        ringTone = MediaPlayer.create(this, R.raw.alaram);
        ringTone.setAudioStreamType(AudioManager.STREAM_MUSIC);
        ringTone.setLooping(false);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        ringTone.start();


        later.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CountTime();
                if (ringTone != null) {
                    ringTone.stop();
                    ringTone = null;
                }
                finish();
            }
        });

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                myDialog.cancel();
                viewPath();
                if (ringTone != null) {
                    ringTone.stop();
                    ringTone = null;
                }
                finish();

            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ringTone != null) {
                    ringTone.stop();
                    ringTone = null;
                }
                finish();
            }
        });



    }






    NotificationManager manager;
    Uri alarm = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
    static  int id =1;

    private void sendPop()
    {

        NotificationCompat.Builder nbuild = new  NotificationCompat.Builder(this).
                setContentTitle("Trip").setContentText("the trip finished ").setSmallIcon(R.mipmap.ic_launcher);

        Intent res = new Intent(this,DialogServer.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);


        stackBuilder.addParentStack(DialogServer.class);

        stackBuilder.addNextIntent(res);

        PendingIntent respe = stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);

        nbuild.setContentIntent(respe);


        nbuild.addAction(0,null,respe);

        nbuild.setAutoCancel(true);


        nbuild.setOngoing(true);
        nbuild.setSound(alarm);
        manager= (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        manager.notify(id,nbuild.build());
        //manager.cancel(id);


    }

    private void viewPath(){


        incomingIntent = getIntent();

        String firstTrip = incomingIntent.getStringExtra("from");
        String endTrip  =incomingIntent.getStringExtra("to");

        String uri = "http://maps.google.com/maps?saddr="+firstTrip+"&daddr="+endTrip+"";
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        intent.setPackage("com.google.android.apps.maps");
        startActivity(intent);
    }

    //counter
    private void CountTime(){


        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //   myDialog.dismiss();
                sendPop();

                //Do something after 100ms
            }
        }, 5000);


    }









}
