package com.innon.education.admin.group.service;

import com.innon.education.admin.group.repository.Group;
import com.innon.education.controller.dto.ResultDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;

public interface GroupService {
    ResultDTO findGroupList(Group group, HttpServletRequest request, Authentication auth);

    ResultDTO saveGroup(Group group, HttpServletRequest request, Authentication auth);

    ResultDTO findGroupDeptList(Group group, HttpServletRequest request, Authentication auth);

    ResultDTO updateGroup(Group group, HttpServletRequest request, Authentication auth);

    ResultDTO findGroupDept(Group group, HttpServletRequest request, Authentication auth);
}
