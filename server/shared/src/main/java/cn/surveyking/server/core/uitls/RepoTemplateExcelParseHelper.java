package cn.surveyking.server.core.uitls;

import cn.surveyking.server.core.constant.ErrorCode;
import cn.surveyking.server.core.constant.ProjectModeEnum;
import cn.surveyking.server.core.exception.ErrorCodeException;
import cn.surveyking.server.domain.dto.SurveySchema;
import cn.surveyking.server.domain.dto.TemplateRequest;
import lombok.SneakyThrows;
import org.dhatim.fastexcel.reader.ReadableWorkbook;
import org.dhatim.fastexcel.reader.Row;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * @author javahuang
 * @date 2022/5/31
 */
public class RepoTemplateExcelParseHelper {

	private final MultipartFile templateFile;

	private List<TemplateRequest> result = new ArrayList<>();

	private Set<String> ids = new HashSet<>();

	/**
	 * 标题与索引的映射关系
	 */
	private Map<String, Integer> name2index = new HashMap<>();

	public RepoTemplateExcelParseHelper(MultipartFile templateFile) {
		this.templateFile = templateFile;
	}

	@SneakyThrows
	public List<TemplateRequest> parse() {
		try (InputStream is = templateFile.getInputStream(); ReadableWorkbook wb = new ReadableWorkbook(is)) {
			wb.getSheets().forEach(sheet -> {
				String sheetName = sheet.getName();
				try (Stream<Row> rows = sheet.openStream()) {
					rows.forEach(r -> {
						int rowNum = r.getRowNum();
						if (rowNum == 1) {
							// 第一行作为行头
							parseHeader(r);
						}
						else {
							switch (sheetName) {
							case "单选题":
							case "判断题":
								parseRadio(r);
								break;
							case "多选题":
								parseCheckbox(r);
								break;
							case "填空题":
								parseFillBlank(r);
								break;
							case "简答题":
								parseTextarea(r);
								break;
							}
						}
					});
				}
				catch (Exception e) {
					e.printStackTrace();
					throw new ErrorCodeException(ErrorCode.FileParseError, e);
				}
			});
		}
		return result;
	}

	@SneakyThrows
	private void parseRadio(Row r) {
		SurveySchema schema = row2schema(r);
		schema.setType(SurveySchema.QuestionType.Radio);
		schema.getAttribute().setExamAnswerMode(SurveySchema.ExamScoreMode.onlyOne);

		TemplateRequest templateRequest = TemplateRequest.builder().questionType(SurveySchema.QuestionType.Radio)
				.mode(ProjectModeEnum.exam).name(schema.getTitle()).template(schema).build();
		result.add(templateRequest);
	}

	@SneakyThrows
	private void parseCheckbox(Row r) {
		SurveySchema schema = row2schema(r);
		schema.setType(SurveySchema.QuestionType.Checkbox);
		schema.getAttribute().setExamAnswerMode(SurveySchema.ExamScoreMode.selectAll);

		TemplateRequest templateRequest = TemplateRequest.builder().questionType(SurveySchema.QuestionType.Checkbox)
				.mode(ProjectModeEnum.exam).name(schema.getTitle()).template(schema).build();
		result.add(templateRequest);
	}

	@SneakyThrows
	private void parseFillBlank(Row r) {
		SurveySchema schema = row2schema(r);
		SurveySchema.QuestionType questionType = schema.getChildren().size() > 1
				? SurveySchema.QuestionType.MultipleBlank : SurveySchema.QuestionType.FillBlank;
		schema.setType(questionType);
		schema.getAttribute().setExamAnswerMode(SurveySchema.ExamScoreMode.selectAll);
		schema.getAttribute().setExamMatchRule(SurveySchema.ExamMatchRule.completeSame);

		TemplateRequest templateRequest = TemplateRequest.builder().questionType(questionType)
				.mode(ProjectModeEnum.exam).name(schema.getTitle()).template(schema).build();
		result.add(templateRequest);
	}

	@SneakyThrows
	private void parseTextarea(Row r) {
		SurveySchema schema = row2schema(r);
		schema.setType(SurveySchema.QuestionType.Textarea);

		schema.getAttribute().setExamAnswerMode(SurveySchema.ExamScoreMode.selectAll);
		schema.getAttribute().setExamMatchRule(SurveySchema.ExamMatchRule.completeSame);
		String correctAnswer = getCellAsString(r, "examCorrectAnswer");
		// 简答题没有设置选项，只有答案列，所以需要手动的添加 option
		schema.setChildren(Collections.singletonList(SurveySchema.builder().id(generateId())
				.attribute(SurveySchema.Attribute.builder().examCorrectAnswer(correctAnswer).build()).build()));
		;
		TemplateRequest templateRequest = TemplateRequest.builder().questionType(SurveySchema.QuestionType.Textarea)
				.mode(ProjectModeEnum.exam).name(schema.getTitle()).template(schema).build();
		result.add(templateRequest);
	}

