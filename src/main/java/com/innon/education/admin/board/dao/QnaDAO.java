package com.innon.education.admin.board.dao;

import com.innon.education.admin.board.repository.dto.QnaDTO;
import com.innon.education.admin.board.repository.dto.QnaReplyDTO;
import com.innon.education.admin.board.repository.model.Qna;
import com.innon.education.admin.board.repository.model.QnaReply;

import java.util.List;

public interface QnaDAO {
    int saveQna(Qna qna);
    List<QnaDTO> findQnaList(Qna qna);
    int updateQna(Qna qna);
    int updateQnaWriteCnt(Qna qna);
    int deleteQna(Qna qna);

    int saveQnaReply(QnaReply qnaReply);
    List<QnaReplyDTO> findQnaReplyList(int qnaId);
    int updateQnaReply(QnaReply qnaReply);
    int deleteQnaReply(QnaReply qnaReply);
}
