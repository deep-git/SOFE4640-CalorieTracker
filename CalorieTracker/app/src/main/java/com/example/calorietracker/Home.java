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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.units.qual.A;

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

        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar.clear();

        Long today = MaterialDatePicker.todayInUtcMilliseconds();

        MaterialDatePicker.Builder builder = MaterialDatePicker.Builder.datePicker();
        builder.setTitleText("Select a date");
        builder.setSelection(today);
        MaterialDatePicker<Long> materialDatePicker = builder.build();
        final String[] calendarDay = new String[1];
        calendarDay[0] = String.valueOf(today);

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
                calendarDay[0] = String.valueOf(selection);

                totalCals.clear();
                getCalories("Breakfast", calendarDay[0]);
                getCalories("Lunch", calendarDay[0]);
                getCalories("Dinner", calendarDay[0]);
                getCalories("Snack", calendarDay[0]);
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

//  --------------------------------------------------------------------------------------------------

        breakfastAddBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home.this, FoodList.class);
                intent.putExtra("meal", "Breakfast");
                intent.putExtra("date", calendarDay[0]);

                startActivity(intent);
            }
        });

        lunchAddBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home.this, FoodList.class);
                intent.putExtra("meal", "Lunch");
                intent.putExtra("date", calendarDay[0]);

                startActivity(intent);
            }
        });

        dinnerAddBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home.this, FoodList.class);
                intent.putExtra("meal", "Dinner");
                intent.putExtra("date", calendarDay[0]);

                startActivity(intent);
            }
        });

        snackAddBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home.this, FoodList.class);
                intent.putExtra("meal", "Snack");
                intent.putExtra("date", calendarDay[0]);

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

        //getTotalCals();
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