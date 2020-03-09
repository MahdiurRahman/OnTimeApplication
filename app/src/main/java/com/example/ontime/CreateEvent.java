package com.example.ontime;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class CreateEvent extends AppCompatActivity {

    // move to onclick listener
    public void createEvent(View view) {
        EditText name = (EditText) findViewById(R.id.createEventName);
        EditText startLocation = (EditText) findViewById(R.id.createEventStartLocation);
        EditText startTime = (EditText) findViewById(R.id.createEventStartTime);
        EditText destination = (EditText) findViewById(R.id.createEventDestination);
        EditText alarmSound = (EditText) findViewById(R.id.createEventAlarm);
        EditText vibration = (EditText) findViewById(R.id.createEventVibration);
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
        //newEvent.addProperty("weeklySchedule", );
        //newEvent.addProperty("ownerId", );
        //newEvent.addProperty("startLocation", startLocation);
        //newEvent.addProperty("destination", destination);
        //newEvent.addProperty("startDate", );
        //newEvent.addProperty("repeatWeekly", );


        /*
                lat,
                lng,
         */




        // Send json to backend to put into db

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
    }


}
