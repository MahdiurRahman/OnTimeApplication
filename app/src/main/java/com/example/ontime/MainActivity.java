package com.example.ontime;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity
{
    // Connect to db.
    private static final String user = "";
    private static final String pass = "";

    private Button button, sign_up;

    // Creating the app.
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // If user is already logged in, send straight to HomePage, otherwise send to login page
        SharedPreferences userInfo = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        // If loggedIn boolean is not found in preferences, returns false
        Boolean loggedIn = userInfo.getBoolean("loggedIn", false);
        Log.i("loggedIn", loggedIn.toString());
        Intent intent;
        if (loggedIn == true) {
            intent = new Intent(this, HomePage.class);
            startActivity(intent);
        }

        // User not logged in
        // Button links to Login Page's screen.
        button = (Button) findViewById(R.id.LogIn);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                loginPage();
            }
        });

        sign_up = (Button) findViewById(R.id.SignUp);
        sign_up.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(v.getContext(), SignUp.class);
                startActivity(intent);
            }
        });
    }

    public void loginPage()
    {
        // Stuff goes here.
        Intent intent = new Intent(this, Login.class);

        startActivity(intent);
    }
}
