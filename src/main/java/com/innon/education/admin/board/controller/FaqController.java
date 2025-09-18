package com.innon.education.admin.board.controller;

import com.innon.education.admin.board.repository.model.Faq;
import com.innon.education.admin.board.service.FaqService;
import com.innon.education.code.controller.dto.ResultDTO;
import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api/admin/faq")
public class FaqController {

    @Autowired
    private FaqService faqService;

    // 등록
    @PostMapping("/save")
    public ResponseEntity<ResultDTO> saveFaq(@RequestBody Faq faq, HttpServletRequest request, Authentication auth) {
        ResultDTO res = faqService.saveFaq(faq, request, auth);
        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }

    // 조회
    @PostMapping("/list")
    public ResponseEntity<ResultDTO> findFaqList(@RequestBody @Nullable Faq faq, HttpServletRequest request, Authentication auth) {
        ResultDTO res = faqService.findFaqList(faq, request, auth);
        return ResponseEntity.ok(res);
    }

    // 수정
    @PutMapping("/update")
    public ResponseEntity<ResultDTO> updateFaq(@RequestBody Faq faq, HttpServletRequest request, Authentication auth) {
        ResultDTO res = faqService.updateFaq(faq, request, auth);
        return new ResponseEntity<>(res, HttpStatus.ACCEPTED);
    }

    // 삭제
    @PutMapping("/delete")
    public ResponseEntity<ResultDTO> deleteFaq(@RequestBody Faq faq, HttpServletRequest request, Authentication auth) {
        ResultDTO res = faqService.deleteFaq(faq, request, auth);
        return new ResponseEntity<>(res, HttpStatus.ACCEPTED);
    }
}
