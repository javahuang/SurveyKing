package cn.surveyking.server.core.uitls;

import cn.surveyking.server.domain.dto.AnswerView;
import cn.surveyking.server.domain.dto.SurveySchemaType;

import java.text.Format;
import java.text.SimpleDateFormat;
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

	public static List<String> parseColumns(List<SurveySchemaType> schemaDataTypes) {
		List<String> result = new ArrayList<>();
		result.add("序号");
		schemaDataTypes.forEach(schemaType -> {
			result.add(schemaType.getTitle());
		});
		result.add("提交人");
		result.add("提交时间");
		result.add("填写时长");
		result.add("填写设备");
		result.add("操作系统");
		result.add("浏览器");
		result.add("填写地区");
		result.add("IP");
		result.add("ID");
		return result;
	}

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

	public static List<Object> parseRowData(AnswerView answerInfo, List<SurveySchemaType> dataTypes, int index) {
		LinkedHashMap answer = answerInfo.getAnswer();
		List<Object> rowData = new ArrayList<>();
		rowData.add(index);
		// 转换答案
		for (SurveySchemaType schemaType : dataTypes) {
			String questionId = schemaType.getId();
			SurveySchemaType.QuestionType questionType = schemaType.getType();
			Object valueObj = answer.get(questionId);

			if (valueObj == null) {
				rowData.add(null);
				continue;
			}
			if (questionType == SurveySchemaType.QuestionType.Upload) {
				Map mapValue = (Map) valueObj;
				rowData.add(mapValue.values().stream().map((x) -> {
					List<String> fileIds = (List<String>) x;
					return fileIds.stream()
							.map(id -> answerInfo.getAttachment().stream()
									.filter(attachment -> attachment.getId().equals(id)).findFirst().get()
									.getOriginalName())
							.collect(Collectors.joining(","));
				}).collect(Collectors.joining(",")));
			}
			else if (!questionType.name().startsWith("Matrix")) {
				// 需要将数字类型转换成字符串
				List<String> result = new ArrayList<>();
				((Map<?, ?>) valueObj).forEach((optionId, v) -> {
					if (v != null && v instanceof Boolean) {
						// 单选、多选题，选中的话，答案会是 true，需要转换成标题
						result.add(trimHtmlTag(schemaType.getChildren().stream().filter(x -> x.getId().equals(optionId))
								.findFirst().get().getTitle()));
					}
					else {
						result.add(v + "");
					}
				});
				rowData.add(String.join(",", result));
			}
			else {
				// TODO: 矩阵题如何展示答案
				rowData.add(null);
			}
		}
		// 转换答卷元数据
		rowData.add(answerInfo.getCreateBy());
		Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		rowData.add(formatter.format(answerInfo.getCreateAt()));
		rowData.add(parseHumanReadableDuration(answerInfo));
		rowData.add(answerInfo.getMetaInfo().getClientInfo().getDeviceType());
		rowData.add(answerInfo.getMetaInfo().getClientInfo().getPlatform());
		rowData.add(answerInfo.getMetaInfo().getClientInfo().getBrowser());
		rowData.add(answerInfo.getMetaInfo().getClientInfo().getRegion());
		rowData.add(answerInfo.getMetaInfo().getClientInfo().getRemoteIp());
		rowData.add(answerInfo.getId());
		return rowData;
	}

	private static String parseHumanReadableDuration(AnswerView answerInfo) {
		long duration = answerInfo.getMetaInfo().getAnswerInfo().getEndTime()
				- answerInfo.getMetaInfo().getAnswerInfo().getStartTime();
		double d = Math.floor(duration / (3600000 * 24));
		double h = Math.floor((duration / 3600000) % 24);
		double m = Math.floor((duration / 60000) % 60);
		double s = Math.floor((duration / 1000) % 60);
		String result = "";
		if (d > 0) {
			result += d + "天";
		}
		if (h > 0) {
			result += h + "小时";
		}
		if (m > 0) {
			result += m + "分钟";
		}
		if (s > 0) {
			result += s + "秒";
		}
		return result;
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
