package cn.surveyking.server.mapper;

import cn.surveyking.server.domain.dto.RepoQuestionTypeTotalView;
import cn.surveyking.server.domain.dto.TemplateTagTotalView;
import cn.surveyking.server.domain.model.Repo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @Entity cn.surveyking.server.domain.model.Repo
 */
public interface RepoMapper extends BaseMapper<Repo> {

	/**
	 * 题库中问题按照标签分类统计数量
	 * @param repoId
	 * @return
	 */
	@Select("SELECT a.`name`, count(*) total FROM t_tag a " + "LEFT JOIN t_template b ON a.entity_id = b.id "
			+ "LEFT JOIN t_repo d ON b.repo_id = d.id " + "WHERE d.id = #{repoId} GROUP by a.`name`")
	List<TemplateTagTotalView> selectRepoTemplateTags(String repoId);

	/**
	 * 题库中按照问题分类统计数量
	 * @param repoId
	 * @return
	 */
	@Select("SELECT a.question_type, COUNT(*) total FROM t_template a "
			+ "WHERE a.repo_id = #{repoId} GROUP BY a.question_type")
	List<RepoQuestionTypeTotalView> selectRepoQuestionTypes(String repoId);

}
