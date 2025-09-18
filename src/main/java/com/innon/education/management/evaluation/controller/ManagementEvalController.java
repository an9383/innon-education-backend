package com.innon.education.management.evaluation.controller;

import com.innon.education.code.controller.dto.ResultDTO;
import com.innon.education.management.evaluation.repository.model.ManagementEval;
import com.innon.education.management.evaluation.service.ManagementEvalService;
import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/education/management/eval")
public class ManagementEvalController {

    @Autowired
    private ManagementEvalService managementEvalService;

    @PostMapping("/save")
    public ResponseEntity<ResultDTO> savePuqAnswer(@RequestBody @Nullable List<ManagementEval> evalList, HttpServletRequest request, Authentication auth) {
        ResultDTO res = managementEvalService.savePuqAnswer(evalList, request, auth);
        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }

    @PutMapping("/update")
    public ResponseEntity<ResultDTO> updatePuqAnswer(@RequestBody @Nullable List<ManagementEval> evalList, HttpServletRequest request, Authentication auth) {
        ResultDTO res = managementEvalService.updatePuqAnswer(evalList);
        return new ResponseEntity<>(res, HttpStatus.ACCEPTED);
    }

    @PostMapping("/question/list")
    public ResponseEntity<ResultDTO> findQuestionInfo(@RequestBody ManagementEval eval, HttpServletRequest request, Authentication auth) {
        ResultDTO res = managementEvalService.findQuestionInfo(eval, request, auth);
        return ResponseEntity.ok(res);
    }

    @PostMapping("/wrong/list")
    public ResponseEntity<ResultDTO> findWrongAnswerList(@RequestBody ManagementEval eval, HttpServletRequest request, Authentication auth) {
        ResultDTO res = managementEvalService.findWrongAnswerList(eval, request, auth);
        return ResponseEntity.ok(res);
    }
}
