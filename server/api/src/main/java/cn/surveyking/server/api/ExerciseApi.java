package cn.surveyking.server.api;

import cn.surveyking.server.core.common.PaginationResponse;
import cn.surveyking.server.domain.dto.ExerciseView;
import cn.surveyking.server.domain.dto.HistoryExerciseQuery;
import cn.surveyking.server.service.AnswerService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("${api.prefix}/exercise")
@RequiredArgsConstructor
public class ExerciseApi {

    private final AnswerService answerService;

    /**
     * 历史练习列表。
     *
     * @param query 查询参数。
     * @return 当前答案。
     */
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('exercise:list')")
    public PaginationResponse<ExerciseView> historyExercise(@Valid HistoryExerciseQuery query) {
        return answerService.historyExercise(query);
    }

}
