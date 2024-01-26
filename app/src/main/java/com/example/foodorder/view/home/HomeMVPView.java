package com.example.foodorder.view.home;

import com.example.foodorder.model.Food;

import java.util.List;

public interface HomeMVPView {
    void loadListFoodSuccess(List<Food> list);
    void loadListFoodError();
}
