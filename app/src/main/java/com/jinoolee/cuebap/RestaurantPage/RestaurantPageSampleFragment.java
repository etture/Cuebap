package com.jinoolee.cuebap.RestaurantPage;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jinoolee.cuebap.Helper.Utils;
import com.jinoolee.cuebap.R;

public class RestaurantPageSampleFragment extends Fragment {

    TextView serviceInPrep;

    //Language
    private String curLang;

    public static RestaurantPageSampleFragment createInstance(){
        return new RestaurantPageSampleFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_restaurant_page_sample, container, false);

        Bundle receivedBundle = getArguments();
        curLang = receivedBundle.getString("curLang");

        serviceInPrep = view.findViewById(R.id.sample_fragment_service_in_preparation);

        layoutElementStringSetup();

        return view;
    }

    private void layoutElementStringSetup(){

        serviceInPrep.setText(Utils.getLangString(getContext(), curLang, R.string.service_in_preparation));

    }
}
