package com.innon.education.admin.board.service.impl;

import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.innon.education.admin.board.dao.FaqDAO;
import com.innon.education.admin.board.repository.dto.FaqDTO;
import com.innon.education.admin.board.repository.model.Faq;
import com.innon.education.admin.board.service.FaqService;
import com.innon.education.common.repository.entity.LogEntity;
import com.innon.education.common.service.CommonService;
import com.innon.education.common.util.DataLib;
import com.innon.education.code.controller.dto.ResultDTO;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class FaqServiceImpl implements FaqService {

    private ResultDTO resultDTO;

    @Autowired
    FaqDAO faqDAO;
    @Autowired
    MessageSource messageSource;
    @Autowired
    CommonService commonService;

    @Override
    public ResultDTO saveFaq(Faq faq, HttpServletRequest request, Authentication auth) {
        resultDTO = new ResultDTO();
        faq.setSys_reg_user_id(auth.getName());
        int saveNum = faqDAO.saveFaq(faq);

        if(saveNum > 0) {
            int logId = commonService.saveLog(LogEntity.builder()
                    .table_id(faq.getId())
                    .page_nm("faq")
                    .url_addr(request.getRequestURI())
                    .state("insert")
                    .reg_user_id(auth.getName())
                    .build());

            resultDTO.setState(true);
            resultDTO.setCode(201);
            resultDTO.setMessage(messageSource.getMessage("api.message.201", new String[]{"FAQ"}, Locale.KOREA));
            resultDTO.setResult(faq.getId());
        } else {
            resultDTO.setState(false);
            resultDTO.setCode(400);
            resultDTO.setMessage(messageSource.getMessage("api.message.400", new String[]{"FAQ"}, Locale.KOREA));
            resultDTO.setResult(null);
        }

        return resultDTO;
    }

    @Override
    public ResultDTO findFaqList(Faq faq, HttpServletRequest request, Authentication auth) {
        resultDTO = new ResultDTO();
        List<FaqDTO> resultList = faqDAO.findFaqList(faq);

        if(!DataLib.isEmpty(resultList)) {
//            int logId = commonService.saveLog(LogEntity.builder()
//                    .table_id(faq.getId())
//                    .page_nm("faq")
//                    .url_addr(request.getRequestURI())
//                    .state("view")
//                    .reg_user_id(auth.getName())
//                    .build());

            resultDTO.setState(true);
            resultDTO.setCode(200);
            resultDTO.setMessage(messageSource.getMessage("api.message.200", new String[]{"FAQ"}, Locale.KOREA));
            resultDTO.setResult(resultList);
        } else {
            resultDTO.setState(false);
            resultDTO.setCode(400);
            resultDTO.setMessage(messageSource.getMessage("api.message.400", new String[]{"FAQ"}, Locale.KOREA));
            resultDTO.setResult(null);
        }

        return resultDTO;
    }

    @Override
    public ResultDTO updateFaq(Faq faq, HttpServletRequest request, Authentication auth) {
        resultDTO  = new ResultDTO();
        faq.setSys_reg_user_id(auth.getName());
        int updateNum = faqDAO.updateFaq(faq);

        if(updateNum > 0) {
            int logId = commonService.saveLog(LogEntity.builder()
                    .table_id(faq.getId())
                    .page_nm("faq")
                    .url_addr(request.getRequestURI())
                    .state("update")
                    .reg_user_id(auth.getName())
                    .build());

            resultDTO.setState(true);
            resultDTO.setCode(202);
            resultDTO.setMessage(messageSource.getMessage("api.message.202", new String[]{"FAQ", "수정"}, Locale.KOREA));
            resultDTO.setResult(faq.getId());
        } else {
            resultDTO.setState(false);
            resultDTO.setCode(400);
            resultDTO.setMessage(messageSource.getMessage("api.message.400", new String[]{"FAQ"}, Locale.KOREA));
            resultDTO.setResult(null);
        }

        return resultDTO;
    }

    @Override
    public ResultDTO deleteFaq(Faq faq, HttpServletRequest request, Authentication auth) {
        resultDTO  = new ResultDTO();
        faq.setSys_reg_user_id(auth.getName());
        int deleteNum = faqDAO.deleteFaq(faq);

        if(deleteNum > 0) {
            int logId = commonService.saveLog(LogEntity.builder()
                    .table_id(faq.getId())
                    .page_nm("faq")
                    .url_addr(request.getRequestURI())
                    .state("delete")
                    .reg_user_id(auth.getName())
                    .build());

            resultDTO.setState(true);
            resultDTO.setCode(202);
            resultDTO.setMessage(messageSource.getMessage("api.message.202", new String[]{"FAQ", "삭제"}, Locale.KOREA));
            resultDTO.setResult(faq.getId());
        } else {
            resultDTO.setState(false);
            resultDTO.setCode(400);
            resultDTO.setMessage(messageSource.getMessage("api.message.400", new String[]{"FAQ"}, Locale.KOREA));
            resultDTO.setResult(null);
        }

        return resultDTO;
    }
}
