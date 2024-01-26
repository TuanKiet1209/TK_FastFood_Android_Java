package com.example.foodorder.view.order;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.foodorder.R;
import com.example.foodorder.adapter.OrderAdapter;
import com.example.foodorder.databinding.FragmentOrderBinding;
import com.example.foodorder.model.Order;
import com.example.foodorder.view.BaseFragment;
import com.example.foodorder.view.main.MainActivity;

import java.util.List;

public class OrderFragment extends BaseFragment implements OrderMVPView {

    private FragmentOrderBinding mFragmentOrderBinding;
    private OrderPresenter mOrderPresenter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mFragmentOrderBinding = FragmentOrderBinding.inflate(inflater, container, false);

        mOrderPresenter = new OrderPresenter(this);
        initView();

        mOrderPresenter.getListOrders(getActivity());

        return mFragmentOrderBinding.getRoot();
    }

    @Override
    protected void initToolbar() {
        if (getActivity() != null) {
            ((MainActivity) getActivity()).setToolBar(false, getString(R.string.order));
        }
    }

    private void initView() {
        if (getActivity() == null) {
            return;
        }
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mFragmentOrderBinding.rcvOrder.setLayoutManager(linearLayoutManager);
    }

    @Override
    public void loadListOrders(List<Order> list) {
        OrderAdapter orderAdapter = new OrderAdapter(list);
        mFragmentOrderBinding.rcvOrder.setAdapter(orderAdapter);
    }

    @Override
    public void loadDataError() {
        Toast.makeText(getActivity(), getString(R.string.msg_get_date_error), Toast.LENGTH_SHORT).show();
    }
}
