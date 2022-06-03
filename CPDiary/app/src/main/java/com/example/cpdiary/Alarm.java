package com.example.cpdiary;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

public class Alarm extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        //you might want to check what's inside the Intent
        Log.d("hi", "onReceive: in alarm");
        createNotificationChannel(context);


            NotificationManager manager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
             String varia=intent.getStringExtra("varia");
             String text=intent.getStringExtra("contestName");
             if(varia.equals("1"))
                 text+='\n'+"Starts within 10 minutes";
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context,"CP_DAIRY")
                    .setSmallIcon(R.drawable.ic_action_name)
                    //example for large icon
                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher))
                    .setContentTitle("Upcoming contest")
                    .setContentText(text)
                    .setOngoing(false)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setAutoCancel(true);

        Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(intent.getStringExtra("contestUrl")));
        PendingIntent pendingIntent =
                    PendingIntent.getActivity(
                            context,
                            0,
                            myIntent,
                            PendingIntent.FLAG_ONE_SHOT
                    );
            // example for blinking LED
            builder.setLights(0xFFb71c1c, 1000, 2000);
            builder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM));
            builder.setContentIntent(pendingIntent);
            manager.notify(intent.getStringExtra("contestUrl").hashCode(), builder.build());
        }

    private void createNotificationChannel(Context context) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "CP_DAIRY";
            String description = "Get contest updates";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(name.toString(), name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

}