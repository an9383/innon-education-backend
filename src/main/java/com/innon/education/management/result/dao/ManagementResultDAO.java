package com.innon.education.management.result.dao;

import com.innon.education.management.plan.repository.entity.PlanQmsEntity;
import com.innon.education.management.plan.repository.model.PlanQms;
import com.innon.education.management.result.repository.dto.ManagementResultDTO;
import com.innon.education.management.result.repository.dto.ReEducationDTO;
import com.innon.education.management.result.repository.entity.ManagementResultEntity;
import com.innon.education.management.result.repository.entity.ReEducationEntity;
import com.innon.education.management.result.repository.model.ManagementResult;

import java.util.List;

public interface ManagementResultDAO {
    int saveEducationResult(ManagementResultEntity entity);


    List<ManagementResultDTO> findResultPlanUserList(int plan_id);
    List<PlanQms> findPlanQmsList(PlanQmsEntity planQmsEntity);

    int saveQmsRes(PlanQms planQms);

    PlanQms findPlanQms(int planId);

    int saveReduceStudent(ReEducationEntity reEducationEntity);

    ManagementResultDTO managementResultDetail(ManagementResult planId);

    int updateEducationResult(ManagementResultEntity resultEntity);

    List<ReEducationDTO> reduceStudentList(int planId);
}
