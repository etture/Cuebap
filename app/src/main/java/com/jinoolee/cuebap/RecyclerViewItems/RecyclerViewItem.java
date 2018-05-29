package com.jinoolee.cuebap.RecyclerViewItems;

public class RecyclerViewItem {

    public static final int ITEM_BUILDING = 0;
    public static final int ITEM_RESTAURANT = 1;
    public static final int ITEM_CATEGORY = 2;
    public static final int ITEM_FOOD = 3;
    public static final int ITEM_CART = 4;
    public static final int ITEM_ORDER = 5;

    protected int type;

    public RecyclerViewItem(){}

    public int getType(){
        return type;
    }

}
