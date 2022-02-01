package cn.surveyking.server.domain.mapper;

import cn.surveyking.server.domain.dto.DeptRequest;
import cn.surveyking.server.domain.dto.DeptView;
import cn.surveyking.server.domain.model.Dept;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * @author javahuang
 * @date 2021/11/2
 */
@Mapper
public interface DeptDtoMapper {

	Dept fromRequest(DeptRequest request);

	DeptView toView(Dept dept);

	List<DeptView> toView(List<Dept> depts);

}
