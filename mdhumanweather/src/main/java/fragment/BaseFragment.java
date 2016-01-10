package fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.example.humanweather.MyApp;

import view.RainSnowView;

public class BaseFragment extends Fragment implements OnClickListener {

	public static String ZHENYU="阵雨",ZHENXUE="阵雪",RAIN_SNOW="雨夹雪", RAIN = "雨", SNOW = "雪",MIN_RAIN = "小雨", MID_RAIN = "中雨", MAX_RAIN = "大雨",SUPER_RAIN = "暴雨", MIN_SNOW = "小雪", MID_SNOW = "中雪",MAX_SNOW = "大雪", SUPER_SNOW = "暴雪";
	public RainSnowView minRainSnowView;
	public String rainSnow;
	public SharedPreferences pref;
	public SharedPreferences.Editor editor;
	//public static ConnectivityManager cm;// 用于判断网络是否可用
	//public static NetworkInfo info;

	/**
	 * 当创建fragment时, 系统调用此方法. 在实现代码中, 应当初始化想要在fragment中保持的必要组件,
	 * 当fragment被暂停或者停止后可以恢复.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//cm = (ConnectivityManager) MyApp.getContex().getSystemService(Context.CONNECTIVITY_SERVICE);
		//info = cm.getActiveNetworkInfo();
		pref = PreferenceManager.getDefaultSharedPreferences(MyApp.getContex());
		editor = pref.edit();
	}

	/**
	 * fragment第一次绘制它的用户界面的时候, 系统会调用此方法.
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return super.onCreateView(inflater, container, savedInstanceState);
	}
	/**
	 * 用户将要离开fragment时,系统调用这个方法作为第一个指示(然而它不总是意味着fragment将被销毁.) 在当前用户会话结束之前,
	 * 通常应当在这里提交任何应该持久化的变化(因为用户有可能不会返回).
	 */
	@Override
	public void onPause() {
		super.onPause();
	}

	/**
	 * 先判断是否包含雨或者雪，如果不包含直接return，否则再进行操作 20大雪,，15中雪，10小雪 27大雨，18 中雨 ，9小雨
	 * true 下雨
	 * false下雪
	 * @param rainSnow
	 */
	public void rainOrSnow(RelativeLayout rtLayout, String rainSnow) {
		if (!(rainSnow.contains(RAIN)|rainSnow.contains(SNOW))) {
			return;
		} else {

		 if (MIN_RAIN.equals(rainSnow)||ZHENYU.equals(rainSnow)) {
				rtLayout.removeView(minRainSnowView);
				minRainSnowView = new RainSnowView(MyApp.getContex(), true,15, 9);
				rtLayout.addView(minRainSnowView);
			} else if (MID_RAIN.equals(rainSnow)) {
				rtLayout.removeView(minRainSnowView);
				minRainSnowView = new RainSnowView(MyApp.getContex(), true,25, 18);
				rtLayout.addView(minRainSnowView);
			}else if (MAX_RAIN.equals(rainSnow)) {
				rtLayout.removeView(minRainSnowView);
				minRainSnowView = new RainSnowView(MyApp.getContex(), true,35, 27);
				rtLayout.addView(minRainSnowView);
			}else if (SUPER_RAIN.equals(rainSnow)) {
				rtLayout.removeView(minRainSnowView);
				minRainSnowView = new RainSnowView(MyApp.getContex(), true,45, 27);
				rtLayout.addView(minRainSnowView);
			}else if (MIN_SNOW.equals(rainSnow)||ZHENXUE.equals(rainSnow)) {
				rtLayout.removeView(minRainSnowView);
				minRainSnowView = new RainSnowView(MyApp.getContex(), false,15, 9);
				rtLayout.addView(minRainSnowView);
			} else if (MID_SNOW.equals(rainSnow)) {
				rtLayout.removeView(minRainSnowView);
				minRainSnowView = new RainSnowView(MyApp.getContex(), false,25, 15);
				rtLayout.addView(minRainSnowView);
			}else if (MAX_SNOW.equals(rainSnow)) {
				rtLayout.removeView(minRainSnowView);
				minRainSnowView = new RainSnowView(MyApp.getContex(), false,35, 20);
				rtLayout.addView(minRainSnowView);
			}else if (SUPER_SNOW.equals(rainSnow)) {
				rtLayout.removeView(minRainSnowView);
				minRainSnowView = new RainSnowView(MyApp.getContex(), false,45, 20);
				rtLayout.addView(minRainSnowView);
			}else if (RAIN_SNOW.equals(rainSnow)) {
				rtLayout.removeView(minRainSnowView);
				minRainSnowView = new RainSnowView(MyApp.getContex(), false,15, 15);
				rtLayout.addView(minRainSnowView);
			}
		}
	}

	@Override
	public void onClick(View view) {
	}
}
