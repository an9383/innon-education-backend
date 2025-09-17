package com.innon.education.admin.jobtype.service.impl;

import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.innon.education.admin.jobtype.dao.JobTypeDAO;
import com.innon.education.admin.jobtype.repository.JobType;
import com.innon.education.admin.jobtype.repository.JobTypeDTO;
import com.innon.education.admin.jobtype.repository.JobTypeDept;
import com.innon.education.admin.jobtype.service.JobTypeService;
import com.innon.education.auth.entity.User;
import com.innon.education.common.repository.entity.LogEntity;
import com.innon.education.common.service.CommonService;
import com.innon.education.common.util.DataLib;
import com.innon.education.controller.dto.ResultDTO;
import com.innon.education.dao.CodeDAO;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class JobTypeServiceImpl implements JobTypeService {
    private ResultDTO resultDTO;
    @Autowired
    JobTypeDAO jobTypeDAO;
    @Autowired
    MessageSource messageSource;
    @Autowired
    CommonService commonService;
    @Autowired
    CodeDAO codeDAO;

    @Override
    public ResultDTO findJobTypeList(JobType jobType, HttpServletRequest request, Authentication auth) {
        resultDTO = new ResultDTO();
        resultDTO.setResult(jobTypeDAO.findJobTpyeList(jobType));
        resultDTO.setCode(200);

        return resultDTO;
    }

    @Override
    public ResultDTO saveJobType(JobType jobType, HttpServletRequest request, Authentication auth) {
        resultDTO = new ResultDTO();
        User user = (User) auth.getPrincipal();

        jobType.setSys_reg_user_id(auth.getName());
        jobType.setDept_cd(user.getDept_cd());
        jobType.getJobTypeDepts().forEach(jobTypeDept -> {
            jobTypeDept.setSys_reg_user_id(auth.getName());
        });
        int resultId = jobTypeDAO.saveJobType(jobType);
        if (resultId > 0) {
            int logId = commonService.saveLog(LogEntity.builder()
                    .table_id(jobType.getId())
                    .page_nm("jobType")
                    .url_addr(request.getRequestURI())
                    .state("insert")
                    .reg_user_id(auth.getName())
                    .build());
            try {

//                    Object obj=sign;
//                    for (Field field : obj.getClass().getDeclaredFields()){
//                        field.setAccessible(true);
//                        Object value=field.get(obj);
//                        resultDTO = commonService.saveLogDetail(LogChild
//                                .builder()
//                                .log_id(logId)
//                                .field(field.getName())
//                                .new_value(value.toString())
//                                .reg_user_id(authentication.getName())
//                                .build());
//                    }

            } catch (Exception e) {
                e.printStackTrace();
            }

            jobType.getJobTypeDepts().forEach(jobTypeDept -> {
                jobTypeDept.setJob_type_id(jobType.getId());
                jobTypeDept.setSys_reg_user_id(auth.getName());
                int jobTypeDeptId = jobTypeDAO.saveJobTypeDept(jobTypeDept);
            });

            resultDTO.setState(true);
            resultDTO.setCode(201);
            resultDTO.setMessage(messageSource.getMessage("api.message.201", new String[]{"직무유형"}, Locale.KOREA));
            resultDTO.setResult(jobType.getId());
        } else {
            resultDTO.setState(false);
            resultDTO.setCode(400);
            resultDTO.setMessage(messageSource.getMessage("api.message.400", new String[]{"직무유형"}, Locale.KOREA));
            resultDTO.setResult(null);
        }

        return resultDTO;
    }

    @Override
    public ResultDTO findJobTypeDeptList(JobType jobType, HttpServletRequest request, Authentication auth) {
        resultDTO = new ResultDTO();
        List<JobTypeDept> jobTypeDeptList = jobTypeDAO.findJobTypeDeptList(jobType);
        if(!DataLib.isEmpty(jobTypeDeptList)) {
//            int logId = commonService.saveLog(LogEntity.builder()
//                    .table_id(jobType.getId())
//                    .page_nm("jobType")
//                    .url_addr(request.getRequestURI())
//                    .state("view")
//                    .reg_user_id(auth.getName())
//                    .build());


            resultDTO.setState(true);
            resultDTO.setCode(200);
            resultDTO.setMessage(messageSource.getMessage("api.message.200", new String[]{"직무유형"}, Locale.KOREA));
            resultDTO.setResult(jobTypeDeptList);
        } else {
            resultDTO.setState(false);
            resultDTO.setCode(400);
            resultDTO.setMessage(messageSource.getMessage("api.message.400", new String[]{"직무유형"}, Locale.KOREA));
            resultDTO.setResult(null);
        }
        
        return resultDTO;
    }

    @Override
    public ResultDTO updateJobType(JobType jobType, HttpServletRequest request, Authentication auth) {
        resultDTO = new ResultDTO();
        jobType.setSys_reg_user_id(auth.getName());
        int update = jobTypeDAO.updateJobTypeUpdate(jobType);
        if(update > 0) {
            JobTypeDept delJobType = new JobTypeDept();
            delJobType.setJob_type_id(jobType.getId());
            delJobType.setSys_reg_user_id(auth.getName());
            int delJobTypeDeptId = jobTypeDAO.updateJobTypeDept(delJobType);

            if(jobType.getJobTypeDepts() != null) {
                jobType.getJobTypeDepts().forEach(jobTypeDept -> {
                    jobTypeDept.setJob_type_id(jobType.getId());
                    jobTypeDept.setSys_reg_user_id(auth.getName());
                    int jobTypeDeptId = jobTypeDAO.saveJobTypeDept(jobTypeDept);
                });
            }


            resultDTO.setState(true);
            resultDTO.setCode(202);
            resultDTO.setMessage(messageSource.getMessage("api.message.202", new String[]{"직무유형", "수정"}, Locale.KOREA));
            resultDTO.setResult(jobType.getId());
        } else {
            resultDTO.setState(false);
            resultDTO.setCode(400);
            resultDTO.setMessage(messageSource.getMessage("api.message.400", new String[]{"직무유형"}, Locale.KOREA));
            resultDTO.setResult(null);
        }

        return resultDTO;
    }

    @Override
    public ResultDTO findJobTypeDept(JobType jobType, HttpServletRequest request, Authentication auth) {
        resultDTO = new ResultDTO();
        List<JobTypeDTO> jobTypeList = jobTypeDAO.findJobTpyeList(jobType);
        jobTypeList.forEach(jobTypeDTO -> {
            jobType.setId(jobTypeDTO.getId());
            jobTypeDTO.setChildren(jobTypeDAO.findJobTypeDeptList(jobType));
        });

        if(!DataLib.isEmpty(jobTypeList)) {
//            int logId = commonService.saveLog(LogEntity.builder()
//                    .table_id(jobType.getId())
//                    .page_nm("jobType")
//                    .url_addr(request.getRequestURI())
//                    .state("view")
//                    .reg_user_id(auth.getName())
//                    .build());


            resultDTO.setState(true);
            resultDTO.setCode(200);
            resultDTO.setMessage(messageSource.getMessage("api.message.200", new String[]{"직무유형"}, Locale.KOREA));
            resultDTO.setResult(jobTypeList);
        } else {
            resultDTO.setState(false);
            resultDTO.setCode(400);
            resultDTO.setMessage(messageSource.getMessage("api.message.400", new String[]{"직무유형"}, Locale.KOREA));
            resultDTO.setResult(null);
        }

        return resultDTO;
    }
}
