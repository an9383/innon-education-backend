package com.innon.education.management.plan.controller;

import java.util.List;
import java.util.Map;

import com.innon.education.controller.dto.ResultDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.innon.education.admin.group.repository.Group;
import com.innon.education.auth.entity.User;
import com.innon.education.common.dao.CommonDAO;
import com.innon.education.common.util.DataLib;
import com.innon.education.library.document.repasitory.dto.DocumentDTO;
import com.innon.education.library.document.repasitory.dto.PlanDocument;
import com.innon.education.management.plan.dao.ManagementPlanDAO;
import com.innon.education.management.plan.repository.dto.ManagementPlanUserDTO;
import com.innon.education.management.plan.repository.entity.ManagementPlanUserEntity;
import com.innon.education.management.plan.repository.model.EducationPlanContent;
import com.innon.education.management.plan.repository.model.EducationPlanLab;
import com.innon.education.management.plan.repository.model.ManagementPlan;
import com.innon.education.management.plan.repository.model.PlanQms;
import com.innon.education.management.plan.repository.model.QuestionInfo;
import com.innon.education.management.plan.repository.model.UserEduCurrent;
import com.innon.education.management.plan.service.ManagementPlanService;
import com.innon.education.new_education.dto.New_SearchDTO;

import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/api/education/management/plan")
public class ManagementPlanController {

    @Autowired
    ManagementPlanService managementPlanService;

    @Autowired
    CommonDAO commonDAO;
    @Autowired
    ManagementPlanDAO planDAO;

    /**
     * URL : /save
     * description : 교육계획 등록
     * param : plan
     * response : resultDTO
     */
    @Transactional
    @PostMapping("/save")
    public ResponseEntity<ResultDTO> saveManagementPlan(@RequestBody ManagementPlan plan, HttpServletRequest request, Authentication auth) {
        ResultDTO res = new ResultDTO();
        try {

            res = managementPlanService.savePlan(plan, request, auth);
            if(res.getResult() != null) {
                int plan_id = Integer.parseInt(res.getResult().toString());
                managementPlanService.savePlanUser(plan,  (Integer) res.getResult(), request, auth);
                managementPlanService.savePlanContent(plan,  (Integer) res.getResult(), request, auth);
                if(plan.getEvaluation_type().equals("edue11001")){
                    managementPlanService.saveQuestionInfo(plan,  (Integer) res.getResult(), request, auth);
                }
                managementPlanService.savePlanDocument(plan,  (Integer) res.getResult(), request, auth);
            }
        } catch (Exception e) {
            log.error(e.getCause().toString());
            res.setCode(400);
            res.setMessage("교육계획 등록 실패");
        }

        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }

    /**
     * URL : /update
     * description : 교육계획 등록
     * param : ManagementPlan.java
     * response : resultDTO
     */
    @Transactional
    @PostMapping("/update")
    public ResponseEntity<ResultDTO> updateManagementPlan(@RequestBody ManagementPlan plan, HttpServletRequest request, Authentication auth) {
        ResultDTO res = new ResultDTO();
        try {

            res = managementPlanService.updatePlan(plan, request, auth);
            if(res.getResult() != null) {
                int plan_id = Integer.parseInt(res.getResult().toString());

                ManagementPlanUserEntity delPlanUser = new ManagementPlanUserEntity();
                delPlanUser.setDelete_at('Y');
                delPlanUser.setPlan_id(plan.getId());
                managementPlanService.updatePlanUser(delPlanUser);
                managementPlanService.savePlanUser(plan,  (Integer) res.getResult(), request, auth);
                EducationPlanContent delContent = new EducationPlanContent();
                delContent.setDelete_at('Y');
                delContent.setPlan_id(plan.getId());
                managementPlanService.updatePlanContent(delContent);
                managementPlanService.savePlanContent(plan,  (Integer) res.getResult(), request, auth);
                if(plan.getEvaluation_type().equals("edue11001")){

                    QuestionInfo delQInfo = new QuestionInfo();
                    delQInfo.setPlan_id(plan.getId());
                    delQInfo.setDelete_at('Y');
                    managementPlanService.updateQuestionInfo(delQInfo);
                    managementPlanService.saveQuestionInfo(plan,  (Integer) res.getResult(), request, auth);
                }
                PlanDocument delPlanDocument = new PlanDocument();
                delPlanDocument.setDelete_at('Y');
                delPlanDocument.setPlan_id(plan.getId());
                managementPlanService.updatePlanDocument(delPlanDocument);
                managementPlanService.savePlanDocument(plan,  (Integer) res.getResult(), request, auth);
            }
        } catch (Exception e) {
            log.error(e.getCause().toString());
            res.setCode(400);
            res.setMessage("교육계획 수정 실패");
        }
        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }

    /**
     * URL : /list
     * description : 교육진행 조회
     * param : plan
     * response : resultDTO
     */
    @PostMapping("/list")
    public ResponseEntity<ResultDTO> managementPlanList(@RequestBody @Nullable ManagementPlan plan, Authentication auth) {

        User user = (User) auth.getPrincipal();
        if(plan.getGroup_id() == 0){
            List<Group> groupList = commonDAO.findGroupInfoByDeptCd(user.getDept_cd());

            if(!DataLib.isEmpty(groupList)) {
                plan.setGroupList(groupList);
            }
        }
        ResultDTO res = managementPlanService.managementPlanList(plan);
        return ResponseEntity.ok(res);
    }



