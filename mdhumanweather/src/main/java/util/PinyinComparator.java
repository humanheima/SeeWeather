package util;

import java.util.Comparator;

import model.WeatherCityModel;

/**排序效果不是太好是有原因的
 * 因为每个省份的第一个市开头是这样的
 * 比如北京朝阳区
 * citynm 朝阳 cityo bjchaoyang
 * @author human
 *
 */
public class PinyinComparator implements Comparator<WeatherCityModel> {

	@Override
	public int compare(WeatherCityModel m0, WeatherCityModel m1) {

		String m0no = m0.getCityno().substring(0, 1);
		String m1no = m1.getCityno().substring(0, 1);
		return m0no.compareTo(m1no);
	}

}
