package cn.surveyking.server.api.service.impl;

import cn.surveyking.server.api.domain.dto.ReportData;
import cn.surveyking.server.api.domain.model.Answer;
import cn.surveyking.server.api.mapper.AnswerMapper;
import cn.surveyking.server.api.service.ReportService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
		QueryWrapper<Answer> wrapper = new QueryWrapper<>();
		wrapper.select("answer").eq("short_id", shortId);
		List<Answer> answerList = answerMapper.selectList(wrapper);
		ReportData result = new ReportData();
		result.setTotal(answerList.size());
		Map<String, ReportData.Data> data = new HashMap<>();
		result.setStatistics(data);
		for (Answer answer : answerList) {
			parseAnswer(data, answer.getAnswer());
		}
		return result;
	}

	// TODO: 考虑分页取，然后计算
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
				optionData.setTotal(optionData.getTotal() + 1);
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

	public int compareTo(Number n1, Number n2) {
		BigDecimal b1 = new BigDecimal(n1.doubleValue());
		BigDecimal b2 = new BigDecimal(n2.doubleValue());
		return b1.compareTo(b2);
	}

}
