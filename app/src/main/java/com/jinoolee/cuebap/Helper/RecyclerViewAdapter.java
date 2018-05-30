package com.jinoolee.cuebap.Helper;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.jinoolee.cuebap.Data.CartSingleton;
import com.jinoolee.cuebap.R;
import com.jinoolee.cuebap.RecyclerViewItems.CartItem;
import com.jinoolee.cuebap.RecyclerViewItems.FoodItem;
import com.jinoolee.cuebap.RecyclerViewItems.OrderItem;
import com.jinoolee.cuebap.RecyclerViewItems.RecyclerViewItem;

import java.util.List;
import java.util.Locale;

public abstract class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "RecyclerViewAdapter";
    private List<RecyclerViewItem> mList;
    private Context mContext;
    private CartSingleton cart = CartSingleton.getInstance();

    //Language
    private String curLang;

    public RecyclerViewAdapter(Context context, List<RecyclerViewItem> list, String curLang){
        mContext = context;
        mList = list;
        this.curLang = curLang;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;

        if(viewType == RecyclerViewItem.ITEM_BUILDING){

        }else if(viewType == RecyclerViewItem.ITEM_RESTAURANT){

        }else if(viewType == RecyclerViewItem.ITEM_CATEGORY){

        }else if(viewType == RecyclerViewItem.ITEM_FOOD){

            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_item, parent, false);
            return new FoodHolder(view);

        }else if(viewType == RecyclerViewItem.ITEM_CART){

            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item, parent, false);
            return new CartItemHolder(view);

        }else if(viewType == RecyclerViewItem.ITEM_ORDER){

            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item, parent, false);
            return new OrderItemHolder(view);

        }

        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {

        RecyclerViewItem obj = mList.get(position);

        if(obj != null){

            if(obj.getType() == RecyclerViewItem.ITEM_BUILDING){

            }else if(obj.getType() == RecyclerViewItem.ITEM_RESTAURANT){

            }else if(obj.getType() == RecyclerViewItem.ITEM_CATEGORY){

            }else if(obj.getType() == RecyclerViewItem.ITEM_FOOD){

                /*
                FoodItem food = (FoodItem) obj;
                FoodHolder foodHolder = (FoodHolder) holder;

                foodHolder.image.setImageResource(food.getImage());
                foodHolder.name.setText(Resources.getSystem().getString(food.getName()));
                foodHolder.price.setText(Resources.getSystem().getString(R.string.menu_price, food.getPrice()));
                */

            }else if(obj.getType() == RecyclerViewItem.ITEM_CART){

                final CartItem cartItem = (CartItem) obj;
                CartItemHolder cartItemHolder = (CartItemHolder) holder;

                cartItemHolder.name.setText(Utils.getLangString(mContext, curLang, cartItem.getName()));
                cartItemHolder.price.setText(Utils.getLangString(mContext, curLang, R.string.menu_price, cartItem.getPrice()));
                cartItemHolder.image.setImageResource(cartItem.getImage());

                cartItemHolder.deleteBtn.setText(Utils.getLangString(mContext, curLang, R.string.delete));

                cartItemHolder.deleteBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showRemoveItemDialog(cartItem, position); //includes notifyDataSetChanged()
                    }
                });

            }else if(obj.getType() == RecyclerViewItem.ITEM_ORDER){

                final OrderItem orderItem = (OrderItem) obj;
                OrderItemHolder orderItemHolder = (OrderItemHolder) holder;

                orderItemHolder.name.setText(Utils.getLangString(mContext, curLang, orderItem.getName()));
                orderItemHolder.price.setText(Utils.getLangString(mContext, curLang, R.string.menu_price, orderItem.getPrice()));

            }
        }
    }

    @Override
    public int getItemCount() {
        if(mList == null){
            return 0;
        }else{
            return mList.size();
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(mList != null){
            RecyclerViewItem obj = mList.get(position);
            if(obj != null){
                return obj.getType();
            }
        }
        return 0;
    }

    public static class FoodHolder extends RecyclerView.ViewHolder{

        private ImageView image;
        private TextView name;
        private TextView price;

        public FoodHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.food_image_left);
            name = itemView.findViewById(R.id.food_name_left);
            price = itemView.findViewById(R.id.food_price_left);
        }
    }

    public static class CartItemHolder extends RecyclerView.ViewHolder{

        private TextView name;
        private TextView price;
        private TextView deleteBtn;
        private ImageView image;

        public CartItemHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.cart_item_name);
            price = itemView.findViewById(R.id.cart_item_price);
            deleteBtn = itemView.findViewById(R.id.cart_item_delete_btn);
            image = itemView.findViewById(R.id.cart_item_image);
        }
    }

    public static class OrderItemHolder extends RecyclerView.ViewHolder{

        private TextView name;
        private TextView price;

        public OrderItemHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.order_item_name);
            price = itemView.findViewById(R.id.order_item_price);
        }
    }

    private void showRemoveItemDialog(CartItem cartItem, int index){

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

        String foodName = Utils.getLangString(mContext, curLang, cartItem.getName());

        builder.setTitle(Utils.getLangString(mContext, curLang, R.string.remove_item));
        builder.setMessage(Utils.getLangString(mContext, curLang, R.string.do_you_want_to_remove, foodName));

        final String foodNameStr = foodName;
        final int position = index;

        builder.setPositiveButton(Utils.getLangString(mContext, curLang, R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                if(MyDebug.LOG){
                    Log.d(TAG, "Yes button clicked");
                }

                cart.removeItem(position);

                if(MyDebug.LOG){
                    Log.d(TAG, "Menu items in cart: " + cart.toString());
                }

                notifyDataSetChanged();

                onItemDeleted(); //Update total price display, method defined in activity
            }
        });

        builder.setNegativeButton(Utils.getLangString(mContext, curLang, R.string.no), new DialogInterface.OnClickListener() {
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

    public abstract void onItemDeleted();

}
