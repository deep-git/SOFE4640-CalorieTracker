package com.example.calorietracker;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class foodRecyclerAdapter extends RecyclerView.Adapter<foodRecyclerAdapter.Viewholder> {

    private final Context context;
    private final ArrayList<foodDataModel> foodModelArrayList;

    public foodRecyclerAdapter(Context context, ArrayList<foodDataModel> foodModelArrayList) {
        this.context = context;
        this.foodModelArrayList = foodModelArrayList;
    }

    @NonNull
    @Override
    public foodRecyclerAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_food, parent, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull foodRecyclerAdapter.Viewholder holder, int position) {
        foodDataModel model = foodModelArrayList.get(position);
        holder.tvItemName.setText(model.getItemName());
        holder.tvCalories.setText("Calories: " + model.getCalories());
        holder.tvProteinCal.setText("Protein: " + model.getProtein());
        holder.tvFatCal.setText("Fat: " +model.getFat());
        holder.tvCarbsCal.setText("Carbs: " + model.getCarbs());
        holder.tvBrandName.setText("Brand: " + model.getBrandName());
    }

    @Override
    public int getItemCount() {
        return foodModelArrayList.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder{
        private final TextView tvItemName;
        private final TextView tvCalories;
        private final TextView tvProteinCal;
        private final TextView tvFatCal;
        private final TextView tvCarbsCal;
        private final TextView tvBrandName;


        public Viewholder(@NonNull View itemView) {
            super(itemView);

            tvItemName = itemView.findViewById(R.id.tvItemName);
            tvCalories = itemView.findViewById(R.id.tvCalories);
            tvProteinCal = itemView.findViewById(R.id.tvProteinCal);
            tvFatCal = itemView.findViewById(R.id.tvFatCal);
            tvCarbsCal = itemView.findViewById(R.id.tvCarbsCal);
            tvBrandName = itemView.findViewById(R.id.tvBrandName);

        }
    }
}