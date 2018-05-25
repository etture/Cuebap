package com.jinoolee.cuebap.Data;

import com.jinoolee.cuebap.R;
import com.jinoolee.cuebap.RecyclerViewItems.CartItem;
import com.jinoolee.cuebap.RecyclerViewItems.FoodItem;
import com.jinoolee.cuebap.RecyclerViewItems.RecyclerViewItem;
import com.jinoolee.cuebap.RecyclerViewItems.RestaurantItem;

import java.util.ArrayList;
import java.util.List;

public class CartSingleton {

    private static final CartSingleton cartSingleton = new CartSingleton();
    private static final RestaurantItem nullRestaurantItem = new RestaurantItem(R.string.no_restaurant_selected, 0, 0, 0, R.drawable.cuebap_logo_twolines_yellow);

    //private List<FoodItem> foodItems = new ArrayList<>();
    private List<CartItem> cartItems = new ArrayList<>();
    private List<RecyclerViewItem> recyclerViewItems = new ArrayList<>();
    private RestaurantItem currentRestaurant = nullRestaurantItem;

    //Coupon and points
    private List<Coupon> coupons = new ArrayList<>();
    private int points = 0;

    private CartSingleton(){

    }

    public static CartSingleton getInstance(){
        return cartSingleton;
    }

    public void addItem(FoodItem item){
        CartItem cartItem = new CartItem(item);
        cartItems.add(cartItem);
        recyclerViewItems.add(cartItem);
    }

    public void removeItem(int index){
        cartItems.remove(index);
        recyclerViewItems.remove(index);
    }

    public void empty(){
        cartItems.clear();
        recyclerViewItems.clear();

        currentRestaurant = nullRestaurantItem;
    }

    public boolean isEmpty(){
        return cartItems.isEmpty();
    }

    public int getItemCount(){
        return cartItems.size();
    }

    public List<RecyclerViewItem> getCartItems(){
        return recyclerViewItems;
    }

    public int getTotalPrice(){

        int totalPrice = 0;

        for(CartItem item : cartItems){
            totalPrice += item.getPrice();
        }

        return totalPrice;

    }

    public RestaurantItem getCurrentRestaurant() {
        return currentRestaurant;
    }

    public void setCurrentRestaurant(RestaurantItem restaurant){
        this.currentRestaurant = restaurant;
    }

    public String toString(){

        StringBuilder sb = new StringBuilder();
        int totalPrice = 0;

        for(CartItem item : cartItems){
            sb.append(item.getName());
            sb.append(", ");
            totalPrice += item.getPrice();
        }

        sb.append("TOTAL NUMBER = " + getItemCount() + ", ");
        sb.append("TOTAL PRICE = " + totalPrice + " won");

        return sb.toString();
    }

    public ArrayList<Integer> getItemNames(){

        ArrayList<Integer> itemNames = new ArrayList<>();

        for(CartItem item : cartItems){
            itemNames.add(item.getName());
        }

        return itemNames;

    }

    public ArrayList<Integer> getItemPrices(){

        ArrayList<Integer> itemPrices = new ArrayList<>();

        for(CartItem item : cartItems){
            itemPrices.add(item.getPrice());
        }

        return itemPrices;

    }

    public static RestaurantItem getNullRestaurantItem() {
        return nullRestaurantItem;
    }

    public int getPoints(){
        return points;
    }

    public int getNumCoupons(){
        return coupons.size();
    }

    public int getDiscountAmount(){

        int totalDiscount = 0;

        for(Coupon c : coupons){
            totalDiscount += c.getAmount();
        }

        totalDiscount += getPoints();

        return totalDiscount;

    }

    public int getTotalDiscountedAmount(){

        return getTotalPrice() - getDiscountAmount();

    }
}
