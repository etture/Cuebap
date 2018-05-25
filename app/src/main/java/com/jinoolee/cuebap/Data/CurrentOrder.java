package com.jinoolee.cuebap.Data;

import android.os.Bundle;

import java.util.ArrayList;

public class CurrentOrder {

    private static final CurrentOrder currentOrder = new CurrentOrder();

    private int totalPrice;
    private int totalDiscount;
    private int totalDiscountedPrice;

    private boolean orderLater;
    private int orderHour, orderMinute, orderAmPm;

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

    }

}
