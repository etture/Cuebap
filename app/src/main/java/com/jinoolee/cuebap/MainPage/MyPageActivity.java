package com.jinoolee.cuebap.MainPage;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jinoolee.cuebap.BaseActivity;
import com.jinoolee.cuebap.Helper.MyDebug;
import com.jinoolee.cuebap.Helper.Utils;
import com.jinoolee.cuebap.R;
import com.ramotion.foldingcell.FoldingCell;

import java.util.Locale;

public class MyPageActivity extends BaseActivity implements View.OnClickListener{

    private static final String TAG = "MyPageActivity";

    private Button signOutBtn;
    private FoldingCell foldingCell;

    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();

    //Language settings
    private SharedPreferences langPref;
    private String curLang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_page);

        //Current language
        langPref = getSharedPreferences("language", MODE_PRIVATE);
        curLang = langPref.getString("currentLanguage", "en");

        Toolbar toolbar = this.findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //ActionBar set text to center, with back button
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(R.layout.actionbar_layout);

        TextView actionBarTitle = actionBar.getCustomView().findViewById(R.id.actionbar_title);
        actionBarTitle.setText(Utils.getLangString(this, curLang, R.string.my_page));

        //Code for displaying back button --> true: back button, false: no back button
        actionBar.setDisplayHomeAsUpEnabled(true);
        //ActionBar configuration DONE

        signOutBtn = findViewById(R.id.sign_out_btn);

        signOutBtn.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();

        //Change language string in all layout elements
        layoutElementStringSetup();

        if(MyDebug.LOG){
            Log.e(TAG, "onCreate() called");
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(id == signOutBtn.getId()){

            if(MyDebug.LOG){
                Log.e(TAG, "signOutBtn clicked");
            }

            mAuth.signOut();

            Intent intent = new Intent();
            intent.putExtra("sign_out", true);

            setResult(RESULT_OK, intent);
            finish();

        }else if(id == foldingCell.getId()){

            foldingCell.toggle(false);

        }


        /*else if(id == changeLanguageBtn.getId()){

            Locale loc = getResources().getConfiguration().locale;
            Configuration config = new Configuration();

            if(loc.equals(Locale.KOREA)){
                config.locale = Locale.US;
                getResources().updateConfiguration(config, getResources().getDisplayMetrics());
            }else{
                config.locale = Locale.KOREA;
                getResources().updateConfiguration(config, getResources().getDisplayMetrics());
            }

        }*/
    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent();
        intent.putExtra("sign_out", false);

        setResult(RESULT_CANCELED, intent);
        finish();

        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){

            onBackPressed();
            return true;

        }

        return false;
    }

    @Override
    protected void layoutElementStringSetup(){

        Button sign_out_btn = findViewById(R.id.sign_out_btn);
        sign_out_btn.setText(Utils.getLangString(this, curLang, R.string.sign_out));

    }
}
