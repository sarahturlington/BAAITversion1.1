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

public class TabTwo extends Fragment


{
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_two, container, false);
        GraphView graph = (GraphView) view.findViewById(R.id.graph);
        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(new
                DataPoint[]{
                new DataPoint(0, 98.6),
                new DataPoint(1, 99),
                new DataPoint(2, 99),
                new DataPoint(3, 99.2),
                new DataPoint(4, 99.3)
        });
        graph.addSeries(series);
        return view;
    }
}
