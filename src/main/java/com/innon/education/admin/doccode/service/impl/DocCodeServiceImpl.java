package com.innon.education.admin.doccode.service.impl;

import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.innon.education.admin.doccode.dao.DocCodeDAO;
import com.innon.education.admin.doccode.repository.DocCode;
import com.innon.education.admin.doccode.repository.DocCodeDTO;
import com.innon.education.admin.doccode.service.DocCodeService;
import com.innon.education.auth.entity.User;
import com.innon.education.common.dao.CommonDAO;
import com.innon.education.common.repository.entity.LogEntity;
import com.innon.education.common.service.CommonService;
import com.innon.education.common.util.DataLib;
import com.innon.education.code.controller.dto.ResultDTO;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class DocCodeServiceImpl implements DocCodeService {
    private ResultDTO resultDTO;
    @Autowired
    DocCodeDAO docCodeDAO;
    @Autowired
    MessageSource messageSource;
    @Autowired
    CommonService commonService;
    @Autowired
    CommonDAO commonDAO;

    @Override
    public ResultDTO saveDocCode(DocCode docCode, HttpServletRequest request, Authentication auth) {
        resultDTO = new ResultDTO();
        User user = (User) auth.getPrincipal();

        docCode.setSys_reg_user_id(auth.getName());
        docCode.setDept_cd(user.getDept_cd());
        int resultId = docCodeDAO.saveDocCode(docCode);
        if (resultId > 0) {
            int logId = commonService.saveLog(LogEntity.builder()
                    .table_id(docCode.getId())
                    .page_nm("docCode")
                    .url_addr(request.getRequestURI())
                    .state("insert")
                    .reg_user_id(auth.getName())
                    .build());

            try {

//                    Object obj=sign;
//                    for (Field field : obj.getClass().getDeclaredFields()){
//                        field.setAccessible(true);
//                        Object value=field.get(obj);
//                        resultDTO = commonService.saveLogDetail(LogChild
//                                .builder()
//                                .log_id(logId)
//                                .field(field.getName())
//                                .new_value(value.toString())
//                                .reg_user_id(authentication.getName())
//                                .build());
//                    }

            } catch (Exception e) {
                e.printStackTrace();
            }

            resultDTO.setState(true);
            resultDTO.setCode(201);
            resultDTO.setMessage(messageSource.getMessage("api.message.201", new String[]{"문서유형"}, Locale.KOREA));
            resultDTO.setResult(docCode.getId());
        } else {
            resultDTO.setState(false);
            resultDTO.setCode(400);
            resultDTO.setMessage(messageSource.getMessage("api.message.400", new String[]{"문서유형"}, Locale.KOREA));
            resultDTO.setResult(null);
        }

        return resultDTO;
    }

    @Override
    public ResultDTO findDocCodeList(DocCode docCode, HttpServletRequest request, Authentication auth) {
        resultDTO = new ResultDTO();
        User user = (User) auth.getPrincipal();
        boolean chkUserRole = auth.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
        if(!chkUserRole) {
            docCode.setGroupList(commonDAO.findGroupInfoByDeptCd(user.getDept_cd()));
        }
        List<DocCodeDTO> docCodeList = docCodeDAO.findDocCodeList(docCode);
        if(!DataLib.isEmpty(docCodeList)) {
//            int logId = commonService.saveLog(LogEntity.builder()
//                    .table_id(docCode.getId())
//                    .page_nm("docCode")
//                    .url_addr(request.getRequestURI())
//                    .state("view")
//                    .reg_user_id(auth.getName())
//                    .build());

            resultDTO.setState(true);
            resultDTO.setCode(200);
            resultDTO.setMessage(messageSource.getMessage("api.message.200", new String[]{"문서유형"}, Locale.KOREA));
            resultDTO.setResult(docCodeList);
        } else {
            resultDTO.setState(false);
            resultDTO.setCode(400);
            resultDTO.setMessage(messageSource.getMessage("api.message.400", new String[]{"문서유형"}, Locale.KOREA));
            resultDTO.setResult(null);
        }

        return resultDTO;
    }

    @Override
    public ResultDTO deleteDocCode(List<DocCode> docCodeList, HttpServletRequest request, Authentication auth) {
        resultDTO = new ResultDTO();
        try {
            for (DocCode docCode : docCodeList) {
                int resultId = docCodeDAO.deleteDocCode(docCode);
                if (resultId > 0) {
                    int logId = commonService.saveLog(LogEntity.builder()
                            .table_id(docCode.getId())
                            .page_nm("docCode")
                            .url_addr(request.getRequestURI())
                            .state("delete")
                            .reg_user_id(auth.getName())
                            .build());
                }
            }

            resultDTO.setState(true);
            resultDTO.setCode(202);
            resultDTO.setMessage(messageSource.getMessage("api.message.202", new String[]{"문서유형", "삭제"}, Locale.KOREA));
            resultDTO.setResult(docCodeList.get(0).getId());
        } catch (Exception e) {
            resultDTO.setState(false);
            resultDTO.setCode(400);
            resultDTO.setMessage(messageSource.getMessage("api.message.400", new String[]{"문서유형"}, Locale.KOREA));
            resultDTO.setResult(null);
        }

        return resultDTO;
    }

    @Override
    public ResultDTO updateDocCode(List<DocCode> docCodeList, HttpServletRequest request, Authentication auth) {
        resultDTO = new ResultDTO();
        try {
            for (DocCode docCode : docCodeList) {
                docCode.setSys_upd_user_id(auth.getName());
                int resultId = docCodeDAO.updateDocCode(docCode);
                if (resultId > 0) {
                    int logId = commonService.saveLog(LogEntity.builder()
                            .table_id(docCode.getId())
                            .page_nm("docCode")
                            .url_addr(request.getRequestURI())
                            .state("delete")
                            .reg_user_id(auth.getName())
                            .build());
                }
            }

            resultDTO.setState(true);
            resultDTO.setCode(202);
            resultDTO.setMessage(messageSource.getMessage("api.message.202", new String[]{"문서유형", "수정"}, Locale.KOREA));
            resultDTO.setResult(docCodeList.get(0).getId());
        } catch (Exception e) {
            resultDTO.setState(false);
            resultDTO.setCode(400);
            resultDTO.setMessage(messageSource.getMessage("api.message.400", new String[]{"문서유형"}, Locale.KOREA));
            resultDTO.setResult(null);
        }

        return resultDTO;
    }

    @Override
    public ResultDTO findDocNumByDocCode(DocCode docCode, HttpServletRequest request, Authentication auth) {
        resultDTO = new ResultDTO();
        String docNum = docCodeDAO.findDocNumByDocCode(docCode);
        if(docNum != null) {
            resultDTO.setState(true);
            resultDTO.setCode(200);
            resultDTO.setMessage(messageSource.getMessage("api.message.200", new String[]{"문서유형"}, Locale.KOREA));
            resultDTO.setResult(docNum);
        } else {
            resultDTO.setState(false);
            resultDTO.setCode(400);
            resultDTO.setMessage(messageSource.getMessage("api.message.400", new String[]{"문서유형"}, Locale.KOREA));
            resultDTO.setResult(null);
        }

        return resultDTO;
    }
}
