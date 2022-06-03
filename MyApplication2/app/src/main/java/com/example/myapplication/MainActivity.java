package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.Settings;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public DatabaseReference serverdata;
    public String user_id="",user_email="",sb,bd,rp,admin,contestid;
    public int count = 0,previous = 0,preiousrecondirt=0;
    public GMailSender sender;
    public ArrayList< Pair<String, Pair<String,String> > >vv;
    public TextView ContestId,AdminEmail,AdminEmailPassword;
    public Button Submit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("mytag"," Debug 1 ");
        Submit = findViewById(R.id.Submit);
        ContestId = findViewById(R.id.ContestId);
        AdminEmail = findViewById(R.id.AdminEmail);
        AdminEmailPassword = findViewById(R.id.AdminEmailPassword);
        Log.d("mytag"," ID1 : "+Submit.getId());
        Submit.setOnClickListener(this);
        vv = new ArrayList< Pair<String, Pair<String,String> > >();
    }
    public void sova(String handle)
    {
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        String url = "https://codeforces.com/api/user.status?handle="+handle+"&from=1&count=20";
//        Log.d("mytag",""+url);
        JsonObjectRequest jsonobjectresponse = new JsonObjectRequest
                (Request.Method.GET, url, null, new com.android.volley.Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            String status = response.getString("status");
                            if(status.equals("OK"))
                            {
                                JSONArray submission_list = response.getJSONArray("result");
                                for(int i=0;i<submission_list.length();i++)
                                {
                                    JSONObject submission = submission_list.getJSONObject(i);
                                    JSONObject problem = submission.getJSONObject("problem");
                                    String contest_id = problem.getString("contestId");
                                    String problem_id = problem.getString("index");
                                    String problem_name = problem.getString("name");
                                    String verdict = submission.getString("verdict");
                                    JSONObject author = submission.getJSONObject("author");
                                    String participant_type = author.getString("participantType");
                                    Log.d("mytag","PT: "+participant_type+"\nVerdict: "+verdict);
                                    if(participant_type.equals("CONTESTANT")&&verdict.equals("OK")&&contest_id.equals(contestid))
                                    {
                                        Log.d("mytag",""+handle+" Solved problem "+contest_id+problem_id+" name : "+problem_name);
                                        sb = handle+" just solved a problem.\nCheck out more information in the attached email.";
                                        bd = ""+handle+" Solved problem "+contest_id+problem_id+" name : "+problem_name;
                                        rp = user_email;
                                        count++;
                                        Pair<String,String> chamber= new Pair<String,String>(bd,rp);
                                        Pair<String,Pair<String,String>> fft = new Pair<String,Pair<String,String>>(sb,chamber);
                                        vv.add(fft);
//                                        Log.d("mytag","BD : "+count);
                                        while(previous<count)
                                        {
//                                            Log.d("mytag"," PREVIOUS 1 : "+previous);
                                            ++previous;
                                            new recon_dirt().execute();
//                                            Log.d("mytag"," PREVIOUS 2 : "+previous);
                                        }
                                    }
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
                        Log.d("mytag","error_occured");
                    }
                });
        queue.add(jsonobjectresponse);
        Log.d("mytag",""+queue.getSequenceNumber());
    }

    @Override
    public void onClick(View v) {
        Log.d("mytag"," Id : "+v.getId());
        if(v==findViewById(R.id.Submit))
        {
            Log.d("mytag","OnClick");
            contestid = ContestId.getText().toString();
            Log.d("mytag","Contest Id : "+contestid);
            admin = AdminEmail.getText().toString();
            sender = new GMailSender(admin, AdminEmailPassword.getText().toString());
            serverdata = FirebaseDatabase.getInstance().getReference();
            serverdata.child("Email").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (task.isSuccessful()) {
                        String User_id_with_email = String.valueOf(task.getResult().getValue());
                        boolean ok = false;
                        for (int i = 1; i < User_id_with_email.length() - 1; i++) {
                            if (User_id_with_email.charAt(i) == '=') {
                                ok = true;
                            } else {
                                if (!ok) {
                                    user_id = user_id + User_id_with_email.charAt(i);
                                } else
                                    user_email = user_email + User_id_with_email.charAt(i);
                            }
                        }
                        Log.d("mytag", user_id);
                        Log.d("mytag", user_email);
                    }
                }
            });
            serverdata.child("Users").child(user_id).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    for (DataSnapshot ds : task.getResult().getChildren()) {
                        String chamber = String.valueOf(ds.getValue());
                        Log.d("mytag","CHAMBER : "+chamber);
                        String handle = "";
                        boolean ok = false;
                        for (int i = 0; i < chamber.length() - 1; i++) {
                            Log.d("mytag",""+chamber.charAt(i));
                            if (chamber.charAt(i) == '=') ok = true;
                            else if (chamber.charAt(i) != ',' && ok == true) {
                                handle += chamber.charAt(i);
                            } else {
                                if (ok) {
                                    Log.d("mytag",handle);
                                    count = 0;
                                    previous = 0;
                                    preiousrecondirt = 0;
                                    sova(handle);
                                    handle = "";
                                }
                                ok = false;
                            }
                        }
                        Log.d("mytag",handle);
                        sova(handle);
                    }
                }
            });
        }
    }

    class recon_dirt extends AsyncTask<Void, Void, Void> {

        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.show();
        }

        @Override

        protected Void doInBackground(Void... mApi) {
            try {
                while (preiousrecondirt<previous)
                {
                    Pair< String, Pair< String, String> > temp = vv.get(preiousrecondirt);
                    Pair<String,String> fft = temp.second;
                    sender.sendMail(temp.first, fft.first, admin, fft.second);
                    ++preiousrecondirt;
                }
                Log.d("send", "done");
            }
            catch (Exception ex) {
                Log.d("exceptionsending", ex.toString());
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            pDialog.cancel();
            Toast.makeText(MainActivity.this, "mail send", Toast.LENGTH_SHORT).show();
        }
    }
}
