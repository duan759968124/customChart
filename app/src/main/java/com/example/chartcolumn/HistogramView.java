package com.example.chartcolumn;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.List;

public class HistogramView extends View {

    private int Colum = 1;
    private int Line = 2;

    private Path mPathLine;

    //定义画笔
    private Paint mLinePaint;
    private Paint mGreenPaint;
    private Paint mTextPaint;
    private Paint mStopPaint;
    private Paint mBrokenLine;

    //定义上下文
    private Context mContext;
    //定义宽高
    private float width_start;
    private float weight;
    private float height;
    private float mScale;//
    private float mgTop; //
    //这个数组是高度的值
    private String[] y_title = new String[6];
    //分别定义数据源跟数据源名称集合
    private List<String> mData;
    private List<String> mNames;

    //定义颜色
    private int mLineColor;
    private int mGeenColor;
    private int mTextColor;
    private int mTextDataColor;
    private int MaxY;
    //数值与柱条的距离
    private int dis_data = 50;
    //柱条名称距离x轴的距离
    private int dis_nameX = 20;

    private float startX = 0.0F, startY = 0.0F, endX = 0.0F, endY = 0.0F;
    private int Type = 1; // 2：折线  1：柱状图

    public void setType(int type)
    {
        Type = type;
    }

    public void setmLineColor(int mLineColor)
    {
        this.mLineColor = mLineColor;
        mLinePaint.setColor(mLineColor);
    }

    public void setmGeenColor(int mGeenColor)
    {
        this.mGeenColor = mGeenColor;
        mGreenPaint.setColor(mGeenColor);
        mStopPaint.setColor(mGeenColor);
        mBrokenLine.setColor(mGeenColor);
    }

    public void setmTextColor(int mTextColor)
    {
        this.mTextColor = mTextColor;
        mTextPaint.setColor(mTextColor);
    }

    public void setmTextDataColor(int mTextDataColor)
    {
        this.mTextDataColor = mTextDataColor;
    }

    public void setMaxY(int maxY)
    {
        MaxY = Math.round(((float) maxY / 10) + (float) 5 / 10) * 10;  //让最大值向上取整得到y轴坐标点
        int unit = MaxY / 5;
        for (int i = 5; i >= 0; i--) {
            y_title[5 - i] = (unit * i) + "";
        }
    }

    public HistogramView(Context context)
    {
        super(context);
    }

    public HistogramView(Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);
        //给定义的画笔进行加工
        mContext = context;

        mPathLine = new Path();

        mLinePaint = new Paint();
        mGreenPaint = new Paint();
        mTextPaint = new Paint();
        mStopPaint = new Paint();
        mBrokenLine = new Paint();

        mGreenPaint.setStyle(Paint.Style.FILL);
        mStopPaint.setStyle(Paint.Style.FILL);
        mBrokenLine.setStyle(Paint.Style.STROKE);

        mTextPaint.setAntiAlias(true);
        mGreenPaint.setAntiAlias(true);
        mLinePaint.setAntiAlias(true);
        mStopPaint.setAntiAlias(true);
        mBrokenLine.setAntiAlias(true);

