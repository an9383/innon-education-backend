package com.innon.education.qualified.current.controller;

import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.innon.education.admin.group.repository.Group;
import com.innon.education.auth.entity.User;
import com.innon.education.common.dao.CommonDAO;
import com.innon.education.common.util.DataLib;
import com.innon.education.controller.dto.ResultDTO;
import com.innon.education.management.plan.repository.dto.ManagementPlanUserDTO;
import com.innon.education.qualified.current.repository.model.JobRequest;
import com.innon.education.qualified.current.repository.model.JobRevision;
import com.innon.education.qualified.current.repository.model.JobSkill;
import com.innon.education.qualified.current.service.CurrentService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/api/education/qualified")
public class CurrentController {

    @Autowired
    private CurrentService currentService;
    @Autowired
    MessageSource messageSource;
    @Autowired
    CommonDAO commonDAO;
    /**
     * URL : /save
     * description : 직무요구서 추가
     * param : JobRequest
     * response : resultDTO
     */
    @PostMapping("/save")
    public ResponseEntity<ResultDTO> saveQualifiedCategory(@RequestBody JobRequest jobRequest, HttpServletRequest request, Authentication auth) {
        ResultDTO res = new ResultDTO();
        try {
            res = currentService.saveJobRequest(jobRequest, request, auth);
        } catch (Exception e) {
            log.error(e.getCause().toString());
            res.setState(false);
            res.setCode(400);
            res.setMessage(messageSource.getMessage("api.message.400", new String[]{"직무요구서"}, Locale.KOREA));
            res.setResult(null);
        }

        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }

    @PostMapping("/update")
    public ResponseEntity<ResultDTO> updateJobRequest(@RequestBody JobRequest jobRequest, HttpServletRequest request, Authentication auth) {
        ResultDTO res = new ResultDTO();
        try {
            res = currentService.updateJobRequst(jobRequest, request, auth);
        } catch (Exception e) {
            log.error(e.getCause().toString());
            res.setState(false);
            res.setCode(400);
            res.setMessage(messageSource.getMessage("api.message.400", new String[]{"직무요구서"}, Locale.KOREA));
            res.setResult(null);
        }

        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }



    /**
     * URL : /list
     * description : 직무요구서 조회
     * param : JobRequest
     * response : resultDTO
     */
    @PostMapping("/list")
    public ResponseEntity<ResultDTO> findJobRequestList(@RequestBody JobRequest jobRequest, HttpServletRequest request, Authentication auth) {
        ResultDTO res = new ResultDTO();
        try {
            res = currentService.findJobRequestList(jobRequest, request, auth);
        } catch (Exception e) {
            log.error(e.getCause().toString());
            res.setState(false);
            res.setCode(400);
            res.setMessage(messageSource.getMessage("api.message.400", new String[]{"직무요구서"}, Locale.KOREA));
            res.setResult(null);
        }

        return ResponseEntity.ok(res);
    }

    @PostMapping("/my/list")
    public ResponseEntity<ResultDTO> findMyJobRequestList(@RequestBody JobRequest jobRequest, HttpServletRequest request, Authentication auth) {
        ResultDTO res = new ResultDTO();
        try {
            res = currentService.findMyJobRequestList(jobRequest, request, auth);
        } catch (Exception e) {
            log.error(e.getCause().toString());
            res.setState(false);
            res.setCode(400);
            res.setMessage(messageSource.getMessage("api.message.400", new String[]{"직무요구서"}, Locale.KOREA));
            res.setResult(null);
        }

        return ResponseEntity.ok(res);
    }

    @PostMapping("/temp/delete")
    public ResponseEntity<ResultDTO> deleteTempJobRequest(@RequestBody JobRequest jobRequest, HttpServletRequest request, Authentication auth) {
        ResultDTO res = new ResultDTO();
        try {
            res = currentService.deleteTempJobRequest(jobRequest, request, auth);
        } catch (Exception e) {
            log.error(e.getCause().toString());
            res.setState(false);
            res.setCode(400);
            res.setMessage(messageSource.getMessage("api.message.400", new String[]{"직무요구서 임시저장"}, Locale.KOREA));
            res.setResult(null);
        }

        return new ResponseEntity<>(res, HttpStatus.ACCEPTED);
    }

    @PostMapping("/revision/list")
    public ResponseEntity<ResultDTO> findJobRevisionList(@RequestBody JobRequest jobRequest, HttpServletRequest request, Authentication auth) {
        ResultDTO res = new ResultDTO();
        try {
            res = currentService.findJobRevisionList(jobRequest, request, auth);
        } catch (Exception e) {
            log.error(e.getCause().toString());
            res.setState(false);
            res.setCode(400);
            res.setMessage(messageSource.getMessage("api.message.400", new String[]{"직무버전"}, Locale.KOREA));
            res.setResult(null);
        }

        return ResponseEntity.ok(res);
    }


