package com.innon.education.admin.board.dao.impl;

import com.innon.education.admin.board.dao.NoticeDAO;
import com.innon.education.admin.board.repository.dto.NoticeDTO;
import com.innon.education.admin.board.repository.entity.NoticeEntity;
import com.innon.education.admin.board.repository.entity.NoticeSearchEntity;
import com.innon.education.admin.board.repository.model.Notice;
import org.mybatis.spring.MyBatisSystemException;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class NoticeDAOImpl implements NoticeDAO {

    @Autowired
    SqlSessionTemplate sqlSession;

    @Override
    public int saveNotice(Notice notice) {
        try {
            return sqlSession.insert("com.innon.education.admin.mapper.notice-mapper.saveNotice", notice);
        } catch(MyBatisSystemException e) {
            throw new MyBatisSystemException(e.getCause());
        }
    }

    @Override
    public List<NoticeDTO> findNoticeList(Notice notice) {
        try {
            return sqlSession.selectList("com.innon.education.admin.mapper.notice-mapper.findNoticeList", notice);
        } catch(MyBatisSystemException e) {
            throw new MyBatisSystemException(e.getCause());
        }
    }

    @Override
    public int updateNotice(Notice notice) {
        try {
            return sqlSession.update("com.innon.education.admin.mapper.notice-mapper.updateNotice", notice);
        } catch(MyBatisSystemException e) {
            throw new MyBatisSystemException(e.getCause());
        }
    }

    @Override
    public int deleteNotice(Notice notice) {
        try {
            return sqlSession.update("com.innon.education.admin.mapper.notice-mapper.deleteNotice", notice);
        } catch(MyBatisSystemException e) {
            throw new MyBatisSystemException(e.getCause());
        }
    }
}
