package cn.surveyking.server.core.uitls;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * @author javahuang
 * @date 2022/6/5
 */
public class PinyinUtils {

	/**
	 * 汉字转拼音
	 * @param str 要转换的字符串
	 * @return 汉字转拼音
	 */
	public static String chineseToPinyin(String str) {
		if (isBlank(str)) {
			return str;
		}
		StringBuilder result = new StringBuilder();
		HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
		format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		for (char c : str.toCharArray()) {
			// 非汉字原样输出
			if (Character.toString(c).matches("[\\u4E00-\\u9FA5]+")) {
				String[] charPinyin = new String[0];
				try {
					charPinyin = PinyinHelper.toHanyuPinyinStringArray(c, format);
				}
				catch (BadHanyuPinyinOutputFormatCombination e) {
					e.printStackTrace();
				}
				if (charPinyin != null) {
					result.append(charPinyin[0]);
				}
			}
			else {
				result.append(c);
			}
		}
		return result.toString();
	}

}
