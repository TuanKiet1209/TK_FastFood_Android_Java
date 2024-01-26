package com.example.foodorder.view.cart;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.foodorder.R;
import com.example.foodorder.adapter.CartAdapter;
import com.example.foodorder.constant.Constant;
import com.example.foodorder.constant.GlobalFuntion;
import com.example.foodorder.databinding.FragmentCartBinding;
import com.example.foodorder.event.ReloadListCartEvent;
import com.example.foodorder.model.Food;
import com.example.foodorder.model.Order;
import com.example.foodorder.utils.StringUtil;
import com.example.foodorder.view.BaseFragment;
import com.example.foodorder.view.main.MainActivity;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class CartFragment extends BaseFragment implements CartMVPView {

    private CartPresenter mCartPresenter;
    private FragmentCartBinding mFragmentCartBinding;
    private CartAdapter mCartAdapter;
    private List<Food> mListFoodCart;
    private BottomSheetDialog mBottomSheetDialog;
    private int mAmount;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mFragmentCartBinding = FragmentCartBinding.inflate(inflater, container, false);

        mCartPresenter = new CartPresenter(this);

        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        displayListFoodInCart();
        mFragmentCartBinding.tvOrderCart.setOnClickListener(v -> onClickOrderCart());

        return mFragmentCartBinding.getRoot();
    }

    @Override
    protected void initToolbar() {
        if (getActivity() != null) {
            ((MainActivity) getActivity()).setToolBar(false, getString(R.string.cart));
        }
    }

    private void displayListFoodInCart() {
        if (getActivity() == null) {
            return;
        }
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mFragmentCartBinding.rcvFoodCart.setLayoutManager(linearLayoutManager);
        DividerItemDecoration itemDecoration = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        mFragmentCartBinding.rcvFoodCart.addItemDecoration(itemDecoration);

        initDataFoodCart();
    }

    private void initDataFoodCart() {
        mListFoodCart = new ArrayList<>();
        mCartPresenter.getListFoodInCart(getActivity());
    }

    @Override
    public void loadListFoodInCart(List<Food> list) {
        mListFoodCart = list;
        if (mListFoodCart == null || mListFoodCart.isEmpty()) {
            return;
        }

        mCartAdapter = new CartAdapter(mListFoodCart, new CartAdapter.IClickListener() {
            @Override
            public void clickDeteteFood(Food food, int position) {
                deleteFoodFromCart(food, position);
            }

            @Override
            public void updateItemFood(Food food, int position) {
                mCartPresenter.updateFoodInCart(getActivity(), food, position);
            }
        });
        mFragmentCartBinding.rcvFoodCart.setAdapter(mCartAdapter);

        mCartPresenter.calculateTotalPrice(getActivity());
    }

    @Override
    public void loadCalculatePriceResult(String totalPrice, int amount) {
        mFragmentCartBinding.tvTotalPrice.setText(totalPrice);
        mAmount = amount;
    }

    @Override
    public void deleteFoodFromCartSuccess(int position) {
        mListFoodCart.remove(position);
        mCartAdapter.notifyItemRemoved(position);
        mCartPresenter.calculateTotalPrice(getActivity());
    }

    @Override
    public void updateFoodInCartSuccess(int position) {
        mCartAdapter.notifyItemChanged(position);
        mCartPresenter.calculateTotalPrice(getActivity());
    }

    @Override
    public void sendOderSuccess(Order order) {
        GlobalFuntion.showToastMessage(getActivity(), getString(R.string.msg_order_success));
        GlobalFuntion.hideSoftKeyboard(getActivity());
        mBottomSheetDialog.dismiss();

        mCartPresenter.deleteAllFoodInCart(getActivity());
        // Send email
        mCartPresenter.sendEmail(getActivity(), order);
    }

    @Override
    public void deleteAllFoodInCartSuccess() {
        clearCart();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void clearCart() {
        if (mListFoodCart != null) {
            mListFoodCart.clear();
        }
        mCartAdapter.notifyDataSetChanged();
        mCartPresenter.calculateTotalPrice(getActivity());
    }

    private void deleteFoodFromCart(Food food, int position) {
        new AlertDialog.Builder(getActivity())
                .setTitle(getString(R.string.confirm_delete_food))
                .setMessage(getString(R.string.message_delete_food))
                .setPositiveButton(getString(R.string.delete), (dialog, which) -> mCartPresenter.deleteFoodFromCart(getActivity(), food, position))
                .setNegativeButton(getString(R.string.dialog_cancel), (dialog, which) -> dialog.dismiss())
                .show();
    }

    public void onClickOrderCart() {
        if (getActivity() == null) {
            return;
        }

        if (mListFoodCart == null || mListFoodCart.isEmpty()) {
            return;
        }

        @SuppressLint("InflateParams") View viewDialog = getLayoutInflater().inflate(R.layout.layout_bottom_sheet_order, null);

        mBottomSheetDialog = new BottomSheetDialog(getActivity());
        mBottomSheetDialog.setContentView(viewDialog);
        mBottomSheetDialog.getBehavior().setState(BottomSheetBehavior.STATE_EXPANDED);

        // init ui
        TextView tvFoodsOrder = viewDialog.findViewById(R.id.tv_foods_order);
        TextView tvPriceOrder = viewDialog.findViewById(R.id.tv_price_order);
        TextView edtNameOrder = viewDialog.findViewById(R.id.edt_name_order);
        TextView edtPhoneOrder = viewDialog.findViewById(R.id.edt_phone_order);
        TextView edtAddressOrder = viewDialog.findViewById(R.id.edt_address_order);
        TextView tvCancelOrder = viewDialog.findViewById(R.id.tv_cancel_order);
        TextView tvCreateOrder = viewDialog.findViewById(R.id.tv_create_order);

        // Set data
        tvFoodsOrder.setText(mCartPresenter.getStringListFoodsOrder(getActivity(), mListFoodCart));
        tvPriceOrder.setText(mFragmentCartBinding.tvTotalPrice.getText().toString());

        // Set listener
        tvCancelOrder.setOnClickListener(v -> mBottomSheetDialog.dismiss());

        tvCreateOrder.setOnClickListener(v -> {
            String strName = edtNameOrder.getText().toString().trim();
            String strPhone = edtPhoneOrder.getText().toString().trim();
            String strAddress = edtAddressOrder.getText().toString().trim();

            if (StringUtil.isEmpty(strName) || StringUtil.isEmpty(strPhone) || StringUtil.isEmpty(strAddress)) {
                GlobalFuntion.showToastMessage(getActivity(), getString(R.string.message_enter_infor_order));
            } else {
                long id = System.currentTimeMillis();
                Order order = new Order(id, strName, strPhone, strAddress,
                        mAmount, mCartPresenter.getStringListFoodsOrder(getActivity(), mListFoodCart),
                        Constant.TYPE_PAYMENT_CASH);
                mCartPresenter.sendOrderToFirebase(getActivity(), id, order);
            }
        });

        mBottomSheetDialog.show();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ReloadListCartEvent event) {
        displayListFoodInCart();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }
}
