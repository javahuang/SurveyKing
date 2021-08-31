package cn.surveyking.server.core.uitls;

import cn.surveyking.server.api.domain.dto.SurveySchemaType;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author javahuang
 * @date 2021/8/10
 */
public class SchemaParser {

	/**
	 * @param schema
	 * @return 有数据
	 */
	public static List<SurveySchemaType> parseDataTypes(SurveySchemaType schema) {
		List<SurveySchemaType> dataTypes = new ArrayList<>();
		if (SurveySchemaType.QuestionType.dataType().contains(schema.getType())) {
			SurveySchemaType dataType = (SurveySchemaType) schema.clone();
			dataType.setTitle(trimHtmlTag(schema.getTitle()));
			dataTypes.add(dataType);
		}
		if (schema.getChildren() != null) {
			schema.getChildren().forEach(child -> {
				dataTypes.addAll(parseDataTypes(child));
			});
		}

		return dataTypes;
	}

	public static List<Object> parseRowData(LinkedHashMap answer, List<SurveySchemaType> dataTypes) {
		List<Object> rowData = new ArrayList<>();
		for (SurveySchemaType schemaType : dataTypes) {
			String questionId = schemaType.getId();
			SurveySchemaType.QuestionType questionType = schemaType.getType();
			Object valueObj = answer.get(questionId);

			if (valueObj == null) {
				rowData.add(null);
				continue;
			}
			if (!questionType.name().startsWith("Matrix")) {
				Map mapValue = (Map) valueObj;
				rowData.add(mapValue.values().stream().collect(Collectors.joining(",")));
			}
			else {
				// TODO: 矩阵题如何展示答案
				rowData.add(null);
			}
		}
		return rowData;
	}

	/**
	 * 更好的方式是使用 Jsoup.parse(html).text(); 但是我不想引入过多的第三方 jar
	 * @param string
	 * @return
	 */
	public static String trimHtmlTag(String string) {
		if (string == null) {
			return "";
		}
		return string.replaceAll("(<.*?>)|(&.*?;)", " ").replaceAll("\\s{2,}", " ").trim();
	}

}
