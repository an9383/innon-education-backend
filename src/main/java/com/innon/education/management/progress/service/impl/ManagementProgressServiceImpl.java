package com.innon.education.management.progress.service.impl;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.innon.education.common.repository.entity.LogChild;
import com.innon.education.common.repository.entity.LogEntity;
import com.innon.education.common.service.CommonService;
import com.innon.education.common.util.DataLib;
import com.innon.education.controller.dto.ResultDTO;
import com.innon.education.management.plan.dao.ManagementPlanDAO;
import com.innon.education.management.plan.repository.dto.ManagementPlanDTO;
import com.innon.education.management.plan.repository.entity.ManagementPlanUserEntity;
import com.innon.education.management.progress.dao.ManagementProgressDAO;
import com.innon.education.management.progress.repository.dto.EduContentDTO;
import com.innon.education.management.progress.repository.dto.ManagementProgressDTO;
import com.innon.education.management.progress.repository.dto.PlanContentDTO;
import com.innon.education.management.progress.repository.dto.UserAttendanceDTO;
import com.innon.education.management.progress.repository.entity.ManagementProgressEntity;
import com.innon.education.management.progress.repository.entity.UserAttendanceEntity;
import com.innon.education.management.progress.repository.model.ManagementProgress;
import com.innon.education.management.progress.service.ManagementProgressService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ManagementProgressServiceImpl implements ManagementProgressService {
    private ResultDTO resultDTO;

    @Autowired
    ManagementProgressDAO managementProgressDAO;
    @Autowired
    MessageSource messageSource;
    @Autowired
    CommonService commonService;
    @Autowired
    ManagementPlanDAO planDAO;

    @Override
    public ResultDTO saveEpuReason(ManagementProgress progress) {
        resultDTO = new ResultDTO();
        if(progress != null) {
            ManagementProgressEntity entity = new ManagementProgressEntity(progress);
            int saveNum = managementProgressDAO.saveEpuReason(entity);

            if(saveNum > 0) {
                resultDTO.setMessage("수강생 참석진행 등록이 완료되었습니다.");
            } else {
                resultDTO.setMessage("수강생 참석진행 등록이 실패하였습니다.");
            }
        } else {
            resultDTO.setMessage("참석 진행될 수강생 정보가 없습니다.");
        }

        return resultDTO;
    }

    @Override
    public ResultDTO findManagementProgress(ManagementProgress progress, Authentication auth) {
        ResultDTO resultDTO = new ResultDTO();
        ManagementProgressEntity entity = new ManagementProgressEntity(progress);
        List<ManagementProgressDTO> resultList = managementProgressDAO.findManagementProgress(entity);

        if(!DataLib.isEmpty(resultList)) {
            int saveNum = 0;
            for(ManagementProgressDTO result : resultList) {
                int planId = result.getId();

                // 교육대상 리스트 조회 후 값 설정
                List<String> planUserList = managementProgressDAO.findEducationPlanUserList(planId);
                if(!DataLib.isEmpty(planUserList)) {
                    result.setPlanUserList(planUserList);
                }

                // 참고URL 리스트 조회 후 값 설정
                List<String> helpUrlList = managementProgressDAO.findPlanContent(planId);
                if(!DataLib.isEmpty(helpUrlList)) {
                    result.setHelpUrlList(helpUrlList);
                }
            }

       //     User user = (User) auth.getPrincipal();
       //     Collection<Role> roles = user.getRoles();

            // 유저일 경우 출석체크 데이터 등록
            /*for(Role role : roles) {
                if(role.getName().equals("ROLE_USER")) {
                    ManagementPlanUserEntity planUser = new ManagementPlanUserEntity(progress.getPlan_id(), user.getUser_id());
                    EducationPlanUserDTO educationPlanUser = managementProgressDAO.findEducationPlanUserByPlanIdAndUserId(planUser);
                    UserAttendanceEntity userEntity = new UserAttendanceEntity(educationPlanUser);
                    saveNum += managementProgressDAO.saveUserAttendance(userEntity);
                }
            }*/

            resultDTO.setState(true);
            resultDTO.setResult(resultList);
            if(saveNum > 0) {
                resultDTO.setMessage(saveNum + "건 출석체크 등록이 완료되었습니다.");
            }
        }

        return resultDTO;
    }

    @Override
    public ResultDTO saveUserAttendace(ManagementProgress progress, HttpServletRequest request, Authentication auth) {
        resultDTO = commonService.findCheckUserAuth(progress.getUser(), request, auth);

        if(resultDTO.getCode() == 200) {
            UserAttendanceEntity userEntity = new UserAttendanceEntity();
            userEntity.setAttend_user_id(auth.getName());
            userEntity.setPlan_id(progress.getId());
            userEntity.setFlag(progress.getFlag());
            userEntity.setPlan_id(progress.getPlan_id());
            UserAttendanceDTO userAttendanceDTO = null;
            if(userEntity.getContent_id() > 0) {
                userAttendanceDTO = managementProgressDAO.findUserAttendance(userEntity);
            }
            int saveNum = 0;
            if(userAttendanceDTO == null ){
                saveNum = managementProgressDAO.saveUserAttendance(userEntity);
                ManagementPlanUserEntity plan = new ManagementPlanUserEntity();
                plan.setQms_user_id(userEntity.getAttend_user_id());
                plan.setPlan_id(userEntity.getPlan_id());
                plan.setStatus("edus21002");
                planDAO.updatePlanUser(plan);
            }



            if(saveNum > 0) {
                int logId = commonService.saveLog(LogEntity.builder()
                        .table_id(userEntity.getId())
                        .page_nm("attendance")
                        .url_addr(request.getRequestURI())
                        .state("insert")
                        .reg_user_id(auth.getName())
                        .build());

                if(logId > 0) {
                    try {
                        Object obj = userEntity;
                        for (Field field : obj.getClass().getDeclaredFields()) {
                            field.setAccessible(true);
                            Object value = field.get(obj);
                            if(value != null) {
                                resultDTO = commonService.saveLogDetail(LogChild
                                        .builder()
                                        .log_id(logId)
                                        .field(field.getName())
                                        .new_value(value.toString())
                                        .reg_user_id(auth.getName())
                                        .build());
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                resultDTO.setState(true);
                resultDTO.setCode(201);
                resultDTO.setMessage(messageSource.getMessage("api.message.201", new String[]{"본인인증"}, Locale.KOREA));
                resultDTO.setResult(saveNum);
            } else {
                resultDTO.setState(false);
                resultDTO.setCode(400);
                resultDTO.setMessage(messageSource.getMessage("api.message.400", new String[]{"본인인증"}, Locale.KOREA));
                resultDTO.setResult(null);
            }
        }

        return resultDTO;
    }

    @Override
    public ResultDTO findEduContentInfo(ManagementProgress progress, HttpServletRequest request, Authentication auth) {
        resultDTO = new ResultDTO();
        EduContentDTO eduContentDTO = new EduContentDTO();

        ManagementPlanDTO plan = managementProgressDAO.findEducationPlanById(progress.getPlan_id());
        if(plan != null) {
            eduContentDTO.setTitle(plan.getTitle());
        }

        List<PlanContentDTO> planContentList = managementProgressDAO.findEduContentList(progress);
        if(!DataLib.isEmpty(planContentList)) {
            eduContentDTO.setPlanContents(planContentList);
        }

        try {
            resultDTO.setState(true);
            resultDTO.setCode(200);
            resultDTO.setMessage(messageSource.getMessage("api.message.200", new String[]{"교육계획"}, Locale.KOREA));
            resultDTO.setResult(eduContentDTO);
        } catch(Exception e) {
            resultDTO.setState(false);
            resultDTO.setCode(400);
            resultDTO.setMessage(messageSource.getMessage("api.message.400", new String[]{"교육계획"}, Locale.KOREA));
            resultDTO.setResult(null);
        }

        return resultDTO;
    }

    @Override
    public ResultDTO attendUserEduContent(UserAttendanceEntity entity, HttpServletRequest request, Authentication auth) {
        resultDTO = new ResultDTO();
        //todo 이현석 진도 나갔을떄 체크해서 리턴하는  로직 필요 ..


        UserAttendanceDTO userAttendanceDTO = managementProgressDAO.findUserAttendance(entity);
        int saveNum = 0;
        if(userAttendanceDTO == null ){
             saveNum = managementProgressDAO.saveUserAttendance(entity);

             List<UserAttendanceDTO> attendanceDTOList = managementProgressDAO.planByAttendList(entity);

             if(attendanceDTOList.stream().filter(userAttendanceDTO1 -> userAttendanceDTO1.getAttend_date_str() == null).count() == 0){
                 ManagementPlanUserEntity plan = new ManagementPlanUserEntity();
                 plan.setQms_user_id(entity.getAttend_user_id());
                 plan.setPlan_id(entity.getPlan_id());
                 plan.setStatus("edus21003");
                 planDAO.updatePlanUser(plan);
             }
        }

        if(saveNum > 0) {
            int logId = commonService.saveLog(LogEntity.builder()
                    .table_id(entity.getId())
                    .page_nm("attendance")
                    .url_addr(request.getRequestURI())
                    .state("insert")
                    .reg_user_id(auth.getName())
                    .build());

            if(logId > 0) {
                try {
                    Object obj = entity;
                    for (Field field : obj.getClass().getDeclaredFields()) {
                        field.setAccessible(true);
                        Object value = field.get(obj);
                        if(value != null) {
                            resultDTO = commonService.saveLogDetail(LogChild
                                    .builder()
                                    .log_id(logId)
                                    .field(field.getName())
                                    .new_value(value.toString())
                                    .reg_user_id(auth.getName())
                                    .build());
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    log.info(e.getCause().toString());
                }
            }

            resultDTO.setState(true);
            resultDTO.setCode(202);
            resultDTO.setMessage(messageSource.getMessage("api.message.202", new String[]{"교육진행", ""}, Locale.KOREA));
            resultDTO.setResult(saveNum);
        } else {
            resultDTO.setState(false);
            resultDTO.setCode(400);
            resultDTO.setMessage(messageSource.getMessage("api.message.400", new String[]{"본인인증"}, Locale.KOREA));
            resultDTO.setResult(null);
        }

        return resultDTO;
    }

    @Override
    public ResultDTO planByAttendList(UserAttendanceEntity progress, HttpServletRequest request, Authentication auth) {
        resultDTO = new ResultDTO();
        List<UserAttendanceDTO> resultList = managementProgressDAO.planByAttendList(progress);

        if (!DataLib.isEmpty(resultList)) {
            resultDTO.setState(true);
            resultDTO.setCode(200);
            resultDTO.setMessage(messageSource.getMessage("api.message.200", new String[]{"사용자 현황"}, Locale.KOREA));
            resultDTO.setResult(resultList);
        } else {
            resultDTO.setState(false);
            resultDTO.setCode(400);
            resultDTO.setMessage(messageSource.getMessage("api.message.400", new String[]{"사용자 현황"}, Locale.KOREA));
            resultDTO.setResult(null);
        }

        return resultDTO;
    }
}
