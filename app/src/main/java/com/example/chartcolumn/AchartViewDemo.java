package com.example.chartcolumn;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * 带有坐标轴的自定义柱状图
 * 此类结合recycleview加载大量数据
 */
public class AchartViewDemo extends View {

    /*****************************定义控件*********************************/
    private Paint mXYLinePaint;//xy轴横线画笔
    private Paint mLinePaint;//普通横线画笔
    private Paint mTextPaint;//文字颜色
    private Paint mTextPaint1;//文字颜色
    private Paint mGreenPaint;//柱状图颜色

    /******************************定义变量***************************************/
    private Context mContext; //应用上下文
    //定义宽高
    private int ScreenWidth = 0;
    private float width_start;//图表起始位置
    private float viewWidth;//图表宽度
    private float viewHeight;//图表高度
    private float mScale = 1;//比例
    private float mgTop = 30;//局顶部距离
    //这个是数组高度的值
    private String[] y_title = new String[6]; //5等分y轴坐标
    //分别定义数据源跟数据源名称集合
    private List<Object> mData;
    private List<String> mNames;
    private List<String> mNums;
    //定义颜色
    private int mLineColor = 0;
    private int mLineColorXY = 0;
    private int mTextColor = 0;
    private int mTextDataColor = 0;
    private int mGreenColor = 0;
    //定义图标属性
    //y轴最大数值
    private int Max_Y = 10;
    //数值与柱条的距离
    private int dis_data = 5;
    //柱条名称距离x轴的距离
    private int dis_nameX = 5;
    //每个柱条的宽度
    private float colum_Width = 80;
    //每一个柱条的间隔
    private float dis_colum = 20;
    //y轴刻度间距
    private float dis_spacing_Y;
    //是否绘制y轴
    private boolean isShow_Y;

    //
    private int measureWidth = 0;
    private int defaultHeight = dp2Px(180);

    /***********************************构造器************************************/
    public AchartViewDemo(Context context)
    {
        super(context);
    }

    public AchartViewDemo(Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);
        //给定义的画笔进行加工
        mContext = context;
        mLinePaint = new Paint();
        mXYLinePaint = new Paint();
        mGreenPaint = new Paint();
        mTextPaint = new Paint();
        mTextPaint1 = new Paint();

        mGreenPaint.setStyle(Paint.Style.FILL);

