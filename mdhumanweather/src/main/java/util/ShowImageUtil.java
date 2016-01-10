package util;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.humanweather.R;

import java.util.Calendar;
import java.util.Date;

import view.RainSnowView;

/**��ʾ����ͼƬ�Ĺ�����
 * ���Ը���ʱ����ʾ��������������ϵ�ͼƬ
 * @author dumingwei
 *
 */
public class ShowImageUtil {
	private static ShowImageUtil showImageUtil;
	private Activity activity;
	private RainSnowView rainSnowView;
	private ShowImageUtil(){
	}
	public static ShowImageUtil getInstance() {
		if (showImageUtil == null) {
			synchronized (ShowImageUtil.class) {
				if (showImageUtil == null) {
					showImageUtil = new ShowImageUtil();
				}
			}
		}
		return showImageUtil;
	}
	public static int RAIN=0;//��ʾ����
	public static int SNOW=1;//��ʾ��ѩ
	//public static MinRainSnowView minRainSnowView=null;
	//����ɫֵ
		public  int []cloudy=new int[]{
				Color.rgb(69, 69, 69),//#454545
				Color.rgb(77, 77, 77),//#4D4D4D
				Color.rgb(99, 99, 99),//#636363
				Color.rgb(163, 163, 163),//#A3A3A3
				Color.rgb(198, 226, 255)//#C6E2FF
		};
		
		//����ɫֵ					
		public  int []sunShine=new int[]{
		Color.rgb(0, 0, 127),//#00000f
		Color.rgb(0, 0, 255),//#0000ff
		Color.rgb(127, 0, 255),//0f00ff
		Color.rgb(127, 127, 255),//0f0fff
		Color.rgb(255, 255, 255)//#ffffff
		};
	public  boolean DayorNight() {
		// ����ͼƬ�жϼ��ذ���Ļ���ҹ���,������糿6�㵽����18��
		Calendar calendar6 = Calendar.getInstance();
		calendar6.set(Calendar.HOUR_OF_DAY, 6);
		calendar6.set(Calendar.MINUTE, 0);
		calendar6.set(Calendar.SECOND, 0);
		Date date6 = calendar6.getTime();

		Calendar calendar18 = Calendar.getInstance();
		calendar18.set(Calendar.HOUR_OF_DAY, 18);
		calendar18.set(Calendar.MINUTE, 0);
		calendar18.set(Calendar.SECOND, 0);
		Date date18 = calendar18.getTime();

		// ���ڵ�ʱ��
		Calendar calendarNow = Calendar.getInstance();
		Date dateNow = calendarNow.getTime();
		// ���ذ����ͼƬ
		if (dateNow.after(date6) && dateNow.before(date18)) {
			return true;
		}
		// ���غ�ҹ��ͼƬ
		else {
			return false;
		}

	}