        mStopPaint.setStrokeWidth(2);
        mBrokenLine.setStrokeWidth(2);

    }

    //尺寸发生改变的时候调用
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh)
    {
        super.onSizeChanged(w, h, oldw, oldh);
        weight = 0.7F * w;
        width_start = 0.3F * w / 2;
        height = 0.70F * h;
    }

    //绘制
    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        mScale = 1;
        mgTop = 50;
        float min_height = height / 5;
        for (int i = 5; i >= 0; i--) {
            //调整画笔的颜色
            if (i == 5) {
                mLinePaint.setColor(mLineColor);
            } else {
                mLinePaint.setColor(mLineColor);
            }
            //绘制y轴对应横线
            canvas.drawLine(70 * mScale + width_start / 2, 30 * mScale + min_height * i + mgTop, 70 * mScale + weight + width_start, 30 * mScale + min_height * i + mgTop, mLinePaint);
            mTextPaint.setTextAlign(Paint.Align.RIGHT);
            mTextPaint.setTextSize(40 * mScale);
            //绘制y轴数值
            canvas.drawText(y_title[i], 55 * mScale + width_start / 2, 30 * mScale + min_height * i + mgTop + 10, mTextPaint);
        }

        float min_weight = 0.0F;
        switch (Type) {
            case 1:
                //每个柱条的宽度
                float colum_Weight = 60;
                //平均每一个柱条的间隔
                min_weight = (weight + width_start / 2 - colum_Weight * mData.size()) / mData.size();
                mTextPaint.setTextSize(42 * mScale);
                mTextPaint.setTextAlign(Paint.Align.CENTER);
                for (int i = 0; i < mData.size(); i++) {

                    float leftR = (float) (70 * mScale + width_start / 2 //x初始位置
                            + ((weight + width_start / 2) - min_weight * (mData.size() - 1) - colum_Weight * (mData.size())) / 2  //除去柱条加间隔剩余的宽度
                            + (i * (min_weight + colum_Weight))   //每增加一个 间隔宽度跟之前柱条宽度
                    );
                    float rightR = leftR + colum_Weight;   // (int) (min_weight / 2)


                    float buttomR = (float) (colum_Weight / 2 * mScale + min_height * 5 + mgTop);
                    float topR = buttomR - (float) (height / MaxY * Float.parseFloat(mData.get(i)));

                    canvas.drawRect(new RectF(leftR, topR, rightR, buttomR), mGreenPaint); //绘制柱条

                    //x轴对应柱条的名称
                    mTextPaint.setColor(mTextColor);
                    canvas.drawText(mNames.get(i), leftR + colum_Weight / 2, buttomR + dis_data * mScale, mTextPaint);
                    mTextPaint.setColor(mTextDataColor);
                    //柱条上的数值
                    canvas.drawText(mData.get(i) + "", leftR + colum_Weight / 2, topR - dis_nameX * mScale, mTextPaint);
                }
                break;
            case 2:
                min_weight = (weight + width_start / 2) / mData.size();
                mTextPaint.setTextSize(42 * mScale);
                mTextPaint.setTextAlign(Paint.Align.CENTER);

                startX = (70 * mScale + width_start / 2);//x初始位置
                startY = (min_height * 5 + mgTop);

                for (int i = 0; i < mData.size(); i++) {

                    float leftR = (70 * mScale + width_start / 2 //x初始位置
                            + ((weight + width_start / 2) - min_weight * (mData.size() - 1)) / 2  //除去间隔剩余的宽度
                            + (i * (min_weight))   //每增加一个 间隔宽度跟之前柱条宽度
                    );
                    float rightR = leftR;   // (int) (min_weight / 2)
                    float buttomR = (min_height * 5 + mgTop) + 30 * mScale;
                    float topR = buttomR - (height / MaxY * Float.parseFloat(mData.get(i)));

                    endX = leftR;
                    endY = topR;
                    if (i==0){
                        mPathLine.moveTo(endX,endY);
                    }else{
                        mPathLine.lineTo(endX,endY);
//                        canvas.drawLine(startX, startY, endX, endY, mGreenPaint); //绘制折线
                        canvas.drawCircle(startX,startY,10,mStopPaint);
                        if (i==mData.size()-1){
                            canvas.drawCircle(endX,endY,10,mStopPaint);
                        }
                    }
                    canvas.drawPath(mPathLine,mBrokenLine);

                    startX = endX;
                    startY = endY;

                    //x轴对应柱条的名称
                    mTextPaint.setColor(mTextColor);
                    canvas.drawText(mNames.get(i), leftR, buttomR + dis_data * mScale, mTextPaint);
                    mTextPaint.setColor(mTextDataColor);
                    //柱条上的数值
                    canvas.drawText(mData.get(i) + "", leftR, topR - dis_nameX * mScale, mTextPaint);
                }
                break;
        }


    }

    //传入数据并进行绘制
    public void updateThisData(List<String> data, List<String> name)
    {
        mData = data;
        mNames = name;
        invalidate();
    }
}
