package com.example.ontime;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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
