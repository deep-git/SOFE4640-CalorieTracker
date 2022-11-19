package com.example.calorietracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class BreakfastList extends AppCompatActivity {

    ImageView back;
    ImageView search;
    FirebaseFirestore fStore;
    String userID;
    RecyclerView rvDisplayBreakfast;
    private DisplayFoodAdapter displayFoodAdapter;
    private List<foodDataModel> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_breakfast_list);

        back = findViewById(R.id.back);
        search = findViewById(R.id.search);
        rvDisplayBreakfast = findViewById(R.id.rvDisplayBreakfast);
        rvDisplayBreakfast.setHasFixedSize(true);
        rvDisplayBreakfast.setLayoutManager(new LinearLayoutManager(this));

        fStore = FirebaseFirestore.getInstance();
        list = new ArrayList<>();
        displayFoodAdapter = new DisplayFoodAdapter(this, list);
        rvDisplayBreakfast.setAdapter(displayFoodAdapter);

        showData();

        back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BreakfastList.this, Home.class);
                startActivity(intent);
            }
        });

        search.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BreakfastList.this, FoodSearch.class);
                startActivity(intent);
            }
        });
    }

    private void showData() {
        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        fStore.collection("users")
                .document(userID)
                .collection("breakfast")
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
                displayFoodAdapter.notifyDataSetChanged();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(BreakfastList.this, "Error", Toast.LENGTH_SHORT).show();

            }
        });
    }
}