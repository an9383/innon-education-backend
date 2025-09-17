package com.innon.education.common.service;

import com.innon.education.auth.entity.Role;
import com.innon.education.controller.dto.ResultDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;

public interface UserRoleService {
    ResultDTO findUserRoleList(Role role);

    ResultDTO updateRole(Role role, HttpServletRequest request, Authentication auth);

    ResultDTO saveRole(Role role, HttpServletRequest request, Authentication auth);

    ResultDTO deleteRole(Role role);

    ResultDTO roleManageList(Role role);

    ResultDTO roleUserList(Role role);
}
