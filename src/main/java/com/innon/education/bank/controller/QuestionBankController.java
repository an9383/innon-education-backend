package com.innon.education.bank.controller;

import com.innon.education.bank.repository.model.QbQuestion;
import com.innon.education.bank.repository.model.QuestionBank;
import com.innon.education.bank.service.QuestionBankService;
import com.innon.education.controller.dto.ResultDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

@CrossOrigin
@RestController
@RequestMapping("/api/education/bank")
public class QuestionBankController {

    @Autowired
    private QuestionBankService questionBankService;
    @Autowired
    MessageSource messageSource;

    @PostMapping("/save")
    public ResponseEntity<ResultDTO> saveQuestionBank(@RequestBody QuestionBank bank, HttpServletRequest request, Authentication auth) {
        ResultDTO res = new ResultDTO();
        try {
            res = questionBankService.saveQuestionBank(bank, request, auth);
        } catch(Exception e) {
            System.out.println(e.getCause());
            res.setState(false);
            res.setCode(400);
            res.setMessage(messageSource.getMessage("api.message.400", new String[]{"문제은행"}, Locale.KOREA));
        }

        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }

    @PostMapping("/update")
    public ResponseEntity<ResultDTO> updateQuestionBank(@RequestBody QuestionBank bank, HttpServletRequest request, Authentication auth) {
        ResultDTO res = new ResultDTO();
        try {
            res = questionBankService.updateQuestionBank(bank, request, auth);
        } catch(Exception e) {
            System.out.println(e.getCause());
            res.setState(false);
            res.setCode(400);
            res.setMessage(messageSource.getMessage("api.message.400", new String[]{"문제은행"}, Locale.KOREA));
        }

        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }

    @PostMapping("/delete")
    public ResponseEntity<ResultDTO> deleteQuestionBank(@RequestBody QuestionBank bank, HttpServletRequest request, Authentication auth) {
        ResultDTO res = new ResultDTO();
        try {
            res = questionBankService.deleteQuestionBank(bank, request, auth);
        } catch(Exception e) {
            System.out.println(e.getCause());
            res.setState(false);
            res.setCode(400);
            res.setMessage(messageSource.getMessage("api.message.400", new String[]{"문제은행"}, Locale.KOREA));
        }

        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }

    @PostMapping("/list")
    public ResponseEntity<ResultDTO> findQuestionBankList(@RequestBody QuestionBank bank, HttpServletRequest request, Authentication auth) {
        ResultDTO res = new ResultDTO();
        try {
            res = questionBankService.findQuestionBankList(bank, request, auth);
        } catch(Exception e) {
            System.out.println(e.getCause());
            res.setState(false);
            res.setCode(400);
            res.setMessage(messageSource.getMessage("api.message.400", new String[]{"문제은행"}, Locale.KOREA));
        }

        return ResponseEntity.ok(res);
    }

    @PostMapping("/question/save")
    public ResponseEntity<ResultDTO> saveQbQuestion(@RequestBody QbQuestion question, HttpServletRequest request, Authentication auth) {
        ResultDTO res = new ResultDTO();
        try {
            res = questionBankService.saveQbQuestion(question, request, auth);
        } catch(Exception e) {
            System.out.println(e.getCause());
            res.setState(false);
            res.setCode(400);
            res.setMessage(messageSource.getMessage("api.message.400", new String[]{"문제"}, Locale.KOREA));
        }

        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }

    @PostMapping("/question/update")
    public ResponseEntity<ResultDTO> updateQbQuestion(@RequestBody QbQuestion question, HttpServletRequest request, Authentication auth) {
        ResultDTO res = new ResultDTO();
        try {
            res = questionBankService.updateQbQuestion(question, request, auth);
        } catch(Exception e) {
            System.out.println(e.getCause());
            res.setState(false);
            res.setCode(400);
            res.setMessage(messageSource.getMessage("api.message.400", new String[]{"문제"}, Locale.KOREA));
        }

        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }

    @PostMapping("/question/delete")
    public ResponseEntity<ResultDTO> deleteQbQuestions(@RequestBody QuestionBank bank, HttpServletRequest request, Authentication auth) {
        ResultDTO res = new ResultDTO();
        try {
            res = questionBankService.deleteQbQuestions(bank, request, auth);
        } catch(Exception e) {
            System.out.println(e.getCause());
            res.setState(false);
            res.setCode(400);
            res.setMessage(messageSource.getMessage("api.message.400", new String[]{"문제"}, Locale.KOREA));
        }

        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }

    @PostMapping("/question/list")
    public ResponseEntity<ResultDTO> findQbQuestionList(@RequestBody QbQuestion question, HttpServletRequest request, Authentication auth) {
        ResultDTO res = new ResultDTO();
        try {
            res = questionBankService.findQbQuestionList(question, request, auth);
        } catch(Exception e) {
            System.out.println(e.getCause());
            res.setState(false);
            res.setCode(400);
            res.setMessage(messageSource.getMessage("api.message.400", new String[]{"문제"}, Locale.KOREA));
        }

        return ResponseEntity.ok(res);
    }
}
