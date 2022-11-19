package com.example.calorietracker;

import static android.content.ContentValues.TAG;

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
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class DisplayFoodAdapter extends RecyclerView.Adapter<DisplayFoodAdapter.MyViewHolder> {
    private BreakfastList activity;
    private List<foodDataModel> mList;
    FirebaseFirestore fStore;
    String userID;
    String breakfastID;
    String id;
    foodRecyclerAdapter breakfastAdapter;
    ArrayList<String> newBreakfastID = new ArrayList<String>();

    public DisplayFoodAdapter(BreakfastList activity, List<foodDataModel> mList) {
        this.activity = activity;
        this.mList = mList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        android.view.View view = LayoutInflater.from(activity).inflate(R.layout.cardview_remove_food, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.tvItemName.setText(mList.get(position).getItemName());
        holder.tvCalories.setText("Calories: " + mList.get(position).getCalories());
        holder.tvProteinCal.setText("Protein: " + mList.get(position).getProtein());
        holder.tvFatCal.setText("Fat: " + mList.get(position).getFat());
        holder.tvCarbsCal.setText("Carbs: " + mList.get(position).getCarbs());
        holder.tvBrandName.setText("Brand: " + mList.get(position).getBrandName());

        fStore = FirebaseFirestore.getInstance();

        holder.removeFood.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                breakfastID = breakfastAdapter.returnID(id);
                fStore.collection("users").document(userID).collection("breakfast").document(breakfastID).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "DocumentSnapshot successfully deleted!");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error deleting document", e);
                            }
                        });
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvItemName;
        TextView tvCalories;
        TextView tvProteinCal;
        TextView tvFatCal;
        TextView tvCarbsCal;
        TextView tvBrandName;
        Button removeFood;

        public MyViewHolder(android.view.View view) {
            super(view);

            tvItemName = itemView.findViewById(R.id.tvItemName);
            tvCalories = itemView.findViewById(R.id.tvCalories);
            tvProteinCal = itemView.findViewById(R.id.tvProteinCal);
            tvFatCal = itemView.findViewById(R.id.tvFatCal);
            tvCarbsCal = itemView.findViewById(R.id.tvCarbsCal);
            tvBrandName = itemView.findViewById(R.id.tvBrandName);
            removeFood = itemView.findViewById(R.id.removeFood);
        }
    }
}
