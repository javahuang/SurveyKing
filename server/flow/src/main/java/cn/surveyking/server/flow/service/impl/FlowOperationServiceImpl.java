package cn.surveyking.server.flow.service.impl;

import cn.surveyking.server.flow.domain.model.FlowOperation;
import cn.surveyking.server.flow.mapper.FlowOperationMapper;
import cn.surveyking.server.flow.service.FlowOperationService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 *
 */
@Service
public class FlowOperationServiceImpl extends ServiceImpl<FlowOperationMapper, FlowOperation>
		implements FlowOperationService {

}
