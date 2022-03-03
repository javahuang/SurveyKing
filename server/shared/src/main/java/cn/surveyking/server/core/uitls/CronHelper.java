package cn.surveyking.server.core.uitls;

import cn.surveyking.server.core.common.Tuple2;
import org.springframework.scheduling.support.CronExpression;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * @author javahuang
 * @date 2022/2/28
 */
public class CronHelper {

	private final String cron;

	private final CronExpression cronExpression;

	public CronHelper(String cron) {
		this.cron = cron;
		if (CronExpression.isValidExpression(cron)) {
			this.cronExpression = CronExpression.parse(cron);
		}
		else {
			this.cronExpression = null;
		}
	}

	/**
	 * @return 当前时间窗
	 */
	public Tuple2<LocalDateTime, LocalDateTime> currentWindow() {
		if (this.cronExpression == null) {
			return defaultWindow();
		}
		Tuple2<LocalDateTime, LocalDateTime> nextWindow = nextWindow();
		long millis = ChronoUnit.MILLIS.between(nextWindow.getFirst(), nextWindow.getSecond());
		LocalDateTime currentWindowStart = nextWindow.getFirst().minus(millis, ChronoUnit.MILLIS);
		return new Tuple2<>(currentWindowStart, nextWindow.getFirst());
	}

	/**
	 * 下个时间窗
	 * @return
	 */
	public Tuple2<LocalDateTime, LocalDateTime> nextWindow() {
		if (this.cronExpression == null) {
			return defaultWindow();
		}
		LocalDateTime nextWindowStart = cronExpression.next(LocalDateTime.now());
		LocalDateTime nextWindowEnd = cronExpression.next(nextWindowStart);
		return new Tuple2(nextWindowStart, nextWindowEnd);
	}

	private Tuple2<LocalDateTime, LocalDateTime> defaultWindow() {
		return null;
	}

}
