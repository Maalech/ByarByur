package com.byarbyur.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class LandingPageUser extends AppCompatActivity {
    Button logout, addBusiness;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page_user);

        logout = findViewById(R.id.logout_btn);

        addBusiness = findViewById(R.id.addB_btn);
        addBusiness.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LandingPageUser.this, "Logout Success", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), Login.class));
                finish();
            }
        });
    }
}