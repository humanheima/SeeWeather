package view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;

import com.example.humanweather.R;

/**
 * 自定义的带清除按钮的文本搜索框
 */
public class ClearEditText extends EditText implements  
        OnFocusChangeListener, TextWatcher {
    /**
     * 删除按钮的引用
     */
    private Drawable mClearDrawable; 
 
    public ClearEditText(Context context) { 
    	this(context, null); 
    } 
 
    public ClearEditText(Context context, AttributeSet attrs) { 
    	//这里的构造方法很重要，不加这个很多属性不能再xml里面定义
    	this(context, attrs, android.R.attr.editTextStyle); 
    } 
    
    public ClearEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }
    
    
    private void init() { 
    //获取EditText的DrawableRight，假如没有设置我们就使用默认的图片
    	mClearDrawable = getCompoundDrawables()[2]; 
        if (mClearDrawable == null) { 
        	mClearDrawable = getResources().getDrawable(R.drawable.clear_input);
        } 
        //Specify a bounding rectangle for the Drawable. This is where the drawable will draw when its draw() method is called. 
        mClearDrawable.setBounds(0, 0, mClearDrawable.getIntrinsicWidth(), mClearDrawable.getIntrinsicHeight()); 
        //默认设置隐藏图片
        setClearIconVisible(false);
        //设置焦点改变的监听
        setOnFocusChangeListener(this); 
        //设置输入框里面内容发生改变的监听
        addTextChangedListener(this); 
    }

    /**
     * 因为我们不能直接给EditText设置点击事件，所以我们用记住我们按下的位置来模拟点击事件
     * 当我们按下的位置在EditText的宽度-图标到控件右边的间距-图标的宽度和
     * EditText的宽度-图标到控件的间距之间就算点击了图标，数值方向没有考虑
     * @param event
     * @return
     */
    @Override 
    public boolean onTouchEvent(MotionEvent event) { 
    	//如果drawableRight不为空
        if (getCompoundDrawables()[2] != null) {
        	if(event.getAction()==MotionEvent.ACTION_UP) {
        		boolean touchable = event.getX() > (getWidth() - getPaddingRight() - mClearDrawable.getIntrinsicWidth()) && (event.getX() < ((getWidth() - getPaddingRight())));
            	//如果触摸的位置在图片的范围之内，清空EditText
                if (touchable) { 
                    this.setText(""); 
                } 
			}
        } 
        return super.onTouchEvent(event); 
    }

    /**
     * 当clearEditText的焦点发生变化的时候，判断里面的字符串长度设置清除图标的显示与隐藏
     * @param v
     * @param hasFocus
     */
    @Override 
    public void onFocusChange(View v, boolean hasFocus) { 
        if (hasFocus) { 
            setClearIconVisible(getText().length() > 0); 
        } else { 
            setClearIconVisible(false); 
        } 
    }

    /**
     * 设置清除图标的显示与隐藏，调用setCompoundDrawables为EditText绘制上去
     * @param visible
     */
    protected void setClearIconVisible(boolean visible) { 
        Drawable right = visible ? mClearDrawable : null; 
        setCompoundDrawables(getCompoundDrawables()[0],getCompoundDrawables()[1], right, getCompoundDrawables()[3]); 
    }

    /**
     * 当输入框里面的内容发生变化的时候调用
     * @param s
     * @param start
     * @param count
     * @param after
     */
    @Override 
    public void onTextChanged(CharSequence s, int start, int count, 
            int after) { 
        setClearIconVisible(s.length() > 0); 
    } 
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, 
            int after) { 
         
    } 
    @Override
    public void afterTextChanged(Editable s) { 

    }
}