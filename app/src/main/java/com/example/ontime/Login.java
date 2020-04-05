package com.example.ontime;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.telephony.CarrierConfigManager;
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
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.prefs.PreferenceChangeEvent;

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

    private void loginUser(final String email, String password) {
        // Put user's info into a JSON object
        final JSONObject userInfo = new JSONObject();
        try {
            userInfo.put("email", email);
            userInfo.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Instantiate the RequestQueue ***
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="https://fair-hallway-265819.appspot.com/api/login";

        // Request a string response from the provided URL.
        JsonObjectRequest loginRequest = new JsonObjectRequest(Request.Method.POST, url, userInfo, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                Log.i("Success", "" + response.toString());
                // Successful login
                if (response.has("user")) {
                    // Save user's information into SharedPreferences: data that is stored persistently and is available across all activities
                    SharedPreferences userInfo = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor userInfoEditor = userInfo.edit();
                    try {
                        JSONObject userInfoJSON = (JSONObject) response.get("userInfo");
                        JSONObject userJSON = (JSONObject) response.get("user");
                        JSONObject userEvents = (JSONObject) response.get("events");
                        JSONArray privateEventsList = userEvents.getJSONArray("private");
                        JSONArray publicEventsList = userEvents.getJSONArray("public");

                        // Convert the array of event objects into a string so that it can be stored in SharedPreferences
                        String privateEvents = privateEventsList.toString();
                        String publicEvents = publicEventsList.toString();

                        String firstName = userInfoJSON.get("firstName").toString();
                        String lastName = userInfoJSON.get("lastName").toString();
                        String id = userJSON.get("id").toString();
                        Log.i("user id", id);
                        userInfoEditor.putBoolean("loggedIn", true);
                        userInfoEditor.putString("email", email);
                        userInfoEditor.putString("firstName", firstName);
                        userInfoEditor.putString("lastName", lastName);
                        userInfoEditor.putString("lastName", lastName);
                        userInfoEditor.putString("id", id);
                        userInfoEditor.putString("privateEvents", privateEvents);
                        userInfoEditor.putString("publicEvents", publicEvents);
                        userInfoEditor.commit();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    // Redirect to the HomePage
                    Intent intent = new Intent(Login.this, HomePage.class);
                    startActivity(intent);
                }

                // Wrong credentials
                else if (response.has("authError")) {
                    Toast wrongCredentials = Toast.makeText(getApplicationContext(),"Wrong username or password.", Toast. LENGTH_SHORT);
                    wrongCredentials.show();
                }

                // Login error (backend issue)
                else {
                    Toast error = Toast.makeText(getApplicationContext(),"There was an error logging in.", Toast. LENGTH_SHORT);
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
