package com.jinoolee.cuebap.SignInCreateAccount;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.jinoolee.cuebap.BaseActivity;
import com.jinoolee.cuebap.Data.RC;
import com.jinoolee.cuebap.Helper.MyDebug;
import com.jinoolee.cuebap.Helper.Utils;
import com.jinoolee.cuebap.MainPage.MainPageActivity;
import com.jinoolee.cuebap.R;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class SignInActivity extends BaseActivity implements View.OnClickListener{

    private static final String TAG = "SignInActivity";

    private Button signInBtn, createAccountBtn;
    private EditText emailInput, passwordInput;
    private ImageButton googleSignInBtn, fbSignInBtn;

    //Authentication-related objects
    private FirebaseAuth mAuth;
    //Google
    private GoogleSignInClient mGoogleSignInClient;
    private String googleClientId = "615334450749-odoimlm7gj9dcbd145n5mflv8jfi1643.apps.googleusercontent.com";
    //Facebook
    private LoginManager mLoginManager;
    private CallbackManager mCallbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Initialize Facebook SDK before setContentView(Layout ID)
        FacebookSdk.sdkInitialize(getApplicationContext());

        setContentView(R.layout.activity_sign_in);

        //getSupportActionBar().hide();

        //Get hash key
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.jinoolee.cuebap",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                if(MyDebug.LOG){
                    Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
                }

            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }


        //Initialize views
        signInBtn = findViewById(R.id.sign_in_btn);
        createAccountBtn = findViewById(R.id.create_account_btn);
        emailInput = findViewById(R.id.email_input);
        passwordInput = findViewById(R.id.password_input);
        googleSignInBtn = findViewById(R.id.google_sign_in_btn);
        fbSignInBtn = findViewById(R.id.fb_sign_in_btn);

        //Set on-click listeners
        signInBtn.setOnClickListener(this);
        createAccountBtn.setOnClickListener(this);
        googleSignInBtn.setOnClickListener(this);
        fbSignInBtn.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();

        //After entering password, press 'enter' key on keyboard to perform sign in action
        passwordInput.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press

                    signIn(emailInput.getText().toString(), passwordInput.getText().toString());

                    return true;
                }
                return false;
            }
        });

        //Google sign-in configurations
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(googleClientId)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        //Change language string in all layout elements
        layoutElementStringSetup();

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser, false);
    }

    private void signIn(String email, String password){

        Log.d(TAG, "signIn: " + email);

        if(!validateForm()){
            return;
        }

        showProgressDialog();

        AuthCredential credential = EmailAuthProvider.getCredential(email, password);

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){

                            if(MyDebug.LOG){
                                Log.d(TAG, "signInWithEmailAndPassword: success");
                            }

                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user, false);

                        }else{

                            if(MyDebug.LOG){
                                Log.w(TAG, "signInWithEmailAndPassword: failure", task.getException());
                            }

                            updateUI(null, false);

                        }

                        if(!task.isSuccessful()){

                            showSignInFailedSnackbar(R.id.rootView);

                            if(MyDebug.LOG){
                                Log.d(TAG, "Sign in failed with email");
                            }


                        }

                        hideProgressDialog();

                    }
                });
    }

    private boolean validateForm(){
        boolean valid = true;

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

        return valid;
    }

    private void googleSignIn(){

        Intent googleSignInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(googleSignInIntent, RC.GOOGLE_SIGN_IN);

        if(MyDebug.LOG){
            Log.d(TAG, "googleSignIn() started");
        }

    }

    private void handleGoogleSignInResult(Task<GoogleSignInAccount> completedTask){

        try{

            GoogleSignInAccount account = completedTask.getResult(ApiException.class);



            firebaseAuthWithGoogle(account);
            if(MyDebug.LOG){
                Log.d(TAG, "handleGoogleSignInResult() executed");
            }

        }catch(ApiException e){

            if(MyDebug.LOG){
                Log.w(TAG, "googleSignIn() result: failed code = " + e.getStatusCode());
            }

            showSignInFailedSnackbar(R.id.rootView);

            if(MyDebug.LOG){
                Log.d(TAG, "Sign in failed with Google");
            }

            updateUI(null, false);

        }

    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account){

        if(MyDebug.LOG){
            Log.d(TAG, "firebaseAuthWithGoogle(): " + account.getId());
        }

        showProgressDialog();

        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){

                            if(MyDebug.LOG){
                                Log.d(TAG, "signInWithCredential(Google): success");
                            }

                            FirebaseUser user = mAuth.getCurrentUser();

                            boolean isNew = task.getResult().getAdditionalUserInfo().isNewUser();

                            updateUI(user, isNew);

                        }else{

                            if(MyDebug.LOG){
                                Log.w(TAG, "signInWithCredential(Google): failure", task.getException());
                            }

                            if(task.getException() instanceof FirebaseAuthUserCollisionException){
                                showSnackbar(R.id.rootView, getString(R.string.account_already_exists));
                            }else{
                                showSignInFailedSnackbar(R.id.rootView);
                            }

                            if(MyDebug.LOG){
                                Log.d(TAG, "Sign in failed with Google");
                            }

                            updateUI(null, false);

                        }

                        hideProgressDialog();

                    }
                });

    }

    private void fbSignIn(){

        if (MyDebug.LOG) {
            Log.d(TAG, "fbSignIn() started");
        }

        //Facebook sign-in configurations
        mCallbackManager = CallbackManager.Factory.create();
        mLoginManager = LoginManager.getInstance();
        mLoginManager.logInWithReadPermissions(this, Arrays.asList("email", "public_profile"));
        mLoginManager.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                if(MyDebug.LOG){
                    Log.d(TAG, "Facebook sign-in: onSuccess(): " + loginResult);
                }

                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                if(MyDebug.LOG){
                    Log.d(TAG, "Facebook sign-in: onCancel()");
                }
            }

            @Override
            public void onError(FacebookException error) {
                if(MyDebug.LOG){
                    Log.d(TAG, "Facebook sign-in: onError()");
                }
            }
        });

    }

    private void handleFacebookAccessToken(AccessToken token){

        if(MyDebug.LOG){
            Log.d(TAG, "handleFacebookAccessToken(): " + token);
        }


        showProgressDialog();

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){

                            if(MyDebug.LOG){
                                Log.d(TAG, "signInWithCredential(Facebook): success");
                            }

                            FirebaseUser user = mAuth.getCurrentUser();

                            boolean isNew = task.getResult().getAdditionalUserInfo().isNewUser();

                            updateUI(user, isNew);

                        }else{

                            if(MyDebug.LOG){
                                Log.w(TAG, "signInWithCredential(Facebook): failure", task.getException());
                            }

                            if(task.getException() instanceof FirebaseAuthUserCollisionException){
                                showSnackbar(R.id.rootView, getString(R.string.account_already_exists));
                            }else{
                                showSignInFailedSnackbar(R.id.rootView);
                            }

                            if(MyDebug.LOG){
                                Log.d(TAG, "Sign in failed with Facebook");
                            }

                            updateUI(null, false);

                        }

                        hideProgressDialog();

                    }
                });

    }

    private void updateUI(FirebaseUser user, boolean isNew){
        hideProgressDialog();

        if(user != null && !isNew){

            Intent intent = new Intent(this, MainPageActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            intent.putExtra("from", "SignInActivity");
            startActivityForResult(intent, RC.MAIN_PAGE);

        }else if(user != null && isNew) {

            Intent intent = new Intent(this, CreateAccountOtherActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            intent.putExtra("from", "SignInActivity");
            startActivity(intent);

        }else {

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RC.MAIN_PAGE){

            showSnackbar(R.id.rootView, getString(R.string.signed_out));

            //Google sign-out
            /*
            mGoogleSignInClient.signOut().addOnCompleteListener(this, new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    updateUI(null, false);

                }
            });
            */

        }else if(requestCode == RC.GOOGLE_SIGN_IN){

            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleGoogleSignInResult(task);
            if(MyDebug.LOG){
                Log.d(TAG, "handleGoogleSignInResult() called");
            }

        }else{

            mCallbackManager.onActivityResult(requestCode, resultCode, data);

        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        Intent intent;

        if(id == signInBtn.getId()){

            signIn(emailInput.getText().toString(), passwordInput.getText().toString());

        }else if(id == createAccountBtn.getId()){

            intent = new Intent(this, CreateAccountActivity.class);
            startActivity(intent);

        }else if(id == googleSignInBtn.getId()){

            googleSignIn();

        }else if(id == fbSignInBtn.getId()){

            if(MyDebug.LOG){
                Log.d(TAG, "fbSignInBtn clicked");
            }

            fbSignIn();

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

    @Override
    protected void layoutElementStringSetup(){

        //Toast.makeText(this, "layoutElementStringSetup() called", Toast.LENGTH_SHORT).show();

        signInBtn.setText(Utils.getLangString(this, curLang, R.string.sign_in));
        createAccountBtn.setText(Utils.getLangString(this, curLang, R.string.create_account));
        emailInput.setHint(Utils.getLangString(this, curLang, R.string.example_email));
        passwordInput.setHint(Utils.getLangString(this, curLang, R.string.enter_password));

        TextView emailText = findViewById(R.id.sign_in_email);
        TextView passwordText = findViewById(R.id.sign_in_password);
        TextView orStartWith = findViewById(R.id.sign_in_or_start_with);

        emailText.setText(Utils.getLangString(this, curLang, R.string.email));
        passwordText.setText(Utils.getLangString(this, curLang, R.string.password));
        orStartWith.setText(Utils.getLangString(this, curLang, R.string.or_start_with));

    }
}
