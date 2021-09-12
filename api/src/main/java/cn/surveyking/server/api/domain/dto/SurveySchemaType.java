package cn.surveyking.server.api.domain.dto;

import lombok.Data;

import java.util.EnumSet;
import java.util.List;

/**
 * @author javahuang
 * @date 2021/8/10
 */
@Data
public class SurveySchemaType implements Cloneable {

	private String id;

	private String title;

	private String description;

	private QuestionType type;

	private Attribute attribute;

	private List<DataSource> dataSource;

	private List<SurveySchemaType> children;

	private List<Row> row;

	public enum QuestionType {

		FillBlank, MultipleBlank, Single, Multi, Select, Cascader, MatrixAuto, MatrixSingle, MatrixMulti, MatrixFillBlank, MatrixScore, Survey, QuestionSet, Pagination, Remark, SplitLine, Option;

		// 分为数据类型和空类型
		public static EnumSet<QuestionType> dataType() {
			return EnumSet.of(FillBlank, MultipleBlank, Single, Multi, Select, Cascader, MatrixAuto, MatrixSingle,
					MatrixMulti, MatrixFillBlank, MatrixScore);
		}

		public static EnumSet<QuestionType> voidType() {
			return EnumSet.of(Survey, QuestionSet, Pagination, Remark, SplitLine, Option);
		}

	}

	@Data
	public static class Row {

		private String id;

		private String title;

	}

	@Data
	public static class Attribute {

		private Boolean hidden;

		private Integer width;

		private String dataType;

		private Boolean required;

		private Boolean defaultChecked;

		private Integer rows;

		private String scope;

		private String scoreDesc;

		private String softScope;

		private Boolean readOnly;

		private String suffix;

		private Boolean finish;

		private String finishRule;

		private String calculate;

		private Integer currentPage;

		private Integer totalPage;

		private String submitButton;

		private String visibleRule;

		private Integer numericScale;

		private String requiredRule;

		private String backgroundImage;

		private String headerImage;

	}

	@Data
	public static class DataSource {

		private String label;

		private String value;

		private List<DataSource> children;

	}

	@Override
	public Object clone() {
		try {
			return super.clone();
		}
		catch (CloneNotSupportedException e) {
			e.printStackTrace();
			return new SurveySchemaType();
		}
	}

}
