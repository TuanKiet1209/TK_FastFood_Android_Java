package com.example.foodorder.view.feedback;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.foodorder.R;
import com.example.foodorder.constant.GlobalFuntion;
import com.example.foodorder.databinding.FragmentFeedbackBinding;
import com.example.foodorder.model.Feedback;
import com.example.foodorder.utils.StringUtil;
import com.example.foodorder.view.BaseFragment;
import com.example.foodorder.view.main.MainActivity;

public class FeedbackFragment extends BaseFragment implements FeedbackMVPView {

    private FeedbackPresenter mFeedbackPresenter;
    private FragmentFeedbackBinding mFragmentFeedbackBinding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mFragmentFeedbackBinding = FragmentFeedbackBinding.inflate(inflater, container, false);
        mFeedbackPresenter = new FeedbackPresenter(this);

        mFragmentFeedbackBinding.tvSendFeedback.setOnClickListener(v -> onClickSendFeedback());

        return mFragmentFeedbackBinding.getRoot();
    }

    private void onClickSendFeedback() {
        if (getActivity() == null) {
            return;
        }
        MainActivity activity = (MainActivity) getActivity();

        String strName = mFragmentFeedbackBinding.edtName.getText().toString();
        String strPhone = mFragmentFeedbackBinding.edtPhone.getText().toString();
        String strEmail = mFragmentFeedbackBinding.edtEmail.getText().toString();
        String strComment = mFragmentFeedbackBinding.edtComment.getText().toString();

        if (StringUtil.isEmpty(strName)) {
            GlobalFuntion.showToastMessage(activity, getString(R.string.name_require));
        } else if (StringUtil.isEmpty(strComment)) {
            GlobalFuntion.showToastMessage(activity, getString(R.string.comment_require));
        } else {
            activity.showProgressDialog(true);
            Feedback feedback = new Feedback(strName, strPhone, strEmail, strComment);
            mFeedbackPresenter.sendFeedbackToFirebase(getActivity(), feedback);
        }
    }

    @Override
    protected void initToolbar() {
        if (getActivity() != null) {
            ((MainActivity) getActivity()).setToolBar(false, getString(R.string.feedback));
        }
    }

    @Override
    public void sendFeedbackSuccess() {
        if (getActivity() == null) {
            return;
        }
        MainActivity activity = (MainActivity) getActivity();
        activity.showProgressDialog(false);
        GlobalFuntion.hideSoftKeyboard(activity);
        GlobalFuntion.showToastMessage(activity, getString(R.string.send_feedback_success));
        mFragmentFeedbackBinding.edtName.setText("");
        mFragmentFeedbackBinding.edtPhone.setText("");
        mFragmentFeedbackBinding.edtEmail.setText("");
        mFragmentFeedbackBinding.edtComment.setText("");
    }
}