        mLinePaint.setAntiAlias(true);
        mXYLinePaint.setAntiAlias(true);
        mTextPaint.setAntiAlias(true);
        mTextPaint1.setAntiAlias(true);

    }

    /***************************重写方法***************************************/
    //尺寸发生变化
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh)
    {
        super.onSizeChanged(w, h, oldw, oldh);
        ScreenWidth = w;
        viewWidth = 1.0f * w;
        viewHeight = 0.5f * h;
        width_start = 0f;
    }

    /**
     * 绘制方法
     *
     * @param canvas 绘制x、y轴  间距分割标记线  y轴间隔数值
     *               绘制数据横线
     *               绘制柱状图  数值文字 x轴对应文字
     */
    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        dis_spacing_Y = viewHeight / 5;
        if (mData == null || mData.size() == 0)
            return;
        //绘制x，y轴，y轴标记，y轴数值
        for (int i = 5; i >= 0; i--) {
            if (mLineColorXY != 0) {
                mXYLinePaint.setColor(mLineColorXY);
            }
            if (mLineColor != 0) {
                mLinePaint.setColor(mLineColor);
            }

            mLinePaint.setStrokeWidth(3f);
            if (i == 5) {
                if (isShow_Y)
                    canvas.drawLine(width_start + 40 + 40, dis_spacing_Y * i + mgTop, viewWidth, dis_spacing_Y * i + mgTop, mXYLinePaint);
                else
                    canvas.drawLine(width_start, dis_spacing_Y * i + mgTop, viewWidth, dis_spacing_Y * i + mgTop, mXYLinePaint);
            } else {
                if (isShow_Y) {
                    canvas.drawLine(width_start + 40 + 40, dis_spacing_Y * i + mgTop, width_start + 40 + 40 + 10, dis_spacing_Y * i + mgTop, mXYLinePaint);
                    canvas.drawLine(width_start + 40 + 40 + 10, dis_spacing_Y * i + mgTop, viewWidth, dis_spacing_Y * i + mgTop, mLinePaint);
                } else {
                    canvas.drawLine(width_start, dis_spacing_Y * i + mgTop, viewWidth, dis_spacing_Y * i + mgTop, mLinePaint);
                }

            }

            if (isShow_Y) {
                //绘制y轴数值
                mTextPaint.setTextSize(28);
                if (y_title[i] != null) {
                    canvas.drawText(y_title[i], width_start + 40, dis_spacing_Y * i + mgTop, mTextPaint);
                }

            }
        }
        mXYLinePaint.setStrokeWidth(2f);
        if (isShow_Y) {
            canvas.drawLine(width_start + 50 + 40, mgTop, width_start + 50 + 40, (dis_spacing_Y * 5) + mgTop + 10, mXYLinePaint);
        }

        //绘制柱状图 、数值、x轴名称、间隔标识
        mTextPaint.setTextSize(30);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        for (int i = 0; i < mData.size(); i++) {
            float leftR;
            if (isShow_Y) {
                leftR = (float)
                        ((viewWidth + width_start / 2) - dis_colum * (mData.size() - 1) - colum_Width * (mData.size())) / 2  //除去柱条加间隔剩余的宽度
                        + (i * (dis_colum + colum_Width)) + colum_Width/2 +9;//每增加一个 间隔宽度跟之前柱条宽度
            } else {
                leftR = (float)
                        ((viewWidth + width_start / 2) - dis_colum * (mData.size() - 1) - colum_Width * (mData.size())) / 2  //除去柱条加间隔剩余的宽度
                        + (i * (dis_colum + colum_Width));//每增加一个 间隔宽度跟之前柱条宽度
            }

            float rightR = leftR + colum_Width;
            float buttomR = (float) (dis_spacing_Y * 5 + mgTop);
            float topR = buttomR - (float) (viewHeight / Max_Y * Float.parseFloat(mData.get(i).toString()));
            if (mGreenColor != 0) {
                mGreenPaint.setColor(mGreenColor);
            }
            canvas.drawRect(new RectF(leftR - 1, topR, rightR, buttomR - 1), mGreenPaint); //绘制柱条
            //x轴对应柱条的名称
            Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
            mTextPaint.clearShadowLayer();
            for (int j = 0; j < mNames.get(i).length(); j++) {
                canvas.drawText(String.valueOf(mNames.get(i).charAt(j)), leftR + colum_Width / 2, buttomR + dis_data * mScale
                        + j * (Math.abs(fontMetrics.top)) + mgTop, mTextPaint);
                //x轴对应数字
                if (j == mNames.get(i).length() - 1) {
                    mTextPaint.setTextSize(24);
//                    mTextPaint.setColor(mTextDataColor);
//                    canvas.drawText(mNums.get(i), leftR + colum_Width / 2, buttomR + dis_data * mScale
//                            + (j + 1) * (Math.abs(fontMetrics.top)) + mgTop, mTextPaint);

                    for (int k = 0; k < mNums.get(i).length(); k += 2) {
                        if (k + 1 < mNums.get(i).length())
                            canvas.drawText(String.valueOf(mNums.get(i).charAt(k) + String.valueOf(mNums.get(i).charAt(k + 1))), leftR + colum_Width / 2, buttomR + dis_data * mScale
                                    + (j + 1) * (Math.abs(fontMetrics.top)) + mgTop + k / 2 * (Math.abs(fontMetrics.top)), mTextPaint);
                        else
                            canvas.drawText(String.valueOf(mNums.get(i).charAt(k)), leftR + colum_Width / 2, buttomR + dis_data * mScale
                                    + (j + 1) * (Math.abs(fontMetrics.top)) + mgTop + k / 2 * (Math.abs(fontMetrics.top)), mTextPaint);
                    }

                }
                //x轴分割标识
                canvas.drawLine(leftR + colum_Width + dis_colum / 2, buttomR, leftR + colum_Width + dis_colum / 2, buttomR + 10, mXYLinePaint);
            }
        }
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
    }

    /****************************getter/setter********************************************/


    public void setMgTop(float mgTop)
    {
        this.mgTop = mgTop;
    }

    public void setY_title(String[] y_title)
    {
        this.y_title = y_title;
    }

    public void setmData(List<Object> mData)
    {
        this.mData = mData;
    }

    public void setmNames(List<String> mNames)
    {
        this.mNames = mNames;
    }

    public void setmNums(List<String> mNums)
    {
        this.mNums = mNums;
    }

    public void setmLineColor(int mLineColor)
    {
        this.mLineColor = mLineColor;
    }

    public void setmLineColorXY(int mLineColorXY)
    {
        this.mLineColorXY = mLineColorXY;
    }

    public void setmTextColor(int mTextColor)
    {
        this.mTextColor = mTextColor;
    }

    public void setmTextDataColor(int mTextDataColor)
    {
        this.mTextDataColor = mTextDataColor;
    }


    public void setMax_Y(int max_Y)
    {
        Max_Y = max_Y;
        Max_Y = Math.round(((float) max_Y / 10) + (float) 5 / 10) * 10;  //让最大值向上取整得到y轴坐标点
        int unit = Max_Y / 5;
        for (int i = 5; i >= 0; i--) {
            y_title[5 - i] = (unit * i) + "";
        }
    }

    public void setDis_data(int dis_data)
    {
        this.dis_data = dis_data;
    }

    public void setDis_nameX(int dis_nameX)
    {
        this.dis_nameX = dis_nameX;
    }

    public void setColum_Width(float colum_Width)
    {
        this.colum_Width = colum_Width;
    }

    public void setDis_colum(float dis_colum)
    {
        this.dis_colum = dis_colum;
    }

    public void setDis_spacing_XY(float dis_spacing_XY)
    {
        this.dis_spacing_Y = dis_spacing_XY;
    }

    public void setShow_Y(boolean show_Y)
    {
        isShow_Y = show_Y;
    }

    public void setmGreenColor(int mGreenColor)
    {
        this.mGreenColor = mGreenColor;
    }

    /******************************工具方法***************************/
    //单位转换
    private int dp2Px(float dipValue)
    {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
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
        // 屏幕宽度算法:屏幕宽度（像素）/屏幕密度
        int screenWidth = (int) (width / density);  // 屏幕宽度(dp)
        int screenHeight = (int) (height / density);// 屏幕高度(dp)

        ArrayList<Integer> integers = new ArrayList<>();
        integers.add(screenWidth);
        integers.add(screenHeight);
        return integers;
    }
}