    @PutMapping("/status/update")
    public ResponseEntity<ResultDTO> updateEducationPlanStatus(@RequestBody @Nullable List<ManagementPlan> planList) {
        ResultDTO res = managementPlanService.updateEducationPlanStatus(planList);
        return new ResponseEntity<>(res, HttpStatus.ACCEPTED);
    }

    @PostMapping("/qms/request")
    public ResponseEntity<ResultDTO> requestQmsPlan(@RequestBody @Nullable PlanQms plan) {
        ResultDTO res = managementPlanService.requestQmsPlan(plan);
        return new ResponseEntity<>(res, HttpStatus.ACCEPTED);
    }

    @PostMapping("/lab/save")
    public ResponseEntity<ResultDTO> saveEducationPlan(@RequestBody @Nullable List<EducationPlanLab> planList) {
        ResultDTO res = managementPlanService.saveEducationPlan(planList);
        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }

    @PostMapping("/user/list")
    public ResponseEntity<ResultDTO> findUserEduCurrentList(@RequestBody @Nullable UserEduCurrent userEduCurrent, HttpServletRequest request, Authentication auth) {
        ResultDTO res = managementPlanService.findUserEduCurrentList(userEduCurrent, request, auth);
        return ResponseEntity.ok(res);
    }

    @PostMapping("/student")
    public ResponseEntity<ResultDTO> planStudentList(@RequestBody New_SearchDTO searchDTO, HttpServletRequest request, Authentication auth) {
        List<ManagementPlanUserDTO> planUserList = planDAO.findManagementPlanUser(searchDTO.getPlan_id());
        ResultDTO res = new ResultDTO();
        res.setResult(true);
        res.setCode(200);
        res.setMessage("피교육자 조회");
        res.setResult(planUserList);
        return ResponseEntity.ok(res);
    }


    @PostMapping("/document")
    public ResponseEntity<ResultDTO> planDocumenttList(@RequestBody New_SearchDTO searchDTO, HttpServletRequest request, Authentication auth) {
        List<DocumentDTO> planUserList = planDAO.findManagementPlanDocument(searchDTO.getPlan_id());
        ResultDTO res = new ResultDTO();
        res.setResult(true);
        res.setCode(200);
        res.setMessage("교육문서 조회");
        res.setResult(planUserList);
        return ResponseEntity.ok(res);
    }


    @PostMapping("/test")
    public ResponseEntity<ResultDTO> responseTest(@RequestBody Map<String, Object> plan) {
        ResultDTO res = new ResultDTO();
        res.setMessage("메시지가 출력되는지 확인해보자");
        res.setResult(plan);
        return new ResponseEntity<>(res, HttpStatus.ACCEPTED);
    }
    /**
     * URL : /detail
     * description : 교육 단건 조회
     * param : plan
     * response : resultDTO
     */
    @PostMapping("/detail")
    public ResponseEntity<ResultDTO> managementPlanDetail(@RequestBody @Nullable ManagementPlan plan, Authentication auth) {
        User user = (User) auth.getPrincipal();
        boolean chkUserRole = auth.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
        if(!chkUserRole) {
            plan.setGroupList(commonDAO.findGroupInfoByDeptCd(user.getDept_cd()));
        }
        ResultDTO res = managementPlanService.managementPlanDetail(plan);
        return ResponseEntity.ok(res);
    }

    @PostMapping("/file/upload")
    public ResponseEntity<ResultDTO> uploadEducationPlanFile(@RequestParam("file") MultipartFile uploadFiles, HttpServletRequest request, Authentication auth) {
        ResultDTO res = managementPlanService.uploadEducationPlanFile(uploadFiles, request, auth);
        return new ResponseEntity<>(res, HttpStatus.ACCEPTED);
    }

    @PostMapping("/temp/delete")
    public ResponseEntity<ResultDTO> deleteTempEducation(@RequestBody ManagementPlan plan, HttpServletRequest request, Authentication auth) {
        ResultDTO res = managementPlanService.deleteTempEducation(plan, request, auth);
        return new ResponseEntity<>(res, HttpStatus.ACCEPTED);
    }

    // @PostMapping("/file/upload")
    // public String uploadEducationPlanFile(@RequestParam("file") MultipartFile uploadFiles, HttpServletRequest request, Authentication auth) {
    //     User user = (User) auth.getPrincipal();
    //     String userId = user.getUser_id();
    //     log.info(userId);
    //     try {
    //         Path uploadPath = Paths.get("./upload");
    //         if (!Files.exists(uploadPath)) {
    //             Files.createDirectories(uploadPath);
    //         }
    //         Path filePath = uploadPath.resolve(uploadFiles.getOriginalFilename());

    //         // 파일 저장
    //         Files.copy(uploadFiles.getInputStream(), filePath);
    //         log.info(uploadFiles.getOriginalFilename());
    //         return "파일 업로드 성공: " + uploadFiles.getOriginalFilename();

    //     } catch (IOException e) {
    //         e.printStackTrace();
    //         return "파일 업로드 실패";
    //     }
    // }

}
