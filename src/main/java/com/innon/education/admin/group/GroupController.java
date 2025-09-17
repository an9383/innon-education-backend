package com.innon.education.admin.group;

import com.innon.education.admin.group.repository.Group;
import com.innon.education.admin.group.service.GroupService;
import com.innon.education.controller.dto.ResultDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

@RestController
@RequestMapping("/api/common/group")
public class GroupController {
    @Autowired
    MessageSource messageSource;
    @Autowired
    GroupService groupService;

    @PostMapping("/list")
    public ResponseEntity<ResultDTO> findAll(@RequestBody Group group, HttpServletRequest request, Authentication auth) {
        ResultDTO res = new ResultDTO();
        try {
            res = groupService.findGroupList(group, request, auth);
        } catch(Exception e) {
            System.out.println(e.getCause());
        }

        return ResponseEntity.ok().body(res);
    }

    @PostMapping("/save")
    public ResponseEntity<ResultDTO> save(@RequestBody Group group, HttpServletRequest request, Authentication auth) {
        ResultDTO res = new ResultDTO();
        try {
            res = groupService.saveGroup(group, request, auth);
        } catch(Exception e) {
            System.out.println(e.getCause());
            res.setState(false);
            res.setCode(400);
            res.setMessage(messageSource.getMessage("api.message.400", new String[]{"그룹관리 등록"}, Locale.KOREA));
        }

        return ResponseEntity.ok().body(res);
    }

    @PostMapping("/list/dept")
    public ResponseEntity<ResultDTO> findGroupDeptList(@RequestBody Group group, HttpServletRequest request, Authentication auth) {
        ResultDTO res = new ResultDTO();
        try {
            res = groupService.findGroupDeptList(group, request, auth);
        } catch(Exception e) {
            System.out.println(e.getCause());
            res.setState(false);
            res.setCode(400);
            res.setMessage(messageSource.getMessage("api.message.400", new String[]{"부서"}, Locale.KOREA));
        }

        return ResponseEntity.ok(res);
    }

    @PostMapping("/list/group")
    public ResponseEntity<ResultDTO> getGroupList(@RequestBody Group group, HttpServletRequest request, Authentication auth) {
        ResultDTO res = new ResultDTO();
        try {
            res = groupService.findGroupDept(group, request, auth);
        } catch(Exception e) {
            System.out.println(e.getCause());
            res.setState(false);
            res.setCode(400);
            res.setMessage(messageSource.getMessage("api.message.400", new String[]{"그룹관리 조회"}, Locale.KOREA));
        }

        return ResponseEntity.ok(res);
    }

    @PutMapping("/update")
    public ResponseEntity<ResultDTO> updateGroup(@RequestBody Group group, HttpServletRequest request, Authentication auth) {
        ResultDTO res = new ResultDTO();
        try {
            res = groupService.updateGroup(group, request, auth);
        } catch(Exception e) {
            System.out.println(e.getCause());
            res.setState(false);
            res.setCode(400);
            res.setMessage(messageSource.getMessage("api.message.400", new String[]{"그룹관리 수정"}, Locale.KOREA));
        }

        return new ResponseEntity<>(res, HttpStatus.ACCEPTED);
    }
}
