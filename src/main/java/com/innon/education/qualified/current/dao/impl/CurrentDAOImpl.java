package com.innon.education.qualified.current.dao.impl;

import com.innon.education.management.plan.repository.dto.ManagementPlanUserDTO;
import com.innon.education.qualified.current.dao.CurrentDAO;
import com.innon.education.qualified.current.repository.dto.JobRequestDTO;
import com.innon.education.qualified.current.repository.dto.JobSkillDTO;
import com.innon.education.qualified.current.repository.entity.JobSkillEntity;
import com.innon.education.qualified.current.repository.entity.JobSkillItemEntity;
import com.innon.education.qualified.current.repository.model.*;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.MyBatisSystemException;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
public class CurrentDAOImpl implements CurrentDAO {
    @Autowired
    SqlSessionTemplate sqlSession;

    @Override
    public int saveJobRequest(JobRequest jobRequest) {
        try {
            return sqlSession.insert("com.innon.education.qualified.mapper.current-mapper.saveJobRequest", jobRequest);
        } catch(MyBatisSystemException e) {
            System.out.println(e.getCause().toString());
            throw new MyBatisSystemException(e.getCause());
        }
    }

    @Override
    public int saveJobRevision(JobRevision jobRevision) {
        try {
            return sqlSession.insert("com.innon.education.qualified.mapper.current-mapper.saveJobRevision", jobRevision);
        } catch(MyBatisSystemException e) {
            System.out.println(e.getCause().toString());
            throw new MyBatisSystemException(e.getCause());
        }
    }

    @Override
    public int saveRevisionContent(RevisionContent revisionContent) {
        try {
            return sqlSession.insert("com.innon.education.qualified.mapper.current-mapper.saveRevisionContent", revisionContent);
        } catch(MyBatisSystemException e) {
            System.out.println(e.getCause().toString());
            throw new MyBatisSystemException(e.getCause());
        }
    }

    @Override
    public int saveRevisionDocument(RevisionDocument revisionDocument) {
        try {
            return sqlSession.insert("com.innon.education.qualified.mapper.current-mapper.saveRevisionDocument", revisionDocument);
        } catch(MyBatisSystemException e) {
            System.out.println(e.getCause().toString());
            throw new MyBatisSystemException(e.getCause());
        }
    }

    @Override
    public List<JobRequestDTO> findJobRequestList(JobRequest jobRequest) {
        try {
            return sqlSession.selectList("com.innon.education.qualified.mapper.current-mapper.findJobRequestList", jobRequest);
        } catch(MyBatisSystemException e) {
            System.out.println(e.getCause().toString());
            throw new MyBatisSystemException(e.getCause());
        }
    }

    @Override
    public List<JobRevision> findJobRevisionList(JobRevision jobRevision) {
        try {
            return sqlSession.selectList("com.innon.education.qualified.mapper.current-mapper.findJobRevisionList", jobRevision);
        } catch(MyBatisSystemException e) {
            System.out.println(e.getCause().toString());
            throw new MyBatisSystemException(e.getCause());
        }
    }

    @Override
    public List<RevisionContent> findRevisionContentList(int revision_id) {
        try {
            return sqlSession.selectList("com.innon.education.qualified.mapper.current-mapper.findRevisionContentList", revision_id);
        } catch(MyBatisSystemException e) {
             System.out.println(e.getCause().toString());
            throw new MyBatisSystemException(e.getCause());
        }
    }

    @Override
    public List<RevisionDocument> findRevisionDocumentList(int revision_id) {
        try {
            return sqlSession.selectList("com.innon.education.qualified.mapper.current-mapper.findRevisionDocumentList", revision_id);
        } catch(MyBatisSystemException e) {
             System.out.println(e.getCause().toString());
            throw new MyBatisSystemException(e.getCause());
        }
    }

    @Override
    public JobRevision findJobRevision(JobRevision paramJob) {
        try {
            return sqlSession.selectOne("com.innon.education.qualified.mapper.current-mapper.findJobRevision", paramJob);
        } catch(MyBatisSystemException e) {
             System.out.println(e.getCause().toString());
            throw new MyBatisSystemException(e.getCause());
        }
    }

