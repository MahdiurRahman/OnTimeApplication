package com.example.ontime;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

public class Login extends AppCompatActivity
{
    private Button button;

    EditText email, password;
    private boolean rememberMe = false;
    CheckBox remember;
    Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.login_email);
        password = findViewById(R.id.login_password);
        remember = findViewById(R.id.remember_me);
        login = findViewById(R.id.login_btn);

        login.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(view.getContext(), HomePage.class);
                startActivity(intent);
            }
        });

        // Links to next screen.
        button = (Button) findViewById(R.id.forgot_pass_btn);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                forgotPass();
            }
        });
    }

    public void forgotPass()
    {
        Intent intent = new Intent(this, ForgottenPassword.class);
        startActivity(intent);
    }
}
