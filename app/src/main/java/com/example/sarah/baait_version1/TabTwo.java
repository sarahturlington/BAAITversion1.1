package com.example.sarah.baait_version1;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.w3c.dom.Text;

/**
 * Created by sarah on 4/3/2018.
 */

public class TabTwo extends Fragment {
    public GraphView graph;
    public LineGraphSeries<DataPoint> series = null;
    int maxlength = 30;
    double x_value = 0;
    public boolean isCelcius;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_two, container, false);
        graph = (GraphView) view.findViewById(R.id.graph);
        series = new LineGraphSeries<DataPoint>();
        graph.addSeries(series);
        graph.setTitle("Temperature");
        graph.getViewport().setScrollable(true);
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMinY(70);
        graph.getViewport().setMaxY(120);
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(50);


        return view;
    }


    protected void displayData(String message){
        //testing.setText(message);
        double data = 0;
        try{
            data = Double.parseDouble(message);
        }
        catch (Exception e){
           return;
        }
        series.appendData(new DataPoint(x_value, data), true, 500 );
        x_value++;
        return;
    }

    public void changeFormat(Boolean isCelsius){


    }


}
