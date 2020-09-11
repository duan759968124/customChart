package com.example.chartcolumn;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private HistogramView customView;
    private List<Long> mDatas;
    private List<String> mNames;
    private int MaxY;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        customView = findViewById(R.id.customView);
        customView.setMaxY(999);
        customView.setmLineColor(getResources().getColor(R.color.colorLine));
        customView.setmGeenColor(getResources().getColor(R.color.colorAccent));
        customView.setmTextColor(getResources().getColor(R.color.colorText));
        mDatas = new ArrayList<>();
//        mDatas.add(85l);
//        mDatas.add(76l);
//        mDatas.add(66l);
        mDatas.add(555L);
        mDatas.add(885L);
        mNames = new ArrayList<>();
//        mNames.add("9.8");
//        mNames.add("9.9");
//        mNames.add("9.10");
        mNames.add("9.11");
        mNames.add("9.12");
        customView.updateThisData(mDatas,mNames);
    }
}