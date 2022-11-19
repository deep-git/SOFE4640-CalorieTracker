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

public class foodListRecyclerAdapter extends RecyclerView.Adapter<foodListRecyclerAdapter.Viewholder> {
    private final Context context;
    private final ArrayList<foodDataModel> foodModelArrayList;
    FirebaseAuth mAuth;

//    -------------------------------------------------------------------------------------------

    public foodListRecyclerAdapter(Context context, ArrayList<foodDataModel> foodModelArrayList) {
        this.context = context;
        this.foodModelArrayList = foodModelArrayList;
    }

    @NonNull
    @Override
    public foodListRecyclerAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_remove_food, parent, false);
        return new foodListRecyclerAdapter.Viewholder(view);
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
        Button removeFood;


        public Viewholder(@NonNull View itemView) {
            super(itemView);

            tvItemName = itemView.findViewById(R.id.tvItemName);
            tvCalories = itemView.findViewById(R.id.tvCalories);
            tvProteinCal = itemView.findViewById(R.id.tvProteinCal);
            tvFatCal = itemView.findViewById(R.id.tvFatCal);
            tvCarbsCal = itemView.findViewById(R.id.tvCarbsCal);
            tvBrandName = itemView.findViewById(R.id.tvBrandName);
            removeFood = itemView.findViewById(R.id.removeFood);

        }

    }

    @Override
    public void onBindViewHolder(@NonNull foodListRecyclerAdapter.Viewholder holder, int position) {
        foodDataModel model = foodModelArrayList.get(position);
        holder.tvItemName.setText(model.getItemName());
        holder.tvCalories.setText("Calories: " + model.getCalories());
        holder.tvProteinCal.setText("Protein: " + model.getProtein());
        holder.tvFatCal.setText("Fat: " +model.getFat());
        holder.tvCarbsCal.setText("Carbs: " + model.getCarbs());
        holder.tvBrandName.setText("Brand: " + model.getBrandName());

        holder.removeFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Map<String, Object> food = new HashMap<>();
                food.put("itemName", model.getItemName());
                food.put("brandName", model.getBrandName());
                food.put("calories", model.getCalories());
                food.put("fat", model.getFat());
                food.put("carbs", model.getCarbs());
                food.put("protein", model.getProtein());
                String userID;

                FirebaseFirestore fdb = FirebaseFirestore.getInstance();
                mAuth = FirebaseAuth.getInstance();
                userID = mAuth.getCurrentUser().getUid();
                fdb.collection("users").document(userID)
                        .collection("breakfast")
                        .add(food)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error adding document", e);

                            }
                        });
            }

        });
    }

}
