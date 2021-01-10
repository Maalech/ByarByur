package com.byarbyur.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.byarbyur.myapplication.Adapter.OrderHistoryAdapter;
import com.byarbyur.myapplication.data.SharedPref;
import com.byarbyur.myapplication.model.Pesanan;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class OrderRequestFragment extends Fragment {

    FirebaseAuth fAuth;
    String userId;
    private FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    private OrderHistoryAdapter adapter;

    public OrderRequestFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_order_request, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        fAuth = FirebaseAuth.getInstance();
        userId = fAuth.getCurrentUser().getUid();
        final String role= SharedPref.getRole(getContext());
        Query query;
        if(role.equals("buyer")){
            query = fStore.collection("pesan").whereEqualTo("userid", userId);
        }else {
            query = fStore.collection("pesan").whereEqualTo("usahaid", userId);
        }
        FirestoreRecyclerOptions<Pesanan> options = new FirestoreRecyclerOptions.Builder<Pesanan>()
                .setQuery(query, Pesanan.class)
                .build();
        adapter = new OrderHistoryAdapter(options);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView2);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

        adapter.setOnClickListener(new OrderHistoryAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(DocumentSnapshot documentSnapshot, int position) {
                String id = documentSnapshot.getId();
                String usahaid =documentSnapshot.get("usahaid").toString();
                String userid=documentSnapshot.get("userid").toString();
                if(role.equals("buyer")){
                    Intent i = new Intent(getActivity(), ReservationDetailActivity.class);
                    i.putExtra("id",id);
                    startActivity(i);
                }else{
                    Intent i = new Intent(getActivity(), OrderDetailActivity.class);
                    i.putExtra("id",id);
                    i.putExtra("usahaid",usahaid);
                    i.putExtra("userid",userid);
                    startActivity(i);
                }

            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

}