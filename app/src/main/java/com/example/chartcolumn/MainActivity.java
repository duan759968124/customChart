package com.example.chartcolumn;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

        private HistogramView customView;
//    private HistogramView_polyline customView_polyline;
    private List<Object> mDatas;
    private List<String> mNames;
    private float MaxY=50F;//y轴最大值

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//设置图表及画笔属性
        customView = findViewById(R.id.customView);
        customView.setMaxY(MaxY);
        customView.setType(1);
        customView.setScroll(true);
        customView.setmLineColor(getResources().getColor(R.color.colorLine));
        customView.setmGeenColor(getResources().getColor(R.color.colorAccent));
        customView.setmTextColor(getResources().getColor(R.color.colorText));
        customView.setmTextDataColor(getResources().getColor(R.color.color979797));
        customView.setOnChartClickListener(new HistogramView.OnChartClickListener() {
            @Override
            public void onClick(int num, float x, float y, float value)
            {
                Log.e("TAG", "onClick: " +num + " x: " + x + " y: " + y + " value: " + value);
                Toast.makeText(MainActivity.this, "value:   "+ value, Toast.LENGTH_SHORT).show();
            }
        });

//        customView_polyline = findViewById(R.id.customView_polyline);
//        customView_polyline.setMaxY(50);
//        customView_polyline.setmLineColor(getResources().getColor(R.color.colorLine));
//        customView_polyline.setmGeenColor(getResources().getColor(R.color.colorAccent));
//        customView_polyline.setmTextColor(getResources().getColor(R.color.colorText));
//        customView_polyline.setmTextDataColor(getResources().getColor(R.color.color979797));
//初始化数据
        mDatas = new ArrayList<>();
        mDatas.add("40");
        mDatas.add("20");
        mDatas.add("15");
        mDatas.add("15");
        mDatas.add("12");
        mDatas.add("24");
        mDatas.add("15");
        mDatas.add("40");
        mDatas.add("20");
        mDatas.add("15");
        mDatas.add("40");
        mDatas.add("20");
        mDatas.add("15");
        //
        mDatas.add("40");
        mDatas.add("20");
        mDatas.add("15");
        mDatas.add("15");
        mDatas.add("12");
        mDatas.add("24");
        mDatas.add("15");
        mDatas.add("40");
        mDatas.add("20");
        mDatas.add("15");
        mDatas.add("40");
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
        mNames.add("5.8");
        mNames.add("5.9");
        mNames.add("5.10");
        mNames.add("5.11");
        mNames.add("5.12");
        mNames.add("5.13");
        //
        mNames.add("5.1");
        mNames.add("5.2");
        mNames.add("5.3");
        mNames.add("5.4");
        mNames.add("5.5");
        mNames.add("5.6");
        mNames.add("5.7");
        mNames.add("5.8");
        mNames.add("5.9");
        mNames.add("5.10");
        mNames.add("5.11");
        mNames.add("5.12");
        mNames.add("5.13");
//更新图表
        customView.updateThisData(mDatas,mNames);
//        customView_polyline.updateThisData(mDatas, mNames);
    }
}