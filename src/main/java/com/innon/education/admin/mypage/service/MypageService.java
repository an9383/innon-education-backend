package com.innon.education.admin.mypage.service;

import com.innon.education.admin.mypage.repository.model.Mypage;
import com.innon.education.code.controller.dto.ResultDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;

public interface MypageService {
    ResultDTO updateEduInsaInfo(Mypage mypage);
    ResultDTO findMyCurrentInfo(Mypage mypage, HttpServletRequest request, Authentication auth);

    ResultDTO findMySignInfo(Mypage mypage, HttpServletRequest request, Authentication auth);
    ResultDTO findMyEducationInfo(Mypage mypage, HttpServletRequest request, Authentication auth);
    ResultDTO findMyDocumentInfo(Mypage mypage, HttpServletRequest request, Authentication auth);
    ResultDTO findMyRequestSignInfo(Mypage mypage, HttpServletRequest request, Authentication auth);
}
