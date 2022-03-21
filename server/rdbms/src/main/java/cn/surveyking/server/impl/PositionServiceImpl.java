package cn.surveyking.server.impl;

import cn.surveyking.server.core.common.PaginationResponse;
import cn.surveyking.server.domain.dto.PositionQuery;
import cn.surveyking.server.domain.dto.PositionRequest;
import cn.surveyking.server.domain.dto.PositionView;
import cn.surveyking.server.domain.dto.SelectPositionRequest;
import cn.surveyking.server.domain.mapper.PositionDtoMapper;
import cn.surveyking.server.domain.model.Position;
import cn.surveyking.server.mapper.PositionMapper;
import cn.surveyking.server.service.BaseService;
import cn.surveyking.server.service.PositionService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.baomidou.mybatisplus.core.toolkit.StringUtils.isNotBlank;

/**
 * @author javahuang
 * @date 2021/11/2
 */
@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
public class PositionServiceImpl extends BaseService<PositionMapper, Position> implements PositionService {

	private final PositionDtoMapper positionDtoMapper;

	@Override
	public PaginationResponse<PositionView> listPosition(PositionQuery query) {
		Page<Position> page = pageByQuery(query,
				Wrappers.<Position>lambdaQuery().like(isNotBlank(query.getName()), Position::getName, query.getName()));
		PaginationResponse<PositionView> result = new PaginationResponse<>(page.getTotal(),
				positionDtoMapper.toView(page.getRecords()));
		return result;
	}

	@Override
	public void addPosition(PositionRequest request) {
		save(positionDtoMapper.fromRequest(request));
	}

	@Override
	public void updatePosition(PositionRequest request) {
		updateById(positionDtoMapper.fromRequest(request));
	}

	@Override
	public void deletePosition(String id) {
		removeById(id);
	}

	@Override
	public List<PositionView> selectPositions(SelectPositionRequest request) {
		return positionDtoMapper.toView(list());
	}

}
