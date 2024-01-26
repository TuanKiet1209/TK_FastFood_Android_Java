package com.example.foodorder.view.cart;

import com.example.foodorder.model.Food;
import com.example.foodorder.model.Order;

import java.util.List;

public interface CartMVPView {
    void loadListFoodInCart(List<Food> list);
    void loadCalculatePriceResult(String totalPrice, int amount);
    void deleteFoodFromCartSuccess(int position);
    void updateFoodInCartSuccess(int position);
    void sendOderSuccess(Order order);
    void deleteAllFoodInCartSuccess();
}
