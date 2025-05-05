package com.example.sudokuproject;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.sudokuproject.utils.SchedulerUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class LogIn extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseDatabase myDB;
    private DatabaseReference myRef1;
    private ArrayList<String> emails;

    private EditText etEmail, etPass;

    private Button btLogIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_log_in);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        emails = new ArrayList<>();

        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser()!=null) {
            mAuth.signOut();
        }
        myDB = FirebaseDatabase.getInstance("https://sudokuproject-67973-default-rtdb.firebaseio.com/");
        myRef1 = myDB.getReference().child("Users");

        // Attach a listener to read the data at our posts reference
        myRef1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot child: dataSnapshot.getChildren()) {
                    emails.add(child.getValue(String.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }});


        etEmail = findViewById(R.id.etEmail);
        etPass = findViewById(R.id.etPass);
        btLogIn = findViewById(R.id.btLogIn);//button for login and for register

        btLogIn.setOnClickListener(view -> {
            String email = etEmail.getText().toString().trim();
            String password = etPass.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(LogIn.this, "Please enter both email and password", Toast.LENGTH_SHORT).show();
            } else if (emails.indexOf(email) != -1){
                login(email, password);
            } else {
                register(email, password);
            }
        });
    }

    private void login(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull com.google.android.gms.tasks.Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    Toast.makeText(LogIn.this, "Login success.", Toast.LENGTH_SHORT).show();
                    goToMain();
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(LogIn.this, "Login failed.",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void register(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull com.google.android.gms.tasks.Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(LogIn.this, "Registration success.", Toast.LENGTH_SHORT).show();
                            emails.add(email);
                            DatabaseReference myRef2 = myDB.getReference().child("Users");
                            myRef2.setValue(emails);
                            goToMain();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(LogIn.this, "Registration failed.",Toast.LENGTH_SHORT).show();
                        }
                    }

                });
    }

    private void goToMain() {
        Intent intent=new Intent(LogIn.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        if(mAuth.getCurrentUser() != null){
            goToMain();
        }

    }
}