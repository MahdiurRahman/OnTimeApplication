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


// TODO: Move these out of this file
class Event implements Serializable {
    int id;
    int ownerId;
    String eventName;
    String startDate;
    String endDate;
    String time;
    int repeatWeekly;
    String weeklySchedule;  // binary string
    String locationName;
    Double lat;
    Double lng;
    String code;
    String days;            // weekly schedule in days (Mon Tues etc)
    Boolean isPrivate;
    //number of attendees?
    // PRIVATE OR PUBLIC


    Event(int id, int ownerId, String eventName, String startDate, String endDate, String weeklySchedule, String time, int repeatWeekly, String locationName, Double lat, Double lng, String code, Boolean isPrivate) {
        // convert weekly schedule from binary string to a list of days (1000110 -> Sun, Thu, Fri)
        String days = "";
        if (weeklySchedule.charAt(0) == '1') {
            days += "Sun ";
        }
        if (weeklySchedule.charAt(1) == '1') {
            days += "Mon ";
        }
        if (weeklySchedule.charAt(2) == '1') {
            days += "Tue ";
        }
        if (weeklySchedule.charAt(3) == '1') {
            days += "Wed ";
        }
        if (weeklySchedule.charAt(4) == '1') {
            days += "Thu ";
        }
        if (weeklySchedule.charAt(5) == '1') {
            days += "Fri ";
        }
        if (weeklySchedule.charAt(6) == '1') {
            days += "Sat";
        }

        this.eventName = eventName;
        this.startDate = startDate;
        this.endDate = endDate;
        if (time.length() >5) {
            time = time.substring(0, 5);
            // Convert 24h time
            int hours = Integer.parseInt(time.substring(0,2));
            String minutes = time.substring(3,5);

            // TODO: convert time correctly
            if (hours > 12) {
                time = (hours % 12) + ":" + minutes + " PM";
            } else if (hours == 0 || hours == 12) {
                time = "12:" + minutes + "PM";
            } else {
                time = hours + ":" + minutes + " AM";
            }
        }
        this.time = time;
        this.weeklySchedule = weeklySchedule;
        this.days = days;
        this.id = id;
        this.ownerId = ownerId;
        this.repeatWeekly = repeatWeekly;
        this.locationName = locationName;
        this.lat = lat;
        this.lng = lng;
        this.code = code;
        this.isPrivate = isPrivate;
    }
}

class RVAdapter extends RecyclerView.Adapter<RVAdapter.EventViewHolder>{
    List<Event> events;

    RVAdapter(List<Event> events) {
        this.events = events;
    }

    @Override
    public EventViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item, viewGroup, false);
        EventViewHolder evh = new EventViewHolder(v);
        return evh;
    }

    @Override
    public void onBindViewHolder(EventViewHolder eventViewHolder, final int i) {
        eventViewHolder.eventName.setText(events.get(i).eventName);
        //eventViewHolder.startDate.setText(events.get(i).startDate);
        eventViewHolder.time.setText(events.get(i).time);
        eventViewHolder.weeklySchedule.setText(events.get(i).days);
        eventViewHolder.editEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent editEvent = new Intent(v.getContext(), EditEvent.class);
                editEvent.putExtra("event", events.get(i));
                v.getContext().startActivity(editEvent);
            }
        });
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public static class EventViewHolder extends RecyclerView.ViewHolder {
        TextView editEvent;
        CardView cv;
        TextView eventName;
        TextView startDate;
        TextView time;
        TextView weeklySchedule;

        EventViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            eventName = (TextView)itemView.findViewById(R.id.eventName);
            startDate = (TextView)itemView.findViewById(R.id.startDate);
            time = (TextView)itemView.findViewById(R.id.time);
            weeklySchedule = (TextView)itemView.findViewById(R.id.weeklySchedule);
            editEvent = (TextView)itemView.findViewById(R.id.editEvent);
        }
    }

}

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
        // TODO: add searchbar
        // TODO: make request to backend when homepage is loaded to get new events/event changes
        // TODO: also update cache when events are edited or created?

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
    // TODO: figure out how often to update the user's events
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