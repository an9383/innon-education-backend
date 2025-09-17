package com.innon.education.management.progress.controller;

import com.innon.education.controller.dto.ResultDTO;
import com.innon.education.management.progress.repository.entity.UserAttendanceEntity;
import com.innon.education.management.progress.repository.model.ManagementProgress;
import com.innon.education.management.progress.service.ManagementProgressService;
import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api/education/management/progress")
public class ManagementProgressController {

    @Autowired
    ManagementProgressService managementProgressService;

    /**
     * URL : /reason/save
     * description : 수강생 사유 등록
     * param : progress
     * return : resultDTO
     */
    @PostMapping("/reason/save")
    public ResponseEntity<ResultDTO> saveEpuReason(@RequestBody @Nullable ManagementProgress progress) {
        ResultDTO res = managementProgressService.saveEpuReason(progress);
        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }

    /**
     * URL : /list
     * description : 교육진행 조회
     * param : plan
     * response : resultDTO
     */
    @PostMapping("/list")
    public ResponseEntity<ResultDTO> findManagementProgress(@RequestBody @Nullable ManagementProgress progress, Authentication auth) {
        ResultDTO res = managementProgressService.findManagementProgress(progress, auth);
        return ResponseEntity.ok(res);
    }

    @PostMapping("/user/attend")
    public ResponseEntity<ResultDTO> planByAttendList(@RequestBody UserAttendanceEntity progress, HttpServletRequest request, Authentication auth) {
        ResultDTO res = managementProgressService.planByAttendList(progress, request, auth);
        return ResponseEntity.ok(res);
    }


    @PostMapping("/content/list")
    public ResponseEntity<ResultDTO> findEduContentInfo(@RequestBody ManagementProgress progress, HttpServletRequest request, Authentication auth) {
        ResultDTO res = managementProgressService.findEduContentInfo(progress, request, auth);
        return ResponseEntity.ok(res);
    }

    @PostMapping("/content/attend")
    public ResponseEntity<ResultDTO> attendUserEduContent(@RequestBody UserAttendanceEntity entity, HttpServletRequest request, Authentication auth) {
        ResultDTO res = managementProgressService.attendUserEduContent(entity, request, auth);
        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }

    /**
     * URL : /attend
     * description : 출석체크 등록
     * param : attend_user_id
     * response : resultDTO
     */
    @PostMapping("/attend")
    public ResponseEntity<ResultDTO> saveUserAttendance(@RequestBody ManagementProgress progress, HttpServletRequest request, Authentication auth) {
        ResultDTO res = managementProgressService.saveUserAttendace(progress, request, auth);
        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }
}
