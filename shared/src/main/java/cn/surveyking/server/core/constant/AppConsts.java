package cn.surveyking.server.core.constant;

/**
 * @author javahuang
 * @date 2021/8/6
 */
public class AppConsts {

	/**
	 * 支持的图片类型
	 */
	public static final String[] SUPPORT_IMAGE_TYPE = { "JPG", "JPEG", "PNG", "GIF", "BMP", "WBMP" };

	/**
	 * 逻辑删除列名
	 */
	public static final String COLUMN_IS_DELETED = "is_deleted";

	public static final String ROLE_ADMIN = "ROLE_admin";

	/**
	 * 匿名用户 ID
	 */
	public static final String ANONYMOUS_USER_ID = "guest";

	/**
	 * 当前机构 ID 变量名
	 */
	public static final String VARIABLE_CURRENT_ORG_ID = "currentOrgId";

	/**
	 * 父机构 ID 变量名
	 */
	public static final String VARIABLE_PARENT_ORG_ID = "parentOrgId";

	/**
	 * 存储的文件类型
	 */
	public enum StorageType {

		/**
		 * 1.背景图片 2.顶部图片 3.问题图片 4.答卷附件 5.问卷模板预览图
		 */
		BACKGROUND_IMAGE(1), HEADER_IMAGE(2), QUESTION_IMAGE(3), ANSWER_ATTACHMENT(4), TEMPLATE_PREVIEW_IMAGE(5);

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
	 * 字典编码 - 权限类型
	 */
	public enum DICTCODE_PERMISSION_TYPE {

		MENU, OPERATION, OTHER

	}

	/**
	 * 前端权限类型
	 */
	public enum RESOURCE_PERMISSION_DISPLAY_TYPE {

		MODULE, MENU, PERMISSION

	}

	public enum AUTH_TYPE {

		PWD

	}

	public enum USER_TYPE {

		/** 系统用户 */
		SysUser

	}

	public enum USER_STATUS {

		/** 正常用户 */
		VALID(1);

		private int status;

		USER_STATUS(int type) {
			this.status = type;
		}

		public int getStatus() {
			return status;
		}

		public void setStatus(int status) {
			this.status = status;
		}

	}

	/**
	 * 岗位对应的数据权限
	 */
	public enum DATA_PERMISSION_TYPE {

		/** 本人、本人及下属、本部门、本部门及下属部门、全部 */
		SELF, SELF_AND_SUB, DEPT, DEPT_AND_SUB, ALL;

	}

	/**
	 * 仪表盘类型
	 */
	public interface DashboardType {

		/** 首页 */
		int HOMEPAGE = 1;

		/** 项目概要页面 */
		int PROJECT_OVERVIEW = 2;

	}

	public interface ProjectPartnerType {

		/**
		 * 所有者
		 */
		Integer OWNER = 1;

		/**
		 * 协作者
		 */
		Integer COLLABORATOR = 2;

	}

	public interface PermType {

		String PROJECT = "project";

	}

}
