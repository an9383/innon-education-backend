package com.innon.education.admin.board.service;

import com.innon.education.admin.board.repository.model.Board;
import com.innon.education.code.controller.dto.ResultDTO;

public interface BoardService {
    ResultDTO saveBoard(Board board);
    ResultDTO findBoardList(Board board);
    ResultDTO updateBoard(Board board);
    ResultDTO deleteBoard(Board board);
}
