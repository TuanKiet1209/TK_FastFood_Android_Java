package com.example.foodorder.view.order;

import com.example.foodorder.model.Order;

import java.util.List;

public interface OrderMVPView {
    void loadListOrders(List<Order> list);
    void loadDataError();
}
