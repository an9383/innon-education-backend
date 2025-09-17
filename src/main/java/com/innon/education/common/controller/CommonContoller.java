package com.innon.education.common.controller;

import com.innon.education.auth.dto.request.LoginRequest;
import com.innon.education.common.repository.dto.MenuDTO;
import com.innon.education.common.repository.entity.LogEntity;
import com.innon.education.common.repository.model.Dept;
import com.innon.education.common.repository.model.Insa;
import com.innon.education.common.service.CommonService;
import com.innon.education.controller.dto.ResultDTO;
import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/api/common")
public class CommonContoller {

    @Autowired
    MessageSource messageSource;
    @Autowired
    private CommonService commonService;

    @PostMapping("/dept")
    public ResponseEntity<ResultDTO> findEduDeptList(@RequestBody @Nullable Dept dept,Authentication auth) {
        ResultDTO res = commonService.findEduDeptList(dept, auth);
        return ResponseEntity.ok(res);
    }

    @PostMapping("/dept/user")
    public ResponseEntity<ResultDTO> findEduDeptUserList(@RequestBody @Nullable Dept dept, Authentication auth) {
        ResultDTO res = commonService.findEduDeptUserList(dept,auth);
        return ResponseEntity.ok(res);
    }

    @PostMapping("/insa")
    public ResponseEntity<ResultDTO> findEduInsaList(@RequestBody @Nullable Insa insa, HttpServletRequest request, Authentication auth) {
//        ResultDTO res = commonService.findEduInsaList(insa);
        ResultDTO res = commonService.getEduInsaList(insa, request, auth);
        return ResponseEntity.ok(res);
    }

    @PostMapping("/popup/insa")
    public ResponseEntity<ResultDTO> findEduPopupInsaList(@RequestBody @Nullable Insa insa, HttpServletRequest request, Authentication auth) {
        ResultDTO res = commonService.getEduPopupInsaList(insa, request, auth);
        return ResponseEntity.ok(res);
    }

    @PostMapping("/factory")
    public ResponseEntity<ResultDTO> findFactoryCdByUserId(@RequestParam("user_id") String user_id) {
        ResultDTO res = commonService.findFactoryCdByUserId(user_id);
        return ResponseEntity.ok(res);
    }

    @PostMapping("/log/list")
    public ResponseEntity<ResultDTO> findLogList(@RequestBody LogEntity entity) {
        ResultDTO res = commonService.findLogList(entity);
        return ResponseEntity.ok(res);
    }

    @PostMapping("/insa/save")
    public ResponseEntity<ResultDTO> updateInsa(@RequestBody Insa insa, HttpServletRequest request, Authentication auth) {
        ResultDTO res = commonService.updateInsa(insa, request, auth);
        return new ResponseEntity<ResultDTO>(res, HttpStatus.ACCEPTED);
    }

    @PutMapping("/dept/update")
    public ResponseEntity<ResultDTO> updateDept(@RequestBody List<Dept> deptList, HttpServletRequest request, Authentication auth) {
        ResultDTO res = new ResultDTO();
        try {
            res = commonService.updateDept(deptList, request, auth);
        } catch (Exception e) {
            e.printStackTrace();
            res.setState(false);
            res.setCode(400);
            res.setMessage(messageSource.getMessage("api.message.400", new String[]{"부서정보 변경"}, Locale.KOREA));
        }

        return new ResponseEntity<>(res, HttpStatus.ACCEPTED);
    }

    @PostMapping("/check/session")
    public ResponseEntity<ResultDTO> checkSession(@RequestBody LoginRequest user, HttpServletRequest request, Authentication auth) {
        ResultDTO res = commonService.findCheckUserAuth(user, request, auth);
        return ResponseEntity.ok(res);
    }

    @PostMapping("/user_group")
    public ResponseEntity<ResultDTO> findGroupInfoByDeptCd(HttpServletRequest request, Authentication auth) {
        ResultDTO res = new ResultDTO();
        try {
            res = commonService.findGroupInfoByDeptCd(request, auth);
        } catch(Exception e) {
            System.out.println(e.getCause());
            res.setState(false);
            res.setCode(400);
            res.setMessage(messageSource.getMessage("api.message.400", new String[]{"그룹"}, Locale.KOREA));
        }

        return ResponseEntity.ok().body(res);
    }

    @PostMapping("/top/dept")
    public ResponseEntity<ResultDTO> findTopDeptByDeptCd(HttpServletRequest request, Authentication auth) {
        ResultDTO res = new ResultDTO();
        try {
            res = commonService.findTopDeptByDeptCd(request, auth);
        } catch(Exception e) {
            System.out.println(e.getCause());
            res.setState(false);
            res.setCode(400);
            res.setMessage(messageSource.getMessage("api.message.400", new String[]{"상위부서"}, Locale.KOREA));
        }

        return ResponseEntity.ok().body(res);
    }

    @PostMapping("/menu/list")
    public ResponseEntity<ResultDTO> findRoleMenuList(@RequestBody MenuDTO menu, HttpServletRequest request, Authentication auth) {
        ResultDTO res = new ResultDTO();
        try {
            res = commonService.findRoleMenuList(menu, request, auth);
        } catch(Exception e) {
            System.out.println(e.getCause());
            res.setState(false);
            res.setCode(400);
            res.setMessage(messageSource.getMessage("api.message.400", new String[]{"메뉴권한"}, Locale.KOREA));
        }

        return ResponseEntity.ok().body(res);
    }

    @PostMapping("/menu/save")
    public ResponseEntity<ResultDTO> saveMenuAuth(@RequestBody Map<String, Object> menuList, HttpServletRequest request, Authentication auth) {
        ResultDTO res = new ResultDTO();
        try {
            res = commonService.saveMenuAuth(menuList, request, auth);
        } catch(Exception e) {
            System.out.println(e.getCause());
            res.setState(false);
            res.setCode(400);
            res.setMessage(messageSource.getMessage("api.message.400", new String[]{"메뉴권한 등록"}, Locale.KOREA));
        }

        return ResponseEntity.ok().body(res);
    }

    @PostMapping("/file")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file, HttpServletRequest request, Authentication auth) {
        String uploadFolder = "C:\\test\\";
        try {
            File uploadDir = new File(uploadFolder);
            if(!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            String filePath = uploadFolder + file.getOriginalFilename();
            file.transferTo(new File(filePath));

            return ResponseEntity.ok(filePath);
        } catch(IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("파일 업로드 실패");
        }
    }
}
