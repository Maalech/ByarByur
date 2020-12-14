package com.byarbyur.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterAs extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_as);
    }

    public void teacher(View view) {
        startActivity(new Intent(getApplicationContext(),RegisterSellerActivity.class));
        finish();
    }

    public void student(View view) {
        startActivity(new Intent(getApplicationContext(),RegisterBuyerActivity.class));
        finish();
    }
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(),Login.class));
        finish();
    }
}