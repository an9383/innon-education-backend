package com.innon.education.educurrent.dao.impl;

import com.innon.education.educurrent.dao.EduCurrentDAO;
import com.innon.education.educurrent.repository.dto.EduCurrentUserDTO;
import com.innon.education.management.plan.repository.dto.ManagementPlanDTO;
import com.innon.education.management.plan.repository.entity.PlanQmsEntity;
import com.innon.education.management.plan.repository.model.PlanQms;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.MyBatisSystemException;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@Slf4j
public class EduCurrentDAOImpl implements EduCurrentDAO {

    @Autowired
    SqlSessionTemplate sqlSession;

    @Override
    public int findEduResultByPlanId(int plan_id) {
        try {
            return sqlSession.selectOne("com.innon.education.edu-current.mapper.edu-current-mapper.findEduResultByPlanId", plan_id);
        } catch(MyBatisSystemException e) {
            log.error(e.getCause().getMessage());
            throw new MyBatisSystemException(e.getCause());
        }
    }

    @Override
    public int findEduProgressByPlanId(int plan_id) {
        try {
            return sqlSession.selectOne("com.innon.education.edu-current.mapper.edu-current-mapper.findEduProgressByPlanId", plan_id);
        } catch(MyBatisSystemException e) {
            log.error(e.getCause().getMessage());
            throw new MyBatisSystemException(e.getCause());
        }
    }

    @Override
    public int findEduEvaluationByPlanId(int plan_id) {
        try {
            return sqlSession.selectOne("com.innon.education.edu-current.mapper.edu-current-mapper.findEduEvaluationByPlanId", plan_id);
        } catch(MyBatisSystemException e) {
            log.error(e.getCause().getMessage());
            throw new MyBatisSystemException(e.getCause());
        }
    }

    @Override
    public int findEduPlanByPlanId(int plan_id) {
        try {
            return sqlSession.selectOne("com.innon.education.edu-current.mapper.edu-current-mapper.findEduPlanByPlanId", plan_id);
        } catch(MyBatisSystemException e) {
            log.error(e.getCause().getMessage());
            throw new MyBatisSystemException(e.getCause());
        }
    }

    @Override
    public int findAllEduPlan(Map<String, Object> searchParam) {
        try {
            return sqlSession.selectOne("com.innon.education.edu-current.mapper.edu-current-mapper.findAllEduPlan", searchParam);
        } catch(MyBatisSystemException e) {
            log.error(e.getCause().getMessage());
            throw new MyBatisSystemException(e.getCause());
        }
    }

    @Override
    public int findAllEduProgress(Map<String, Object> searchParam) {
        try {
            return sqlSession.selectOne("com.innon.education.edu-current.mapper.edu-current-mapper.findAllEduProgress", searchParam);
        } catch(MyBatisSystemException e) {
            log.error(e.getCause().getMessage());
            throw new MyBatisSystemException(e.getCause());
        }
    }

    @Override
    public List<ManagementPlanDTO> findEduPlanByUserId(String qms_user_id) {
        try {
            return sqlSession.selectList("com.innon.education.edu-current.mapper.edu-current-mapper.findEduPlanByUserId", qms_user_id);
        } catch(MyBatisSystemException e) {
            log.error(e.getCause().getMessage());
            throw new MyBatisSystemException(e.getCause());
        }
    }

    @Override
    public List<ManagementPlanDTO> findEduProgressByUserId(String qms_user_id) {
        try {
            return sqlSession.selectList("com.innon.education.edu-current.mapper.edu-current-mapper.findEduProgressByUserId", qms_user_id);
        } catch(MyBatisSystemException e) {
            log.error(e.getCause().getMessage());
            throw new MyBatisSystemException(e.getCause());
        }
    }

    @Override
    public List<ManagementPlanDTO> findEduEvaluationByUserId(String qms_user_id) {
        try {
            return sqlSession.selectList("com.innon.education.edu-current.mapper.edu-current-mapper.findEduEvaluationByUserId", qms_user_id);
        } catch(MyBatisSystemException e) {
            log.error(e.getCause().getMessage());
            throw new MyBatisSystemException(e.getCause());
        }
    }

    @Override
    public List<ManagementPlanDTO> findEduResultByUserId(String qms_user_id) {
        try {
            return sqlSession.selectList("com.innon.education.edu-current.mapper.edu-current-mapper.findEduResultByUserId", qms_user_id);
        } catch(MyBatisSystemException e) {
            log.error(e.getCause().getMessage());
            throw new MyBatisSystemException(e.getCause());
        }
    }

    @Override
    public List<ManagementPlanDTO> findEduQmsByEduUserId(String edu_user_id) {
        try {
            return sqlSession.selectList("com.innon.education.edu-current.mapper.edu-current-mapper.findEduQmsByEduUserId", edu_user_id);
        } catch(MyBatisSystemException e) {
            log.error(e.getCause().getMessage());
            throw new MyBatisSystemException(e.getCause());
        }
    }

    @Override
    public List<EduCurrentUserDTO> findEduCurrentUserList(String qms_user_id) {
        try {
            return sqlSession.selectList("com.innon.education.edu-current.mapper.edu-current-mapper.findEduCurrentUserList", qms_user_id);
        } catch(MyBatisSystemException e) {
            log.error(e.getCause().getMessage());
            throw new MyBatisSystemException(e.getCause());
        }
    }
}
