package com.example.ontime;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class Login extends AppCompatActivity {
    private Button button;
    EditText email, password;
    CheckBox remember;
    Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.login_email);
        password = findViewById(R.id.login_password);
        remember = findViewById(R.id.remember_me);
        login = findViewById(R.id.login_btn);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = getIntent();
                String user_name = i.getStringExtra("name");

                if (validateFields()) {
                    loginUser(email.getText().toString(), password.getText().toString());
                }
                //intent.putExtra("name", user_name);
            }
        });

        // Links to next screen.
        button = (Button) findViewById(R.id.forgot_pass_btn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forgotPass();
            }
        });
    }

    private void loginUser(String email, String password) {
        // Put user's info into a JSON object
        JSONObject userInfo = new JSONObject();
        try {
            userInfo.put("email", email);
            userInfo.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Instantiate the RequestQueue ***
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="http://10.0.2.2:8080/api/login";

        // Request a string response from the provided URL.
        JsonObjectRequest loginRequest = new JsonObjectRequest(Request.Method.POST, url, userInfo, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    // TODO: handle different responses: wrong username/pw, successful login
                    Log.i("Success", "" + response.toString());

                    // email/password correct
                    if (response.has("user")) {
                        // Redirect to the HomePage
                        Intent intent = new Intent(Login.this, HomePage.class);
                        startActivity(intent);
                    }

                    // Wrong credentials
                    // TODO: handle when there is an error on the backend logging in
                    else {
                        Toast wrongCredentials = Toast. makeText(getApplicationContext(),"Wrong username or password.", Toast. LENGTH_SHORT);
                        wrongCredentials.show();
                    }

                }
            }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        // Add the request to the RequestQueue.
        queue.add(loginRequest);
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
        else if (password.getText().length() == 0)
        {
            password.setError("Enter a password.");
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
