package cn.surveyking.server.core.uitls;

import cn.surveyking.server.domain.dto.SurveySchema;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

/**
 * 用于计算问卷的分值
 *
 * @author javahuang
 * @date 2022/4/7
 */
@Slf4j
public class AnswerScoreEvaluator {

	// 问卷 schema
	private final SurveySchema schema;

	// 当前答案
	private final LinkedHashMap answer;

	List<Evaluator> evaluatorList = new ArrayList<>();

	LinkedHashMap<String, Double> questionScore = new LinkedHashMap<>();

	public AnswerScoreEvaluator(SurveySchema schema, LinkedHashMap answer) {
		this.schema = schema;
		this.answer = answer;
		evaluatorList.add(new OnlyOneEvaluator());
		evaluatorList.add(new SelectAllEvaluator());
		evaluatorList.add(new SelectCorrectEvaluator());
		evaluatorList.add(new SelectEvaluator());
	}

	/**
	 * 计算总分
	 * @return
	 */
	public Double eval() {
		return doEval(schema);
	}

	/**
	 * 获取问题的分值
	 * @return
	 */
	public LinkedHashMap<String, Double> getQuestionScore() {
		return this.questionScore;
	}

	private Double doEval(SurveySchema qSchema) {
		double score = evaluatorList.stream().filter(x -> x.support(qSchema)).findFirst().orElse(new DefaultEvaluator())
				.eval(qSchema);
		if (SurveySchema.QuestionType.dataType().contains(qSchema.getType())) {
			questionScore.put(qSchema.getId(), score);
		}
		if (qSchema.getChildren() != null) {
			for (SurveySchema child : qSchema.getChildren()) {
				score += doEval(child);
			}
		}
		return score;
	}

	interface Evaluator {

		/**
		 * @param qSchema 当前问题的 schema
		 * @return 当前问题的分值
		 */
		Double eval(SurveySchema qSchema);

		/**
		 * @param qSchema 当前问题的 schema
		 * @return 是否能使用当前 evaluator 计算分值
		 */
		boolean support(SurveySchema qSchema);

	}

	class OnlyOneEvaluator implements Evaluator {

		/**
		 * 只有一个正确答案
		 * @param qSchema 当前问题的 schema
		 * @return
		 */
		@Override
		public Double eval(SurveySchema qSchema) {
			String qId = qSchema.getId();
			Double qScore = qSchema.getAttribute().getExamScore();
			Map qValue = (Map) answer.get(qId);
			for (SurveySchema option : qSchema.getChildren()) {
				// 只有一个正确答案
				if (option.getAttribute().getExamCorrectAnswer() != null && qValue.containsKey(option.getId())
						&& qValue.values().size() == 1) {
					return qScore;
				}
			}
			return .0;
		}

		@Override
		public boolean support(SurveySchema qSchema) {
			if (qSchema.getAttribute() == null) {
				return false;
			}
			return SurveySchema.ExamScoreMode.onlyOne.equals(qSchema.getAttribute().getExamAnswerMode())
					&& qSchema.getAttribute().getExamScore() != null && answer.get(qSchema.getId()) != null;
		}

	}

	class SelectAllEvaluator implements Evaluator {

		/**
		 * 全部答对才得分
		 * @param qSchema 当前问题的 schema
		 * @return
		 */
		@Override
		public Double eval(SurveySchema qSchema) {
			String qId = qSchema.getId();
			Double qScore = qSchema.getAttribute().getExamScore();
			Map qValue = (Map) answer.get(qId);
			for (SurveySchema option : qSchema.getChildren()) {
				// 选了非正确答案,直接0分
				if (option.getAttribute().getExamCorrectAnswer() == null && qValue.containsKey(option.getId())) {
					return .0;
				}
				// 正确答案没有选，直接0分
				if (option.getAttribute().getExamCorrectAnswer() != null && !qValue.containsKey(option.getId())) {
					return .0;
				}
				// 正确答案不匹配，直接 0 分
				Object optionValue = qValue.get(option.getId());
				if (option.getAttribute().getExamCorrectAnswer() != null
						&& !optionValueMatchCorrectAnswer(qSchema, option.getId(), optionValue)) {
					return .0;
				}
			}
			return qScore;
		}

		@Override
		public boolean support(SurveySchema qSchema) {
			if (qSchema.getAttribute() == null) {
				return false;
			}
			return SurveySchema.ExamScoreMode.selectAll.equals(qSchema.getAttribute().getExamAnswerMode())
					&& qSchema.getAttribute().getExamScore() != null && answer.get(qSchema.getId()) != null;
		}

	}

