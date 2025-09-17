package com.innon.education.service;

import com.innon.education.common.repository.model.Code;
import com.innon.education.controller.dto.ResultDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;

public interface CodeService {
    ResultDTO findCodeList(Code Code, HttpServletRequest request, Authentication auth);

    ResultDTO saveCode(Code code, HttpServletRequest request, Authentication auth);

    ResultDTO updateCode(Code code, HttpServletRequest request, Authentication auth);

    ResultDTO deleteCode(Code code, HttpServletRequest request, Authentication auth);

    /*ResultDTO editById(CodeDTO codeDto);

    ResultDTO delete(int no);*/
}
