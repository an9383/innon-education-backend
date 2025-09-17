package com.innon.education.annual.plan.controller;

import com.innon.education.annual.plan.repository.entity.AnnualPlanSearchEntity;
import com.innon.education.annual.plan.repository.model.AnnualPlan;
import com.innon.education.annual.plan.service.AnnualPlanService;
import com.innon.education.auth.entity.User;
import com.innon.education.common.service.CommonService;
import com.innon.education.controller.dto.ResultDTO;
import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api/education/annual")
public class AnnualPlanController {

    @Autowired
    private AnnualPlanService annualPlanService;
    @Autowired
    CommonService commonService;


    /**
     * URL : /save
     * description : 연간교육계획 등록
     * param : planList
     * response : resultDTO
     */
    @PostMapping("/save")
    public ResponseEntity<ResultDTO> saveAnnualPlan(@RequestBody AnnualPlan annualPlan, Authentication auth, HttpServletRequest request) {
        ResultDTO res = new ResultDTO();

        if(annualPlan.getId() > 0){
            res = annualPlanService.updateAnnualPlan(annualPlan, auth, request);
        } else {

            res = annualPlanService.saveAnnualPlan(annualPlan, auth, request);

        }

        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }

    @PostMapping("/revision")
    public ResponseEntity<ResultDTO> revisionAnnualPlan(@RequestBody AnnualPlan annualPlan, Authentication auth, HttpServletRequest request) {
        ResultDTO res = new ResultDTO();
        res = annualPlanService.revisionAnnualPlan(annualPlan, auth, request);

        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }

    @PostMapping("/examine")
    public ResponseEntity<ResultDTO> examineAnnualPlan(@RequestBody AnnualPlan annualPlan, Authentication auth, HttpServletRequest request) {
        ResultDTO res = new ResultDTO();

        res = commonService.findCheckUserAuth(annualPlan.getUser(), request, auth);
        if (res.getCode() == 200) {
            res = annualPlanService.examineAnnualPlan(annualPlan, auth, request);
        }
        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }

    /**
     * URL : /lsit
     * description : 연간교육계획 조회
     * param : plan
     * response : resultDTO
     */
    @PostMapping("/list")
    public ResponseEntity<ResultDTO> annualEducationList(@RequestBody @Nullable AnnualPlanSearchEntity plan, Authentication auth) {

        User user = (User) auth.getPrincipal();
        ResultDTO res = annualPlanService.annualPlanList(plan);
        return ResponseEntity.ok(res);
    }

    /**
     * URL : /annual
     * description : 연간교육계획 조회
     * param : plan
     * response : resultDTO
     */
    @PostMapping("/annual")
    public ResponseEntity<ResultDTO> annualList(@RequestBody @Nullable AnnualPlanSearchEntity plan, Authentication auth) {

        User user = (User) auth.getPrincipal();
        ResultDTO res = annualPlanService.annualList(plan);
        return ResponseEntity.ok(res);
    }

    /**
     * URL : /plan
     * description : 연간교육계획 조회
     * param : AnnualPlan
     * response : resultDTO
     */
    @PostMapping("/plan")
    public ResponseEntity<ResultDTO> annualEducationPlanList(@RequestBody @Nullable AnnualPlanSearchEntity plan) {
        ResultDTO res = annualPlanService.annualEducationPlanList(plan);
        return ResponseEntity.ok(res);
    }


    @PostMapping("/view")
    public ResponseEntity<ResultDTO> findAnnualPlanView(@RequestBody @Nullable AnnualPlanSearchEntity plan) {
        ResultDTO res = annualPlanService.findAnnualPlanView(plan);
        return ResponseEntity.ok(res);
    }
    @PostMapping("/migrate")
    public ResponseEntity<ResultDTO> migrateAnnualPlan(@RequestBody AnnualPlanSearchEntity plan) {
        ResultDTO res = annualPlanService.migrateAnnualPlan(plan);
        return ResponseEntity.ok(res);
    }

    /**
     * URL : /save/dept
     * description : 연간교육계획 부서 계획 등록
     * param : planList
     * response : resultDTO
     */
    @PostMapping("/save/dept")
    public ResponseEntity<ResultDTO> saveDeptAnnualPlan(@RequestBody AnnualPlan annualPlan, Authentication auth, HttpServletRequest request) {
        ResultDTO res = new ResultDTO();

        res = annualPlanService.saveDeptAnnualPlan(annualPlan, auth, request);

        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }

    /**
     * URL : /save/dept
     * description : 연간교육계획 재정
     * param : planList
     * response : resultDTO
     */
    @PostMapping("/save/sign")
    public ResponseEntity<ResultDTO> saveAnnualSign(@RequestBody AnnualPlan annualPlan, Authentication auth, HttpServletRequest request) {
        ResultDTO res = new ResultDTO();

        res = annualPlanService.saveAnnualSign(annualPlan, auth, request);

        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }

    /**
     * URL : /update/editable
     * description : 연간교육계획 부서교육 완료-미완료 전환
     * param : id, save_yn
     * response : resultDTO
     */
    @PostMapping("/update/editable")
    public ResponseEntity<ResultDTO> editableDept(@RequestBody AnnualPlan annualPlan, Authentication auth, HttpServletRequest request) {
        ResultDTO res = new ResultDTO();

        res = annualPlanService.editableDept(annualPlan, auth, request);

        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }

}
