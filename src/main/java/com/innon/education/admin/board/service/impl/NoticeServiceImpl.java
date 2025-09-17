package com.innon.education.admin.board.service.impl;

import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.innon.education.admin.board.dao.NoticeDAO;
import com.innon.education.admin.board.repository.dto.NoticeDTO;
import com.innon.education.admin.board.repository.model.Notice;
import com.innon.education.admin.board.service.NoticeService;
import com.innon.education.common.repository.entity.LogEntity;
import com.innon.education.common.service.CommonService;
import com.innon.education.common.util.DataLib;
import com.innon.education.controller.dto.ResultDTO;

import io.jsonwebtoken.lang.Strings;
import jakarta.servlet.http.HttpServletRequest;

@Service
public class NoticeServiceImpl implements NoticeService {

    private ResultDTO resultDTO;

    @Autowired
    NoticeDAO noticeDAO;
    @Autowired
    MessageSource messageSource;
    @Autowired
    CommonService commonService;

    @Override
    public ResultDTO saveNotice(Notice notice, HttpServletRequest request, Authentication auth) {
        resultDTO = new ResultDTO();
        notice.setSys_reg_user_id(auth.getName());
        if(notice.getContent() == null) {
            notice.setContent(Strings.EMPTY);
        }
        int saveNum = noticeDAO.saveNotice(notice);

        if (saveNum > 0) {
            int logId = commonService.saveLog(LogEntity.builder()
                    .table_id(notice.getId())
                    .page_nm("notice")
                    .url_addr(request.getRequestURI())
                    .state("insert")
                    .reg_user_id(auth.getName())
                    .build());

            resultDTO.setState(true);
            resultDTO.setCode(201);
            resultDTO.setMessage(messageSource.getMessage("api.message.201", new String[]{"공지사항"}, Locale.KOREA));
            resultDTO.setResult(notice.getId());
        } else {
            resultDTO.setState(false);
            resultDTO.setCode(400);
            resultDTO.setMessage(messageSource.getMessage("api.message.400", new String[]{"공지사항"}, Locale.KOREA));
            resultDTO.setResult(null);
        }

        return resultDTO;
    }

    @Override
    public ResultDTO findNoticeList(Notice notice, HttpServletRequest request, Authentication auth) {
        resultDTO = new ResultDTO();
        List<NoticeDTO> resultList = noticeDAO.findNoticeList(notice);

        if(!DataLib.isEmpty(resultList)) {
//            int logId = commonService.saveLog(LogEntity.builder()
//                    .table_id(notice.getId())
//                    .page_nm("notice")
//                    .url_addr(request.getRequestURI())
//                    .state("view")
//                    .reg_user_id(auth.getName())
//                    .build());

            resultDTO.setState(true);
            resultDTO.setCode(200);
            resultDTO.setMessage(messageSource.getMessage("api.message.200", new String[]{"공지사항"}, Locale.KOREA));
            resultDTO.setResult(resultList);
        } else {
            resultDTO.setState(false);
            resultDTO.setCode(400);
            resultDTO.setMessage(messageSource.getMessage("api.message.400", new String[]{"공지사항"}, Locale.KOREA));
            resultDTO.setResult(null);
        }

        return resultDTO;
    }

    @Override
    public ResultDTO updateNotice(Notice notice, HttpServletRequest request, Authentication auth) {
        resultDTO = new ResultDTO();
        notice.setSys_reg_user_id(auth.getName());
        if(notice.getContent() == null) {
            notice.setContent(Strings.EMPTY);
        }
        int updateNum = noticeDAO.updateNotice(notice);

        if (updateNum > 0) {
            int logId = commonService.saveLog(LogEntity.builder()
                    .table_id(notice.getId())
                    .page_nm("notice")
                    .url_addr(request.getRequestURI())
                    .state("update")
                    .reg_user_id(auth.getName())
                    .build());

            resultDTO.setState(true);
            resultDTO.setCode(202);
            resultDTO.setMessage(messageSource.getMessage("api.message.202", new String[]{"공지사항", "수정"}, Locale.KOREA));
            resultDTO.setResult(notice.getId());
        } else {
            resultDTO.setState(false);
            resultDTO.setCode(400);
            resultDTO.setMessage(messageSource.getMessage("api.message.400", new String[]{"공지사항 수정"}, Locale.KOREA));
            resultDTO.setResult(null);
        }

        return resultDTO;
    }

    @Override
    public ResultDTO deleteNotice(Notice notice, HttpServletRequest request, Authentication auth) {
        resultDTO = new ResultDTO();
        notice.setSys_reg_user_id(auth.getName());
        int deleteNum = noticeDAO.deleteNotice(notice);

        if (deleteNum > 0) {
            int logId = commonService.saveLog(LogEntity.builder()
                    .table_id(notice.getId())
                    .page_nm("notice")
                    .url_addr(request.getRequestURI())
                    .state("delete")
                    .reg_user_id(auth.getName())
                    .build());

            resultDTO.setState(true);
            resultDTO.setCode(202);
            resultDTO.setMessage(messageSource.getMessage("api.message.202", new String[]{"공지사항", "삭제"}, Locale.KOREA));
            resultDTO.setResult(notice.getId());
        } else {
            resultDTO.setState(false);
            resultDTO.setCode(400);
            resultDTO.setMessage(messageSource.getMessage("api.message.400", new String[]{"공지사항 삭제"}, Locale.KOREA));
            resultDTO.setResult(null);
        }

        return resultDTO;
    }
}
