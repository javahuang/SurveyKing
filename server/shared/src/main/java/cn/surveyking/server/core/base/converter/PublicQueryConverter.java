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
public class PublicQueryConverter implements Converter<LinkedHashMap<String, ?>, ProjectSetting.PublicQuery> {

	@Override
	@SneakyThrows
	public ProjectSetting.PublicQuery convert(LinkedHashMap<String, ?> value) {
		return MapBeanUtils.mapToBean(value, ProjectSetting.PublicQuery.class);
	}

}
