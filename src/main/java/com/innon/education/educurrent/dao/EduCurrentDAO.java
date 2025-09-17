package com.innon.education.educurrent.dao;

import com.innon.education.educurrent.repository.dto.EduCurrentUserDTO;
import com.innon.education.management.plan.repository.dto.ManagementPlanDTO;

import java.util.List;
import java.util.Map;

public interface EduCurrentDAO {
    int findEduResultByPlanId(int plan_id);
    int findEduProgressByPlanId(int plan_id);
    int findEduEvaluationByPlanId(int plan_id);
    int findEduPlanByPlanId(int plan_id);

    int findAllEduPlan(Map<String, Object> searchParam);
    int findAllEduProgress(Map<String, Object> searchParam);

//    int findEduPlanByUserId(String qms_user_id);
//    int findEduProgressByUserId(String qms_user_id);
//    int findEduEvaluationByUserId(String qms_user_id);
//    int findEduResultByUserId(String qms_user_id);

    List<ManagementPlanDTO> findEduPlanByUserId(String qms_user_id);
    List<ManagementPlanDTO> findEduProgressByUserId(String qms_user_id);
    List<ManagementPlanDTO> findEduEvaluationByUserId(String qms_user_id);
    List<ManagementPlanDTO> findEduResultByUserId(String qms_user_id);
    List<ManagementPlanDTO> findEduQmsByEduUserId(String edu_user_id);

    List<EduCurrentUserDTO> findEduCurrentUserList(String qms_user_id);
}
