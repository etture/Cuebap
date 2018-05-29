package com.jinoolee.cuebap.RecyclerViewItems;

public class OrderItem extends RecyclerViewItem {

    private int name;
    private int price;

    public OrderItem(int name, int price){
        this.name = name;
        this.price = price;
        type = ITEM_ORDER;
    }

    public int getName(){
        return name;
    }

    public int getPrice(){
        return price;
    }

}
