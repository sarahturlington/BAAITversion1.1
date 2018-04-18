package com.example.sarah.baait_version1;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ToggleButton;


/**
 * Created by sarah on 4/3/2018.
 */

public class TabOne extends Fragment

{
    public ToggleButton toggleButton;
    public double Temperature;

    @Nullable
    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view=inflater.inflate(R.layout.tab_one,container,false);
    Temperature=99.3;



    return view;}



}