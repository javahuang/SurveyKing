package cn.surveyking.server.mapper;

import cn.surveyking.server.domain.model.Project;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @author javahuang
 * @date 2021/8/6
 */
public interface ProjectMapper extends BaseMapper<Project> {

	@Select("select * from t_project where is_deleted = 1 and create_by = #{userId}")
	@ResultMap("mybatis-plus_Project")
	List<Project> selectLogicDeleted(String userId);

	@Delete({ "<script>", "delete", "FROM t_project", "WHERE id IN",
			"<foreach item='item' index='index' collection='ids'", "open='(' separator=',' close=')'>", "#{item}",
			"</foreach>", "</script>" })
	void batchDestroy(List<String> ids);

	@Update({ "<script>", "update", "t_project set is_deleted = 0", "WHERE id IN",
			"<foreach item='item' index='index' collection='ids'", "open='(' separator=',' close=')'>", "#{item}",
			"</foreach>", "</script>" })
	void restoreProject(List<String> ids);

}
