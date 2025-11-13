package cn.surveyking.server.core.uitls;

import cn.surveyking.server.core.constant.FieldPermissionType;
import cn.surveyking.server.core.constant.ProjectModeEnum;
import cn.surveyking.server.domain.dto.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.util.CollectionUtils;

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
@Slf4j
public class SchemaHelper {

	protected static ThreadLocal<Boolean> localOpenId = new ThreadLocal<>();

	// 常量定义
	public static final String OPENID_COLUMN_NAME = "自定义字段";
	public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
	public static final String HTML_TAG_REGEX = "(<.*?>)|(&.*?;)";
	public static final String MULTIPLE_SPACES_REGEX = "\\s{2,}";
	public static final String DICT_SEPARATOR = "\\|";
	public static final int DICT_LABEL_INDEX = 1;

	private static final String[] FORMULA_INJECTION_PREFIXES = { "=", "+", "-", "@" };
	private static final long MILLISECONDS_PER_SECOND = 1000L;
	private static final long MILLISECONDS_PER_MINUTE = 60000L;
	private static final long MILLISECONDS_PER_HOUR = 3600000L;
	private static final long MILLISECONDS_PER_DAY = MILLISECONDS_PER_HOUR * 24;

	private static final MessageSource MESSAGE_SOURCE = initMessageSource();

	@Deprecated
	public static final String openidColumnName = OPENID_COLUMN_NAME;

	private static MessageSource initMessageSource() {
		ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
		messageSource.setBasenames("i18n/schema");
		messageSource.setDefaultEncoding("UTF-8");
		messageSource.setFallbackToSystemLocale(false);
		return messageSource;
	}

	public static String getOpenIdColumnName() {
		return i18n("schema.column.customField", OPENID_COLUMN_NAME);
	}

	private static String i18n(String code, String defaultMessage, Object... args) {
		Locale locale = LocaleContextHolder.getLocale();
		if (locale == null) {
			locale = Locale.SIMPLIFIED_CHINESE;
		}
		return MESSAGE_SOURCE.getMessage(code, args, defaultMessage, locale);
	}

