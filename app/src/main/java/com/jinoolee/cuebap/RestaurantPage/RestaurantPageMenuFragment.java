package com.jinoolee.cuebap.RestaurantPage;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jinoolee.cuebap.CheckOutPage.CartActivity;
import com.jinoolee.cuebap.Data.BuildingsAndRestaurants;
import com.jinoolee.cuebap.Data.CartSingleton;
import com.jinoolee.cuebap.Helper.AnimatedExpandableListView;
import com.jinoolee.cuebap.Helper.MyDebug;
import com.jinoolee.cuebap.Helper.Utils;
import com.jinoolee.cuebap.R;
import com.jinoolee.cuebap.RecyclerViewItems.BuildingItem;
import com.jinoolee.cuebap.RecyclerViewItems.CategoryItem;
import com.jinoolee.cuebap.RecyclerViewItems.FoodItem;
import com.jinoolee.cuebap.RecyclerViewItems.RestaurantItem;

import java.util.List;

public class RestaurantPageMenuFragment extends Fragment {

    private static final String TAG = "RestaurantPageMenu";

    //AnimatedExpandableListView variables
    private AnimatedExpandableListView restaurantPageListView;
    private RestaurantPageAdapter adapter;

    //Cart singleton variable
    CartSingleton cart;
    int leftOrRight = 0;
    private static final int LEFT = 0;
    private static final int RIGHT = 1;

    //On-screen elements
    private TextView cartBtn;

    //Current restaurant
    RestaurantItem currentRestaurant;

    //Language
    private String curLang;

    public static RestaurantPageMenuFragment createInstance(){
        RestaurantPageMenuFragment restaurantPageMenuFragment = new RestaurantPageMenuFragment();
        return restaurantPageMenuFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_restaurant_page_menu, container, false);

        //Get cart instance
        cart = CartSingleton.getInstance();

        //Main category and food items ListView settings

        Bundle receivedBundle = getArguments();
        int buildingIndex = receivedBundle.getInt("buildingIndex");
        int restaurantIndex = receivedBundle.getInt("restaurantIndex");
        curLang = receivedBundle.getString("curLang");

        BuildingsAndRestaurants bnr = new BuildingsAndRestaurants();
        List<BuildingItem> buildingItems = bnr.getBuildings();
        List<CategoryItem> items = (List<CategoryItem>)(((List<RestaurantItem>)(buildingItems
                .get(buildingIndex).getItems()))
                .get(restaurantIndex).getCategoryItems());

        //Current restaurant
        currentRestaurant = ((List<RestaurantItem>)(buildingItems
                .get(buildingIndex).getItems()))
                .get(restaurantIndex);

        adapter = new RestaurantPageAdapter(getContext());
        adapter.setData(items);

