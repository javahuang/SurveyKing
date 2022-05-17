package cn.surveyking.server.core.uitls;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * @author javahuang
 * @date 2022/2/28
 */
public class DateUtils {

	public static Date localDateTime2date(LocalDateTime in) {
		return Date.from(in.atZone(ZoneId.systemDefault()).toInstant());
	}

	public static LocalDateTime date2localDateTime(Date in) {
		return LocalDateTime.ofInstant(in.toInstant(), ZoneId.systemDefault());
	}

	/**
	 * @param current 当前日期
	 * @param start 起始日期
	 * @param end 结束日期
	 * @return 当前日期是否在指定日期范围内
	 */
	public static boolean isBetween(Date current, Date start, Date end) {
		if (current.compareTo(start) >= 0 && current.compareTo(end) <= 0) {
			return true;
		}
		return false;
	}

}
