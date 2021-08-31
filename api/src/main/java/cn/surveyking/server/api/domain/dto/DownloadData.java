package cn.surveyking.server.api.domain.dto;

import lombok.Data;

import java.util.List;

/**
 * @author javahuang
 * @date 2021/8/10
 */
@Data
public class DownloadData {

	private String fileName;

	private List<String> headerNames;

	private List<List<Object>> rows;

}
