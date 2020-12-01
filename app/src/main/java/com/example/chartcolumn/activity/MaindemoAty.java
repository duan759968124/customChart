package com.example.chartcolumn.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.chartcolumn.R;
import com.example.chartcolumn.adapter.ManageCenterAdapter;
import com.example.chartcolumn.util.Utiles;

import java.util.ArrayList;
import java.util.List;

public class MaindemoAty extends AppCompatActivity implements View.OnClickListener {

    private Button Btn_achartview;
    RecyclerView rlv_fieryaty;

    //
    private List<Object> mDatas;
    private List<String> mNames;
    private List<String> mIds;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maindemo_aty);

        rlv_fieryaty = findViewById(R.id.rlv_fieryaty);

        initDatas();
    }

    private void initDatas()
    {
        mDatas = new ArrayList<>();
        mIds = new ArrayList<>();
        mNames = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            mDatas.add("15");
            mIds.add("123456");
            mNames.add("旅游文化");
        }
        float max = (float) Utiles.main(mDatas);//y轴最大值
        LinearLayoutManager layoutManager = new LinearLayoutManager(MaindemoAty.this);
        layoutManager.setOrientation(RecyclerView.HORIZONTAL);
        rlv_fieryaty.setLayoutManager(layoutManager);
        ManageCenterAdapter adapter_test = new ManageCenterAdapter(MaindemoAty.this);
        adapter_test.setmDatas(mDatas);
        adapter_test.setmNames(mIds);
        adapter_test.setmNums(mNames);
        adapter_test.setMaxY(max);
        adapter_test.setmOnItemClickListener(new ManageCenterAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, String data)
            {
                Log.i("TAG", "onItemClick: " + data);
            }
        });
        rlv_fieryaty.setAdapter(adapter_test);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId()){

        }
    }
}