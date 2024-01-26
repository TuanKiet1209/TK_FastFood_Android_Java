package com.example.foodorder.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodorder.constant.Constant;
import com.example.foodorder.databinding.ItemOrderBinding;
import com.example.foodorder.model.Order;
import com.example.foodorder.utils.DateTimeUtils;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private final List<Order> mListOrder;

    public OrderAdapter(List<Order> mListOrder) {
        this.mListOrder = mListOrder;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemOrderBinding itemOrderBinding = ItemOrderBinding.inflate(LayoutInflater.from(parent.getContext()),
                parent, false);
        return new OrderViewHolder(itemOrderBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = mListOrder.get(position);
        if (order == null) {
            return;
        }
        holder.mItemOrderBinding.tvId.setText(String.valueOf(order.getId()));
        holder.mItemOrderBinding.tvName.setText(order.getName());
        holder.mItemOrderBinding.tvPhone.setText(order.getPhone());
        holder.mItemOrderBinding.tvAddress.setText(order.getAddress());
        holder.mItemOrderBinding.tvMenu.setText(order.getFoods());
        holder.mItemOrderBinding.tvDate.setText(DateTimeUtils.convertTimeStampToDate(order.getId()));

        String strAmount = order.getAmount() + Constant.CURRENCY;
        holder.mItemOrderBinding.tvTotalAmount.setText(strAmount);

        String paymentMethod = "";
        if (Constant.TYPE_PAYMENT_CASH == order.getPayment()) {
            paymentMethod = Constant.PAYMENT_METHOD_CASH;
        }
        holder.mItemOrderBinding.tvPayment.setText(paymentMethod);
    }

    @Override
    public int getItemCount() {
        if (mListOrder != null) {
            return mListOrder.size();
        }
        return 0;
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {

        private final ItemOrderBinding mItemOrderBinding;

        public OrderViewHolder(@NonNull ItemOrderBinding itemOrderBinding) {
            super(itemOrderBinding.getRoot());
            this.mItemOrderBinding = itemOrderBinding;
        }
    }
}
