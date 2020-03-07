package com.example.ontime;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ForgottenPassword extends AppCompatActivity
{
    Button cancel, submit;
    EditText recover_pass;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotten_password);

        cancel = findViewById(R.id.cancel_btn);
        submit = findViewById(R.id.signup_submit_btn);
        recover_pass = findViewById(R.id.recover_password);

        // Redirects to different screens on button press.
        cancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(v.getContext(), Login.class);
                startActivity(intent);
            }
        });

        submit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (validateField())
                {
                    Intent intent = new Intent(v.getContext(), HomePage.class);
                    startActivity(intent);
                }
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
        if (!validateEmail(recover_pass.getText()))
        {
            recover_pass.setError("Enter a valid Email!");
            return false;
        }
        else
        {
            return true;
        }
    }
}
