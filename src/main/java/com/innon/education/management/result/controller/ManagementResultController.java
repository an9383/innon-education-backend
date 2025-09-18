package com.innon.education.management.result.controller;

import com.innon.education.common.service.CommonService;
import com.innon.education.code.controller.dto.ResultDTO;
import com.innon.education.management.result.repository.model.ManagementResult;
import com.innon.education.management.result.service.ManagementResultService;
import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api/education/management/result")
public class ManagementResultController {

    @Autowired
    private ManagementResultService resultService;
    @Autowired
    CommonService commonService;
    private ResultDTO resultDTO;

    @PostMapping("/save")
    public ResponseEntity<ResultDTO> saveManagementResult(@RequestBody @Nullable ManagementResult result, HttpServletRequest request, Authentication auth) {

        resultDTO = new ResultDTO();
        resultDTO = commonService.findCheckUserAuth(result.getUser(), request, auth);
        if (resultDTO.getCode() == 200) {
            resultDTO = resultService.saveEducationResult(result, request, auth);
        }
        return new ResponseEntity<>(resultDTO, HttpStatus.CREATED);
    }

    @PostMapping("/update")
    public ResponseEntity<ResultDTO> updateManagementResult(@RequestBody @Nullable ManagementResult result, HttpServletRequest request, Authentication auth) {

        resultDTO = new ResultDTO();
        resultDTO = commonService.findCheckUserAuth(result.getUser(), request, auth);
        if (resultDTO.getCode() == 200) {
            resultDTO = resultService.updateEducationResult(result, request, auth);
        }
        return new ResponseEntity<>(resultDTO, HttpStatus.CREATED);
    }


    @PostMapping("/detail")
    public  ResponseEntity<ResultDTO> managementResultDetail(@RequestBody @Nullable ManagementResult result) {
        ResultDTO res = resultService.managementResultDetail(result);
        return ResponseEntity.ok(res);
    }

    @PostMapping("/list")
    public  ResponseEntity<ResultDTO> findManagementResult(@RequestBody @Nullable ManagementResult result) {
        ResultDTO res = resultService.findResultPlanUserList(result);
        return ResponseEntity.ok(res);
    }
}
