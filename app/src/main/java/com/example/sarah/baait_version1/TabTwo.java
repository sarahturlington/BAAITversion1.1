package com.example.sarah.baait_version1;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.Series;


import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by sarah on 4/3/2018.
 */

public class TabTwo extends Fragment {
    public GraphView graph;
    public LineGraphSeries<DataPoint> series = null;
    int maxlength = 30;
    int x_value = 0;
    public boolean isCelcius;
    ArrayList<DataPoint> datapoint = new ArrayList<DataPoint>(); //holds te data for future use
    ArrayList<String> times = new ArrayList<String>();//hold the times for future use

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_two, container, false);
        graph = (GraphView) view.findViewById(R.id.graph);
        series = new LineGraphSeries<DataPoint>();
        graph.addSeries(series);
        //set the qualities of the graph
        graph.setTitle("Temperature");
        graph.getViewport().setScrollable(true);
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMinY(70);
        graph.getViewport().setMaxY(120);
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(50);
        series.setOnDataPointTapListener(new OnDataPointTapListener() {
            @Override
            public void onTap(Series series, DataPointInterface dataPointInterface) { //on tap displays a toast message with the time collected and the temperature value
                Toast.makeText(getActivity(), "Temperature: " + Double.toString(dataPointInterface.getY()) + "@ " + times.get((int)dataPointInterface.getX()).toString(), Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }


    protected void displayData(String message){//adds the message to the graph
        //testing.setText(message);
        double data = 0;
        try{
            data = Double.parseDouble(message);//try catch in case of a non number input
        }
        catch (Exception e){
           return;
        }
        DataPoint mdataPoint = new DataPoint(x_value, data);
        series.appendData(mdataPoint, true, 500 );//add to the graph
        datapoint.add(mdataPoint);
       //add current time as a string to the times array for use in toast messages
        Time now = new Time();//
        now.setToNow();//
        times.add(x_value, now.toString());// plaves
        x_value++;//increment to make sure sequential points
        return;
    }

    public void changeFormat(Boolean isCelsius){//converts entire graph to celcius and back, will make later


    }


}
