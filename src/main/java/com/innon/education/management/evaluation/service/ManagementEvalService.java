package com.innon.education.management.evaluation.service;

import com.innon.education.controller.dto.ResultDTO;
import com.innon.education.management.evaluation.repository.model.ManagementEval;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface ManagementEvalService {
    ResultDTO savePuqAnswer(List<ManagementEval> evalList, HttpServletRequest request, Authentication auth);
    ResultDTO updatePuqAnswer(List<ManagementEval> evalList);
    ResultDTO findQuestionInfo(ManagementEval eval, HttpServletRequest request, Authentication auth);
    ResultDTO findWrongAnswerList(ManagementEval eval, HttpServletRequest request, Authentication auth);
}
