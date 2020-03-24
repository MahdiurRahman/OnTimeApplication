package com.example.ontime;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

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

    public void logout()
    {
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