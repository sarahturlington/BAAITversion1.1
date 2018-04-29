package com.example.sarah.baait_version1;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

/**
 * Created by sarah on 4/3/2018.
 */

public class TabTwo extends Fragment implements graphControl


{
    public GraphView graph;
    public LineGraphSeries<DataPoint> series = null;
    int maxlength = 30;
    long x_value = 0;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_two, container, false);
        graph = (GraphView) view.findViewById(R.id.graph);
        series = new LineGraphSeries<DataPoint>();
        series.appendData(new DataPoint(0, 98.6),true,maxlength);
        series.appendData(new DataPoint(1,98.1), true, maxlength);
        graph.addSeries(series);
        return view;
    }
    @Override
    public void addToGraph(String s){
        double data = 0;
        try{
            data = Double.parseDouble(s);
        }
        catch(Exception e){
            return;
        }

        series.appendData(new DataPoint(x_value,data),true, maxlength);
        x_value++;
    }

    public boolean addToGraph(int y){
        series.appendData(new DataPoint(x_value,y),true, maxlength);
        x_value++;
        graph.addSeries(series);
        return true;

    }


}
