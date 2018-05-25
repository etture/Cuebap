package com.jinoolee.cuebap.RecyclerViewItems;

import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class BuildingItem extends RecyclerViewItem{

    private int name;
    private int away;

    private int groupPosition;

    public List<RestaurantItem> items = new ArrayList<RestaurantItem>();

    public BuildingItem(int name, int away, int groupPosition){
        this.name = name;
        this.away = away;
        this.groupPosition = groupPosition;
        type = ITEM_BUILDING;
    }

    public int getName(){
        return name;
    }

    public int getAway(){
        return away;
    }

    public Object getItems(){
        return items;
    }

    public void addRestaurantItem(RestaurantItem item){
        item.setGroupPosition(groupPosition);
        item.setChildPosition(items.size());
        items.add(item);
    }

}
