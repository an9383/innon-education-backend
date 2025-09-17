package com.innon.education.management.evaluation.dao;

import com.innon.education.management.evaluation.repository.dto.ManagementEvalDTO;
import com.innon.education.management.evaluation.repository.dto.QuestionInfoDTO;
import com.innon.education.management.evaluation.repository.dto.QuestionItemDTO;
import com.innon.education.management.evaluation.repository.entity.ManagementEvalEntity;
import com.innon.education.management.evaluation.repository.model.ManagementEval;

import java.util.List;

public interface ManagementEvalDAO {
    int savePuqAnswer(ManagementEvalEntity entity);
    int updatePuqAnswer(ManagementEvalEntity entity);
    List<QuestionInfoDTO> findQuestionInfo(ManagementEval eval);
    List<QuestionItemDTO> findQuestionItem(int qb_q_id);
    List<ManagementEvalDTO> findWrongAnswerList(ManagementEval eval);
    List<QuestionInfoDTO> findTempQuestionList(ManagementEval eval);
    String findUserPassYn(ManagementEval eval);
    List<QuestionInfoDTO> findEduQuestionInfo(ManagementEval eval);
}
