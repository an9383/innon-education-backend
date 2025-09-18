package com.innon.education.management.plan.service;

import java.util.List;

import com.innon.education.code.controller.dto.ResultDTO;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;

import com.innon.education.library.document.repasitory.dto.PlanDocument;
import com.innon.education.management.plan.repository.entity.ManagementPlanUserEntity;
import com.innon.education.management.plan.repository.model.EducationPlanContent;
import com.innon.education.management.plan.repository.model.EducationPlanLab;
import com.innon.education.management.plan.repository.model.ManagementPlan;
import com.innon.education.management.plan.repository.model.PlanQms;
import com.innon.education.management.plan.repository.model.QuestionInfo;
import com.innon.education.management.plan.repository.model.UserEduCurrent;

import jakarta.servlet.http.HttpServletRequest;

public interface ManagementPlanService {
    ResultDTO managementPlanList(ManagementPlan plan);

    ResultDTO updateEducationPlanStatus(List<ManagementPlan> planList);

    ResultDTO savePlan(ManagementPlan plan, HttpServletRequest request, Authentication auth);
    ResultDTO savePlanUser(ManagementPlan plan, int plan_id, HttpServletRequest request, Authentication auth);
    ResultDTO savePlanContent(ManagementPlan plan, int plan_id, HttpServletRequest request, Authentication auth);
    ResultDTO saveQuestionInfo(ManagementPlan plan, int plan_id, HttpServletRequest request, Authentication auth);
    ResultDTO saveQuestionInfoDetail(ManagementPlan plan, int question_id);

    ResultDTO requestQmsPlan(PlanQms plan);

    ResultDTO saveEducationPlan(List<EducationPlanLab> planList);

    ResultDTO findUserEduCurrentList(UserEduCurrent userEduCurrent, HttpServletRequest request, Authentication auth);

    ResultDTO savePlanDocument(ManagementPlan plan, int planId, HttpServletRequest request, Authentication auth);

    ResultDTO updatePlan(ManagementPlan plan, HttpServletRequest request, Authentication auth);

    ResultDTO uploadEducationPlanFile (MultipartFile uploadFiles, HttpServletRequest request, Authentication auth);

    int updatePlanUser(ManagementPlanUserEntity plan);

    int updatePlanContent(EducationPlanContent plan);

    int updateQuestionInfo(QuestionInfo plan);

    int updatePlanDocument(PlanDocument plan);

    ResultDTO managementPlanDetail(ManagementPlan plan);

    ResultDTO deleteTempEducation(ManagementPlan plan, HttpServletRequest request, Authentication auth);
}
