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

	public static String ZHENYU="����",ZHENXUE="��ѩ",RAIN_SNOW="���ѩ", RAIN = "��", SNOW = "ѩ",MIN_RAIN = "С��", MID_RAIN = "����", MAX_RAIN = "����",SUPER_RAIN = "����", MIN_SNOW = "Сѩ", MID_SNOW = "��ѩ",MAX_SNOW = "��ѩ", SUPER_SNOW = "��ѩ";
	public RainSnowView minRainSnowView;
	public String rainSnow;
	public SharedPreferences pref;
	public SharedPreferences.Editor editor;
	//public static ConnectivityManager cm;// �����ж������Ƿ����
	//public static NetworkInfo info;

	/**
	 * ������fragmentʱ, ϵͳ���ô˷���. ��ʵ�ִ�����, Ӧ����ʼ����Ҫ��fragment�б��ֵı�Ҫ���,
	 * ��fragment����ͣ����ֹͣ����Իָ�.
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
	 * fragment��һ�λ��������û������ʱ��, ϵͳ����ô˷���.
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return super.onCreateView(inflater, container, savedInstanceState);
	}
	/**
	 * �û���Ҫ�뿪fragmentʱ,ϵͳ�������������Ϊ��һ��ָʾ(Ȼ������������ζ��fragment��������.) �ڵ�ǰ�û��Ự����֮ǰ,
	 * ͨ��Ӧ���������ύ�κ�Ӧ�ó־û��ı仯(��Ϊ�û��п��ܲ��᷵��).
	 */
	@Override
	public void onPause() {
		super.onPause();
	}

	/**
	 * ���ж��Ƿ���������ѩ�����������ֱ��return�������ٽ��в��� 20��ѩ,��15��ѩ��10Сѩ 27���꣬18 ���� ��9С��
	 * true ����
	 * false��ѩ
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
