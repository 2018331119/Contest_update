package com.example.cpdiary;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity  implements View.OnClickListener {
    public Button login;
    public TextView register,email_id,password;
    public static int is_developer_mode_on=0,kotobar_try=0;
    private String unm="",pass="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        email_id = findViewById(R.id.emailid);
        password = findViewById(R.id.password);
        login = findViewById(R.id.main_login);
        register = findViewById(R.id.main_register);
        login.setOnClickListener(this);
        register.setOnClickListener(this);
        SharedPreferences sp1=this.getSharedPreferences("Login",MODE_PRIVATE);
        unm=sp1.getString("Unm", "");
        pass = sp1.getString("Psw", "");
        if(unm.length()>0 && pass.length()>0)
        {
         find_valid_login();
        }
    }
    void find_valid_login()
   {
       FirebaseAuth.getInstance().signInWithEmailAndPassword(unm,"omar25852+-").addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
           @Override
           public void onComplete(@NonNull Task<AuthResult> task) {
               if(task.isSuccessful())
               {
                   Intent intent = new Intent(MainActivity.this,Dashboard.class);
                   startActivity(intent);
               }
               else
               {
                   Toast.makeText(MainActivity.this,"Some problem while login",Toast.LENGTH_SHORT).show();
                   kotobar_try++;
                   if(kotobar_try<20)
                   find_valid_login();
               }
           }
       });
   }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_item,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
            if(item.isChecked())
            {
                item.setChecked(false);
                is_developer_mode_on=0;
                Toast.makeText(MainActivity.this, item.getTitle() +" is off", Toast.LENGTH_SHORT).show();
            }
            else
            {
                is_developer_mode_on=1;
                item.setChecked(true);
                Toast.makeText(MainActivity.this,item.getTitle()+" is on",Toast.LENGTH_SHORT).show();
            }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void onClick(View v) {
        if(v==findViewById(R.id.main_login)) {
            SharedPreferences sp1 = this.getSharedPreferences("Login", MODE_PRIVATE);
            unm = sp1.getString("Unm", "");
            pass = sp1.getString("Psw", "");
            if (unm.length() > 0 && pass.length() > 0) {
                find_valid_login();
            } else {
                FirebaseAuth.getInstance().signInWithEmailAndPassword(email_id.getText().toString(),password.getText().toString()).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            Log.d("hi",email_id.getText().toString());
                            SharedPreferences sp=getSharedPreferences("Login", MODE_PRIVATE);
                            SharedPreferences.Editor Ed=sp.edit();
                            Ed.putString("Unm",email_id.getText().toString() );
                            Ed.putString("Psw",password.getText().toString());
                            Ed.commit();
                            email_id.setText("");
                            password.setText("");
                            finish();
                            Intent intent = new Intent(MainActivity.this,Dashboard.class);
                            startActivity(intent);
                        }
                        else
                        {
                            Toast.makeText(MainActivity.this,"LogIn Unsuccessful, Register First.",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }
        else if(v==findViewById(R.id.main_register))
        {
            Intent intent = new Intent(MainActivity.this,RegisterInTheApp.class);
            startActivity(intent);
        }
    }
}