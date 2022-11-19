package com.example.calorietracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

public class Home extends AppCompatActivity {

    ImageView home;
    ImageView progress;
    ImageView profile;
    TextView calendarSelection;
    TextView date;
    LinearLayout breakfastAddBackground;

    FirebaseAuth mAuth;
    FirebaseFirestore fdb;
    String userID;

    private List<String> totalCals;
    private List<String> breafastCals;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        home = (ImageView) findViewById(R.id.home);
        progress = (ImageView) findViewById(R.id.progress);
        profile = (ImageView) findViewById(R.id.profile);
        calendarSelection = (TextView) findViewById(R.id.calendarSelection);
        date = (TextView) findViewById(R.id.date);
        breakfastAddBackground = (LinearLayout) findViewById(R.id.breakfastAddBackground);

        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar.clear();

        Long today = MaterialDatePicker.todayInUtcMilliseconds();

        MaterialDatePicker.Builder builder = MaterialDatePicker.Builder.datePicker();
        builder.setTitleText("Select a date");
        builder.setSelection(today);
        MaterialDatePicker<Long> materialDatePicker = builder.build();

        calendarSelection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                materialDatePicker.show(getSupportFragmentManager(), "DATE_PICKER");
            }
        });

        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
            @Override
            public void onPositiveButtonClick(Long selection) {
                date.setText(materialDatePicker.getHeaderText());
            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home.this, Home.class);
                startActivity(intent);
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home.this, Profile.class);
                startActivity(intent);
            }
        });

        breakfastAddBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home.this, BreakfastList.class);
                startActivity(intent);
            }
        });


//  --------------------------------------------------------------------------------------------------

        fdb = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        userID = mAuth.getCurrentUser().getUid();

        totalCals = new ArrayList<String>();
        breafastCals = new ArrayList<String>();

        //getTotalCals();
        getBreakfastCal();

    }

    public void getBreakfastCal(){
        fdb.collection("users")
                .document(userID)
                .collection("breakfast")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        DecimalFormat df = new DecimalFormat();
                        df.setMaximumFractionDigits(2);

                        breafastCals.clear();

                        for (DocumentSnapshot snapshot : task.getResult()) {
                            String cal = snapshot.getString("calories");
                            breafastCals.add(cal);
                            totalCals.add(cal);
                        }

                        float sumBreakfastCal = 0;
                        for (String number : breafastCals) {
                            float n = Float.parseFloat(number);
                            sumBreakfastCal += n;
                        }


                        Toast.makeText(Home.this, "" + totalCals, Toast.LENGTH_SHORT).show();

                        TextView breakfastCalories = findViewById(R.id.breakfastCalories);
                        breakfastCalories.setText("" + (df.format(sumBreakfastCal)) + " Cal");

                        getTotalCals();
                    }

                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Home.this, "Error", Toast.LENGTH_SHORT).show();

                    }
                });

    }

    public void getTotalCals(){
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);

        float sumTotalCal = 0;
        for (String number : totalCals) {
            float n = Float.parseFloat(number);
            sumTotalCal += n;
        }
        Toast.makeText(Home.this, "" + sumTotalCal, Toast.LENGTH_SHORT).show();

        TextView totalCalorie = findViewById(R.id.totalCalorie);
        totalCalorie.setText("" + (df.format(sumTotalCal)) + " Cal");
    }

}