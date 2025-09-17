package com.innon.education.admin.board.controller;

import com.innon.education.admin.board.repository.model.Notice;
import com.innon.education.admin.board.service.NoticeService;
import com.innon.education.controller.dto.ResultDTO;
import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api/admin/notice")
public class NoticeController {

    @Autowired
    private NoticeService noticeService;

    @PostMapping("/save")
    public ResponseEntity<ResultDTO> saveNotice(@RequestBody Notice notice, HttpServletRequest request, Authentication auth) {
        ResultDTO res = noticeService.saveNotice(notice, request, auth);
        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }

    @PostMapping("/list")
    public ResponseEntity<ResultDTO> findNoticeList(@RequestBody @Nullable Notice notice, HttpServletRequest request, Authentication auth) {
        ResultDTO res = noticeService.findNoticeList(notice, request, auth);
        return ResponseEntity.ok(res);
    }

    @PutMapping("/update")
    public ResponseEntity<ResultDTO> updateNotice(@RequestBody Notice notice, HttpServletRequest request, Authentication auth) {
        ResultDTO res = noticeService.updateNotice(notice, request, auth);
        return new ResponseEntity<>(res, HttpStatus.ACCEPTED);
    }

    @PutMapping("/delete")
    public ResponseEntity<ResultDTO> deleteNotice(@RequestBody Notice notice, HttpServletRequest request, Authentication auth) {
        ResultDTO res = noticeService.deleteNotice(notice, request, auth);
        return new ResponseEntity<>(res, HttpStatus.ACCEPTED);
    }
}
