package view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

public abstract class BaseView extends View {

	private MyThread myThread;
	public int num;//雨点或者雪花的数量
	public int size;//雨点或者雪花的大小
	public boolean rainOrSnow;//下雨还是下雪

	/**
	 * 设置雨点或者雪花的数量
	 * @param num
     */
	public void setNum(int num) {
		this.num = num;
	}

	/**
	 * 设置雨点或者雪花的大小
	 * @param size
     */
	public void setSize(int size) {
		this.size = size;
	}

	/**
	 * 同时设置雨点或者雪花的大小和数量
	 * @param num
	 * @param size
     */
	public void setNumAndSize(int num,int size){
		this.num=num;
		this.size=size;
	}
	/**
	 * 设置下雨还是下雪，true表示下雨，false表示下雪
	 * @param rainOrSnow
     */
	public void setRainOrSnow(boolean rainOrSnow) {
		this.rainOrSnow = rainOrSnow;
	}

	
	public BaseView(Context context) {
		super(context);

	}

	public BaseView(Context context, AttributeSet attrs) {
		super(context, attrs);

	}

	@Override
	protected final void onDraw(Canvas canvas) {

		if (myThread == null) {
			myThread = new MyThread();
			myThread.start();
		} else {
			drawSub(canvas);
		}
	}
	protected abstract void drawSub(Canvas canvas);
	protected abstract void logic();
	protected abstract void init();
	
	private boolean running=true;

	/**
	 * 判断view是否离开了屏幕
	 */
	@Override
	protected void onDetachedFromWindow() {
		running=false;
		super.onDetachedFromWindow();
	}
	class MyThread extends Thread {

		@Override
		public void run() {
				
			init();
			while (running) {
				logic();
				postInvalidate();
				try {
					sleep(30);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			}

		}

		
	}

}