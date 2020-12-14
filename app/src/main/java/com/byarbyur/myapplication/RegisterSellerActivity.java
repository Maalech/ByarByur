package com.byarbyur.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class RegisterSellerActivity extends AppCompatActivity {
    public static final String TAG = "TAG";
    EditText reg_nama, reg_email, reg_password, reg_telephone;
    Button mRegisterBtn, upload_img;
    FirebaseFirestore fStore;
    FirebaseAuth fAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_seller);

        reg_nama = findViewById(R.id.nama_reg);
        reg_email = findViewById(R.id.email_reg);
        reg_password = findViewById(R.id.pass_reg);
        reg_telephone = findViewById(R.id.telephone_reg);
        mRegisterBtn = findViewById(R.id.register_btn);

        fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email = reg_email.getText().toString().trim();
                String password = reg_password.getText().toString().trim();
                final String fullName = reg_nama.getText().toString();
                final String telephone = reg_telephone.getText().toString();
                final String type = "seller";

                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                Pattern UpperCasePatten = Pattern.compile("[A-Z ]");
                Pattern lowerCasePatten = Pattern.compile("[a-z ]");
                Pattern digitCasePatten = Pattern.compile("[0-9 ]");

                if (TextUtils.isEmpty(fullName)) {
                    reg_nama.setError("Fullname is Required.");
                    return;
                }
                if (TextUtils.isEmpty(email)) {
                    reg_email.setError("Email is Required.");
                    return;
                }

                if (!email.matches(emailPattern)){
                    reg_email.setError("Email is Invalid.");
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    reg_password.setError("Password is Required.");
                    return;
                }

                if (password.length() < 8) {
                    reg_password.setError("Password Must be >= 8 Characters");
                    return;
                }

                if (!UpperCasePatten.matcher(password).find()){
                    reg_password.setError("Password must have atleast one uppercase character");
                    return;
                }

                if (!lowerCasePatten.matcher(password).find()){
                    reg_password.setError("Password must have atleast one lowercase character");
                    return;
                }

                if (!digitCasePatten.matcher(password).find()){
                    reg_password.setError("Password must have atleast one digit character");
                    return;
                }

                if (TextUtils.isEmpty(telephone)) {
                    reg_telephone.setError("Telephone is Required.");
                    return;
                }

                fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(RegisterSellerActivity.this, "User Created.", Toast.LENGTH_SHORT).show();
                            final String userID = fAuth.getCurrentUser().getUid();
                            DocumentReference documentReference = fStore.collection("users").document(userID);
                            Map<String, Object> user = new HashMap<>();
                            user.put("nama", fullName);
                            user.put("email", email);
                            user.put("telephone", telephone);
                            user.put("type",type);
                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "onSuccess: user Profile is created for " + userID);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, "onFailure: " + e.toString());
                                }
                            });
                            startActivity(new Intent(getApplicationContext(), LandingPageUser.class));
                            finish();
                        } else {
                            Toast.makeText(RegisterSellerActivity.this, "Error !" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                        }

                    }
                });

            }
        });

    }
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(),Login.class));
        finish();
    }
}