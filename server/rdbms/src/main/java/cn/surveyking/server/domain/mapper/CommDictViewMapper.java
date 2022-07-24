package cn.surveyking.server.domain.mapper;

import cn.surveyking.server.core.base.mapper.BaseModelMapper;
import cn.surveyking.server.domain.dto.CommDictRequest;
import cn.surveyking.server.domain.dto.CommDictView;
import cn.surveyking.server.domain.model.CommDict;
import org.mapstruct.Mapper;

/**
 * @author javahuang
 * @date 2022/7/19
 */
@Mapper
public interface CommDictViewMapper extends BaseModelMapper<CommDictRequest, CommDictView, CommDict> {

}
