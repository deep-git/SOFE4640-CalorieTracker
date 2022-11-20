package com.example.calorietracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class FoodList extends AppCompatActivity {

    ImageView back;
    ImageView search;
    FirebaseFirestore fStore;
    String userID;
    RecyclerView rvDisplayBreakfast;
    TextView dashboardTitle;

    private DisplayFoodAdapter displayFoodAdapter;
    private List<foodDataModel> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_breakfast_list);

        back = findViewById(R.id.back);
        search = findViewById(R.id.search);
        dashboardTitle = findViewById(R.id.dashboardTitle);

        rvDisplayBreakfast = findViewById(R.id.rvDisplayBreakfast);
        rvDisplayBreakfast.setHasFixedSize(true);
        rvDisplayBreakfast.setLayoutManager(new LinearLayoutManager(this));

        Intent intent = getIntent();
        String checkMeal = intent.getStringExtra("meal");
        String calendarDay = intent.getStringExtra("date");
        String calHeader = intent.getStringExtra("calHeader");

        list = new ArrayList<>();

        fStore = FirebaseFirestore.getInstance();
        displayFoodAdapter = new DisplayFoodAdapter(this, list, checkMeal, calendarDay);
        rvDisplayBreakfast.setAdapter(displayFoodAdapter);

        switch (checkMeal){
            case "Breakfast":
            case "Lunch":
            case "Dinner":
            case "Snack":
                dashboardTitle.setText(checkMeal + " List");
                showData(checkMeal, calendarDay);
                break;
            default:
                throw new IllegalArgumentException("Invalid Meal" + checkMeal);
        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FoodList.this, Home.class);
                saveIntentExtra(intent, calendarDay, calHeader, checkMeal);
                startActivity(intent);
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FoodList.this, FoodSearch.class);
                saveIntentExtra(intent, calendarDay, calHeader, checkMeal);
                startActivity(intent);
            }
        });

    }

    public void saveIntentExtra(Intent intent, String calendarDay, String calHeader, String meal){
        intent.putExtra("meal", meal);
        intent.putExtra("date", calendarDay);
        intent.putExtra("calHeader", calHeader);
    }

    private void showData(String meal, String date) {
        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        fStore.collection("users").document(userID)
                .collection("calendar").document(date)
                .collection(meal)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (DocumentSnapshot snapshot : task.getResult()) {
                        foodDataModel foodData = new foodDataModel(
                                snapshot.getString("itemName"),
                                snapshot.getString("brandName"),
                                snapshot.getString("calories"),
                                snapshot.getString("fat"),
                                snapshot.getString("protein"),
                                snapshot.getString("carbs"));
                        list.add(foodData);
                }

                Intent intent = getIntent();
                String checkMeal = intent.getStringExtra("meal");
                String date = intent.getStringExtra("date");

                displayFoodAdapter = new DisplayFoodAdapter(FoodList.this, list, checkMeal, date);
                rvDisplayBreakfast.setAdapter(displayFoodAdapter);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(FoodList.this, "Error", Toast.LENGTH_SHORT).show();

            }
        });
    }
}