	class SelectCorrectEvaluator implements Evaluator {

		/**
		 * 按正确答案计分，选择题答错不得分、填空题按正确答案算分
		 * @param qSchema 当前问题的 schema
		 * @return
		 */
		@Override
		public Double eval(SurveySchema qSchema) {
			String qId = qSchema.getId();
			Map qValue = (Map) answer.get(qId);
			double sum = 0;
			for (SurveySchema option : qSchema.getChildren()) {
				// 选择题选了非正确答案,直接0分
				if (!isBlank(qSchema) && option.getAttribute().getExamCorrectAnswer() == null
						&& qValue.containsKey(option.getId())) {
					return .0;
				}
				// 按正确答案算分
				Object optionValue = qValue.get(option.getId());
				if (optionValueMatchCorrectAnswer(qSchema, option.getId(), optionValue)) {
					sum += option.getAttribute().getExamScore();
				}
			}
			return sum;
		}

		@Override
		public boolean support(SurveySchema qSchema) {
			if (qSchema.getAttribute() == null) {
				return false;
			}
			return SurveySchema.ExamScoreMode.selectCorrect.equals(qSchema.getAttribute().getExamAnswerMode())
					&& qSchema.getAttribute().getExamScore() != null && answer.get(qSchema.getId()) != null;
		}

	}

	class SelectEvaluator implements Evaluator {

		/**
		 * 按选中的答案算分
		 * @param qSchema 当前问题的 schema
		 * @return
		 */
		@Override
		public Double eval(SurveySchema qSchema) {
			String qId = qSchema.getId();
			Map qValue = (Map) answer.get(qId);
			double sum = 0;
			for (SurveySchema option : qSchema.getChildren()) {
				// 按正确答案算分
				Object optionValue = qValue.get(option.getId());
				if (optionValueMatchCorrectAnswer(qSchema, option.getId(), optionValue)) {
					Double score = option.getAttribute().getExamScore();
					if (score == null) {
						score = .0;
						log.warn("问卷{}问题{}选项{}未设置分值", schema.getId(), qSchema.getId(), option.getId());
					}
					sum += score;
				}
			}
			return sum;
		}

		@Override
		public boolean support(SurveySchema qSchema) {
			if (qSchema.getAttribute() == null) {
				return false;
			}
			return SurveySchema.ExamScoreMode.select.equals(qSchema.getAttribute().getExamAnswerMode())
					&& qSchema.getAttribute().getExamScore() != null && answer.get(qSchema.getId()) != null;
		}

	}

	class DefaultEvaluator implements Evaluator {

		@Override
		public Double eval(SurveySchema qSchema) {
			return 0.0;
		}

		@Override
		public boolean support(SurveySchema qSchema) {
			return true;
		}

	}

	/**
	 * @param qSchema 问题 schema
	 * @param optionId 选项 id
	 * @param optionValue 选项的值
	 * @return 该选项答案是否匹配正确答案
	 */
	private boolean optionValueMatchCorrectAnswer(SurveySchema qSchema, String optionId, Object optionValue) {
		if (optionValue == null) {
			return false;
		}
		// 选择题选项有值，则直接匹配回答正确
		if (!isBlank(qSchema)) {
			return true;
		}
		SurveySchema.ExamMatchRule matchRule = qSchema.getAttribute().getExamMatchRule();
		SurveySchema optionSchema = qSchema.getChildren().stream().filter(x -> x.getId().equals(optionId)).findFirst()
				.get();
		String correctAnswer = optionSchema.getAttribute().getExamCorrectAnswer();
		if (correctAnswer == null) {
			log.warn("问卷{}问题{}选项{}未设置正确答案", schema.getId(), qSchema.getId(), optionSchema.getId());
			return false;
		}
		// 多个正确答案可以按照分号隔开，只要有一个正确即可
		for (String item : correctAnswer.split(":")) {
			if (SurveySchema.ExamMatchRule.completeSame.equals(matchRule) && item.equals(optionValue.toString())) {
				return true;
			}
			if (SurveySchema.ExamMatchRule.contain.equals(matchRule) && item.contains(optionValue.toString())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 选择题selectCorrect选择了错误的，整题不会得分；填空题selectCorrect只计算正确的分数
	 * @param qSchema 问题schema
	 * @return 是否是填空题
	 */
	private boolean isBlank(SurveySchema qSchema) {
		if (SurveySchema.QuestionType.Radio.equals(qSchema.getType())
				|| SurveySchema.QuestionType.Checkbox.equals(qSchema.getType())
				|| SurveySchema.QuestionType.Select.equals(qSchema.getType())) {
			return false;
		}
		return true;
	}

}
