package com.example.calorietracker;


import static android.content.ContentValues.TAG;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class foodSearchRecyclerAdapter extends RecyclerView.Adapter<foodSearchRecyclerAdapter.Viewholder> {

    private final Context context;
    private final ArrayList<foodDataModel> foodModelArrayList;
    private final String checkMeal;
    FirebaseAuth mAuth;

//    -------------------------------------------------------------------------------------------

    public foodSearchRecyclerAdapter(Context context, ArrayList<foodDataModel> foodModelArrayList, String checkMeal) {
        this.context = context;
        this.foodModelArrayList = foodModelArrayList;
        this.checkMeal = checkMeal;
    }

    @NonNull
    @Override
    public foodSearchRecyclerAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_add_food, parent, false);
        return new Viewholder(view);
    }

    @Override
    public int getItemCount() {
        return foodModelArrayList.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder{
        TextView tvItemName;
        TextView tvCalories;
        TextView tvProteinCal;
        TextView tvFatCal;
        TextView tvCarbsCal;
        TextView tvBrandName;
        Button addFood;


        public Viewholder(@NonNull View itemView) {
            super(itemView);

            tvItemName = itemView.findViewById(R.id.tvItemName);
            tvCalories = itemView.findViewById(R.id.tvCalories);
            tvProteinCal = itemView.findViewById(R.id.tvProteinCal);
            tvFatCal = itemView.findViewById(R.id.tvFatCal);
            tvCarbsCal = itemView.findViewById(R.id.tvCarbsCal);
            tvBrandName = itemView.findViewById(R.id.tvBrandName);
            addFood = itemView.findViewById(R.id.addFood);
        }
    }


    @Override
    public void onBindViewHolder(@NonNull foodSearchRecyclerAdapter.Viewholder holder, int position) {
        foodDataModel model = foodModelArrayList.get(position);
        holder.tvItemName.setText(model.getItemName());
        holder.tvCalories.setText("Calories: " + model.getCalories());
        holder.tvProteinCal.setText("Protein: " + model.getProtein());
        holder.tvFatCal.setText("Fat: " +model.getFat());
        holder.tvCarbsCal.setText("Carbs: " + model.getCarbs());
        holder.tvBrandName.setText("Brand: " + model.getBrandName());

        holder.addFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Map<String, Object> food = new HashMap<>();
                food.put("itemName", model.getItemName());
                food.put("brandName", model.getBrandName());
                food.put("calories", model.getCalories());
                food.put("fat", model.getFat());
                food.put("carbs", model.getCarbs());
                food.put("protein", model.getProtein());
                food.put("id", true);
                String userID;

                FirebaseFirestore fdb = FirebaseFirestore.getInstance();
                mAuth = FirebaseAuth.getInstance();
                userID = mAuth.getCurrentUser().getUid();
                fdb.collection("users").document(userID)
                        .collection(checkMeal)
                        .add(food)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                        String id = documentReference.getId();
                        documentReference.set(food);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);

                    }
                });

//               --- -------------------REALTIME DATABASE---------------------------
//                FirebaseDatabase db = FirebaseDatabase.getInstance();
//                DatabaseReference myRef = db.getReference();
//
//                myRef.child("breakfast").setValue(food).addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void unused) {
//                        Log.d("TAG","successfully added to db");
//                    }
//                });

            }

        });
    }

}