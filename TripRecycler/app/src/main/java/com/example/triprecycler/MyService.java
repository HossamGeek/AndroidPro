package com.example.triprecycler;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.RequiresApi;

public class MyService extends Service {
    public static final String MY_PREFS_NAME = "File";

   Intent coming;
    public MyService() {
    }

    @RequiresApi(api = Build.VERSION_CODES.N)


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return super.onStartCommand(intent, flags, startId);

    }
        @Override
    public void onCreate() {
        super.onCreate();

        SharedPreferences sharedPreferences =  getSharedPreferences(MY_PREFS_NAME, 0);
        final String emailUser = sharedPreferences.getString("email","");

        //coming = Intent.getIntent();
//        final String EmailUser = coming.getStringExtra("email");

        Intent brs = new Intent(MyService.this, BroadCasts.class);
        brs.putExtra("email",emailUser);
        sendBroadcast(brs);


    }
    }
