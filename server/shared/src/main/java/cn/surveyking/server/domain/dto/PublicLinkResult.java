package cn.surveyking.server.domain.dto;

import lombok.Data;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author javahuang
 * @date 2022/11/05
 */
@Data
public class PublicLinkResult {

	LinkedHashMap<String, Map<String, Object>> answer;

}
