package cn.surveyking.server.domain.mapper;

import cn.surveyking.server.core.base.mapper.BaseModelMapper;
import cn.surveyking.server.domain.dto.CommDictItemRequest;
import cn.surveyking.server.domain.dto.CommDictItemView;
import cn.surveyking.server.domain.model.CommDictItem;
import org.mapstruct.Mapper;

/**
 * @author javahuang
 * @date 2022/7/19
 */
@Mapper
public interface CommDictItemViewMapper extends BaseModelMapper<CommDictItemRequest, CommDictItemView, CommDictItem> {

}
