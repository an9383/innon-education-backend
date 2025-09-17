package com.innon.education.admin.manage.service;

import com.innon.education.controller.dto.ResultDTO;
import jakarta.servlet.http.HttpServletRequest;
import com.innon.education.admin.manage.repository.Manager;
import org.springframework.security.core.Authentication;


public interface ManagerService {
    ResultDTO findManagerList(Manager manager, HttpServletRequest request, Authentication authentication);

    ResultDTO saveManager(Manager manager, HttpServletRequest request, Authentication authentication);

    ResultDTO findManagerUserList(Manager manager, HttpServletRequest request, Authentication auth);

    ResultDTO updateManager(Manager manager, HttpServletRequest request, Authentication auth);

    ResultDTO findManagerDept(Manager manager, HttpServletRequest request, Authentication auth);
}
