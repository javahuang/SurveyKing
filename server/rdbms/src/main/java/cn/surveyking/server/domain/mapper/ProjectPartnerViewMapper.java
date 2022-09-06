package cn.surveyking.server.domain.mapper;

import cn.surveyking.server.core.base.mapper.BaseModelMapper;
import cn.surveyking.server.domain.dto.ProjectPartnerRequest;
import cn.surveyking.server.domain.dto.ProjectPartnerView;
import cn.surveyking.server.domain.model.ProjectPartner;
import org.mapstruct.Mapper;

/**
 * @author javahuang
 * @date 2022/9/6
 */
@Mapper
public interface ProjectPartnerViewMapper
		extends BaseModelMapper<ProjectPartnerRequest, ProjectPartnerView, ProjectPartner> {

}
