package com.innon.education.management.progress.dao.impl;

import com.innon.education.management.plan.repository.dto.ManagementPlanDTO;
import com.innon.education.management.plan.repository.entity.ManagementPlanUserEntity;
import com.innon.education.management.progress.dao.ManagementProgressDAO;
import com.innon.education.management.progress.repository.dto.EducationPlanUserDTO;
import com.innon.education.management.progress.repository.dto.ManagementProgressDTO;
import com.innon.education.management.progress.repository.dto.PlanContentDTO;
import com.innon.education.management.progress.repository.dto.UserAttendanceDTO;
import com.innon.education.management.progress.repository.entity.ManagementProgressEntity;
import com.innon.education.management.progress.repository.entity.UserAttendanceEntity;
import com.innon.education.management.progress.repository.model.ManagementProgress;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.MyBatisSystemException;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Slf4j
public class ManagementProgressDAOImpl implements ManagementProgressDAO {

    @Autowired
    SqlSessionTemplate sqlSession;

    @Override
    public int saveEpuReason(ManagementProgressEntity entity) {
        try {
            return sqlSession.insert("com.innon.education.management.mapper.progress-mapper.saveEpuReason", entity);
        } catch(MyBatisSystemException e) {
            log.error(e.getCause().getMessage());
            throw new MyBatisSystemException(e.getCause());
        }
    }

    @Override
    public List<ManagementProgressDTO> findManagementProgress(ManagementProgressEntity entity) {
        try {
            return sqlSession.selectList("com.innon.education.management.mapper.progress-mapper.findManagementProgress", entity);
        } catch(MyBatisSystemException e) {
            log.error(e.getCause().getMessage());
            throw new MyBatisSystemException(e.getCause());
        }
    }

    @Override
    public List<String> findEducationPlanUserList(int plan_id) {
        try {
            return sqlSession.selectList("com.innon.education.management.mapper.progress-mapper.findEducationPlanUserList", plan_id);
        } catch(MyBatisSystemException e) {
            log.error(e.getCause().getMessage());
            throw new MyBatisSystemException(e.getCause());
        }
    }

    @Override
    public List<String> findPlanContent(int planId) {
        try {
            return sqlSession.selectList("com.innon.education.management.mapper.progress-mapper.findPlanContent", planId);
        } catch(MyBatisSystemException e) {
            log.error(e.getCause().getMessage());
            throw new MyBatisSystemException(e.getCause());
        }
    }

    @Override
    public EducationPlanUserDTO findEducationPlanUserByPlanIdAndUserId(ManagementPlanUserEntity entity) {
        try {
            return sqlSession.selectOne("com.innon.education.management.mapper.progress-mapper.findEducationPlanUserByPlanIdAndUserId", entity);
        } catch(MyBatisSystemException e) {
            log.error(e.getCause().getMessage());
            throw new MyBatisSystemException(e.getCause());
        }
    }

    @Override
    public int saveUserAttendance(UserAttendanceEntity entity) {
        try {
            return sqlSession.insert("com.innon.education.management.mapper.progress-mapper.saveUserAttendance", entity);
        } catch(MyBatisSystemException e) {
            throw new MyBatisSystemException(e.getCause());
        }
    }

    @Override
    public List<PlanContentDTO> findEduContentList(ManagementProgress progress) {
        try {
            return sqlSession.selectList("com.innon.education.management.mapper.progress-mapper.findEduContentInfo", progress);
        } catch(MyBatisSystemException e) {
            log.error(e.getCause().getMessage());
            throw new MyBatisSystemException(e.getCause());
        }
    }

    @Override
    public ManagementPlanDTO findEducationPlanById(int plan_id) {
        try {
            return sqlSession.selectOne("com.innon.education.management.mapper.progress-mapper.findEducationPlanById", plan_id);
        } catch(MyBatisSystemException e) {
            log.error(e.getCause().getMessage());
            throw new MyBatisSystemException(e.getCause());
        }
    }

    @Override
    public UserAttendanceDTO findUserAttendance(UserAttendanceEntity entity) {
        try {
            return sqlSession.selectOne("com.innon.education.management.mapper.progress-mapper.findUserAttendance", entity);
        } catch(MyBatisSystemException e) {
            log.info(e.getCause().toString());
            throw new MyBatisSystemException(e.getCause());
        }
    }

    @Override
    public List<UserAttendanceDTO> planByAttendList(UserAttendanceEntity progress) {
        try {
            return sqlSession.selectList("com.innon.education.management.mapper.progress-mapper.planByAttendList", progress);
        } catch(MyBatisSystemException e) {
            log.error(e.getCause().getMessage());
            throw new MyBatisSystemException(e.getCause());
        }
    }


}
