package com.example.foodorder.view.contact;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.foodorder.R;
import com.example.foodorder.adapter.ContactAdapter;
import com.example.foodorder.constant.GlobalFuntion;
import com.example.foodorder.databinding.FragmentContactBinding;
import com.example.foodorder.model.Contact;
import com.example.foodorder.view.BaseFragment;
import com.example.foodorder.view.main.MainActivity;

import java.util.List;

public class ContactFragment extends BaseFragment implements ContactMVPView {

    private FragmentContactBinding mFragmentContactBinding;
    private ContactPresenter mContactPresenter;
    private ContactAdapter mContactAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mFragmentContactBinding = FragmentContactBinding.inflate(inflater, container, false);

        mContactPresenter = new ContactPresenter(this);
        mContactPresenter.getListContacts();

        return mFragmentContactBinding.getRoot();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mContactAdapter.release();
    }

    @Override
    protected void initToolbar() {
        if (getActivity() != null) {
            ((MainActivity) getActivity()).setToolBar(false, getString(R.string.contact));
        }
    }

    @Override
    public void loadListContacts(List<Contact> list) {
        mContactAdapter = new ContactAdapter(getActivity(), list,
                () -> GlobalFuntion.callPhoneNumber(getActivity()));
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 3);
        mFragmentContactBinding.rcvData.setNestedScrollingEnabled(false);
        mFragmentContactBinding.rcvData.setFocusable(false);
        mFragmentContactBinding.rcvData.setLayoutManager(layoutManager);
        mFragmentContactBinding.rcvData.setAdapter(mContactAdapter);
    }
}
