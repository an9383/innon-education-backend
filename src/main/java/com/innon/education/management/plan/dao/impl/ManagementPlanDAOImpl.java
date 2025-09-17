package com.innon.education.management.plan.dao.impl;

import com.innon.education.library.document.repasitory.dto.DocumentDTO;
import com.innon.education.library.document.repasitory.dto.PlanDocument;
import com.innon.education.management.plan.dao.ManagementPlanDAO;
import com.innon.education.management.plan.repository.dto.EducationPlanUserInfoDTO;
import com.innon.education.management.plan.repository.dto.ManagementPlanDTO;
import com.innon.education.management.plan.repository.dto.ManagementPlanUserDTO;
import com.innon.education.management.plan.repository.dto.UserEduCurrentDTO;
import com.innon.education.management.plan.repository.entity.*;
import com.innon.education.management.plan.repository.model.EducationPlanContent;
import com.innon.education.management.plan.repository.model.ManagementPlan;
import com.innon.education.management.plan.repository.model.QuestionInfo;
import com.innon.education.management.plan.repository.model.UserEduCurrent;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.MyBatisSystemException;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@Slf4j
public class ManagementPlanDAOImpl implements ManagementPlanDAO {

    @Autowired
    SqlSessionTemplate sqlSession;

    @Override
    public int savePlan(ManagementPlan plan) {
        try {
            return sqlSession.insert("com.innon.education.management.mapper.plan-mapper.savePlan", plan);
        } catch(MyBatisSystemException e) {
            log.error(e.getCause().getMessage());
            throw new MyBatisSystemException(e.getCause());
        }
    }

    @Override
    public int savePlanUser(ManagementPlanUserEntity planUserEntity) {
        try {
            return sqlSession.insert("com.innon.education.management.mapper.plan-mapper.savePlanUser", planUserEntity);
        } catch(MyBatisSystemException e) {
            log.error(e.getCause().getMessage());
            throw new MyBatisSystemException(e.getCause());
        }
    }

    @Override
    public int savePlanContent(HelpUrlEntity helpUrlEntity) {
        try {
            return sqlSession.insert("com.innon.education.management.mapper.plan-mapper.savePlanContent", helpUrlEntity);
        } catch(MyBatisSystemException e) {
            log.error(e.getCause().getMessage());
            throw new MyBatisSystemException(e.getCause());
        }
    }

    @Override
    public int saveQuestionInfo(QuestionInfoEntity questionInfoEntity) {
        try {
            return sqlSession.insert("com.innon.education.management.mapper.plan-mapper.saveQuestionInfo", questionInfoEntity);
        } catch(MyBatisSystemException e) {
            log.error(e.getCause().getMessage());
            throw new MyBatisSystemException(e.getCause());
        }
    }

    @Override
    public int saveQuestionInfoDetail(QuestionInfoDetailEntity questionInfoDetailEntity) {
        try {
            return sqlSession.insert("com.innon.education.management.mapper.plan-mapper.saveQuestionInfoDetail", questionInfoDetailEntity);
        } catch(MyBatisSystemException e) {
            log.error(e.getCause().getMessage());
            throw new MyBatisSystemException(e.getCause());
        }
    }

    @Override
    public List<ManagementPlanDTO> educationPlanList(ManagementPlanEntity entity) {
        try {
            return sqlSession.selectList("com.innon.education.management.mapper.plan-mapper.educationPlanList", entity);
        } catch(MyBatisSystemException e) {
            log.error(e.getCause().toString());
            throw new MyBatisSystemException(e.getCause());
        }
    }

    @Override
    public List<ManagementPlanUserDTO> findManagementPlanUser(int planId) {
        try {
            return sqlSession.selectList("com.innon.education.management.mapper.plan-mapper.findManagementPlanUser", planId);
        } catch(MyBatisSystemException e) {
            log.error(e.getCause().getMessage());
            throw new MyBatisSystemException(e.getCause());
        }
    }

    @Override
    public List<Map<String, Object>> findManagementHelpUrl(int planId) {
        try {
            return sqlSession.selectList("com.innon.education.management.mapper.plan-mapper.findManagementHelpUrl", planId);
        } catch(MyBatisSystemException e) {
            log.error(e.getCause().getMessage());
            throw new MyBatisSystemException(e.getCause());
        }
    }

    @Override
    public List<Map<String, Object>> findQuestionInfo(int planId) {
        try {
            return sqlSession.selectList("com.innon.education.management.mapper.plan-mapper.findQuestionInfo", planId);
        } catch(MyBatisSystemException e) {
            log.error(e.getCause().getMessage());
            throw new MyBatisSystemException(e.getCause());
        }
    }

    @Override
    public List<Map<String, Object>> findQuestionDetail(Object questionInfoId) {
        try {
            return sqlSession.selectList("com.innon.education.management.mapper.plan-mapper.findQuestionDetail", questionInfoId);
        } catch(MyBatisSystemException e) {
            log.error(e.getCause().getMessage());
            throw new MyBatisSystemException(e.getCause());
        }
    }

    @Override
    public int updateEducationPlanStatus(ManagementPlanEntity planEntity) {
        try {
            return sqlSession.update("com.innon.education.management.mapper.plan-mapper.updateEducationPlanStatus", planEntity);
        } catch(MyBatisSystemException e) {
            log.error(e.getCause().getMessage());
            throw new MyBatisSystemException(e.getCause());
        }
    }

