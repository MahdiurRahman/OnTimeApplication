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

public class CreateEvent extends AppCompatActivity {
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

    public void createEvent() {
        EditText name = (EditText) findViewById(R.id.createEventName);
        EditText startLocationName = (EditText) findViewById(R.id.createEventStartLocation);
        EditText startDate = (EditText) findViewById(R.id.createEventStartDate);
        EditText endDate = (EditText) findViewById(R.id.createEventEndDate);
        EditText startTime = (EditText) findViewById(R.id.createEventStartTime);
        EditText eventLocation = (EditText) findViewById(R.id.createEventLocation);
        EditText alarmSound = (EditText) findViewById(R.id.createEventAlarm);
        EditText vibration = (EditText) findViewById(R.id.createEventVibration);
        Switch publicPrivateSwitch = (Switch) findViewById(R.id.publicPrivateSwitch);
        Switch repeatWeeklySwitch = (Switch) findViewById(R.id.repeatWeeklySwitch);

        // TODO: add validation for form, select different input types for form
        // TODO: add map
        // TODO: owner id

        // Get value from each input and put into json object
        // Put user's info into a JSON object
        final JSONObject newEvent = new JSONObject();
        SharedPreferences userInfo = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Map<String, ?> allEntries = userInfo.getAll();
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            Log.d("map values", entry.getKey() + ": " + entry.getValue().toString());
        }
        String id = userInfo.getString("id", "0");
        Log.i("USER'S ID", id);
        // check location
/*

+----------------+---------------+------+-----+---------+----------------+
| Field          | Type          | Null | Key | Default | Extra          |
+----------------+---------------+------+-----+---------+----------------+
| id             | int(11)       | NO   | PRI | NULL    | auto_increment |
| ownerId        | int(11)       | NO   | MUL | NULL    |                |
| eventName      | varchar(255)  | YES  |     | NULL    |                |
| startDate      | date          | NO   |     | NULL    |                |
| endDate        | date          | NO   |     | NULL    |                |
| repeatWeekly   | tinyint(1)    | NO   |     | NULL    |                |
| weeklySchedule | varchar(7)    | YES  |     | NULL    |                |
| time           | time          | NO   |     | NULL    |                |
| locationName   | varchar(255)  | YES  |     | NULL    |                |
| lat            | double(15,12) | NO   |     | NULL    |                |
| lng            | double(15,12) | NO   |     | NULL    |                |
| code           | varchar(255)  | YES  |     | NULL    |                |


 */
        // at least 1 date must be checked
        // all fields must have an entry
        // make sure date and time is in the correct format

        try {
            newEvent.put("ownerId", id); //error bc string
            newEvent.put("eventName", name.getText().toString());
            newEvent.put("startDate", startDate.getText().toString()); // parse on frontend or backend into string
            newEvent.put("endDate", endDate.getText().toString()); // parse on frontend or backend into string
            newEvent.put("repeatWeekly", repeatWeeklySwitch.isChecked()); //True: repeat weekly, False: not weekly
            newEvent.put("weeklySchedule", getEventDays());
            newEvent.put("time", startTime.getText().toString());
            //newEvent.put("startLocationName", startLocationName.getText().toString());      // creator's start location: needs to be handled on backend
            newEvent.put("locationName", eventLocation.getText().toString());               // event location
            // when joining an event need to have a form for joining
            newEvent.put("lat", 1);
            newEvent.put("lng", 1);
            // lat
            // lng
            // TODO: save user's start location (will be saved for the event creator only)
            // will be different for user / event creator

        } catch (JSONException e) {
            e.printStackTrace();
        }

        String url;
        if (publicPrivateSwitch.isChecked()) {
            url = "http://10.0.2.2:8080/api/events/public";
        } else {
            url = "http://10.0.2.2:8080/api/events/private";
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
                Intent intent = new Intent(CreateEvent.this, HomePage.class);
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
    boolean validateFields() {
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        Button createEvent = findViewById(R.id.createEventBtn);

        createEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            if (validateFields()) { // also check if location is valid
                createEvent();
            }
            }
        });
    }
}

