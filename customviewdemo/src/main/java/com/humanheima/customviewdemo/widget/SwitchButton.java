package com.humanheima.customviewdemo.widget;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import com.humanheima.customviewdemo.R;


/**
 * Created by chenchao on 16/9/29.
 * cc@cchao.org
 * 开关按钮
 */
public class SwitchButton extends View {

    //选中时背景色
    private int selectColor;

    //未选中时背景色
    private int normalColor;

    //选择按钮颜色
    private int buttonColor;

    //选择按钮padding
    private int buttonPadding;

    //滑动动画时间
    private int switchRate;

    //是否选中
    private boolean isSelect = false;

    //是否滑动中
    private boolean isScroll = false;

    //是否有动画
    private boolean isAnim = false;

    private Paint paint;
    private RectF rectF;

    private int width = 0;
    private int height = 0;

    //重绘次数
    private int paintTimes = 0;

    private float buttonRadius = 0;
    private float animEach = 0;
    private ObjectAnimator objectAnimator;
    private long aniDuration = 300;
    private float process;

    public float getProcess() {
        return process;
    }

    public void setProcess(float process) {
        this.process = process;
    }

    private OnSwitchChangeListener onSwitchChangeListener;

    public SwitchButton(Context context) {
        this(context, null);
    }

    public SwitchButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwitchButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.SwitchButton, defStyle, 0);
        selectColor = typedArray.getColor(R.styleable.SwitchButton_selectColor, Color.GREEN);
        normalColor = typedArray.getColor(R.styleable.SwitchButton_normalColor, Color.GRAY);
        buttonColor = typedArray.getColor(R.styleable.SwitchButton_buttonColor, Color.WHITE);
        buttonPadding = typedArray.getDimensionPixelSize(R.styleable.SwitchButton_buttonPadding, (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 1, getResources().getDisplayMetrics()));
        switchRate = typedArray.getInteger(R.styleable.SwitchButton_switchRate, 10);
        typedArray.recycle();
        paint = new Paint();
        paint.setAntiAlias(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);
        if (widthSpecMode == MeasureSpec.AT_MOST && heightSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(160, 100);
        } else if (widthSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension((int) (1.6 * heightSpecSize), heightSpecSize);
        } else if (heightSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(widthSpecSize, (int) (widthSpecSize * 0.625));
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (width == 0) {
            width = getWidth();
            height = getHeight();
            //构建一个矩形用来绘制
            rectF = new RectF(0, 0, width, height);

            //选择按钮半径,以为运算符优先级低
            buttonRadius = height - buttonPadding * 2 >> 1;
            //每次移动距离为宽度减去按钮的直径和两边的padding除以switchRate
            animEach = (width - buttonRadius * 2 - buttonPadding * 2) / switchRate;
        }
        if (paintTimes < switchRate && isAnim) {
            paint.setColor(isSelect ? normalColor : selectColor);
            canvas.drawRoundRect(rectF, height / 2, height / 2, paint);
        } else {
            //初始绘制到屏幕上的时候isAnim为false,isSelect为false,绘制圆角矩形的颜色为normalColor
            paint.setColor(!isSelect ? normalColor : selectColor);
            canvas.drawRoundRect(rectF, height / 2, height / 2, paint);
        }
        paint.setColor(buttonColor);
        if (isSelect) {
            if (isAnim) {
                canvas.drawCircle(buttonRadius + buttonPadding + animEach * paintTimes, buttonRadius + buttonPadding, buttonRadius, paint);
            } else {
                canvas.drawCircle(width - buttonRadius - buttonPadding, buttonRadius + buttonPadding, buttonRadius, paint);
            }
        } else {
            if (isAnim) {
                canvas.drawCircle(width - buttonRadius - buttonPadding - animEach * paintTimes, buttonRadius + buttonPadding, buttonRadius, paint);
            } else {
                //初始绘制thumb到屏幕上
                canvas.drawCircle(buttonRadius + buttonPadding, buttonRadius + buttonPadding, buttonRadius, paint);
            }
        }

        //移动动画
        if (paintTimes < switchRate && isAnim) {
            paintTimes++;
            invalidate();
        } else {
            paintTimes = 0;
            isAnim = false;
            isScroll = false;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_UP:
                if (!isScroll) {
                    isSelect = !isSelect;
                    isAnim = true;
                    isScroll = true;
                    postInvalidate();
                    if (onSwitchChangeListener != null) {
                        onSwitchChangeListener.onChange(isSelect);
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            default:
                break;
        }
        return true;
    }

    public void setOnSwitchChangeListener(OnSwitchChangeListener onSwitchChangeListener) {
        this.onSwitchChangeListener = onSwitchChangeListener;
    }

    public boolean isChecked() {
        return isSelect;
    }

    public void setCheck(boolean select) {
        isSelect = select;
        postInvalidate();
    }

    public int getButtonColor() {
        return buttonColor;
    }

    public void setButtonColor(int buttonColor) {
        this.buttonColor = buttonColor;
    }

    public int getButtonPadding() {
        return buttonPadding;
    }

    public void setButtonPadding(int buttonPadding) {
        this.buttonPadding = buttonPadding;
    }

    public int getNormalColor() {
        return normalColor;
    }

    public void setNormalColor(int normalColor) {
        this.normalColor = normalColor;
    }

    public int getSelectColor() {
        return selectColor;
    }

    public void setSelectColor(int selectColor) {
        this.selectColor = selectColor;
    }

    public int getSwitchRate() {
        return switchRate;
    }

    public void setSwitchRate(int switchRate) {
        this.switchRate = switchRate;
    }

    public interface OnSwitchChangeListener {
        void onChange(boolean isChecked);
    }
}
