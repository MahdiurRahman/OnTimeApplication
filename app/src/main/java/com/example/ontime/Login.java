package com.example.ontime;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

public class Login extends AppCompatActivity
{
    private Button button;

    EditText email, password;
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
//                Intent i = getIntent();
//                String user_name = i.getStringExtra("name");

                if (validateFields())
                {
                    Intent intent = new Intent(view.getContext(), HomePage.class);
                    startActivity(intent);
                }
                //intent.putExtra("name", user_name);
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

    // This method of validating email is actually genius level lol. - Kevin
    public static boolean validateEmail(CharSequence target)
    {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    private boolean validateFields()
    {
        int minimum_password_length = 8;

        if (!validateEmail(email.getText()))
        {
            email.setError("Incorrect Email!");
            return false;
        }
        else if (password.getText().length() < minimum_password_length)
        {
            password.setError("Minimum length of password is 8.");
            return false;
        }
        else
        {
            return true;
        }
    }

    public void forgotPass()
    {
        Intent intent = new Intent(this, ForgottenPassword.class);
        startActivity(intent);
    }
}
