package com.example.cpdiary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterInTheApp extends AppCompatActivity implements View.OnClickListener {
    public TextView email_id, password, username;
    public Button register;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_in_the_app);
        Log.d("mytag"," RegisterInTheApp activity started ");
        email_id = findViewById(R.id.registeremailid);
        password = findViewById(R.id.password);

        register = findViewById(R.id.registerbutton);
        register.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if(v==findViewById(R.id.registerbutton))
        {
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email_id.getText().toString(),password.getText().toString()).addOnCompleteListener(RegisterInTheApp.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful())
                    {
                        SharedPreferences sp=getSharedPreferences("Login", MODE_PRIVATE);
                        SharedPreferences.Editor Ed=sp.edit();
                        Ed.putString("Unm",email_id.getText().toString() );
                        Ed.putString("Psw",password.getText().toString());
                        Ed.commit();
                        Toast.makeText(RegisterInTheApp.this,"Registered Successfully",Toast.LENGTH_SHORT).show();
                        finish();
                        Intent intent = new Intent(RegisterInTheApp.this,MainActivity.class);
                        startActivity(intent);
                    }
                    else
                    {
                        Toast.makeText(RegisterInTheApp.this,"Registration Unsuccessful",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}