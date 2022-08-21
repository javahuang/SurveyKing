package cn.surveyking.server.core.base.converter;

import cn.surveyking.server.core.uitls.MapBeanUtils;
import cn.surveyking.server.domain.dto.ProjectSetting;
import lombok.SneakyThrows;
import org.springframework.core.convert.converter.Converter;

import java.util.LinkedHashMap;

/**
 * @author javahuang
 * @date 2022/5/15
 */
public class RandomSurveyConverter
		implements Converter<LinkedHashMap<String, Object>, ProjectSetting.RandomSurveyCondition> {

	@Override
	@SneakyThrows
	public ProjectSetting.RandomSurveyCondition convert(LinkedHashMap<String, Object> value) {
		Object examScore = value.get("examScore");
		// 将 Integer/Double 统一转换为 Double
		if (examScore != null && examScore instanceof Integer) {
			value.put("examScore", ((Integer) examScore).doubleValue());
		}

		return MapBeanUtils.mapToBean(value, ProjectSetting.RandomSurveyCondition.class);
	}

}
