package com.innon.education.admin.board.dao;

import com.innon.education.admin.board.repository.dto.NoticeDTO;
import com.innon.education.admin.board.repository.entity.NoticeEntity;
import com.innon.education.admin.board.repository.entity.NoticeSearchEntity;
import com.innon.education.admin.board.repository.model.Notice;

import java.util.List;

public interface NoticeDAO {
    int saveNotice(Notice notice);
    List<NoticeDTO> findNoticeList(Notice notice);
    int updateNotice(Notice notice);
    int deleteNotice(Notice notice);
}
