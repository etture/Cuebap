package com.jinoolee.cuebap.SignInCreateAccount;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.jinoolee.cuebap.BaseActivity;
import com.jinoolee.cuebap.Helper.Utils;
import com.jinoolee.cuebap.R;

import java.util.ArrayList;

public class CampusSearchActivity extends BaseActivity {

    private static final String TAG = "CampusSearchActivity";

    private ListView listView;
    private CampusItemAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_campus_search);

        Toolbar toolbar = this.findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //ActionBar set text to center, with back button
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(R.layout.actionbar_layout);

        TextView actionBarTitle = actionBar.getCustomView().findViewById(R.id.actionbar_title);
        actionBarTitle.setText(Utils.getLangString(this, curLang, R.string.choose_your_campus));

        //Code for displaying back button --> true: back button, false: no back button
        actionBar.setDisplayHomeAsUpEnabled(true);
        //ActionBar configuration DONE

        listView = findViewById(R.id.campus_listView);
        adapter = new CampusItemAdapter();

        adapter.addItem(new CampusListItem(R.string.yonsei_university, R.string.sinchon_campus));
        adapter.addItem(new CampusListItem(R.string.yonsei_university, R.string.international_campus));
        adapter.addItem(new CampusListItem(R.string.yonsei_university, R.string.wonju_campus));

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                CampusListItem item = (CampusListItem) adapter.getItem(i);

                int school = item.getSchool();
                int campus = item.getCampus();

                showDialog(Utils.getLangString(getApplicationContext(), curLang, school),
                        Utils.getLangString(getApplicationContext(), curLang, campus));

                /*
                Pattern pattern = Pattern.compile("(.*?) University");
                Matcher matcher = pattern.matcher(schoolStr);

                String school = null;

                if (matcher.find()) {
                    school = matcher.group(1);
                }
                */

            }
        });

        layoutElementStringSetup();
    }

    @Override
    protected void layoutElementStringSetup() {

    }

    private void showDialog(String school, String campus){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(Utils.getLangString(this, curLang, R.string.choose_campus));
        builder.setMessage(Utils.getLangString(this, curLang, R.string.are_you_willing_to_choose, school, campus));

        final String schoolStr = school;
        final String campusStr = campus;

        builder.setPositiveButton(Utils.getLangString(this, curLang, R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                Log.d(TAG, "Yes button clicked");

                Intent intent = new Intent();
                intent.putExtra("school", schoolStr);
                intent.putExtra("campus", campusStr);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        builder.setNegativeButton(Utils.getLangString(this, curLang, R.string.no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Log.d(TAG, "No button clicked");
            }
        });

        builder.setNeutralButton(Utils.getLangString(this, curLang, R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Log.d(TAG, "Cancel button clicked");
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

    }

    @Override
    public void onBackPressed() {

        setResult(RESULT_CANCELED);
        finish();

    }

    class CampusItemAdapter extends BaseAdapter{

        ArrayList<CampusListItem> items = new ArrayList<>();

        public void addItem(CampusListItem item){
            items.add(item);
        }

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object getItem(int i) {
            return items.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View convertView, ViewGroup viewGroup) {

            CampusListItemView view = new CampusListItemView(getApplicationContext());
            CampusListItem item = items.get(i);
            view.setSchoolText(Utils.getLangString(getApplicationContext(), curLang, item.getSchool()));
            view.setCampusText(Utils.getLangString(getApplicationContext(), curLang, item.getCampus()));

            return view;

        }
    }
}
