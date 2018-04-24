package com.example.sarah.baait_version1;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.sarah.baait_version1.R;
import android.widget.TextView;
import android.widget.ToggleButton;



/**
 * Created by sarah on 4/3/2018.
 */

public class TabOne extends Fragment

{
   // public BtSerial blue = null;
    public ToggleButton toggleButton = null;
    public double Temperature;
    public TextView temp = null;
    public boolean CvF = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view=inflater.inflate(R.layout.tab_one,container,false);
    temp = (TextView) view.findViewById(R.id.textView4);
    ToggleButton toggle = (ToggleButton) view.findViewById(R.id.toggleButton);
   // blue = new BtSerial(this.getContext());
    toggle.setOnClickListener(new View.OnClickListener(){
        @Override
        public void onClick(View v){
            if(CvF == false){
                CvF = true;
                temp.setText("Fahrenheit");
            }
            else{CvF = false;
                temp.setText("Celsius");}
        }
        });
    return view;}

}

