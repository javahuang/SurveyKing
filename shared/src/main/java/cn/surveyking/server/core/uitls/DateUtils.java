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

}
