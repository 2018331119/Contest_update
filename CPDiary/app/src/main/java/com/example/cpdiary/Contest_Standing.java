package com.example.cpdiary;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Contest_Standing extends AppCompatActivity {
    private  int tot=0,lstid=0,fntcnt=0;
    private String ccountry="Bangladesh",selectText="Bangladesh";
    private TableLayout tl;
    private Context context;
    private EditText editText;
    private List<String> mLines;
    private SearchableSpinner dropdown;
    private CheckBox unofficial;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contest_standing);
        tl = findViewById(R.id.table);
        context=Contest_Standing.this;
        // editText=findViewById(R.id.editText);
        dropdown = findViewById(R.id.spinner);
        unofficial=findViewById(R.id.unofficial);
//create a list of items for the spinner.
        mLines = new ArrayList<>();
        dropdown.setTitle("Select a country");
        AssetManager am = Contest_Standing.this.getAssets();
        try {
            InputStream is = am.open("country_list.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = reader.readLine()) != null)
                mLines.add(line);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String[] items = new String[172];
        for(int i=0;i<172;i++)
            items[i]=mLines.get(i);
//create an adapter to describe how the items are displayed, adapters are used in several places in android.
//There are multiple variations of this, but this is the basic variant.
        ArrayAdapter<String> adapter = new ArrayAdapter<>(Contest_Standing.this, android.R.layout.simple_spinner_dropdown_item, items);
//set the spinners adapter to the previously created one.
        dropdown.setAdapter(adapter);
        selectText=dropdown.getSelectedItem().toString();
        Log.d("hi","Main"+selectText);
        kor();
    }
    void kor()
    {
        Log.d("hi","2");
        TableRow tr = new TableRow(context);
        String name="Name";
        tr.addView(getTextView(context,0, name, Color.BLUE, Typeface.BOLD, Color.WHITE));
        tl.addView(tr,getLayoutParams());
        String url="https://www.codeforces.com/api/contest.list?gym=false";
        solve_api_request api_request = new solve_api_request(context);
        api_request.api_request(url, new solve_api_request.VolleyResponseListener() {
            @Override
            public void onError(String message) {
                Log.d("hi",message);
            }
            @Override
            public void onResponse(JSONObject object) {
                kor1(object);
            }
        });
    }
    void kor1(JSONObject object)
    {
        Log.d("hi","seondkor1");
        try {
            JSONArray resullt = object.getJSONArray("result");
            int len=resullt.length();
            for(int i=0;i<len;i++)
            {
                JSONObject cur_contest=resullt.getJSONObject(i);
                String phase=cur_contest.getString("phase");
                //  Log.d("hi","ab"+i);
                if(phase.equals("BEFORE"))
                    continue;
                int id=cur_contest.getInt("id");
                String name=cur_contest.getString("name");
                TableRow tr= new TableRow(context);
                tr.addView(getTextView(context,id, name, Color.BLUE, Typeface.BOLD, Color.WHITE));
                tl.addView(tr,getLayoutParams());
            }
        }
        catch (Exception e)
        {
            Log.d("hi",e.toString());
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
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(id!=0) {
                     Log.d("hi", "hello1");
                    try {
                        Integer id1=new Integer(id);
                        Intent intent = new Intent(context,Contest_Standing2.class);
                        Bundle bundle = new Bundle();
                        String unofficial1="false";
                        if(unofficial.isChecked())
                            unofficial1="true";
                        bundle.putString("country",dropdown.getSelectedItem().toString());
                        bundle.putString("id",id1.toString());
                        bundle.putString("unofficial",unofficial1);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    } catch (Exception e) {
                        Log.d("hi",e.toString());
                    }
                }
                else {
                      Log.d("hi", "hello2");
                    //  Toast.makeText(context, title, Toast.LENGTH_SHORT).show();
                }
            }
        });
        return tv;
    }
    @NonNull
    TableRow.LayoutParams getLayoutParams() {
        TableRow.LayoutParams params = new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.MATCH_PARENT);
        params.setMargins(2, 0, 0, 2);
        return params;
    }
    @NonNull
    TableLayout.LayoutParams getTblLayoutParams() {
        return new TableLayout.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT);
    }
    @Override
    public void onBackPressed() {
        finish();
    }

}