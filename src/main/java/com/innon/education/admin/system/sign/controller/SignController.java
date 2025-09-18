package com.innon.education.admin.system.sign.controller;

import com.innon.education.admin.system.sign.repository.PlanSignManager;
import com.innon.education.admin.system.sign.repository.Sign;
import com.innon.education.admin.system.sign.service.SignService;
import com.innon.education.code.controller.dto.ResultDTO;
import com.innon.education.management.plan.dao.ManagementPlanDAO;
import com.innon.education.management.plan.repository.entity.PlanQmsEntity;
import com.innon.education.management.plan.repository.model.PlanQms;
import com.innon.education.management.progress.dao.ManagementProgressDAO;
import com.innon.education.management.result.dao.ManagementResultDAO;
import com.innon.education.new_education.dto.New_SearchDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

@RestController
@RequestMapping("/api/common/sign")
public class SignController {

    @Autowired
    SignService signService;
    @Autowired
    MessageSource messageSource;

    @Autowired
    ManagementPlanDAO planDAO;
    @Autowired
    ManagementResultDAO resultDAO;
    @Autowired
    ManagementProgressDAO progressDAO;

    @PostMapping("/list")
    public ResponseEntity findAll(@RequestBody Sign sign,HttpServletRequest request, Authentication auth){
        ResultDTO res = new ResultDTO();

        try {
            res = signService.findSignList(sign, request, auth);
        }catch (Exception e){
            System.out.println(e.getCause());
            res.setState(false);
            res.setCode(400);
            res.setMessage(messageSource.getMessage("api.message.400", new String[]{"전자결재 조회"}, Locale.KOREA));
        }

        return ResponseEntity.ok().body(res);
    }

    @PostMapping("/save")
    public ResponseEntity save(@RequestBody Sign sign, HttpServletRequest request, Authentication authentication){
        ResultDTO res = new ResultDTO();

        try {
            res = signService.saveSign(sign,request, authentication);
        }catch (Exception e){
            System.out.println(e.getCause());
            res.setState(false);
            res.setCode(400);
            res.setMessage(messageSource.getMessage("api.message.400", new String[]{"전자결재 등록"}, Locale.KOREA));
        }

        return ResponseEntity.ok().body(res);
    }

    @PostMapping("/user/list")
    public ResponseEntity findSignUserList(@RequestBody Sign sign, HttpServletRequest request, Authentication auth) {
        ResultDTO res = new ResultDTO();

        try {
            res = signService.findSignUserList(sign, request, auth);
        } catch (Exception e) {
            System.out.println(e.getCause().toString());
            res.setState(false);
            res.setCode(400);
            res.setMessage(messageSource.getMessage("api.message.400", new String[]{"결재권자 조회"}, Locale.KOREA));
        }

        return ResponseEntity.ok(res);
    }

    @PutMapping("/approve/update")
    public ResponseEntity<ResultDTO> updateSignApproveState(@RequestBody PlanSignManager signUser, HttpServletRequest request, Authentication auth) {
        ResultDTO res = new ResultDTO();

        try {
//            res = signService.updateSignApproveState(signUser, request, auth);
            res = signService.updatePlanSign(signUser, request, auth);
        } catch (Exception e) {
            System.out.println(e.getCause().toString());
            res.setState(false);
            res.setCode(400);
            res.setMessage(messageSource.getMessage("api.message.400", new String[]{"결재상태 수정"}, Locale.KOREA));
        }

        return new ResponseEntity<>(res, HttpStatus.ACCEPTED);
    }

    @PutMapping("/update")
    public ResponseEntity<ResultDTO> updateSign(@RequestBody Sign sign, HttpServletRequest request, Authentication auth) {
        ResultDTO res = new ResultDTO();

        try {
            res = signService.updateSign(sign, request, auth);
        } catch (Exception e) {
            System.out.println(e.getCause().toString());
            res.setState(false);
            res.setCode(400);
            res.setMessage(messageSource.getMessage("api.message.400", new String[]{"전자결재 수정"}, Locale.KOREA));
        }

        return new ResponseEntity<>(res, HttpStatus.ACCEPTED);
    }

    @PutMapping("/delete")
    public ResponseEntity<ResultDTO> deleteSign(@RequestBody Sign sign, HttpServletRequest request, Authentication auth) {
        ResultDTO res = new ResultDTO();

        try {
            res = signService.deleteSign(sign, request, auth);
        } catch (Exception e) {
            System.out.println(e.getCause());
            res.setState(false);
            res.setCode(400);
            res.setMessage(messageSource.getMessage("api.message.400", new String[]{"전자결재 삭제"}, Locale.KOREA));
        }

        return new ResponseEntity<>(res, HttpStatus.ACCEPTED);
    }

    @PostMapping("/manager/list")
    public ResponseEntity<ResultDTO> findSignApproveList(@RequestBody PlanSignManager signUser, HttpServletRequest request, Authentication auth) {
        ResultDTO res = new ResultDTO();

        try {
            res = signService.findApproverList(signUser, request, auth);
        } catch (Exception e) {
            System.out.println(e.getCause());
            res.setState(false);
            res.setCode(400);
            res.setMessage(messageSource.getMessage("api.message.400", new String[]{"결재권자 조회"}, Locale.KOREA));
        }

        return ResponseEntity.ok().body(res);
    }

    @PostMapping("/manager/user")
    public ResponseEntity<ResultDTO> findPlanSignManager(@RequestBody PlanSignManager planSignManager, HttpServletRequest request, Authentication auth) {
        ResultDTO res = new ResultDTO();

        try {
            res = signService.findPlanSignManager(planSignManager, request, auth);
        } catch (Exception e) {
            System.out.println(e.getCause());
            res.setState(false);
            res.setCode(400);
            res.setMessage(messageSource.getMessage("api.message.400", new String[]{"결재 승인자"}, Locale.KOREA));
        }

        return ResponseEntity.ok().body(res);
    }


    @PostMapping("/reponse")
    public ResultDTO sendQms(@RequestBody New_SearchDTO searchDTO){

        PlanQms planeeQms = new PlanQms();
        planeeQms.setPlan_id(searchDTO.getPlan_id());
        PlanQmsEntity planQmsEntity = new PlanQmsEntity(planeeQms);

        PlanQms planQms = resultDAO.findPlanQms(searchDTO.getPlan_id());
        resultDAO.saveQmsRes(planQms);

        ResultDTO resultDTO = new ResultDTO();
        return resultDTO;
    }


}
