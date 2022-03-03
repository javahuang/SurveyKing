package cn.surveyking.server.mapper;

import cn.surveyking.server.domain.model.ProjectPartner;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @Entity cn.surveyking.server.domain.model.ProjectPartner
 */
public interface ProjectPartnerMapper extends BaseMapper<ProjectPartner> {

	@Select("select distinct project_id from t_project_partner where user_id = #{userId}")
	List<String> getProjectPerms(String userId);

}
