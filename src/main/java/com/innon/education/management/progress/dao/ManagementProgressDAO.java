package com.innon.education.management.progress.dao;

import com.innon.education.management.plan.repository.dto.ManagementPlanDTO;
import com.innon.education.management.plan.repository.entity.ManagementPlanUserEntity;
import com.innon.education.management.progress.repository.dto.EducationPlanUserDTO;
import com.innon.education.management.progress.repository.dto.ManagementProgressDTO;
import com.innon.education.management.progress.repository.dto.PlanContentDTO;
import com.innon.education.management.progress.repository.dto.UserAttendanceDTO;
import com.innon.education.management.progress.repository.entity.ManagementProgressEntity;
import com.innon.education.management.progress.repository.entity.UserAttendanceEntity;
import com.innon.education.management.progress.repository.model.ManagementProgress;

import java.util.List;

public interface ManagementProgressDAO {
    int saveEpuReason(ManagementProgressEntity entity);
    List<ManagementProgressDTO> findManagementProgress(ManagementProgressEntity entity);
    List<String> findEducationPlanUserList(int plan_id);
    List<String> findPlanContent(int planId);
    EducationPlanUserDTO findEducationPlanUserByPlanIdAndUserId(ManagementPlanUserEntity entity);

    int saveUserAttendance(UserAttendanceEntity entity);

    List<PlanContentDTO> findEduContentList(ManagementProgress progress);
    ManagementPlanDTO findEducationPlanById(int plan_id);

    UserAttendanceDTO findUserAttendance(UserAttendanceEntity entity);

    List<UserAttendanceDTO> planByAttendList(UserAttendanceEntity progress);
}
