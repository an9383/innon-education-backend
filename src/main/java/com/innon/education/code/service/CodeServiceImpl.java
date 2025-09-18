package com.innon.education.code.controller.service;

import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.innon.education.common.repository.dto.CodeDTO;
import com.innon.education.common.repository.entity.LogEntity;
import com.innon.education.common.repository.model.Code;
import com.innon.education.common.service.CommonService;
import com.innon.education.common.util.DataLib;
import com.innon.education.code.controller.dto.ResultDTO;
import com.innon.education.code.controller.dao.CodeDAO;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class CodeServiceImpl implements CodeService{

    private ResultDTO rDto;

    @Autowired
    CodeDAO codeDAO; 
    @Autowired
    MessageSource messageSource;
    @Autowired
    CommonService commonService;

    @Override
    public ResultDTO findCodeList(Code code, HttpServletRequest request, Authentication auth) {
        rDto = new ResultDTO();
        List<CodeDTO> resultList = codeDAO.findCodeList(code);
        if(!DataLib.isEmpty(resultList)){
//            int logId = commonService.saveLog(LogEntity.builder()
//                    .table_id(code.getId())
//                    .page_nm("code")
//                    .url_addr(request.getRequestURI())
//                    .state("view")
//                    .reg_user_id(auth.getName())
//                    .build());

            rDto.setState(true);
            rDto.setCode(200);
            rDto.setMessage(messageSource.getMessage("api.message.200", new String[]{"공통코드 조회"}, Locale.KOREA));
            rDto.setResult(resultList);
        } else {
            rDto.setState(false);
            rDto.setCode(400);
            rDto.setMessage(messageSource.getMessage("api.message.400", new String[]{"공통코드 조회"}, Locale.KOREA));
            rDto.setResult(null);
        }
        return rDto;
    }

    @Override
    public ResultDTO saveCode(Code code, HttpServletRequest request, Authentication auth) {
        rDto = new ResultDTO();
        code.setSys_reg_user_id(auth.getName());
        int resultId = codeDAO.saveCode(code);

        if(resultId > 0) {
            int logId = commonService.saveLog(LogEntity.builder()
                    .table_id(code.getId())
                    .page_nm("code")
                    .url_addr(request.getRequestURI())
                    .state("insert")
                    .reg_user_id(auth.getName())
                    .build());

            rDto.setState(true);
            rDto.setCode(201);
            rDto.setMessage(messageSource.getMessage("api.message.201", new String[]{"공통코드"}, Locale.KOREA));
            rDto.setResult(code.getId());
        } else {
            rDto.setState(false);
            rDto.setCode(400);
            rDto.setMessage(messageSource.getMessage("api.message.400", new String[]{"공통코드 등록"}, Locale.KOREA));
            rDto.setResult(null);
        }

        return rDto;
    }

    @Override
    public ResultDTO updateCode(Code code, HttpServletRequest request, Authentication auth) {
        rDto = new ResultDTO();
        try {
            code.setSys_upd_user_id(auth.getName());
            int resultId = codeDAO.updateCode(code);
            if(resultId > 0) {
                int logId = commonService.saveLog(LogEntity.builder()
                        .table_id(code.getId())
                        .page_nm("code")
                        .url_addr(request.getRequestURI())
                        .state("update")
                        .reg_user_id(auth.getName())
                        .build());
            }

            rDto.setState(true);
            rDto.setCode(202);
            rDto.setMessage(messageSource.getMessage("api.message.202", new String[]{"공통코드", "수정"}, Locale.KOREA));
            rDto.setResult(code.getId());
        } catch (Exception e) {
            rDto.setState(false);
            rDto.setCode(400);
            rDto.setMessage(messageSource.getMessage("api.message.400", new String[]{"공통코드 수정"}, Locale.KOREA));
            rDto.setResult(null);
        }

        return rDto;
    }

    @Override
    public ResultDTO deleteCode(Code code, HttpServletRequest request, Authentication auth) {
        rDto = new ResultDTO();

        try {
            int resultId = codeDAO.deleteCode(code);
            if(resultId > 0) {
                int logId = commonService.saveLog(LogEntity.builder()
                        .table_id(code.getId())
                        .page_nm("code")
                        .url_addr(request.getRequestURI())
                        .state("delete")
                        .reg_user_id(auth.getName())
                        .build());

                deleteChildren(code.getChildren());
            }

            rDto.setState(true);
            rDto.setCode(202);
            rDto.setMessage(messageSource.getMessage("api.message.202", new String[]{"공통코드", "삭제"}, Locale.KOREA));
            rDto.setResult(code.getId());
        } catch (Exception e) {
            rDto.setState(false);
            rDto.setCode(400);
            rDto.setMessage(messageSource.getMessage("api.message.400", new String[]{"공통코드 삭제"}, Locale.KOREA));
            rDto.setResult(null);
        }

        return rDto;
    }

    private void deleteChildren(List<Code> children) {
        if(children == null) {
            return;
        }

        for(Code child : children) {
            int resultId = codeDAO.deleteCode(child);
            if(child.getChildren() != null) {
                deleteChildren(child.getChildren());
            }
        }
    }


    /*public ResultDTO findAll(Code code) {
        rDto = new ResultDTO();
        List<CodeDTO> resultList = codeDAO.findAll(code);
        if(!DataLib.isEmpty(resultList)){
            rDto.setState(true);
            rDto.setResult(resultList);
        } else {
            rDto.setState(false);
        }
        return rDto;
    }

    @Override
    public ResultDTO editById(CodeDTO codeDto) {
        rDto = new ResultDTO();
        int state = codeDAO.editById(codeDto);
        if(state == 1) {
            rDto.setState(true);
            messageSource.getMessage("api.message.201", new String[]{"코드"}, Locale.KOREA);
        } else {
            rDto.setState(false);
            rDto.setMessage("코드 수정이 실패 하였습니다.");
        }
        return rDto;
    }

    @Override
    public ResultDTO delete(int no) {
        rDto = new ResultDTO();
        int state = codeDAO.delete(no);
        if(state == 1) {
            rDto.setState(true);
            rDto.setMessage("코드 삭제가 성공 하였습니다.");
        } else {
            rDto.setState(false);
            rDto.setMessage("코드 삭제가 실패 하였습니다.");
        }
        return rDto;
    }*/
}
