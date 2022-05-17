package cn.surveyking.server.core.uitls;

import cn.surveyking.server.core.constant.FieldPermissionType;
import cn.surveyking.server.core.constant.ProjectModeEnum;
import cn.surveyking.server.domain.dto.*;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author javahuang
 * @date 2021/8/10
 */
public class SchemaParser {

	protected static ThreadLocal<Boolean> localOpenId = new ThreadLocal<>();

	public static final String openidColumnName = "自定义字段";

	public static List<String> parseColumns(List<SurveySchema> schemaDataTypes, ProjectModeEnum mode) {
		List<String> result = new ArrayList<>();
		result.add("序号");
		schemaDataTypes.forEach(schemaType -> {
			result.add(schemaType.getTitle());
		});
		if (ProjectModeEnum.exam.equals(mode)) {
			result.add("分数");
		}
		result.add(openidColumnName);
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
	public static List<SurveySchema> flatSurveySchema(SurveySchema schema) {
		List<SurveySchema> dataTypes = new ArrayList<>();
		if (SurveySchema.QuestionType.dataType().contains(schema.getType())) {
			SurveySchema dataType = (SurveySchema) schema.clone();
			dataType.setTitle(trimHtmlTag(schema.getTitle()));
			dataTypes.add(dataType);
		}
		if (schema.getChildren() != null) {
			schema.getChildren().forEach(child -> {
				dataTypes.addAll(flatSurveySchema(child));
			});
		}

		return dataTypes;
	}

	public static List<Object> parseRowData(AnswerView answerInfo, List<SurveySchema> dataTypes, int index,
			ProjectModeEnum mode) {
		LinkedHashMap<String, Object> answer = answerInfo.getAnswer();
		List<Object> rowData = new ArrayList<>();
		rowData.add(index);
		// 转换答案
		// TODO: 中途修改 schema 可能会出错、提取公共解析方法
		for (SurveySchema schemaType : dataTypes) {
			String questionId = schemaType.getId();
			SurveySchema.QuestionType questionType = schemaType.getType();
			Object valueObj = answer.get(questionId);

			if (valueObj == null) {
				rowData.add(null);
				continue;
			}
			if (questionType == SurveySchema.QuestionType.Upload
					|| questionType == SurveySchema.QuestionType.Signature) {
				Map mapValue = (Map) valueObj;
				rowData.add(mapValue.values().stream().map((x) -> {
					List<String> fileIds = (List<String>) x;
					return fileIds.stream()
							.map(id -> answerInfo.getAttachment().stream()
									.filter(attachment -> attachment.getId().equals(id)).findFirst()
									.orElse(new FileView()).getOriginalName())
							.collect(Collectors.joining(","));
				}).collect(Collectors.joining(",")));
			}
			else if (questionType == SurveySchema.QuestionType.Cascader) {
				Map mapValue = (Map) valueObj;
				List<String> result = new ArrayList<>();
				List<SurveySchema.DataSource> dataSources = schemaType.getDataSource();
				for (SurveySchema child : schemaType.getChildren()) {
					String optionId = child.getId();
					String optionValue = (String) mapValue.get(optionId);
					SurveySchema.DataSource dataSource = dataSources.stream()
							.filter(x -> x.getValue().equals(optionValue)).findFirst()
							.orElse(new SurveySchema.DataSource("", "", new ArrayList<>()));
					result.add(dataSource.getLabel());
					dataSources = dataSource.getChildren();
				}
				rowData.add(String.join(",", result));
			}
			else if (questionType == SurveySchema.QuestionType.User) {
				Map mapValue = (Map) valueObj;
				rowData.add(mapValue.values().stream().map((x) -> {
					List<String> userIds = (List<String>) x;
					return userIds.stream().map(id -> answerInfo.getUsers().stream()
							.filter(user -> user.getUserId().equals(id)).findFirst().orElse(new UserInfo()).getName())
							.collect(Collectors.joining(","));
				}).collect(Collectors.joining(",")));
			}
			else if (questionType == SurveySchema.QuestionType.Dept) {
				Map mapValue = (Map) valueObj;
				rowData.add(mapValue.values().stream().map((x) -> {
					List<String> userIds = (List<String>) x;
					return userIds.stream().map(id -> answerInfo.getDepts().stream()
							.filter(dept -> dept.getId().equals(id)).findFirst().orElse(new DeptView()).getName())
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
			else if (questionType == SurveySchema.QuestionType.MatrixAuto) {
				List<String> result = new ArrayList<>();
				((List<Map<?, ?>>) valueObj).forEach((rowValue) -> {
					List<String> matrixRowData = new ArrayList<>();
					rowValue.forEach((optionId, v) -> {
						SurveySchema optionSchema = schemaType.getChildren().stream()
								.filter(option -> option.getId().equals(optionId)).findFirst().orElse(null);
						if (optionSchema != null && optionSchema.getDataSource() != null) {
							matrixRowData.add(optionSchema.getDataSource().stream().filter(x -> x.getValue().equals(v))
									.findFirst().orElse(new SurveySchema.DataSource()).getLabel());
						}
						else if (v != null && v instanceof Boolean) {
							// 单选、多选题，选中的话，答案会是 true，需要转换成标题
							matrixRowData.add(trimHtmlTag(schemaType.getChildren().stream()
									.filter(x -> x.getId().equals(optionId)).findFirst().get().getTitle()));
						}
						else {
							matrixRowData.add(v + "");
						}
					});
					result.add(String.format("(%s)", String.join(",", matrixRowData)));
				});
				rowData.add(String.join(",", result));
			}
			else if (questionType.name().startsWith("Matrix")) {
				List<String> result = new ArrayList<>();
				((Map<?, ?>) valueObj).forEach((optionId, valueMap) -> {
					String title = trimHtmlTag(schemaType.getRow().stream().filter(x -> x.getId().equals(optionId))
							.findFirst().get().getTitle());
					List<String> valueList = new ArrayList<>();
					((LinkedHashMap) valueMap).forEach((childOptId, val) -> {
						if (val != null && val instanceof Boolean) {
							valueList.add(trimHtmlTag(schemaType.getChildren().stream()
									.filter(x -> x.getId().equals(childOptId)).findFirst().get().getTitle()));
						}
						else {
							valueList.add(val + "");
						}
					});
					result.add(String.format("%s:(%s)", title, String.join(",", valueList)));
				});
				rowData.add(String.join(",", result));
			}
			else {
				rowData.add(null);
			}
		}
		if (ProjectModeEnum.exam.equals(mode)) {
			rowData.add(answerInfo.getExamScore());
		}
		if (answer.containsKey("openid")) {
			rowData.add(answer.get("openid"));
			localOpenId.set(true);
		}
		else {
			rowData.add("");
		}
		// 转换答卷元数据
		rowData.add(answerInfo.getCreateByName());
		Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		rowData.add(formatter.format(answerInfo.getCreateAt()));
		rowData.add(parseHumanReadableDuration(answerInfo));
		rowData.add(answerInfo.getMetaInfo().getClientInfo().getDeviceType());
		rowData.add(answerInfo.getMetaInfo().getClientInfo().getPlatform());
		rowData.add(answerInfo.getMetaInfo().getClientInfo().getBrowser());
		rowData.add(answerInfo.getMetaInfo().getClientInfo().getRegion());
		rowData.add(answerInfo.getMetaInfo().getClientInfo().getRemoteIp());
		rowData.add(answerInfo.getId());

		avoidFormulaInjection(rowData);
		return rowData;
	}

	private static void avoidFormulaInjection(List<Object> rowData) {
		ListIterator<Object> iterator = rowData.listIterator();
		while (iterator.hasNext()) {
			Object next = iterator.next();
			if (next instanceof String && (((String) next).startsWith("=") || ((String) next).startsWith("+")
					|| ((String) next).startsWith("-") || ((String) next).startsWith("@"))) {
				iterator.set("'" + next);
			}
		}
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

	public static void updateSchemaByPermission(LinkedHashMap<String, Integer> fieldPermission, SurveySchema schema) {
		if (schema.getChildren() == null || fieldPermission == null) {
			return;
		}
		schema.getChildren().removeIf(child -> {
			Integer permValue = fieldPermission.get(child.getId());
			if (permValue == null) {
				return false;
			}
			// 隐藏题目
			if (permValue == FieldPermissionType.hidden) {
				return true;
			}
			// 只读
			if (permValue == FieldPermissionType.visible) {
				if (child.getAttribute() == null) {
					child.setAttribute(new SurveySchema.Attribute());
				}
				child.getAttribute().setReadOnly(true);
			}
			return false;
		});
		schema.getChildren().forEach(child -> updateSchemaByPermission(fieldPermission, child));
	}

}
