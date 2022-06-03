package com.example.cpdiary;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AutoStart extends BroadcastReceiver
{
    Context context;
    @Override
    public void onReceive(Context context, Intent intent)
    {
        this.context=context;
        //if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED))
       // {
         ekhon_kor();
       // }
    }
    void ekhon_kor()
    {

        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(context, ContestFetcher.class);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, PendingIntent.FLAG_NO_CREATE);
        Log.d("hi", "contestFetcher: "+pi);
        // if (pi == null) {
        Log.d("hi", "contestFetcher: alarm set");
        pi=PendingIntent.getBroadcast(context, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
        Log.d("hi", "contestFetcher: "+pi);
        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+2000, 12*60*60*1000, pi); // Millisec * Second * Minute
        // }
    }
}