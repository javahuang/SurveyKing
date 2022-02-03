package cn.surveyking.server.domain.dto;

import lombok.Data;

import java.util.List;

/**
 * @author javahuang
 * @date 2022/2/2
 */
@Data
public class FileQuery {

	Integer type;

	List<String> ids;

}
