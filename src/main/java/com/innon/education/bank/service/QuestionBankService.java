package com.innon.education.bank.service;

import com.innon.education.bank.repository.model.QbQuestion;
import com.innon.education.bank.repository.model.QuestionBank;
import com.innon.education.code.controller.dto.ResultDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;

public interface QuestionBankService {
    ResultDTO saveQuestionBank(QuestionBank questionBank, HttpServletRequest request, Authentication auth);
    ResultDTO updateQuestionBank(QuestionBank questionBank, HttpServletRequest request, Authentication auth);
    ResultDTO deleteQuestionBank(QuestionBank questionBank, HttpServletRequest request, Authentication auth);
    ResultDTO findQuestionBankList(QuestionBank questionBank, HttpServletRequest request, Authentication auth);

    ResultDTO saveQbQuestion(QbQuestion question, HttpServletRequest request, Authentication auth);
    ResultDTO updateQbQuestion(QbQuestion question, HttpServletRequest request, Authentication auth);
    ResultDTO deleteQbQuestions(QuestionBank questionBank, HttpServletRequest request, Authentication auth);
    ResultDTO findQbQuestionList(QbQuestion question, HttpServletRequest request, Authentication auth);
}
