package com.example.foodorder.view.home;

import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.viewpager2.widget.ViewPager2;

import com.example.foodorder.R;
import com.example.foodorder.adapter.FoodGridAdapter;
import com.example.foodorder.adapter.FoodPopularAdapter;
import com.example.foodorder.constant.Constant;
import com.example.foodorder.constant.GlobalFuntion;
import com.example.foodorder.databinding.FragmentHomeBinding;
import com.example.foodorder.model.Food;
import com.example.foodorder.view.BaseFragment;
import com.example.foodorder.view.food_detail.FoodDetailActivity;
import com.example.foodorder.view.main.MainActivity;

import java.util.List;

public class HomeFragment extends BaseFragment implements HomeMVPView {

    private HomePresenter mHomePresenter;
    private FragmentHomeBinding mFragmentHomeBinding;

    private List<Food> mListFood;
    private List<Food> mListFoodPopular;

    private final Handler mHandlerBanner = new Handler();
    private final Runnable mRunnableBanner = new Runnable() {
        @Override
        public void run() {
            if (mListFoodPopular == null || mListFoodPopular.isEmpty()) {
                return;
            }
            if (mFragmentHomeBinding.viewpager2.getCurrentItem() == mListFoodPopular.size() - 1) {
                mFragmentHomeBinding.viewpager2.setCurrentItem(0);
                return;
            }
            mFragmentHomeBinding.viewpager2.setCurrentItem(mFragmentHomeBinding.viewpager2.getCurrentItem() + 1);
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mFragmentHomeBinding = FragmentHomeBinding.inflate(inflater, container, false);
        mHomePresenter = new HomePresenter(this);

        mHomePresenter.getListFoodFromFirebase(getActivity(), "");
        initListener();

        return mFragmentHomeBinding.getRoot();
    }

    @Override
    protected void initToolbar() {
        if (getActivity() != null) {
            ((MainActivity) getActivity()).setToolBar(true, getString(R.string.home));
        }
    }

    private void initListener() {
        mFragmentHomeBinding.edtSearchName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Do nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Do nothing
            }

            @Override
            public void afterTextChanged(Editable s) {
                String strKey = s.toString().trim();
                if (strKey.equals("") || strKey.length() == 0) {
                    if (mListFood != null) mListFood.clear();
                    mHomePresenter.getListFoodFromFirebase(getActivity(), "");
                }
            }
        });

        mFragmentHomeBinding.imgSearch.setOnClickListener(view -> searchFood());

        mFragmentHomeBinding.edtSearchName.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                searchFood();
                return true;
            }
            return false;
        });
    }

    private void displayListFoodPopular() {
        mListFoodPopular = mHomePresenter.getListFoodPopular(mListFood);
        FoodPopularAdapter foodPopularAdapter = new FoodPopularAdapter(mListFoodPopular, this::goToFoodDetail);
        mFragmentHomeBinding.viewpager2.setAdapter(foodPopularAdapter);
        mFragmentHomeBinding.indicator3.setViewPager(mFragmentHomeBinding.viewpager2);

        mFragmentHomeBinding.viewpager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                mHandlerBanner.removeCallbacks(mRunnableBanner);
                mHandlerBanner.postDelayed(mRunnableBanner, 3000);
            }
        });
    }

    private void displayListFoodSuggest() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        mFragmentHomeBinding.rcvFood.setLayoutManager(gridLayoutManager);

        FoodGridAdapter foodGridAdapter = new FoodGridAdapter(mListFood, this::goToFoodDetail);
        mFragmentHomeBinding.rcvFood.setAdapter(foodGridAdapter);
    }

    private void searchFood() {
        String strKey = mFragmentHomeBinding.edtSearchName.getText().toString().trim();
        if (mListFood != null) mListFood.clear();
        mHomePresenter.getListFoodFromFirebase(getActivity(), strKey);
        GlobalFuntion.hideSoftKeyboard(getActivity());
    }

    private void goToFoodDetail(@NonNull Food food) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.KEY_INTENT_FOOD_OBJECT, food);
        GlobalFuntion.startActivity(getActivity(), FoodDetailActivity.class, bundle);
    }

    @Override
    public void loadListFoodSuccess(List<Food> list) {
        mListFood = list;
        mFragmentHomeBinding.layoutContent.setVisibility(View.VISIBLE);
        displayListFoodPopular();
        displayListFoodSuggest();
    }

    @Override
    public void loadListFoodError() {
        GlobalFuntion.showToastMessage(getActivity(), getString(R.string.msg_get_date_error));
    }

    @Override
    public void onPause() {
        super.onPause();
        mHandlerBanner.removeCallbacks(mRunnableBanner);
    }

    @Override
    public void onResume() {
        super.onResume();
        mHandlerBanner.postDelayed(mRunnableBanner, 3000);
    }
}
