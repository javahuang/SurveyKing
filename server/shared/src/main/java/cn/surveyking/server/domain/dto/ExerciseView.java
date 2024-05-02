package cn.surveyking.server.domain.dto;

import cn.surveyking.server.core.constant.ExamExerciseTypeEnum;
import lombok.Data;

import java.util.Date;

/**
 * @author javahuang
 * @date 2024/4/6
 */
@Data
public class ExerciseView {

    private String id;

    private String projectId;

    private String projectName;

    /**
     * 0：暂存，1：已完成
     */
    private Integer tempSave;

    /**
     * 答题进度
     */
    private Long percent;

    private Date createAt;

    private ExamExerciseTypeEnum examPracticeType;

    private String repoId;

    private String answerId;
}