    @PostMapping("/jobskill/list")
    public ResponseEntity<ResultDTO> findJobSkillList(@RequestBody ManagementPlanUserDTO managementPlanUserDTO, HttpServletRequest request, Authentication auth) {
        ResultDTO res = new ResultDTO();
        User user = (User) auth.getPrincipal();
        try {
            if(managementPlanUserDTO.getGroup_id() == 0){
                List<Group> groupList = commonDAO.findGroupInfoByDeptCd(user.getDept_cd());
                if(!DataLib.isEmpty(groupList)) {
                    managementPlanUserDTO.setGroupList(groupList);
                }
            }
            res = currentService.findJobSkillList( managementPlanUserDTO, request,  auth);
        } catch (Exception e) {
            log.error(e.getCause().toString());
            res.setState(false);
            res.setCode(400);
            res.setMessage(messageSource.getMessage("api.message.400", new String[]{"직무 완료"}, Locale.KOREA));
            res.setResult(null);
        }

        return ResponseEntity.ok(res);
    }

    @PostMapping("/jobskill/approve")
    public ResponseEntity<ResultDTO> findJobSkillUserList(@RequestBody JobSkill jobSkill,
                                                          HttpServletRequest request, Authentication auth) {
        ResultDTO res = new ResultDTO();
        try {
            res = currentService.userJobSkillList( jobSkill, request,  auth);
        } catch (Exception e) {
            log.error(e.getCause().toString());
            res.setState(false);
            res.setCode(400);
            res.setMessage(messageSource.getMessage("api.message.400", new String[]{"직무 완료"}, Locale.KOREA));
            res.setResult(null);
        }

        return ResponseEntity.ok(res);
    }
    @PostMapping("/jobskill/approve/list")
    public ResponseEntity<ResultDTO> JobSkillUserItemList(@RequestBody JobSkill jobSkill,
                                                          HttpServletRequest request, Authentication auth) {
        ResultDTO res = new ResultDTO();
        try {
            res = currentService.JobSkillUserItemList( jobSkill, request,  auth);
        } catch (Exception e) {
            log.error(e.getCause().toString());
            res.setState(false);
            res.setCode(400);
            res.setMessage(messageSource.getMessage("api.message.400", new String[]{"직무 완료"}, Locale.KOREA));
            res.setResult(null);
        }

        return ResponseEntity.ok(res);
    }


    @PostMapping("/content/list")
    public ResponseEntity<ResultDTO> findRevisionContentList(@RequestBody JobRevision jobRevision, HttpServletRequest request, Authentication auth) {
        ResultDTO res = new ResultDTO();
        try {
            res = currentService.findRevisionContentList(jobRevision, request, auth);
        } catch (Exception e) {
            log.error(e.getCause().toString());
            res.setState(false);
            res.setCode(400);
            res.setMessage(messageSource.getMessage("api.message.400", new String[]{"교육내용"}, Locale.KOREA));
            res.setResult(null);
        }
        
        return ResponseEntity.ok(res);
    }

    @PostMapping("/document/list")
    public ResponseEntity<ResultDTO> findRevisionDocumentList(@RequestBody JobRevision jobRevision, HttpServletRequest request, Authentication auth) {
        ResultDTO res = new ResultDTO();
        try {
            res = currentService.findRevisionDocumentList(jobRevision, request, auth);
        } catch (Exception e) {
            log.error(e.getCause().toString());
            res.setState(false);
            res.setCode(400);
            res.setMessage(messageSource.getMessage("api.message.400", new String[]{"교육문서"}, Locale.KOREA));
            res.setResult(null);
        }

        return ResponseEntity.ok(res);
    }

    /**
     * URL : /jobskill/save
     * description : 직무 기술서 추가
     * param : JobRequest
     * response : resultDTO
     */
    @PostMapping("/jobskill/save")
    public ResponseEntity<ResultDTO> saveJobSkill(@RequestBody JobSkill jobSkill, HttpServletRequest request, Authentication auth) {
        ResultDTO res = new ResultDTO();
        try {
            res = currentService.saveJobSkill(jobSkill, request, auth);
        } catch (Exception e) {
            log.error(e.getCause().toString());
            e.printStackTrace();
            res.setState(false);
            res.setCode(400);
            res.setMessage(messageSource.getMessage("api.message.400", new String[]{"직무 기술서"}, Locale.KOREA));
            res.setResult(null);
        }

        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }

    @PostMapping("/jobskill/update")
    public ResponseEntity<ResultDTO> updateJobSkill(@RequestBody JobSkill jobSkill, HttpServletRequest request, Authentication auth) {
        ResultDTO res = new ResultDTO();
        try {
            res = currentService.updateJobSkill(jobSkill, request, auth);
        } catch (Exception e) {
            log.error(e.getCause().toString());
            res.setState(false);
            res.setCode(400);
            res.setMessage(messageSource.getMessage("api.message.400", new String[]{"직무요구서"}, Locale.KOREA));
            res.setResult(null);
        }

        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }
}
