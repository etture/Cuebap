package com.jinoolee.cuebap.CheckOutPage;

import android.content.Intent;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.jinoolee.cuebap.MainPage.MainPageActivity;
import com.jinoolee.cuebap.R;
import com.jinoolee.cuebap.SignInCreateAccount.SignInActivity;

public class CheckOutActivity extends AppCompatActivity {

    private TextView homeBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out);

        homeBtn = findViewById(R.id.checkout_page_home_btn);

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
    }

    @Override
    public void onBackPressed() {

    }
}
