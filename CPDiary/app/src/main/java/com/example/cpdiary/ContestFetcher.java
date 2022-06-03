package com.example.cpdiary;

import static android.content.Context.MODE_PRIVATE;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class ContestFetcher extends BroadcastReceiver {
    Context context;

    void solve_next_kaj()
    {
        Log.d("hi","hello");
        String url="https://kontests.net/api/v1/all";
        solve_api_request api_request = new solve_api_request(context);
        api_request.api_request(url, new solve_api_request.VolleyResponseListener1() {
            @Override
            public void onError(String message) {

            }

            @Override
            public void onResponse(JSONArray array) {
              int len=array.length();
              try
              {
                  Log.d("hi","hello1");
                  SharedPreferences sp1=context.getSharedPreferences("Login",MODE_PRIVATE);
                  for(int i=0;i<len;i++)
                  {
                      JSONObject cur=array.getJSONObject(i);
                      if(cur.getString("in_24_hours").equals("No"))
                          continue;
                      if(cur.getString("status").equals("CODING"))
                          continue;
                      if(sp1.getString(cur.getString("site"),"false").equals("false"))
                          continue;
                      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-dd hh:mm:ss");
                      sdf.setTimeZone(TimeZone.getTimeZone("0"));
                      String dateString = cur.getString("start_time");
                      String date1="";
                      Long timeInMilli1= Long.valueOf(0);
                      for(int ii=0;ii<dateString.length();ii++)
                      {
                          if(dateString.charAt(ii)=='T')
                              date1+=" ";
                          else if(dateString.charAt(ii)=='.')
                              break;
                          else
                              date1+=dateString.charAt(ii);
                      }
                      try{
                          Date date = sdf.parse(date1);
                          timeInMilli1=Math.max(date.getTime()-10*60*1000,System.currentTimeMillis());
                      }catch(Exception e){
                          e.printStackTrace();
                      }
                      Long timeInMilli = System.currentTimeMillis()+2*1000;
                      for(int ii=0;ii<2;ii++) {
                          AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                          Intent intent = new Intent(context, Alarm.class);
                          intent.setType(cur.getString("url")+ii);
                          String varia="0";
                          long ttt=timeInMilli;
                          if(ii==1) {
                              ttt = timeInMilli1;
                              varia = "1";
                          }
                          intent.putExtra("contestName", cur.getString("name"));
                          intent.putExtra("contestUrl", cur.getString("url"));
                          intent.putExtra("varia",varia);
                          PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                          am.set(AlarmManager.RTC_WAKEUP,ttt, pendingIntent);
                          Log.d("hi", "onResponse: notification set");
                      }
                  }
              }
              catch (Exception e)
              {

              }
            }
        });
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context=context;
        solve_next_kaj();
    }
}
