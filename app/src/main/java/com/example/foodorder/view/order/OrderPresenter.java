package com.example.foodorder.view.order;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.foodorder.ControllerApplication;
import com.example.foodorder.model.Order;
import com.example.foodorder.utils.Utils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class OrderPresenter {

    private final OrderMVPView mOrderMVPView;

    public OrderPresenter(OrderMVPView mOrderMVPView) {
        this.mOrderMVPView = mOrderMVPView;
    }

    public void getListOrders(@Nullable Context context) {
        if (context == null) {
            return;
        }
        ControllerApplication.get(context).getBookingDatabaseReference().child(Utils.getDeviceId(context))
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        List<Order> list = new ArrayList<>();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Order order = dataSnapshot.getValue(Order.class);
                            list.add(0, order);
                        }
                        mOrderMVPView.loadListOrders(list);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        mOrderMVPView.loadDataError();
                    }
                });
    }
}
