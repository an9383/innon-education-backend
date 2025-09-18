package com.innon.education.code.controller;

import com.innon.education.code.controller.dto.ResultDTO;
import com.innon.education.common.repository.model.Code;
import com.innon.education.code.controller.service.CodeService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

@CrossOrigin
@RestController
@RequestMapping("/api/common/code")
public class CodeController {

    @Autowired
    CodeService codeService;
    @Autowired
    MessageSource messageSource;

    @GetMapping("/")
    public String dashboard() {
        return "Data 준비 중 ....";
    }

    @PostMapping("/list")
    public ResponseEntity<ResultDTO> findCodeList(@RequestBody Code code, HttpServletRequest request, Authentication auth) {
        ResultDTO res = new ResultDTO();
        try {
            res = codeService.findCodeList(code, request, auth);
        }catch (Exception e){
            System.out.println(e.getCause().toString());
            res.setState(false);
            res.setCode(400);
            res.setMessage(messageSource.getMessage("api.message.400", new String[]{"코드관리 조회"}, Locale.KOREA));
        }

        return ResponseEntity.ok().body(res);
    }

    @PostMapping("/save")
    public ResponseEntity<ResultDTO> saveCode(@RequestBody Code code, HttpServletRequest request, Authentication auth) {
        ResultDTO res = new ResultDTO();
        try {
            res = codeService.saveCode(code, request, auth);
        } catch (Exception e) {
            System.out.println(e.getCause());
            res.setState(false);
            res.setCode(400);
            res.setMessage(messageSource.getMessage("api.message.400", new String[]{"공통코드 등록"}, Locale.KOREA));
        }

        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }

    @PutMapping("/update")
    public ResponseEntity<ResultDTO> updateCode(@RequestBody Code code, HttpServletRequest request, Authentication auth){
        ResultDTO res = new ResultDTO();
        try {
            res = codeService.updateCode(code, request, auth);
        } catch (Exception e) {
            System.out.println(e.getCause());
            res.setState(false);
            res.setCode(400);
            res.setMessage(messageSource.getMessage("api.message.400", new String[]{"공통코드 수정"}, Locale.KOREA));
        }

        return new ResponseEntity<>(res, HttpStatus.ACCEPTED);
    }

    @PutMapping("/delete")
    public ResponseEntity<ResultDTO> deleteCode(@RequestBody Code code, HttpServletRequest request, Authentication auth){
        ResultDTO res = new ResultDTO();
        try {
            res = codeService.deleteCode(code, request, auth);
        } catch (Exception e) {
            System.out.println(e.getCause());
            res.setState(false);
            res.setCode(400);
            res.setMessage(messageSource.getMessage("api.message.400", new String[]{"공통코드 삭제"}, Locale.KOREA));
        }

        return new ResponseEntity<>(res, HttpStatus.ACCEPTED);
    }

    /*@PostMapping("/editById")
    public ResultDTO editById(@RequestBody CodeDTO codeDto){
        return codeService.editById(codeDto);
    }

    @DeleteMapping("/delete")
    public ResultDTO delete(@RequestParam("no") int no){
        return codeService.delete(no);
    }

    @PutMapping("/delete")
    public ResultDTO delete(@RequestBody CodeDTO codeDto, HttpServletRequest request, Authentication auth){
        System.out.println("dddd");
        return codeService.deleteCommonCode(codeDto, request, auth);
    }*/
}
