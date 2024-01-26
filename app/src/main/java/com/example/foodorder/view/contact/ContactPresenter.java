package com.example.foodorder.view.contact;

import com.example.foodorder.R;
import com.example.foodorder.model.Contact;

import java.util.ArrayList;
import java.util.List;

public class ContactPresenter {

    private final ContactMVPView mContactMVPView;

    public ContactPresenter(ContactMVPView mContactMVPView) {
        this.mContactMVPView = mContactMVPView;
    }

    public void getListContacts() {
        List<Contact> contactArrayList = new ArrayList<>();
        contactArrayList.add(new Contact(Contact.FACEBOOK, R.drawable.ic_facebook));
        contactArrayList.add(new Contact(Contact.HOTLINE, R.drawable.ic_hotline));
        contactArrayList.add(new Contact(Contact.GMAIL, R.drawable.ic_gmail));
        contactArrayList.add(new Contact(Contact.YOUTUBE, R.drawable.ic_youtube));
        contactArrayList.add(new Contact(Contact.ZALO, R.drawable.ic_zalo));

        mContactMVPView.loadListContacts(contactArrayList);
    }
}
