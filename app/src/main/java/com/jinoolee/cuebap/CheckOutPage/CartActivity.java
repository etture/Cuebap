package com.jinoolee.cuebap.CheckOutPage;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.jinoolee.cuebap.BaseActivity;
import com.jinoolee.cuebap.Data.CartSingleton;
import com.jinoolee.cuebap.Data.CurrentOrder;
import com.jinoolee.cuebap.Helper.RecyclerViewAdapter;
import com.jinoolee.cuebap.Helper.Utils;
import com.jinoolee.cuebap.R;
import com.jinoolee.cuebap.RecyclerViewItems.FoodItem;
import com.jinoolee.cuebap.RecyclerViewItems.RestaurantItem;
import com.jinoolee.cuebap.RestaurantPage.RestaurantActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CartActivity extends BaseActivity {

    private static final String TAG = "CartActivity";
    private static final String KEY_LAYOUT_MANAGER = "layoutManager";
    private static final int SPAN_COUNT = 2;

    private enum LayoutManagerType {
        GRID_LAYOUT_MANAGER,
        LINEAR_LAYOUT_MANAGER
    }

    protected LayoutManagerType mCurrentLayoutManagerType;
    protected RecyclerView mRecyclerView;
    protected RecyclerViewAdapter mAdapter;
    protected RecyclerView.LayoutManager mLayoutManager;

    private CartSingleton cart;

    private TextView totalPriceDisplay;
    private TextView sumPriceDisplay;
    private RelativeLayout noItemScreen;
    private RelativeLayout yesItemScreen;
    private TextView currentRestaurantText;
    private TextView payBtn, addMenusBtn;
    private TextView emptyBtn;

    private RadioGroup pickUpRadioGroup;
    private RadioButton orderImmediatelyRadio, orderLaterRadio;
    private RadioGroup paymentOptionRadioGroup;
    private RadioButton simplePaymentRadio, creditCardRadio;

    private TextView pointsDisplay, couponsDisplay, discountDisplay;

    private Intent fromIntent;

    private Context mContext;
    private PopupWindow orderLaterPopup;
    private RelativeLayout layout;
    private TextView orderLaterScheduleText;
    private int orderHour, orderMinute;
    private int orderAmPm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        cart = CartSingleton.getInstance();
        fromIntent = getIntent();

        mContext = getApplicationContext();
        layout = findViewById(R.id.cart_page_layout);

        totalPriceDisplay = findViewById(R.id.cart_page_total_price_display);
        sumPriceDisplay = findViewById(R.id.cart_page_sum_display);
        noItemScreen = findViewById(R.id.cart_page_no_items);
        yesItemScreen = findViewById(R.id.cart_page_yes_items);
        currentRestaurantText = findViewById(R.id.cart_page_current_restaurant);
        payBtn = findViewById(R.id.cart_page_pay_btn);
        addMenusBtn = findViewById(R.id.cart_page_add_menus_btn);
        emptyBtn = findViewById(R.id.cart_page_empty_btn);

        pickUpRadioGroup = findViewById(R.id.cart_page_pick_up_radio_group);
        orderImmediatelyRadio = findViewById(R.id.cart_page_order_immediately_radio);
        orderLaterRadio = findViewById(R.id.cart_page_order_later_radio);
        paymentOptionRadioGroup = findViewById(R.id.cart_page_payment_option_radio_group);
        simplePaymentRadio = findViewById(R.id.cart_page_simple_payment_radio);
        creditCardRadio = findViewById(R.id.cart_page_credit_card_radio);

        pointsDisplay = findViewById(R.id.cart_page_total_points_point_display);
        couponsDisplay = findViewById(R.id.cart_page_total_points_coupon_display);
        discountDisplay = findViewById(R.id.cart_page_total_points_discount_display);
        orderLaterScheduleText = findViewById(R.id.order_later_schedule_text);

        //Toolbar setup
        Toolbar toolbar = findViewById(R.id.cart_page_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();

        //Set toolbar text
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(R.layout.actionbar_layout);
        TextView actionBarTitle = actionBar.getCustomView().findViewById(R.id.actionbar_title);
        actionBarTitle.setText(Utils.getLangString(this, curLang, R.string.cart));

        //Put back button in toolbar
        actionBar.setDisplayHomeAsUpEnabled(true);

        //RecyclerView setup
        mRecyclerView = findViewById(R.id.cart_page_list);
        mLayoutManager = new LinearLayoutManager(this);
        mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
        if (savedInstanceState != null) {
            // Restore saved layout manager type.
            mCurrentLayoutManagerType = (LayoutManagerType) savedInstanceState
                    .getSerializable(KEY_LAYOUT_MANAGER);
        }
        setRecyclerViewLayoutManager(mCurrentLayoutManagerType);

        mAdapter = new RecyclerViewAdapter(this, cart.getCartItems(), curLang) {
            @Override
            public void onItemDeleted() {
                updateUI();
            }
        }; //Data input, define method updating total price display when item is deleted from the cart page

        mRecyclerView.setAdapter(mAdapter);

        //Set vertical divider spacing between items in RecyclerView
        RecyclerView.ItemDecoration dividerDecoration = new VerticalSpaceItemDecoration(20);
        mRecyclerView.addItemDecoration(dividerDecoration);

        //Update initial screen
        updateUI();

        //Bottom button setup
        addMenusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fromIntent.getStringExtra("from").equals("restaurant_page")){

                    finish();

                }else if(fromIntent.getStringExtra("from").equals("main_page")){

                    RestaurantItem curRestaurant = cart.getCurrentRestaurant();

                    Intent intent = new Intent(getApplicationContext(), RestaurantActivity.class);
                    intent.putExtra("restaurant_name", curRestaurant.getName());
                    intent.putExtra("restaurant_image", curRestaurant.getImage());
                    intent.putExtra("buildingIndex", curRestaurant.getGroupPosition());
                    intent.putExtra("restaurantIndex", curRestaurant.getChildPosition());
                    startActivity(intent);

                    finish();

                }
            }
        });

        payBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();

                Intent intent = new Intent(getApplicationContext(), CheckOutActivity.class);

                ArrayList<Integer> itemNames = cart.getItemNames();
                ArrayList<Integer> itemPrices = cart.getItemPrices();

                bundle.putIntegerArrayList("itemNames", itemNames);
                bundle.putIntegerArrayList("itemPrices", itemPrices);

                bundle.putInt("totalPrice", cart.getTotalPrice());
                bundle.putInt("totalDiscount", cart.getDiscountAmount());
                bundle.putInt("totalDiscountedPrice", cart.getTotalDiscountedAmount());


                if(orderLaterRadio.isChecked()){

                    bundle.putBoolean("orderLater", true);
                    bundle.putInt("orderHour", orderHour);
                    bundle.putInt("orderMinute", orderMinute);
                    bundle.putInt("orderAmPm", orderAmPm);

                }else{

                    bundle.putBoolean("orderLater", false);

                }

                CurrentOrder currentOrder = CurrentOrder.getInstance();
                currentOrder.setCurrentOrder(bundle);

                cart.empty();

                try{
                    showProgressDialog();
                    Thread.sleep(200);
                }catch(InterruptedException e){}

                hideProgressDialog();

                startActivity(intent);

            }
        });

        //Empty button for emptying the cart
        emptyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showEmptyCartDialog();

            }
        });

        //When the order-later button is pressed, bring up a popup window for getting the time
        orderImmediatelyRadio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked){
                    orderLaterScheduleText.setVisibility(View.GONE);
                }

            }
        });

        orderLaterRadio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked){

                    LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);

                    final View popupView = inflater.inflate(R.layout.order_later_time_picker_layout, null);

                    orderLaterPopup = new PopupWindow(
                            popupView,
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                    );

                    if(Build.VERSION.SDK_INT>=21){
                        orderLaterPopup.setElevation(5.0f);
                    }

                    final TimePicker orderLaterTimePicker = popupView.findViewById(R.id.order_later_time_picker);
                    Button orderLaterCancelBtn = popupView.findViewById(R.id.order_later_cancel_btn);
                    Button orderLaterSetBtn = popupView.findViewById(R.id.order_later_set_btn);

                    orderLaterCancelBtn.setText(Utils.getLangString(mContext, curLang, R.string.cancel));
                    orderLaterSetBtn.setText(Utils.getLangString(mContext, curLang, R.string.set));


                    orderLaterCancelBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            orderLaterPopup.dismiss();
                            orderImmediatelyRadio.toggle();
                        }
                    });

                    orderLaterSetBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            orderHour = orderLaterTimePicker.getCurrentHour();
                            orderMinute = orderLaterTimePicker.getCurrentMinute();

                            if (orderHour == 0) {
                                orderHour += 12;
                                orderAmPm = R.string.am;
                            } else if (orderHour == 12) {
                                orderAmPm = R.string.pm;
                            } else if (orderHour > 12) {
                                orderHour -= 12;
                                orderAmPm = R.string.pm;
                            } else {
                                orderAmPm = R.string.am;
                            }

                            String amPmStr = Utils.getLangString(mContext, curLang, orderAmPm);

                            orderLaterScheduleText.setText(Utils.getLangString(mContext, curLang,
                                    R.string.order_later_time, orderHour, orderMinute, amPmStr));
                            orderLaterScheduleText.setVisibility(View.VISIBLE);

                            orderLaterPopup.dismiss();

                        }
                    });

                    orderLaterPopup.showAtLocation(layout, Gravity.CENTER, 0, 0);

                }
            }
        });

        layoutElementStringSetup();

    }

    @Override
    protected void layoutElementStringSetup() {

        emptyBtn.setText(Utils.getLangString(this, curLang, R.string.empty));
        payBtn.setText(Utils.getLangString(this, curLang, R.string.pay));
        addMenusBtn.setText(Utils.getLangString(this, curLang, R.string.add_menus));
        orderImmediatelyRadio.setText(Utils.getLangString(this, curLang, R.string.order_immediately));
        orderLaterRadio.setText(Utils.getLangString(this, curLang, R.string.order_later));
        simplePaymentRadio.setText(Utils.getLangString(this, curLang, R.string.simple_payment));
        creditCardRadio.setText(Utils.getLangString(this, curLang, R.string.credit_card));

        TextView orderList, pickUp, paymentOption, totalPrice, totalPriceBelow, nothingInCart, orderMethod;
        orderList = findViewById(R.id.cart_page_order_list_text);
        pickUp = findViewById(R.id.cart_page_pick_up_text);
        paymentOption = findViewById(R.id.cart_page_payment_option_text);
        totalPrice = findViewById(R.id.cart_page_total_price_text);
        totalPriceBelow = findViewById(R.id.cart_page_total_price_bottom_text);
        nothingInCart = findViewById(R.id.cart_page_nothing_in_cart);
        orderMethod = findViewById(R.id.cart_page_order_method_text);

        orderList.setText(Utils.getLangString(this, curLang, R.string.order_list));
        pickUp.setText(Utils.getLangString(this, curLang, R.string.pick_up));
        paymentOption.setText(Utils.getLangString(this, curLang, R.string.payment_option));
        totalPrice.setText(Utils.getLangString(this, curLang, R.string.total_price));
        totalPriceBelow.setText(Utils.getLangString(this, curLang, R.string.total_price));
        nothingInCart.setText(Utils.getLangString(this, curLang, R.string.nothing_in_cart));
        orderMethod.setText(Utils.getLangString(this, curLang, R.string.order_method));

        TextView pointText, couponText, discountText, sumText;
        pointText = findViewById(R.id.cart_page_total_points_point_text);
        couponText = findViewById(R.id.cart_page_total_points_coupon_text);
        discountText = findViewById(R.id.cart_page_total_points_discount_text);
        sumText = findViewById(R.id.cart_page_sum_text);

        pointText.setText(Utils.getLangString(this, curLang, R.string.point));
        couponText.setText(Utils.getLangString(this, curLang, R.string.coupon));
        discountText.setText(Utils.getLangString(this, curLang, R.string.discount));
        sumText.setText(Utils.getLangString(this, curLang, R.string.sum));

    }

    /**
     * Set RecyclerView's LayoutManager to the one given.
     *
     * @param layoutManagerType Type of layout manager to switch to.
     */
    public void setRecyclerViewLayoutManager(LayoutManagerType layoutManagerType) {
        int scrollPosition = 0;

        // If a layout manager has already been set, get current scroll position.
        if (mRecyclerView.getLayoutManager() != null) {
            scrollPosition = ((LinearLayoutManager) mRecyclerView.getLayoutManager())
                    .findFirstCompletelyVisibleItemPosition();
        }

        switch (layoutManagerType) {
            case GRID_LAYOUT_MANAGER:
                mLayoutManager = new GridLayoutManager(this, SPAN_COUNT);
                mCurrentLayoutManagerType = LayoutManagerType.GRID_LAYOUT_MANAGER;
                break;
            case LINEAR_LAYOUT_MANAGER:
                mLayoutManager = new LinearLayoutManager(this);
                mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
                break;
            default:
                mLayoutManager = new LinearLayoutManager(this);
                mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
        }

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.scrollToPosition(scrollPosition);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){

            onBackPressed();
            return true;

        }

        return false;
    }

    public class VerticalSpaceItemDecoration extends RecyclerView.ItemDecoration{

        private final int verticalSpaceHeight;

        public VerticalSpaceItemDecoration(int verticalSpaceHeight) {
            this.verticalSpaceHeight = verticalSpaceHeight;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            if (parent.getChildAdapterPosition(view) != parent.getAdapter().getItemCount() - 1) {
                outRect.bottom = verticalSpaceHeight;
            }
        }
    }

    private void updateUI(){

        //Update total price display
        sumPriceDisplay.setText(Utils.getLangString(this, curLang, R.string.menu_price, cart.getTotalPrice()));

        pointsDisplay.setText(Utils.getLangString(this, curLang, R.string.points_display, cart.getPoints()));
        couponsDisplay.setText(Utils.getLangString(this, curLang, R.string.coupons_display, cart.getNumCoupons()));
        discountDisplay.setText(Utils.getLangString(this, curLang, R.string.menu_price, cart.getDiscountAmount()));

        totalPriceDisplay.setText(Utils.getLangString(this, curLang, R.string.menu_price, cart.getTotalDiscountedAmount()));

        //If there are no items in the cart, display appropriate screen
        if(cart.isEmpty()){
            noItemScreen.setVisibility(View.VISIBLE);
            yesItemScreen.setVisibility(View.GONE);
        }else{
            noItemScreen.setVisibility(View.GONE);
            yesItemScreen.setVisibility(View.VISIBLE);

            String curRestaurantText = "(" + Utils.getLangString(this, curLang, cart.getCurrentRestaurant().getName()) + ")";
            currentRestaurantText.setText(curRestaurantText);
        }

    }

    private void showEmptyCartDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(Utils.getLangString(this, curLang, R.string.cuebap));
        builder.setMessage(Utils.getLangString(this, curLang, R.string.do_you_want_to_empty_the_cart));

        builder.setPositiveButton(Utils.getLangString(this, curLang, R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                Log.d(TAG, "Yes button clicked");

                cart.setCurrentRestaurant(cart.getNullRestaurantItem());
                cart.empty();

                Log.d(TAG, "Menu items in cart: " + cart.toString());

                updateUI();

            }
        });

        builder.setNegativeButton(Utils.getLangString(this, curLang, R.string.no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Log.d(TAG, "No button clicked");
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
