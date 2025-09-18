package com.innon.education.library.document.controller;

import com.innon.education.code.controller.dto.ResultDTO;
import com.innon.education.library.document.repasitory.model.Document;
import com.innon.education.library.document.repasitory.model.DocumentMemo;
import com.innon.education.library.document.service.DocumentService;
import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;
import java.util.Map;
@Slf4j
@RestController
@RequestMapping("/api/library/document")
public class DocumentController {

    @Autowired
    DocumentService documentService;
    @Autowired
    MessageSource messageSource;

    /**
     * URL : /list
     * description : 문서현황 조회
     * param : Document
     * response : resultDTO
     */
    @PostMapping("/list")
    public ResponseEntity findDocumentList(@RequestBody @Nullable Document document, HttpServletRequest request, Authentication auth) {
        ResultDTO res = documentService.findDocumentList(document, request, auth);
        
        return ResponseEntity.ok().body(res);
    }

    /**
     * URL : /document_no
     * description : 문서번호 조회
     * param : Document
     * response : String
     */
    @PostMapping("/document_no")
    public ResponseEntity findDocument(@RequestBody @Nullable Document document, Authentication auth) {
        ResultDTO res = documentService.findDocumentNo(document, auth);
        return ResponseEntity.ok().body(res);
    }

    /**
     * URL : /save
     * description : 문서등록
     * param : Document
     * response : resultDTO
     */
    @PostMapping("/save")
    public ResponseEntity<ResultDTO> save(@RequestBody Document document, HttpServletRequest request, Authentication auth) {
        ResultDTO res = documentService.saveDocument(document, request, auth);
        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }

    /**
     * URL : /update
     * description : 문서수정
     * param : Document
     * response : resultDTO
     */
    @PostMapping("/update")
    public ResponseEntity<ResultDTO> update(@RequestBody Document document, HttpServletRequest request, Authentication auth) {
        ResultDTO res = documentService.updateDocument(document, request, auth);
        return new ResponseEntity<>(res, HttpStatus.ACCEPTED);
    }

    /**
     * URL : /loan/save
     * description : 문서신청
     * param : List<Document>
     * response : resultDTO
     */
    @PostMapping("/loan/save")
    public ResponseEntity<ResultDTO> applyDocument(@RequestBody List<Document> documentList, Authentication auth) {
        ResultDTO res = documentService.saveDocumentApply(documentList, auth);
        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }

    @PostMapping("/read/excel")
    public ResponseEntity<ResultDTO> readExcel() {
        ResultDTO res = documentService.readExcel();
        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }

    @PostMapping("/list/save")
    public ResponseEntity<ResultDTO> saveLibraryList(@RequestBody List<Map<String, Object>> insertList) {
        ResultDTO res = documentService.saveLibraryList(insertList);
        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }

    @PostMapping("/lab/save")
    public ResponseEntity<ResultDTO> saveLibrary(@RequestBody @Nullable List<Map<String, Object>> libraryList, HttpServletRequest request, Authentication auth) {
        ResultDTO res = documentService.saveLibrary(libraryList, request, auth);
        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }

    @PostMapping("/mywork/list")
    public ResponseEntity<ResultDTO> findApproveMyworkList(@RequestBody @Nullable Document document, Authentication auth) {

        // role 중에  if ROLE_ADMIN이 존재하면 plant_cd = > paramther 에서 넘어온 값으로 조회 else 내 plant_cd
        ResultDTO res = documentService.findApproveMyworkList(document, auth);


        return ResponseEntity.ok(res);
    }

    /**
     * URL : /loan/list
     * description : 문서현황
     * param : Document
     * response : resultDTO
     */
    @PostMapping("/loan/list")
    public ResponseEntity<ResultDTO> findDocumentLoanList(@RequestBody @Nullable Document document, Authentication auth) {
        ResultDTO res = documentService.findDocumentLoanList(document, auth);
        return ResponseEntity.ok(res);
    }

    @PostMapping("/form/save")
    public ResponseEntity<ResultDTO> saveDocumentform(@RequestBody DocumentMemo memo, HttpServletRequest request, Authentication auth) {
        ResultDTO res = new ResultDTO();
        try {
            res = documentService.saveDocumentForm(memo, request, auth);
            
        } catch(Exception e) {
            log.info(e.getCause().toString());
            res.setState(false);
            res.setCode(400);
            res.setMessage(messageSource.getMessage("api.message.400", new String[]{"문서신청"}, Locale.KOREA));
        }

        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }

    @PostMapping("/history")
    public ResponseEntity<ResultDTO> findDocumentHistoryList(@RequestBody DocumentMemo memo, HttpServletRequest request, Authentication auth) {
        ResultDTO res = new ResultDTO();
        try {
            res = documentService.findDocumentMemoList(memo, request, auth);
        } catch(Exception e) {
            log.info(e.getCause().toString());
            res.setState(false);
            res.setCode(400);
            res.setMessage(messageSource.getMessage("api.message.400", new String[]{"문서비고"}, Locale.KOREA));
        }
        return ResponseEntity.ok(res);
    }

    @PostMapping("/loan/update")
    public ResponseEntity<ResultDTO> updateDocumentLoan(@RequestBody Document document, HttpServletRequest request, Authentication auth) {
        ResultDTO res = new ResultDTO();
        try {
            res = documentService.updateDocumentLoan(document, request, auth);
        } catch(Exception e) {
            log.info(e.getCause().toString());
            res.setState(false);
            res.setCode(400);
            res.setMessage(messageSource.getMessage("api.message.400", new String[]{"대출관리"}, Locale.KOREA));
        }
        return new ResponseEntity<>(res, HttpStatus.ACCEPTED);
    }

    /**
     * 개발과정에서 데이터 삭제를 진행시키기 위한 API
     * @return
     */
    @DeleteMapping("/trash/delete")
    public ResponseEntity<ResultDTO> deleteTrashDocument() {
        ResultDTO res = documentService.deleteTrashDocument();
        return new ResponseEntity<>(res, HttpStatus.ACCEPTED);
    }

    /**
     * 개발과정에서 데이터 삭제를 진행시키기 위한 API
     * @return
     */
    @GetMapping("/trash/delete/all")
    public ResponseEntity<ResultDTO> deleteAllTrashDocument() {
        ResultDTO res = documentService.deleteAllTrashDocument();
        return new ResponseEntity<>(res, HttpStatus.ACCEPTED);
    }
}
