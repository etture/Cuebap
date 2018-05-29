package com.jinoolee.cuebap.Data;

import android.os.Bundle;

import com.jinoolee.cuebap.RecyclerViewItems.OrderItem;
import com.jinoolee.cuebap.RecyclerViewItems.RecyclerViewItem;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class CurrentOrder {

    private static final CurrentOrder currentOrder = new CurrentOrder();

    private int totalPrice;
    private int totalDiscount;
    private int totalDiscountedPrice;

    private boolean orderLater;
    private int orderHour, orderMinute, orderAmPm;
    private int waitingNumber;

    private boolean orderPlaced;

    private ArrayList<Integer> itemNames;
    private ArrayList<Integer> itemPrices;
    private List<RecyclerViewItem> orderItems = new ArrayList<>();

    private CurrentOrder(){
        orderPlaced = false;
    }

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

        Random random = new Random();
        waitingNumber = random.nextInt(999) + 1;

        orderPlaced = true;

        itemNames = bundle.getIntegerArrayList("itemNames");
        itemPrices = bundle.getIntegerArrayList("itemPrices");

        orderItems.clear();

        for(int i = 0; i < itemNames.size(); i++){
            int name = itemNames.get(i);
            int price = itemPrices.get(i);
            OrderItem item = new OrderItem(name, price);
            orderItems.add(item);
        }

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

    public boolean isOrderPlaced(){
        return orderPlaced;
    }

    public List<RecyclerViewItem> getOrderItems(){
        return orderItems;
    }
}