    @Override
    public int updateEducationPlan(ManagementPlanEntity planEntity) {
        try {
            return sqlSession.update("com.innon.education.management.mapper.plan-mapper.updateEducationPlan", planEntity);
        } catch(MyBatisSystemException e) {
            log.error(e.getCause().getMessage());
            throw new MyBatisSystemException(e.getCause());
        }
    }

    @Override
    public int saveWorkManagement(WorkManagementEntity workEntity) {
        try {
            return sqlSession.insert("com.innon.education.management.mapper.plan-mapper.saveWorkManagement", workEntity);
        } catch(MyBatisSystemException e) {
            log.error(e.getCause().getMessage());
            throw new MyBatisSystemException(e.getCause());
        }
    }

    @Override
    //미사용 됨
    public int saveQmsRes(PlanQmsEntity qmsEntity) {
        try {
            return sqlSession.insert("com.innon.education.management.mapper.plan-mapper.saveQmsRes", qmsEntity);
        } catch(MyBatisSystemException e) {
            log.error(e.getCause().getMessage());
            throw new MyBatisSystemException(e.getCause());
        }
    }

    @Override
    public List<UserEduCurrentDTO> findUserEduCurrentList(UserEduCurrent userEduCurrent) {
        try {
            return sqlSession.selectList("com.innon.education.management.mapper.plan-mapper.findUserEduCurrentList", userEduCurrent);
        } catch(MyBatisSystemException e) {
            log.error(e.getCause().getMessage());
            throw new MyBatisSystemException(e.getCause());
        }
    }

    @Override
    public int savePlanDocument(PlanDocument paramDocument) {
        try {
            return sqlSession.insert("com.innon.education.management.mapper.plan-mapper.savePlanDocument", paramDocument);
        } catch(MyBatisSystemException e) {
            log.error(e.getCause().getMessage());
            throw new MyBatisSystemException(e.getCause());
        }
    }



    @Override
    public List<DocumentDTO> findManagementPlanDocument(int planId) {
        try {
            return sqlSession.selectList("com.innon.education.management.mapper.plan-mapper.findManagementPlanDocument", planId);
        } catch(MyBatisSystemException e) {
            log.error(e.getCause().getMessage());
            throw new MyBatisSystemException(e.getCause());
        }
    }

    @Override
    public int updatePlan(ManagementPlan plan) {
        try {
            return sqlSession.update("com.innon.education.management.mapper.plan-mapper.updateEducationPlan", plan);
        } catch(MyBatisSystemException e) {
            log.error(e.getCause().toString());
            throw new MyBatisSystemException(e.getCause());
        }
    }

    @Override
    public int updatePlanUser(ManagementPlanUserEntity plan) {
        try {
            return sqlSession.delete("com.innon.education.management.mapper.plan-mapper.updatePlanUser", plan);
        } catch(MyBatisSystemException e) {
            log.error(e.getCause().getMessage());
            throw new MyBatisSystemException(e.getCause());
        }
    }

    @Override
    public int updatePlanContent(EducationPlanContent plan) {
        try {
            return sqlSession.delete("com.innon.education.management.mapper.plan-mapper.updatePlanContent", plan);
        } catch(MyBatisSystemException e) {
            log.error(e.getCause().getMessage());
            throw new MyBatisSystemException(e.getCause());
        }
    }

    @Override
    public int updateQuestionInfo(QuestionInfo plan) {
        try {
            return sqlSession.delete("com.innon.education.management.mapper.plan-mapper.updateQuestionInfo", plan);
        } catch(MyBatisSystemException e) {
            log.error(e.getCause().getMessage());
            throw new MyBatisSystemException(e.getCause());
        }
    }

    @Override
    public int updatePlanDocument(PlanDocument plan) {
        try {
            return sqlSession.delete("com.innon.education.management.mapper.plan-mapper.updatePlanDocument", plan);
        } catch(MyBatisSystemException e) {
            log.error(e.getCause().getMessage());
            throw new MyBatisSystemException(e.getCause());
        }
    }

    @Override
    public ManagementPlanDTO educationPlanDetail(ManagementPlan plan) {
        try {
            return sqlSession.selectOne("com.innon.education.management.mapper.plan-mapper.educationPlanDetail", plan);
        } catch(MyBatisSystemException e) {
            log.error(e.getCause().toString());
            throw new MyBatisSystemException(e.getCause());
        }
    }

    @Override
    public int deleteTempEducation(ManagementPlan plan) {
        try {
            return sqlSession.update("com.innon.education.management.mapper.plan-mapper.deleteTempEducation", plan);
        } catch(MyBatisSystemException e) {
            log.error(e.getCause().getMessage());
            throw new MyBatisSystemException(e.getCause());
        }
    }

    @Override
    public EducationPlanUserInfoDTO educationPlanUserInfo(ManagementPlan plan) {
        try {
            return sqlSession.selectOne("com.innon.education.management.mapper.plan-mapper.educationPlanUserInfo", plan);
        } catch(MyBatisSystemException e) {
            log.error(e.getCause().toString());
            throw new MyBatisSystemException(e.getCause());
        }
    }
}
