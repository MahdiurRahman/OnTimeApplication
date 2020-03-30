package com.example.ontime;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class EditEvent extends AppCompatActivity {
    public void cancelCreateEvent(View view) {
        // navigate back to homepage
        Intent intent = new Intent(this, HomePage.class);
        startActivity(intent);
    }

    public void validateForm(View view) {

    }

    // create string from days entered - in order "SMTWTFS". For example, if an event is on Tuesday, Wednesday, and Friday: 0011010
    public String getEventDays() {
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
        return daysString;
    }

    public void editEvent() {
        EditText name = (EditText) findViewById(R.id.createEventName);
        EditText startLocationName = (EditText) findViewById(R.id.createEventStartLocation);
        EditText startDate = (EditText) findViewById(R.id.createEventStartDate);
        EditText endDate = (EditText) findViewById(R.id.createEventEndDate);
        EditText startTime = (EditText) findViewById(R.id.createEventStartTime);
        EditText eventLocation = (EditText) findViewById(R.id.createEventLocation);
        EditText alarmSound = (EditText) findViewById(R.id.createEventAlarm);
        //EditText vibration = (EditText) findViewById(R.id.createEventVibration);
        Switch publicPrivateSwitch = (Switch) findViewById(R.id.publicPrivateSwitch);
        Switch repeatWeeklySwitch = (Switch) findViewById(R.id.repeatWeeklySwitch);

        // TODO: add validation for form, select different input types for form
        // TODO: add map
        // TODO: owner id

        // Get value from each input and put into json object
        // Put user's info into a JSON object
        final JSONObject newEvent = new JSONObject();
        SharedPreferences userInfo = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
/*      See what is in the userInfo object in sharedPreferences
        Map<String, ?> allEntries = userInfo.getAll();
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            Log.d("map values", entry.getKey() + ": " + entry.getValue().toString());
        }
  */
        String id = userInfo.getString("id", "0");
        Log.i("USER'S ID", id);


        try {
            newEvent.put("ownerId", id); //error bc string
            newEvent.put("eventName", name.getText().toString());
            newEvent.put("startDate", startDate.getText().toString());
            newEvent.put("endDate", endDate.getText().toString());
            newEvent.put("repeatWeekly", repeatWeeklySwitch.isChecked());                   //True: repeat weekly, False: not weekly
            newEvent.put("weeklySchedule", getEventDays());
            newEvent.put("time", startTime.getText().toString());
            newEvent.put("startLocationName", startLocationName.getText().toString());
            newEvent.put("locationName", eventLocation.getText().toString());               // event location
            // Temporary values until google api is implemented
            newEvent.put("lat", 1);
            newEvent.put("lng", 1);
            newEvent.put("startLat", 1);
            newEvent.put("startLng", 1);
            // lat
            // lng
            // TODO: save user's start location (will be saved for the event creator only)
            // will be different for user / event creator

        } catch (JSONException e) {
            e.printStackTrace();
        }

        String url;
        if (publicPrivateSwitch.isChecked()) {
            url = "https://fair-hallway-265819.appspot.com/api/events/public";
        } else {
            url = "https://fair-hallway-265819.appspot.com/api/events/private";
        }

        RequestQueue queue = Volley.newRequestQueue(this);

        // Request a string response from the provided URL.
        JsonObjectRequest createEventRequest = new JsonObjectRequest(Request.Method.POST, url, newEvent, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i("Success", "" + response.toString());

                // Event created successfully
                if (response.has("ownerId")) {
                    // Redirect to the HomePage
                    Intent intent = new Intent(EditEvent.this, HomePage.class);
                    startActivity(intent);
                }

                // Error
                else {
                    Toast error = Toast.makeText(getApplicationContext(), "There was an error creating the event.", Toast.LENGTH_SHORT);
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
        queue.add(createEventRequest);
    }

    // TODO
    // check location
    // at least 1 date must be checked
    // all fields must have an entry
    // make sure date and time is in the correct format
    // ...more
    boolean validateFields() {
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_event);
        Button editEvent = findViewById(R.id.editEventBtn);

        editEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateFields()) { // also check if location is valid
                    editEvent();
                }
            }
        });
    }
}

