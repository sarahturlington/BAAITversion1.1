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
    public Button addpatch;//add patch button
    public Button sendCSV; //send csv button
    public EditText email; //input for the email
    //defines all of the behaviour of the UI
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view=inflater.inflate(R.layout.tab_three,container,false);
    addpatch = (Button) view.findViewById(R.id.button);
    email = (EditText) view.findViewById(R.id.editText);
    addpatch.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) { //creates an alet button on pressing the button
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity()); // gets the activity of the current fragment
            builder.setCancelable(true);//can click out of
            builder.setMessage("Patches Available to Add: None");// placeholder text
            builder.setTitle("Adding Patches");//placeholder text
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();//cancel on press
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();// cancel on press
                }
            });
            builder.show();//show the alert
        }

    });


    sendCSV = (Button) view.findViewById(R.id.button2);
    sendCSV.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {//creates an alert button on the pressin of the button
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());//get the fragment activity
            builder.setCancelable(true);// can click out of
            builder.setMessage("Sending Compiled Data");//placeholder text
            builder.setTitle("Sending Data to: " + email.getText()); // display email to send the data to
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();//cancel the alert
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();//cancel alert
                }
            });
            builder.show();//display the buttons
        }
    });
    return view;}
}
