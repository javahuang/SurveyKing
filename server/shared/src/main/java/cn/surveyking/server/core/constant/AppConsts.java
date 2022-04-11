package cn.surveyking.server.core.constant;

/**
 * @author javahuang
 * @date 2021/8/6
 */
public class AppConsts {

	/**
	 * cookie 保存的 token
	 */
	public static final String COOKIE_TOKEN_NAME = "sk-token";

	/**
	 *
	 */
	public static final String COOKIE_LIMIT_NAME = "sk-limit";

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
	 * 存储的文件类型 TODO: 文件添加权限控制
	 */
	public interface StorageType {

		/** 背景图片 */
		int BACKGROUND_IMAGE = 1;

		/** 顶部图片 */
		int HEADER_IMAGE = 2;

		/** 问题图片 */
		int QUESTION_IMAGE = 3;

		/** 答卷附件 */
		int ANSWER_ATTACHMENT = 4;

		/** 问题模板预览图 */
		int TEMPLATE_PREVIEW_IMAGE = 5;

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

	public interface USER_STATUS {

		/** 正常用户 */
		int VALID = 1;

		/** 失活用户 */
		int INVALID = 0;

	}

	/**
	 * 岗位对应的数据权限
	 */
	public enum DataPermissionTypeEnum {

		/** 本人、本人及下属、本部门、本部门及下属部门、全部 */
		SELF, SELF_AND_SUB, DEPT, DEPT_AND_SUB, ALL;

	}

	/**
	 * 下载文件方式
	 */
	public enum DispositionTypeEnum {

		/**
		 * 预览
		 */
		inline,
		/**
		 * 附件方式下载
		 */
		attachment

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
