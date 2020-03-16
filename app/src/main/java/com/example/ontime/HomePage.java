package com.example.ontime;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

public class HomePage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    Button add_event;
    TextView user_name;

    DrawerLayout drawer_layout;
    NavigationView navigation_view;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        add_event = findViewById(R.id.add_event_btn);
        user_name = findViewById(R.id.name_of_user);

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
//                Intent i = getIntent();
//                String name_of_user = i.getStringExtra("name");
                Intent intent = new Intent(v.getContext(), CreateEvent.class);
                //user_name.setText(name_of_user);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item)
    {
        switch (item.getItemId())
        {
            case (R.id.invites):
                Intent intent = new Intent(HomePage.this, Invites.class);
                startActivity(intent);

            case (R.id.notifications):
                break;

            case (R.id.private_event):
                break;

            case (R.id.public_event):
                break;

            case (R.id.change_password):
                break;

            case (R.id.log_out_homepage):
                break;
        }

        drawer_layout.closeDrawer(GravityCompat.START);

        return true;
    }
}