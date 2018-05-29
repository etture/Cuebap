package com.jinoolee.cuebap;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.jinoolee.cuebap.SignInCreateAccount.SignInActivity;

public class SplashActivity extends BaseActivity {

    private static final String TAG = "SplashActivity";

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        editor = langPref.edit();

        if(langPref.getString("currentLanguage", "ko").equals("en")){

            editor.putString("currentLanguage", "en");
            editor.commit();

        }else{

            editor.putString("currentLanguage", "ko");
            editor.commit();

        }

        intent = new Intent(this, SignInActivity.class);

        try{
            Thread.sleep(200);
        }catch(InterruptedException e){
            Log.e(TAG, "SplashActivity: fail", e);
        }

        startActivity(intent);
        finish();

    }

    @Override
    protected void layoutElementStringSetup() {

    }
}
