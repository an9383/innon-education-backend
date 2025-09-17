package com.innon.education.qualified.current.dao;

import com.innon.education.management.plan.repository.dto.ManagementPlanUserDTO;
import com.innon.education.qualified.current.repository.dto.JobRequestDTO;
import com.innon.education.qualified.current.repository.dto.JobSkillDTO;
import com.innon.education.qualified.current.repository.entity.JobSkillEntity;
import com.innon.education.qualified.current.repository.entity.JobSkillItemEntity;
import com.innon.education.qualified.current.repository.model.*;

import java.util.List;

public interface CurrentDAO {
    int saveJobRequest(JobRequest jobRequest);
    int saveJobRevision(JobRevision jobRevision);
    int saveRevisionContent(RevisionContent revisionContent);
    int saveRevisionDocument(RevisionDocument revisionDocument);

    List<JobRequestDTO> findJobRequestList(JobRequest jobRequest);
    List<JobRevision> findJobRevisionList(JobRevision jobRevision);
    List<RevisionContent> findRevisionContentList(int revision_id);
    List<RevisionDocument> findRevisionDocumentList(int revision_id);

    JobRevision findJobRevision(JobRevision paramJob);

    int updateJobRevision(JobRevision updateJob);

    int updateRevisionContent(RevisionContent delContent);

    int updateRevisionDocument(RevisionDocument delDocument);

    List<JobRevision> findJobSkillList(ManagementPlanUserDTO managementPlanUserDTO);

    int saveJobSkill(JobSkillEntity jobSkill);

    int saveJobSkillItem(JobSkillItemEntity jobSkillItemEntity);

    List<JobSkillDTO> userJobSkillList(JobSkill jobSkill);

    List<JobSkillDTO> JobSkillUserItemList(JobSkill jobSkill);

    int updateJobSkill(JobSkillEntity updateSkill);

    int updateJobSKillItem(JobSkillItemEntity jobSkillItemEntity);

    JobRequestDTO findJobQualifiedStatusById(JobRequest jobRequest);

    int deleteTempJobRequest(JobRequest jobRequest);
}
