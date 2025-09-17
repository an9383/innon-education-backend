package com.innon.education.educurrent.service;

import com.innon.education.controller.dto.ResultDTO;
import com.innon.education.educurrent.repository.model.EduCurrent;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;

public interface EduCurrentService {
    ResultDTO findEduCurrentByPlanId(EduCurrent eduCurrent);
    ResultDTO findAllEduCurrent(EduCurrent eduCurrent);
    ResultDTO findEduCurrentByUserId(EduCurrent eduCurrent);

    ResultDTO findEduCurrentUserList(EduCurrent eduCurrent);

    ResultDTO findEduCurrentPlanList(EduCurrent eduCurrent, HttpServletRequest request, Authentication auth);
    ResultDTO findEduCurrentProgressList(EduCurrent eduCurrent, HttpServletRequest request, Authentication auth);
    ResultDTO findEduCurrentEvaluationList(EduCurrent eduCurrent, HttpServletRequest request, Authentication auth);
    ResultDTO findEduCurrentResultList(EduCurrent eduCurrent, HttpServletRequest request, Authentication auth);
}
