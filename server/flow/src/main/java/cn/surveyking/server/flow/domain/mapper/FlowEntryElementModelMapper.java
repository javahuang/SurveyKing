package cn.surveyking.server.flow.domain.mapper;

import cn.surveyking.server.core.base.mapper.BaseModelMapper;
import cn.surveyking.server.flow.domain.dto.FlowEntryNodeRequest;
import cn.surveyking.server.flow.domain.dto.FlowEntryNodeView;
import cn.surveyking.server.flow.domain.model.FlowEntryNode;
import org.mapstruct.Mapper;

/**
 * @author javahuang
 * @date 2022/1/6
 */
@Mapper(componentModel = "spring")
public interface FlowEntryElementModelMapper
		extends BaseModelMapper<FlowEntryNodeRequest, FlowEntryNodeView, FlowEntryNode> {

}
