package com.innon.education.admin.manage;

import com.innon.education.admin.manage.service.ManagerService;
import com.innon.education.code.controller.dto.ResultDTO;
import jakarta.servlet.http.HttpServletRequest;
import com.innon.education.admin.manage.repository.Manager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

@RestController
@RequestMapping("/api/common/manager")
public class ManageController {
    @Autowired
    MessageSource messageSource;
    @Autowired
    ManagerService managerService;

    @PostMapping("/list")
    public ResponseEntity findAll(@RequestBody Manager manager, HttpServletRequest request, Authentication authentication){
        ResultDTO res = new ResultDTO();
        try {
            res = managerService.findManagerList(manager, request, authentication);
        }catch (Exception e){
            System.out.println(e.getCause());
        }

        return ResponseEntity.ok().body(res);
    }

    @PostMapping("/save")
    public ResponseEntity save(@RequestBody Manager manager, HttpServletRequest request, Authentication authentication){
        ResultDTO res = new ResultDTO();
        try {
            res = managerService.saveManager(manager,request, authentication);
        }catch (Exception e){
            System.out.println(e.getCause());
            res.setState(false);
            res.setCode(400);
            res.setMessage(messageSource.getMessage("api.message.400", new String[]{"담당자 등록"}, Locale.KOREA));
        }

        return ResponseEntity.ok().body(res);
    }

    @PostMapping("/list/user")
    public ResponseEntity findManagerUserList(@RequestBody Manager manager, HttpServletRequest request, Authentication auth) {
        ResultDTO res = new ResultDTO();
        try {
//            User user = (User) auth.getPrincipal();
//            manager.setPlant_cd(user.getPlant_cd());

            res = managerService.findManagerUserList(manager, request, auth);
        } catch (Exception e) {
            System.out.println(e.getCause());
            res.setState(false);
            res.setCode(400);
            res.setMessage(messageSource.getMessage("api.message.400", new String[]{"담당자 조회"}, Locale.KOREA));
        }
        return ResponseEntity.ok(res);
    }
    @PostMapping("/list/dept")
    public ResponseEntity getManagerList(@RequestBody Manager manager, HttpServletRequest request, Authentication auth) {
        ResultDTO res = new ResultDTO();
        try {
            res = managerService.findManagerDept(manager, request, auth);
        } catch (Exception e) {
            System.out.println(e.getCause());
            res.setState(false);
            res.setCode(400);
            res.setMessage(messageSource.getMessage("api.message.400", new String[]{"담당자 조회"}, Locale.KOREA));
        }
        return ResponseEntity.ok(res);
    }
    @PutMapping("/update")
    public ResponseEntity<ResultDTO> updateManager (@RequestBody Manager manager, HttpServletRequest request, Authentication auth) {
        ResultDTO res = new ResultDTO();
        try {
            res = managerService.updateManager(manager, request, auth);
        } catch (Exception e) {
     
            System.out.println(e.getCause());
            res.setState(false);
            res.setCode(400);
            res.setMessage(messageSource.getMessage("api.message.400", new String[]{"담당자 수정"}, Locale.KOREA));

        }

        return new ResponseEntity<>(res, HttpStatus.ACCEPTED);
    }

}
