package com.innon.education.bank.service.impl;

import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.innon.education.bank.dao.QuestionBankDAO;
import com.innon.education.bank.repository.dto.QuestionBankDTO;
import com.innon.education.bank.repository.model.QbQItem;
import com.innon.education.bank.repository.model.QbQuestion;
import com.innon.education.bank.repository.model.QuestionBank;
import com.innon.education.bank.service.QuestionBankService;
import com.innon.education.common.repository.entity.LogEntity;
import com.innon.education.common.service.CommonService;
import com.innon.education.common.util.DataLib;
import com.innon.education.code.controller.dto.ResultDTO;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class QuestionBankServiceImpl implements QuestionBankService {

    private ResultDTO resultDTO;
    @Autowired
    QuestionBankDAO bankDAO;
    @Autowired
    MessageSource messageSource;
    @Autowired
    CommonService commonService;

    @Override
    public ResultDTO saveQuestionBank(QuestionBank questionBank, HttpServletRequest request, Authentication auth) {
        resultDTO = new ResultDTO();
        questionBank.setSys_reg_user_id(auth.getName());
        int resultId = bankDAO.saveQuestionBank(questionBank);
        if(resultId > 0) {
            int logId = commonService.saveLog(LogEntity.builder()
                    .table_id(questionBank.getId())
                    .page_nm("bank")
                    .url_addr(request.getRequestURI())
                    .state("insert")
                    .reg_user_id(auth.getName())
                    .build());

            resultDTO.setState(true);
            resultDTO.setCode(201);
            resultDTO.setMessage(messageSource.getMessage("api.message.201", new String[]{"문제은행"}, Locale.KOREA));
            resultDTO.setResult(questionBank.getId());
        } else {
            resultDTO.setState(false);
            resultDTO.setCode(400);
            resultDTO.setMessage(messageSource.getMessage("api.message.400", new String[]{"문제은행"}, Locale.KOREA));
            resultDTO.setResult(null);
        }

        return resultDTO;
    }

    @Override
    public ResultDTO updateQuestionBank(QuestionBank questionBank, HttpServletRequest request, Authentication auth) {
        resultDTO = new ResultDTO();
        questionBank.setSys_reg_user_id(auth.getName());
        int resultId = bankDAO.updateQuestionBank(questionBank);
        if(resultId > 0) {
            int logId = commonService.saveLog(LogEntity.builder()
                    .table_id(questionBank.getId())
                    .page_nm("bank")
                    .url_addr(request.getRequestURI())
                    .state("update")
                    .reg_user_id(auth.getName())
                    .build());

            resultDTO.setState(true);
            resultDTO.setCode(201);
            resultDTO.setMessage(messageSource.getMessage("api.message.202", new String[]{"문제은행", "수정"}, Locale.KOREA));
            resultDTO.setResult(questionBank.getId());
        } else {
            resultDTO.setState(false);
            resultDTO.setCode(400);
            resultDTO.setMessage(messageSource.getMessage("api.message.400", new String[]{"문제은행 수정"}, Locale.KOREA));
            resultDTO.setResult(null);
        }

        return resultDTO;
    }

    @Override
    public ResultDTO deleteQuestionBank(QuestionBank questionBank, HttpServletRequest request, Authentication auth) {
        resultDTO = new ResultDTO();
        questionBank.setSys_reg_user_id(auth.getName());
        int resultId = bankDAO.deleteQuestionBank(questionBank);
        if(resultId > 0) {
            int logId = commonService.saveLog(LogEntity.builder()
                    .table_id(questionBank.getId())
                    .page_nm("bank")
                    .url_addr(request.getRequestURI())
                    .state("delete")
                    .reg_user_id(auth.getName())
                    .build());

            resultDTO.setState(true);
            resultDTO.setCode(201);
            resultDTO.setMessage(messageSource.getMessage("api.message.202", new String[]{"문제은행", "삭제"}, Locale.KOREA));
            resultDTO.setResult(questionBank.getId());
        } else {
            resultDTO.setState(false);
            resultDTO.setCode(400);
            resultDTO.setMessage(messageSource.getMessage("api.message.400", new String[]{"문제은행 삭제"}, Locale.KOREA));
            resultDTO.setResult(null);
        }

        return resultDTO;
    }

    @Override
    public ResultDTO findQuestionBankList(QuestionBank questionBank, HttpServletRequest request, Authentication auth) {
        resultDTO = new ResultDTO();
        List<QuestionBankDTO> resultList = bankDAO.findQuestionBankList(questionBank);

        if(!DataLib.isEmpty(resultList)) {
//            int logId = commonService.saveLog(LogEntity.builder()
//                    .table_id(questionBank.getId())
//                    .page_nm("bank")
//                    .url_addr(request.getRequestURI())
//                    .state("view")
//                    .reg_user_id(auth.getName())
//                    .build());

            resultDTO.setState(true);
            resultDTO.setCode(200);
            resultDTO.setMessage(messageSource.getMessage("api.message.200", new String[]{"문제은행"}, Locale.KOREA));
            resultDTO.setResult(resultList);
        } else {
            resultDTO.setState(false);
            resultDTO.setCode(400);
            resultDTO.setMessage(messageSource.getMessage("api.message.400", new String[]{"문제은행"}, Locale.KOREA));
            resultDTO.setResult(null);
        }

        return resultDTO;
    }

    @Override
    public ResultDTO saveQbQuestion(QbQuestion question, HttpServletRequest request, Authentication auth) {
        resultDTO = new ResultDTO();
        question.setSys_reg_user_id(auth.getName());

        int resultId = 0;
        if(question.getId() > 0 ){
            resultId = bankDAO.deleteQbQuestion(question);
            bankDAO.deleteQbQItemList(question.getId());
        }else{
            resultId = bankDAO.saveQbQuestion(question);

        }

        if(resultId > 0) {
            int logId = commonService.saveLog(LogEntity.builder()
                    .table_id(question.getId())
                    .page_nm("bank")
                    .url_addr(request.getRequestURI())
                    .state("insert")
                    .reg_user_id(auth.getName())
                    .build());

            question.getAnswers().forEach(answer -> {
                answer.setSys_reg_user_id(auth.getName());
                answer.setQb_q_id(question.getId());
                int qbQItemId = bankDAO.saveQbQItem(answer);
            });

            resultDTO.setState(true);
            resultDTO.setCode(201);
            resultDTO.setMessage(messageSource.getMessage("api.message.201", new String[]{"문제"}, Locale.KOREA));
            resultDTO.setResult(question.getId());
        } else {
            resultDTO.setState(false);
            resultDTO.setCode(400);
            resultDTO.setMessage(messageSource.getMessage("api.message.400", new String[]{"문제"}, Locale.KOREA));
            resultDTO.setResult(null);
        }

        return resultDTO;
    }

    @Override
    public ResultDTO updateQbQuestion(QbQuestion question, HttpServletRequest request, Authentication auth) {
        resultDTO = new ResultDTO();
        question.setSys_reg_user_id(auth.getName());

        int resultId = 0;
        if(question.getId() > 0 ){
            resultId = bankDAO.deleteQbQuestion(question);
            bankDAO.deleteQbQItemList(question.getId());
        }else{
            resultId = bankDAO.saveQbQuestion(question);
        }

        if(resultId > 0) {
            int logId = commonService.saveLog(LogEntity.builder()
                    .table_id(question.getId())
                    .page_nm("bank")
                    .url_addr(request.getRequestURI())
                    .state("insert")
                    .reg_user_id(auth.getName())
                    .build());

            question.getAnswers().forEach(answer -> {
                answer.setSys_reg_user_id(auth.getName());
                answer.setQb_q_id(question.getId());
                int qbQItemId = bankDAO.updateQbQItem(answer);
            });

            resultDTO.setState(true);
            resultDTO.setCode(201);
            resultDTO.setMessage(messageSource.getMessage("api.message.201", new String[]{"문제"}, Locale.KOREA));
            resultDTO.setResult(question.getId());
        } else {
            resultDTO.setState(false);
            resultDTO.setCode(400);
            resultDTO.setMessage(messageSource.getMessage("api.message.400", new String[]{"문제"}, Locale.KOREA));
            resultDTO.setResult(null);
        }

        return resultDTO;
    }

    @Override
    public ResultDTO deleteQbQuestions(QuestionBank questionBank, HttpServletRequest request, Authentication auth) {
        resultDTO = new ResultDTO();
        int resultId = 0;
        for(QbQuestion question : questionBank.getQuestions()) {
            question.setSys_reg_user_id(auth.getName());
            question.setDelete_at('Y');
            resultId = bankDAO.deleteQbQuestion(question);

            if(resultId > 0) {
                int logId = commonService.saveLog(LogEntity.builder()
                        .table_id(question.getId())
                        .page_nm("bank")
                        .url_addr(request.getRequestURI())
                        .state("delete")
                        .reg_user_id(auth.getName())
                        .build());
            }
        }

        if(resultId > 0) {
            resultDTO.setState(true);
            resultDTO.setCode(201);
            resultDTO.setMessage(messageSource.getMessage("api.message.202", new String[]{"문제", "삭제"}, Locale.KOREA));
            resultDTO.setResult(null);
        } else {
            resultDTO.setState(false);
            resultDTO.setCode(400);
            resultDTO.setMessage(messageSource.getMessage("api.message.400", new String[]{"문제 삭제"}, Locale.KOREA));
            resultDTO.setResult(null);
        }

        return resultDTO;
    }

    @Override
    public ResultDTO findQbQuestionList(QbQuestion question, HttpServletRequest request, Authentication auth) {
        resultDTO = new ResultDTO();



        List<QbQuestion> resultList = bankDAO.findQbQuestionList(question);

        if(!DataLib.isEmpty(resultList)) {
//            int logId = commonService.saveLog(LogEntity.builder()
//                    .table_id(question.getId())
//                    .page_nm("bank")
//                    .url_addr(request.getRequestURI())
//                    .state("view")
//                    .reg_user_id(auth.getName())
//                    .build());

            for(QbQuestion qbQuestion : resultList) {
                List<QbQItem> itemList = bankDAO.findQbQItemList(qbQuestion.getId());
                if(!DataLib.isEmpty(itemList)) {
                    qbQuestion.setAnswers(itemList);
                }
            }

            resultDTO.setState(true);
            resultDTO.setCode(200);
            resultDTO.setMessage(messageSource.getMessage("api.message.200", new String[]{"문제"}, Locale.KOREA));
            resultDTO.setResult(resultList);
        } else {
            resultDTO.setState(false);
            resultDTO.setCode(400);
            resultDTO.setMessage(messageSource.getMessage("api.message.400", new String[]{"문제"}, Locale.KOREA));
            resultDTO.setResult(null);
        }

        return resultDTO;
    }
}
