package cn.surveyking.server.domain.mapper;

import cn.surveyking.server.domain.dto.AnswerRequest;
import cn.surveyking.server.domain.dto.AnswerView;
import cn.surveyking.server.domain.model.Answer;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * @author javahuang
 * @date 2021/10/6
 */
@Mapper
public interface AnswerViewMapper {

	AnswerView toAnswerView(Answer answer);

	List<AnswerView> toAnswerView(List<Answer> answerList);

	Answer fromRequest(AnswerRequest request);

}
