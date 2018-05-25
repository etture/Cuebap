package com.jinoolee.cuebap.RecyclerViewItems;

import com.jinoolee.cuebap.RecyclerViewItems.CategoryItem;

import java.util.ArrayList;
import java.util.List;

public class RestaurantItem extends RecyclerViewItem{

    private int name;
    private int startTime;
    private int endTime;
    private int waiting;
    private int image;

    private int groupPosition, childPosition;

    private List<CategoryItem> items = new ArrayList<>();

    public RestaurantItem(int name, int startTime, int endTime, int waiting, int image){
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
        this.waiting = waiting;
        this.image = image;
        type = ITEM_RESTAURANT;
    }

    public int getName(){
        return name;
    }

    public int getStartTime(){
        return startTime;
    }

    public int getEndTime(){
        return endTime;
    }

    public int getWaiting(){
        return waiting;
    }

    public int getImage(){
        return image;
    }

    public Object getCategoryItems(){
        return items;
    }

    public void addCategoryItem(CategoryItem item){
        items.add(item);
    }

    public CategoryItem getCategoryItem(int index){
        return items.get(index);
    }

    public void setGroupPosition(int position){
        this.groupPosition = position;
    }

    public int getGroupPosition(){
        return groupPosition;
    }

    public void setChildPosition(int position){
        this.childPosition = position;
    }

    public int getChildPosition(){
        return childPosition;
    }

}
