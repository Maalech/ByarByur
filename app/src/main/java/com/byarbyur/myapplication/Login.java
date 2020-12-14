package com.byarbyur.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Login extends AppCompatActivity {
    EditText uEmail;
    EditText uPass;
    Button submit;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    TextView registerT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        submit = findViewById(R.id.login_btn);
        uEmail = findViewById(R.id.login_t);
        uPass = findViewById(R.id.pass_t);
        registerT = findViewById(R.id.register_t);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final String email = uEmail.getText().toString().trim();
                    final String password = uPass.getText().toString().trim();

                    if (TextUtils.isEmpty(email)) {
                        uEmail.setError("Email Tidak Boleh Kosong");
                        return;
                    }

                    if (TextUtils.isEmpty(password)) {
                        uPass.setError("Password Tidak Boleh Kosong");
                        return;
                    }

                    if (password.length() < 8) {
                        uPass.setError("Password Harus >= 8 Characters");
                        return;
                    }

                    fAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                final String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                fStore.collection("users").document(uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            DocumentSnapshot document = task.getResult();
                                            if (document.exists()) {
                                                String type = document.getString("type");
                                                if (type.equals("student")) {
                                                    Toast.makeText(Login.this, "Logged in Success", Toast.LENGTH_SHORT).show();
                                                    startActivity(new Intent(getApplicationContext(), LandingPageUser.class));
                                                    finish();
                                                } else if (type.equals("teacher")) {
                                                    Toast.makeText(Login.this, "Logged in Success", Toast.LENGTH_SHORT).show();
                                                    startActivity(new Intent(getApplicationContext(), LandingPageUser.class));
                                                    finish();
                                                }
                                            }
                                        }
                                    }
                                });

                            } else {
                                Toast.makeText(Login.this, "Error !" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                            }
                        }
                    });

                }

            });

        registerT.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(getApplicationContext(),RegisterAs.class));
                    finish();
                }
            });
}
}