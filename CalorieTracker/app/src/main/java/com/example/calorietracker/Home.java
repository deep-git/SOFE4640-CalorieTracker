package com.example.calorietracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
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
    LinearLayout lunchAddBackground;
    LinearLayout dinnerAddBackground;
    LinearLayout snackAddBackground;

    FirebaseAuth mAuth;
    FirebaseFirestore fdb;
    String userID;

    private List<String> totalCals;
    private List<String> calories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        home = findViewById(R.id.home);
        progress = findViewById(R.id.progress);
        profile = findViewById(R.id.profile);
        calendarSelection = findViewById(R.id.calendarSelection);
        date = findViewById(R.id.date);

        breakfastAddBackground = findViewById(R.id.breakfastAddBackground);
        lunchAddBackground = findViewById(R.id.lunchAddBackground);
        dinnerAddBackground = findViewById(R.id.dinnerAddBackground);
        snackAddBackground = findViewById(R.id.snackAddBackground);

//        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
//        calendar.clear();

        SimpleDateFormat simpleFormat = new SimpleDateFormat("MMM dd, yyyy");
        simpleFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        Long today = MaterialDatePicker.todayInUtcMilliseconds();

        String breakfast = "Breakfast";
        String lunch = "Lunch";
        String dinner = "Dinner";
        String snack = "Snack";

        MaterialDatePicker.Builder builder = MaterialDatePicker.Builder.datePicker();
        builder.setTitleText("Select a date");
        MaterialDatePicker<Long> materialDatePicker = builder.build();
        final String[] calendarDay = new String[1];
        final String[] calHeader = new String[1];

        Intent intent = getIntent();
        String selectedDay = intent.getStringExtra("date");
        calHeader[0] = intent.getStringExtra("calHeader");

        if (selectedDay == null ) {
            calendarDay[0] = String.valueOf(today);
            calHeader[0] = simpleFormat.format(today);

        }else{
            calendarDay[0] = selectedDay;
        }

        date.setText(calHeader[0]);

//        Toast.makeText(Home.this, "Day: " + calendarDay[0] + " Header: " + calHeader[0], Toast.LENGTH_SHORT).show();

        calendarSelection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                materialDatePicker.show(getSupportFragmentManager(), "DATE_PICKER");
            }
        });

        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
            @Override
            public void onPositiveButtonClick(Long selection) {
                calendarDay[0] = String.valueOf(selection);
                calHeader[0] = materialDatePicker.getHeaderText();

                date.setText(calHeader[0]);
                totalCals.clear();
                getAllCalories(calendarDay);
            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home.this, Home.class);
                saveIntentExtra(intent, calendarDay, calHeader, breakfast);
                startActivity(intent);
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home.this, Profile.class);
                saveIntentExtra(intent, calendarDay, calHeader, breakfast);
                startActivity(intent);
            }
        });



//  --------------------------------------------------------------------------------------------------

        breakfastAddBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home.this, FoodList.class);
                saveIntentExtra(intent, calendarDay, calHeader, breakfast);
                startActivity(intent);
            }
        });

        lunchAddBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home.this, FoodList.class);
                saveIntentExtra(intent, calendarDay, calHeader, lunch);
                startActivity(intent);
            }
        });

        dinnerAddBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home.this, FoodList.class);
                saveIntentExtra(intent, calendarDay, calHeader, dinner);
                startActivity(intent);
            }
        });

        snackAddBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home.this, FoodList.class);
                saveIntentExtra(intent, calendarDay, calHeader, snack);
                startActivity(intent);
            }
        });

//  --------------------------------------------------------------------------------------------------

        fdb = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        userID = mAuth.getCurrentUser().getUid();

        totalCals = new ArrayList<String>();
        calories = new ArrayList<String>();

        totalCals.clear();
        getAllCalories(calendarDay);
    }

    public void saveIntentExtra(Intent intent, String[] calendarDay, String[] calHeader, String meal){
        intent.putExtra("meal", meal);
        intent.putExtra("date", calendarDay[0]);
        intent.putExtra("calHeader", calHeader[0]);
    }

    public void getAllCalories(String[] calendarDay){
        getCalories("Breakfast", calendarDay[0]);
        getCalories("Lunch", calendarDay[0]);
        getCalories("Dinner", calendarDay[0]);
        getCalories("Snack", calendarDay[0]);
    }

    public void getCalories(String checkMeal, String date){
        fdb.collection("users").document(userID)
                .collection("calendar").document(date)
                .collection(checkMeal)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        DecimalFormat df = new DecimalFormat();
                        df.setMaximumFractionDigits(2);

                        calories.clear();

                        for (DocumentSnapshot snapshot : task.getResult()) {
                            String cal = snapshot.getString("calories");
                            calories.add(cal);
                            totalCals.add(cal);
                        }

                        float sumCal = 0;
                        for (String number : calories) {
                            float n = Float.parseFloat(number);
                            sumCal += n;
                        }

//                        Toast.makeText(Home.this, "" + totalCals, Toast.LENGTH_SHORT).show();

                        switch (checkMeal){
                            case "Breakfast":
                                TextView breakfastCalories = findViewById(R.id.breakfastCalories);
                                breakfastCalories.setText("" + (df.format(sumCal)) + " Cal");
                                break;
                            case "Lunch":
                                TextView lunchCalories = findViewById(R.id.lunchCalories);
                                lunchCalories.setText("" + (df.format(sumCal)) + " Cal");
                                break;
                            case "Dinner":
                                TextView dinnerCalories = findViewById(R.id.dinnerCalories);
                                dinnerCalories.setText("" + (df.format(sumCal)) + " Cal");
                                break;
                            case "Snack":
                                TextView snackCalories = findViewById(R.id.snackCalories);
                                snackCalories.setText("" + (df.format(sumCal)) + " Cal");
                                break;
                            default:
                                throw new IllegalArgumentException("Invalid Meal" + checkMeal);
                        }

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
//        Toast.makeText(Home.this, "" + sumTotalCal, Toast.LENGTH_SHORT).show();

        TextView totalCalorie = findViewById(R.id.totalCalorie);
        totalCalorie.setText("" + (df.format(sumTotalCal)) + " Cal");
    }



}