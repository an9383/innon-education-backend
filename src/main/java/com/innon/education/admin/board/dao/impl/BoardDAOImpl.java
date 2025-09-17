package com.innon.education.admin.board.dao.impl;

import com.innon.education.admin.board.dao.BoardDAO;
import com.innon.education.admin.board.repository.dto.BoardDTO;
import com.innon.education.admin.board.repository.entity.BoardEntity;
import com.innon.education.admin.board.repository.entity.BoardSearchEntity;
import org.mybatis.spring.MyBatisSystemException;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BoardDAOImpl implements BoardDAO {

    @Autowired
    SqlSessionTemplate sqlSession;

    @Override
    public int saveBoard(BoardEntity entity) {
        try {
            return sqlSession.insert("com.innon.education.admin.mapper.board-mapper.saveBoard", entity);
        } catch(MyBatisSystemException e) {
            throw new MyBatisSystemException(e.getCause());
        }
    }

    @Override
    public List<BoardDTO> findBoardList(BoardSearchEntity entity) {
        try {
            return sqlSession.selectList("com.innon.education.admin.mapper.board-mapper.findBoardList", entity);
        } catch(MyBatisSystemException e) {
            throw new MyBatisSystemException(e.getCause());
        }
    }

    @Override
    public int updateBoard(BoardEntity entity) {
        try {
            return sqlSession.update("com.innon.education.admin.mapper.board-mapper.updateBoard", entity);
        } catch(MyBatisSystemException e) {
            throw new MyBatisSystemException(e.getCause());
        }
    }

    @Override
    public int deleteBoard(BoardEntity entity) {
        try {
            return sqlSession.update("com.innon.education.admin.mapper.board-mapper.deleteBoard", entity);
        } catch (MyBatisSystemException e) {
            throw new MyBatisSystemException(e.getCause());
        }
    }
}
