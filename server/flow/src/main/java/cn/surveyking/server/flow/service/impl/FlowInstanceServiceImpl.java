package cn.surveyking.server.flow.service.impl;

import cn.surveyking.server.flow.domain.model.FlowInstance;
import cn.surveyking.server.flow.mapper.FlowInstanceMapper;
import cn.surveyking.server.flow.service.FlowInstanceService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 *
 */
@Service
public class FlowInstanceServiceImpl extends ServiceImpl<FlowInstanceMapper, FlowInstance>
		implements FlowInstanceService {

}
