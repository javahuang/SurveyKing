package cn.surveyking.server.core.constant;

/**
 * 项目参与者
 * @author javahuang
 * @date 2022/6/11
 */
public enum ProjectPartnerTypeEnum {

	/**
	 * 所有者
	 */
	OWNER(1),
	/**
	 * 协作者
	 */
	COLLABORATOR(2),
	/**
	 * 答卷人(系统用户)
	 */
	RESPONDENT_SYS_USER(3),
	/**
	 * 答卷人(外部导入用户)
	 */
	RESPONDENT_IMP_USER(4);

	private int type;

	ProjectPartnerTypeEnum(int type) {
		this.type = type;
	}

	public int getType() {
		return type;
	}

}
