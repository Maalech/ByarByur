package com.byarbyur.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class FragmentDashboardBuyer extends Fragment {

    TextView namaTv,alamatTv;
    ImageView profileIv;
    Button findSeller,orderHistory,chatRoom;
    FirebaseFirestore fstore;
    FirebaseAuth fAuth;
    String userId;

    StorageReference storageReference;

    public FragmentDashboardBuyer() {
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
        return inflater.inflate(R.layout.fragment_dashboard_buyer, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        namaTv = view.findViewById(R.id.nama_user);
        alamatTv = view.findViewById(R.id.alamat_user);
        profileIv = view.findViewById(R.id.profileImageView);
        findSeller = view.findViewById(R.id.find_seller);
        orderHistory = view.findViewById(R.id.orderhis_btn);
        chatRoom = view.findViewById(R.id.chat_btn);


        storageReference = FirebaseStorage.getInstance().getReference();

        fstore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
        userId = fAuth.getCurrentUser().getUid();

        DocumentReference docRef = fstore.collection("users").document(userId);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                namaTv.setText(documentSnapshot.getString("nama"));
                alamatTv.setText(documentSnapshot.getString("alamat"));
                showImage();
            }
            private void showImage() {
                StorageReference profileRef = storageReference.child("users/" + userId + "/profile.jpg");
                profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).fit()
                                .centerCrop().into(profileIv);

                    }
                });
            }
        });

        findSeller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), FindSellerActivity.class));
            }
        });

        orderHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), OrderHistoryActivity.class));
            }
        });

        chatRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), ChatRoomActivity.class));
            }
        });
    }
}