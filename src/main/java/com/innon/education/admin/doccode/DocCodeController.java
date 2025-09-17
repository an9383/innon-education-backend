package com.innon.education.admin.doccode;

import com.innon.education.admin.doccode.repository.DocCode;
import com.innon.education.admin.doccode.service.DocCodeService;
import com.innon.education.controller.dto.ResultDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("/api/common/docCode")
public class DocCodeController {
    @Autowired
    MessageSource messageSource;
    @Autowired
    DocCodeService docCodeService;

    @PostMapping("/save")
    public ResponseEntity<ResultDTO> saveDocCode(@RequestBody DocCode docCode, HttpServletRequest request, Authentication auth) {
        ResultDTO res = new ResultDTO();
        try {
            res = docCodeService.saveDocCode(docCode, request, auth);
        } catch (Exception e) {
            System.out.println(e.getCause().toString());
            res.setState(false);
            res.setCode(400);
            res.setMessage(messageSource.getMessage("api.message.400", new String[]{"문서유형"}, Locale.KOREA));
        }

        return ResponseEntity.ok().body(res);
    }

    @PostMapping("/list")
    public ResponseEntity<ResultDTO> findDocCodeList(@RequestBody DocCode docCode, HttpServletRequest request, Authentication auth) {
        ResultDTO res = new ResultDTO();
        try {
            res = docCodeService.findDocCodeList(docCode, request, auth);
        } catch (Exception e) {
            System.out.println(e.getCause().toString());
            res.setState(false);
            res.setCode(400);
            res.setMessage(messageSource.getMessage("api.message.400", new String[]{"문서유형"}, Locale.KOREA));
        }

        return ResponseEntity.ok().body(res);
    }

    @PutMapping("/delete")
    public ResponseEntity<ResultDTO> deleteDocCode(@RequestBody List<DocCode> docCodeList, HttpServletRequest request, Authentication auth) {
        ResultDTO res = new ResultDTO();
        try {
            res = docCodeService.deleteDocCode(docCodeList, request, auth);
        } catch (Exception e) {
            System.out.println(e.getCause().toString());
            res.setState(false);
            res.setCode(400);
            res.setMessage(messageSource.getMessage("api.message.400", new String[]{"문서유형 삭제"}, Locale.KOREA));
        }

        return new ResponseEntity<>(res, HttpStatus.ACCEPTED);
    }

    @PutMapping("/update")
    public ResponseEntity<ResultDTO> updateDocCode(@RequestBody List<DocCode> docCodeList, HttpServletRequest request, Authentication auth) {
        ResultDTO res = new ResultDTO();
        try {
            res = docCodeService.updateDocCode(docCodeList, request, auth);
        } catch (Exception e) {
            System.out.println(e.getCause().toString());
            res.setState(false);
            res.setCode(400);
            res.setMessage(messageSource.getMessage("api.message.400", new String[]{"문서유형 수정"}, Locale.KOREA));
        }

        return new ResponseEntity<>(res, HttpStatus.ACCEPTED);
    }

    @PostMapping("/docnum")
    public ResponseEntity<ResultDTO> findDocNum(@RequestBody DocCode docCode, HttpServletRequest request, Authentication auth) {
        ResultDTO res = new ResultDTO();
        try {
            res = docCodeService.findDocNumByDocCode(docCode, request, auth);
        } catch (Exception e) {
            System.out.println(e.getCause().toString());
            res.setState(false);
            res.setCode(400);
            res.setMessage(messageSource.getMessage("api.message.400", new String[]{"문서유형 생성"}, Locale.KOREA));
        }

        return ResponseEntity.ok().body(res);
    }
}
