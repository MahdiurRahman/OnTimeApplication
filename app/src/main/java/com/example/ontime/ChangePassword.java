package com.example.ontime;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class ChangePassword extends AppCompatActivity
{
    Button confirm_password_changes;
    EditText password, confirm_pass;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        confirm_password_changes = findViewById(R.id.save_changed_password);
        password = findViewById(R.id.new_password);
        confirm_pass = findViewById(R.id.confirm_password);

        confirm_password_changes.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Context context = getApplicationContext();
                int duration = Toast.LENGTH_SHORT;

                if (matching_password())
                {
                    Toast.makeText(context, "Password Changed!", duration).show();
                    Intent intent = new Intent(ChangePassword.this, HomePage.class);
                    startActivity(intent);
                }
            }
        });
    }

//    private void changePassword()
//    {
//        final JSONObject user_password = new JSONObject();
//
//        try
//        {
//            user_password.put("password", password.getText().toString());
//            user_password.put("new_password", confirm_pass.getText().toString());
//        }
//        catch (JSONException error)
//        {
//            error.printStackTrace();
//        }
//
//        // Create JSON Object.
//        RequestQueue queue = Volley.newRequestQueue(ChangePassword.this);
//        String url = "https://fair-hallway-265819.appspot.com/api/login";
//
//        JsonObjectRequest change_password_request = new JsonObjectRequest(Request.Method.POST, url, user_password, new Response.Listener<JSONObject>()
//        {
//            @Override
//            public void onResponse(JSONObject repsonse)
//            {
//
//            }
//        });
//    }

    private boolean matching_password()
    {
        if (password.getText().toString().equals(confirm_pass.getText().toString()))
        {
            return true;
        }
        else
        {
            Toast.makeText(getApplicationContext(), "Password is blank!", Toast.LENGTH_SHORT).show();
            password.setError("Password not the same");
            confirm_pass.setError("Password not the same");
            return false;
        }
    }
}
