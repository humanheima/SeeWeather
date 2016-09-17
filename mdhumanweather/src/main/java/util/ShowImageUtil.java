package util;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.humanweather.R;

import java.util.Calendar;
import java.util.Date;

import view.RainSnowView;

/**
 * 显示天气图片的工具类
 */
public class ShowImageUtil {
	private static ShowImageUtil showImageUtil;
	private Activity activity;
	private RainSnowView rainSnowView;
	private ViewGroup rainViewGroup;
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
	//阴天色值
		public  int []cloudy=new int[]{
				Color.rgb(69, 69, 69),//#454545
				Color.rgb(77, 77, 77),//#4D4D4D
				Color.rgb(99, 99, 99),//#636363
				Color.rgb(163, 163, 163),//#A3A3A3
				Color.rgb(198, 226, 255)//#C6E2FF
		};
	//晴天色值
		public  int []sunShine=new int[]{
		Color.rgb(0, 0, 127),//#00000f
		Color.rgb(0, 0, 255),//#0000ff
		Color.rgb(127, 0, 255),//0f00ff
		Color.rgb(127, 127, 255),//0f0fff
		Color.rgb(255, 255, 255)//#ffffff
		};

	/**
	 * 判断是白天还是黑夜
	 * @return
     */
	public  boolean DayorNight() {
		//请求图片判断是加载夜间的还是白天的白天从早晨6点到下午18点
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
		//现在的时间
		Calendar calendarNow = Calendar.getInstance();
		Date dateNow = calendarNow.getTime();
		//加载白天的图片
		if (dateNow.after(date6) && dateNow.before(date18)) {
			return true;
		}
		//加载黑夜的图片
		else {
			return false;
		}

	}

