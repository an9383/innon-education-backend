package com.innon.education.admin.jobtype;

import com.innon.education.admin.jobtype.repository.JobType;
import com.innon.education.admin.jobtype.service.JobTypeService;
import com.innon.education.code.controller.dto.ResultDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

@RestController
@RequestMapping("/api/common/jobType")
public class JobTypeController {
    @Autowired
    MessageSource messageSource;
    @Autowired
    JobTypeService jobTypeService;

    @PostMapping("/list")
    public ResponseEntity<ResultDTO> findAll(@RequestBody JobType jobType, HttpServletRequest request, Authentication auth) {
        ResultDTO res = new ResultDTO();
        try {
            res = jobTypeService.findJobTypeList(jobType, request, auth);
        } catch(Exception e) {
            System.out.println(e.getCause());
        }

        return ResponseEntity.ok().body(res);
    }

    @PostMapping("/save")
    public ResponseEntity<ResultDTO> save(@RequestBody JobType jobType, HttpServletRequest request, Authentication auth) {
        ResultDTO res = new ResultDTO();
        try {
            res = jobTypeService.saveJobType(jobType, request, auth);
        } catch(Exception e) {
            System.out.println(e.getCause());
            res.setState(false);
            res.setCode(400);
            res.setMessage(messageSource.getMessage("api.message.400", new String[]{"직무유형 등록"}, Locale.KOREA));
        }

        return ResponseEntity.ok().body(res);
    }

    @PostMapping("/list/dept")
    public ResponseEntity<ResultDTO> findJobTypeDeptList(@RequestBody JobType jobType, HttpServletRequest request, Authentication auth) {
        ResultDTO res = new ResultDTO();
        try {
            res = jobTypeService.findJobTypeDeptList(jobType, request, auth);
        } catch(Exception e) {
            System.out.println(e.getCause());
            res.setState(false);
            res.setCode(400);
            res.setMessage(messageSource.getMessage("api.message.400", new String[]{"직무유형"}, Locale.KOREA));
        }

        return ResponseEntity.ok(res);
    }

    @PostMapping("/list/group")
    public ResponseEntity<ResultDTO> getJobTypeList(@RequestBody JobType jobType, HttpServletRequest request, Authentication auth) {
        ResultDTO res = new ResultDTO();
        try {
            res = jobTypeService.findJobTypeDept(jobType, request, auth);
        } catch(Exception e) {
            System.out.println(e.getCause());
            res.setState(false);
            res.setCode(400);
            res.setMessage(messageSource.getMessage("api.message.400", new String[]{"직무유형 조회"}, Locale.KOREA));
        }

        return ResponseEntity.ok(res);
    }

    @PutMapping("/update")
    public ResponseEntity<ResultDTO> updateJobType(@RequestBody JobType jobType, HttpServletRequest request, Authentication auth) {
        ResultDTO res = new ResultDTO();
        try {
            res = jobTypeService.updateJobType(jobType, request, auth);
        } catch(Exception e) {
            System.out.println(e.getCause());
            res.setState(false);
            res.setCode(400);
            res.setMessage(messageSource.getMessage("api.message.400", new String[]{"직무유형 수정"}, Locale.KOREA));
        }

        return new ResponseEntity<>(res, HttpStatus.ACCEPTED);
    }
}
