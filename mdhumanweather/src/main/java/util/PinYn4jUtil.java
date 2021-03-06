package util;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

/**
 * 拼音汉字转化的工具类
 */
public class PinYn4jUtil {
	/**
	 * 将汉字转为全拼
	 * @param src
	 * @return
     */
	public static String getPinYin(String src) {
		char[] t1 = null;
		t1 = src.toCharArray();
		String[] t2 = new String[t1.length];
		//设置汉字拼音的输出格式
		HanyuPinyinOutputFormat t3 = new HanyuPinyinOutputFormat();
		t3.setCaseType(HanyuPinyinCaseType.UPPERCASE);
		t3.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		t3.setVCharType(HanyuPinyinVCharType.WITH_V);
		String t4 = "";
		int t0 = t1.length;
		try {
			for (int i = 0; i < t0; i++) {
				//判断能否为汉字字符
				if (Character.toString(t1[i]).matches("[\u4E00-\u9FA5]+")) {
					t2 = PinyinHelper.toHanyuPinyinStringArray(t1[i], t3);//将汉字的集中拼音都存到t2数组中
					t4 += t2[0];//取出该汉字拼音的第一种拼音并连接到字符串t4后
				} else {
					//如果不是汉字字符，间接取出字符并连接到t4后
					t4 += Character.toString(t1[i]);
				}
			}
		} catch (BadHanyuPinyinOutputFormatCombination e) {
			e.printStackTrace();
		}
		return t4;
	}

	/**
	 * 提取每个汉字的首字母
	 * @param str
	 * @return
     */
	public static String getPinYinHeadChar(String str) {

		String convert = "";
		//设置汉字拼音输出的格式
		HanyuPinyinOutputFormat t3 = new HanyuPinyinOutputFormat();
		t3.setCaseType(HanyuPinyinCaseType.UPPERCASE);
		try {
			for (int j = 0; j < str.length(); j++) {
				char word = str.charAt(j);
				//提取汉字的首字母
				String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(
						word, t3);
				if (pinyinArray != null) {
					convert += pinyinArray[0].charAt(0);
				} else {
					convert += word;
				}
			}
		} catch (Exception e) {
		}

		return convert;
	}

	/**
	 * 将字符串转换成ASCII码
	 * @param cnStr
	 * @return
     */
	public static String getCnASCII(String cnStr) {
		StringBuffer strBuf = new StringBuffer();
		// ���ַ���ת�����ֽ�����
		byte[] bGBK = cnStr.getBytes();
		for (int i = 0; i < bGBK.length; i++) {
			//将每个字符转换成ASCII码
			strBuf.append(Integer.toHexString(bGBK[i] & 0xff));
		}
		return strBuf.toString();
	}

}