        restaurantPageListView = view.findViewById(R.id.restaurant_page_list_view);
        restaurantPageListView.setAdapter(adapter);
        restaurantPageListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View view, int groupPosition, long id) {
                if(restaurantPageListView.isGroupExpanded(groupPosition)){
                    restaurantPageListView.collapseGroupWithAnimation(groupPosition);
                }else{
                    restaurantPageListView.expandGroupWithAnimation(groupPosition);
                }
                return true;
            }
        });

        restaurantPageListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int groupPosition, int childPosition, long id) {

                if(MyDebug.LOG){
                    Log.d(TAG, "Two-item child view clicked: TRUE");
                }

                return true;
            }
        });

        //Bottom cart button settings
        cartBtn = view.findViewById(R.id.restaurant_page_cart_btn);
        cartBtn.setText(Utils.getLangString(getContext(), curLang, R.string.cart) + " " + cart.getItemCount());
        cartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), CartActivity.class);
                intent.putExtra("from", "restaurant_page");
                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        cartBtn.setText(Utils.getLangString(getContext(), curLang, R.string.cart) + " " + cart.getItemCount());
    }

    private static class CategoryHolder{
        TextView name;
        ImageView arrowUp;
        ImageView arrowDown;
    }

    private static class FoodHolder{
        ImageView image_left;
        TextView name_left;
        TextView price_left;
        LinearLayout item_left_layout;

        ImageView image_right;
        TextView name_right;
        TextView price_right;
        LinearLayout item_right_layout;
    }

    public class RestaurantPageAdapter extends AnimatedExpandableListView.AnimatedExpandableListAdapter{

        private LayoutInflater inflater;
        private List<CategoryItem> items;

        public RestaurantPageAdapter(Context context){
            inflater = LayoutInflater.from(context);
        }

        public void setData(List<CategoryItem> items){
            this.items = items;
        }

        @Override
        public View getRealChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

            FoodHolder holder;
            FoodItem item_left;
            FoodItem item_right;

            final int groupIndex = groupPosition;
            final int childIndex = childPosition;

            /* recycling makes views disappear, so disabling for now...
            if(convertView == null){
                holder = new FoodHolder();
                convertView = inflater.inflate(R.layout.food_item, parent, false);

                holder.name_left = convertView.findViewById(R.id.food_name_left);
                holder.price_left = convertView.findViewById(R.id.food_price_left);
                holder.image_left = convertView.findViewById(R.id.food_image_left);

                holder.name_right = convertView.findViewById(R.id.food_name_right);
                holder.price_right = convertView.findViewById(R.id.food_price_right);
                holder.image_right = convertView.findViewById(R.id.food_image_right);

                holder.right_item = convertView.findViewById(R.id.food_right_item);

                convertView.setTag(holder);
            }else{
                holder = (FoodHolder) convertView.getTag();
            }
            */

            holder = new FoodHolder();
            convertView = inflater.inflate(R.layout.food_item, parent, false);

            holder.name_left = convertView.findViewById(R.id.food_name_left);
            holder.price_left = convertView.findViewById(R.id.food_price_left);
            holder.image_left = convertView.findViewById(R.id.food_image_left);
            holder.item_left_layout = convertView.findViewById(R.id.food_item_left);

            holder.name_right = convertView.findViewById(R.id.food_name_right);
            holder.price_right = convertView.findViewById(R.id.food_price_right);
            holder.image_right = convertView.findViewById(R.id.food_image_right);
            holder.item_right_layout = convertView.findViewById(R.id.food_item_right);

            convertView.setTag(holder);

            //OnClickListener for each of the left and right items --> add FoodItem to cart
            holder.item_left_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    leftOrRight = LEFT;

                    if(MyDebug.LOG){
                        Log.d(TAG, "Menu item selected: LEFT");
                    }

                    FoodItem food = (FoodItem) getChild(groupIndex, childIndex*2);

                    showItemSelectedDialog(food);
                }
            });

            holder.item_right_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    leftOrRight = RIGHT;

                    if(MyDebug.LOG){
                        Log.d(TAG, "Menu item selected: RIGHT");
                    }

                    FoodItem food = (FoodItem) getChild(groupIndex, (childIndex*2)+1);

                    showItemSelectedDialog(food);
                }
            });

            //If the number of children is odd, remove the right item from sight
            int right_index = (childPosition*2)+1;
            int childrenCount = items.get(groupPosition).items.size() - 1;

            if(childPosition > (childrenCount/2)){ //if child index is greater than available view counts

                return convertView;

            }else if(right_index > childrenCount){ //if there are odd number of children

                item_left = (FoodItem) getChild(groupPosition, childPosition*2);
                item_right = null;

                holder.name_left.setText(Utils.getLangString(getContext(), curLang, item_left.getName()));
                holder.price_left.setText(Utils.getLangString(getContext(), curLang, R.string.menu_price, item_left.getPrice()));
                holder.image_left.setImageResource(item_left.getImage());

                holder.item_right_layout.setVisibility(View.GONE);

            }else{

                item_left = (FoodItem) getChild(groupPosition, childPosition*2);
                item_right = (FoodItem) getChild(groupPosition, (childPosition*2)+1);

                holder.name_left.setText(Utils.getLangString(getContext(), curLang, item_left.getName()));
                holder.price_left.setText(Utils.getLangString(getContext(), curLang, R.string.menu_price, item_left.getPrice()));
                holder.image_left.setImageResource(item_left.getImage());

                holder.name_right.setText(Utils.getLangString(getContext(), curLang, item_right.getName()));
                holder.price_right.setText(Utils.getLangString(getContext(), curLang, R.string.menu_price, item_right.getPrice()));
                holder.image_right.setImageResource(item_right.getImage());

            }

            if(MyDebug.LOG){
                Log.d(TAG, "Children count: " + Integer.toString(getRealChildrenCount(groupPosition)));
                Log.d(TAG, "Child index: " + Integer.toString(childPosition));
            }

            convertView.setClickable(false);
            return convertView;
        }

        @Override
        public int getRealChildrenCount(int groupPosition) {
            return ((items.get(groupPosition).items.size())+1) / 2;
        }

        @Override
        public int getGroupCount() {
            return items.size();
        }

        @Override
        public Object getGroup(int groupPosition) {
            return items.get(groupPosition);
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return items.get(groupPosition).items.get(childPosition);
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

            CategoryHolder holder;
            CategoryItem item = (CategoryItem) getGroup(groupPosition);

            if(convertView == null){
                holder = new CategoryHolder();
                convertView = inflater.inflate(R.layout.category_item, parent, false);
                holder.name = convertView.findViewById(R.id.category_name);
                holder.arrowUp = convertView.findViewById(R.id.category_arrow_up);
                holder.arrowDown = convertView.findViewById(R.id.category_arrow_down);
                convertView.setTag(holder);
            }else{
                holder = (CategoryHolder) convertView.getTag();
            }

            holder.name.setText(Utils.getLangString(getContext(), curLang, item.getName()));

            if(isExpanded){
                holder.arrowUp.setVisibility(View.VISIBLE);
                holder.arrowDown.setVisibility(View.INVISIBLE);
            }else{
                holder.arrowUp.setVisibility(View.INVISIBLE);
                holder.arrowDown.setVisibility(View.VISIBLE);
            }

            convertView.setClickable(false);
            return convertView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }

    private void showItemSelectedDialog(FoodItem foodItem){

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        String foodName = Utils.getLangString(getContext(), curLang, foodItem.getName());

        builder.setTitle(Utils.getLangString(getContext(), curLang, R.string.item_selected));
        builder.setMessage(Utils.getLangString(getContext(), curLang, R.string.do_you_want_to_add_to_cart, foodName));

        final String foodNameStr = foodName;
        final FoodItem food = foodItem;

        builder.setPositiveButton(Utils.getLangString(getContext(), curLang, R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                if(MyDebug.LOG){
                    Log.d(TAG, "Yes button clicked");
                }

                if(!cart.isEmpty()){

                    if(currentRestaurant.getName() != cart.getCurrentRestaurant().getName()){

                        showDifferentRestaurantDialog(food);
                        return;

                    }

                }

                cart.setCurrentRestaurant(currentRestaurant);
                cart.addItem(food);

                if(MyDebug.LOG){
                    Log.d(TAG, "Menu items in cart: " + cart.toString());
                }

                cartBtn.setText(Utils.getLangString(getContext(), curLang, R.string.cart) + " " + cart.getItemCount());

            }
        });

        builder.setNegativeButton(Utils.getLangString(getContext(), curLang, R.string.no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(MyDebug.LOG){
                    Log.d(TAG, "No button clicked");
                }
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

    }

    private void showDifferentRestaurantDialog(FoodItem foodItem){

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        String foodName = Utils.getLangString(getContext(), curLang, foodItem.getName());

        builder.setTitle(Utils.getLangString(getContext(), curLang, R.string.cuebap));
        builder.setMessage(Utils.getLangString(getContext(), curLang, R.string.order_from_new_restaurant));

        final String foodNameStr = foodName;
        final FoodItem food = foodItem;

        builder.setPositiveButton(Utils.getLangString(getContext(), curLang, R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                if(MyDebug.LOG){
                    Log.d(TAG, "Yes button clicked");
                }

                //First empty the original menus
                cart.empty();

                //Set to new restaurant
                cart.setCurrentRestaurant(currentRestaurant);

                cart.addItem(food);

                if(MyDebug.LOG){
                    Log.d(TAG, "Menu items in cart: " + cart.toString());
                }

                cartBtn.setText(Utils.getLangString(getContext(), curLang, R.string.cart) + " " + cart.getItemCount());

            }
        });

        builder.setNegativeButton(Utils.getLangString(getContext(), curLang, R.string.no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(MyDebug.LOG){
                    Log.d(TAG, "No button clicked");
                }
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

    }
}
