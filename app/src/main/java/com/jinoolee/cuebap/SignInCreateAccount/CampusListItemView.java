package com.jinoolee.cuebap.SignInCreateAccount;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jinoolee.cuebap.R;

public class CampusListItemView extends LinearLayout {

    TextView schoolText, campusText;

    public CampusListItemView(Context context) {
        super(context);
        init(context);
    }

    public CampusListItemView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context){

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.campus_list_item, this, true);

        schoolText = findViewById(R.id.school_text);
        campusText = findViewById(R.id.campus_text);

    }

    public void setSchoolText(String school){
        schoolText.setText(school);
    }

    public void setCampusText(String campus){
        campusText.setText(campus);
    }


}
