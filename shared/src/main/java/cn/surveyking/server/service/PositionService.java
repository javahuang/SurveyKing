package cn.surveyking.server.service;

import cn.surveyking.server.core.common.PaginationResponse;
import cn.surveyking.server.domain.dto.PositionQuery;
import cn.surveyking.server.domain.dto.PositionRequest;
import cn.surveyking.server.domain.dto.PositionView;

/**
 * @author javahuang
 * @date 2021/11/2
 */
public interface PositionService {

	PaginationResponse<PositionView> listPosition(PositionQuery query);

	void addPosition(PositionRequest request);

	void updatePosition(PositionRequest request);

	void deletePosition(String id);

}