    @Override
    public int updateJobRevision(JobRevision updateJob) {
        try {
            return sqlSession.update("com.innon.education.qualified.mapper.current-mapper.updateJobRevision", updateJob);
        } catch(MyBatisSystemException e) {
            log.error(e.getCause().toString());
            throw new MyBatisSystemException(e);
        }
    }

    @Override
    public int updateRevisionContent(RevisionContent delContent) {
        try {
            return sqlSession.update("com.innon.education.qualified.mapper.current-mapper.updateRevisionContent", delContent);
        } catch(MyBatisSystemException e) {
            log.error(e.getCause().toString());
            throw new MyBatisSystemException(e);
        }
    }

    @Override
    public int updateRevisionDocument(RevisionDocument delDocument) {
        try {
            return sqlSession.update("com.innon.education.qualified.mapper.current-mapper.updateRevisionDocument", delDocument);
        } catch(MyBatisSystemException e) {
            log.error(e.getCause().toString());
            throw new MyBatisSystemException(e);
        }
    }

    @Override
    public List<JobRevision> findJobSkillList(ManagementPlanUserDTO managementPlanUserDTO) {
        try {
            return sqlSession.selectList("com.innon.education.qualified.mapper.current-mapper.findJobSkillList", managementPlanUserDTO);
        } catch(MyBatisSystemException e) {
            log.error(e.getCause().toString());
            throw new MyBatisSystemException(e);
        }
    }

    @Override
    public int saveJobSkill(JobSkillEntity jobSkill) {
        try {
            return sqlSession.insert("com.innon.education.qualified.mapper.current-mapper.saveJobSkill", jobSkill);
        } catch(MyBatisSystemException e) {
            log.error(e.getCause().toString());
            throw new MyBatisSystemException(e.getCause());
        }
    }

    @Override
    public int saveJobSkillItem(JobSkillItemEntity jobskill) {
        try {
            return sqlSession.insert("com.innon.education.qualified.mapper.current-mapper.saveJobSkillItem", jobskill);
        } catch(MyBatisSystemException e) {
            log.error(e.getCause().toString());
            throw new MyBatisSystemException(e.getCause());
        }
    }

    @Override
    public List<JobSkillDTO> userJobSkillList(JobSkill jobSkill) {
        try {
            return sqlSession.selectList("com.innon.education.qualified.mapper.current-mapper.userJobSkillList", jobSkill);
        } catch(MyBatisSystemException e) {
            log.error(e.getCause().toString());
            throw new MyBatisSystemException(e);
        }
    }

    @Override
    public List<JobSkillDTO> JobSkillUserItemList(JobSkill jobSkill) {
        try {
            return sqlSession.selectList("com.innon.education.qualified.mapper.current-mapper.JobSkillUserItemList", jobSkill);
        } catch(MyBatisSystemException e) {
            log.error(e.getCause().toString());
            throw new MyBatisSystemException(e);
        }
    }

    @Override
    public int updateJobSkill(JobSkillEntity updateSkill) {
        try {
            return sqlSession.update("com.innon.education.qualified.mapper.current-mapper.updateJobSkill", updateSkill);
        } catch(MyBatisSystemException e) {
            log.error(e.getCause().toString());
            throw new MyBatisSystemException(e);
        }
    }

    @Override
    public int updateJobSKillItem(JobSkillItemEntity jobSkillItemEntity) {
        try {
            return sqlSession.update("com.innon.education.qualified.mapper.current-mapper.updateJobSKillItem", jobSkillItemEntity);
        } catch(MyBatisSystemException e) {
            log.error(e.getCause().toString());
            throw new MyBatisSystemException(e);
        }
    }

    @Override
    public JobRequestDTO findJobQualifiedStatusById(JobRequest jobRequest) {
        try {
            return sqlSession.selectOne("com.innon.education.qualified.mapper.current-mapper.findJobQualifiedStatusById", jobRequest);
        } catch(MyBatisSystemException e) {
            log.error(e.getCause().toString());
            throw new MyBatisSystemException(e);
        }
    }

    @Override
    public int deleteTempJobRequest(JobRequest jobRequest) {
        try {
            return sqlSession.delete("com.innon.education.qualified.mapper.current-mapper.deleteTempJobRequest", jobRequest);
        } catch(MyBatisSystemException e) {
            log.error(e.getCause().toString());
            throw new MyBatisSystemException(e);
        }
    }
}
