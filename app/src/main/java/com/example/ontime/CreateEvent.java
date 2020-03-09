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

public class CreateEvent extends AppCompatActivity {
    public void cancelCreateEvent(View view) {
        // navigate back to homepage
        Intent intent = new Intent(this, HomePage.class);
        startActivity(intent);
    }


    // move to onclick listener
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
            Log.i("aldskfjlkasd", String.valueOf(day.getId()));
            if (day.isChecked()) {
                Log.i("checked", "checked");
                eventDays[i] = '1';
            } else {
                eventDays[i] = '0';
            }
        }

        String daysString = new String(eventDays);
        Log.i("days entered", daysString);



        // get days too -> convert into weeklyschedule string
        // add switch for private/public
        // switch for repeat weekly
        // owner id
        // lat, lng: frontend or backend?


        // validate input

        // Get value from each input and put into json
        //JsonObject newEvent = new JsonObject();
        //newEvent.addProperty("eventName", name);
        //newEvent.addProperty("time", startTime);
        //newEvent.addProperty("endDate", );
        //newEvent.addProperty("weeklySchedule", daysString);
        //newEvent.addProperty("ownerId", );
        //newEvent.addProperty("startLocation", startLocation);
        //newEvent.addProperty("destination", destination);
        //newEvent.addProperty("startDate", );
        //newEvent.addProperty("repeatWeekly", );


    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);


    }

}

