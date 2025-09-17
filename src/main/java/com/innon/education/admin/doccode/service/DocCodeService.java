package com.innon.education.admin.doccode.service;

import com.innon.education.admin.doccode.repository.DocCode;
import com.innon.education.controller.dto.ResultDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface DocCodeService {
    ResultDTO saveDocCode(DocCode docCode, HttpServletRequest request, Authentication auth);
    ResultDTO findDocCodeList(DocCode docCode, HttpServletRequest request, Authentication auth);
    ResultDTO deleteDocCode(List<DocCode> docCodeList, HttpServletRequest request, Authentication auth);
    ResultDTO updateDocCode(List<DocCode> docCodeList, HttpServletRequest request, Authentication auth);
    ResultDTO findDocNumByDocCode(DocCode docCode, HttpServletRequest request, Authentication auth);
}
