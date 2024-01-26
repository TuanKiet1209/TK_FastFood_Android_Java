package com.example.foodorder.model;

import java.io.Serializable;

public class Order implements Serializable {

    private long id;
    private String name;
    private String phone;
    private String address;
    private int amount;
    private String foods;
    private int payment;

    public Order() {
    }

    public Order(long id, String name, String phone, String address, int amount, String foods, int payment) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.amount = amount;
        this.foods = foods;
        this.payment = payment;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getFoods() {
        return foods;
    }

    public void setFoods(String foods) {
        this.foods = foods;
    }

    public int getPayment() {
        return payment;
    }

    public void setPayment(int payment) {
        this.payment = payment;
    }
}
