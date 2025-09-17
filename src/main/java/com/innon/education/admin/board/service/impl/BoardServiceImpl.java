package com.innon.education.admin.board.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.innon.education.admin.board.dao.BoardDAO;
import com.innon.education.admin.board.repository.dto.BoardDTO;
import com.innon.education.admin.board.repository.entity.BoardEntity;
import com.innon.education.admin.board.repository.entity.BoardSearchEntity;
import com.innon.education.admin.board.repository.model.Board;
import com.innon.education.admin.board.service.BoardService;
import com.innon.education.common.util.DataLib;
import com.innon.education.controller.dto.ResultDTO;

@Service
public class BoardServiceImpl implements BoardService {

    private ResultDTO resultDTO;

    @Autowired
    BoardDAO boardDAO;

    @Override
    public ResultDTO saveBoard(Board board) {
        resultDTO = new ResultDTO();
        BoardEntity entity = new BoardEntity(board);
        int saveNum = boardDAO.saveBoard(entity);

        if(saveNum > 0) {
            resultDTO.setMessage("게시글 등록이 완료되었습니다.");
        } else {
            resultDTO.setMessage("게시글 등록이 실패하였습니다.");
        }

        return resultDTO;
    }

    @Override
    public ResultDTO findBoardList(Board board) {
        resultDTO = new ResultDTO();
        BoardSearchEntity entity = new BoardSearchEntity(board);
        List<BoardDTO> resultList = boardDAO.findBoardList(entity);

        if(!DataLib.isEmpty(resultList)) {
            resultDTO.setState(true);
            resultDTO.setResult(resultList);
        } else {
            resultDTO.setState(false);
        }

        return resultDTO;
    }

    @Override
    public ResultDTO updateBoard(Board board) {
        resultDTO = new ResultDTO();
        BoardEntity entity = new BoardEntity(board);
        int updateNum = boardDAO.updateBoard(entity);
        if(updateNum > 0) {
            resultDTO.setMessage("게시글 수정이 완료되었습니다.");
        } else {
            resultDTO.setMessage("게시글 수정이 실패하였습니다.");
        }

        return resultDTO;
    }

    @Override
    public ResultDTO deleteBoard(Board board) {
        resultDTO = new ResultDTO();
        BoardEntity entity = new BoardEntity(board);
        int deleteNum = boardDAO.deleteBoard(entity);

        if(deleteNum > 0) {
            resultDTO.setMessage("게시글 삭제가 완료되었습니다.");
        } else {
            resultDTO.setMessage("게시글 삭제가 실패하엿습니다.");
        }

        return resultDTO;
    }
}
