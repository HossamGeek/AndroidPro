package com.example.triprecycler;

import android.app.PendingIntent;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class AlertDaialog extends AppCompatActivity {

    TextView secInfo;
    Button btnStop;
    Button btnstart;
    Button btnclose;
    MediaPlayer ringTone;
    TextView noteView;
    static  int alarmID = 1;
    NotificationManagerCompat notificationManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert_daialog);

        Intent notifiIntent=new Intent(this,AlertDaialog.class);
        final PendingIntent pendingIntent_notific=PendingIntent.getActivity(this,1,notifiIntent,PendingIntent.FLAG_UPDATE_CURRENT);

        btnStop = (Button) findViewById(R.id.stop);
        btnclose = (Button) findViewById(R.id.close);
        btnstart = (Button) findViewById(R.id.reset);
        noteView = findViewById(R.id.noteView);
        final String from = getIntent().getStringExtra("from");
        final String to = getIntent().getStringExtra("to");
        final String note = getIntent().getStringExtra("note");
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.x = -20;
        params.y = -10;
        noteView.setText(note);
        this.getWindow().setAttributes(params);
        ringTone = new MediaPlayer();
        ringTone = MediaPlayer.create(this, R.raw.alaram);

        ringTone.setAudioStreamType(AudioManager.STREAM_MUSIC);
        ringTone.setLooping(false);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        ringTone.start();


        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ringTone != null) {
                    ringTone.stop();
                    ringTone = null;
                }

                NotificationCompat.Builder notifyBuilder= new NotificationCompat.Builder(AlertDaialog.this,"Channel")

                        .setContentTitle("You Have a Trip")
                        .setSmallIcon(R.drawable.common_google_signin_btn_icon_light_normal)
                        .setContentIntent(pendingIntent_notific)
                        .setAutoCancel(false);

                notificationManager= NotificationManagerCompat.from(AlertDaialog.this);
                notifyBuilder.setOngoing(true);
                notificationManager.notify(1,notifyBuilder.build());

                finish();
            }
        });

        btnclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)

            {
                if (ringTone != null) {
                    ringTone.stop();
                    ringTone = null;
                }
                NotificationManagerCompat.from(AlertDaialog.this).cancel(alarmID);
                finish();
            }
        });


        btnstart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                 if (ringTone != null) {
                ringTone.stop();
                ringTone = null;
            }
                String uri = "http://maps.google.com/maps?saddr=" + from + "&daddr=" + to;
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                intent.setPackage("com.google.android.apps.maps");
                startActivity(intent);
            }
        });


    }

}