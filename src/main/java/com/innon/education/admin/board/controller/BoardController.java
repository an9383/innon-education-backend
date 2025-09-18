package com.innon.education.admin.board.controller;

import com.innon.education.admin.board.repository.model.Board;
import com.innon.education.admin.board.service.BoardService;
import com.innon.education.code.controller.dto.ResultDTO;
import jakarta.annotation.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api/admin/board")
public class BoardController {

    @Autowired
    private BoardService boardService;

    // 등록
    @PostMapping("/save")
    public ResponseEntity<ResultDTO> saveBoard(@RequestBody Board board) {
        ResultDTO res = boardService.saveBoard(board);
        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }

    // 조회
    @PostMapping("/list")
    public ResponseEntity<ResultDTO> findBoardList(@RequestBody @Nullable Board board) {
        ResultDTO res = boardService.findBoardList(board);
        return ResponseEntity.ok(res);
    }

    // 수정
    @PutMapping("/update")
    public ResponseEntity<ResultDTO> updateBoard(@RequestBody Board board) {
        ResultDTO res = boardService.updateBoard(board);
        return new ResponseEntity<>(res, HttpStatus.ACCEPTED);
    }

    // 삭제
    @PutMapping("/delete")
    public ResponseEntity<ResultDTO> deleteBoard(@RequestBody Board board) {
        ResultDTO res = boardService.deleteBoard(board);
        return new ResponseEntity<>(res, HttpStatus.ACCEPTED);
    }
}
