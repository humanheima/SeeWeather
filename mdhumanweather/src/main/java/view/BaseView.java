package view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

public abstract class BaseView extends View {

	private MyThread myThread;
	public int num;//����ѩ��������
	public int size;//������ѩ���Ĵ�С
	public boolean rainOrSnow;//���껹����ѩ
	/**
	 * ����������ѩ��������
	 * @param num
	 */
	public void setNum(int num) {
		this.num = num;
	}
/**
 * ����������ѩ���Ĵ�С
 * @param size
 */
	public void setSize(int size) {
		this.size = size;
	}
/**
 * ������ѩ�������꣬true��ʾ���꣬false��ʾ��ѩ
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
	 * �ж�view�Ƿ��뿪��Ļ
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