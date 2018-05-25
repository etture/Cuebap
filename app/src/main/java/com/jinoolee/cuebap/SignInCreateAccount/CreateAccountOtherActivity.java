package com.jinoolee.cuebap.SignInCreateAccount;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jinoolee.cuebap.BaseActivity;
import com.jinoolee.cuebap.Data.RC;
import com.jinoolee.cuebap.Data.User;
import com.jinoolee.cuebap.Helper.Utils;
import com.jinoolee.cuebap.MainPage.MainPageActivity;
import com.jinoolee.cuebap.R;
import com.ramotion.foldingcell.FoldingCell;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CreateAccountOtherActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "CreateAccountOther";

    //Account information field
    private EditText phoneInput;
    private TextView campusInput, registerCardToggle;
    private Button campusSearchBtn;
    private TextView signUpBtn;

    //Register card field, including folding cell
    private FoldingCell foldingCell;
    private ImageView registerCardArrowDown, registerCardArrowUp;
    private EditText cardNumInput1, cardNumInput2, cardNumInput3, cardNuminput4;
    private EditText expirationMonth, expirationYear;

    private Animation rotateTo180, rotateFrom180;

    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account_other);

        Toolbar toolbar = this.findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Account information field
        phoneInput = findViewById(R.id.other_create_phone_number_input);
        campusInput = findViewById(R.id.other_create_campus_input);
        registerCardToggle = findViewById(R.id.other_register_card_toggle);
        campusSearchBtn = findViewById(R.id.other_campus_search_btn);
        signUpBtn = findViewById(R.id.other_sign_up_btn);

        //Register card field
        foldingCell = findViewById(R.id.other_register_card_folding_cell);
        registerCardArrowDown = findViewById(R.id.other_register_card_arrow_down);
        registerCardArrowUp = findViewById(R.id.other_register_card_arrow_up);
        cardNumInput1 = findViewById(R.id.other_card_number_input1);
        cardNumInput2 = findViewById(R.id.other_card_number_input2);
        cardNumInput3 = findViewById(R.id.other_card_number_input3);
        cardNumInput3 = findViewById(R.id.other_card_number_input4);
        expirationMonth = findViewById(R.id.other_expiration_month);
        expirationYear = findViewById(R.id.other_expiration_year);

        //Set button listeners
        campusSearchBtn.setOnClickListener(this);
        signUpBtn.setOnClickListener(this);
        registerCardToggle.setOnClickListener(this);

        //Get Firebase Auth instance for signing up
        mAuth = FirebaseAuth.getInstance();

        //Folding cell button rotation animation
        rotateTo180 = AnimationUtils.loadAnimation(this, R.anim.arrow_rotate_to_180);
        rotateFrom180 = AnimationUtils.loadAnimation(this, R.anim.arrow_rotate_from_180);

        rotateTo180.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                if(foldingCell.isUnfolded()){

                    registerCardArrowDown.setVisibility(View.VISIBLE);
                    registerCardArrowUp.setVisibility(View.INVISIBLE);

                }else{

                    registerCardArrowDown.setVisibility(View.INVISIBLE);
                    registerCardArrowUp.setVisibility(View.VISIBLE);

                }

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        rotateFrom180.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                if(foldingCell.isUnfolded()){

                    registerCardArrowDown.setVisibility(View.VISIBLE);
                    registerCardArrowUp.setVisibility(View.INVISIBLE);

                }else{

                    registerCardArrowDown.setVisibility(View.INVISIBLE);
                    registerCardArrowUp.setVisibility(View.VISIBLE);

                }

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        layoutElementStringSetup();

    }

    @Override
    protected void layoutElementStringSetup() {

        phoneInput.setHint(Utils.getLangString(this, curLang, R.string.example_phone_number));
        campusInput.setText(Utils.getLangString(this, curLang, R.string.select_campus));
        registerCardToggle.setText(Utils.getLangString(this, curLang, R.string.register_your_card));
        campusSearchBtn.setText(Utils.getLangString(this, curLang, R.string.search));
        signUpBtn.setText(Utils.getLangString(this, curLang, R.string.sign_up));

        TextView accountInfo, cellPhone, chooseYourCampus, cardNumber, expiration, optionalRegistration;
        accountInfo = findViewById(R.id.create_other_account_information);
        cellPhone = findViewById(R.id.create_other_cell_phone);
        chooseYourCampus = findViewById(R.id.create_other_choose_your_campus);
        cardNumber = findViewById(R.id.create_other_card_number);
        expiration = findViewById(R.id.create_other_expiration);
        optionalRegistration = findViewById(R.id.create_other_optional_card_registration);

        accountInfo.setText(Utils.getLangString(this, curLang, R.string.account_information));
        cellPhone.setText(Utils.getLangString(this, curLang, R.string.cell_phone));
        chooseYourCampus.setText(Utils.getLangString(this, curLang, R.string.choose_your_campus));
        cardNumber.setText(Utils.getLangString(this, curLang, R.string.card_number));
        expiration.setText(Utils.getLangString(this, curLang, R.string.expiration));
        optionalRegistration.setText(Utils.getLangString(this, curLang, R.string.optional_card_registration));

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void createAccount(FirebaseUser user){
        Log.d(TAG, "createAccount: " + user.getEmail());

        if(!validateForm()){
            return;
        }

        showProgressDialog();

        writeNewUser(user);

        updateUI(user);

    }

    private void writeNewUser(String userId, String name, String email, String phone, String school, String campus){

        User user = new User(name, email, phone, school, campus);
        databaseReference.child("users").child(userId).setValue(user);

    }

    private void writeNewUser(FirebaseUser user){

        String userId = user.getUid();
        String name = user.getDisplayName();
        String email = user.getEmail();
        String phone = phoneInput.getText().toString();
        String school = null;
        String campus = null;

        Pattern pEng = Pattern.compile("(.*?) University");
        Pattern pKor = Pattern.compile("(.*?)학교");

        Matcher mEng = pEng.matcher(campusInput.getText().toString());
        Matcher mKor = pKor.matcher(campusInput.getText().toString());

        if(mEng.find()){
            school = mEng.group(1);
        }else if(mKor.find()){
            school = mKor.group(1);
        }

        pEng = Pattern.compile("University (.*?)$");
        pKor = Pattern.compile("대학교 (.*?)$");

        mEng = pEng.matcher(campusInput.getText().toString());
        mKor = pKor.matcher(campusInput.getText().toString());

        if(mEng.find()){
            campus = mEng.group(1);
        }else if(mKor.find()){
            campus = mKor.group(1);
        }

        writeNewUser(userId, name, email, phone, school, campus);
    }

    private boolean validateForm(){
        boolean valid = true;

        String phone = phoneInput.getText().toString();

        if(TextUtils.isEmpty(phone)){
            phoneInput.setError(Utils.getLangString(this, curLang, R.string.required));
            valid = false;
        }else{
            phoneInput.setError(null);
        }

        String campus = campusInput.getText().toString();

        if(campus.equals(Utils.getLangString(this, curLang, R.string.select_campus))){
            campusInput.setError(Utils.getLangString(this, curLang, R.string.required));
            valid = false;
        }else{
            campusInput.setError(null);
        }

        return valid;
    }

    private void updateUI(FirebaseUser user){
        hideProgressDialog();

        if(user != null){

            Intent intent = new Intent(this, MainPageActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            intent.putExtra("from", "CreateAccountOtherActivity");
            startActivityForResult(intent, RC.MAIN_PAGE);

        }else{

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RC.MAIN_PAGE){

            finish();

        }else if(requestCode == RC.CAMPUS_SEARCH){

            if(data != null){

                String school = data.getStringExtra("school");
                String campus = data.getStringExtra("campus");

                campusInput.setText(school + " " + campus);

            }else{



            }

        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if(id == signUpBtn.getId()){

            FirebaseUser user = mAuth.getCurrentUser();

            hideKeyboard(view);
            createAccount(user);

        }else if(id == campusSearchBtn.getId()){

            Intent intent = new Intent(this, CampusSearchActivity.class);
            startActivityForResult(intent, RC.CAMPUS_SEARCH);

        }else if(id == registerCardToggle.getId()){

            if(foldingCell.isUnfolded()){

                registerCardArrowUp.startAnimation(rotateFrom180);

            }else{

                registerCardArrowDown.startAnimation(rotateTo180);

            }

            foldingCell.toggle(false);

        }
    }

    @Override
    public void onBackPressed() {

    }

    //Code for making EditText lose focus when touching anything else + hiding keyboard
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        View currentView = getCurrentFocus();

        if(ev.getAction() == MotionEvent.ACTION_DOWN){

            if(currentView.isFocused()){

                Rect outRect = new Rect();
                currentView.getGlobalVisibleRect(outRect);

                if(!outRect.contains((int)ev.getRawX(), (int)ev.getRawY())){

                    currentView.clearFocus();

                    hideKeyboard(currentView);

                }

            }

        }

        return super.dispatchTouchEvent(ev);
    }
}
