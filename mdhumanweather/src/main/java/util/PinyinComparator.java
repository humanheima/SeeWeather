package util;

import java.util.Comparator;

import model.WeatherCityModel;

/**
 * 拼音比较器
 */
public class PinyinComparator implements Comparator<WeatherCityModel> {

	@Override
	public int compare(WeatherCityModel m0, WeatherCityModel m1) {

		String m0no = m0.getCityno().substring(0, 1);
		String m1no = m1.getCityno().substring(0, 1);
		return m0no.compareTo(m1no);
	}

}
