package com.innon.education.management.evaluation.dao.impl;

import com.innon.education.management.evaluation.dao.ManagementEvalDAO;
import com.innon.education.management.evaluation.repository.dto.ManagementEvalDTO;
import com.innon.education.management.evaluation.repository.dto.QuestionInfoDTO;
import com.innon.education.management.evaluation.repository.dto.QuestionItemDTO;
import com.innon.education.management.evaluation.repository.entity.ManagementEvalEntity;
import com.innon.education.management.evaluation.repository.model.ManagementEval;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.MyBatisSystemException;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Slf4j
public class ManagementEvalDAOImpl implements ManagementEvalDAO {

    @Autowired
    SqlSessionTemplate sqlSession;

    @Override
    public int savePuqAnswer(ManagementEvalEntity entity) {
        try {
            return sqlSession.insert("com.innon.education.management.mapper.eval-mapper.savePuqAnswer", entity);
        } catch(MyBatisSystemException e) {
            log.error(e.getCause().getMessage());
            throw new MyBatisSystemException(e.getCause());
        }
    }

    @Override
    public int updatePuqAnswer(ManagementEvalEntity entity) {
        try {
            return sqlSession.update("com.innon.education.management.mapper.eval-mapper.updatePuqAnswer", entity);
        } catch(MyBatisSystemException e) {
            log.error(e.getCause().getMessage());
            throw new MyBatisSystemException(e.getCause());
        }
    }

    @Override
    public List<QuestionInfoDTO> findQuestionInfo(ManagementEval eval) {
        try {
            return sqlSession.selectList("com.innon.education.management.mapper.eval-mapper.findQuestionInfo", eval);
        } catch(MyBatisSystemException e) {
            log.error(e.getCause().getMessage());
            throw new MyBatisSystemException(e.getCause());
        }
    }

    @Override
    public List<QuestionItemDTO> findQuestionItem(int qb_q_id) {
        try {
            return sqlSession.selectList("com.innon.education.management.mapper.eval-mapper.findQuestionItem", qb_q_id);
        } catch(MyBatisSystemException e) {
            log.error(e.getCause().getMessage());
            throw new MyBatisSystemException(e.getCause());
        }
    }

    @Override
    public List<ManagementEvalDTO> findWrongAnswerList(ManagementEval eval) {
        try {
            return sqlSession.selectList("com.innon.education.management.mapper.eval-mapper.findWrongAnswerList", eval);
        } catch(MyBatisSystemException e) {
            log.error(e.getCause().getMessage());
            throw new MyBatisSystemException(e.getCause());
        }
    }

    @Override
    public List<QuestionInfoDTO> findTempQuestionList(ManagementEval eval) {
        try {
            return sqlSession.selectList("com.innon.education.management.mapper.eval-mapper.findTempQuestionList", eval);
        } catch(MyBatisSystemException e) {
            log.error(e.getCause().getMessage());
            throw new MyBatisSystemException(e.getCause());
        }
    }

    @Override
    public String findUserPassYn(ManagementEval eval) {
        try {
            return sqlSession.selectOne("com.innon.education.management.mapper.eval-mapper.findUserPassYn", eval);
        } catch(MyBatisSystemException e) {
            log.error(e.getCause().getMessage());
            throw new MyBatisSystemException(e.getCause());
        }
    }

    @Override
    public List<QuestionInfoDTO> findEduQuestionInfo(ManagementEval eval) {
        try {
            return sqlSession.selectList("com.innon.education.management.mapper.eval-mapper.findEduQuestionInfo", eval);
        } catch(MyBatisSystemException e) {
            log.error(e.getCause().getMessage());
            throw new MyBatisSystemException(e.getCause());
        }
    }
}
