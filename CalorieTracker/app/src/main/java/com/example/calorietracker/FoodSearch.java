package com.example.calorietracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FoodSearch extends AppCompatActivity {

    ImageView back;
    ImageView search;
    EditText searchFood;
    RecyclerView rvCardList;
    TextView dashboardTitle;

    private ArrayList<foodDataModel> foodArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_search);

        back = findViewById(R.id.back);
        search = findViewById(R.id.search);
        searchFood = findViewById(R.id.searchFood);
        rvCardList = findViewById(R.id.rvCardList);
        dashboardTitle = findViewById(R.id.dashboardTitle);

        searchFood(""); //initialize
        Intent intent = getIntent();
        String checkMeal = intent.getStringExtra("meal");
        String date = intent.getStringExtra("date");

        dashboardTitle.setText("Search " + checkMeal);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FoodSearch.this, FoodList.class);
                intent.putExtra("meal", checkMeal);
                intent.putExtra("date", date);
                startActivity(intent);
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String input = searchFood.getText().toString();
                searchFood(input);
            }
        });
    }

    public void searchFood(String input){
        // Instantiate the RequestQueue.
        rvCardList = findViewById(R.id.rvCardList);
        foodArrayList.clear();
        RequestQueue queue = Volley.newRequestQueue(FoodSearch.this);

        String url = "https://api.nutritionix.com/v1_1/search/" + input
                + "?results=0%3A20&cal_min=0&cal_max=50000&"
                + "fields=item_name%2C"
                + "item_id%2C"
                + "brand_name%2C"
                + "brand_id%2C"
                + "nf_protein%2C"
                + "nf_calories%2C"
                + "nf_total_carbohydrate%2C"
                + "nf_total_fat&"
                + "appId=[93cd396f]&"
                + "appKey=[be3ca651707918c2a2839fc6460e4056—]";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, response -> {
            try {
                JSONArray hitArray = response.getJSONArray("hits");

                for (int i = 0; i < hitArray.length(); i++) {
                    JSONObject foodDetails = hitArray.getJSONObject(i);
                    JSONObject fields = foodDetails.getJSONObject("fields");

                    String itemName = fields.getString("item_name");
                    String brandName = fields.getString("brand_name");
                    String calories = fields.getString("nf_calories");
                    String fat = fields.getString("nf_total_fat");
                    String protein = fields.getString("nf_protein");
                    String carbs = fields.getString("nf_total_carbohydrate");

                    foodArrayList.add(new foodDataModel(itemName,brandName,calories,fat,protein,carbs));
                }

                Intent intent = getIntent();
                String checkMeal = intent.getStringExtra("meal");
                String date = intent.getStringExtra("date");

                foodSearchRecyclerAdapter foodAdapter = new foodSearchRecyclerAdapter(FoodSearch.this, foodArrayList, checkMeal, date);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(FoodSearch.this, LinearLayoutManager.VERTICAL, false);
                rvCardList.setLayoutManager(linearLayoutManager);
                rvCardList.setAdapter(foodAdapter);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(FoodSearch.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(request);
    }

}