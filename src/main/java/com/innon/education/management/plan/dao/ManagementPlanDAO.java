package com.innon.education.management.plan.dao;

import com.innon.education.library.document.repasitory.dto.DocumentDTO;
import com.innon.education.library.document.repasitory.dto.PlanDocument;
import com.innon.education.management.plan.repository.dto.EducationPlanUserInfoDTO;
import com.innon.education.management.plan.repository.dto.ManagementPlanDTO;
import com.innon.education.management.plan.repository.dto.ManagementPlanUserDTO;
import com.innon.education.management.plan.repository.dto.UserEduCurrentDTO;
import com.innon.education.management.plan.repository.entity.*;
import com.innon.education.management.plan.repository.model.EducationPlanContent;
import com.innon.education.management.plan.repository.model.ManagementPlan;
import com.innon.education.management.plan.repository.model.QuestionInfo;
import com.innon.education.management.plan.repository.model.UserEduCurrent;

import java.util.List;
import java.util.Map;

public interface ManagementPlanDAO {
    int savePlan(ManagementPlan plan);
    int savePlanUser(ManagementPlanUserEntity planUserEntity);
    int savePlanContent(HelpUrlEntity helpUrlEntity);
    int saveQuestionInfo(QuestionInfoEntity questionInfoEntity);
    int saveQuestionInfoDetail(QuestionInfoDetailEntity questionInfoDetailEntity);

    List<ManagementPlanDTO> educationPlanList(ManagementPlanEntity entity);
    List<ManagementPlanUserDTO> findManagementPlanUser(int plan_id);
    List<Map<String, Object>> findManagementHelpUrl(int plan_id);
    List<Map<String, Object>> findQuestionInfo(int plan_id);
    List<Map<String, Object>> findQuestionDetail(Object question_info_id);

    int updateEducationPlanStatus(ManagementPlanEntity planEntity);
    int updateEducationPlan(ManagementPlanEntity planEntity);;

    int saveWorkManagement(WorkManagementEntity workEntity);
    int saveQmsRes(PlanQmsEntity qmsEntity);

    List<UserEduCurrentDTO> findUserEduCurrentList(UserEduCurrent userEduCurrent);

    int savePlanDocument(PlanDocument paramDocument);

    List<DocumentDTO> findManagementPlanDocument(int planId);


    int updatePlan(ManagementPlan plan);

    int updatePlanUser(ManagementPlanUserEntity plan);

    int updatePlanContent(EducationPlanContent plan);

    int updateQuestionInfo(QuestionInfo plan);

    int updatePlanDocument(PlanDocument plan);

    ManagementPlanDTO educationPlanDetail(ManagementPlan plan);

    int deleteTempEducation(ManagementPlan plan);


    EducationPlanUserInfoDTO educationPlanUserInfo(ManagementPlan plan);
}
