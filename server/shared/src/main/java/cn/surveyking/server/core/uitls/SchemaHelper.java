package cn.surveyking.server.core.uitls;

import cn.surveyking.server.core.constant.FieldPermissionType;
import cn.surveyking.server.core.constant.ProjectModeEnum;
import cn.surveyking.server.domain.dto.*;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * schema的一些帮助方法
 *
 * @author javahuang
 * @date 2021/8/10
 */
public class SchemaHelper {

	protected static ThreadLocal<Boolean> localOpenId = new ThreadLocal<>();

	public static final String openidColumnName = "自定义字段";

	/**
	 * 将 schema 解析为导出的 excel的header
	 * @param schemaDataTypes
	 * @param mode
	 * @return
	 */
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
		return flatSurveySchemaWitchOption(schema, false);
	}

	/**
	 * @param schema
	 * @param withOption 是否解析选项
	 * @return 有数据
	 */
	public static List<SurveySchema> flatSurveySchemaWitchOption(SurveySchema schema, boolean withOption) {
		List<SurveySchema> dataTypes = new ArrayList<>();
		// 选项 schema 额类型为 Option 或者为空
		if (SurveySchema.QuestionType.dataType().contains(schema.getType()) || (withOption
				&& (SurveySchema.QuestionType.Option.equals(schema.getType()) || schema.getType() == null))) {
			SurveySchema dataType = schema.deepCopy();
			dataType.setTitle(trimHtmlTag(schema.getTitle()));
			dataTypes.add(dataType);
		}
		if (schema.getChildren() != null) {
			schema.getChildren().forEach(child -> {
				dataTypes.addAll(flatSurveySchemaWitchOption(child, withOption));
			});
		}

		return dataTypes;
	}

	/**
	 * 转换答案为导出 excel 的行格式
	 * @param answerInfo 单条答案记录
	 * @param dataTypes 所有的问题 schema
	 * @param index 当前行索引
	 * @param mode 项目模式
	 * @return excel 行记录
	 */
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
					if (dataSources == null) {
						break;
					}
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
				// 通过 valueObj 遍历可能会导致选项的顺序乱掉，所以得按照 children schema 的顺序来构建答案
				rowData.add(schemaType.getChildren().stream().map(optionSchema -> {
					Object optionValue = ((Map<?, ?>) valueObj).get(optionSchema.getId());
					if (optionValue == null) {
						return null;
					}
					if (optionValue != null && optionValue instanceof Boolean) {
						// 单选、多选题，选中的话，答案会是 true，需要转换成标题
						return trimHtmlTag(optionSchema.getTitle());
					}
					// 如果是单选填空，需要同时显示选项标题和答案
					if (schemaType.getType().equals(SurveySchema.QuestionType.Radio)
							|| schemaType.getType().equals(SurveySchema.QuestionType.Checkbox)) {
						return String.format("%s(%s)", trimHtmlTag(optionSchema.getTitle()), optionValue);
					}
					return optionValue.toString();
				}).filter(x -> x != null).collect(Collectors.joining(",")));
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
		if (answerInfo.getMetaInfo() == null || answerInfo.getMetaInfo().getClientInfo() == null) {
			rowData.addAll(Arrays.asList("", "", "", "", ""));
		}
		else {
			rowData.add(answerInfo.getMetaInfo().getClientInfo().getDeviceType());
			rowData.add(answerInfo.getMetaInfo().getClientInfo().getPlatform());
			rowData.add(answerInfo.getMetaInfo().getClientInfo().getBrowser());
			rowData.add(answerInfo.getMetaInfo().getClientInfo().getRegion());
			rowData.add(answerInfo.getMetaInfo().getClientInfo().getRemoteIp());
			rowData.add(answerInfo.getId());
		}

		avoidFormulaInjection(rowData);
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

	/**
	 * 移除 schema 里面的指定属性值
	 * @param schema
	 * @param attributes
	 */
	public static void ignoreAttributes(SurveySchema schema, String... attributes) {
		if (schema.getChildren() == null) {
			return;
		}
		schema.getChildren().forEach(child -> {
			for (String attributeName : attributes) {
				BeanWrapper wrapper = PropertyAccessorFactory.forBeanPropertyAccess(schema.getAttribute());
				wrapper.setPropertyValue(attributeName, null);
			}
			ignoreAttributes(child, attributes);
		});
	}

	/**
	 * 根据属性名和属性值找到所有满足条件的子 schema 列表
	 * @param schema
	 * @param attributeName
	 * @param attributeValue
	 */
	public static List<SurveySchema> findSchemaListByAttribute(SurveySchema schema, String attributeName,
			Object attributeValue) {
		List<SurveySchema> result = new ArrayList<>();
		BeanWrapper wrapper = PropertyAccessorFactory.forBeanPropertyAccess(schema.getAttribute());
		if (attributeValue.equals(wrapper.getPropertyValue(attributeName))) {
			result.add(schema);
		}
		if (schema.getChildren() != null) {
			schema.getChildren().forEach(child -> {
				result.addAll(findSchemaListByAttribute(child, attributeName, attributeValue));
			});
		}
		return result;
	}

	/**
	 * 主要是用于构建 FillBlank 类型的查询表单
	 * @param field
	 * @return
	 */
	public static SurveySchema buildFillBlankQuerySchema(LoginFormFieldEnum field) {
		return SurveySchema.builder().id(field.name()).type(SurveySchema.QuestionType.FillBlank).title(field.getTitle())
				.attribute(SurveySchema.Attribute.builder().required(true).build())
				.children(Collections.singletonList(SurveySchema.builder().id(field.name()).build())).build();
	}

	public static Object getLoginFormAnswer(LinkedHashMap<String, Object> answer, LoginFormFieldEnum field) {
		Map<String, Object> questionValue = (Map<String, Object>) answer.get(field.name());
		if (questionValue == null) {
			return null;
		}
		return questionValue.get(field.name());
	}

	/**
	 * 将问题 schema 添加到问卷里面
	 * @param parent 问卷 schema
	 * @param child 问题 schema
	 */
	public static void appendChildIfNotExist(SurveySchema parent, SurveySchema child) {
		boolean exists = flatSurveySchema(parent).stream().anyMatch(x -> child.getId().equals(x.getId()));
		if (!exists) {
			parent.getChildren().add(child);
		}
	}

	public static TreeNode SurveySchema2TreeNode(SurveySchema surveySchema) {
		return new TreeNode(surveySchema, null);
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
		String result = "";
		if (answerInfo.getMetaInfo() == null || answerInfo.getMetaInfo().getAnswerInfo() == null) {
			return result;
		}
		long duration = answerInfo.getMetaInfo().getAnswerInfo().getEndTime()
				- answerInfo.getMetaInfo().getAnswerInfo().getStartTime();
		double d = Math.floor(duration / (3600000 * 24));
		double h = Math.floor((duration / 3600000) % 24);
		double m = Math.floor((duration / 60000) % 60);
		double s = Math.floor((duration / 1000) % 60);
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

	public static class TreeNode {

		SurveySchema data;

		TreeNode parent;

		TreeNode root;

		Map<String, TreeNode> treeNodeMap = new LinkedHashMap<>();

		public TreeNode(SurveySchema data, TreeNode parent) {
			this.data = data;
			if (parent == null) {
				this.root = this;
			}
			else {
				this.root = parent.root;
				this.parent = parent;
			}
			this.root.getTreeNodeMap().put(data.getId(), this);
			if (data.getChildren() != null) {
				data.getChildren().forEach(child -> {
					new TreeNode(child, this);
				});
			}
		}

		public Map<String, TreeNode> getTreeNodeMap() {
			return treeNodeMap;
		}

		public TreeNode getParent() {
			return parent;
		}

		public SurveySchema getData() {
			return data;
		}

	}

	public enum LoginFormFieldEnum {

		username("用户名"), password("密码"), extraPassword("请输入问卷密码"), whitelistName("请先输入名单，再进行填写");

		private String title;

		LoginFormFieldEnum(String title) {
			this.title = title;
		}

		public String getTitle() {
			return title;
		}

	}

}
