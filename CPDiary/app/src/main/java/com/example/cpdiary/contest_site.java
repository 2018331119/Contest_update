package com.example.cpdiary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class contest_site extends AppCompatActivity {
    private Context context;
    private List<Contests>contests;
    private ScrollView sv;
    private  LinearLayout ll;
    private  CheckBox cb;
    private SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_contest_site);
        context=contest_site.this;
         sv = new ScrollView(this);
         ll = new LinearLayout(this);
         ll.setOrientation(LinearLayout.VERTICAL);
         sv.addView(ll);
        sp=getSharedPreferences("Login", MODE_PRIVATE);
         contests = new ArrayList<>();
         solve_api_request();
         this.setContentView(sv);
    }
    void solve_api_request()
    {
        String url="https://kontests.net/api/v1/sites";
        solve_api_request api_request = new solve_api_request(context);
        api_request.api_request(url, new solve_api_request.VolleyResponseListener1() {
            @Override
            public void onError(String message) {

            }

            @Override
            public void onResponse(JSONArray array) {
             int len=array.length();
             for(int i=0;i<len;i++) {
                 try {
                     JSONArray cur = array.getJSONArray(i);
                     String heading_name = cur.getString(0);
                     String lower_name = cur.getString(1);
                     String url = cur.getString(2);
                     contests.add(new Contests( url,heading_name,lower_name,"false"));
                 }
                 catch (Exception e)
                 {
                     Toast.makeText(context, "Problem", Toast.LENGTH_SHORT).show();
                 }
             }
             add_in_checkbox();
            }
        });
    }
    void add_in_checkbox()
    {
        for(int i=0;i<contests.size();i++) {
            Contests cur=contests.get(i);
            CheckBox cb = new CheckBox(this);
            String as=sp.getString(cur.heading_name,"false");
             Log.d("hi",as+" "+i);
             if(as.equals("true"))
               cb.setChecked(true);
            cb.setText(cur.heading_name);
            cb.setId(i);
            cb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(cb.isChecked())
                    {
                        cur.chk="true";
                    }
                    else
                        cur.chk="false";
                    Log.d("hi",cur.heading_name);
                    SharedPreferences.Editor Ed=sp.edit();
                    Ed.putString(cur.heading_name,cur.chk);
                    Ed.commit();
                }
            });
            contests.set(i,cur);
            ll.addView(cb);
        }
    }
    private static final class Contests{
        String url;
        String heading_name;
        String lower_name;
        String chk;
        private Contests(String url,String heading_name,String lower_name,String chk)
        {
            this.url=url;
            this.heading_name=heading_name;
            this.lower_name=lower_name;
            this.chk=chk;
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}