package com.example.ontime;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SignUp extends AppCompatActivity
{
    EditText email, pass, name;
    Button submit_button, cancel_button;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        submit_button = findViewById(R.id.submit_btn);
        cancel_button = findViewById(R.id.cancel_btn);

        email = findViewById(R.id.email_field);
        pass = findViewById(R.id.pass_field);
        name = findViewById(R.id.name_field);

        submit_button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (validateField())
                {
                    Intent intent = new Intent(v.getContext(), Login.class);
                    //intent.putExtra("name", user_name);
                    startActivity(intent);
                }
            }
        });

        cancel_button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(v.getContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }

    // This method of validating email is actually genius level lol. - Kevin
    public static boolean validateEmail(CharSequence target)
    {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    private boolean validateField()
    {
        int minimum_password_length = 8;

        if (!validateEmail(email.getText()))
        {
            email.setError("Enter a valid Email!");
            return false;
        }
        else if (pass.getText().length() < minimum_password_length)
        {
            pass.setError("Minimum length of password is 8.");
            return false;
        }
        else if (!name.getText().toString().matches("[a-z, A-Z]*"))
        {
            name.setError("Your name got numbers or symbols in it!");
            return false;
        }
        else
        {
            return true;
        }
    }
}
