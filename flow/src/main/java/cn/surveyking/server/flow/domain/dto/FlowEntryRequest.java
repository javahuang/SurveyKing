package cn.surveyking.server.flow.domain.dto;

import cn.surveyking.server.flow.domain.model.FlowEntryNode;
import lombok.Data;

import java.util.List;

/**
 * @author javahuang
 * @date 2022/1/5
 */
@Data
public class FlowEntryRequest {

	/** xml 的 processId */
	private String bpmnXml;

	private String projectId;

	private List<FlowEntryNode> nodes;

}
