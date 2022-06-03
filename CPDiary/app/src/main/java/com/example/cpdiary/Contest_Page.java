package com.example.cpdiary;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;

import android.os.CountDownTimer;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.webkit.WebChromeClient;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.IOException;
import java.sql.Array;
import java.sql.SQLOutput;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

// sage function for setting up contest list through api.
// cypher function for transform the time into date/miniute/second.
public class Contest_Page extends AppCompatActivity{

    public ArrayList<TextView> contest_tag = new ArrayList<TextView>();
    public ArrayList<TextView> contest_time = new ArrayList<TextView>();
    public ArrayList<TextView> contest_link = new ArrayList<TextView>();
    public ArrayList<TextView> contest_site = new ArrayList<TextView>();
    public ArrayList<TextView> before_contest = new ArrayList<TextView>();
    public ArrayList<Integer> Thread_Id = new ArrayList<Integer>();
    //    public TextView first_contest_tag,second_contest_tag,third_contest_tag,fourth_contest_tag,fifth_contest_tag;
//    public TextView first_contest_time,second_contest_time,third_contest_time,fourth_contest_time,fifth_contest_time;
//    public TextView first_contest_link,second_contest_link,third_contest_link,fourth_contest_link,fifth_contest_link;
    public TextView handle;
    public Button handle_submission;
    String xd =" Register ";
    public String Channel_Id = "channel_id";
    private int sage_i=0,sage_j=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contest__page);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            NotificationChannel channel_one = new NotificationChannel(Channel_Id,"Channel One", NotificationManager.IMPORTANCE_HIGH);
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            manager.createNotificationChannel(channel_one);
        }
        //test comment here for git purpose
        contest_tag.add(findViewById(R.id.first_contest_tag));
        contest_tag.add(findViewById(R.id.second_contest_tag));
        contest_tag.add(findViewById(R.id.third_contest_tag));
        contest_tag.add(findViewById(R.id.fourth_contest_tag));
        contest_tag.add(findViewById(R.id.fifth_contest_tag));
        contest_tag.add(findViewById(R.id.six_contest_tag));
        contest_tag.add(findViewById(R.id.seven_contest_tag));
        contest_tag.add(findViewById(R.id.eight_contest_tag));
        contest_tag.add(findViewById(R.id.nine_contest_tag));
        contest_tag.add(findViewById(R.id.ten_contest_tag));
        contest_tag.add(findViewById(R.id.eleven_contest_tag));
        contest_tag.add(findViewById(R.id.tweleve_contest_tag));
        contest_tag.add(findViewById(R.id.thirteen_contest_tag));
        contest_tag.add(findViewById(R.id.fourteen_contest_tag));
        contest_tag.add(findViewById(R.id.fifteen_contest_tag));
        contest_time.add(findViewById(R.id.first_contest_time));
        contest_time.add(findViewById(R.id.second_contest_time));
        contest_time.add(findViewById(R.id.third_contest_time));
        contest_time.add(findViewById(R.id.fourth_contest_time));
        contest_time.add(findViewById(R.id.fifth_contest_time));
        contest_time.add(findViewById(R.id.six_contest_time));
        contest_time.add(findViewById(R.id.seven_contest_time));
        contest_time.add(findViewById(R.id.eight_contest_time));
        contest_time.add(findViewById(R.id.nine_contest_time));
        contest_time.add(findViewById(R.id.ten_contest_time));
        contest_time.add(findViewById(R.id.eleven_contest_time));
        contest_time.add(findViewById(R.id.tweleve_contest_time));
        contest_time.add(findViewById(R.id.thirteen_contest_time));
        contest_time.add(findViewById(R.id.fourteen_contest_time));
        contest_time.add(findViewById(R.id.fifteen_contest_time));
        contest_link.add(findViewById(R.id.first_contest_link));
        contest_link.add(findViewById(R.id.second_contest_link));
        contest_link.add(findViewById(R.id.third_contest_link));
        contest_link.add(findViewById(R.id.fourth_contest_link));
        contest_link.add(findViewById(R.id.fifth_contest_link));
        contest_link.add(findViewById(R.id.six_contest_link));
        contest_link.add(findViewById(R.id.seven_contest_link));
        contest_link.add(findViewById(R.id.eight_contest_link));
        contest_link.add(findViewById(R.id.nine_contest_link));
        contest_link.add(findViewById(R.id.ten_contest_link));
        contest_link.add(findViewById(R.id.eleven_contest_link));
        contest_link.add(findViewById(R.id.tweleve_contest_link));
        contest_link.add(findViewById(R.id.thirteen_contest_link));
        contest_link.add(findViewById(R.id.fourteen_contest_link));
        contest_link.add(findViewById(R.id.fifteen_contest_link));

        before_contest.add(findViewById(R.id.first_contest_before_contest));
        before_contest.add(findViewById(R.id.second_contest_before_contest));
        before_contest.add(findViewById(R.id.third_contest_before_contest));
        before_contest.add(findViewById(R.id.fourth_contest_before_contest));
        before_contest.add(findViewById(R.id.fifth_contest_before_contest));
        before_contest.add(findViewById(R.id.six_contest_before_contest));
        before_contest.add(findViewById(R.id.seven_contest_before_contest));
        before_contest.add(findViewById(R.id.eight_contest_before_contest));
        before_contest.add(findViewById(R.id.nine_contest_before_contest));
        before_contest.add(findViewById(R.id.ten_contest_before_contest));
        before_contest.add(findViewById(R.id.eleven_contest_before_contest));
        before_contest.add(findViewById(R.id.tweleve_contest_before_contest));
        before_contest.add(findViewById(R.id.thirteen_contest_before_contest));
        before_contest.add(findViewById(R.id.fourteen_contest_before_contest));
        before_contest.add(findViewById(R.id.fifteen_contest_before_contest));
        contest_site.add(findViewById(R.id.first_contest_site));
        contest_site.add(findViewById(R.id.second_contest_site));
        contest_site.add(findViewById(R.id.third_contest_site));
        contest_site.add(findViewById(R.id.fourth_contest_site));
        contest_site.add(findViewById(R.id.fifth_contest_site));
        contest_site.add(findViewById(R.id.six_contest_site));
        contest_site.add(findViewById(R.id.seven_contest_site));
        contest_site.add(findViewById(R.id.eight_contest_site));
        contest_site.add(findViewById(R.id.nine_contest_site));
        contest_site.add(findViewById(R.id.ten_contest_site));
        contest_site.add(findViewById(R.id.eleven_contest_site));
        contest_site.add(findViewById(R.id.tweleve_contest_site));
        contest_site.add(findViewById(R.id.thirteen_contest_site));
        contest_site.add(findViewById(R.id.fourteen_contest_site));
        contest_site.add(findViewById(R.id.fifteen_contest_site));

        sage_i=0;
        sage_j=0;
        sage();
    }
    public String cypher(String Time)
    {
        boolean ok = false;
        String ourDate = "";
        for(int i =0;i<Time.length();i++)
        {
            if(Time.charAt(i)=='T')
            {
                ok = true;
            }
        }
        if(ok)
        {
            for(int i = 0;i<Time.length();i++)
            {
                if(Time.charAt(i)=='T')ourDate += " ";
                else if (Time.charAt(i)=='.')break;
                else ourDate += Time.charAt(i);
            }
            ourDate += " UTC";
        }
        else
        {
            ourDate = Time;
        }
//        Log.d("mytag","cypher(ourDate)"+ourDate);
        try
        {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date value = formatter.parse(ourDate);
//            Log.d("mytag",""+value.toString());
            SimpleDateFormat dateFormatter = new SimpleDateFormat("h:mm a\nEEEE,MMMM d,yyyy", Locale.getDefault());
            dateFormatter.setTimeZone(TimeZone.getTimeZone("GMT+6"));
            ourDate = dateFormatter.format(value);

//            Log.d("mytag", ourDate);
        }
        catch (Exception e)
        {
            ourDate = "T.B.D";
        }
        return ourDate;
    }
    long cypher1(String tim)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-dd hh:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("0"));
        String dateString = tim;
        String date1="";
        for(int i=0;i<dateString.length();i++)
        {
            if(dateString.charAt(i)=='T') {
                date1 += " ";
            }
            else if(dateString.charAt(i)=='.')
                break;
            else
                date1+=dateString.charAt(i);
        }
        Log.d("mytag"," DATE : "+date1);
       Date date = new Date();
            // formatting the dateString to convert it into a Date
            try {
                date = sdf.parse(date1);

            }
            catch (Exception e)
            {

            }
       // Toast.makeText(Contest_Page.this,date.toString(), Toast.LENGTH_SHORT).show();
      //  Toast.makeText(Contest_Page.this,Long.toString(date.getTime()), Toast.LENGTH_SHORT).show();
        Log.d("mytag","date "+date+" "+date.getTime());
        return date.getTime();
    }
    public void sage()
    {
        RequestQueue queue = Volley.newRequestQueue(Contest_Page.this);
        String url = "https://kontests.net/api/v1/all";
        JsonArrayRequest jsonarrayresponse = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        try{
                            Log.d("mytag","STARTED");
                            for(sage_i=0,sage_j=0;sage_j<15 && sage_i<response.length();sage_i++)
                            {
//                                    Log.d("mytag","SAGE I is : "+sage_i);
                                JSONObject contest = response.getJSONObject(sage_i);
                                String status = contest.getString("status");
                                if(status.equals("BEFORE"))
                                {
//                                        Log.d("mytag",""+sage_j);
//                                        Log.d("mytag",""+contest.getString("name"));
                                    contest_tag.get(sage_j).setText(contest.getString("site"));
                                    contest_site.get(sage_j).setText(contest.getString("name"));
                                    contest_time.get(sage_j).setText(cypher(contest.getString("start_time")));
//                                    Log.d("mytag"," Original Time : "+cypher(contest.getString("start_time")));
                                    String value = "<html><a href=\""+contest.getString("url")+"\">"+xd+"</a></html>";
                                    contest_link.get(sage_j).setText(Html.fromHtml(value));
                                    contest_link.get(sage_j).setMovementMethod(LinkMovementMethod.getInstance());
                                    CountDown obj = new CountDown();
                                    obj.Alfeh(cypher1(contest.getString("start_time")),sage_j);
                                    sage_j++;
                                }
                            }
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("mytag","error_occured ");
                    }
                });
        queue.add(jsonarrayresponse);
