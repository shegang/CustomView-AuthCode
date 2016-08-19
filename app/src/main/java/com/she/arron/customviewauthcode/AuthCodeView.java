package com.she.arron.customviewauthcode;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * Created by Administrator on 2016/8/4.
 */
public class AuthCodeView extends View {

    private String mText;
    private int mColor;
    private int background;
    private int mTextSize;

    /**
     * 绘制时控制文本绘制的范围
     */
    private Rect mBound;
    private Paint mPaint;

    public AuthCodeView(Context context) {

        this(context, null);
    }

    public AuthCodeView(Context context, AttributeSet attrs) {

        this(context, attrs, 0);
    }

    public AuthCodeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.AuthCodeView, defStyleAttr, 0);
        int a = typedArray.getIndexCount();
        for (int i = 0; i < a; i++) {
            int attr = typedArray.getIndex(i);
            switch (attr) {
                case R.styleable.AuthCodeView_authCode:
                    mText = typedArray.getString(attr);
                    break;
                case R.styleable.AuthCodeView_codeColor:
                    mColor = typedArray.getColor(attr, Color.BLACK);
                    break;
                case R.styleable.AuthCodeView_backgroundColor:
                    background = typedArray.getColor(attr,Color.YELLOW);
                    break;
                case R.styleable.AuthCodeView_codeSize:
                    mTextSize = (int) typedArray.getDimension(attr, (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16, getResources().getDisplayMetrics())));
                    break;
            }
        }
        typedArray.recycle();
        /**
         * 获得绘制文本的宽和高
         */
        mPaint = new Paint();
        mPaint.setTextSize(mTextSize);
        mPaint.setColor(mColor);
        mBound = new Rect();
        mPaint.getTextBounds(mText, 0, mText.length(), mBound);


        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mText=randomText();
                invalidate();
            }
        });
    }
    private String randomText()
    {
        Random random = new Random();
        Set<Integer> set = new HashSet<Integer>();
        while (set.size() < 4)
        {
            int randomInt = random.nextInt(10);
            set.add(randomInt);
        }
        StringBuffer sb = new StringBuffer();
        for (Integer i : set)
        {
            sb.append("" + i);
        }

        return sb.toString();
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setColor(background);
        mPaint.setAntiAlias(true);
        canvas.drawRect(0, 0, getMeasuredWidth(), getMeasuredHeight(), mPaint);

        mPaint.setColor(mColor);
        canvas.drawText(mText, getWidth() / 2 - mBound.width() / 2, getHeight() / 2 + mBound.height() / 2, mPaint);

        mPaint.setColor(Color.RED);
        canvas.drawLine(0,getWidth() / 2,mBound.width()+getWidth()/2,mBound.height()/2,mPaint);
        mPaint.setColor(Color.BLACK);
        canvas.drawLine(0,0,mBound.width()+getWidth()/2,mBound.height()/2,mPaint);
        mPaint.setColor(Color.GRAY);
        canvas.drawLine(0,0,getWidth(),getHeight(),mPaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        /**
         *  EXACTLY：一般是设置了明确的值或者是MATCH_PARENT
         AT_MOST：表示子布局限制在一个最大值内，一般为WARP_CONTENT
         UNSPECIFIED：表示子布局想要多大就多大，很少使用
         */
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        // 默认设置为16sp，TypeValue也可以把sp转化为px
        int deaflautSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 15, getResources().getDisplayMetrics());
        int width;
        int height;
        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else {
            mPaint.setTextSize(mTextSize);
            mPaint.getTextBounds(mText, 0, mText.length(), mBound);
            float textWidth = mBound.width();
            int desired = (int) (getPaddingLeft()  +
                                textWidth +
                                getPaddingRight());
            width = desired;
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {
            mPaint.setTextSize(mTextSize);
            mPaint.getTextBounds(mText, 0, mText.length(), mBound);
            float textHeight = mBound.height();
            int desired = (int) (getPaddingTop() +
                                textHeight +
                                getPaddingBottom());
            height = desired;
        }

        setMeasuredDimension(width, height);
    }
}
