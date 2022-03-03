package cn.surveyking.server.flow.mapper;

import cn.surveyking.server.flow.domain.dto.UpdateFlowOperationUserRequest;
import cn.surveyking.server.flow.domain.model.FlowOperation;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Entity cn.surveyking.server.flow.domain.FlowOperation
 */
public interface FlowOperationMapper extends BaseMapper<FlowOperation> {

	void updateOperationLatest(@Param("instanceId") String instanceId);

	void updateOperationUserLatest(UpdateFlowOperationUserRequest request);

}
