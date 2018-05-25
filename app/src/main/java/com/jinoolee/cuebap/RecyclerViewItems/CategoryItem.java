package com.jinoolee.cuebap.RecyclerViewItems;

import java.util.ArrayList;
import java.util.List;

public class CategoryItem extends RecyclerViewItem{

    int name;

    public List<FoodItem> items = new ArrayList<FoodItem>();

    public CategoryItem(int name){
        this.name = name;
        type = ITEM_CATEGORY;
    }

    public int getName(){
        return name;
    }

    public void addMenus(FoodItem... menus){
        for(FoodItem menu : menus){
            items.add(menu);
        }
    }

    public Object getFoodItems(){
        return items;
    }

}
