package cn.surveyking.server.api.domain.mapper;

import cn.surveyking.server.api.domain.dto.TemplateView;
import cn.surveyking.server.api.domain.model.Template;
import cn.surveyking.server.core.uitls.SecurityContextUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author javahuang
 * @date 2021/9/23
 */
@Mapper
public interface TemplateViewMapper {

	TemplateViewMapper INSTANCE = Mappers.getMapper(TemplateViewMapper.class);

	@Mapping(source = "item", target = "owner", qualifiedByName = "ownerType")
	TemplateView toView(Template item);

	List<TemplateView> toViewList(List<Template> items);

	@Named("ownerType")
	default boolean itemToOwner(Template item) {
		return SecurityContextUtils.getUsername().equals(item.getCreateBy());
	}

}
