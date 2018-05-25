package com.jinoolee.cuebap.RecyclerViewItems;

public class FoodItem extends RecyclerViewItem{

    private int name;
    private int price;
    private int image;

    public FoodItem(int name, int price, int image){
        this.name = name;
        this.price = price;
        this.image = image;
        type = ITEM_FOOD;
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
