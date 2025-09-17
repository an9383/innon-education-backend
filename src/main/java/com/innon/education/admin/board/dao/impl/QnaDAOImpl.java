package com.innon.education.admin.board.dao.impl;

import com.innon.education.admin.board.dao.QnaDAO;
import com.innon.education.admin.board.repository.dto.QnaDTO;
import com.innon.education.admin.board.repository.dto.QnaReplyDTO;
import com.innon.education.admin.board.repository.model.Qna;
import com.innon.education.admin.board.repository.model.QnaReply;
import org.mybatis.spring.MyBatisSystemException;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class QnaDAOImpl implements QnaDAO {

    @Autowired
    SqlSessionTemplate sqlSession;

    @Override
    public int saveQna(Qna qna) {
        try {
            return sqlSession.insert("com.innon.education.admin.mapper.qna-mapper.saveQna", qna);
        } catch(MyBatisSystemException e) {
            throw new MyBatisSystemException(e.getCause());
        }
    }

    @Override
    public List<QnaDTO> findQnaList(Qna qna) {
        try {
            return sqlSession.selectList("com.innon.education.admin.mapper.qna-mapper.findQnaList", qna);
        } catch(MyBatisSystemException e) {
            throw new MyBatisSystemException(e.getCause());
        }
    }

    @Override
    public int updateQna(Qna qna) {
        try {
            return sqlSession.update("com.innon.education.admin.mapper.qna-mapper.updateQna", qna);
        } catch(MyBatisSystemException e) {
            throw new MyBatisSystemException(e.getCause());
        }
    }

    @Override
    public int updateQnaWriteCnt(Qna qna) {
        try {
            return sqlSession.update("com.innon.education.admin.mapper.qna-mapper.updateQnaWriteCnt", qna);
        } catch(MyBatisSystemException e) {
            throw new MyBatisSystemException(e.getCause());
        }
    }

    @Override
    public int deleteQna(Qna qna) {
        try {
            return sqlSession.update("com.innon.education.admin.mapper.qna-mapper.deleteQna", qna);
        } catch(MyBatisSystemException e) {
            throw new MyBatisSystemException(e.getCause());
        }
    }

    @Override
    public int saveQnaReply(QnaReply qnaReply) {
        try {
            return sqlSession.insert("com.innon.education.admin.mapper.qna-mapper.saveQnaReply", qnaReply);
        } catch(MyBatisSystemException e) {
            throw new MyBatisSystemException(e.getCause());
        }
    }

    @Override
    public List<QnaReplyDTO> findQnaReplyList(int qnaId) {
        try {
            return sqlSession.selectList("com.innon.education.admin.mapper.qna-mapper.findQnaReplyList", qnaId);
        } catch(MyBatisSystemException e) {
            throw new MyBatisSystemException(e.getCause());
        }
    }

    @Override
    public int updateQnaReply(QnaReply qnaReply) {
        try {
            return sqlSession.update("com.innon.education.admin.mapper.qna-mapper.updateQnaReply", qnaReply);
        } catch(MyBatisSystemException e) {
            throw new MyBatisSystemException(e.getCause());
        }
    }

    @Override
    public int deleteQnaReply(QnaReply qnaReply) {
        try {
            return sqlSession.update("com.innon.education.admin.mapper.qna-mapper.deleteQnaReply", qnaReply);
        } catch(MyBatisSystemException e) {
            throw new MyBatisSystemException(e.getCause());
        }
    }
}
