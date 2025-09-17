package com.innon.education.admin.board.controller;

import com.innon.education.admin.board.repository.model.Qna;
import com.innon.education.admin.board.repository.model.QnaReply;
import com.innon.education.admin.board.service.QnaService;
import com.innon.education.controller.dto.ResultDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api/admin/qna")
public class QnaController {

    @Autowired
    private QnaService qnaService;

    // Qna 등록
    @PostMapping("/save")
    public ResponseEntity<ResultDTO> save(@RequestBody Qna qna, HttpServletRequest request, Authentication auth) {
        ResultDTO res = qnaService.saveQna(qna, request, auth);
        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }

    // Qna 조회
    @PostMapping("/list")
    public ResponseEntity<ResultDTO> find(@RequestBody Qna qna, HttpServletRequest request, Authentication auth) {
        ResultDTO res = qnaService.findQnaList(qna, request, auth);
        return ResponseEntity.ok(res);
    }

    // Qna 수정
    @PutMapping("/update")
    public ResponseEntity<ResultDTO> update(@RequestBody Qna qna, HttpServletRequest request, Authentication auth) {
        ResultDTO res = qnaService.updateQna(qna, request, auth);
        return new ResponseEntity<>(res, HttpStatus.ACCEPTED);
    }

    // Qna 조회수 수정
    @PutMapping("/update/write")
    public ResponseEntity<ResultDTO> updateQnaWriteCnt(@RequestBody Qna qna, HttpServletRequest request, Authentication auth) {
        ResultDTO res = qnaService.updateQnaWriteCnt(qna, request, auth);
        return new ResponseEntity<>(res, HttpStatus.ACCEPTED);
    }

    // Qna 삭제
    @PutMapping("/delete")
    public ResponseEntity<ResultDTO> delete(@RequestBody Qna qna, HttpServletRequest request, Authentication auth) {
        ResultDTO res = qnaService.deleteQna(qna, request, auth);
        return new ResponseEntity<>(res, HttpStatus.ACCEPTED);
    }

    // Qna 댓글 등록
    @PostMapping("/reply/save")
    public ResponseEntity<ResultDTO> saveQnaReply(@RequestBody QnaReply qnaReply, HttpServletRequest request, Authentication auth) {
        ResultDTO res = qnaService.saveQnaReply(qnaReply, request, auth);
        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }

    // Qna 댓글 조회
    @PostMapping("/reply/list")
    public ResponseEntity<ResultDTO> findQnaReply(@RequestBody QnaReply qnaReply, HttpServletRequest request, Authentication auth) {
        ResultDTO res = qnaService.findQnaReplyList(qnaReply, request, auth);
        return ResponseEntity.ok(res);
    }

    // Qna 댓글 수정
    @PutMapping("/reply/update")
    public ResponseEntity<ResultDTO> updateQnaReply(@RequestBody QnaReply qnaReply, HttpServletRequest request, Authentication auth) {
        ResultDTO res = qnaService.updateQnaReply(qnaReply, request, auth);
        return new ResponseEntity<>(res, HttpStatus.ACCEPTED);
    }

    // Qna 댓글 삭제
    @PutMapping("/reply/delete")
    public ResponseEntity<ResultDTO> deleteQnaReply(@RequestBody QnaReply qnaReply, HttpServletRequest request, Authentication auth) {
        ResultDTO res = qnaService.deleteQnaReply(qnaReply, request, auth);
        return new ResponseEntity<>(res, HttpStatus.ACCEPTED);
    }
}
