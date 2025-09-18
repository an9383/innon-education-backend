package com.innon.education.qualified.current.service;

import com.innon.education.code.controller.dto.ResultDTO;
import com.innon.education.management.plan.repository.dto.ManagementPlanUserDTO;
import com.innon.education.qualified.current.repository.model.JobRequest;
import com.innon.education.qualified.current.repository.model.JobRevision;
import com.innon.education.qualified.current.repository.model.JobSkill;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;

public interface CurrentService {
    ResultDTO saveJobRequest(JobRequest jobRequest, HttpServletRequest request, Authentication auth);

    ResultDTO findJobRequestList(JobRequest jobRequest, HttpServletRequest request, Authentication auth);

    ResultDTO findMyJobRequestList(JobRequest jobRequest, HttpServletRequest request, Authentication auth);

    ResultDTO findJobRevisionList(JobRequest jobRequest, HttpServletRequest request, Authentication auth);

    ResultDTO findRevisionContentList(JobRevision jobRevision, HttpServletRequest request, Authentication auth);

    ResultDTO findRevisionDocumentList(JobRevision jobRevision, HttpServletRequest request, Authentication auth);

    ResultDTO updateJobRequst(JobRequest jobRequest, HttpServletRequest request, Authentication auth);

    ResultDTO findJobSkillList(ManagementPlanUserDTO managementPlanUserDTO, HttpServletRequest request, Authentication auth);

    ResultDTO updateJobSkill(JobSkill jobSkill, HttpServletRequest request, Authentication auth);

    ResultDTO saveJobSkill(JobSkill jobSkill, HttpServletRequest request, Authentication auth);

    ResultDTO userJobSkillList(JobSkill jobSkill, HttpServletRequest request, Authentication auth);

    ResultDTO JobSkillUserItemList(JobSkill jobSkill, HttpServletRequest request, Authentication auth);

    ResultDTO deleteTempJobRequest(JobRequest jobRequest, HttpServletRequest request, Authentication auth);
}
