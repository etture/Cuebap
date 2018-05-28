package com.jinoolee.cuebap.Data;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class CurrentOrder {

    private static final CurrentOrder currentOrder = new CurrentOrder();

    private int totalPrice;
    private int totalDiscount;
    private int totalDiscountedPrice;

    private boolean orderLater;
    private int orderHour, orderMinute, orderAmPm;
    private int waitingNumber;

    private ArrayList<Integer> itemNames;
    private ArrayList<Integer> itemPrices;

    private CurrentOrder(){}

    public static CurrentOrder getInstance(){
        return currentOrder;
    }

    public void setCurrentOrder(Bundle bundle){

        totalPrice = bundle.getInt("totalPrice");
        totalDiscount = bundle.getInt("totalDiscount");
        totalDiscountedPrice = bundle.getInt("totalDiscountedPrice");

        orderLater = bundle.getBoolean("orderLater");

        if(orderLater){

            orderHour = bundle.getInt("orderHour");
            orderMinute = bundle.getInt("orderMinute");
            orderAmPm = bundle.getInt("orderAmPm");

        }

        itemNames = bundle.getIntegerArrayList("itemNames");
        itemPrices = bundle.getIntegerArrayList("itemPrices");

        Random random = new Random();
        waitingNumber = random.nextInt(999) + 1;

    }

    public int getTotalPrice(){
        return totalPrice;
    }

    public int getTotalDiscount(){
        return totalDiscount;
    }

    public int getTotalDiscountedPrice(){
        return totalDiscountedPrice;
    }

    public boolean getOrderLater(){
        return orderLater;
    }

    public int getOrderHour(){
        return orderHour;
    }

    public int getOrderMinute(){
        return orderMinute;
    }

    public int getOrderAmPm(){
        return orderAmPm;
    }

    public ArrayList<Integer> getItemNames(){
        return itemNames;
    }

    public ArrayList<Integer> getItemPrices() {
        return itemPrices;
    }

    public int getWaitingNumber(){
        return waitingNumber;
    }
}
