package util;

import java.util.Comparator;

import model.WeatherCityModel;

/**����Ч������̫������ԭ���
 * ��Ϊÿ��ʡ�ݵĵ�һ���п�ͷ��������
 * ���籱��������
 * citynm ���� cityo bjchaoyang
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