	/**
	 * @param currentIndex 当前选项的序号，从 0 开始
	 * @param rightAnswer 正确答案序号 A/B/C/D
	 * @return
	 */
	private boolean currentChecked(int currentIndex, String rightAnswer) {
		if (isBlank(rightAnswer)) {
			return false;
		}
		for (int i = 0; i < rightAnswer.length(); i++) {
			if (rightAnswer.toUpperCase().codePointAt(i) - 65 == currentIndex) {
				return true;
			}
		}
		return false;
	}

	private SurveySchema row2schema(Row r) {
		String title = getCellAsString(r, "title");
		String correctAnswer = getCellAsString(r, "examCorrectAnswer");
		Double examScore = getCellAsDouble(r, "examScore");
		String tags = getCellAsString(r, "tags");

		List<SurveySchema> optionSchemaList = r.stream()
				.filter(cell -> cell != null && name2index.get("minOptionIndex") != null
						&& cell.getColumnIndex() >= name2index.get("minOptionIndex")
						&& cell.getColumnIndex() <= name2index.get("maxOptionIndex") && isNotBlank(cell.getText()))
				.map(x -> {
					String optionTitle = x.getText();
					String examCorrectAnswer = x.getText();
					String optionId = generateId();
					// 填空题选项存的是答案，单选多选存的是选项的文本
					if (name2index.get("isChoice") != null) {
						// 处理选项
						int columnIndex = x.getColumnIndex() - name2index.get("minOptionIndex");
						boolean currentCorrect = currentChecked(columnIndex, correctAnswer);
						examCorrectAnswer = currentCorrect ? optionId : null;
					}
					else {
						optionTitle = null;
					}

					SurveySchema optionSchema = SurveySchema.builder().id(optionId).title(optionTitle)
							.attribute(SurveySchema.Attribute.builder().examCorrectAnswer(examCorrectAnswer).build())
							.build();

					return optionSchema;
				}).collect(Collectors.toList());

		return SurveySchema.builder().title(title).attribute(SurveySchema.Attribute.builder()
				// 解析
				.examAnalysis(getCellAsString(r, "examAnalysis"))
				// 分值
				.examScore(examScore).build()).id(generateId())
				.tags(tags != null ? Arrays.asList(tags.split("\\s|,")) : null).children(optionSchemaList).build();

	}

	/**
	 * 根据标题解析出来各个选项的顺序
	 * @param header
	 */
	private void parseHeader(Row header) {
		name2index = new HashMap<>();
		List<Integer> optionCellIndexes = new ArrayList<>();
		header.stream().forEach(cell -> {
			int columnIndex = cell.getColumnIndex();
			String cellText = cell.getText();
			switch (cellText) {
			case "题干":
				name2index.put("title", columnIndex);
				break;
			case "解析":
				name2index.put("examAnalysis", columnIndex);
				break;
			case "分数":
			case "单空分数":
				name2index.put("examScore", columnIndex);
				break;
			case "答案":
				name2index.put("examCorrectAnswer", columnIndex);
				break;
			case "标签":
				name2index.put("tags", columnIndex);
				break;
			default:
				if (isNotBlank(cellText) && (cellText.startsWith("选项") || cellText.startsWith("空"))) {
					// 其他列都解析为答案列
					optionCellIndexes.add(columnIndex);
					if (cellText.startsWith("选项")) {
						name2index.put("isChoice", -1);
					}
					else {
						name2index.put("isFillBlank", -1);
					}
				}
				break;
			}
		});
		if (optionCellIndexes.size() > 0) {
			name2index.put("minOptionIndex", optionCellIndexes.stream().mapToInt(v -> v).min().getAsInt());
			name2index.put("maxOptionIndex", optionCellIndexes.stream().mapToInt(v -> v).max().getAsInt());
		}
	}

	/**
	 * 避免列索引为空时获取值报错
	 * @param row
	 * @param cellName
	 * @return
	 */
	private String getCellAsString(Row row, String cellName) {
		if (name2index.get(cellName) == null) {
			return null;
		}
		return row.getCellAsString(name2index.get(cellName)).orElse(null);
	}

	private Double getCellAsDouble(Row row, String cellName) {
		if (name2index.get(cellName) == null) {
			return null;
		}
		String value = row.getCellText(name2index.get(cellName));
		if (isBlank(value)) {
			return null;
		}
		return Double.parseDouble(value);
	}

	private String generateId() {
		return NanoIdUtils.randomNanoId(4, ids);
	}

}
