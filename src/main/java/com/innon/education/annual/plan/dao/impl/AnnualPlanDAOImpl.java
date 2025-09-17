package com.innon.education.annual.plan.dao.impl;

import com.innon.education.annual.plan.dao.AnnualPlanDAO;
import com.innon.education.annual.plan.repository.dto.AnnualPlanDTO;
import com.innon.education.annual.plan.repository.dto.TbAnnualPlanDTO;
import com.innon.education.annual.plan.repository.entity.AnnualPlanEntity;
import com.innon.education.annual.plan.repository.entity.AnnualPlanJoinTable;
import com.innon.education.annual.plan.repository.entity.AnnualPlanMigrateEntity;
import com.innon.education.annual.plan.repository.entity.AnnualPlanSearchEntity;
import com.innon.education.management.plan.repository.model.ManagementPlan;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.MyBatisSystemException;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Slf4j
public class AnnualPlanDAOImpl implements AnnualPlanDAO {

    @Autowired
    SqlSessionTemplate sqlSession;

    @Override
    public int saveAnnualPlan(AnnualPlanEntity plan) {
        try {
            return sqlSession.insert("com.innon.education.annual.mapper.plan-mapper.saveAnnualPlan", plan);
        } catch(MyBatisSystemException e) {
            log.error(e.getCause().toString());
            throw new MyBatisSystemException(e.getCause());
        }
    }

    @Override
    public List<AnnualPlanDTO> findAnnualPlanList(AnnualPlanSearchEntity entity) {
        try {
            return sqlSession.selectList("com.innon.education.annual.mapper.plan-mapper.findAnnualPlanList", entity);
        } catch(MyBatisSystemException e) {
            log.error(e.getCause().toString());
            throw new MyBatisSystemException(e.getCause());
        }
    }

    @Override
    public AnnualPlanDTO findAnnualPlan(AnnualPlanSearchEntity annualPlan) {
        try {
            return sqlSession.selectOne("com.innon.education.annual.mapper.plan-mapper.findAnnualPlan", annualPlan);
        } catch(MyBatisSystemException e) {
            log.error(e.getCause().toString());
            throw new MyBatisSystemException(e.getCause());
        }
    }

    @Override
    public int migrateAnnualPlan(AnnualPlanMigrateEntity entity) {
        try {
            return sqlSession.insert("com.innon.education.annual.mapper.plan-mapper.migrateAnnualPlan", entity);
        } catch(MyBatisSystemException e) {
            log.error(e.getCause().toString());
            throw new MyBatisSystemException(e.getCause());
        }
    }

    @Override
    public int findAnnualPlanListCnt(AnnualPlanSearchEntity entity) {
        try {
            return sqlSession.selectOne("com.innon.education.annual.mapper.plan-mapper.findAnnualPlanListCnt", entity);
        } catch(MyBatisSystemException e) {
            log.error(e.getCause().toString());
            throw new MyBatisSystemException(e.getCause());
        }
    }

    @Override
    public int saveAnnualPlanJoin(AnnualPlanJoinTable annualPlan) {
        try {
            return sqlSession.insert("com.innon.education.annual.mapper.plan-mapper.saveAnnualPlanJoin", annualPlan);
        } catch(MyBatisSystemException e) {
            log.error(e.getCause().toString());
            throw new MyBatisSystemException(e.getCause());
        }
    }

    @Override
    public int updateAnnualPlan(AnnualPlanEntity annualPlan) {
        try {
            return sqlSession.update("com.innon.education.annual.mapper.plan-mapper.updateAnnualPlan", annualPlan);
        } catch(MyBatisSystemException e) {
            System.out.println(e.getCause().toString());
            throw new MyBatisSystemException(e.getCause());
        }
    }

    @Override
    public List<AnnualPlanDTO> annualEducationPlanList(AnnualPlanSearchEntity plan) {
        try {
            return sqlSession.selectList("com.innon.education.annual.mapper.plan-mapper.annualEducationPlanList", plan);
        } catch(MyBatisSystemException e) {
            log.error(e.getCause().toString());
            throw new MyBatisSystemException(e.getCause());
        }
    }

    @Override
    public int updateAnnualPlanJoin(AnnualPlanJoinTable delPlan) {
        try {
            return sqlSession.update("com.innon.education.annual.mapper.plan-mapper.updateAnnualPlanJoin", delPlan);
        } catch(MyBatisSystemException e) {
            System.out.println(e.getCause().toString());
            throw new MyBatisSystemException(e.getCause());
        }
    }

    @Override
    public List<AnnualPlanDTO> annualList(AnnualPlanSearchEntity plan) {
        try {
            return sqlSession.selectList("com.innon.education.annual.mapper.plan-mapper.findAnnualPlan", plan);
        } catch(MyBatisSystemException e) {
            log.error(e.getCause().toString());
            throw new MyBatisSystemException(e.getCause());
        }
    }

    @Override
    public int saveAnnualSaveYn(AnnualPlanEntity annualPlan) {
        try {
            return sqlSession.update("com.innon.education.annual.mapper.plan-mapper.saveAnnualSaveYn", annualPlan);
        } catch(MyBatisSystemException e) {
            log.error(e.getCause().toString());
            throw new MyBatisSystemException(e.getCause());
        }
    }

    @Override
    public int saveAnnualSign(AnnualPlanEntity saveAnnualPlan) {
        try {
            return sqlSession.update("com.innon.education.annual.mapper.plan-mapper.saveAnnualSign", saveAnnualPlan);
        } catch(MyBatisSystemException e) {
            log.error(e.getCause().toString());
            throw new MyBatisSystemException(e.getCause());
        }
    }

    @Override
    public int findAnnualEducationMaxVersion(AnnualPlanSearchEntity plan) {
        try {
            return sqlSession.selectOne("com.innon.education.annual.mapper.plan-mapper.findAnnualEducationMaxVersion", plan);
        } catch(MyBatisSystemException e) {
            log.error(e.getCause().toString());
            throw new MyBatisSystemException(e.getCause());
        }
    }

    @Override
    public List<AnnualPlanDTO> findBeforeVersionAnnualEdu(AnnualPlanEntity searchAnnualEntity) {
        try {
            return sqlSession.selectList("com.innon.education.annual.mapper.plan-mapper.findBeforeVersionAnnualEdu", searchAnnualEntity);
        } catch(MyBatisSystemException e) {
            log.error(e.getCause().toString());
            throw new MyBatisSystemException(e.getCause());
        }
    }

    @Override
    public List<ManagementPlan> findBeforeVersionEducationPlan(AnnualPlanEntity searchAnnualEntity) {
        try {
            return sqlSession.selectList("com.innon.education.annual.mapper.plan-mapper.findBeforeVersionEducationPlan", searchAnnualEntity);
        } catch(MyBatisSystemException e) {
            log.error(e.getCause().toString());
            throw new MyBatisSystemException(e.getCause());
        }
    }

    @Override
    public List<TbAnnualPlanDTO> findBeforeVersionAnnualPlan(AnnualPlanEntity searchAnnualEntity) {
        try {
            return sqlSession.selectList("com.innon.education.annual.mapper.plan-mapper.findBeforeVersionAnnualPlan", searchAnnualEntity);
        } catch(MyBatisSystemException e) {
            log.error(e.getCause().toString());
            throw new MyBatisSystemException(e.getCause());
        }
    }
}
