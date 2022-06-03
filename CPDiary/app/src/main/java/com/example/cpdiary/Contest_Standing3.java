package com.example.cpdiary;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;

import androidx.appcompat.app.AppCompatActivity;

public class Contest_Standing3 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contest_standing3);
        WebView myWebView = (WebView) findViewById(R.id.webview);
        Intent intent =getIntent();
        Bundle bundle = intent.getExtras();
        Log.d("hi","now in handle page");
        String handle="Alfeh";
        if(bundle!=null)
        {
            handle = bundle.getString("handle");
        }
        myWebView.loadUrl("https://codeforces.com/profile/"+handle);
    }
    @Override
    public void onBackPressed() {
        finish();
    }
}