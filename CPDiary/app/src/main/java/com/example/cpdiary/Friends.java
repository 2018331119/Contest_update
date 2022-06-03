package com.example.cpdiary;

import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Friends extends AppCompatActivity implements View.OnClickListener{

    public DatabaseReference cpdiarydatabase;
    public Button add_friends;
    public TextView friend_list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);
        cpdiarydatabase = FirebaseDatabase.getInstance().getReference();
        add_friends = findViewById(R.id.add_friends);
        friend_list = findViewById(R.id.friend_list);
        killjoy();
        add_friends.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
         if(v.getId()==R.id.add_friends)
         {
             String new_friend = ((TextView)findViewById(R.id.friend_handle)).getText().toString();
             FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
             if(user!=null)
             {
                 String user_id = user.getUid();
                 String user_email = user.getEmail();
                 cpdiarydatabase.child("Users").child(user_id).push().setValue(new_friend);
                 cpdiarydatabase.child("Email").child(user_id).setValue(user_email);
             }
         }
         killjoy();
    }
    public void killjoy()
    {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null)
        {
            String user_id = user.getUid();
            Log.d("hi","friends"+"   "+user_id);
            cpdiarydatabase.child("Users").child(user_id).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {

                    if(task.isSuccessful())
                    {
                        Log.d("hi","friends"+"   "+"gfgdfshello");
                        String friends="";
                        int counter = 1;
                        for(DataSnapshot ds : task.getResult().getChildren())
                        {
                            String chamber = String.valueOf(ds.getValue());
                            Log.d("hi","friends"+"   "+chamber);
                            friends = friends + "\n " + counter + ") " + chamber;
                            ++counter;
                        }
                        friend_list.setText(friends);
                    }
                }
            });
        }
    }
}