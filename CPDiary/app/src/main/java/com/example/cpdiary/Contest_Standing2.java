package com.example.cpdiary;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Contest_Standing2 extends AppCompatActivity {
    private  int tot=0,lstid=0,lastquerydone=0,st=1,cur_try=0;
    private String ccountry="Bangladesh",contestid="1606",unofficial="false";
    private TableLayout tl;
    private Context context;
    private TableRow initrow;
    private List<Contestant> contestants;
    private boolean isfinished;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contest_standing2);
        tl = findViewById(R.id.table);
        context=Contest_Standing2.this;
        isfinished=false;
        initrow = new TableRow(context);
        Intent intent =getIntent();
        contestants = new ArrayList<>();
        // Log.d("hi","find_handle_list_from_standing"+fntcnt);
        Bundle bundle=intent.getExtras();
        if(bundle!=null)
        {
            contestid=bundle.getString("id");
            ccountry=bundle.getString("country");
            unofficial=bundle.getString("unofficial");
        }
        standing_from();
    }
    public boolean isRunning(Context ctx) {
        ActivityManager activityManager = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = activityManager.getRunningTasks(Integer.MAX_VALUE);

        for (ActivityManager.RunningTaskInfo task : tasks) {
            if (ctx.getPackageName().equalsIgnoreCase(task.baseActivity.getPackageName()))
                return true;
        }

        return false;
    }
    // Creating the standing list
    void standing_from() {
        if(isRunning(context)==false)
            return;
//          Log.d("hi","find_handle_list_from_standing"+fntcnt);
        int bag=10000;
        String url = "https://www.codeforces.com/api/contest.standings?contestId="+contestid+"&from="+st+"&count="+bag+"&showUnofficial="+unofficial;
        solve_api_request api_request = new solve_api_request(context);
        api_request.api_request(url, new solve_api_request.VolleyResponseListener() {
            @Override
            public void onError(String message) {
                standing_from();
                Log.d("hi","api wrong in standing call"+st);
                if(MainActivity.is_developer_mode_on==1)
                    Toast.makeText(context,"api wrong in standing call"+st,Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onResponse(JSONObject object) {
                try {
                    String status=object.getString("status");
                    if(status.equals("OK"))
                    {
                        JSONObject result = object.getJSONObject("result");
                        JSONObject contest = result.getJSONObject("contest");
                        JSONArray problems = result.getJSONArray("problems");
                        int len= problems.length();
                        if(st==1)
                        {
                            initrow.setLayoutParams(getLayoutParams());
                            //  Log.d("hi","debug 2"+fntcnt);
                            initrow.addView(getTextView(context, 0, "Rank", Color.BLUE, Typeface.BOLD, Color.WHITE));
                            //  Log.d("hi","debug 3"+fntcnt);
                            initrow.addView(getTextView(context, 0, "Name", Color.BLUE, Typeface.BOLD, Color.WHITE));
                            //  Log.d("hi","debug 4"+fntcnt);
                            initrow.addView(getTextView(context, 0, "Expected Rank", Color.BLUE, Typeface.BOLD, Color.WHITE));
                            //  Log.d("hi","debug 5"+fntcnt);
                            initrow.addView(getTextView(context, 0, "Points", Color.BLUE, Typeface.BOLD, Color.WHITE));
                            for(int i=0;i<len;i++)
                            {
                                JSONObject a=problems.getJSONObject(i);
                                String b=a.getString("index");
                                initrow.addView(getTextView(context,0, b, Color.BLUE, Typeface.BOLD, Color.WHITE));
                            }
                        }
                        JSONArray rows = result.getJSONArray("rows");
                        len=rows.length();
                        for(int i=0;i<len;i++)
                        {
                            JSONObject a = rows.getJSONObject(i);
                            JSONObject party = a.getJSONObject("party");
                            JSONArray members = party.getJSONArray("members");
                            JSONObject member=members.getJSONObject(0);
                            String name = member.getString("handle");
                            int rank = a.getInt("rank");
                            String totalpoints = a.getString("points");
                            JSONArray problemresults = a.getJSONArray("problemResults");
                            int len1 = problemresults.length();
                            ArrayList<String> points = new ArrayList<String>();
                            for (int j = 0; j < len1; j++) {
                                JSONObject b = problemresults.getJSONObject(j);
                                String point = b.getString("points");
                                points.add(point);
                            }
                            contestants.add(new Contestant(0,0,"All", rank, name, points, totalpoints));
                        }
                        if(len>0)
                        {
                            st+=10000;
                            standing_from();
                        }
                        else{
                            find_country_list_from_handle_list(0);
                        }
                    }
                    else{
                        Log.d("hi","status is not OK in standing api call"+st);
                        if(MainActivity.is_developer_mode_on==1)
                            Toast.makeText(context,"status is not OK in standing api call"+st,Toast.LENGTH_SHORT).show();
                        standing_from();
                    }
                } catch (JSONException e) {
                    Log.d("hi",e.toString());
                    Toast.makeText(context,e.toString(),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    void find_country_list_from_handle_list(int cur_ind)
    {
        if(isRunning(context)==false)
            return;
        int contestants_len=contestants.size();
        Log.d("hi","debug "+cur_ind+" "+contestants_len);
        if(MainActivity.is_developer_mode_on==1)
            Toast.makeText(context,"debug "+cur_ind+" "+contestants_len,Toast.LENGTH_SHORT).show();
        String url = "https://www.codeforces.com/api/user.info?handles=";
        int cur_sz=0;
        int cur_string_len=url.length();
        for(int i=cur_ind;i<contestants_len;i++)
        {
            if(i>cur_ind)
                url+=";";
            int name_len=contestants.get(i).name.length();
            if(cur_string_len+name_len>=7000)
                break;
            cur_string_len+=name_len;
            if(contestants.get(i).name.equals("tianbu"))
                url+="feecIe6418";
            else if(contestants.get(i).name.equals("NTU_Rev"))
                url+="tataredmi";
            else if(contestants.get(i).name.equals("znstz_qaq"))
                url+="znstz2018";
            else
                url+=contestants.get(i).name;
            cur_sz++;
        }
        int url_len=url.length();
        //Log.d("hi","debug 55");
        if(cur_sz==0 || cur_try>5)
        {
            add_all_to_table();
            return;
        }
        else {
            solve_api_request api_request = new solve_api_request(context);
            api_request.api_request(url, new solve_api_request.VolleyResponseListener() {
                @Override
                public void onError(String message) {

                    Log.d("hi", "Error in name api call"+cur_ind+" "+contestants_len+" "+url_len);
                    if(MainActivity.is_developer_mode_on==1)
                        Toast.makeText(context,"Error in name api call"+cur_ind+" "+contestants_len+" "+url_len,Toast.LENGTH_SHORT).show();
                    cur_try++;
                    find_country_list_from_handle_list(cur_ind);
                }
                @Override
                public void onResponse(JSONObject object) {
                    cur_try=0;
                    try {
                        if (object.getString("status").equals("OK")) {
                            JSONArray result = object.getJSONArray("result");
                            int len = result.length();
                            for (int i = 0; i < len; i++) {
                                JSONObject cur = result.getJSONObject(i);
                                String country = "nul5l";
                                if (cur.has("country"))
                                    country = cur.getString("country");
                                int previusrating = 1500;
                                if (cur.has("rating"))
                                    previusrating = cur.getInt("rating");
                                Contestant contestant = contestants.get(cur_ind + i);
                                contestant.country = country;
                                contestant.previusrating = previusrating;
                                contestants.set(cur_ind + i, contestant);
                            }
                            find_country_list_from_handle_list(cur_ind+len);
                        }
                        else{
                            Log.d("hi","status is not ok in name api call"+cur_ind+" "+contestants_len);
                            if(MainActivity.is_developer_mode_on==1)
                                Toast.makeText(context,"status is not ok in name api call"+cur_ind+" "+contestants_len,Toast.LENGTH_SHORT).show();
                            find_country_list_from_handle_list(cur_ind);
                        }
                    }catch(JSONException e){
                        Log.d("hi", e.toString());
                        if(MainActivity.is_developer_mode_on==1)
                            Toast.makeText(context,"problem in try/catch",Toast.LENGTH_SHORT).show();
                    }
                }

            });
        }
    }
    void add_all_to_table()
    {
        Log.d("hi","now in upore");
        if(MainActivity.is_developer_mode_on==1)
            Toast.makeText(context,"now in add_all_to_table",Toast.LENGTH_SHORT).show();
        if(unofficial.equals("false"))
            process(contestants);
        lastquerydone=contestants.size();
        Log.d("hi","now in ans"+lastquerydone);
        if(MainActivity.is_developer_mode_on==1)
            Toast.makeText(context,"now in ans"+lastquerydone,Toast.LENGTH_SHORT).show();
        tl.addView(initrow,getTblLayoutParams());
        int total_add=0;
        for(int i=0;i<lastquerydone && total_add<600;i++)
        {
              Log.d("hi","arr"+i+"  "+i);
            Contestant currentcontesttant=contestants.get(i);
            if(!currentcontesttant.country.equals(ccountry) && !ccountry.equals("All"))
                continue;
            tot++;
            total_add++;
                Log.d("hi","arr"+i+"   ");
            TableRow tr1 = new TableRow(context);
            tr1.setLayoutParams(getLayoutParams());
            Integer rank = new Integer(currentcontesttant.rank);
            Integer a123= new Integer(lstid);
               Log.d("hi","arr3"+i);
            if(!a123.toString().equals(rank.toString()))
                lstid=tot;
            a123=lstid;
             Log.d("hi","debug 4"+i);
            tr1.addView(getTextView(context,0,a123.toString()+"("+rank.toString()+")",Color.BLUE,Typeface.BOLD,Color.WHITE));
              Log.d("hi","debug 5"+i);
            String name = currentcontesttant.name;
            int col=Color.BLUE;
            if(currentcontesttant.previusrating<1200)
                col=Color.parseColor("#80808080");
            else if(currentcontesttant.previusrating<1400)
                col=Color.parseColor("008000");
            else if(currentcontesttant.previusrating<1600)
                col=Color.parseColor("03A89E");
            else if(currentcontesttant.previusrating<1900)
                col=Color.parseColor("#0000FF");
            else if(currentcontesttant.previusrating<2100)
                col=Color.parseColor("#AA00AA");
            else if(currentcontesttant.previusrating<2400)
                col=Color.parseColor("#FF8C00");
            else
                col=Color.parseColor("#FF0000");
            tr1.addView(getTextView(context,1, name, col, Typeface.BOLD, Color.WHITE));
            Double se=new Double(currentcontesttant.seed);
            int a1=se.intValue();
            Integer deltas=new Integer(a1);
            tr1.addView(getTextView(context,0, deltas.toString(), Color.BLUE, Typeface.BOLD, Color.WHITE));
            String points = currentcontesttant.totalpoints;
            //  Log.d("hi","debug 6"+i);
            tr1.addView(getTextView(context,0, points, Color.BLUE, Typeface.BOLD, Color.WHITE));
            int len1=currentcontesttant.points.size();
            //  Log.d("hi","debug 4");
            for(int j=0;j<len1;j++)
            {
                String point = currentcontesttant.points.get(j);
                tr1.addView(getTextView(context,0, point, Color.BLUE, Typeface.BOLD, Color.WHITE));
            }
               Log.d("hi","arr"+i);
            tl.addView(tr1,getTblLayoutParams());
              Log.d("hi","brr"+i);
        }
    }
    TextView getTextView(Context context, int id, String title, int color, int typeface, int bgColor) {
        TextView tv = new TextView(context);
        tv.setId(id);
        tv.setText(title.toUpperCase());
        tv.setTextColor(color);
        tv.setPadding(40, 40, 40, 40);
        tv.setTypeface(Typeface.DEFAULT, typeface);
        tv.setBackgroundColor(bgColor);
        tv.setLayoutParams(getLayoutParams());
        if(id==1) {
            try {
                tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d("hi",title);
                        Toast.makeText(context,title,Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(context,Contest_Standing3.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("handle",title);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });
            }
            catch (Exception e)
            {
                Log.d("hi",e.toString());
                if(MainActivity.is_developer_mode_on==1)
                    Toast.makeText(context,"Error in handle api call,try/atch",Toast.LENGTH_SHORT).show();
            }
        }
        return tv;
    }
    @NonNull
    LayoutParams getLayoutParams() {
        LayoutParams params = new LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);
        params.setMargins(2, 0, 0, 2);
        return params;
    }
    @NonNull
    TableLayout.LayoutParams getTblLayoutParams() {
        return new TableLayout.LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);
    }
    private void process(List<Contestant> contestants) {
        if (contestants.isEmpty()) {
            return;
        }
        for (Contestant a : contestants) {
            a.seed = 1;
            // Log.d("hi",a.name);
            for (Contestant b : contestants) {
                if (a != b) {
                    a.seed += (1.0 / (1 + Math.pow(10, (a.previusrating - b.previusrating) / 400.0)));
                }
            }
        }
    }
    private static final class Contestant {
        private int serialnum;
        private String name;
        private String country;
        private int rank;
        private ArrayList<String> points;
        private int previusrating;
        private String totalpoints;
        private int needRating;
        private double seed;
        private int delta=0;

        private Contestant(int serialnum,int previusrating,String country,int rank,String name,ArrayList<String> points,String totalpoints) {
            this.serialnum=serialnum;
            this.previusrating=previusrating;
            this.country=country;
            this.rank=rank;
            this.name=name;
            this.points=points;
            this.totalpoints=totalpoints;
        }
    }
    @Override
    public void onBackPressed() {
        finish();
    }
}