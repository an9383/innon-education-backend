package com.innon.education.management.result.dao.impl;

import com.innon.education.management.plan.repository.entity.PlanQmsEntity;
import com.innon.education.management.plan.repository.model.PlanQms;
import com.innon.education.management.result.dao.ManagementResultDAO;
import com.innon.education.management.result.repository.dto.ManagementResultDTO;
import com.innon.education.management.result.repository.dto.ReEducationDTO;
import com.innon.education.management.result.repository.entity.ManagementResultEntity;
import com.innon.education.management.result.repository.entity.ReEducationEntity;
import com.innon.education.management.result.repository.model.ManagementResult;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.MyBatisSystemException;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
@Slf4j
@Repository
public class ManagementResultDAOImpl implements ManagementResultDAO {

    @Autowired
    SqlSessionTemplate sqlSession;

    @Override
    public int saveEducationResult(ManagementResultEntity entity) {
        try {
            return sqlSession.insert("com.innon.education.management.mapper.result-mapper.saveEducationResult", entity);
        } catch(MyBatisSystemException e) {
            throw new MyBatisSystemException(e.getCause());
        }
    }



    @Override
    public List<ManagementResultDTO> findResultPlanUserList(int plan_id) {
        try {
            return sqlSession.selectList("com.innon.education.management.mapper.result-mapper.findResultPlanUserList", plan_id);
        } catch(MyBatisSystemException e) {
            throw new MyBatisSystemException(e.getCause());
        }
    }

    @Override
    public List<PlanQms> findPlanQmsList(PlanQmsEntity planQmsEntity) {
        return sqlSession.selectList("com.innon.education.management.mapper.result-mapper.findPlanQmsList", planQmsEntity);
    }

    @Override
    public int saveQmsRes(PlanQms planQms) {
        return sqlSession.insert("com.innon.education.management.mapper.result-mapper.saveQmsRes", planQms);
    }

    @Override
    public PlanQms findPlanQms(int planId) {
        return sqlSession.selectOne("com.innon.education.management.mapper.result-mapper.findPlanQms",planId);
    }

    @Override
    public int saveReduceStudent(ReEducationEntity reEducationEntity) {
        return sqlSession.insert("com.innon.education.management.mapper.result-mapper.saveReduceStudent", reEducationEntity);
    }

    @Override
    public ManagementResultDTO managementResultDetail(ManagementResult result) {
        return sqlSession.selectOne("com.innon.education.management.mapper.result-mapper.findEducationResult",result);
    }

    @Override
    public int updateEducationResult(ManagementResultEntity resultEntity) {
        try {
            return sqlSession.update("com.innon.education.management.mapper.result-mapper.updateEducationResult", resultEntity);
        } catch(MyBatisSystemException e) {
            log.error(e.getCause().getMessage());
            throw new MyBatisSystemException(e);
        }
    }

    @Override
    public List<ReEducationDTO> reduceStudentList(int planId) {
        return sqlSession.selectList("com.innon.education.management.mapper.result-mapper.reduceStudentList",planId);
    }
}
