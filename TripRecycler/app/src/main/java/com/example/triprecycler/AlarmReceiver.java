package com.example.triprecycler;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by rizk on 05/04/18.
 */

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.

        String from = intent.getStringExtra("from");
        String to = intent.getStringExtra("to");
        String note=intent.getStringExtra("note");
        Intent secIntent = new Intent(context, AlertDaialog.class);
        secIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        secIntent.putExtra("from", from);
        secIntent.putExtra("to", to);
        secIntent.putExtra("note",note);
        context.startActivity(secIntent);

    }
}
