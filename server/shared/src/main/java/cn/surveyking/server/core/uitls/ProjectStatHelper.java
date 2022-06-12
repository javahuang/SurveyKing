package cn.surveyking.server.core.uitls;

import cn.surveyking.server.domain.dto.AnswerView;
import cn.surveyking.server.domain.dto.PublicStatisticsView;
import cn.surveyking.server.domain.dto.SurveySchema;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author javahuang
 * @date 2022/3/23
 */
public class ProjectStatHelper {

	final SurveySchema schema;

	final List<AnswerView> answers;

	public ProjectStatHelper(SurveySchema schema, List<AnswerView> answers) {
		this.schema = schema;
		this.answers = answers;
	}

	public PublicStatisticsView stat() {
		PublicStatisticsView result = new PublicStatisticsView();
		result.setCount(answers.size());
		Map<String, PublicStatisticsView.QuestionStatistics> questionStatistics = initQuestionStatistics();

		answers.forEach(answerView -> {
			LinkedHashMap<String, Object> answer = answerView.getAnswer();
			questionStatistics.entrySet().forEach(entry -> {
				String questionId = entry.getKey();
				PublicStatisticsView.QuestionStatistics qStatistics = entry.getValue();
				if (answer.containsKey(questionId)) {
					qStatistics.setCount(qStatistics.getCount() + 1);
					qStatistics.getOptionStatistics().forEach(optionStatistics -> {
						String optionId = optionStatistics.getOptionId();
						if (((Map) answer.get(questionId)).containsKey(optionId)) {
							optionStatistics.setCount(optionStatistics.getCount() + 1);
						}
					});
				}
			});
		});
		result.setQuestionStatistics(questionStatistics);
		return result;
	}

	private Map<String, PublicStatisticsView.QuestionStatistics> initQuestionStatistics() {
		List<SurveySchema> flatSurveySchema = SchemaHelper.flatSurveySchema(schema);
		// 只有配置了 statEnabled 属性的题才能进行统计
		List<SurveySchema> canStatSchema = flatSurveySchema.stream()
				.filter(questionSchema -> Boolean.TRUE.equals(questionSchema.getAttribute().getStatEnabled()))
				.collect(Collectors.toList());

		Map<String, PublicStatisticsView.QuestionStatistics> questionStatistics = new LinkedHashMap<>();
		canStatSchema.forEach(item -> {
			questionStatistics.put(item.getId(),
					new PublicStatisticsView.QuestionStatistics(0, item.getChildren().stream().map(optionSchema -> {
						PublicStatisticsView.OptionStatistics optionStatistics = new PublicStatisticsView.OptionStatistics();
						optionStatistics.setOptionId(optionSchema.getId());
						return optionStatistics;
					}).collect(Collectors.toList())));
		});
		return questionStatistics;
	}

}
