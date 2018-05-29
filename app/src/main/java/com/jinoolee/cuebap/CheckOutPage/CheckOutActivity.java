package com.jinoolee.cuebap.CheckOutPage;

import android.content.Intent;
import android.graphics.Rect;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jinoolee.cuebap.BaseActivity;
import com.jinoolee.cuebap.Data.CurrentOrder;
import com.jinoolee.cuebap.Helper.RecyclerViewAdapter;
import com.jinoolee.cuebap.Helper.Utils;
import com.jinoolee.cuebap.MainPage.MainPageActivity;
import com.jinoolee.cuebap.R;
import com.jinoolee.cuebap.SignInCreateAccount.SignInActivity;
import com.ramotion.foldingcell.FoldingCell;

public class CheckOutActivity extends BaseActivity implements View.OnClickListener{

    private static final String TAG = "CheckOutActivity";
    private static final String KEY_LAYOUT_MANAGER = "layoutManager";
    private static final int SPAN_COUNT = 2;

    private RelativeLayout yesItem, noItem;
    private TextView noOrderPlacedText;

    private CurrentOrder currentOrder;
    private FoldingCell foldingCell;

    private TextView homeBtn;
    private TextView mobileReceiptText, orderCompletedText, waitingNumberText, pickUpText, timeText, orderStatusText, reservationText, preparingText, readyText;

    //from current order information
    private int orderHour, orderMinute, orderAmPm;
    private int waitingNumber;

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if(id == foldingCell.getId()){

            foldingCell.toggle(false);

        }
    }

    //RecyclerView for detailed receipt

    private enum LayoutManagerType {
        GRID_LAYOUT_MANAGER,
        LINEAR_LAYOUT_MANAGER
    }
    private RecyclerView mRecyclerView;
    private LayoutManagerType mCurrentLayoutManagerType;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerViewAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out);

        currentOrder = CurrentOrder.getInstance();

        yesItem = findViewById(R.id.check_out_yes_items);
        noItem = findViewById(R.id.check_out_no_items);
        noOrderPlacedText = findViewById(R.id.check_out_no_order_placed);

        foldingCell = findViewById(R.id.check_out_foldingCell);
        foldingCell.setOnClickListener(this);

        homeBtn = findViewById(R.id.checkout_page_home_btn);
        mobileReceiptText = findViewById(R.id.check_out_mobile_receipt_text);
        orderCompletedText = findViewById(R.id.check_out_order_completed_text);
        waitingNumberText = findViewById(R.id.check_out_waiting_number);
        pickUpText = findViewById(R.id.check_out_pick_up_text);
        timeText = findViewById(R.id.check_out_time_text);
        orderStatusText = findViewById(R.id.check_out_order_status_text);
        reservationText = findViewById(R.id.check_out_reservation_text);
        preparingText = findViewById(R.id.check_out_preparing_text);
        readyText = findViewById(R.id.check_out_ready_text);

        //RecyclerView setup
        mRecyclerView = findViewById(R.id.check_out_receipt_list);
        mLayoutManager = new LinearLayoutManager(this);
        mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
        if (savedInstanceState != null) {
            // Restore saved layout manager type.
            mCurrentLayoutManagerType = (LayoutManagerType) savedInstanceState
                    .getSerializable(KEY_LAYOUT_MANAGER);
        }
        setRecyclerViewLayoutManager(mCurrentLayoutManagerType);

        mAdapter = new RecyclerViewAdapter(this, currentOrder.getOrderItems(), curLang) {
            @Override
            public void onItemDeleted() {}
        }; //Data input, define method updating total price display when item is deleted from the cart page

        mRecyclerView.setAdapter(mAdapter);

        //Set vertical divider spacing between items in RecyclerView
        RecyclerView.ItemDecoration dividerDecoration = new VerticalSpaceItemDecoration(20);
        mRecyclerView.addItemDecoration(dividerDecoration);

        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), MainPageActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.putExtra("from", "CheckOutActivity");
                startActivity(intent);

                finish();

            }
        });

        layoutElementStringSetup();
    }

    @Override
    protected void layoutElementStringSetup() {

        if(currentOrder.isOrderPlaced()){

            yesItem.setVisibility(View.VISIBLE);
            noItem.setVisibility(View.GONE);

            waitingNumber = currentOrder.getWaitingNumber();
            orderHour = currentOrder.getOrderHour();
            orderMinute = currentOrder.getOrderMinute();
            orderAmPm = currentOrder.getOrderAmPm();

            homeBtn.setText(Utils.getLangString(this, curLang, R.string.home));
            mobileReceiptText.setText(Utils.getLangString(this, curLang, R.string.mobile_receipt));
            orderCompletedText.setText(Utils.getLangString(this, curLang, R.string.your_order_has_been_completed));
            waitingNumberText.setText(Integer.toString(waitingNumber));
            pickUpText.setText(Utils.getLangString(this, curLang, R.string.pick_up));

            if(currentOrder.getOrderLater()){
                String amPm = Utils.getLangString(this, curLang, orderAmPm);
                timeText.setText(Utils.getLangString(this, curLang, R.string.basic_time, orderHour, orderMinute, amPm));
            }else{
                timeText.setVisibility(View.GONE);
                pickUpText.setVisibility(View.GONE);
            }

            orderStatusText.setText(Utils.getLangString(this, curLang, R.string.order_status));
            reservationText.setText(Utils.getLangString(this, curLang, R.string.reservation));
            preparingText.setText(Utils.getLangString(this, curLang, R.string.preparing));
            readyText.setText(Utils.getLangString(this, curLang, R.string.ready));

            //detailed receipt
            TextView subtotalText, discountText, totalPriceText, viewReceiptText, foldReceiptText;
            TextView subtotal, discount, totalPrice;

            subtotalText = findViewById(R.id.check_out_subtotal);
            discountText = findViewById(R.id.check_out_total_points_discount_text);
            totalPriceText = findViewById(R.id.check_out_total_price_bottom_text);
            viewReceiptText = findViewById(R.id.check_out_view_receipt_text);
            foldReceiptText = findViewById(R.id.check_out_fold_receipt_text);
            subtotal = findViewById(R.id.check_out_subtotal_display);
            discount = findViewById(R.id.check_out_total_points_discount_display);
            totalPrice = findViewById(R.id.check_out_total_price_display);

            subtotalText.setText(Utils.getLangString(this, curLang, R.string.subtotal));
            discountText.setText(Utils.getLangString(this, curLang, R.string.discount));
            totalPriceText.setText(Utils.getLangString(this, curLang, R.string.total_price));
            viewReceiptText.setText(Utils.getLangString(this, curLang, R.string.view_detailed_receipt));
            foldReceiptText.setText(Utils.getLangString(this, curLang, R.string.fold_detailed_receipt));

            subtotal.setText(Utils.getLangString(this, curLang, R.string.menu_price, currentOrder.getTotalPrice()));
            discount.setText(Utils.getLangString(this, curLang, R.string.menu_price, currentOrder.getTotalDiscount()));
            totalPrice.setText(Utils.getLangString(this, curLang, R.string.menu_price, currentOrder.getTotalDiscountedPrice()));

        }else{

            yesItem.setVisibility(View.GONE);
            noItem.setVisibility(View.VISIBLE);

            noOrderPlacedText.setText(Utils.getLangString(this, curLang, R.string.no_order_placed));

        }

    }

    @Override
    public void onBackPressed() {

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
}
