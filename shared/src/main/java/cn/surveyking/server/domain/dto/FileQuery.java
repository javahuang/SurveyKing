package cn.surveyking.server.domain.dto;

import cn.surveyking.server.core.constant.AppConsts;
import lombok.Data;

import java.util.List;

/**
 * @author javahuang
 * @date 2022/2/2
 */
@Data
public class FileQuery {

	AppConsts.StorageType storageType;

	List<String> ids;

}
