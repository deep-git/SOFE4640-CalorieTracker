package com.example.calorietracker;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Profile extends AppCompatActivity {

    TextView logout;
    TextView username;
    TextView email;
    TextView age;
    TextView weight;
    TextView height;
    TextView update;

    EditText enterAge;
    EditText enterWeight;
    EditText enterHeight;

    ImageView home;
    ImageView progress;
    ImageView profile;

    FirebaseAuth mAuth;
    FirebaseFirestore fdb;
    FirebaseUser user;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        logout = (TextView) findViewById(R.id.logout);
        update = (TextView) findViewById(R.id.update);
        home = (ImageView) findViewById(R.id.home);
        progress = (ImageView) findViewById(R.id.progress);
        profile = (ImageView) findViewById(R.id.profile);
        enterAge = (EditText) findViewById(R.id.enterAge);
        enterWeight = (EditText) findViewById(R.id.enterWeight);
        enterHeight = (EditText) findViewById(R.id.enterHeight);
        age = (TextView) findViewById(R.id.age);
        weight = (TextView) findViewById(R.id.weight);
        height = (TextView) findViewById(R.id.height);

        username = findViewById(R.id.username);
        email = findViewById(R.id.email);

        Intent intent = getIntent();
        String checkMeal = intent.getStringExtra("meal");
        String calendarDay = intent.getStringExtra("date");
        String calHeader = intent.getStringExtra("calHeader");

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Profile.this, Home.class);
                saveIntentExtra(intent, calendarDay, calHeader, checkMeal);
                startActivity(intent);
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Profile.this, Profile.class);
                saveIntentExtra(intent, calendarDay, calHeader, checkMeal);
                startActivity(intent);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(Profile.this, Login.class);
                startActivity(intent);
                finish();
            }
        });

//  --------------------------------------------------------------------------------------------------

        fdb = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        update.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String getAge = enterAge.getText().toString();
                String getWeight = enterWeight.getText().toString();
                String getHeight = enterHeight.getText().toString();

                DocumentReference documentReference = fdb.collection("users").document(userID);
                Map<String,Object> profileStats = new HashMap<>();

                if (!getAge.isEmpty()) {
                    profileStats.put("age", getAge);
                }

                if (!getWeight.isEmpty()) {
                    profileStats.put("weight", getWeight);
                }

                if (!getHeight.isEmpty()) {
                    profileStats.put("height", getHeight);
                }
                documentReference.update(profileStats).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("TAG","User profile statistics have been updated" + userID);
                        Toast.makeText(Profile.this, "Your profile statistics have been updated", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        if (user != null) {
            String emailDetail = user.getEmail();
            email.setText(emailDetail);

            fdb.collection("users")
                    .document(userID)
                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Log.d(TAG, "DocumentSnapshot data: " + document.getData());

                            String usernameDetail = document.getString("username");
                            String ageDetail = document.getString("age");
                            String weightDetail = document.getString("weight");
                            String heightDetail = document.getString("height");

                            username.setText(usernameDetail);
                            age.setText(ageDetail);
                            weight.setText(weightDetail);
                            height.setText(heightDetail);

                        } else {
                            Log.d(TAG, "No such document");
                        }
                    } else {
                        Log.d(TAG, "get failed with ", task.getException());
                    }
                }
            });

        }else{
            Toast.makeText(Profile.this, "Error: unable to retrieve profile details.", Toast.LENGTH_SHORT).show();
        }
    }

    public void saveIntentExtra(Intent intent, String calendarDay, String calHeader, String meal){
        intent.putExtra("meal", meal);
        intent.putExtra("date", calendarDay);
        intent.putExtra("calHeader", calHeader);
    }
}