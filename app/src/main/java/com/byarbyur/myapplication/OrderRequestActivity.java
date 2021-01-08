package com.byarbyur.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.byarbyur.myapplication.Adapter.OrderRequestAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
public class OrderRequestActivity extends AppCompatActivity {

    FirebaseAuth fAuth;
    String userId;
    Button inProgress, selesai;
    private FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    private OrderRequestAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_request);
        fAuth = FirebaseAuth.getInstance();
        userId = fAuth.getCurrentUser().getUid();

        fragmentProgress();

        selesai = findViewById(R.id.selesai_btn);
        inProgress = findViewById(R.id.inProgrees_btn);

        selesai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentFinish();
            }
        });

        inProgress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentProgress();
            }
        });



    }

    private void fragmentProgress() {
        // Begin the transaction
        Fragment fragment = new OrderRequestFragment();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();

        // Replace the contents of the container with the new fragment
        fragmentTransaction.replace(R.id.frame_container, fragment);

        // Complete the changes added above
        fragmentTransaction.commit();
    }
    private void fragmentFinish() {
        // Begin the transaction
        Fragment fragment = new OrderRequestCompleteFragment();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();

        // Replace the contents of the container with the new fragment
        fragmentTransaction.replace(R.id.frame_container, fragment);

        // Complete the changes added above
        fragmentTransaction.commit();
    }
}