	public  void showDayWeatherImage(Activity activity,String weather,ImageView imageView,RelativeLayout rtLayout) {
		this.activity=activity;
		if (DayorNight()) {
			if ("��".equals(weather)) {
				imageView.setBackgroundResource(R.drawable.d0);
			} else if ("����".equals(weather)) {
				imageView.setBackgroundResource(R.drawable.d1);
			} else if ("��".equals(weather)) {
				imageView.setBackgroundResource(R.drawable.d2);
			} else if ("����".equals(weather)) {
				imageView.setBackgroundResource(R.drawable.d3);
			} else if ("������".equals(weather)) {
				imageView.setBackgroundResource(R.drawable.d4);
			} else if ("�������б���".equals(weather)) {
				imageView.setBackgroundResource(R.drawable.d5);
			} else if ("���ѩ".equals(weather)) {
				imageView.setBackgroundResource(R.drawable.d6);
			} else if ("С��".equals(weather)) {
				imageView.setBackgroundResource(R.drawable.d7);
			} else if ("����".equals(weather)) {
				imageView.setBackgroundResource(R.drawable.d8);
			} else if ("����".equals(weather)) {
				imageView.setBackgroundResource(R.drawable.d9);
			} else if ("����".equals(weather)) {
				imageView.setBackgroundResource(R.drawable.d10);
			} else if ("����".equals(weather)) {
				imageView.setBackgroundResource(R.drawable.d11);
			} else if ("�ش���".equals(weather)) {
				imageView.setBackgroundResource(R.drawable.d12);
			} else if ("��ѩ".equals(weather)) {
				imageView.setBackgroundResource(R.drawable.d13);
			} else if ("Сѩ".equals(weather)) {
				imageView.setBackgroundResource(R.drawable.d14);
			} else if ("��ѩ".equals(weather)) {
				imageView.setBackgroundResource(R.drawable.d15);
			} else if ("��ѩ".equals(weather)) {
				imageView.setBackgroundResource(R.drawable.d16);
			} else if ("��ѩ".equals(weather)) {
				imageView.setBackgroundResource(R.drawable.d17);
			} else if ("��".equals(weather)) {
				imageView.setBackgroundResource(R.drawable.d18);
			} else if ("����".equals(weather)) {
				imageView.setBackgroundResource(R.drawable.d19);
			} else if ("ɳ����".equals(weather)) {
				imageView.setBackgroundResource(R.drawable.d20);
			} else if ("С��-����".equals(weather)) {
				imageView.setBackgroundResource(R.drawable.d21);
			} else if ("����-����".equals(weather)) {
				imageView.setBackgroundResource(R.drawable.d22);
			} else if ("����-����".equals(weather)) {
				imageView.setBackgroundResource(R.drawable.d23);
			} else if ("����-����".equals(weather)) {
				imageView.setBackgroundResource(R.drawable.d24);
			} else if ("����-�ش���".equals(weather)) {
				imageView.setBackgroundResource(R.drawable.d25);
			} else if ("Сѩ-��ѩ".equals(weather)) {
				imageView.setBackgroundResource(R.drawable.d26);
			} else if ("��ѩ-��ѩ".equals(weather)) {
				imageView.setBackgroundResource(R.drawable.d27);
			} else if ("��ѩ-��ѩ".equals(weather)) {
				imageView.setBackgroundResource(R.drawable.d28);
			} else if ("����".equals(weather)) {
				imageView.setBackgroundResource(R.drawable.d29);
			} else if ("��ɳ".equals(weather)) {
				imageView.setBackgroundResource(R.drawable.d30);
			} else if ("ǿɳ����".equals(weather)) {
				imageView.setBackgroundResource(R.drawable.d31);
			}
			else if ("��".equals(weather)) {
				imageView.setBackgroundResource(R.drawable.d53);
			}
		} else {

			if ("��".equals(weather)) {
				imageView.setBackgroundResource(R.drawable.n0);
			} else if ("����".equals(weather)) {
				imageView.setBackgroundResource(R.drawable.n1);
			} else if ("��".equals(weather)) {
				imageView.setBackgroundResource(R.drawable.n2);
			} else if ("����".equals(weather)) {
				imageView.setBackgroundResource(R.drawable.n3);
			} else if ("������".equals(weather)) {
				imageView.setBackgroundResource(R.drawable.n4);
			} else if ("�������б���".equals(weather)) {
				imageView.setBackgroundResource(R.drawable.n5);
			} else if ("���ѩ".equals(weather)) {
				imageView.setBackgroundResource(R.drawable.n6);
			} else if ("С��".equals(weather)) {
				imageView.setBackgroundResource(R.drawable.n7);
			} else if ("����".equals(weather)) {
				imageView.setBackgroundResource(R.drawable.n8);
			} else if ("����".equals(weather)) {
				imageView.setBackgroundResource(R.drawable.n9);
			} else if ("����".equals(weather)) {
				imageView.setBackgroundResource(R.drawable.n10);
			} else if ("����".equals(weather)) {
				imageView.setBackgroundResource(R.drawable.n11);
			} else if ("�ش���".equals(weather)) {
				imageView.setBackgroundResource(R.drawable.n12);
			} else if ("��ѩ".equals(weather)) {
				imageView.setBackgroundResource(R.drawable.n13);
			} else if ("Сѩ".equals(weather)) {
				imageView.setBackgroundResource(R.drawable.n14);
			} else if ("��ѩ".equals(weather)) {
				imageView.setBackgroundResource(R.drawable.n15);
			} else if ("��ѩ".equals(weather)) {
				imageView.setBackgroundResource(R.drawable.n16);
			} else if ("��ѩ".equals(weather)) {
				imageView.setBackgroundResource(R.drawable.n17);
			} else if ("��".equals(weather)) {
				imageView.setBackgroundResource(R.drawable.n18);
			} else if ("����".equals(weather)) {
				imageView.setBackgroundResource(R.drawable.n19);
			} else if ("ɳ����".equals(weather)) {
				imageView.setBackgroundResource(R.drawable.n20);
			} else if ("С��-����".equals(weather)) {
				imageView.setBackgroundResource(R.drawable.n21);
			} else if ("����-����".equals(weather)) {
				imageView.setBackgroundResource(R.drawable.n22);
			} else if ("����-����".equals(weather)) {
				imageView.setBackgroundResource(R.drawable.n23);
			} else if ("����-����".equals(weather)) {
				imageView.setBackgroundResource(R.drawable.n24);
			} else if ("����-�ش���".equals(weather)) {
				imageView.setBackgroundResource(R.drawable.n25);
			} else if ("Сѩ-��ѩ".equals(weather)) {
				imageView.setBackgroundResource(R.drawable.n26);
			} else if ("��ѩ-��ѩ".equals(weather)) {
				imageView.setBackgroundResource(R.drawable.n27);
			} else if ("��ѩ-��ѩ".equals(weather)) {
				imageView.setBackgroundResource(R.drawable.n28);
			} else if ("����".equals(weather)) {
				imageView.setBackgroundResource(R.drawable.n29);
			} else if ("��ɳ".equals(weather)) {
				imageView.setBackgroundResource(R.drawable.n30);
			} else if ("ǿɳ����".equals(weather)) {
				imageView.setBackgroundResource(R.drawable.n31);
			}else if ("��".equals(weather)) {
				imageView.setBackgroundResource(R.drawable.d53);
			}
		}

	}
	/**���������ı���
	 * @param activity
	 * @param colorArr
	 */
	public  void drawBackground(Activity activity,String weather) {
		GradientDrawable grad;
		grad = new GradientDrawable(Orientation.BR_TL,sunShine);
		activity.getWindow().setBackgroundDrawable(grad);
			
	}
}