	/**
	 * 将 schema 解析为导出的 excel的header
	 * 
	 * @param schemaDataTypes 问题数据类型列表
	 * @param mode            项目模式
	 * @return Excel表头列表
	 */
	public static List<String> parseColumns(List<SurveySchema> schemaDataTypes, ProjectModeEnum mode) {
		List<String> result = new ArrayList<>();
		result.add(i18n("schema.column.serialNumber", "序号"));

		// 添加问题标题
		schemaDataTypes.forEach(schemaType -> result.add(schemaType.getTitle()));

		// 考试模式添加分数列
		if (ProjectModeEnum.exam.equals(mode)) {
			result.add(i18n("schema.column.score", "分数"));
		}

		// 添加固定的元数据列
		result.addAll(Arrays.asList(
				getOpenIdColumnName(),
				i18n("schema.column.submitter", "提交人"),
				i18n("schema.column.submitTime", "提交时间"),
				i18n("schema.column.duration", "填写时长"),
				i18n("schema.column.device", "填写设备"),
				i18n("schema.column.os", "操作系统"),
				i18n("schema.column.browser", "浏览器"),
				i18n("schema.column.region", "填写地区"),
				i18n("schema.column.ip", "IP"),
				i18n("schema.column.id", "ID")));

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
	 * 
	 * @param answerInfo 单条答案记录
	 * @param dataTypes  所有的问题 schema
	 * @param index      当前行索引
	 * @param mode       项目模式
	 * @return excel 行记录
	 */
	public static List<Object> parseRowData(AnswerView answerInfo, List<SurveySchema> dataTypes, int index,
			ProjectModeEnum mode) {
		try {
			LinkedHashMap<String, Object> answer = answerInfo.getAnswer();
			List<Object> rowData = new ArrayList<>();
			rowData.add(index);

			// 转换问题答案
			parseQuestionAnswers(answerInfo, dataTypes, rowData);

			// 添加考试分数
			if (ProjectModeEnum.exam.equals(mode)) {
				rowData.add(answerInfo.getExamScore());
			}

			// 添加 openid 信息
			parseOpenIdInfo(answer, rowData);

			// 添加元数据信息
			parseMetaData(answerInfo, rowData);

			// 防止公式注入
			avoidFormulaInjection(rowData);
			return rowData;
		} catch (Exception e) {
			log.error("Failed to parse answer data, answerId: {}, error: {}", answerInfo.getId(), e.getMessage(), e);
			return createErrorRowData(index);
		}
	}

	/**
	 * 解析问题答案
	 */
	private static void parseQuestionAnswers(AnswerView answerInfo, List<SurveySchema> dataTypes,
			List<Object> rowData) {
		LinkedHashMap<String, Object> answer = answerInfo.getAnswer();

		for (SurveySchema schemaType : dataTypes) {
			String questionId = schemaType.getId();
			SurveySchema.QuestionType questionType = schemaType.getType();
			Object valueObj = answer.get(questionId);

			if (valueObj == null) {
				rowData.add(null);
				continue;
			}

			Object parsedValue = parseQuestionValue(schemaType, valueObj, answerInfo);
			rowData.add(parsedValue);
		}
	}

	/**
	 * 根据问题类型解析问题值
	 */
	private static Object parseQuestionValue(SurveySchema schemaType, Object valueObj, AnswerView answerInfo) {
		SurveySchema.QuestionType questionType = schemaType.getType();

		switch (questionType) {
			case Upload:
			case Signature:
				return parseFileQuestionValue(valueObj, answerInfo);
			case Cascader:
				return parseCascaderQuestionValue(schemaType, valueObj);
			case User:
				return parseUserQuestionValue(valueObj, answerInfo);
			case Dept:
				return parseDeptQuestionValue(valueObj, answerInfo);
			case MatrixAuto:
				return parseMatrixAutoQuestionValue(schemaType, valueObj);
			default:
				if (questionType.name().startsWith("Matrix")) {
					return parseMatrixQuestionValue(schemaType, valueObj, questionType);
				} else {
					return parseStandardQuestionValue(schemaType, valueObj);
				}
		}
	}

	/**
	 * 解析文件类型问题值
	 */
	private static String parseFileQuestionValue(Object valueObj, AnswerView answerInfo) {
		Map<?, ?> mapValue = (Map<?, ?>) valueObj;
		return mapValue.values().stream()
				.map(x -> {
					if (x instanceof String) {
						return (String) x;
					}
					List<String> fileIds = (List<String>) x;
					return fileIds.stream()
							.map(id -> answerInfo.getAttachment().stream()
									.filter(attachment -> attachment.getId().equals(id))
									.findFirst()
									.orElse(new FileView())
									.getOriginalName())
							.collect(Collectors.joining(","));
				})
				.collect(Collectors.joining(","));
	}

	/**
	 * 解析层级选择器问题值
	 */
	private static String parseCascaderQuestionValue(SurveySchema schemaType, Object valueObj) {
		Map<?, ?> mapValue = (Map<?, ?>) valueObj;
		List<String> result = new ArrayList<>();
		List<SurveySchema.DataSource> dataSources = schemaType.getDataSource();

		for (SurveySchema child : schemaType.getChildren()) {
			String optionId = child.getId();
			String optionValue = (String) mapValue.get(optionId);
			SurveySchema.DataSource dataSource = dataSources.stream()
					.filter(x -> x.getValue().equals(optionValue))
					.findFirst()
					.orElse(new SurveySchema.DataSource("", "", new ArrayList<>()));
			result.add(dataSource.getLabel());
			dataSources = dataSource.getChildren();
			if (dataSources == null) {
				break;
			}
		}
		return String.join(",", result);
	}

	/**
	 * 解析用户选择问题值
	 */
	private static String parseUserQuestionValue(Object valueObj, AnswerView answerInfo) {
		Map<?, ?> mapValue = (Map<?, ?>) valueObj;
		return mapValue.values().stream()
				.map(x -> {
					List<String> userIds = (List<String>) x;
					return userIds.stream()
							.map(id -> answerInfo.getUsers().stream()
									.filter(user -> user.getUserId().equals(id))
									.findFirst()
									.orElse(new UserInfo())
									.getName())
							.collect(Collectors.joining(","));
				})
				.collect(Collectors.joining(","));
	}

	/**
	 * 解析部门选择问题值
	 */
	private static String parseDeptQuestionValue(Object valueObj, AnswerView answerInfo) {
		Map<?, ?> mapValue = (Map<?, ?>) valueObj;
		return mapValue.values().stream()
				.map(x -> {
					List<String> deptIds = (List<String>) x;
					return deptIds.stream()
							.map(id -> answerInfo.getDepts().stream()
									.filter(dept -> dept.getId().equals(id))
									.findFirst()
									.orElse(new DeptView())
									.getName())
							.collect(Collectors.joining(","));
				})
				.collect(Collectors.joining(","));
	}

	/**
	 * 解析标准问题值（单选、多选、填空等）
	 */
	private static String parseStandardQuestionValue(SurveySchema schemaType, Object valueObj) {
		return schemaType.getChildren().stream()
				.map(optionSchema -> parseOptionValue(schemaType, optionSchema, valueObj))
				.filter(Objects::nonNull)
				.collect(Collectors.joining(","));
	}

	/**
	 * 解析选项值
	 */
	private static String parseOptionValue(SurveySchema schemaType, SurveySchema optionSchema, Object valueObj) {
		Object optionValue = ((Map<?, ?>) valueObj).get(optionSchema.getId());
		if (optionValue == null) {
			return null;
		}

		SurveySchema.DataType dataType = optionSchema.getAttribute().getDataType();

		// 数据字典类型
		if (SurveySchema.DataType.selectDict.equals(dataType)) {
			return parseDictValue(optionValue.toString());
		}

		// 下拉选择类型
		if (SurveySchema.DataType.select.equals(dataType)) {
			return parseSelectValue(optionSchema, optionValue);
		}

		// 布尔类型（单选、多选题选中）
		if (optionValue instanceof Boolean) {
			return trimHtmlTag(optionSchema.getTitle());
		}

		// 横向填空类型
		if (SurveySchema.DataType.horzBlank.equals(dataType)) {
			return getHorzBlankValue(optionSchema, (Map<String, String>) optionValue);
		}

		// 单选或多选填空题
		if (SurveySchema.QuestionType.Radio.equals(schemaType.getType())
				|| SurveySchema.QuestionType.Checkbox.equals(schemaType.getType())) {
			return String.format("%s(%s)", trimHtmlTag(optionSchema.getTitle()), optionValue);
		}

		return optionValue.toString();
	}

	/**
	 * 解析字典值
	 */
	private static String parseDictValue(String value) {
		String[] dictValueAndLabel = value.split(DICT_SEPARATOR, 2);
		if (dictValueAndLabel.length > DICT_LABEL_INDEX) {
			return dictValueAndLabel[DICT_LABEL_INDEX];
		}
		return dictValueAndLabel[0]; // 兼容历史版本
	}

	/**
	 * 解析下拉选择值
	 */
	private static String parseSelectValue(SurveySchema optionSchema, Object optionValue) {
		Optional<SurveySchema.DataSource> findDataSource = optionSchema.getDataSource().stream()
				.filter(x -> x.getValue().equals(optionValue))
				.findFirst();

		return findDataSource.map(SurveySchema.DataSource::getLabel)
				.orElse(optionValue.toString());
	}

	/**
	 * 解析矩阵自动问题值
	 */
	private static String parseMatrixAutoQuestionValue(SurveySchema schemaType, Object valueObj) {
		List<String> result = new ArrayList<>();
		((List<Map<?, ?>>) valueObj).forEach(rowValue -> {
			List<String> matrixRowData = new ArrayList<>();
			rowValue.forEach((optionId, v) -> {
				SurveySchema optionSchema = schemaType.getChildren().stream()
						.filter(option -> option.getId().equals(optionId))
						.findFirst()
						.orElse(null);

				if (optionSchema != null && optionSchema.getDataSource() != null) {
					String label = optionSchema.getDataSource().stream()
							.filter(x -> x.getValue().equals(v))
							.findFirst()
							.orElse(new SurveySchema.DataSource())
							.getLabel();
					matrixRowData.add(label);
				} else if (v instanceof Boolean) {
					String title = trimHtmlTag(schemaType.getChildren().stream()
							.filter(x -> x.getId().equals(optionId))
							.findFirst()
							.orElseGet(SurveySchema::new)
							.getTitle());
					matrixRowData.add(title);
				} else {
					matrixRowData.add(String.valueOf(v));
				}
			});
			result.add(String.format("(%s)", String.join(",", matrixRowData)));
		});
		return String.join(",", result);
	}

	/**
	 * 解析矩阵问题值
	 */
	private static String parseMatrixQuestionValue(SurveySchema schemaType, Object valueObj,
			SurveySchema.QuestionType questionType) {
		List<String> result = new ArrayList<>();
		((Map<?, ?>) valueObj).forEach((optionId, valueMap) -> {
			String title = trimHtmlTag(schemaType.getRow().stream()
					.filter(x -> x.getId().equals(optionId))
					.findFirst()
					.orElseGet(SurveySchema.Row::new)
					.getTitle());

			List<String> valueList = new ArrayList<>();
			((LinkedHashMap<?, ?>) valueMap).forEach((childOptId, val) -> {
				if (val != null) {
					String processedValue = schemaType.getChildren().stream()
							.filter(x -> x.getId().equals(childOptId))
							.findFirst()
							.map(x -> processMatrixCellValue(x, val, questionType))
							.orElse("");
					valueList.add(trimHtmlTag(processedValue));
				} else {
					valueList.add("");
				}
			});
			result.add(String.format("%s:(%s)", title, String.join(",", valueList)));
		});
		return String.join(",", result);
	}

	/**
	 * 处理矩阵单元格值
	 */
	private static String processMatrixCellValue(SurveySchema cellSchema, Object val,
			SurveySchema.QuestionType questionType) {
		if (!CollectionUtils.isEmpty(cellSchema.getDataSource())) {
			Optional<SurveySchema.DataSource> findDataSource = cellSchema.getDataSource().stream()
					.filter(data -> data.getValue().equals(val))
					.findFirst();
			return findDataSource.map(SurveySchema.DataSource::getLabel)
					.orElse(String.valueOf(val));
		} else if (SurveySchema.QuestionType.MatrixFillBlank.equals(questionType)) {
			return String.valueOf(val);
		} else {
			return cellSchema.getTitle();
		}
	}

	/**
	 * 解析 openid 信息
	 */
	private static void parseOpenIdInfo(LinkedHashMap<String, Object> answer, List<Object> rowData) {
		if (answer.containsKey("openid")) {
			rowData.add(answer.get("openid"));
			localOpenId.set(true);
		} else {
			rowData.add("");
		}
	}

	/**
	 * 解析元数据信息
	 */
	private static void parseMetaData(AnswerView answerInfo, List<Object> rowData) {
		// 提交人信息
		rowData.add(answerInfo.getCreateByName());

		// 提交时间
		Format formatter = new SimpleDateFormat(DATE_TIME_FORMAT);
		rowData.add(formatter.format(answerInfo.getCreateAt()));

		// 填写时长
		rowData.add(parseHumanReadableDuration(answerInfo));

		// 客户端信息
		if (answerInfo.getMetaInfo() == null || answerInfo.getMetaInfo().getClientInfo() == null) {
			rowData.addAll(Arrays.asList("", "", "", "", "", ""));
		} else {
			AnswerMetaInfo.ClientInfo clientInfo = answerInfo.getMetaInfo().getClientInfo();
			rowData.addAll(Arrays.asList(
					clientInfo.getDeviceType(),
					clientInfo.getPlatform(),
					clientInfo.getBrowser(),
					clientInfo.getRegion(),
					clientInfo.getRemoteIp(),
					answerInfo.getId()));
		}
	}

	/**
	 * 创建错误行数据
	 */
	private static List<Object> createErrorRowData(int index) {
		List<Object> errorRow = new ArrayList<>();
		errorRow.add(index);
		errorRow.add(i18n("schema.error.rowData", "数据解析错误"));
		return errorRow;
	}

	/**
	 * @param optionSchema 选项 schema
	 * @param optionValue
	 * @return
	 */
	private static String getHorzBlankValue(SurveySchema optionSchema, Map<String, String> optionValue) {
		if (!SurveySchema.DataType.horzBlank.equals(optionSchema.getAttribute().getDataType())) {
			return "";
		}
		List<String> result = new ArrayList<>();
		for (SurveySchema subOption : optionSchema.getChildren()) {
			String subOptionValue = Optional.ofNullable(optionValue.get(subOption.getId())).orElse("");
			// 数据字典
			if (SurveySchema.DataType.selectDict.equals(subOption.getAttribute().getDataType())) {
				String[] dictValueAndLabel = subOptionValue.split("\\|", 2);
				if (dictValueAndLabel.length > 1) {
					result.add(dictValueAndLabel[1]);
				} else {
					// 兼容历史版本
					result.add(dictValueAndLabel[0]);
				}
			}
			// 下拉题
			else if (SurveySchema.DataType.select.equals(subOption.getAttribute().getDataType())) {
				Optional<SurveySchema.DataSource> findDataSource = subOption.getDataSource().stream()
						.filter(x -> x.getValue().equals(subOptionValue)).findFirst();
				if (findDataSource.isPresent()) {
					result.add(findDataSource.get().getLabel());
				} else {
					result.add(subOptionValue);
				}
			} else {
				result.add(subOptionValue);
			}
		}
		return result.stream().collect(Collectors.joining(","));
	}

	/**
	 * 移除HTML标签和实体字符
	 * 更好的方式是使用 Jsoup.parse(html).text(); 但是我不想引入过多的第三方 jar
	 * 
	 * @param string 包含HTML标签的字符串
	 * @return 清理后的纯文本字符串
	 */
	public static String trimHtmlTag(String string) {
		if (StringUtils.isBlank(string)) {
			return "";
		}
		return string.replaceAll(HTML_TAG_REGEX, " ")
				.replaceAll(MULTIPLE_SPACES_REGEX, " ")
				.trim();
	}

	/**
	 * 根据字段权限更新Schema
	 * 
	 * @param fieldPermission 字段权限映射
	 * @param schema          Schema对象
	 */
	public static void updateSchemaByPermission(LinkedHashMap<String, Integer> fieldPermission, SurveySchema schema) {
		if (schema.getChildren() == null || fieldPermission == null || fieldPermission.isEmpty()) {
			return;
		}

		// 移除隐藏的题目，设置只读权限
		schema.getChildren().removeIf(child -> processChildPermission(child, fieldPermission));

		// 递归处理子项
		schema.getChildren().forEach(child -> updateSchemaByPermission(fieldPermission, child));
	}

	/**
	 * 处理子项权限
	 * 
	 * @param child           子项Schema
	 * @param fieldPermission 字段权限映射
	 * @return 是否应该移除该子项
	 */
	private static boolean processChildPermission(SurveySchema child, LinkedHashMap<String, Integer> fieldPermission) {
		Integer permValue = fieldPermission.get(child.getId());
		if (permValue == null) {
			return false;
		}

		// 隐藏题目
		if (permValue.equals(FieldPermissionType.hidden)) {
			return true;
		}

		// 设置为只读
		if (permValue.equals(FieldPermissionType.visible)) {
			ensureAttributeExists(child);
			child.getAttribute().setReadOnly(true);
		}

		return false;
	}

	/**
	 * 确保Schema具有Attribute对象
	 * 
	 * @param schema Schema对象
	 */
	private static void ensureAttributeExists(SurveySchema schema) {
		if (schema.getAttribute() == null) {
			schema.setAttribute(new SurveySchema.Attribute());
		}
	}

	/**
	 * 移除 schema 里面的指定属性值
	 * 
	 * @param schema
	 * @param attributes
	 */
	public static void ignoreAttributes(SurveySchema schema, String... attributes) {
		if (schema.getAttribute() != null) {
			for (String attributeName : attributes) {
				BeanWrapper wrapper = PropertyAccessorFactory.forBeanPropertyAccess(schema.getAttribute());
				wrapper.setPropertyValue(attributeName, null);
			}
		}
		if (schema.getChildren() == null) {
			return;
		}
		schema.getChildren().forEach(child -> {
			ignoreAttributes(child, attributes);
		});
	}

	/**
	 * 根据属性名和属性值找到所有满足条件的子 schema 列表
	 * 
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
	 * 根据属性名查找子 schema 列表
	 * 
	 * @param schema
	 * @param attributeName
	 */
	public static List<SurveySchema> findSchemaHasAttribute(SurveySchema schema, String attributeName) {
		List<SurveySchema> result = new ArrayList<>();
		BeanWrapper wrapper = PropertyAccessorFactory.forBeanPropertyAccess(schema.getAttribute());
		Object attrValue = wrapper.getPropertyValue(attributeName);
		if (attrValue != null) {
			result.add(schema);
		}
		if (schema.getChildren() != null) {
			schema.getChildren().forEach(child -> {
				result.addAll(findSchemaHasAttribute(child, attributeName));
			});
		}
		return result;
	}

	/**
	 * 主要是用于构建 FillBlank 类型的查询表单
	 * 
	 * @param field
	 * @return
	 */
	public static SurveySchema buildFillBlankQuerySchema(LoginFormFieldEnum field) {
		return SurveySchema.builder().id(field.name()).type(SurveySchema.QuestionType.FillBlank).title(field.getTitle())
				.attribute(SurveySchema.Attribute.builder().required(true).build())
				.children(Collections.singletonList(SurveySchema.builder().id(field.name())
						.attribute(SurveySchema.Attribute.builder().dataType(field.getDataType()).build()).build()))
				.build();
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
	 * 
	 * @param parent 问卷 schema
	 * @param child  问题 schema
	 */
	public static void appendChildIfNotExist(SurveySchema parent, SurveySchema child) {
		boolean exists = flatSurveySchema(parent).stream().anyMatch(x -> child.getId().equals(x.getId()));
		if (!exists) {
			parent.getChildren().add(child);
		}
	}

	public static void setQuestionValue(LinkedHashMap<String, Object> answer, String qId, String oId, Object newValue) {
		Map<String, Object> qValue = (Map<String, Object>) answer.computeIfAbsent(qId, k -> new LinkedHashMap<>());
		qValue.put(oId, newValue);
	}

	public static Object getQuestionValue(LinkedHashMap<String, Object> answer, String qId, String oId) {
		Map<String, Object> qValue = (Map<String, Object>) answer.get(qId);
		if (qValue == null) {
			return null;
		}
		if (oId == null) {
			return qValue;
		}
		return qValue.get(oId);
	}

	public static TreeNode SurveySchema2TreeNode(SurveySchema surveySchema) {
		return new TreeNode(surveySchema, null);
	}

	/**
	 * 防止Excel公式注入攻击
	 * 在可能包含公式的字符串前添加单引号
	 * 
	 * @param rowData 行数据列表
	 */
	private static void avoidFormulaInjection(List<Object> rowData) {
		for (int i = 0; i < rowData.size(); i++) {
			Object item = rowData.get(i);
			if (item instanceof String) {
				String str = (String) item;
				if (isFormulaInjectionRisk(str)) {
					rowData.set(i, "'" + str);
				}
			}
		}
	}

	/**
	 * 检查字符串是否有公式注入风险
	 * 
	 * @param str 待检查的字符串
	 * @return 是否有风险
	 */
	private static boolean isFormulaInjectionRisk(String str) {
		if (StringUtils.isBlank(str)) {
			return false;
		}

		return Arrays.stream(FORMULA_INJECTION_PREFIXES)
				.anyMatch(str::startsWith);
	}

	/**
	 * 构建 linkSurvey 的查询条件
	 * 
	 * @param linkSurvey
	 * @param value
	 * @return
	 */
	@SneakyThrows
	public static String buildLinkLikeCondition(SurveySchema.LinkSurvey linkSurvey, Object value) {
		Map<String, Object> optionValue = new HashMap<>();
		optionValue.put(linkSurvey.getLinkOptionId(), value);
		ObjectMapper objectMapper = new ObjectMapper();
		return StringUtils.substringBetween(objectMapper.writeValueAsString(optionValue), "{", "}");
	}

	/**
	 * 解析人类可读的时长格式
	 * 
	 * @param answerInfo 答案信息
	 * @return 格式化的时长字符串，如 "1天2小时30分钟15秒"
	 */
	private static String parseHumanReadableDuration(AnswerView answerInfo) {
		if (answerInfo.getMetaInfo() == null || answerInfo.getMetaInfo().getAnswerInfo() == null) {
			return "";
		}

		try {
			long startTime = answerInfo.getMetaInfo().getAnswerInfo().getStartTime();
			long endTime = answerInfo.getMetaInfo().getAnswerInfo().getEndTime();
			long duration = endTime - startTime;

			if (duration <= 0) {
				return "";
			}

			return formatDuration(duration);
		} catch (Exception e) {
			log.warn("Failed to parse duration: {}", e.getMessage());
			return "";
		}
	}

	/**
	 * 格式化时长
	 * 
	 * @param duration 时长（毫秒）
	 * @return 格式化的时长字符串
	 */
	private static String formatDuration(long duration) {
		List<String> parts = new ArrayList<>();

		long days = duration / MILLISECONDS_PER_DAY;
		long hours = (duration / MILLISECONDS_PER_HOUR) % 24;
		long minutes = (duration / MILLISECONDS_PER_MINUTE) % 60;
		long seconds = (duration / MILLISECONDS_PER_SECOND) % 60;

		if (days > 0) {
			parts.add(i18n("schema.duration.days", "{0}天", days));
		}
		if (hours > 0) {
			parts.add(i18n("schema.duration.hours", "{0}小时", hours));
		}
		if (minutes > 0) {
			parts.add(i18n("schema.duration.minutes", "{0}分钟", minutes));
		}
		if (seconds > 0) {
			parts.add(i18n("schema.duration.seconds", "{0}秒", seconds));
		}

		if (parts.isEmpty()) {
			return "";
		}

		Locale locale = LocaleContextHolder.getLocale();
		boolean isChinese = locale != null && locale.getLanguage().toLowerCase(Locale.ROOT).startsWith("zh");
		String delimiter = isChinese ? "" : " ";
		return String.join(delimiter, parts).trim();
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
			} else {
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

		username("schema.login.username", "用户名", SurveySchema.DataType.text),
		password("schema.login.password", "密码", SurveySchema.DataType.password),
		extraPassword("schema.login.extraPassword", "请输入问卷密码", SurveySchema.DataType.text),
		whitelistName("schema.login.whitelistName", "请先输入名单，再进行填写", SurveySchema.DataType.text);

		private final String titleKey;

		private final String defaultTitle;

		private final SurveySchema.DataType dataType;

		LoginFormFieldEnum(String titleKey, String defaultTitle, SurveySchema.DataType dataType) {
			this.titleKey = titleKey;
			this.defaultTitle = defaultTitle;
			this.dataType = dataType;
		}

		public String getTitle() {
			return i18n(this.titleKey, this.defaultTitle);
		}

		public SurveySchema.DataType getDataType() {
			return dataType;
		}

	}

}
