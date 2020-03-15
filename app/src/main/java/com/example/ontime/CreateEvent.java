package com.example.ontime;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class CreateEvent extends AppCompatActivity {
    public void cancelCreateEvent(View view) {
        // navigate back to homepage
        Intent intent = new Intent(this, HomePage.class);
        startActivity(intent);
    }

    public void validateForm(View view) {

    }

    public void createEvent(View view) {
        EditText name = (EditText) findViewById(R.id.createEventName);
        EditText startLocation = (EditText) findViewById(R.id.createEventStartLocation);
        EditText startTime = (EditText) findViewById(R.id.createEventStartTime);
        EditText destination = (EditText) findViewById(R.id.createEventDestination);
        EditText alarmSound = (EditText) findViewById(R.id.createEventAlarm);
        EditText vibration = (EditText) findViewById(R.id.createEventVibration);

        // create string from days entered - in order "SMTWTFS". For example, if an event is on Tuesday, Wednesday, and Friday: 0011010
        LinearLayout daysOfWeek = (LinearLayout) findViewById(R.id.daysOfWeek);
        char[] eventDays = new char[7];
        for (int i = 0; i < 7; i++) {
            ToggleButton day = (ToggleButton) daysOfWeek.getChildAt(i);
            if (day.isChecked()) {
                Log.i("checked", "checked");
                eventDays[i] = '1';
            } else {
                eventDays[i] = '0';
            }
        }

        String daysString = new String(eventDays);
        Log.i("days entered", daysString);

        // TODO: add switch for public and private events
        // TODO: add validation for form, select different input types for form
        // TODO: add switch for repeat weekly
        // TODO: add map
        // TODO: owner id
        // lat, lng


        // Validate fields
        // can't be empty



        // Get value from each input and put into json object

        JSONObject newEvent = new JSONObject();
        //newEvent.put("eventName", name);
        //newEvent.addProperty("time", startTime);
        //newEvent.addProperty("endDate", );
        //newEvent.addProperty("weeklySchedule", daysString);
        //newEvent.addProperty("ownerId", );
        //newEvent.addProperty("startLocation", startLocation);
        //newEvent.addProperty("destination", destination);
        //newEvent.addProperty("startDate", );
        //newEvent.addProperty("repeatWeekly", );

        // Sample get request using Volley
        // put this in its own function
        // send JSON object to backend
        String url = "https://www.reddit.com/r/javascript.json";

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);

        // Request a string response from the provided URL.
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("RESPONSE", response.toString());
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        error.printStackTrace();
                        Log.i("RESPONSE", "ERROR");
                    }
                });

        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
    }
}

