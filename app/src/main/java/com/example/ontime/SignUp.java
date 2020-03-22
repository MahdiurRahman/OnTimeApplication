package com.example.ontime;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class SignUp extends AppCompatActivity
{
    EditText email, pass, firstName, lastName;
    Button submit_button, cancel_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        submit_button = findViewById(R.id.signup_submit_btn);
        cancel_button = findViewById(R.id.signup_cancel_btn);

        email = findViewById(R.id.email_field);
        pass = findViewById(R.id.pass_field);
        firstName = findViewById(R.id.first_name_field);
        lastName = findViewById(R.id.last_name_field);

        submit_button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
            if (validateField()) {
                signUp();
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


    public void signUp() {
        // Put user's info into a JSON object
        final JSONObject userInfo = new JSONObject();
        try {
            userInfo.put("email", email.getText().toString());
            userInfo.put("password", pass.getText().toString());
            userInfo.put("firstName", firstName.getText().toString());
            userInfo.put("lastName", lastName.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Instantiate the RequestQueue ***
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="https://fair-hallway-265819.appspot.com/api/register";

        // Request a string response from the provided URL.
        JsonObjectRequest registrationRequest = new JsonObjectRequest(Request.Method.POST, url, userInfo, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                // If registration is successful, redirect to Login screen
                if (response.has("email")) {
                    Log.i("Success", "" + response.toString());
                    Intent intent = new Intent(SignUp.this, Login.class);
                    startActivity(intent);
                }

                // Wrong credentials: show error
                else if (response.has("duplicateUserError")) {
                    Toast userExistsError = Toast. makeText(getApplicationContext(),"A user with that email already exists.", Toast. LENGTH_SHORT);
                    userExistsError.show();
                }

                // Other registration error (backend issue): show error
                else {
                    Toast error = Toast. makeText(getApplicationContext(),"There was an error creating your account.", Toast. LENGTH_SHORT);
                    error.show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        // Add the request to the RequestQueue.
        queue.add(registrationRequest);
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
        else if (TextUtils.isEmpty(firstName.getText()))
        {
            firstName.setError("Enter in a name!");
            return false;
        }
        else if (!firstName.getText().toString().matches("[a-z, A-Z]*"))
        {
            firstName.setError("Your name can't have or symbols in it!");
            return false;
        }
        else if (TextUtils.isEmpty(lastName.getText()))
        {
            lastName.setError("Enter in a name!");
            return false;
        }
        else if (!lastName.getText().toString().matches("[a-z, A-Z]*"))
        {
            lastName.setError("Your name can't have numbers or symbols in it!");
            return false;
        }
        else
        {
            return true;
        }
    }
}
