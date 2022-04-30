package cn.surveyking.server.domain.mapper;

import cn.surveyking.server.core.base.mapper.BaseModelMapper;
import cn.surveyking.server.core.uitls.SecurityContextUtils;
import cn.surveyking.server.domain.dto.TemplateRequest;
import cn.surveyking.server.domain.dto.TemplateView;
import cn.surveyking.server.domain.model.Template;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

/**
 * @author javahuang
 * @date 2021/9/23
 */
@Mapper
public interface TemplateViewMapper extends BaseModelMapper<TemplateRequest, TemplateView, Template> {

	Template fromRequest(TemplateRequest request);

	@Mapping(source = "item", target = "owner", qualifiedByName = "ownerType")
	TemplateView toView(Template item);

	@Named("ownerType")
	default boolean itemToOwner(Template item) {
		return SecurityContextUtils.getUserId().equals(item.getCreateBy());
	}

}
