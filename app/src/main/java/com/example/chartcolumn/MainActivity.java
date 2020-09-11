package com.example.chartcolumn;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private HistogramView customView;
    private List<String> mDatas;
    private List<String> mNames;
    private int MaxY;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        customView = findViewById(R.id.customView);
        customView.setMaxY(50);
        customView.setmLineColor(getResources().getColor(R.color.colorLine));
        customView.setmGeenColor(getResources().getColor(R.color.colorAccent));
        customView.setmTextColor(getResources().getColor(R.color.colorText));
        customView.setmTextDataColor(getResources().getColor(R.color.color979797));
        mDatas = new ArrayList<>();
        mDatas.add("40");
        mDatas.add("20");
        mDatas.add("15");
        mDatas.add("15");
        mDatas.add("10");
        mDatas.add("20");
        mDatas.add("15");
        mNames = new ArrayList<>();
        mNames.add("5.1");
        mNames.add("5.2");
        mNames.add("5.3");
        mNames.add("5.4");
        mNames.add("5.5");
        mNames.add("5.6");
        mNames.add("5.7");
        customView.updateThisData(mDatas,mNames);
    }
}