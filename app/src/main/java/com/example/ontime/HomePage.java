package com.example.ontime;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.nio.channels.InterruptedByTimeoutException;

public class HomePage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    Button add_event, search_pub_event;
    TextView user_name;

    DrawerLayout drawer_layout;
    NavigationView navigation_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        add_event = findViewById(R.id.add_event_btn);
        user_name = findViewById(R.id.name_of_user);

        search_pub_event = findViewById(R.id.search_public_events_button);

        // get logged in user's first name (saved during login)
        SharedPreferences userInfo = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String firstName = userInfo.getString("firstName", "");
        user_name.setText(firstName);

        String privateEventsString = userInfo.getString("privateEvents", null);
        String publicEventsString = userInfo.getString("publicEvents", null);

        if (publicEventsString == null && privateEventsString == null) {
            Toast.makeText(getApplicationContext(),"There was an error loading your events.", Toast. LENGTH_SHORT).show();
        } else {
            setEvents(publicEventsString, privateEventsString);
        }
        // TODO: make request to backend when homepage is loaded to get new events/event changes

        // Menu variables.
        drawer_layout = findViewById(R.id.drawer_layout);
        navigation_view = findViewById(R.id.nav_view);

        navigation_view.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer_layout, R.string.navigation_open, R.string.navigation_close);
        drawer_layout.addDrawerListener(toggle);
        toggle.syncState();

        navigation_view.setNavigationItemSelectedListener(this);

        add_event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(v.getContext(), CreateEvent.class);
                startActivity(intent);
            }
        });

        search_pub_event.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(v.getContext(), SearchingPublicEvent.class);
                startActivity(intent);
            }
        });
    }

    // populate the homepage with a list of the user's cached events
    void setEvents(String publicEventsString, String privateEventsString) {
        RecyclerView eventsListView = findViewById(R.id.eventsList);
        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
        eventsListView.setLayoutManager(llm);


        JSONArray privateEventsList = new JSONArray();
        JSONArray publicEventsList = new JSONArray();

        try {
            privateEventsList = new JSONArray(privateEventsString);
            publicEventsList = new JSONArray(publicEventsString);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        List<Event> eventList = new ArrayList<>();
        // put private events into eventList
        for (int i=0; i < privateEventsList.length(); i++) {
            JSONObject event = null;
            try {
                event = (JSONObject) privateEventsList.get(i);
                //create an Event object
                String eventName = event.getString("eventName");
                String startDate = event.getString("startDate");
                String weeklySchedule = event.getString("weeklySchedule");
                String time = event.getString("time");
                int id = event.getInt("id");
                int ownerId = event.getInt("ownerId");
                int repeatWeekly =  event.getInt("repeatWeekly");
                String locationName = event.getString("locationName");
                Double lat = event.getDouble("lat");
                Double lng = event.getDouble("lng");
                String code = event.getString("code");
                String endDate = event.getString("endDate");
                eventList.add(new Event(id, ownerId, eventName, startDate, endDate, weeklySchedule, time, repeatWeekly, locationName, lat, lng, code, true));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.i("EVENT", event.toString());
        }

        // put public events into list
        for (int i=0; i < publicEventsList.length(); i++) {
            JSONObject event = null;
            try {
                event = (JSONObject) publicEventsList.get(i);
                //create an Event object
                String eventName = event.getString("eventName");
                String startDate = event.getString("startDate");
                String weeklySchedule = event.getString("weeklySchedule");
                String time = event.getString("time");
                int id = event.getInt("id");
                int ownerId = event.getInt("ownerId");
                int repeatWeekly =  event.getInt("repeatWeekly");
                String locationName = event.getString("locationName");
                Double lat = event.getDouble("lat");
                Double lng = event.getDouble("lng");
                String code = event.getString("code");
                String endDate = event.getString("endDate");
                eventList.add(new Event(id, ownerId, eventName, startDate, endDate, weeklySchedule, time, repeatWeekly, locationName, lat, lng, code, false));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.i("EVENT", event.toString());
        }

        RVAdapter adapter = new RVAdapter(eventList);
        eventsListView.setAdapter(adapter);
    }

    public void logout() {
        // remove all user preferences
        PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().clear().apply();

        // Navigate to MainActivity
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case (R.id.invites):
                Intent intent = new Intent(HomePage.this, Invites.class);
                startActivity(intent);
                break;

            case (R.id.notifications):
                intent = new Intent(HomePage.this, Notification.class);
                startActivity(intent);
                break;

            case (R.id.private_event):
                intent = new Intent(HomePage.this, PrivateEvents.class);
                startActivity(intent);
                break;

            case (R.id.public_event):
                intent = new Intent(HomePage.this, PublicEvents.class);
                startActivity(intent);
                break;

            case (R.id.change_password):
                intent = new Intent(HomePage.this, ChangePassword.class);
                startActivity(intent);
                break;

            case (R.id.log_out_homepage):
                logout();
                break;
        }
        drawer_layout.closeDrawer(GravityCompat.START);
        return true;
    }
}