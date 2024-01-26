package com.example.foodorder.view.cart;

import android.content.Context;

import androidx.annotation.NonNull;

import com.example.foodorder.ControllerApplication;
import com.example.foodorder.R;
import com.example.foodorder.constant.ConfigEmail;
import com.example.foodorder.constant.Constant;
import com.example.foodorder.database.FoodDatabase;
import com.example.foodorder.model.Food;
import com.example.foodorder.model.Order;
import com.example.foodorder.utils.StringUtil;
import com.example.foodorder.utils.Utils;

import java.util.List;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class CartPresenter {

    private final CartMVPView mCartMVPView;

    public CartPresenter(CartMVPView mCartMVPView) {
        this.mCartMVPView = mCartMVPView;
    }

    public void getListFoodInCart(Context context) {
        List<Food> list = FoodDatabase.getInstance(context).foodDAO().getListFoodCart();
        mCartMVPView.loadListFoodInCart(list);
    }

    public void calculateTotalPrice(Context context) {
        List<Food> listFoodCart = FoodDatabase.getInstance(context).foodDAO().getListFoodCart();
        if (listFoodCart == null || listFoodCart.isEmpty()) {
            String strZero = 0 + Constant.CURRENCY;
            mCartMVPView.loadCalculatePriceResult(strZero, 0);
            return;
        }

        int totalPrice = 0;
        for (Food food : listFoodCart) {
            totalPrice = totalPrice + food.getTotalPrice();
        }

        String strTotalPrice = totalPrice + Constant.CURRENCY;
        mCartMVPView.loadCalculatePriceResult(strTotalPrice, totalPrice);
    }

    public void deleteFoodFromCart(Context context, Food food, int position) {
        FoodDatabase.getInstance(context).foodDAO().deleteFood(food);
        mCartMVPView.deleteFoodFromCartSuccess(position);
    }

    public void updateFoodInCart(Context context, Food food, int position) {
        FoodDatabase.getInstance(context).foodDAO().updateFood(food);
        mCartMVPView.updateFoodInCartSuccess(position);
    }

    public String getStringListFoodsOrder(Context context, List<Food> list) {
        if (list == null || list.isEmpty()) {
            return "";
        }
        String result = "";
        for (Food food : list) {
            if (StringUtil.isEmpty(result)) {
                result = "- " + food.getName() + " (" + food.getRealPrice() + Constant.CURRENCY + ") "
                        + "- " + context.getString(R.string.quantity) + " " + food.getCount();
            } else {
                result = result + "\n" + "- " + food.getName() + " (" + food.getRealPrice()
                        + Constant.CURRENCY + ") "
                        + "- " + context.getString(R.string.quantity) + " " + food.getCount();
            }
        }
        return result;
    }

    public void sendOrderToFirebase(Context context, long id, @NonNull Order order) {
        ControllerApplication.get(context).getBookingDatabaseReference()
                .child(Utils.getDeviceId(context))
                .child(String.valueOf(id))
                .setValue(order, (error1, ref1) -> mCartMVPView.sendOderSuccess(order));
    }

    public void deleteAllFoodInCart(Context context) {
        FoodDatabase.getInstance(context).foodDAO().deleteAllFood();
        mCartMVPView.deleteAllFoodInCartSuccess();
    }

    public void sendEmail(Context context, Order order) {
        try {
            Properties properties = System.getProperties();

            properties.put(ConfigEmail.MAIL_HOST_KEY, ConfigEmail.MAIL_HOST_VALUE);
            properties.put(ConfigEmail.MAIL_POST_KEY, ConfigEmail.MAIL_POST_VALUE);
            properties.put(ConfigEmail.MAIL_SSL_KEY, "true");
            properties.put(ConfigEmail.MAIL_AUTH_KEY, "true");

            javax.mail.Session session = Session.getInstance(properties, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(ConfigEmail.SENDER_EMAIL, ConfigEmail.PASSWORD_SENDER_EMAIL);
                }
            });

            MimeMessage mimeMessage = new MimeMessage(session);
            mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(ConfigEmail.RECEIVER_EMAIL));

            mimeMessage.setSubject(context.getString(R.string.app_name));
            String strMessage = context.getString(R.string.msg_email) + "\n"
                    + "Mã đơn hàng: " + order.getId() + "\n"
                    + "Tên khách hàng: " + order.getName() + "\n"
                    + "Số điện thoại khách hàng: " + order.getPhone() + "\n"
                    + "Địa chỉ khách hàng: " + order.getAddress() + "\n"
                    + "Tổng tiền: " + order.getAmount() + Constant.CURRENCY;
            mimeMessage.setText(strMessage);

            Thread thread = new Thread(() -> {
                try {
                    Transport.send(mimeMessage);
                } catch (MessagingException e) {
                    e.printStackTrace();
                }
            });
            thread.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
