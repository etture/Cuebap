package com.jinoolee.cuebap.SignInCreateAccount;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
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

public class CreateAccountActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "CreateAccountActivity";

    //Account information field
    private EditText emailInput, passwordInput, passwordReenterInput, phoneInput, nameInput;
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
        setContentView(R.layout.activity_create_account);

        Toolbar toolbar = this.findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //ActionBar set text to center, with back button
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(R.layout.actionbar_layout);

        TextView actionBarTitle = actionBar.getCustomView().findViewById(R.id.actionbar_title);
        actionBarTitle.setText(Utils.getLangString(this, curLang, R.string.sign_up));

        //Code for displaying back button --> true: back button, false: no back button
        actionBar.setDisplayHomeAsUpEnabled(true);
        //ActionBar configuration DONE

        //Account information field
        emailInput = findViewById(R.id.create_email_input);
        passwordInput = findViewById(R.id.create_password_input);
        passwordReenterInput = findViewById(R.id.create_password_reenter_input);
        phoneInput = findViewById(R.id.create_phone_number_input);
        campusInput = findViewById(R.id.create_campus_input);
        registerCardToggle = findViewById(R.id.register_card_toggle);
        nameInput = findViewById(R.id.create_name_input);
        campusSearchBtn = findViewById(R.id.campus_search_btn);
        signUpBtn = findViewById(R.id.sign_up_btn);

        //Register card field
        foldingCell = findViewById(R.id.register_card_folding_cell);
        registerCardArrowDown = findViewById(R.id.register_card_arrow_down);
        registerCardArrowUp = findViewById(R.id.register_card_arrow_up);
        cardNumInput1 = findViewById(R.id.card_number_input1);
        cardNumInput2 = findViewById(R.id.card_number_input2);
        cardNumInput3 = findViewById(R.id.card_number_input3);
        cardNumInput3 = findViewById(R.id.card_number_input4);
        expirationMonth = findViewById(R.id.expiration_month);
        expirationYear = findViewById(R.id.expiration_year);

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

        nameInput.setHint(Utils.getLangString(this, curLang, R.string.example_name));
        emailInput.setHint(Utils.getLangString(this, curLang, R.string.example_email));
        passwordInput.setHint(Utils.getLangString(this, curLang, R.string.enter_password));
        passwordReenterInput.setHint(Utils.getLangString(this, curLang, R.string.reenter_password));
        phoneInput.setHint(Utils.getLangString(this, curLang, R.string.example_phone_number));
        campusInput.setText(Utils.getLangString(this, curLang, R.string.select_campus));
        registerCardToggle.setText(Utils.getLangString(this, curLang, R.string.register_your_card));
        campusSearchBtn.setText(Utils.getLangString(this, curLang, R.string.search));
        signUpBtn.setText(Utils.getLangString(this, curLang, R.string.sign_up));

        TextView accountInfo, name, email, password, cellPhone, chooseYourCampus, cardNumber, expiration, optionalRegistration;
        accountInfo = findViewById(R.id.create_account_account_information);
        name = findViewById(R.id.create_account_name);
        email = findViewById(R.id.create_account_email);
        password = findViewById(R.id.create_account_password);
        cellPhone = findViewById(R.id.create_account_cell_phone);
        chooseYourCampus = findViewById(R.id.create_account_choose_your_campus);
        cardNumber = findViewById(R.id.create_account_card_number);
        expiration = findViewById(R.id.create_account_expiration);
        optionalRegistration = findViewById(R.id.create_account_optional_card_registration);

        accountInfo.setText(Utils.getLangString(this, curLang, R.string.account_information));
        name.setText(Utils.getLangString(this, curLang, R.string.name));
        email.setText(Utils.getLangString(this, curLang, R.string.email));
        password.setText(Utils.getLangString(this, curLang, R.string.password));
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

    private void createAccount(String email, String password){
        Log.d(TAG, "createAccount: " + email);

        if(!validateForm()){
            return;
        }

        showProgressDialog();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){

                            Log.d(TAG, "createUserWithEmail : success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            String userId = user.getUid();

                            //Add user info to database
                            /*
                            databaseReference.child("users").child(user.getUid()).child("email").setValue(user.getEmail());
                            databaseReference.child("users").child(user.getUid()).child("phone").setValue(phoneInput.getText().toString());
                            databaseReference.child("users").child(user.getUid()).child("name").setValue(nameInput.getText().toString());
                            databaseReference.child("users").child(user.getUid()).child("campus").setValue(campusInput.getText().toString());
                            */

                            String name = nameInput.getText().toString();
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

                            updateUI(user);

                        }else{

                            Log.w(TAG, "createUserWithEmail : failure", task.getException());

                            if(task.getException() instanceof FirebaseAuthUserCollisionException){
                                showSnackbar(R.id.create_rootView, Utils.getLangString(getApplicationContext(), curLang, R.string.account_already_exists));
                            }else{
                                showSnackbar(R.id.create_rootView, Utils.getLangString(getApplicationContext(), curLang, R.string.sign_up_failed));
                            }

                            updateUI(null);

                        }

                        hideProgressDialog();
                    }
                });
    }

    private void writeNewUser(String userId, String name, String email, String phone, String school, String campus){

        User user = new User(name, email, phone, school, campus);
        databaseReference.child("users").child(userId).setValue(user);

    }

    private boolean validateForm(){
        boolean valid = true;

        String name = nameInput.getText().toString();

        if(TextUtils.isEmpty(name)){
            nameInput.setError(Utils.getLangString(this, curLang, R.string.required));
            valid = false;
        }else{
            nameInput.setError(null);
        }

        String email = emailInput.getText().toString();

        if(TextUtils.isEmpty(email)){
            emailInput.setError(Utils.getLangString(this, curLang, R.string.required));
            valid = false;
        }else{
            emailInput.setError(null);
        }

        String password = passwordInput.getText().toString();

        if(TextUtils.isEmpty(password)){
            passwordInput.setError(Utils.getLangString(this, curLang, R.string.required));
            valid = false;
        }else{
            passwordInput.setError(null);
        }

        String password_reenter = passwordReenterInput.getText().toString();

        if(!password_reenter.equals(password)){
            passwordReenterInput.setError(Utils.getLangString(this, curLang, R.string.password_mismatch));
            valid = false;
        }else{
            passwordReenterInput.setError(null);
        }

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
            intent.putExtra("from", "CreateAccountActivity");
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

            hideKeyboard(view);
            createAccount(emailInput.getText().toString(), passwordInput.getText().toString());

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
