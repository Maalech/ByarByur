package com.byarbyur.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.byarbyur.myapplication.Adapter.FindSellerAdapter;
import com.byarbyur.myapplication.R;
import com.byarbyur.myapplication.SellerPageActivity;
import com.byarbyur.myapplication.model.Seller;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class FindSellerActivity extends AppCompatActivity {
    FirebaseAuth fAuth;
    String userId;
    private FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    private FindSellerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_seller);
        fAuth = FirebaseAuth.getInstance();
        userId = fAuth.getCurrentUser().getUid();

        Query query = fStore.collection("seller");
        FirestoreRecyclerOptions<Seller> options = new FirestoreRecyclerOptions.Builder<Seller>()
                .setQuery(query, Seller.class)
                .build();

        adapter = new FindSellerAdapter(options);
        RecyclerView recyclerView = findViewById(R.id.recyclerView2);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        adapter.setOnClickListener(new FindSellerAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(DocumentSnapshot documentSnapshot, int position) {

                String id = documentSnapshot.getId();

                Intent i = new Intent(com.byarbyur.myapplication.FindSellerActivity.this, SellerPageActivity.class);
                i.putExtra("id",id);
                startActivity(i);


            }
        });

    }



    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}