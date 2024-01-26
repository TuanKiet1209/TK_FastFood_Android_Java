package com.example.foodorder.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.foodorder.view.cart.CartFragment;
import com.example.foodorder.view.contact.ContactFragment;
import com.example.foodorder.view.feedback.FeedbackFragment;
import com.example.foodorder.view.home.HomeFragment;
import com.example.foodorder.view.order.OrderFragment;

public class MainViewPagerAdapter extends FragmentStateAdapter {

    public MainViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new HomeFragment();

            case 1:
                return new CartFragment();

            case 2:
                return new FeedbackFragment();

            case 3:
                return new ContactFragment();

            case 4:
                return new OrderFragment();

            default:
                return new HomeFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 5;
    }
}
