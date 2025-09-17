package com.innon.education.admin.group.service.impl;

import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.innon.education.admin.group.dao.GroupDAO;
import com.innon.education.admin.group.repository.Group;
import com.innon.education.admin.group.repository.GroupDTO;
import com.innon.education.admin.group.repository.GroupDept;
import com.innon.education.admin.group.service.GroupService;
import com.innon.education.auth.entity.User;
import com.innon.education.common.repository.entity.LogEntity;
import com.innon.education.common.service.CommonService;
import com.innon.education.common.util.DataLib;
import com.innon.education.controller.dto.ResultDTO;
import com.innon.education.dao.CodeDAO;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class GroupServiceImpl implements GroupService {
    private ResultDTO resultDTO;
    @Autowired
    GroupDAO groupDAO;
    @Autowired
    MessageSource messageSource;
    @Autowired
    CommonService commonService;
    @Autowired
    CodeDAO codeDAO;

    @Override
    public ResultDTO findGroupList(Group group, HttpServletRequest request, Authentication auth) {
        resultDTO = new ResultDTO();
        resultDTO.setResult(groupDAO.findGroupList(group));
        resultDTO.setCode(200);

        return resultDTO;
    }

    @Override
    public ResultDTO saveGroup(Group group, HttpServletRequest request, Authentication auth) {
        resultDTO = new ResultDTO();
        User user = (User) auth.getPrincipal();

        group.setSys_reg_user_id(auth.getName());
        group.setDept_cd(user.getDept_cd());
        group.getGroupDepts().forEach(groupDept -> {
            groupDept.setSys_reg_user_id(auth.getName());
        });
        int resultId = groupDAO.saveGroup(group);
        if(resultId > 0) {
            int logId = commonService.saveLog(LogEntity.builder()
                    .table_id(group.getId())
                    .page_nm("group")
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

            group.getGroupDepts().forEach(groupDept -> {
                groupDept.setGroup_id(group.getId());
                groupDept.setSys_reg_user_id(auth.getName());
                int groupDeptId = groupDAO.saveGroupDept(groupDept);
            });

            resultDTO.setState(true);
            resultDTO.setCode(201);
            resultDTO.setMessage(messageSource.getMessage("api.message.201", new String[]{"그룹관리"}, Locale.KOREA));
            resultDTO.setResult(group.getId());
        } else {
            resultDTO.setState(false);
            resultDTO.setCode(400);
            resultDTO.setMessage(messageSource.getMessage("api.message.400", new String[]{"그룹관리"}, Locale.KOREA));
            resultDTO.setResult(null);
        }

        return resultDTO;
    }

    @Override
    public ResultDTO findGroupDeptList(Group group, HttpServletRequest request, Authentication auth) {
        resultDTO = new ResultDTO();
        List<GroupDept> groupDeptList = groupDAO.findGroupDeptList(group);

        if(!DataLib.isEmpty(groupDeptList)) {
//            int logId = commonService.saveLog(LogEntity.builder()
//                    .table_id(group.getId())
//                    .page_nm("group")
//                    .url_addr(request.getRequestURI())
//                    .state("view")
//                    .reg_user_id(auth.getName())
//                    .build());


            resultDTO.setState(true);
            resultDTO.setCode(200);
            resultDTO.setMessage(messageSource.getMessage("api.message.200", new String[]{"부서"}, Locale.KOREA));
            resultDTO.setResult(groupDeptList);
        } else {
            resultDTO.setState(false);
            resultDTO.setCode(400);
            resultDTO.setMessage(messageSource.getMessage("api.message.400", new String[]{"부서"}, Locale.KOREA));
            resultDTO.setResult(null);
        }

        return resultDTO;
    }

    @Override
    public ResultDTO updateGroup(Group group, HttpServletRequest request, Authentication auth) {
        resultDTO = new ResultDTO();
        group.setSys_reg_user_id(auth.getName());
        int update = groupDAO.updateGroupUpdate(group);
        if(update > 0) {
            GroupDept delGroup = new GroupDept();
            delGroup.setGroup_id(group.getId());
            delGroup.setSys_reg_user_id(auth.getName());
            int gorupDeptId = groupDAO.updateGroupDept(delGroup);

            if(group.getGroupDepts() != null) {
                group.getGroupDepts().forEach(groupDept -> {
                    groupDept.setGroup_id(group.getId());
                    groupDept.setSys_reg_user_id(auth.getName());
                    int groupDeptId = groupDAO.saveGroupDept(groupDept);
                });
            }


            resultDTO.setState(true);
            resultDTO.setCode(202);
            resultDTO.setMessage(messageSource.getMessage("api.message.202", new String[]{"부서", "수정"}, Locale.KOREA));
            resultDTO.setResult(group.getId());
        } else {
            resultDTO.setState(false);
            resultDTO.setCode(400);
            resultDTO.setMessage(messageSource.getMessage("api.message.400", new String[]{"부서"}, Locale.KOREA));
            resultDTO.setResult(null);
        }

        return resultDTO;
    }

    @Override
    public ResultDTO findGroupDept(Group group, HttpServletRequest request, Authentication auth) {
        resultDTO = new ResultDTO();
        List<GroupDTO> groupList = groupDAO.findGroupList(group);
        groupList.forEach(groupDTO -> {
            group.setId(groupDTO.getId());
            groupDTO.setChildren(groupDAO.findGroupDeptList(group));
        });

        if(!DataLib.isEmpty(groupList)) {
//            int logId = commonService.saveLog(LogEntity.builder()
//                    .table_id(group.getId())
//                    .page_nm("group")
//                    .url_addr(request.getRequestURI())
//                    .state("view")
//                    .reg_user_id(auth.getName())
//                    .build());


            resultDTO.setState(true);
            resultDTO.setCode(200);
            resultDTO.setMessage(messageSource.getMessage("api.message.200", new String[]{"그룹관리"}, Locale.KOREA));
            resultDTO.setResult(groupList);
        } else {
            resultDTO.setState(false);
            resultDTO.setCode(400);
            resultDTO.setMessage(messageSource.getMessage("api.message.400", new String[]{"그룹관리"}, Locale.KOREA));
            resultDTO.setResult(null);
        }

        return resultDTO;
    }
}
