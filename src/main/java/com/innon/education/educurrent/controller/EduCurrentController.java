package com.innon.education.educurrent.controller;

import com.innon.education.controller.dto.ResultDTO;
import com.innon.education.educurrent.repository.model.EduCurrent;
import com.innon.education.educurrent.service.EduCurrentService;
import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api/education/current")
public class EduCurrentController {

    @Autowired
    private EduCurrentService eduCurrentService;

    @PostMapping("/base/plan")
    public ResponseEntity<ResultDTO> findEduCurrentByPlanId(@RequestBody EduCurrent eduCurrent) {
        ResultDTO res = eduCurrentService.findEduCurrentByPlanId(eduCurrent);
        return ResponseEntity.ok(res);
    }

    @PostMapping("/base/all")
    public ResponseEntity<ResultDTO> findAllEduCurrent(@RequestBody @Nullable EduCurrent eduCurrent) {
        ResultDTO res = eduCurrentService.findAllEduCurrent(eduCurrent);
        return ResponseEntity.ok(res);
    }

    @PostMapping("/base/user")
    public ResponseEntity<ResultDTO> findEduCurrentByUserId(@RequestBody EduCurrent eduCurrent) {
        ResultDTO res = eduCurrentService.findEduCurrentByUserId(eduCurrent);
        return ResponseEntity.ok(res);
    }

    @PostMapping("/user")
    public ResponseEntity<ResultDTO> findEduCurrentUserList(@RequestBody @Nullable EduCurrent eduCurrent) {
        ResultDTO res = eduCurrentService.findEduCurrentUserList(eduCurrent);
        return ResponseEntity.ok(res);
    }

    @PostMapping("/plan/list")
    public ResponseEntity<ResultDTO> findEduCurrentPlanList(@RequestBody EduCurrent eduCurrent, HttpServletRequest request, Authentication auth) {
        ResultDTO res = eduCurrentService.findEduCurrentPlanList(eduCurrent, request, auth);
        return ResponseEntity.ok(res);
    }

    @PostMapping("/progress/list")
    public ResponseEntity<ResultDTO> findEduCurrentProgressList(@RequestBody EduCurrent eduCurrent, HttpServletRequest request, Authentication auth) {
        ResultDTO res = eduCurrentService.findEduCurrentProgressList(eduCurrent, request, auth);
        return ResponseEntity.ok(res);
    }

    @PostMapping("/evaluation/list")
    public ResponseEntity<ResultDTO> findEduCurrentEvaluationList(@RequestBody EduCurrent eduCurrent, HttpServletRequest request, Authentication auth) {
        ResultDTO res = eduCurrentService.findEduCurrentEvaluationList(eduCurrent, request, auth);
        return ResponseEntity.ok(res);
    }

    @PostMapping("/result/list")
    public ResponseEntity<ResultDTO> findEduCurrentResultList(@RequestBody EduCurrent eduCurrent, HttpServletRequest request, Authentication auth) {
        ResultDTO res = eduCurrentService.findEduCurrentResultList(eduCurrent, request, auth);
        return ResponseEntity.ok(res);
    }
}
