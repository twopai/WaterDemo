package com.twopai.waterdemo;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * 作者：twopai on 2017/11/7.
 * 邮箱：twopai@hotmail.com
 */

public class WaterView extends View {

    private int mWaterColor=0xBB0000FF;//水的颜色
    private float mWaterPeak=35f;//水的波峰
    private float mWaterTrough=35f;//水的波谷
    private float mWaterHeight=255f;//水的高度
    private Paint mWaterPaint;//水的paint
    private Path mWaterPath;//水的path
    private int mViewHeight;
    private int mViewWidth;
    private PointF mPoint1;//点1
    private PointF mPoint2;//点2
    private PointF mPoint3;//点3
    private PointF mPoint4;//点4
    private PointF mPoint5;//点5
    private PointF mControlPoint1;//控制点1
    private PointF mControlPoint2;//控制点2
    private PointF mControlPoint3;//控制点3
    private PointF mControlPoint4;//控制点4
    private ValueAnimator valueAnimator;//水波动画控制器

    public WaterView(Context context) {
        this(context,null);
    }

    public WaterView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public WaterView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.WaterView);
        mWaterColor =array.getColor(R.styleable.WaterView_water_color, mWaterColor);
        mWaterPeak =array.getDimension(R.styleable.WaterView_water_peak, mWaterPeak);
        mWaterTrough =array.getDimension(R.styleable.WaterView_water_trough, mWaterTrough);
        mWaterHeight =array.getDimension(R.styleable.WaterView_water_height, mWaterHeight);
        array.recycle();
        initSet();
    }

    private void initSet() {
        //设置水的paint
        mWaterPaint = new Paint();
        mWaterPaint.setAntiAlias(true);
        mWaterPaint.setStyle(Paint.Style.FILL);
        mWaterPaint.setColor(mWaterColor);
        //设置水的path
        mWaterPath = new Path();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mViewWidth = w;
        mViewHeight = h;
        initSetPoint();
    }

    private void initSetPoint() {
        mPoint1 = new PointF(-mViewWidth,mViewHeight-mWaterHeight);
        mPoint2 = new PointF(mPoint1.x+mViewWidth/2,mViewHeight-mWaterHeight);
        mPoint3 = new PointF(mPoint2.x+mViewWidth/2,mViewHeight-mWaterHeight);
        mPoint4 = new PointF(mPoint3.x+mViewWidth/2,mViewHeight-mWaterHeight);
        mPoint5 = new PointF(mPoint4.x+mViewWidth/2,mViewHeight-mWaterHeight);
        mControlPoint1 = new PointF(mPoint1.x+mViewWidth/4,mPoint1.y-mWaterPeak);
        mControlPoint2 = new PointF(mPoint2.x+mViewWidth/4,mPoint2.y+mWaterTrough);
        mControlPoint3 = new PointF(mPoint3.x+mViewWidth/4,mPoint3.y-mWaterPeak);
        mControlPoint4 = new PointF(mPoint4.x+mViewWidth/4,mPoint4.y+mWaterTrough);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mWaterPath.reset();
        mWaterPath.moveTo(mPoint1.x,mPoint1.y);
        mWaterPath.quadTo(mControlPoint1.x,mControlPoint1.y,mPoint2.x,mPoint2.y);
        mWaterPath.quadTo(mControlPoint2.x,mControlPoint2.y,mPoint3.x,mPoint3.y);
        mWaterPath.quadTo(mControlPoint3.x,mControlPoint3.y,mPoint4.x,mPoint4.y);
        mWaterPath.quadTo(mControlPoint4.x,mControlPoint4.y,mPoint5.x,mPoint5.y);
        mWaterPath.lineTo(mViewWidth,mViewHeight);
        mWaterPath.lineTo(0,mViewHeight);
        mWaterPath.close();
        canvas.drawPath(mWaterPath,mWaterPaint);
    }

    /**
     * 开始动画
     */
    public void startAnim(View selfView){
        if (valueAnimator == null) {
            selfView.post(new Runnable() {
                @Override
                public void run() {
                    valueAnimator = ValueAnimator.ofFloat(mPoint1.x,0);
                    valueAnimator.setDuration(2000);
                    valueAnimator.setInterpolator(new LinearInterpolator());
                    valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
                    valueAnimator.start();
                    valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator valueAnimator) {
                            //改变每个点的横坐标
                            mPoint1.x = (float) valueAnimator.getAnimatedValue();
                            mPoint2.x = mPoint1.x+mViewWidth/2;
                            mPoint3.x =mPoint2.x+mViewWidth/2;
                            mPoint4.x= mPoint3.x+mViewWidth/2;
                            mPoint5.x =mPoint4.x+mViewWidth/2;
                            mControlPoint1.x = mPoint1.x+mViewWidth/4;
                            mControlPoint2.x = mPoint2.x+mViewWidth/4;
                            mControlPoint3.x = mPoint3.x+mViewWidth/4;
                            mControlPoint4.x = mPoint4.x+mViewWidth/4;
                            invalidate();
                        }
                    });
                }
            });
        }
    }

    /**
     * 结束动画
     */
    public void stopAnim(){
        if (valueAnimator != null) {
            if (valueAnimator.isRunning()) {
                valueAnimator.cancel();
                valueAnimator=null;
            }
        }
    }
}
