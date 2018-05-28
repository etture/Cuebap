package com.jinoolee.cuebap.CheckOutPage;

import android.content.Intent;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.jinoolee.cuebap.BaseActivity;
import com.jinoolee.cuebap.Data.CurrentOrder;
import com.jinoolee.cuebap.Helper.Utils;
import com.jinoolee.cuebap.MainPage.MainPageActivity;
import com.jinoolee.cuebap.R;
import com.jinoolee.cuebap.SignInCreateAccount.SignInActivity;

public class CheckOutActivity extends BaseActivity {

    private static final String TAG = "CheckOutActivity";

    private CurrentOrder currentOrder;

    private TextView homeBtn;
    private TextView mobileReceiptText, orderCompletedText, waitingNumberText, pickUpText, timeText, orderStatusText, reservationText, preparingText, readyText;

    //from current order information
    private int orderHour, orderMinute, orderAmPm;
    private int waitingNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out);

        currentOrder = CurrentOrder.getInstance();

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

        waitingNumber = currentOrder.getWaitingNumber();
        orderHour = currentOrder.getOrderHour();
        orderMinute = currentOrder.getOrderMinute();
        orderAmPm = currentOrder.getOrderAmPm();

        layoutElementStringSetup();
    }

    @Override
    protected void layoutElementStringSetup() {

        homeBtn.setText(Utils.getLangString(this, curLang, R.string.home));
        mobileReceiptText.setText(Utils.getLangString(this, curLang, R.string.mobile_receipt));
        orderCompletedText.setText(Utils.getLangString(this, curLang, R.string.your_order_has_been_completed));
        waitingNumberText.setText(Integer.toString(waitingNumber));
        pickUpText.setText(Utils.getLangString(this, curLang, R.string.pick_up));

        String amPm = Utils.getLangString(this, curLang, orderAmPm);

        if(currentOrder.getOrderLater()){
            timeText.setText(Utils.getLangString(this, curLang, R.string.basic_time, orderHour, orderMinute, amPm));
        }else{
            timeText.setVisibility(View.GONE);
            pickUpText.setVisibility(View.GONE);
        }

        orderStatusText.setText(Utils.getLangString(this, curLang, R.string.order_status));
        reservationText.setText(Utils.getLangString(this, curLang, R.string.reservation));
        preparingText.setText(Utils.getLangString(this, curLang, R.string.preparing));
        readyText.setText(Utils.getLangString(this, curLang, R.string.ready));

    }

    @Override
    public void onBackPressed() {

    }
}
