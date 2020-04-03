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

    EditText name;
    EditText startLocationName;
    EditText startDate;
    EditText endDate;
    EditText startTime;
    EditText eventLocation;
    EditText alarmSound;
    //EditText vibration;
    Switch publicPrivateSwitch;
    Switch repeatWeeklySwitch;
    LinearLayout daysOfWeek;


    public void cancelEditEvent(View view) {
        // navigate back to homepage
        Intent intent = new Intent(this, HomePage.class);
        startActivity(intent);
    }

    //TODO
    public void validateForm(View view) {

    }

    // TODO: move this function and the one from CreateEvent to a file and import it if possible
    // create string from days entered - in order "SMTWTFS". For example, if an event is on Tuesday, Wednesday, and Friday: 0011010
    public String getEventDays() {
        LinearLayout daysOfWeek = (LinearLayout) findViewById(R.id.editDaysOfWeek);
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

        // TODO: add validation for form, select different input types for form

        // Get value from each input and put into json object
        // Put user's info into a JSON object
        final JSONObject newEvent = new JSONObject();
        SharedPreferences userInfo = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Event event = (Event) getIntent().getSerializableExtra("event");

        // Compare each value to the event's current value. Add the fields that have changed to the JSON object changes.
        final JSONObject changes = new JSONObject();

        try {
            if (name.getText().toString() != event.eventName) {
                changes.put("eventName", name.getText().toString());
            }
            if (startDate.getText().toString() != event.startDate) {
                changes.put("startDate", startDate.getText().toString());
            }
            if (repeatWeeklySwitch.isChecked() != event.repeatWeekly) {
                changes.put("repeatWeekly", repeatWeeklySwitch.isChecked());
            }
            String eventDays = getEventDays();
            if (eventDays != event.weeklySchedule) {
                changes.put("weeklySchedule", eventDays);
            }
            if (startTime.getText().toString() != event.time) {
                changes.put("time", startTime.getText().toString());
            }
            if (eventLocation.getText().toString() != event.locationName) {
                changes.put("locationName", eventLocation.getText().toString());
                // TODO: if location is updated, lat and lng should be too
                //newEvent.put("lat", 1);
                //newEvent.put("lng", 1);
            }

            if (endDate.getText().toString() != event.endDate) {
                changes.put("endDate", endDate.getText().toString());
            }

            // alarm sound
            //public/private
            //if (endDate.getText().toString() != event.endDate) {
            //    changes.put("endDate", endDate.getText().toString());
            //}

            //if (startLocationName.getText().toString() != event.) {
            //    changes.put("startLocationName", startLocationName.getText().toString());
            //}
        } catch (JSONException ex) {
            ex.printStackTrace();
        }

        // event location
        // Temporary values until google api is implemented

        //newEvent.put("startLat", 1);
        //newEvent.put("startLng", 1);

        try {
            String id = userInfo.getString("id", "0");
            newEvent.put("ownerId", id);
            newEvent.put("eventId", id);
            newEvent.put("changes", changes);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String url;
        // TODO: This needs to change, it should look at if the event is public or private already
        if (event.isPrivate) {
            url = "http://10.0.2.2:8080/api/events/public/edit";
        } else {
            url = "http://10.0.2.2:8080/api/events/private/edit";
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

        name = (EditText) findViewById(R.id.editEventName);
        startLocationName = (EditText) findViewById(R.id.editEventStartLocation);
        startDate = (EditText) findViewById(R.id.editEventStartDate);
        endDate = (EditText) findViewById(R.id.editEventEndDate);
        startTime = (EditText) findViewById(R.id.editEventStartTime);
        eventLocation = (EditText) findViewById(R.id.editEventLocation);
        alarmSound = (EditText) findViewById(R.id.editEventAlarm);
        //vibration = (EditText) findViewById(R.id.editEventVibration);
        publicPrivateSwitch = (Switch) findViewById(R.id.editPublicPrivateSwitch);
        repeatWeeklySwitch = (Switch) findViewById(R.id.editRepeatWeeklySwitch);
        daysOfWeek = (LinearLayout) findViewById(R.id.editDaysOfWeek);

        editEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            if (validateFields()) { // also check if location is valid
                editEvent();
            }
            }
        });


        setFormFields();
    }

    private void setFormFields() {
        // Get the event object passed to the activity
        Event event = (Event) getIntent().getSerializableExtra("event");
        Log.i("event", event.eventName);

        // Set each field in the form with the current values for the event
        name.setText(event.eventName);

        startDate.setText(event.startDate);
        endDate.setText(event.endDate);
        startTime.setText(event.time);
        eventLocation.setText(event.locationName);

        //alarmSound
        // TODO: change event to private or public
        //startLocationName.setText(event.startLocation);

        // TODO: I think this is broken
        if (event.repeatWeekly == true) {
            repeatWeeklySwitch.setChecked(true);
        }

        Log.i("days", event.weeklySchedule);
        // Set the days highlighted on top
        for (int i = 0; i < 7; i++) {
            ToggleButton day = (ToggleButton) daysOfWeek.getChildAt(i);
            if (event.weeklySchedule.charAt(i) == '1') {
                day.setChecked(true);
            }
        }
    }
}

