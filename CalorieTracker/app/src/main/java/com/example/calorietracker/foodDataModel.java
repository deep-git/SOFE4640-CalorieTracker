package com.example.calorietracker;

public class foodDataModel {

    String itemName;
    String brandName;
    float calories;
    float fat;
    float protein;
    float carbs;

    public foodDataModel(String itemName, String brandName, float calories, float fat, float protein, float carbs) {
        this.itemName = itemName;
        this.brandName = brandName;
        this.calories = calories;
        this.fat = fat;
        this.protein = protein;
        this.carbs = carbs;
    }

    public String getItemName() {
        return itemName;
    }
    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getBrandName() {
        return brandName;
    }
    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public float getCalories() {
        return calories;
    }
    public void setCalories(float calories) {
        this.calories = calories;
    }

    public float getFat() {
        return fat;
    }
    public void setFat(float fat) {
        this.fat = fat;
    }

    public float getProtein() {
        return protein;
    }
    public void setProtein(float protein) {
        this.protein = protein;
    }

    public float getCarbs() {
        return carbs;
    }
    public void setCarbs(float carbs) {
        this.carbs = carbs;
    }


}
