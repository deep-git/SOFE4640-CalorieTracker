package com.example.calorietracker;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class BreakfastList extends AppCompatActivity {

    ImageView back;
    ImageView search;
    FirebaseAuth mAuth;
    FirebaseFirestore fStore;
    String userID;
    String breakfastID;
    ListView displayBreakfast;
    ArrayList foodArrayList = new ArrayList<>();
    private ArrayAdapter<String> itemsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_breakfast_list);

        back = findViewById(R.id.back);
        search = findViewById(R.id.search);
        displayBreakfast = findViewById(R.id.displayBreakfast);
        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        //String userID = mAuth.getCurrentUser().getUid();
        //breakfastID = fStore.collection("users").document(userID).collection("breakfast").getId();

        //itemsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, foodArrayList);

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

        /*
        if (foodArrayList != null) {
            getListItems();
            displayBreakfast.setAdapter(itemsAdapter);
        }
         */
    }

    /*
    private void getListItems() {
        fStore.collection("users").document(userID).collection("breakfast").document(breakfastID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String itemName = Objects.requireNonNull(documentSnapshot.get("item_name")).toString();
                String brandName = documentSnapshot.get("brand_name").toString();
                String calories = documentSnapshot.get("nf_calories").toString();
                String fat = documentSnapshot.get("nf_total_fat").toString();
                String protein = documentSnapshot.get("nf_protein").toString();
                String carbs = documentSnapshot.get("nf_total_carbohydrate").toString();

                foodArrayList.add(itemName);

            }
        });
    }
     */
}