package com.innon.education.annual.plan.service;

import com.innon.education.annual.plan.repository.entity.AnnualPlanJoinTable;
import com.innon.education.annual.plan.repository.entity.AnnualPlanSearchEntity;
import com.innon.education.annual.plan.repository.model.AnnualPlan;
import com.innon.education.controller.dto.ResultDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;

public interface AnnualPlanService {
   // ResultDTO saveAnnualPlanList(AnnualPlan annualPlan, HttpServletRequest request, Authentication auth);
    ResultDTO annualPlanList(AnnualPlanSearchEntity plan);
    ResultDTO migrateAnnualPlan(AnnualPlanSearchEntity plan);

    ResultDTO saveAnnualPlan(AnnualPlan annualPlan, Authentication auth, HttpServletRequest request);

    ResultDTO saveAnnualPlanJoin(AnnualPlanJoinTable annualPlanJoinTable);
    ResultDTO updateAnnualPlanJoin(AnnualPlanJoinTable annualPlanJoinTable);

    ResultDTO updateAnnualPlan(AnnualPlan annualPlan, Authentication auth, HttpServletRequest request);

    ResultDTO findAnnualPlanView(AnnualPlanSearchEntity plan);

    ResultDTO annualEducationPlanList(AnnualPlanSearchEntity plan);

    ResultDTO revisionAnnualPlan(AnnualPlan annualPlan, Authentication auth, HttpServletRequest request);

    ResultDTO annualList(AnnualPlanSearchEntity plan);

 ResultDTO examineAnnualPlan(AnnualPlan annualPlan, Authentication auth, HttpServletRequest request);

 ResultDTO saveDeptAnnualPlan(AnnualPlan annualPlan, Authentication auth, HttpServletRequest request);

 ResultDTO saveAnnualSign(AnnualPlan annualPlan, Authentication auth, HttpServletRequest request);

    ResultDTO editableDept(AnnualPlan annualPlan, Authentication auth, HttpServletRequest request);
}
