package cn.surveyking.server.domain.dto;

import lombok.Data;

/**
 * @author javahuang
 * @date 2021/11/2
 */
@Data
public class PositionView {

	private String id;

	private String name;

	private String code;

	/** 是否虚拟岗 */
	private Boolean isVirtual = false;

	/** 数据权限类型 self:本人 */
	private String dataPermissionType;

}
