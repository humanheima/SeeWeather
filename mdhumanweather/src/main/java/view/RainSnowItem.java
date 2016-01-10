package view;

import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.Random;

public class RainSnowItem {

	private boolean rainOrSnow;
	private int width;
	private int height;
	private Paint paint;
	private float startX;
	private float startY;
	private float stopX;
	private float stopY;
	private float sizeX;
	private float sizeY;
	private float opt;
	private int size=20;
	private int rainColor;
	private boolean randomColor=true;
	
	private Random random;
	
	public RainSnowItem(int width,int height){
		this.width=width;
		this.height=height;
		init();
	}
	public RainSnowItem(int width,int height,int size){
		this.size=size;
		this.width=width;
		this.height=height;
		init();
	}
	public RainSnowItem(int width,int height,int size,boolean rainOrSnow){
		this.size=size;
		this.width=width;
		this.height=height;
		this.rainOrSnow=rainOrSnow;
		init();
	}
	public RainSnowItem(int width,int height,int size,int rainColor){
		this.rainColor=rainColor;
		this.size=size;
		this.width=width;
		this.height=height;
		init();
	}
	public RainSnowItem(int width,int height,int size,int rainColor,boolean randomColor){
		this.randomColor=randomColor;
		this.rainColor=rainColor;
		this.size=size;
		this.width=width;
		this.height=height;
		init();
	}

	public void init() {
		random=new Random();
		sizeX = 1+random.nextInt(size/2);
		sizeY = 10+random.nextInt(size);
		startX =random.nextInt(width);
		startY =random.nextInt(height);
		//startX =random.nextInt(720);
		//startY =random.nextInt(1280);
		/*stopX = startX + random.nextInt(5);
		stopY = startY + random.nextInt(5);*/
		stopX = startX + sizeX;
		stopY = startY + sizeY;
		opt=0.2f+random.nextFloat();
		paint = new Paint();
		paint.setColor(0xffffffff);
		/*if (randomColor) {
			
			int r=random.nextInt(256);
			int g=random.nextInt(256);
			int b=random.nextInt(256);
			paint.setARGB(255, r, g, b);
		} else {

			paint.setColor(rainColor);
		}*/
	}

	public void draw(Canvas canvas) {
		if (rainOrSnow) {
			//����
			canvas.drawLine(startX, startY, stopX, stopY, paint);
		}else {
			//��ѩ
			canvas.drawCircle(startX, startY, sizeX, paint);
		}
	}

	public void move() {
		startX += sizeX * opt;
		stopX += sizeX * opt;
		startY += sizeY * opt;
		stopY += sizeY * opt;
		if (startY > height) {
			init();
		}
	}
}