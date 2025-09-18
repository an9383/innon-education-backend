package com.innon.education.management.progress.service;

import com.innon.education.code.controller.dto.ResultDTO;
import com.innon.education.management.progress.repository.entity.UserAttendanceEntity;
import com.innon.education.management.progress.repository.model.ManagementProgress;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;

public interface ManagementProgressService {
    ResultDTO saveEpuReason(ManagementProgress progress);
    ResultDTO findManagementProgress(ManagementProgress progress, Authentication auth);

    ResultDTO saveUserAttendace(ManagementProgress progress, HttpServletRequest request, Authentication auth);

    ResultDTO findEduContentInfo(ManagementProgress progress, HttpServletRequest request, Authentication auth);
    ResultDTO attendUserEduContent(UserAttendanceEntity entity, HttpServletRequest request, Authentication auth);

    ResultDTO planByAttendList(UserAttendanceEntity progress, HttpServletRequest request, Authentication auth);
}
