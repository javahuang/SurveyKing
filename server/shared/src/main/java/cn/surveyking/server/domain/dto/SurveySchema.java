package cn.surveyking.server.domain.dto;

import lombok.Data;

import java.util.EnumSet;
import java.util.List;

/**
 * @author javahuang
 * @date 2021/8/10
 */
@Data
public class SurveySchema implements Cloneable {

	private String id;

	private String title;

	private String description;

	private QuestionType type;

	private Attribute attribute;

	private List<DataSource> dataSource;

	private List<SurveySchema> children;

	private List<Row> row;

	public enum QuestionType {

		FillBlank, Textarea, MultipleBlank, Signature, Score, Radio, Checkbox, Select, Cascader, Upload, MatrixAuto, MatrixRadio, MatrixCheckbox, MatrixFillBlank, MatrixScore, Survey, QuestionSet, Pagination, Remark, SplitLine, Option, User, Dept, Nps, HorzBlank;

		// 分为数据类型和空类型
		public static EnumSet<QuestionType> dataType() {
			return EnumSet.of(FillBlank, Textarea, MultipleBlank, Signature, Score, Radio, Checkbox, Select, Cascader,
					Upload, MatrixAuto, MatrixRadio, MatrixCheckbox, MatrixFillBlank, MatrixScore, User, Dept, Nps, HorzBlank);
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

		/**
		 * none/visible/hidden
		 */
		private String display;

		private Boolean hidden;

		private Integer width;

		private String dataType;

		private Boolean required;

		private Boolean defaultChecked;

		private Integer rows;

		/**
		 * 范围强制校验
		 */
		private String scope;

		/**
		 * 范围强校验提示信息
		 */
		private String scopeDesc;

		/**
		 * 范围软校验，超出范围会警告，但是依然可以提交
		 */
		private String softScope;

		private String softScopeDesc;

		private Boolean readOnly;

		private String suffix;

		/**
		 * 文字长度限制 [1,2] [,5]
		 */
		private String textLimit;

		/**
		 * 多选答案数量限制 [1, 2] [,3]
		 */
		private String answerLimit;

		private Boolean finish;

		private String finishRule;

		private String calculate;

		private Integer currentPage;

		private Integer totalPage;

		private String submitButton;

		private String visibleRule;

		private Integer numericScale;

		/**
		 * 必填校验规则
		 */
		private String requiredRule;

		/**
		 * 背景图
		 */
		private String backgroundImage;

		/**
		 * 问卷头背景图
		 */
		private String headerImage;

		/**
		 * 上传文件类型后缀，多个文件格式逗号分割
		 */
		private String fileAccept;

		/**
		 * 最大上传文件数量
		 */
		private Integer maxFileCount;

		/**
		 * 单个上传文件大小限制
		 */
		private Double maxFileSize;

		/**
		 * 打分题显示样式
		 */
		private String scoreStyle;

		/**
		 * 文本替换规则
		 */
		private String replaceTextRule;

		/**
		 * Textarea 高度自适应，[4,6] 最低4行，最高6行
		 */
		private String autoSize;

		/**
		 * 只允许使用拍照上传
		 */
		private Boolean cameraOnly;

		/**
		 * 选项排成几列
		 */
		private Integer columns;

		/**
		 * 填空题的题干
		 */
		private String content;

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
			return new SurveySchema();
		}
	}

}
