package cn.surveyking.server.domain.mapper;

import cn.surveyking.server.domain.dto.OrgRequest;
import cn.surveyking.server.domain.dto.OrgView;
import cn.surveyking.server.domain.model.Org;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * @author javahuang
 * @date 2021/11/2
 */
@Mapper
public interface OrgDtoMapper {

	Org fromRequest(OrgRequest request);

	OrgView toView(Org org);

	List<OrgView> toView(List<Org> orgs);

}
