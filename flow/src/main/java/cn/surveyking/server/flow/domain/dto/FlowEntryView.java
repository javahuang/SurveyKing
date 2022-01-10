package cn.surveyking.server.flow.domain.dto;

import lombok.Data;

import java.util.List;

/**
 * @author javahuang
 * @date 2022/1/5
 */
@Data
public class FlowEntryView {

	private String id;

	private String projectId;

	private String bpmnXml;

	private String icon;

	private List<FlowEntryNodeView> nodes;

}
