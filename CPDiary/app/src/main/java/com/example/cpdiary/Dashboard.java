package com.example.cpdiary;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Dashboard extends AppCompatActivity implements View.OnClickListener{
    private TextView nameview;
    Button contest_button,standing_button,tracker_button,resource_button,logout_button,remaindar_button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        contest_button = findViewById(R.id.contest_button);
        standing_button = findViewById(R.id.standing_button);
        tracker_button = findViewById(R.id.contest_tracker);

        logout_button = findViewById(R.id.logout);
        remaindar_button = findViewById(R.id.remaindar_button);
        contest_button.setOnClickListener(this);
        standing_button.setOnClickListener(this);
        tracker_button.setOnClickListener(this);
        logout_button.setOnClickListener(this);
        remaindar_button.setOnClickListener(this);
        Log.d("hi","now in dashboard");
        if(MainActivity.is_developer_mode_on==1)
            Toast.makeText(Dashboard.this,"Now in dashboard",Toast.LENGTH_SHORT).show();

     //   contest_site2 cont = new contest_site2(Dashboard.this);
      //  cont.solve_next_kaj();
        Intent i = new Intent(this.getBaseContext(), AutoStart.class);
        AutoStart autoStart = new AutoStart();
        autoStart.onReceive(Dashboard.this,i);
        //contestFetcher();
    }
    private void contestFetcher() {
        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(this.getBaseContext(), AutoStart.class);
        PendingIntent pi = PendingIntent.getBroadcast(getApplicationContext(), 0, i, PendingIntent.FLAG_NO_CREATE);
        Log.d("hi", "contestFetcher: "+pi);
        if (pi == null) {
            Log.d("hi", "contestFetcher: alarm set");
            pi=PendingIntent.getBroadcast(getApplicationContext(), 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
            Log.d("hi", "contestFetcher: "+pi);
            am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+2000, 12*60*60*1000, pi); // Millisec * Second * Minute
        }
    }
    @Override
    public void onClick(View v) {
        if(v==findViewById(R.id.contest_button))
        {
            Intent intent = new Intent(Dashboard.this,Contest_Page.class);
            startActivity(intent);
        }
        else if(v==findViewById(R.id.standing_button))
        {
            Intent intent = new Intent(Dashboard.this,Contest_Standing.class);
            startActivity(intent);
        }
        else if(v==findViewById(R.id.contest_tracker))
        {
            Intent intent = new Intent(Dashboard.this,Friends.class);
            startActivity(intent);
        }
        else if(v==findViewById(R.id.logout))
        {
            SharedPreferences sp1=this.getSharedPreferences("Login",MODE_PRIVATE);
            sp1.edit().clear().commit();
            finish();
            Intent intent = new Intent(Dashboard.this,MainActivity.class);
            startActivity(intent);
        }
        else if(v==findViewById(R.id.remaindar_button))
        {
            Intent intent = new Intent(Dashboard.this,contest_site.class);
            startActivity(intent);
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}