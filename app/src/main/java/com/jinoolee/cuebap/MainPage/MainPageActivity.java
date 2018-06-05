package com.jinoolee.cuebap.MainPage;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jinoolee.cuebap.CheckOutPage.CartActivity;
import com.jinoolee.cuebap.CheckOutPage.CheckOutActivity;
import com.jinoolee.cuebap.Data.CurrentUser;
import com.jinoolee.cuebap.Helper.AnimatedExpandableListView;
import com.jinoolee.cuebap.BaseActivity;
import com.jinoolee.cuebap.Data.BuildingsAndRestaurants;
import com.jinoolee.cuebap.Helper.MyDebug;
import com.jinoolee.cuebap.Helper.Utils;
import com.jinoolee.cuebap.R;
import com.jinoolee.cuebap.Data.RC;
import com.jinoolee.cuebap.RecyclerViewItems.BuildingItem;
import com.jinoolee.cuebap.RecyclerViewItems.RestaurantItem;
import com.jinoolee.cuebap.RestaurantPage.RestaurantActivity;
import com.jinoolee.cuebap.Data.User;
import com.jinoolee.cuebap.SignInCreateAccount.SignInActivity;
import com.jinoolee.cuebap.SplashActivity;

import java.util.List;
import java.util.Locale;

public class MainPageActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private static final String TAG = "MainPageActivity";

    private static final int KOREAN = 123;
    private static final int ENGLISH = 152;

    private TextView navHeaderName, navHeaderId, navHeaderCampus;
    private TextView changeLanguageBtn;
    private int lang = KOREAN;

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();

    //AnimatedExpandableListView variables
    private AnimatedExpandableListView mainPageListView;
    private MainPageAdapter adapter;

    //Menu
    private Menu myMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //ActionBar set text to center, with back button
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(R.layout.actionbar_logo_layout);

        ImageView actionBarLogo = actionBar.getCustomView().findViewById(R.id.actionbar_logo);

        //Code for displaying back button --> true: back button, false: no back button
        actionBar.setDisplayHomeAsUpEnabled(false);
        //ActionBar configuration DONE

        Intent intent = getIntent();

        /*
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        */

        /* FloatingActionButton XML code
        <android.support.design.widget.FloatingActionButton
        android:id="@+id/fab"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_gravity="bottom|end"
        android:layout_margin="@dimen/fab_margin"
        app:srcCompat="@android:drawable/ic_dialog_email" />
         */

        //Navigation drawer setup
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Set menu instance so it can be used to change the language on the menu items
        myMenu = navigationView.getMenu();

        changeLanguageBtn = navigationView.findViewById(R.id.change_language_btn);
        changeLanguageBtn.setOnClickListener(this);

        //Get reference to header layout in NavigationView
        View headerLayout = navigationView.getHeaderView(0);

        navHeaderName = headerLayout.findViewById(R.id.nav_header_name);
        navHeaderCampus = headerLayout.findViewById(R.id.nav_header_campus);
        //Works!


        //Display "Signed in." or "Signed out." depending on where the activity came from
        String signInOrSignUp = null;
        if(intent.getStringExtra("from").equals("SignInActivity")){

            signInOrSignUp = Utils.getLangString(this, curLang, R.string.signed_in);
            showSnackbar(R.id.drawer_layout, signInOrSignUp);

        }else if(intent.getStringExtra("from").equals("CreateAccountActivity") ||
                intent.getStringExtra("from").equals("CreateAccountOtherActivity")){

            signInOrSignUp = Utils.getLangString(this, curLang, R.string.signed_up);
            showSnackbar(R.id.drawer_layout, signInOrSignUp);

        }else{

        }


        //Main building and restaurant ListView settings
        BuildingsAndRestaurants bnr = new BuildingsAndRestaurants();
        List<BuildingItem> items = bnr.getBuildings();

        adapter = new MainPageAdapter(this);
        adapter.setData(items);

        mainPageListView = findViewById(R.id.main_page_list_view);
        mainPageListView.setAdapter(adapter);
        mainPageListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View view, int groupPosition, long id) {
                if(mainPageListView.isGroupExpanded(groupPosition)){
                    mainPageListView.collapseGroupWithAnimation(groupPosition);
                }else{
                    mainPageListView.expandGroupWithAnimation(groupPosition);
                }
                return true;
            }
        });

        mainPageListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int groupPosition, int childPosition, long id) {
                RestaurantItem item = (RestaurantItem) adapter.getChild(groupPosition, childPosition);
                Intent intent = new Intent(getApplicationContext(), RestaurantActivity.class);
                intent.putExtra("restaurant_name", item.getName());
                intent.putExtra("restaurant_image", item.getImage());
                intent.putExtra("buildingIndex", groupPosition);
                intent.putExtra("restaurantIndex", childPosition);
                startActivity(intent);
                return true;
            }
        });

        //Change language string in all layout elements
        layoutElementStringSetup();

    }

    private static class BuildingHolder{
        TextView name;
        TextView away;
        ImageView arrowUp;
        ImageView arrowDown;
    }

    private static class RestaurantHolder{
        TextView name;
        TextView time;
        TextView waiting;
    }

    public class MainPageAdapter extends AnimatedExpandableListView.AnimatedExpandableListAdapter{

        private LayoutInflater inflater;
        private List<BuildingItem> items;

        public MainPageAdapter(Context context){
            inflater = LayoutInflater.from(context);
        }

        public void setData(List<BuildingItem> items){
            this.items = items;
        }

        @Override
        public View getRealChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

            RestaurantHolder holder;
            RestaurantItem item = (RestaurantItem) getChild(groupPosition, childPosition);

            if(convertView == null){
                holder = new RestaurantHolder();
                convertView = inflater.inflate(R.layout.restaurant_item, parent, false);
                holder.name = convertView.findViewById(R.id.restaurant_name);
                holder.time = convertView.findViewById(R.id.restaurant_time);
                holder.waiting = convertView.findViewById(R.id.restaurant_waiting);
                convertView.setTag(holder);
            }else{
                holder = (RestaurantHolder) convertView.getTag();
            }

            holder.name.setText(Utils.getLangString(getApplicationContext(), curLang, item.getName()));
            holder.time.setText(Utils.getLangString(getApplicationContext(), curLang, R.string.restaurant_time, item.getStartTime(), item.getEndTime()));
            holder.waiting.setText(Utils.getLangString(getApplicationContext(), curLang, R.string.restaurant_waiting, item.getWaiting()));

            return convertView;
        }

        @Override
        public int getRealChildrenCount(int groupPosition) {
            return items.get(groupPosition).items.size();
        }

        @Override
        public int getGroupCount() {
            return items.size();
        }

        @Override
        public Object getGroup(int groupPosition) {
            return items.get(groupPosition);
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return items.get(groupPosition).items.get(childPosition);
        }

        @Override
        public long getGroupId(int groupPosition) {
            return 0;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

            BuildingHolder holder;
            BuildingItem item = (BuildingItem) getGroup(groupPosition);
            //Current language
            curLang = langPref.getString("currentLanguage", "en");

            if(convertView == null){
                holder = new BuildingHolder();
                convertView = inflater.inflate(R.layout.building_item, parent, false);
                holder.name = convertView.findViewById(R.id.building_name);
                holder.away = convertView.findViewById(R.id.building_away);
                holder.arrowUp = convertView.findViewById(R.id.building_arrow_up);
                holder.arrowDown = convertView.findViewById(R.id.building_arrow_down);
                convertView.setTag(holder);
            }else{
                holder = (BuildingHolder) convertView.getTag();
            }

            holder.name.setText(Utils.getLangString(getApplicationContext(), curLang, item.getName()));
            holder.away.setText(Utils.getLangString(getApplicationContext(), curLang, R.string.building_km_away, item.getAway()));

            if(isExpanded){
                holder.arrowUp.setVisibility(View.VISIBLE);
                holder.arrowDown.setVisibility(View.INVISIBLE);
            }else{
                holder.arrowUp.setVisibility(View.INVISIBLE);
                holder.arrowDown.setVisibility(View.VISIBLE);
            }

            return convertView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }


    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser user = mAuth.getCurrentUser();

        //Update navigation drawer to display user's name and campus
        DatabaseReference userRef = databaseReference.child("users").child(user.getUid());

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);

                CurrentUser currentUser = CurrentUser.getCurrentUser();
                currentUser.setCurrentUser(user);

                String name = user.getName();
                String school = user.getSchool();
                String campus = user.getCampus();

                navHeaderName.setText(name);
                navHeaderCampus.setText(school + " " + campus);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        //navHeaderCampus.setText();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Intent intent;

        if (id == R.id.nav_my_page) {

            intent = new Intent(this, MyPageActivity.class);
            startActivityForResult(intent, RC.MY_PAGE);

        } else if (id == R.id.nav_mobile_receipt) {

            intent = new Intent(this, CheckOutActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_cart) {

            intent = new Intent(this, CartActivity.class);
            intent.putExtra("from", "main_page");
            startActivity(intent);

        } else if (id == R.id.nav_favorites) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RC.MY_PAGE){

            if(data.getBooleanExtra("sign_out", false)){
                setResult(RESULT_OK);

                Intent intent = new Intent(this, SignInActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

                finish();
            }

        }else if(requestCode == RC.MOBILE_RECEIPT){

        }else if(requestCode == RC.CART){

        }else if(requestCode == RC.FAVORITES){

        }else if(data == null){

        }
    }

    @Override
    public void onClick(View view) {

        int id = view.getId();

        if(id == changeLanguageBtn.getId()){

            if(MyDebug.LOG){
                Log.d(TAG, "Current locale language: " + this.getResources().getConfiguration().locale.getLanguage());
            }

            if(langPref.getString("currentLanguage", "ko").equals("en")){

                editor = langPref.edit();
                editor.putString("currentLanguage", "ko");
                editor.commit();

            }else{

                editor = langPref.edit();
                editor.putString("currentLanguage", "en");
                editor.commit();

            }

            setResult(RESULT_OK);
            Intent intent = new Intent(this, SplashActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

            finish();

        }

    }

    @NonNull
    private Configuration getEnglishConfiguration() {
        Configuration configuration = new Configuration(this.getResources().getConfiguration());

        configuration.setLocale(new Locale("en"));
        return configuration;
    }

    @Override
    protected void layoutElementStringSetup(){

        TextView change_language_btn = findViewById(R.id.change_language_btn);
        change_language_btn.setText(Utils.getLangString(this, curLang, R.string.change_language));

        TextView main_page_you_are_here = findViewById(R.id.main_page_you_are_here);
        TextView main_page_bottom_location = findViewById(R.id.main_page_bottom_location);
        main_page_you_are_here.setText(Utils.getLangString(this, curLang, R.string.you_are_here));
        main_page_bottom_location.setText(Utils.getLangString(this, curLang, R.string.new_millennium_hall));

        //Navigation view items
        MenuItem nav_my_page = myMenu.findItem(R.id.nav_my_page);
        MenuItem nav_mobile_receipt = myMenu.findItem(R.id.nav_mobile_receipt);
        MenuItem nav_cart = myMenu.findItem(R.id.nav_cart);
        MenuItem nav_favorites = myMenu.findItem(R.id.nav_favorites);

        nav_my_page.setTitle(Utils.getLangString(this, curLang, R.string.my_page));
        nav_mobile_receipt.setTitle(Utils.getLangString(this, curLang, R.string.mobile_receipt));
        nav_cart.setTitle(Utils.getLangString(this, curLang, R.string.cart));
        nav_favorites.setTitle(Utils.getLangString(this, curLang, R.string.favorites));

    }

}
