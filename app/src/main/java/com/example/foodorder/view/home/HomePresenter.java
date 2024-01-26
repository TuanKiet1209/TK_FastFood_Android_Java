package com.example.foodorder.view.home;

import android.content.Context;

import androidx.annotation.NonNull;

import com.example.foodorder.ControllerApplication;
import com.example.foodorder.constant.GlobalFuntion;
import com.example.foodorder.model.Food;
import com.example.foodorder.utils.StringUtil;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomePresenter {

    private final HomeMVPView mHomeMVPView;

    public HomePresenter(HomeMVPView mHomeMVPView) {
        this.mHomeMVPView = mHomeMVPView;
    }

    public void getListFoodFromFirebase(Context context, String key) {
        if (context == null) {
            return;
        }
        ControllerApplication.get(context).getFoodDatabaseReference()
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Food> list = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Food food = dataSnapshot.getValue(Food.class);
                    if (food == null) {
                        return;
                    }

                    if (StringUtil.isEmpty(key)) {
                        list.add(0, food);
                    } else {
                        if (GlobalFuntion.getTextSearch(food.getName()).toLowerCase().trim()
                                .contains(GlobalFuntion.getTextSearch(key).toLowerCase().trim())) {
                            list.add(0, food);
                        }
                    }
                }
                mHomeMVPView.loadListFoodSuccess(list);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                mHomeMVPView.loadListFoodError();
            }
        });
    }

    public List<Food> getListFoodPopular(List<Food> listFood) {
        List<Food> list = new ArrayList<>();
        if (listFood == null || listFood.isEmpty()) {
            return list;
        }
        for (Food food : listFood) {
            if (food.isPopular()) {
                list.add(food);
            }
        }
        return list;
    }
}