//        for(int i=0;i<15;i++)
//        {
//            Pair<Long,Long> pp = remaining_time.get(i);
//            Log.d("mytag"," Contest ID : "+pp.second+" Time : "+pp.first);
//        }
    }
    class CountDown {
        String gar(long val)
        {
            if(val>9)
                return Long.toString(val);
            return "0"+Long.toString(val);
        }
        public void Alfeh(long diffInMsec,long id)
        {
            long id1=id;
            long curtimegap=(diffInMsec-System.currentTimeMillis());
            Log.d("mytag"," ID : "+id+" Remaining Time :"+curtimegap);
            long oomar = curtimegap/1000;
            long ohr=oomar/3600;
            long omt=(oomar%3600)/60;
            long ose=oomar%60;
            String otxt1="Starts in"+"\n";
            otxt1+=gar(ohr)+":"+gar(omt)+":"+gar(ose);
            Log.d("mytag",""+otxt1);
            long day1=curtimegap/86400000;
            if(day1>0){
                String txt1="Starts in"+"\n";
                txt1+=Long.toString(day1)+" days";
                before_contest.get((int)id1).setText(txt1);
            }
            else{
                new CountDownTimer(curtimegap,1000)
                {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        long omar = millisUntilFinished/1000;
                        long hr=omar/3600;
                        long mt=(omar%3600)/60;
                        long se=omar%60;
                        String txt1="Starts in"+"\n";
                        txt1+=gar(hr)+":"+gar(mt)+":"+gar(se);
                        before_contest.get((int)id1).setText(txt1);
//                        Log.d("mytag","COUNTER : 0");
                    }
                    @Override
                    public void onFinish() {
                        Log.d("mytag","FINISHED");
                    }
                }.start();
            }
        }
    }
}
