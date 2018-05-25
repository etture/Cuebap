package com.jinoolee.cuebap;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.jinoolee.cuebap.Helper.Utils;

public abstract class BaseActivity extends AppCompatActivity {

    public ProgressDialog mProgressDialog;

    //Language settings
    protected SharedPreferences langPref;
    protected SharedPreferences.Editor editor;
    protected String curLang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        //Current language
        langPref = getSharedPreferences("language", MODE_PRIVATE);
        curLang = langPref.getString("currentLanguage", "en");
    }

    public void showProgressDialog(){
        if(mProgressDialog == null){
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(Utils.getLangString(this, curLang, R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog(){
        if(mProgressDialog != null && mProgressDialog.isShowing()){
            mProgressDialog.dismiss();
        }
    }

    public void hideKeyboard(View view){
        final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if(imm != null){
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        hideProgressDialog();
    }

    public void showSignInFailedSnackbar(int rootViewId){

        Snackbar.make(findViewById(rootViewId), Utils.getLangString(this, curLang, R.string.sign_in_failed),
                Snackbar.LENGTH_SHORT).show();

    }

    public void showSnackbar(int rootViewId, String string){

        Snackbar.make(findViewById(rootViewId), string,
                Snackbar.LENGTH_SHORT).show();

    }

    @Override
    protected void attachBaseContext(Context newBase) {

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(newBase);

        String lang_code = pref.getString("lang", "en");
        Context context = Utils.changeLang(newBase, lang_code);

        super.attachBaseContext(context);
    }

    protected abstract void layoutElementStringSetup();
}
