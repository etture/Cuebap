package com.jinoolee.cuebap.RecyclerViewItems;

public class CartItem extends RecyclerViewItem {

    private int name;
    private int price;
    private int image;

    public CartItem(int name, int price, int image){
        this.name = name;
        this.price = price;
        this.image = image;
        type = ITEM_CART;
    }

    public CartItem(FoodItem foodItem){
        this(foodItem.getName(), foodItem.getPrice(), foodItem.getImage());
        type = ITEM_CART;
    }

    public int getName(){
        return name;
    }

    public int getPrice(){
        return price;
    }

    public int getImage(){
        return image;
    }

}