	/**
	 * 显示天气图片
	 * @param activity
	 * @param weather
	 * @param imageView
	 * @param viewGroup
     */
	public  void showDayWeatherImage(Activity activity,String weather,ImageView imageView,ViewGroup viewGroup) {
		this.activity=activity;
			if (DayorNight()) {
			if (Constants.QING.equals(weather)) {
                imageView.setBackgroundResource(R.drawable.d0);
			} else if (Constants.DUOYUN.equals(weather)) {
				imageView.setBackgroundResource(R.drawable.d1);
			} else if (Constants.YIN.equals(weather)) {
				imageView.setBackgroundResource(R.drawable.d2);
                /*GradientDrawable grad;
                grad = new GradientDrawable(Orientation.BR_TL,cloudy);
                viewGroup.setBackground(grad);*/
			} else if (Constants.ZHENYU.equals(weather)) {
				imageView.setBackgroundResource(R.drawable.d3);
			} else if (Constants.LEIZHENYU.equals(weather)) {
				imageView.setBackgroundResource(R.drawable.d4);
			} else if (Constants.LEIZHENYUYOUBINGBAO.equals(weather)) {
				imageView.setBackgroundResource(R.drawable.d5);
			} else if (Constants.YUJIAXUE.equals(weather)) {
				imageView.setBackgroundResource(R.drawable.d6);
			} else if (Constants.XIAOYU.equals(weather)) {
				imageView.setBackgroundResource(R.drawable.d7);
			} else if (Constants.ZHONGYU.equals(weather)||Constants.XIAOYU_ZHONGYU.equals(weather)) {
				imageView.setBackgroundResource(R.drawable.d8);
			} else if (Constants.DAYU.equals(weather)||Constants.ZHONGYU_DAYUE.equals(weather)) {
				imageView.setBackgroundResource(R.drawable.d9);
			} else if (Constants.BAOYU.equals(weather)||Constants.DAYU_BAOYU.equals(weather)) {
				imageView.setBackgroundResource(R.drawable.d10);
			} else if (Constants.DABAOYU.equals(weather)||Constants.BAOYU_DABAOYU.equals(weather)) {
				imageView.setBackgroundResource(R.drawable.d11);
			} else if (Constants.TEDABAOYU.equals(weather)||Constants.DABAOYU_TEDABAOYU.equals(weather)) {
				imageView.setBackgroundResource(R.drawable.d12);
			} else if (Constants.ZHENXUE.equals(weather)) {
				imageView.setBackgroundResource(R.drawable.d13);
			} else if (Constants.XIAOXUE.equals(weather)) {
				imageView.setBackgroundResource(R.drawable.d14);
			} else if (Constants.ZHONGXUE.equals(weather)||Constants.XIAOXUE_ZHONGXUE.equals(weather)) {
				imageView.setBackgroundResource(R.drawable.d15);
			} else if (Constants.DAXUE.equals(weather)||Constants.ZHONGXUE_DAXUE.equals(weather)) {
				imageView.setBackgroundResource(R.drawable.d16);
			} else if (Constants.BAOXUE.equals(weather)||Constants.DAXUE_BAOXUE.equals(weather)) {
				imageView.setBackgroundResource(R.drawable.d17);
			} else if (Constants.WU.equals(weather)) {
				imageView.setBackgroundResource(R.drawable.d18);
			} else if (Constants.DONGYU.equals(weather)) {
				imageView.setBackgroundResource(R.drawable.d19);
			} else if (Constants.SHACHENBAO.equals(weather)) {
				imageView.setBackgroundResource(R.drawable.d20);
			} else if (Constants.FUCHEN.equals(weather)) {
				imageView.setBackgroundResource(R.drawable.d29);
			} else if (Constants.YANGSHA.equals(weather)) {
				imageView.setBackgroundResource(R.drawable.d30);
			} else if (Constants.QIANGSHACHENBAO.equals(weather)) {
				imageView.setBackgroundResource(R.drawable.d31);
			} else if (Constants.MAI.equals(weather)) {
				imageView.setBackgroundResource(R.drawable.d53);
			}
		} else {
                if (Constants.QING.equals(weather)) {
                    imageView.setBackgroundResource(R.drawable.n0);
                } else if (Constants.DUOYUN.equals(weather)) {
                    imageView.setBackgroundResource(R.drawable.n1);
                } else if (Constants.YIN.equals(weather)) {
                    imageView.setBackgroundResource(R.drawable.n2);
                    /*GradientDrawable grad;
                    grad = new GradientDrawable(Orientation.BR_TL,cloudy);
                    viewGroup.setBackground(grad);*/
                } else if (Constants.ZHENYU.equals(weather)) {
                    imageView.setBackgroundResource(R.drawable.n3);
                } else if (Constants.LEIZHENYU.equals(weather)) {
                    imageView.setBackgroundResource(R.drawable.n4);
                } else if (Constants.LEIZHENYUYOUBINGBAO.equals(weather)) {
                    imageView.setBackgroundResource(R.drawable.n5);
                } else if (Constants.YUJIAXUE.equals(weather)) {
                    imageView.setBackgroundResource(R.drawable.n6);
                } else if (Constants.XIAOYU.equals(weather)) {
                    imageView.setBackgroundResource(R.drawable.n7);
                } else if (Constants.ZHONGYU.equals(weather)||Constants.XIAOYU_ZHONGYU.equals(weather)) {
                    imageView.setBackgroundResource(R.drawable.n8);
                } else if (Constants.DAYU.equals(weather)||Constants.ZHONGYU_DAYUE.equals(weather)) {
                    imageView.setBackgroundResource(R.drawable.n9);
                } else if (Constants.BAOYU.equals(weather)||Constants.DAYU_BAOYU.equals(weather)) {
                    imageView.setBackgroundResource(R.drawable.n10);
                } else if (Constants.DABAOYU.equals(weather)||Constants.BAOYU_DABAOYU.equals(weather)) {
                    imageView.setBackgroundResource(R.drawable.n11);
                } else if (Constants.TEDABAOYU.equals(weather)||Constants.DABAOYU_TEDABAOYU.equals(weather)) {
                    imageView.setBackgroundResource(R.drawable.n12);
                } else if (Constants.ZHENXUE.equals(weather)) {
                    imageView.setBackgroundResource(R.drawable.n13);
                } else if (Constants.XIAOXUE.equals(weather)) {
                    imageView.setBackgroundResource(R.drawable.n14);
                } else if (Constants.ZHONGXUE.equals(weather)||Constants.XIAOXUE_ZHONGXUE.equals(weather)) {
                    imageView.setBackgroundResource(R.drawable.n15);
                } else if (Constants.DAXUE.equals(weather)||Constants.ZHONGXUE_DAXUE.equals(weather)) {
                    imageView.setBackgroundResource(R.drawable.n16);
                } else if (Constants.BAOXUE.equals(weather)||Constants.DAXUE_BAOXUE.equals(weather)) {
                    imageView.setBackgroundResource(R.drawable.n17);
                } else if (Constants.WU.equals(weather)) {
                    imageView.setBackgroundResource(R.drawable.n18);
                } else if (Constants.DONGYU.equals(weather)) {
                    imageView.setBackgroundResource(R.drawable.n19);
                } else if (Constants.SHACHENBAO.equals(weather)) {
                    imageView.setBackgroundResource(R.drawable.n20);
                } else if (Constants.FUCHEN.equals(weather)) {
                    imageView.setBackgroundResource(R.drawable.n29);
                } else if (Constants.YANGSHA.equals(weather)) {
                    imageView.setBackgroundResource(R.drawable.n30);
                } else if (Constants.QIANGSHACHENBAO.equals(weather)) {
                    imageView.setBackgroundResource(R.drawable.n31);
                } else if (Constants.MAI.equals(weather)) {
                    imageView.setBackgroundResource(R.drawable.n53);
                }
		}

	}
    /*public  void showDayWeatherImage(Activity activity,String weather,ImageView imageView,ViewGroup viewGroup) {
		this.activity=activity;
		*//*if ("".equals(weather)){
			return;
		}*//*
			if (DayorNight()) {
			if (Constants.QING.equals(weather)) {
				imageView.setBackgroundResource(R.drawable.d0);
			} else if (Constants.DUOYUN.equals(weather)) {
				imageView.setBackgroundResource(R.drawable.d1);
			} else if (Constants.YIN.equals(weather)) {
				imageView.setBackgroundResource(R.drawable.d2);
			} else if (Constants.ZHENYU.equals(weather)) {
				imageView.setBackgroundResource(R.drawable.d3);
			} else if (Constants.LEIZHENYU.equals(weather)) {
				imageView.setBackgroundResource(R.drawable.d4);
			} else if (Constants.LEIZHENYUYOUBINGBAO.equals(weather)) {
				imageView.setBackgroundResource(R.drawable.d5);
			} else if (Constants.YUJIAXUE.equals(weather)) {
				imageView.setBackgroundResource(R.drawable.d6);
			} else if (Constants.XIAOYU.equals(weather)) {
				imageView.setBackgroundResource(R.drawable.d7);
			} else if (Constants.ZHONGYU.equals(weather)) {
				imageView.setBackgroundResource(R.drawable.d8);
			} else if (Constants.DAYU.equals(weather)) {
				imageView.setBackgroundResource(R.drawable.d9);
			} else if (Constants.BAOYU.equals(weather)) {
				imageView.setBackgroundResource(R.drawable.d10);
			} else if (Constants.DABAOYU.equals(weather)) {
				imageView.setBackgroundResource(R.drawable.d11);
			} else if (Constants.TEDABAOYU.equals(weather)) {
				imageView.setBackgroundResource(R.drawable.d12);
			} else if (Constants.ZHENXUE.equals(weather)) {
				imageView.setBackgroundResource(R.drawable.d13);
			} else if (Constants.XIAOXUE.equals(weather)) {
				imageView.setBackgroundResource(R.drawable.d14);
			} else if (Constants.ZHONGXUE.equals(weather)) {
				imageView.setBackgroundResource(R.drawable.d15);
			} else if (Constants.DAXUE.equals(weather)) {
				imageView.setBackgroundResource(R.drawable.d16);
			} else if (Constants.BAOXUE.equals(weather)) {
				imageView.setBackgroundResource(R.drawable.d17);
			} else if (Constants.WU.equals(weather)) {
				imageView.setBackgroundResource(R.drawable.d18);
			} else if (Constants.DONGYU.equals(weather)) {
				imageView.setBackgroundResource(R.drawable.d19);
			} else if (Constants.SHACHENBAO.equals(weather)) {
				imageView.setBackgroundResource(R.drawable.d20);
			} else if (Constants.XIAOYU_ZHONGYU.equals(weather)) {
				imageView.setBackgroundResource(R.drawable.d21);
			} else if (Constants.ZHONGYU_DAYUE.equals(weather)) {
				imageView.setBackgroundResource(R.drawable.d22);
			} else if (Constants.DAYU_BAOYU.equals(weather)) {
				imageView.setBackgroundResource(R.drawable.d23);
			} else if (Constants.BAOYU_DABAOYU.equals(weather)) {
				imageView.setBackgroundResource(R.drawable.d24);
			} else if (Constants.DABAOYU_TEDABAOYU.equals(weather)) {
				imageView.setBackgroundResource(R.drawable.d25);
			} else if (Constants.XIAOXUE_ZHONGXUE.equals(weather)) {
				imageView.setBackgroundResource(R.drawable.d26);
			} else if (Constants.ZHONGXUE_DAXUE.equals(weather)) {
				imageView.setBackgroundResource(R.drawable.d27);
			} else if (Constants.DAXUE_BAOXUE.equals(weather)) {
				imageView.setBackgroundResource(R.drawable.d28);
			} else if (Constants.FUCHEN.equals(weather)) {
				imageView.setBackgroundResource(R.drawable.d29);
			} else if (Constants.YANGSHA.equals(weather)) {
				imageView.setBackgroundResource(R.drawable.d30);
			} else if (Constants.QIANGSHACHENBAO.equals(weather)) {
				imageView.setBackgroundResource(R.drawable.d31);
			} else if (Constants.MAI.equals(weather)) {
				imageView.setBackgroundResource(R.drawable.d53);
			}
		} else {
                if (Constants.QING.equals(weather)) {
                    imageView.setBackgroundResource(R.drawable.n0);
                } else if (Constants.DUOYUN.equals(weather)) {
                    imageView.setBackgroundResource(R.drawable.n1);
                } else if (Constants.YIN.equals(weather)) {
                    imageView.setBackgroundResource(R.drawable.n2);
                } else if (Constants.ZHENYU.equals(weather)) {
                    imageView.setBackgroundResource(R.drawable.n3);
                } else if (Constants.LEIZHENYU.equals(weather)) {
                    imageView.setBackgroundResource(R.drawable.n4);
                } else if (Constants.LEIZHENYUYOUBINGBAO.equals(weather)) {
                    imageView.setBackgroundResource(R.drawable.n5);
                } else if (Constants.YUJIAXUE.equals(weather)) {
                    imageView.setBackgroundResource(R.drawable.n6);
                } else if (Constants.XIAOYU.equals(weather)) {
                    imageView.setBackgroundResource(R.drawable.n7);
                } else if (Constants.ZHONGYU.equals(weather)) {
                    imageView.setBackgroundResource(R.drawable.n8);
                } else if (Constants.DAYU.equals(weather)) {
                    imageView.setBackgroundResource(R.drawable.n9);
                } else if (Constants.BAOYU.equals(weather)) {
                    imageView.setBackgroundResource(R.drawable.n10);
                } else if (Constants.DABAOYU.equals(weather)) {
                    imageView.setBackgroundResource(R.drawable.n11);
                } else if (Constants.TEDABAOYU.equals(weather)) {
                    imageView.setBackgroundResource(R.drawable.n12);
                } else if (Constants.ZHENXUE.equals(weather)) {
                    imageView.setBackgroundResource(R.drawable.n13);
                } else if (Constants.XIAOXUE.equals(weather)) {
                    imageView.setBackgroundResource(R.drawable.n14);
                } else if (Constants.ZHONGXUE.equals(weather)) {
                    imageView.setBackgroundResource(R.drawable.n15);
                } else if (Constants.DAXUE.equals(weather)) {
                    imageView.setBackgroundResource(R.drawable.n16);
                } else if (Constants.BAOXUE.equals(weather)) {
                    imageView.setBackgroundResource(R.drawable.n17);
                } else if (Constants.WU.equals(weather)) {
                    imageView.setBackgroundResource(R.drawable.n18);
                } else if (Constants.DONGYU.equals(weather)) {
                    imageView.setBackgroundResource(R.drawable.n19);
                } else if (Constants.SHACHENBAO.equals(weather)) {
                    imageView.setBackgroundResource(R.drawable.n20);
                } else if (Constants.XIAOYU_ZHONGYU.equals(weather)) {
                    imageView.setBackgroundResource(R.drawable.n21);
                } else if (Constants.ZHONGYU_DAYUE.equals(weather)) {
                    imageView.setBackgroundResource(R.drawable.n22);
                } else if (Constants.DAYU_BAOYU.equals(weather)) {
                    imageView.setBackgroundResource(R.drawable.n23);
                } else if (Constants.BAOYU_DABAOYU.equals(weather)) {
                    imageView.setBackgroundResource(R.drawable.n24);
                } else if (Constants.DABAOYU_TEDABAOYU.equals(weather)) {
                    imageView.setBackgroundResource(R.drawable.n25);
                } else if (Constants.XIAOXUE_ZHONGXUE.equals(weather)) {
                    imageView.setBackgroundResource(R.drawable.n26);
                } else if (Constants.ZHONGXUE_DAXUE.equals(weather)) {
                    imageView.setBackgroundResource(R.drawable.n27);
                } else if (Constants.DAXUE_BAOXUE.equals(weather)) {
                    imageView.setBackgroundResource(R.drawable.n28);
                } else if (Constants.FUCHEN.equals(weather)) {
                    imageView.setBackgroundResource(R.drawable.n29);
                } else if (Constants.YANGSHA.equals(weather)) {
                    imageView.setBackgroundResource(R.drawable.n30);
                } else if (Constants.QIANGSHACHENBAO.equals(weather)) {
                    imageView.setBackgroundResource(R.drawable.n31);
                } else if (Constants.MAI.equals(weather)) {
                    imageView.setBackgroundResource(R.drawable.n53);
                }
		}

	}*/
	/**
	 * 画出天气的背景
	 * @param activity
	 * @param weather
     */
	public  void drawBackground(Activity activity,String weather) {
		GradientDrawable grad;
		grad = new GradientDrawable(Orientation.BR_TL,sunShine);
		activity.getWindow().setBackgroundDrawable(grad);

	}
}
