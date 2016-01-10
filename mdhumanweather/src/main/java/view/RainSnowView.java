package view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.util.AttributeSet;

import com.example.humanweather.R;

import java.util.ArrayList;

/**
 * 下小雨或者小雪
 * @author lenovo
 *
 */
public class RainSnowView extends BaseView {

	private ArrayList<RainSnowItem>rainSnowItems=new ArrayList<RainSnowItem>();
	public RainSnowView(Context context) {
		super(context);
	}
	public RainSnowView(Context context,boolean rainOrSnow,int rainSnowNum,int rainSnowSize) {
		super(context);
		this.rainOrSnow=rainOrSnow;
		this.num=rainSnowNum;
		this.size=rainSnowSize;
	}
	public RainSnowView(Context context, AttributeSet attrs) {
		super(context, attrs);
		TypedArray ta=context.obtainStyledAttributes(attrs,R.styleable.RainSnow);
		num=ta.getInteger(R.styleable.RainSnow_num, 10);
		size=ta.getInteger(R.styleable.RainSnow_size, 10);
		rainOrSnow=ta.getBoolean(R.styleable.RainSnow_rainOrsnow, true);//Ĭ������
		ta.recycle();
	}
	@Override
	protected void drawSub(Canvas canvas) {
		for (RainSnowItem item : rainSnowItems) {
			item.draw(canvas);
		}
	}
	@Override
	protected void logic() {
		for (RainSnowItem item : rainSnowItems) {
			item.move();
		}
	}
	@Override
	protected void init() {
		
			for (int i = 0; i <num; i++) {
				RainSnowItem item=new RainSnowItem(getWidth(), getHeight(), size, rainOrSnow);
				rainSnowItems.add(item);
			}
	}

}
