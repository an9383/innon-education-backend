package com.innon.education.admin.board.service;

import com.innon.education.admin.board.repository.model.Qna;
import com.innon.education.admin.board.repository.model.QnaReply;
import com.innon.education.code.controller.dto.ResultDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;

public interface QnaService {
    ResultDTO saveQna(Qna qna, HttpServletRequest request, Authentication auth);
    ResultDTO findQnaList(Qna qna, HttpServletRequest request, Authentication auth);
    ResultDTO updateQna(Qna qna, HttpServletRequest request, Authentication auth);
    ResultDTO updateQnaWriteCnt(Qna qna, HttpServletRequest request, Authentication auth);
    ResultDTO deleteQna(Qna qna, HttpServletRequest request, Authentication auth);

    ResultDTO saveQnaReply(QnaReply qnaReply, HttpServletRequest request, Authentication auth);
    ResultDTO findQnaReplyList(QnaReply qnaReply, HttpServletRequest request, Authentication auth);
    ResultDTO updateQnaReply(QnaReply qnaReply, HttpServletRequest request, Authentication auth);
    ResultDTO deleteQnaReply(QnaReply qnaReply, HttpServletRequest request, Authentication auth);
}
