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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


// Move these out of this file
class Event {
    String eventName;
    String startDate;
    String time;
    String weeklySchedule;
    // number of attendees?

    Event(String eventName, String startDate, String weeklySchedule, String time) {
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
        String shortDate = startDate.substring(0,9);


        this.eventName = eventName;
        this.startDate = shortDate;
        this.time = time;
        this.weeklySchedule = days;
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
        EventViewHolder pvh = new EventViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(EventViewHolder personViewHolder, int i) {
        personViewHolder.eventName.setText(events.get(i).eventName);
        personViewHolder.startDate.setText(events.get(i).startDate);
        personViewHolder.time.setText(events.get(i).time);
        personViewHolder.weeklySchedule.setText(events.get(i).weeklySchedule);

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
        }
    }

}

public class HomePage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    Button add_event;
    TextView user_name;

    DrawerLayout drawer_layout;
    NavigationView navigation_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        add_event = findViewById(R.id.add_event_btn);
        user_name = findViewById(R.id.name_of_user);


        // get logged in user's first name (saved during login), set name on top
        SharedPreferences userInfo = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String firstName = userInfo.getString("firstName", "");
        user_name.setText(firstName);


        // populate the list view with user's cached events
        RecyclerView eventsListView = findViewById(R.id.eventsList);
        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
        eventsListView.setLayoutManager(llm);



        String eventsString = userInfo.getString("events", null);

        JSONArray eventsList = new JSONArray();
        try {
            eventsList = new JSONArray(eventsString);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        List<Event> eventList = new ArrayList<>();
        for (int i=0; i < eventsList.length(); i++) {
            JSONObject event = null;
            try {
                event = (JSONObject) eventsList.get(i);
                //create an Event object
                String eventName = event.getString("eventName");
                String startDate = event.getString("startDate");
                String weeklySchedule = event.getString("weeklySchedule");
                String time = event.getString("time");
                eventList.add(new Event(eventName, startDate, weeklySchedule, time));


            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.i("EVENT", event.toString());

        }

        RVAdapter adapter = new RVAdapter(eventList);
        eventsListView.setAdapter(adapter);


        // Menu variables.
        drawer_layout = findViewById(R.id.drawer_layout);
        navigation_view = findViewById(R.id.nav_view);

        navigation_view.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer_layout, R.string.navigation_open, R.string.navigation_close);
        drawer_layout.addDrawerListener(toggle);
        toggle.syncState();

        navigation_view.setNavigationItemSelectedListener(this);

        add_event.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
//                Intent i = getIntent();
//                String name_of_user = i.getStringExtra("name");
                Intent intent = new Intent(v.getContext(), CreateEvent.class);
                //user_name.setText(name_of_user);
                startActivity(intent);
            }
        });
    }

    public void logout() {
        // remove all user preferences
        PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().clear().apply();

        // Navigate to MainActivity
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item)
    {
        switch (item.getItemId())
        {
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