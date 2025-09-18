package com.innon.education.admin.board.service.impl;

import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.innon.education.admin.board.dao.QnaDAO;
import com.innon.education.admin.board.repository.dto.QnaDTO;
import com.innon.education.admin.board.repository.dto.QnaReplyDTO;
import com.innon.education.admin.board.repository.model.Qna;
import com.innon.education.admin.board.repository.model.QnaReply;
import com.innon.education.admin.board.service.QnaService;
import com.innon.education.common.repository.entity.LogEntity;
import com.innon.education.common.service.CommonService;
import com.innon.education.common.util.DataLib;
import com.innon.education.code.controller.dto.ResultDTO;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class QnaServiceImpl implements QnaService {

    private ResultDTO resultDTO;

    @Autowired
    QnaDAO qnaDAO;
    @Autowired
    MessageSource messageSource;
    @Autowired
    CommonService commonService;

    @Override
    public ResultDTO saveQna(Qna qna, HttpServletRequest request, Authentication auth) {
        resultDTO = new ResultDTO();
        qna.setSys_reg_user_id(auth.getName());
        int saveNum = qnaDAO.saveQna(qna);

        if(saveNum > 0) {
            int logId = commonService.saveLog(LogEntity.builder()
                    .table_id(qna.getId())
                    .page_nm("qna")
                    .url_addr(request.getRequestURI())
                    .state("insert")
                    .reg_user_id(auth.getName())
                    .build());

            resultDTO.setState(true);
            resultDTO.setCode(201);
            resultDTO.setMessage(messageSource.getMessage("api.message.201", new String[]{"QnA"}, Locale.KOREA));
            resultDTO.setResult(qna.getId());
        } else {
            resultDTO.setState(false);
            resultDTO.setCode(400);
            resultDTO.setMessage(messageSource.getMessage("api.message.400", new String[]{"QnA"}, Locale.KOREA));
            resultDTO.setResult(null);
        }

        return resultDTO;
    }

    @Override
    public ResultDTO findQnaList(Qna qna, HttpServletRequest request, Authentication auth) {
        resultDTO = new ResultDTO();
        List<QnaDTO> resultList = qnaDAO.findQnaList(qna);

        if(!DataLib.isEmpty(resultList)) {
//            int logId = commonService.saveLog(LogEntity.builder()
//                    .table_id(qna.getId())
//                    .page_nm("qna")
//                    .url_addr(request.getRequestURI())
//                    .state("view")
//                    .reg_user_id(auth.getName())
//                    .build());

            resultDTO.setState(true);
            resultDTO.setCode(200);
            resultDTO.setMessage(messageSource.getMessage("api.message.200", new String[]{"QnA"}, Locale.KOREA));
            resultDTO.setResult(resultList);
        } else {
            resultDTO.setState(false);
            resultDTO.setCode(400);
            resultDTO.setMessage(messageSource.getMessage("api.message.400", new String[]{"QnA"}, Locale.KOREA));
            resultDTO.setResult(null);
        }

        return resultDTO;
    }

    @Override
    public ResultDTO updateQna(Qna qna, HttpServletRequest request, Authentication auth) {
        resultDTO = new ResultDTO();
        qna.setSys_reg_user_id(auth.getName());
        int updateNum = qnaDAO.updateQna(qna);

        if(updateNum > 0) {
            int logId = commonService.saveLog(LogEntity.builder()
                    .table_id(qna.getId())
                    .page_nm("qna")
                    .url_addr(request.getRequestURI())
                    .state("update")
                    .reg_user_id(auth.getName())
                    .build());

            resultDTO.setState(true);
            resultDTO.setCode(202);
            resultDTO.setMessage(messageSource.getMessage("api.message.202", new String[]{"QnA", "수정"}, Locale.KOREA));
            resultDTO.setResult(qna.getId());
        } else {
            resultDTO.setState(false);
            resultDTO.setCode(400);
            resultDTO.setMessage(messageSource.getMessage("api.message.400", new String[]{"QnA 수정"}, Locale.KOREA));
            resultDTO.setResult(null);
        }

        return resultDTO;
    }

    @Override
    public ResultDTO updateQnaWriteCnt(Qna qna, HttpServletRequest request, Authentication auth) {
        resultDTO = new ResultDTO();
        int updateNum = qnaDAO.updateQnaWriteCnt(qna);

        if(updateNum > 0) {
            resultDTO.setState(true);
            resultDTO.setCode(202);
            resultDTO.setMessage(messageSource.getMessage("api.message.202", new String[]{"QnA", "조회수"}, Locale.KOREA));
            resultDTO.setResult(qna.getId());
        } else {
            resultDTO.setState(false);
            resultDTO.setCode(400);
            resultDTO.setMessage(messageSource.getMessage("api.message.400", new String[]{"QnA 조회수"}, Locale.KOREA));
            resultDTO.setResult(null);
        }

        return resultDTO;
    }

    @Override
    public ResultDTO deleteQna(Qna qna, HttpServletRequest request, Authentication auth) {
        resultDTO = new ResultDTO();
        qna.setSys_reg_user_id(auth.getName());
        int deleteNum = qnaDAO.deleteQna(qna);

        if(deleteNum > 0) {
            int logId = commonService.saveLog(LogEntity.builder()
                    .table_id(qna.getId())
                    .page_nm("qna")
                    .url_addr(request.getRequestURI())
                    .state("delete")
                    .reg_user_id(auth.getName())
                    .build());

            QnaReply qnaReply = new QnaReply();
            qnaReply.setQna_id(qna.getId());
            qnaReply.setSys_reg_user_id(auth.getName());
            qnaDAO.deleteQnaReply(qnaReply);

            resultDTO.setState(true);
            resultDTO.setCode(202);
            resultDTO.setMessage(messageSource.getMessage("api.message.202", new String[]{"QnA", "삭제"}, Locale.KOREA));
            resultDTO.setResult(qna.getId());
        } else {
            resultDTO.setState(false);
            resultDTO.setCode(400);
            resultDTO.setMessage(messageSource.getMessage("api.message.400", new String[]{"QnA 삭제"}, Locale.KOREA));
            resultDTO.setResult(null);
        }

        return resultDTO;
    }

    @Override
    public ResultDTO saveQnaReply(QnaReply qnaReply, HttpServletRequest request, Authentication auth) {
        resultDTO = new ResultDTO();
        qnaReply.setSys_reg_user_id(auth.getName());
        int saveNum = qnaDAO.saveQnaReply(qnaReply);
        
        if(saveNum > 0) {
            int logId = commonService.saveLog(LogEntity.builder()
                    .table_id(qnaReply.getId())
                    .page_nm("qnaReply")
                    .url_addr(request.getRequestURI())
                    .state("insert")
                    .reg_user_id(auth.getName())
                    .build());

            resultDTO.setState(true);
            resultDTO.setCode(201);
            resultDTO.setMessage(messageSource.getMessage("api.message.201", new String[]{"QnA 댓글"}, Locale.KOREA));
            resultDTO.setResult(qnaReply.getId());
        } else {
            resultDTO.setState(false);
            resultDTO.setCode(400);
            resultDTO.setMessage(messageSource.getMessage("api.message.400", new String[]{"QnA 댓글"}, Locale.KOREA));
            resultDTO.setResult(null);
        }

        return resultDTO;
    }

    @Override
    public ResultDTO findQnaReplyList(QnaReply qnaReply, HttpServletRequest request, Authentication auth) {
        resultDTO = new ResultDTO();
        int qnaId = qnaReply.getQna_id();
        List<QnaReplyDTO> resultList = qnaDAO.findQnaReplyList(qnaId);

        if(!DataLib.isEmpty(resultList)) {
//            int logId = commonService.saveLog(LogEntity.builder()
//                    .table_id(qnaReply.getId())
//                    .page_nm("qnaReply")
//                    .url_addr(request.getRequestURI())
//                    .state("view")
//                    .reg_user_id(auth.getName())
//                    .build());

            resultDTO.setState(true);
            resultDTO.setCode(200);
            resultDTO.setMessage(messageSource.getMessage("api.message.200", new String[]{"QnA 댓글"}, Locale.KOREA));
            resultDTO.setResult(resultList);
        } else {
            resultDTO.setState(false);
            resultDTO.setCode(400);
            resultDTO.setMessage(messageSource.getMessage("api.message.400", new String[]{"QnA 댓글"}, Locale.KOREA));
            resultDTO.setResult(null);
        }

        return resultDTO;
    }

    @Override
    public ResultDTO updateQnaReply(QnaReply qnaReply, HttpServletRequest request, Authentication auth) {
        resultDTO = new ResultDTO();
        qnaReply.setSys_reg_user_id(auth.getName());
        int updateNum = qnaDAO.updateQnaReply(qnaReply);

        if(updateNum > 0) {
            int logId = commonService.saveLog(LogEntity.builder()
                    .table_id(qnaReply.getId())
                    .page_nm("qna")
                    .url_addr(request.getRequestURI())
                    .state("update")
                    .reg_user_id(auth.getName())
                    .build());

            resultDTO.setState(true);
            resultDTO.setCode(202);
            resultDTO.setMessage(messageSource.getMessage("api.message.202", new String[]{"QnA 댓글", "수정"}, Locale.KOREA));
            resultDTO.setResult(qnaReply.getId());
        } else {
            resultDTO.setState(false);
            resultDTO.setCode(400);
            resultDTO.setMessage(messageSource.getMessage("api.message.400", new String[]{"QnA 댓글 수정"}, Locale.KOREA));
            resultDTO.setResult(null);
        }

        return resultDTO;
    }

    @Override
    public ResultDTO deleteQnaReply(QnaReply qnaReply, HttpServletRequest request, Authentication auth) {
        resultDTO = new ResultDTO();
        qnaReply.setSys_reg_user_id(auth.getName());
        int deleteNum = qnaDAO.deleteQnaReply(qnaReply);

        if(deleteNum > 0) {
            int logId = commonService.saveLog(LogEntity.builder()
                    .table_id(qnaReply.getId())
                    .page_nm("qna")
                    .url_addr(request.getRequestURI())
                    .state("delete")
                    .reg_user_id(auth.getName())
                    .build());

            deleteChildren(qnaReply.getChildren(), auth.getName());

            resultDTO.setState(true);
            resultDTO.setCode(202);
            resultDTO.setMessage(messageSource.getMessage("api.message.202", new String[]{"QnA 댓글", "삭제"}, Locale.KOREA));
            resultDTO.setResult(qnaReply.getId());
        } else {
            resultDTO.setState(false);
            resultDTO.setCode(400);
            resultDTO.setMessage(messageSource.getMessage("api.message.400", new String[]{"QnA 댓글 삭제"}, Locale.KOREA));
            resultDTO.setResult(null);
        }

        return resultDTO;
    }

    private void deleteChildren(List<QnaReply> children, String user_id) {
        if(children == null) {
            return;
        }

        for(QnaReply child : children) {
            child.setSys_reg_user_id(user_id);
            int resultId = qnaDAO.deleteQnaReply(child);
            if(child.getChildren() != null) {
                deleteChildren(child.getChildren(), user_id);
            }
        }
    }
}
