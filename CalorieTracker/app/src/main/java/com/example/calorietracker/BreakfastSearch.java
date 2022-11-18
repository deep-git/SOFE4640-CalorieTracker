package com.example.calorietracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class BreakfastSearch extends AppCompatActivity {

    ImageView back;
    ImageView search;
    EditText searchFood;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_breakfast_search);


        back = (ImageView) findViewById(R.id.back);
        search = (ImageView) findViewById(R.id.search);
        searchFood = (EditText) findViewById(R.id.searchFood);

        back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BreakfastSearch.this, BreakfastList.class);
                startActivity(intent);
            }
        });

        search.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // Instantiate the RequestQueue.
                RequestQueue queue = Volley.newRequestQueue(BreakfastSearch.this);
                String input = searchFood.getText().toString();
                String url = "https://api.nutritionix.com/v1_1/search/" + input + "?fields=item_name%2Citem_id%2Cbrand_name%2Cnf_calories%2Cnf_total_fat&appId=[98272eda]&appKey=[\n" +
                        "55f4c153b83df856d3fddacb2803b60f\t—\n]";

                // Request a string response from the provided URL.
                StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Toast.makeText(BreakfastSearch.this, response, Toast.LENGTH_SHORT).show();
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(BreakfastSearch.this, "Error occurred", Toast.LENGTH_SHORT).show();
                    }
                });

                // Add the request to the RequestQueue.
                queue.add(stringRequest);
            }
        });

    }
}