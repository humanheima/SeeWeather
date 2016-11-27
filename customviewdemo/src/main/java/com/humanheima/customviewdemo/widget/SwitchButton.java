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
import android.view.animation.AccelerateDecelerateInterpolator;

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
    private boolean checked = false;

    //是否有动画
    private boolean isAnim = false;

    private Paint paint;
    private RectF rectF;

    private int width = 0;
    private int height = 0;
    private int defaultWidth;//默认的宽度
    private float buttonRadius = 0;
    private ObjectAnimator objectAnimator;
    private long aniDuration = 400;//动画时间
    private float process;//动画的属性
    private float distance;//thumb移动的距离

    public float getProcess() {
        return process;
    }

    public void setProcess(float process) {
        float tp = process;
        if (tp > 1) {
            tp = 1;
        } else if (tp < 0) {
            tp = 0;
        }
        this.process = tp;
        invalidate();
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
        selectColor = typedArray.getColor(R.styleable.SwitchButton_selectColor, Color.parseColor("#00e676"));
        normalColor = typedArray.getColor(R.styleable.SwitchButton_normalColor, Color.GRAY);
        buttonColor = typedArray.getColor(R.styleable.SwitchButton_buttonColor, Color.WHITE);
        buttonPadding = typedArray.getDimensionPixelSize(R.styleable.SwitchButton_buttonPadding, (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 1, getResources().getDisplayMetrics()));
        checked = typedArray.getBoolean(R.styleable.SwitchButton_isChecked, checked);
        if (checked) {
            process = 1f;
        }
        typedArray.recycle();
        paint = new Paint();
        defaultWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, getResources().getDisplayMetrics());//默认宽度40dp
        objectAnimator = ObjectAnimator.ofFloat(this, "process", 0, 0).setDuration(aniDuration);
        objectAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
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
            setMeasuredDimension(defaultWidth, (int) (defaultWidth * 0.625));
        } else if (widthSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension((int) (1.6 * heightSpecSize), heightSpecSize);
        } else if (heightSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(widthSpecSize, (int) (widthSpecSize * 0.625));
        } else {
            //精准模式
            if (heightSpecSize>=widthSpecSize)
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
            //选择按钮半径,移位运算符优先级低
            buttonRadius = height - buttonPadding * 2 >> 1;
            //todo 总共移动的距离是(width - buttonRadius * 2 - buttonPadding * 2)
            //todo 每次移动的距离process*(width - buttonRadius * 2 - buttonPadding * 2)
            distance = (width - buttonRadius * 2 - buttonPadding * 2);
        }
        //TODO 原来的背景色应该逐渐淡去，新的背景色逐渐画出来
        if (checked) {
            paint.setColor(selectColor);
            canvas.drawRoundRect(rectF, height / 2, height / 2, paint);
        } else {
            //初始绘制到屏幕上的时候isAnim为false,isSelect为false,绘制圆角矩形的颜色为normalColor
            paint.setColor(normalColor);
            canvas.drawRoundRect(rectF, height / 2, height / 2, paint);
        }
        paint.setColor(buttonColor);
        if (checked) {
            if (isAnim) {
                canvas.drawCircle(buttonRadius + buttonPadding + process * distance, buttonRadius + buttonPadding, buttonRadius, paint);
            } else {
                canvas.drawCircle(width - buttonRadius - buttonPadding, buttonRadius + buttonPadding, buttonRadius, paint);
            }
        } else {
            if (isAnim) {
                canvas.drawCircle(buttonRadius + buttonPadding + process * distance, buttonRadius + buttonPadding, buttonRadius, paint);
            } else {
                //初始绘制thumb到屏幕上
                canvas.drawCircle(buttonRadius + buttonPadding, buttonRadius + buttonPadding, buttonRadius, paint);
            }
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_UP:
                if (!objectAnimator.isRunning()) {
                    isAnim = true;
                    checked = !checked;
                    animateToState(checked);
                    if (onSwitchChangeListener != null) {
                        onSwitchChangeListener.onChange(checked);
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

    private void animateToState(boolean isSelect) {
        if (objectAnimator == null) {
            return;
        }
        objectAnimator.setDuration(aniDuration);
        if (isSelect) {
            objectAnimator.setFloatValues(process, 1f);
        } else {
            objectAnimator.setFloatValues(process, 0f);
        }

        objectAnimator.start();
    }

    public void setOnSwitchChangeListener(OnSwitchChangeListener onSwitchChangeListener) {
        this.onSwitchChangeListener = onSwitchChangeListener;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setCheck(boolean select) {
        checked = select;
        isAnim = true;
        animateToState(checked);
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

    public interface OnSwitchChangeListener {
        void onChange(boolean isChecked);
    }
}
