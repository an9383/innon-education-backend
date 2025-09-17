package com.innon.education.admin.board.dao;

import com.innon.education.admin.board.repository.dto.BoardDTO;
import com.innon.education.admin.board.repository.entity.BoardEntity;
import com.innon.education.admin.board.repository.entity.BoardSearchEntity;

import java.util.List;

public interface BoardDAO {
    int saveBoard(BoardEntity entity);
    List<BoardDTO> findBoardList(BoardSearchEntity entity);
    int updateBoard(BoardEntity entity);
    int deleteBoard(BoardEntity entity);
}
