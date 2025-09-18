package com.innon.education.common.controller;

import com.innon.education.auth.entity.Role;
import com.innon.education.common.service.UserRoleService;
import com.innon.education.code.controller.dto.ResultDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/common/role")
public class UserRoleController {

    @Autowired
    MessageSource messageSource;
    @Autowired
    private UserRoleService userRoleService;

    @PostMapping("/list")
    public ResponseEntity<ResultDTO> findEduInsaList(@RequestBody Role role, Authentication auth) {
        //role.setSys_reg_user_id(authenticator.getName());
        ResultDTO res = userRoleService.findUserRoleList(role);
        return ResponseEntity.ok(res);
    }

    @PostMapping("/manage/list")
    public ResponseEntity<ResultDTO> roleManageList(@RequestBody Role role, Authentication auth) {
        //role.setSys_reg_user_id(authenticator.getName());
        ResultDTO res = userRoleService.roleManageList(role);
        return ResponseEntity.ok(res);
    }

    @PostMapping("/manage/user")
    public ResponseEntity<ResultDTO> roleUserList(@RequestBody Role role, Authentication auth) {
        //role.setSys_reg_user_id(authenticator.getName());
        ResultDTO res = userRoleService.roleUserList(role);
        return ResponseEntity.ok(res);
    }

    @PostMapping("/save")
    public ResponseEntity<ResultDTO> saveRole(@RequestBody Role role, HttpServletRequest request, Authentication auth) {


        ResultDTO res = userRoleService.saveRole(role, request, auth);
        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }


    @PutMapping("/update")
    public ResponseEntity<ResultDTO> updateRoleCodeList(@RequestBody Role role, HttpServletRequest request, Authentication auth) {
        ResultDTO res = userRoleService.updateRole(role, request, auth);
        return new ResponseEntity<>(res, HttpStatus.ACCEPTED);
    }

    @PutMapping("/delete")
    public ResponseEntity<ResultDTO> deleteRoleCodeList(@RequestBody Role role) {
        ResultDTO res = userRoleService.deleteRole(role);
        return new ResponseEntity<>(res, HttpStatus.ACCEPTED);
    }


}
