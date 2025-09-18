package com.innon.education.common.service.impl;

import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.innon.education.auth.entity.Role;
import com.innon.education.auth.entity.RoleDTO;
import com.innon.education.auth.entity.RoleUser;
import com.innon.education.auth.entity.User;
import com.innon.education.common.dao.UserRoleDAO;
import com.innon.education.common.service.UserRoleService;
import com.innon.education.common.util.DataLib;
import com.innon.education.code.controller.dto.ResultDTO;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class UserRoleServiceImpl implements UserRoleService {

    @Autowired
    private UserRoleDAO userRoleDAO;


    private ResultDTO resultDTO;

    @Autowired
    MessageSource messageSource;



    @Override
    public ResultDTO findUserRoleList(Role role) {
        resultDTO = new ResultDTO();
        List<Role> userRoleList = userRoleDAO.findUserRoleList(role);

        if(!DataLib.isEmpty(userRoleList)) {
            resultDTO.setState(true);
            resultDTO.setResult(userRoleList);
            resultDTO.setMessage(messageSource.getMessage("api.message.201", new String[]{"권한 조회"}, Locale.KOREA));
        } else {
            resultDTO.setState(false);
            resultDTO.setCode(400);
            resultDTO.setMessage(messageSource.getMessage("api.message.400", new String[]{"권한 조회"}, Locale.KOREA));
            resultDTO.setResult(null);
        }

        return resultDTO;
    }

    @Override
    public ResultDTO updateRole(Role role, HttpServletRequest request, Authentication auth) {
        resultDTO = new ResultDTO();
        User user = (User) auth.getPrincipal();

        System.out.println("updateRole >>>>> " + role);
        userRoleDAO.updateRoles(role);
        try {
            userRoleDAO.deleteRoleUser(role.getId());

            int resultId = 0;
            for (RoleUser roleUser : role.getRoleUsers()) {
                roleUser.setRole_id(role.getId());
                roleUser.setSys_reg_user_id(user.getUser_id());
                roleUser.setUser_id(role.getUser_id());
                resultId += userRoleDAO.saveRoleUser(roleUser);
            }

            resultDTO.setState(true);
            resultDTO.setCode(202);
            resultDTO.setMessage(messageSource.getMessage("api.message.202", new String[]{"메뉴 담당자 ","수정"}, Locale.KOREA));
            resultDTO.setResult(resultId);
        } catch(Exception e) {
            resultDTO.setState(false);
            resultDTO.setCode(400);
            resultDTO.setMessage(messageSource.getMessage("api.message.400", new String[]{"메뉴 담당자"}, Locale.KOREA));
            resultDTO.setResult(null);
        }

        return resultDTO;
    }

    @Override
    public ResultDTO saveRole(Role role, HttpServletRequest request, Authentication auth) {
        User user = (User) auth.getPrincipal();
        Role responseRole = userRoleDAO.finduserRole(role);
        if(responseRole != null){
            resultDTO.setState(false);
            resultDTO.setCode(409);
            resultDTO.setMessage(messageSource.getMessage("api.message.409", new String[]{"권한 등록"}, Locale.KOREA));
            resultDTO.setResult(null);
        }else{
            role.setSys_reg_user_id(user.getUser_id());
            int roleId = userRoleDAO.saveRole(role);
            if(roleId > 0){
                int resultId = 0;
                for (RoleUser roleUser : role.getRoleUsers()) {
                    roleUser.setRole_id(role.getId());
                    roleUser.setSys_reg_user_id(user.getUser_id());
                    resultId += userRoleDAO.saveRoleUser(roleUser);
                }

                resultDTO.setState(true);
                resultDTO.setCode(201);
                resultDTO.setMessage(messageSource.getMessage("api.message.201", new String[]{"권한 등록"}, Locale.KOREA));
                resultDTO.setResult(resultId);
            }else{
                resultDTO.setState(false);
                resultDTO.setCode(400);
                resultDTO.setMessage(messageSource.getMessage("api.message.400", new String[]{"권한 조회"}, Locale.KOREA));
                resultDTO.setResult(null);
            }
        }
        return resultDTO;
    }

    @Override
    public ResultDTO deleteRole(Role role) {
        return null;
    }

    @Override
    public ResultDTO roleManageList(Role role) {
        resultDTO = new ResultDTO();
        List<Role> roleManageList = userRoleDAO.roleManageList(role);

        if(!DataLib.isEmpty(roleManageList)) {
            resultDTO.setState(true);
            resultDTO.setCode(200);
            resultDTO.setResult(roleManageList);
            resultDTO.setMessage(messageSource.getMessage("api.message.200", new String[]{"권한 조회"}, Locale.KOREA));
        } else {
            resultDTO.setState(false);
            resultDTO.setCode(400);
            resultDTO.setMessage(messageSource.getMessage("api.message.400", new String[]{"권한 조회"}, Locale.KOREA));
            resultDTO.setResult(null);
        }

        return resultDTO;
    }

    @Override
    public ResultDTO roleUserList(Role role) {
        resultDTO = new ResultDTO();
        List<RoleDTO> roleUserList = userRoleDAO.roleUserList(role);

        if(!DataLib.isEmpty(roleUserList)) {
            resultDTO.setState(true);
            resultDTO.setCode(200);
            resultDTO.setResult(roleUserList);
            resultDTO.setMessage(messageSource.getMessage("api.message.200", new String[]{"권한 조회"}, Locale.KOREA));
        } else {
            resultDTO.setState(false);
            resultDTO.setCode(400);
            resultDTO.setMessage(messageSource.getMessage("api.message.400", new String[]{"권한 조회"}, Locale.KOREA));
            resultDTO.setResult(null);
        }

        return resultDTO;
    }
}
