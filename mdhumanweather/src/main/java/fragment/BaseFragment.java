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
import com.example.humanweather.R;

import util.Constants;
import view.RainSnowView;

public class BaseFragment extends Fragment implements OnClickListener {
    public String rainSnow;
    public SharedPreferences pref;
    public SharedPreferences.Editor editor;

    /**
     * 创建fragment的时候，系统会调用此方法，在实现代码中，应该初始化想在fragment中保持的必要组件，
     * 当fragment被暂停或者停止的后可以恢复
     *
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pref = PreferenceManager.getDefaultSharedPreferences(MyApp.getContex());
        editor = pref.edit();
    }
    /**
     * fragment第一次绘制界面的事后调用
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    /**
     * 用户将要离开fragment时，系统调用这个方法作为第一个指示（然而它不总是意味着fragment将被销毁），在用户结束
     * 会话的时候，通常在这里提交任何应该持久化的变化（因为用户有可能不会返回）
     */
    @Override
    public void onPause() {
        super.onPause();
    }

    /**
     * 先判断是否包含雨或者雪，如果不包含就直接return，否则再进行操作，20大雪，15中雪，10小雪 27 大雨，18 中雨，9小雨
     * true 下雨
     * false 下雪
     *
     * @param viewGroup
     * @param rainSnow
     */
    public void rainOrSnow(ViewGroup viewGroup, String rainSnow) {
        if (!(rainSnow.contains(Constants.YU) | rainSnow.contains(Constants.XUE))) {
            return;
        } else {
            ViewGroup newView = (ViewGroup) getActivity().getLayoutInflater().inflate(R.layout.rain_snow, viewGroup, false);
            RainSnowView rainSnowView = (RainSnowView) newView.findViewById(R.id.rainSnowView);
            if (Constants.XIAOYU.equals(rainSnow) || Constants.ZHENYU.equals(rainSnow)) {
                rainSnowView.setNumAndSize(15, 9);
                rainSnowView.setRainOrSnow(true);
            } else if (Constants.ZHONGYU.equals(rainSnow) || Constants.XIAOYU_ZHONGYU.equals(rainSnow)) {
                rainSnowView.setNumAndSize(25, 18);
                rainSnowView.setRainOrSnow(true);
            } else if (Constants.DAYU.equals(rainSnow) || Constants.ZHONGYU_DAYUE.equals(rainSnow)) {
                rainSnowView.setNumAndSize(35, 27);
                rainSnowView.setRainOrSnow(true);
            } else if (Constants.BAOYU.equals(rainSnow) || Constants.DAYU_BAOYU.equals(rainSnow) || Constants.BAOYU_DABAOYU.equals(rainSnow)) {
                rainSnowView.setNumAndSize(45, 27);
                rainSnowView.setRainOrSnow(true);
            } else if (Constants.XIAOXUE.equals(rainSnow) || Constants.ZHENXUE.equals(rainSnow)) {
                rainSnowView.setNumAndSize(15, 9);
                rainSnowView.setRainOrSnow(false);
            } else if (Constants.ZHONGXUE.equals(rainSnow) || Constants.XIAOXUE_ZHONGXUE.equals(rainSnow)) {
                rainSnowView.setNumAndSize(25, 15);
                rainSnowView.setRainOrSnow(false);
            } else if (Constants.DAXUE.equals(rainSnow) || Constants.ZHONGXUE_DAXUE.equals(rainSnow)) {
                rainSnowView.setNumAndSize(35, 20);
                rainSnowView.setRainOrSnow(false);
            } else if (Constants.BAOXUE.equals(rainSnow) || Constants.DAXUE_BAOXUE.equals(rainSnow)) {
                rainSnowView.setNumAndSize(45, 20);
                rainSnowView.setRainOrSnow(false);
            } else if (Constants.YUJIAXUE.equals(rainSnow)) {
                rainSnowView.setNumAndSize(15, 15);
                rainSnowView.setRainOrSnow(false);
            }
            viewGroup.addView(newView, 0);
        }
    }
/*

    */

    /**
     * 先判断是否包含雨或者雪，如果不包含就直接return，否则再进行操作，20大雪，15中雪，10小雪 27 大雨，18 中雨，9小雨
     * true 下雨
     * false 下雪
     *
     * @param rtLayout
     * @param rainSnow
     *//*

	public void rainOrSnow(RelativeLayout rtLayout, String rainSnow) {
		if (!(rainSnow.contains(Constants.YU)|rainSnow.contains(Constants.XUE))) {
			return;
		} else {
			if (Constants.XIAOXUE.equals(rainSnow)||Constants.ZHENXUE.equals(rainSnow)) {
				rtLayout.removeView(minRainSnowView);
				minRainSnowView = new RainSnowView(MyApp.getContex(), true,15, 9);
				rtLayout.addView(minRainSnowView);
			} else if (Constants.ZHONGYU.equals(rainSnow)||Constants.XIAOYU_ZHONGYU.equals(rainSnow)) {
				rtLayout.removeView(minRainSnowView);
				minRainSnowView = new RainSnowView(MyApp.getContex(), true,25, 18);
				rtLayout.addView(minRainSnowView);
			} else if (Constants.DAYU.equals(rainSnow)||Constants.ZHONGYU_DAYUE.equals(rainSnow)) {
				rtLayout.removeView(minRainSnowView);
				minRainSnowView = new RainSnowView(MyApp.getContex(), true,35, 27);
				rtLayout.addView(minRainSnowView);
			}else if (Constants.BAOYU.equals(rainSnow)||Constants.DAYU_BAOYU.equals(rainSnow)||Constants.BAOYU_DABAOYU.equals(rainSnow)) {
				rtLayout.removeView(minRainSnowView);
				minRainSnowView = new RainSnowView(MyApp.getContex(), true,45, 27);
				rtLayout.addView(minRainSnowView);
			}else if (Constants.XIAOXUE.equals(rainSnow)||Constants.ZHENXUE.equals(rainSnow)) {
				rtLayout.removeView(minRainSnowView);
				minRainSnowView = new RainSnowView(MyApp.getContex(), false,15, 9);
				rtLayout.addView(minRainSnowView);
			} else if (Constants.ZHONGXUE.equals(rainSnow)||Constants.XIAOXUE_ZHONGXUE.equals(rainSnow)) {
				rtLayout.removeView(minRainSnowView);
				minRainSnowView = new RainSnowView(MyApp.getContex(), false,25, 15);
				rtLayout.addView(minRainSnowView);
			}else if (Constants.DAXUE.equals(rainSnow)||Constants.ZHONGXUE_DAXUE.equals(rainSnow)) {
				rtLayout.removeView(minRainSnowView);
				minRainSnowView = new RainSnowView(MyApp.getContex(), false,35, 20);
				rtLayout.addView(minRainSnowView);
			}else if (Constants.BAOXUE.equals(rainSnow)||Constants.DAXUE_BAOXUE.equals(rainSnow)) {
				rtLayout.removeView(minRainSnowView);
				minRainSnowView = new RainSnowView(MyApp.getContex(), false,45, 20);
				rtLayout.addView(minRainSnowView);
			}else if (Constants.YUJIAXUE.equals(rainSnow)) {
				rtLayout.removeView(minRainSnowView);
				minRainSnowView = new RainSnowView(MyApp.getContex(), false,15, 15);
				rtLayout.addView(minRainSnowView);
			}
		*/
/* if (MIN_RAIN.equals(rainSnow)||ZHENYU.equals(rainSnow)) {
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
			}*//*

		}
	}
*/
    @Override
    public void onClick(View view) {
    }
}
