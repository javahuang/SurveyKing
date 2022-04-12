package cn.surveyking.server.mapper;

import cn.surveyking.server.domain.model.Answer;
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
public interface AnswerMapper extends BaseMapper<Answer> {

	@Select("select * from t_answer where is_deleted = 1 and project_id = #{projectId}")
	@ResultMap("mybatis-plus_Answer")
	List<Answer> selectLogicDeleted(String projectId);

	@Delete({ "<script>", "delete", "FROM t_answer", "WHERE id IN",
			"<foreach item='item' index='index' collection='ids'", "open='(' separator=',' close=')'>", "#{item}",
			"</foreach>", "</script>" })
	int batchPhysicalDelete(List<String> ids);

	@Update({ "<script>", "update", "t_answer set is_deleted = 0", "WHERE id IN",
			"<foreach item='item' index='index' collection='ids'", "open='(' separator=',' close=')'>", "#{item}",
			"</foreach>", "</script>" })
	void restoreAnswer(List<String> ids);

}
