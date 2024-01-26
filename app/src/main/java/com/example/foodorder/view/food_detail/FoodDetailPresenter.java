package com.example.foodorder.view.food_detail;

import android.content.Context;

import com.example.foodorder.database.FoodDatabase;
import com.example.foodorder.model.Food;

import java.util.List;

public class FoodDetailPresenter {

    private final FoodDetailMVPView mFoodDetailMVPView;

    public FoodDetailPresenter(FoodDetailMVPView mFoodDetailMVPView) {
        this.mFoodDetailMVPView = mFoodDetailMVPView;
    }

    public boolean isFoodInCart(Context context, int foodId) {
        List<Food> list = FoodDatabase.getInstance(context).foodDAO().checkFoodInCart(foodId);
        return list != null && !list.isEmpty();
    }

    public void addFoodToCart(Context context, Food food) {
        FoodDatabase.getInstance(context).foodDAO().insertFood(food);
        mFoodDetailMVPView.addToCartSuccess();
    }
}
