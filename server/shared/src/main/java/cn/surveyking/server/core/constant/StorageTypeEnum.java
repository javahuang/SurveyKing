package cn.surveyking.server.core.constant;

/**
 * 文件类型
 * @author javahuang
 * @date 2022/5/5
 */
public enum StorageTypeEnum {

	/** 背景图片 */
	BACKGROUND_IMAGE(1),

	/** 顶部图片 */
	HEADER_IMAGE(2),

	/** 问题图片 */
	QUESTION_IMAGE(3),

	/** 答卷附件 */
	ANSWER_ATTACHMENT(4),

	/** 问题模板预览图 */
	TEMPLATE_PREVIEW_IMAGE(5);

	private int type;

	StorageTypeEnum(int type) {
		this.type = type;
	}

	public int getType() {
		return type;
	}

}
