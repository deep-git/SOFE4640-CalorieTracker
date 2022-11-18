package com.example.calorietracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class BreakfastSearch extends AppCompatActivity {

    ImageView back;
    ImageView search;
    EditText searchFood;
    ListView foodNames;
    private ArrayList<String> items;
    private ArrayAdapter<String> itemsAdapter;

    ArrayList<String> foodName = new ArrayList<>();
    ArrayList<String> foodCalories = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_breakfast_search);


        back = (ImageView) findViewById(R.id.back);
        search = (ImageView) findViewById(R.id.search);
        searchFood = (EditText) findViewById(R.id.searchFood);
        foodNames = (ListView) findViewById(R.id.foodNames);

        itemsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, foodName);

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
                foodNames.setAdapter(null);
                foodName.clear();
                RequestQueue queue = Volley.newRequestQueue(BreakfastSearch.this);
                String input = searchFood.getText().toString();
                String url = "https://api.nutritionix.com/v1_1/search/" + input + "?fields=item_name%2Citem_id%2Cbrand_name%2Cnf_calories%2Cnf_total_fat&appId=[98272eda]&appKey=[55f4c153b83df856d3fddacb2803b60f—]";

                JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray hitArray = response.getJSONArray("hits");

                            for (int i = 0; i < hitArray.length(); i++) {
                                JSONObject foodDetails = hitArray.getJSONObject(i);
                                JSONObject fields = foodDetails.getJSONObject("fields");
                                foodName.add(fields.getString("item_name"));
                                foodCalories.add(fields.getString("nf_calories"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        foodNames.setAdapter(itemsAdapter);

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(BreakfastSearch.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                });

                queue.add(request);
            }
        });

    }
}