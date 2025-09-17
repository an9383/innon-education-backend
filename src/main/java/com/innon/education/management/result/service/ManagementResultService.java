package com.innon.education.management.result.service;

import com.innon.education.controller.dto.ResultDTO;
import com.innon.education.management.result.repository.model.ManagementResult;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;

public interface ManagementResultService {
    ResultDTO saveEducationResult(ManagementResult result, HttpServletRequest request, Authentication auth);
    ResultDTO findResultPlanUserList(ManagementResult result);

    ResultDTO managementResultDetail(ManagementResult result);

    ResultDTO updateEducationResult(ManagementResult result, HttpServletRequest request, Authentication auth);
}
