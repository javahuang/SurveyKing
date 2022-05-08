package cn.surveyking.server.impl;

import cn.surveyking.server.domain.dto.ReportData;
import cn.surveyking.server.domain.model.Answer;
import cn.surveyking.server.mapper.AnswerMapper;
import cn.surveyking.server.service.ReportService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author javahuang
 * @date 2021/8/4
 */
@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

	private final AnswerMapper answerMapper;

	@Override
	public ReportData getData(String shortId) {
		List<Answer> answerList = answerMapper
				.selectList(Wrappers.<Answer>lambdaQuery().select(Answer::getAnswer, Answer::getCreateAt)
						.eq(Answer::getProjectId, shortId).orderByAsc(Answer::getCreateAt));
		ReportData result = new ReportData();
		result.setTotal(answerList.size());
		Map<String, ReportData.Data> data = new HashMap<>();
		result.setStatistics(data);
		Map<String, Integer> dailyCountStat = new LinkedHashMap<>();
		result.setDailyCountStat(dailyCountStat);
		for (Answer answer : answerList) {
			parseAnswer(data, answer.getAnswer());
			computeDailyAnswer(dailyCountStat, answer);
		}
		return result;
	}

	// TODO: 考虑分页取，然后计算

	/**
	 * 选项报表统计
	 * @param data
	 * @param answer
	 */
	private void parseAnswer(Map<String, ReportData.Data> data, LinkedHashMap answer) {
		Iterator it = answer.entrySet().iterator();

		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry) it.next();
			String id = (String) pair.getKey();
			Object value = pair.getValue();
			ReportData.Data optionData = data.computeIfAbsent(id, s -> new ReportData.Data());
			optionData.setTotal(optionData.getTotal() + 1);
			if (value instanceof Map) {
				parseAnswer(data, (LinkedHashMap) value);
			}
			else if (value instanceof Number) {
				Number numberValue = (Number) value;
				if (optionData.getMin() == null || optionData.getMax() == null || optionData.getAverage() == null) {
					optionData.setMin(numberValue);
					optionData.setMax(numberValue);
					optionData.setAverage(numberValue);
				}
				if (compareTo(optionData.getMin(), numberValue) > 0) {
					optionData.setMin(numberValue);
				}
				if (compareTo(numberValue, optionData.getMax()) > 0) {
					optionData.setMax(numberValue);
				}
				optionData.setSum(new BigDecimal(optionData.getSum().doubleValue())
						.add(new BigDecimal(numberValue.doubleValue())));
				optionData.setAverage(new BigDecimal(optionData.getSum().doubleValue())
						.divide(new BigDecimal(optionData.getTotal()), 2, RoundingMode.HALF_UP)
						.setScale(2, BigDecimal.ROUND_HALF_UP));
			}
		}
	}

	private void computeDailyAnswer(Map<String, Integer> dailyCountStat, Answer answer) {
		String day = new SimpleDateFormat("yyyy-MM-dd").format(answer.getCreateAt());
		dailyCountStat.merge(day, 1, Integer::sum);
	}

	public int compareTo(Number n1, Number n2) {
		BigDecimal b1 = new BigDecimal(n1.doubleValue());
		BigDecimal b2 = new BigDecimal(n2.doubleValue());
		return b1.compareTo(b2);
	}

}
