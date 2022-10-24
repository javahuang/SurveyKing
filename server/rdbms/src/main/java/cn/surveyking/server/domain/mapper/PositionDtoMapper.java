package cn.surveyking.server.domain.mapper;

import cn.surveyking.server.core.base.mapper.BaseModelMapper;
import cn.surveyking.server.domain.dto.PositionRequest;
import cn.surveyking.server.domain.dto.PositionView;
import cn.surveyking.server.domain.model.Position;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * @author javahuang
 * @date 2021/11/2
 */
@Mapper
public interface PositionDtoMapper extends BaseModelMapper<PositionRequest, PositionView, Position> {

}
