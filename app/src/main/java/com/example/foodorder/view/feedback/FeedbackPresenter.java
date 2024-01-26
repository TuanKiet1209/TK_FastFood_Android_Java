package com.example.foodorder.view.feedback;

import android.content.Context;

import androidx.annotation.NonNull;

import com.example.foodorder.ControllerApplication;
import com.example.foodorder.model.Feedback;

public class FeedbackPresenter {

    private final FeedbackMVPView mFeedbackMVPView;

    public FeedbackPresenter(FeedbackMVPView mFeedbackMVPView) {
        this.mFeedbackMVPView = mFeedbackMVPView;
    }

    public void sendFeedbackToFirebase(Context context, @NonNull Feedback feedback) {
        ControllerApplication.get(context).getFeedbackDatabaseReference()
                .child(String.valueOf(System.currentTimeMillis()))
                .setValue(feedback, (databaseError, databaseReference)
                        -> mFeedbackMVPView.sendFeedbackSuccess());
    }
}
