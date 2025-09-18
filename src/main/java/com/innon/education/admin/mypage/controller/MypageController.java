package com.innon.education.admin.mypage.controller;

import com.innon.education.admin.mypage.repository.model.Mypage;
import com.innon.education.admin.mypage.service.MypageService;
import com.innon.education.code.controller.dto.ResultDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api/admin/mypage")
public class MypageController {
	@Autowired
	private MypageService mypageService;

	@PutMapping("/update")
	public ResponseEntity<ResultDTO> updateEduInsaInfo(@RequestBody Mypage mypage) {
		ResultDTO res = mypageService.updateEduInsaInfo(mypage);
		return new ResponseEntity<>(res, HttpStatus.ACCEPTED);
	}

	@PostMapping("/current/list")
	public ResponseEntity<ResultDTO> findMyCurrentInfo(@RequestBody Mypage mypage, HttpServletRequest request, Authentication auth) {
		ResultDTO res = mypageService.findMyCurrentInfo(mypage, request, auth);
		return ResponseEntity.ok(res);
	}

	@PostMapping("/sign/list")
	public ResponseEntity<ResultDTO> findMySignInfo(@RequestBody Mypage mypage, HttpServletRequest request, Authentication auth) {
		//문서 > 마이페이지 > 승인요청
		ResultDTO res = mypageService.findMySignInfo(mypage, request, auth);
		return ResponseEntity.ok(res);
	}

	// 문서 > 마이페이지 > 신청이력
	@PostMapping("/request/sign")
	public ResponseEntity<ResultDTO> findMyRequestInfo(@RequestBody Mypage mypage, HttpServletRequest request, Authentication auth) {
		ResultDTO res = mypageService.findMyRequestSignInfo(mypage, request, auth);
		return ResponseEntity.ok(res);
	}

	@PostMapping("/education/list")
	public ResponseEntity<ResultDTO> findMyEducationInfo(@RequestBody Mypage mypage, HttpServletRequest request, Authentication auth) {
		ResultDTO res = mypageService.findMyEducationInfo(mypage, request, auth);
		return ResponseEntity.ok(res);
	}

	@PostMapping("/document/list")
	public ResponseEntity<ResultDTO> findMyDocumentInfo(@RequestBody Mypage mypage, HttpServletRequest request, Authentication auth) {
		ResultDTO res = mypageService.findMyDocumentInfo(mypage, request, auth);
		return ResponseEntity.ok(res);
	}
}
