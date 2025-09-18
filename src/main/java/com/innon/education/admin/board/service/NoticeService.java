package com.innon.education.admin.board.service;

import com.innon.education.admin.board.repository.model.Notice;
import com.innon.education.code.controller.dto.ResultDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;

public interface NoticeService {
    ResultDTO saveNotice(Notice notice, HttpServletRequest request, Authentication auth);
    ResultDTO findNoticeList(Notice notice, HttpServletRequest request, Authentication auth);
    ResultDTO updateNotice(Notice notice, HttpServletRequest request, Authentication auth);
    ResultDTO deleteNotice(Notice notice, HttpServletRequest request, Authentication auth);
}
