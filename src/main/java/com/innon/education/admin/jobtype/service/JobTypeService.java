package com.innon.education.admin.jobtype.service;

import com.innon.education.admin.jobtype.repository.JobType;
import com.innon.education.controller.dto.ResultDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;

public interface JobTypeService {
    ResultDTO findJobTypeList(JobType jobType, HttpServletRequest request, Authentication auth);

    ResultDTO saveJobType(JobType jobType, HttpServletRequest request, Authentication auth);

    ResultDTO findJobTypeDeptList(JobType jobType, HttpServletRequest request, Authentication auth);

    ResultDTO updateJobType(JobType jobType, HttpServletRequest request, Authentication auth);

    ResultDTO findJobTypeDept(JobType jobType, HttpServletRequest request, Authentication auth);
}
