package com.example.sarah.baait_version1;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


;

/**
 * Created by sarah on 4/3/2018.
 */

public class TabThree extends Fragment {
    public Button addpatch; //button to add a patch
    public Button sendCSV; //button to send as as csv
    public EditText email; //area to put your email
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view=inflater.inflate(R.layout.tab_three,container,false);
    addpatch = (Button) view.findViewById(R.id.button); //define from xml
    email = (EditText) view.findViewById(R.id.editText); //define from xml
    addpatch.setOnClickListener(new View.OnClickListener() {//define the functionality of the addapatch button
        @Override
        public void onClick(View view) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity()); //simple alert message dialog
            builder.setCancelable(true);
            builder.setMessage("Patches Available to Add: None");
            builder.setTitle("Adding Patches");
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {//ok button
                    dialogInterface.cancel();
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {//cancel button
                    dialogInterface.cancel();
                }
            });
            builder.show();
        }

    });


    sendCSV = (Button) view.findViewById(R.id.button2);//define form the xml
    sendCSV.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) { //define the functionality of the add a patch button
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());//simple alert message
            builder.setCancelable(true);
            builder.setMessage("Sending Compiled Data");
            builder.setTitle("Sending Data to: " + email.getText());
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {//ok button
                    dialogInterface.cancel();
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {//cancel button
                    dialogInterface.cancel();
                }
            });
            builder.show();
        }
    });
    return view;}
}
