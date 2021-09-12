package cn.surveyking.server.core.constant;

/**
 * @author javahuang
 * @date 2021/8/6
 */
public class AppConsts {

	/**
	 * 存储的文件类型
	 */
	public enum StorageType {

		/**
		 * 1背景图片 2顶部图片 3问题图片 4答卷上传附件
		 */
		BACKGROUND_IMAGE(1), HEADER_IMAGE(2), QUESTION_IMAGE(3), ANSWER_ATTACHMENT(4);

		private int type;

		StorageType(int type) {
			this.type = type;
		}

		public int getType() {
			return type;
		}

		public void setType(int type) {
			this.type = type;
		}

	}

	/**
	 * 支持的图片类型
	 */
	public static final String[] SUPPORT_IMAGE_TYPE = { "JPG", "JPEG", "PNG", "GIF", "BMP", "WBMP" };

}
