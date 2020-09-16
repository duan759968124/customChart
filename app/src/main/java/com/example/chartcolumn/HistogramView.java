package com.example.chartcolumn;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class HistogramView extends View {

    private int ScreenWidth = 0;

    //定义画笔
    private Paint mLinePaint; //图表线
    private Paint mGreenPaint; //柱状图、折线图主题色
    private Paint mTextPaint; //文字颜色


    //定义上下文
    private Context mContext;
    //定义宽高
    private float width_start; //图表开始位置的x坐标
    private float weight; //图表宽度
    private float height; //图表高度
    private float mScale;//
    private float mgTop; //距顶部的距离
    //这个数组是高度的值
    private String[] y_title = new String[6]; //5等分y轴坐标
    //分别定义数据源跟数据源名称集合
    private List<Object> mData;
    private List<String> mNames;

    //定义颜色
    private int mLineColor;
    private int mGeenColor;
    private int mTextColor;
    private int mTextDataColor; //显示的数据值颜色
    private int MaxY; //y轴最大数值
    //数值与柱条的距离
    private int dis_data = 50;
    //柱条名称距离x轴的距离
    private int dis_nameX = 20;


    //每个柱条的宽度
    float colum_Weight = 60;
    //每一个柱条的间隔
    float min_weight;
    //y轴刻度间隔
    float min_height;

    /**********************************************************图表及柱状图*********************************************************************************/
    private Path mPathLine;//折线图路径
    private Paint mStopPaint; //折线图圆点颜色
    private Paint mBrokenLine; //折线图折线颜色
    //折线图数据值的坐标点
    private float startX = 0.0F, startY = 0.0F, endX = 0.0F, endY = 0.0F;
    private int Type = 1; // 2：折线  1：柱状图
    private boolean isScroll = false;


    /*********************************************************************** 柱条点击提示 *****************************************************/

    private Paint paintText = new Paint();

    private int BitmapWidth; //矩形宽
    private int BitmapHeight; //矩形高
    private int triangle_size = 15; //三角形高

    //三角形下角点（也是基准点）
    private float x = 0.0F;
    private float y = 0.0F;
    //提示框的左上角坐标
    private float leftTopX;
    private float leftTopY;
    //文字中心线坐标
    private float textX;
    private float textY;
    //提示框
    private Bitmap bitmap;
    //提示文字
    private String Strdate = "";
    private String Strvalue = "";


    /*********************************************************************** 属性设置 *****************************************************/
    public void setScroll(boolean scroll)
    {
        isScroll = scroll;
    }

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

    /**
     * 实例化纵坐标
     *
     * @param maxY
     */
    public void setMaxY(float maxY)
    {
        MaxY = Math.round(((float) maxY / 10) + (float) 5 / 10) * 10;  //让最大值向上取整得到y轴坐标点
        int unit = MaxY / 5;
        for (int i = 5; i >= 0; i--) {
            y_title[5 - i] = (unit * i) + "";
        }
    }

    /*********************************************************************** 构造函数 *****************************************************/

    public HistogramView(Context context)
    {
        super(context);
    }

    //实例化
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
//
        paintText.setColor(Color.WHITE);
        paintText.setStrokeWidth(1);
        paintText.setStyle(Paint.Style.FILL);
        paintText.setTextSize(30);
        paintText.setAntiAlias(true);
        paintText.setTextAlign(Paint.Align.CENTER);
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.sjxq_hekuang_bg2);
        Log.e("TAG", "polygonView:width " + bitmap.getWidth());
        Log.e("TAG", "polygonView:height " + bitmap.getHeight());
        BitmapWidth = bitmap.getWidth();
        BitmapHeight = bitmap.getHeight();


    }

    //尺寸发生改变的时候调用
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh)
    {
        super.onSizeChanged(w, h, oldw, oldh);
        ScreenWidth = w;
        weight = 0.7F * w;
        width_start = 0.3F * w / 2;
        height = 0.70F * h;
    }

    /*********************************************************************** 绘制 *****************************************************/
    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        mScale = 1;
        mgTop = 50;
        min_height = height / 5;
        //绘制横纵坐标及标尺线
        for (int i = 5; i >= 0; i--) {
            //调整画笔的颜色
//            if (i == 5) {
//                mLinePaint.setColor(mLineColor);
//            } else {
            mLinePaint.setColor(mLineColor);
//            }
            //绘制y轴对应横线
            canvas.drawLine(70 * mScale + width_start / 2, 30 * mScale + min_height * i + mgTop, 70 * mScale + weight + width_start, 30 * mScale + min_height * i + mgTop, mLinePaint);
            mTextPaint.setTextAlign(Paint.Align.RIGHT);
            mTextPaint.setTextSize(40 * mScale);
            mTextPaint.setColor(mTextColor);
            //绘制y轴数值
            canvas.drawText(y_title[i], 55 * mScale + width_start / 2, 30 * mScale + min_height * i + mgTop + 10, mTextPaint);
        }

        //绘制柱状图或折线图
        switch (Type) {
            case 1:

                //平均每一个柱条的间隔
                if (!isScroll)
                    min_weight = (weight + width_start / 2 - colum_Weight * mData.size()) / mData.size();  //当数据少时自行应间隔
                else
                    min_weight = 50;
                mTextPaint.setTextSize(42 * mScale);
                mTextPaint.setTextAlign(Paint.Align.CENTER);

                for (int i = 0; i < mData.size(); i++) {
                    float leftR;
                    if (mData.size() > 7) { //从左到右绘制
                        leftR = (float) (70 * mScale + width_start / 2 //x初始位置
//                                + ((weight + width_start / 2) - min_weight * (mData.size() - 1) - colum_Weight * (mData.size())) / 2  //除去柱条加间隔剩余的宽度
                                + (i * (min_weight + colum_Weight))   //每增加一个 间隔宽度跟之前柱条宽度
                                + startOriganalX
                        );
                    } else {//从中间向两边绘制
                        leftR = (float) (70 * mScale + width_start / 2 //x初始位置
                                + ((weight + width_start / 2) - min_weight * (mData.size() - 1) - colum_Weight * (mData.size())) / 2  //除去柱条加间隔剩余的宽度
                                + (i * (min_weight + colum_Weight))   //每增加一个 间隔宽度跟之前柱条宽度
                        );
                    }

                    float rightR = leftR + colum_Weight;   // (int) (min_weight / 2)


                    float buttomR = (float) (colum_Weight / 2 * mScale + min_height * 5 + mgTop);
                    float topR = buttomR - (float) (height / MaxY * Float.parseFloat(mData.get(i).toString()));

                    canvas.drawRect(new RectF(leftR, topR, rightR, buttomR), mGreenPaint); //绘制柱条

                    //x轴对应柱条的名称
                    mTextPaint.setColor(mTextColor);
                    canvas.drawText(mNames.get(i), leftR + colum_Weight / 2, buttomR + dis_data * mScale, mTextPaint);
                    mTextPaint.setColor(mTextDataColor);
                    //柱条上的数值
//                    canvas.drawText(mData.get(i) + "", leftR + colum_Weight / 2, topR - dis_nameX * mScale, mTextPaint);
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
                    float topR = buttomR - (height / MaxY * Float.parseFloat(mData.get(i).toString()));

                    endX = leftR;
                    endY = topR;
                    if (i == 0) {
                        mPathLine.moveTo(endX, endY);
                    } else {
                        mPathLine.lineTo(endX, endY);
//                        canvas.drawLine(startX, startY, endX, endY, mGreenPaint); //绘制折线
                        canvas.drawCircle(startX, startY, 10, mStopPaint);
                        if (i == mData.size() - 1) {
                            canvas.drawCircle(endX, endY, 10, mStopPaint);
                        }
                    }
                    canvas.drawPath(mPathLine, mBrokenLine);

                    startX = endX;
                    startY = endY;

                    //x轴对应柱条的名称
                    mTextPaint.setColor(mTextColor);
                    canvas.drawText(mNames.get(i), leftR, buttomR + dis_data * mScale, mTextPaint);
                    mTextPaint.setColor(mTextDataColor);
                    //折线上的数值
//                    canvas.drawText(mData.get(i) + "", leftR, topR - dis_nameX * mScale, mTextPaint);
                }
                break;
        }

        //绘制弹框提示
        if (x != 0.0 && y != 0.0) {
            if (x < ScreenWidth / 3) { //left
                bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.sjxq_hekuang_bg1);
                leftTopX = x - triangle_size;
                leftTopY = y - BitmapHeight - triangle_size;
                textX = x + BitmapWidth / 2;
                textY = y - BitmapHeight + BitmapHeight / 3;
            } else if (ScreenWidth / 3 < x && x < ScreenWidth / 3 * 2.5) { //center
                bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.sjxq_hekuang_bg2);
                leftTopX = x - BitmapWidth / 2;
                leftTopY = y - BitmapHeight - triangle_size;
                textX = x;
                textY = y - BitmapHeight + BitmapHeight / 3;
            } else { //right
                bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.sjxq_hekuang_bg3);
                leftTopX = x + triangle_size - BitmapWidth;
                leftTopY = y - BitmapHeight - triangle_size;
                textX = x - BitmapWidth / 2 + triangle_size;
                textY = y - BitmapHeight + BitmapHeight / 3;
            }

            if (isMove) {
                if (mData.size() <= 7){
                    canvas.drawLine(x, 30 * mScale + min_height * 5 + mgTop, x , (30 * mScale + min_height * 5 + mgTop - min_height * 5), mGreenPaint);
                    canvas.drawBitmap(bitmap, leftTopX, leftTopY, paintText);
                } else{
                    canvas.drawLine(x + startOriganalX, 30 * mScale + min_height * 5 + mgTop, x + startOriganalX, (30 * mScale + min_height * 5 + mgTop - min_height * 5), mGreenPaint);
                    canvas.drawBitmap(bitmap, leftTopX + startOriganalX, leftTopY, paintText);
                }
            } else {
                if (mData.size() <= 7){
                    canvas.drawLine(x, 30 * mScale + min_height * 5 + mgTop, x, (30 * mScale + min_height * 5 + mgTop - min_height * 5), mGreenPaint);
                    canvas.drawBitmap(bitmap, leftTopX, leftTopY, paintText);
                }else{
                    canvas.drawLine(x , 30 * mScale + min_height * 5 + mgTop, x , (30 * mScale + min_height * 5 + mgTop - min_height * 5), mGreenPaint);
                    canvas.drawBitmap(bitmap, leftTopX, leftTopY, paintText);
                }

            }
            if (isMove) {
                paintText.setTypeface(Typeface.DEFAULT);
                canvas.drawText(Strdate, textX + startOriganalX, textY, paintText);
                paintText.setTypeface(Typeface.DEFAULT_BOLD);
                canvas.drawText(Strvalue, textX + startOriganalX, textY + (BitmapHeight / 2), paintText);
            } else {
                paintText.setTypeface(Typeface.DEFAULT);
                canvas.drawText(Strdate, textX, textY, paintText);
                paintText.setTypeface(Typeface.DEFAULT_BOLD);
                canvas.drawText(Strvalue, textX, textY + (36), paintText);
            }
        }


    }

    /*********************************************************************** 更新数据 *****************************************************/
    //传入数据并进行绘制
    public void updateThisData(List<Object> data, List<String> name)
    {
        mData = data;
        mNames = name;
        invalidate();
    }


    /************************************************************* 图表滑动 *****************************************************************************/

    //这是最初的的位置
    private float startOriganalX = 0;
    private HorizontalScrollRunnable horizontalScrollRunnable;
    private int barWidth = 60;//柱条宽度
    private int barInterval = 60; //柱条间隔
    private int measureWidth = 0;
    private float lastX = 0;//
    private float lastY = 0;//
    //临时滑动的距离
    private float tempLength = 0;
    private long startTime = 0;
    private boolean isFling = false;
    private float dispatchTouchX = 0;
    private float dispatchTouchY = 0;
    //是否到达边界
    private boolean isBoundary = false;
    private boolean isMove = false;

    private int defaultHeight = dp2Px(180);

    private int paddingTop;
    private int paddingLeft;
    private int paddingBottom;
    private int paddingRight;

    @Override
    public boolean dispatchTouchEvent(MotionEvent event)
    {
        Log.e("TAG", "MyBarChartView===dispatchTouchEvent==" + event.getAction());
        int dispatchCurrX = (int) event.getX();
        int dispatchCurrY = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //父容器不拦截点击事件，子控件拦截点击事件。如果不设置为true，外层会直接拦截
                //从而导致motionEvent为cancel
                getParent().requestDisallowInterceptTouchEvent(true);
                dispatchTouchX = getX();
                dispatchTouchY = getY();
                break;
            case MotionEvent.ACTION_MOVE:

                float deltaX = dispatchCurrX - dispatchTouchX;
                float deltaY = dispatchCurrY - dispatchTouchY;
                if (Math.abs(deltaY) - Math.abs(deltaX) > 0) {//数值滑动的父容器拦截事件
                    getParent().requestDisallowInterceptTouchEvent(false);
                }
                //这是向右滑动，如果滑动到边界，就让父容器进行拦截
                if ((dispatchCurrX - dispatchTouchX) > 0 && startOriganalX == 0) {
                    getParent().requestDisallowInterceptTouchEvent(false);
                } else if ((dispatchCurrX - dispatchTouchX) < 0 && startOriganalX == -getMoveLength()) {//这是向右滑动
                    getParent().requestDisallowInterceptTouchEvent(false);
                }
                break;
            case MotionEvent.ACTION_UP:

                break;
            default:
                break;
        }
        dispatchTouchX = dispatchCurrX;
        dispatchTouchY = dispatchCurrY;
        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {

        isBoundary = false;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastX = event.getX();
                lastY = event.getY();
                startTime = System.currentTimeMillis();
                //当点击的时候，判断如果是在filing的效果的时候，就停止快速滑动
                if (isFling) {
                    removeCallbacks(horizontalScrollRunnable);
                    isFling = false;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                isMove = true;
                float currX = event.getX();
                float currY = event.getY();
                startOriganalX += currX - lastX;

                //这是向右滑动
                if ((currX - lastX) > 0) {
                    Log.e("TAG", "向右滑动");
                    if (startOriganalX > 0) {
                        startOriganalX = 0;
                        isBoundary = true;
                    }
                } else {
                    Log.e("TAG", "向左滑动");
                    if (-startOriganalX > getMoveLength()) {
                        startOriganalX = -getMoveLength();
                        isBoundary = true;
                    }
                }
                tempLength = currX - lastX;
                Log.e("TAG", "onTouchEvent:tempLength--- " + tempLength);
                //如果数据量少，根本没有充满横屏，就没必要重新绘制
                if (measureWidth < mData.size() * (barWidth + barInterval)) {
                    invalidate();
                }
                lastX = currX;
                lastY = currY;
                break;
            case MotionEvent.ACTION_UP:
                long endTime = System.currentTimeMillis();
                //计算快速滑动的速度，如果是大于某个值，并且数据的长度大于整个屏幕的长度，那么久允许有flIng后逐渐停止的效果
                float speed = tempLength / (endTime - startTime) * 1000;
                if (Math.abs(speed) > 100 && !isFling && measureWidth < mData.size() * (barWidth + barInterval) && isMove) { //是滑动
                    this.post(horizontalScrollRunnable = new HorizontalScrollRunnable(speed));
                } else { //是点击(弹框)

                    switch (Type) {
                        case 1:
                            //判断点击点的位置
                            float leftx = 0;
                            float rightx = 0;
//                    lastX,lastY
                            for (int i = 0; i < mData.size(); i++) {
                                if (mData.size() > 7) { //从左到右计算点击的位置在哪一个柱条
                                    leftx = 70 * mScale + width_start / 2 + i * colum_Weight + i * min_weight + startOriganalX;
                                    rightx = leftx + colum_Weight;
//                          rightx =  70 * mScale + width_start / 2 + (i+1) * colum_Weight +i*min_weight  + startOriganalX;
                                    if (lastX < leftx)
                                        continue;
                                    if (leftx < lastX && lastX < rightx) {
                                        //取点击柱子区域的y值
                                        float buttomR = (float) (colum_Weight / 2 * mScale + min_height * 5 + mgTop);
                                        float topR = buttomR - (float) (height / MaxY * Float.parseFloat(mData.get(i).toString()));
                                        if (topR < lastY && lastY < buttomR) {
                                            //判断是否设置监听
                                            //将点击的第几条柱子，点击柱子顶部的坐值，用于弹出dialog提示数据，还要返回百分比currentHeidht = Float.parseFloat(data[num - 1 - i])
                                            if (listener != null) {
                                                Log.e("ss", "x" + lastX + ";y:" + lastY);
                                                listener.onClick(i + 1, leftx + colum_Weight / 2, topR, Float.parseFloat(mData.get(i).toString()));
                                                //提示框数据
                                                Strdate = mData.get(i).toString();
                                                Strvalue = mNames.get(i);
                                                //方法二绘制提示框
                                                x = leftx + colum_Weight / 2;
                                                y = topR;
                                                invalidate();
                                            }
                                        }
                                    }
                                } else { //从中间计算点击的位置属于哪一个柱条
                                    leftx = (float) (70 * mScale + width_start / 2 //x初始位置
                                            + ((weight + width_start / 2) - min_weight * (mData.size() - 1) - colum_Weight * (mData.size())) / 2  //除去柱条加间隔剩余的宽度
                                            + (i * (min_weight + colum_Weight))   //每增加一个 间隔宽度跟之前柱条宽度
                                    );
                                    rightx = leftx + colum_Weight;
                                    if (lastX < leftx)
                                        continue;
                                    if (leftx < lastX && lastX < rightx) {
                                        float buttomR = (float) (colum_Weight / 2 * mScale + min_height * 5 + mgTop);
                                        float topR = buttomR - (float) (height / MaxY * Float.parseFloat(mData.get(i).toString()));
                                        //判断是否设置监听
                                        //将点击的第几条柱子，点击柱子顶部的坐值，用于弹出dialog提示数据，还要返回百分比currentHeidht = Float.parseFloat(data[num - 1 - i])
                                        if (listener != null) {
                                            if (topR < lastY && lastY < buttomR) {
                                                Log.e("ss", "x" + lastX + ";y:" + lastY);
                                                listener.onClick(i + 1, leftx + colum_Weight / 2, topR, Float.parseFloat(mData.get(i).toString()));
                                                //提示框数据
                                                Strdate = mData.get(i).toString();
                                                Strvalue = mNames.get(i);
                                                //方法二绘制提示框
                                                x = leftx + colum_Weight / 2;
                                                y = topR;
                                                invalidate();
                                            }
                                        }
                                    }
                                }
                            }
                            break;
                        case 2:
                            for (int i = 0; i < mData.size(); i++) {
                                if (mData.size() > 7) {


                                } else {
                                    float leftbrokenx = (70 * mScale + width_start / 2 //x初始位置
                                            + ((weight + width_start / 2) - min_weight * (mData.size() - 1)) / 2  //除去间隔剩余的宽度
                                            + (i * (min_weight))   //每增加一个 间隔宽度跟之前柱条宽度
                                    );
                                    float buttomR = (min_height * 5 + mgTop) + 30 * mScale;
                                    float topR = buttomR - (height / MaxY * Float.parseFloat(mData.get(i).toString()));
                                    if (lastX < leftbrokenx - min_weight / 2)
                                        continue;
                                    if (leftbrokenx - min_weight / 2 < lastX && lastX < leftbrokenx + min_weight / 2) {
                                        if (listener != null) {
                                            Log.e("ss", "x" + lastX + ";y:" + lastY);
                                            listener.onClick(i + 1, leftbrokenx + colum_Weight / 2, topR, Float.parseFloat(mData.get(i).toString()));
                                            //提示框数据
                                            Strdate = mData.get(i).toString();
                                            Strvalue = mNames.get(i);
                                            //方法二绘制提示框
                                            x = leftbrokenx;
                                            y = topR;
                                            invalidate();
                                        }
                                    }
                                }
                            }

                            break;
                    }
                }
                isMove = false;
                break;
            case MotionEvent.ACTION_CANCEL:
                isMove = false;
                break;
        }
        return true;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width;
        int height;

        if (widthMode == MeasureSpec.EXACTLY) {
            measureWidth = width = widthSize;
        } else {
            width = getAndroiodScreenProperty().get(0);
        }
        if (heightMode == MeasureSpec.EXACTLY) {
            defaultHeight = height = heightSize;
        } else {
            height = defaultHeight;
        }
        setMeasuredDimension(width, height);

        paddingTop = getPaddingTop();
        paddingLeft = getPaddingLeft();
        paddingBottom = getPaddingBottom();
        paddingRight = getPaddingRight();

    }


    //内部类
    private class HorizontalScrollRunnable implements Runnable {
        private float speed;

        public HorizontalScrollRunnable(float speed)
        {
            this.speed = speed;
        }

        @Override
        public void run()
        {
            if (Math.abs(speed) < 30) {
                isFling = false;
                return;
            }
            isFling = true;
            startOriganalX += speed / 15;
            speed = speed / 1.15f;
            //这是向右滑动
            if ((speed) > 0) {
                Log.e("TAG", "向右滑动");
                if (startOriganalX > 0) {
                    startOriganalX = 0;
                }
            } else { //这是向左滑
                Log.e("TAG", "向左滑动");
                if (startOriganalX > getMoveLength()) {
                    startOriganalX = -getMoveLength();
                }
            }
            postDelayed(this, 20);
            invalidate();
        }
    }

    private int getMoveLength()
    {
        return (barWidth + barInterval) * mData.size() - measureWidth;
    }

    //单位转换
    private int dp2Px(float dipValue)
    {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    private int sp2Px(float spValue)
    {
        final float fontScale = getContext().getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    //屏幕参数的算法
    private ArrayList<Integer> getAndroiodScreenProperty()
    {
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;         // 屏幕宽度（像素）
        int height = dm.heightPixels;       // 屏幕高度（像素）
        float density = dm.density;         // 屏幕密度（0.75 / 1.0 / 1.5）
        int densityDpi = dm.densityDpi;     // 屏幕密度dpi（120 / 160 / 240）
        // 屏幕宽度算法:屏幕宽度（像素）/屏幕密度
        int screenWidth = (int) (width / density);  // 屏幕宽度(dp)
        int screenHeight = (int) (height / density);// 屏幕高度(dp)

        ArrayList<Integer> integers = new ArrayList<>();
        integers.add(screenWidth);
        integers.add(screenHeight);
        return integers;
    }


    private OnChartClickListener listener;

    /**
     * 柱子点击时的监听接口
     */
    public interface OnChartClickListener {
        void onClick(int num, float x, float y, float value);
    }

    /**
     * 设置柱子点击监听的方法
     *
     * @param listener
     */
    public void setOnChartClickListener(OnChartClickListener listener)
    {
        this.listener = listener;
    }

}
