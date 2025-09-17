package com.innon.education.common.service;

import com.innon.education.auth.dto.request.LoginRequest;
import com.innon.education.common.repository.dto.MenuDTO;
import com.innon.education.common.repository.entity.LogChild;
import com.innon.education.common.repository.entity.LogEntity;
import com.innon.education.common.repository.model.Dept;
import com.innon.education.common.repository.model.Insa;
import com.innon.education.controller.dto.ResultDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Map;

public interface CommonService {
    ResultDTO findEduDeptList(Dept dept, Authentication auth);
    ResultDTO findEduInsaList(Insa insa);
    ResultDTO findFactoryCdByUserId(String user_id);

    ResultDTO findEduDeptUserList(Dept dept, Authentication auth);

    int saveLog(LogEntity logEntity);
    ResultDTO saveLogDetail(LogChild logChild);

    ResultDTO findLogList(LogEntity logEntity);

    ResultDTO updateInsa(Insa insa, HttpServletRequest request, Authentication auth);

    ResultDTO findCheckUserAuth(LoginRequest user, HttpServletRequest request, Authentication auth);
    
    ResultDTO findFileCheckUserAuth(String user, HttpServletRequest request, Authentication auth);

    ResultDTO getEduInsaList(Insa insa, HttpServletRequest request, Authentication auth);
    ResultDTO getEduPopupInsaList(Insa insa, HttpServletRequest request, Authentication auth);

    ResultDTO updateDept(List<Dept> deptList, HttpServletRequest request, Authentication auth);

    ResultDTO findGroupInfoByDeptCd(HttpServletRequest request, Authentication auth);

    ResultDTO findTopDeptByDeptCd(HttpServletRequest request, Authentication auth);

    ResultDTO sendOtp(LoginRequest login, HttpServletRequest request);

    ResultDTO certiCheck(LoginRequest login);

    ResultDTO changePassword(LoginRequest login);

    ResultDTO findRoleMenuList(MenuDTO menuDto, HttpServletRequest request, Authentication auth);

    ResultDTO saveMenuAuth(Map<String, Object> menuDto, HttpServletRequest request, Authentication auth);
}
