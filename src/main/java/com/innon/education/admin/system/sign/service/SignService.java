package com.innon.education.admin.system.sign.service;

import org.springframework.security.core.Authentication;

import com.innon.education.admin.system.sign.repository.PlanSignManager;
import com.innon.education.admin.system.sign.repository.Sign;
import com.innon.education.code.controller.dto.ResultDTO;

import jakarta.servlet.http.HttpServletRequest;

public interface SignService {

	ResultDTO findSignList(Sign Sign, HttpServletRequest request, Authentication authentication);

	ResultDTO saveSign(Sign sign, HttpServletRequest request, Authentication authentication);

	ResultDTO findSignUserList(Sign signUser, HttpServletRequest request, Authentication auth);

	//ResultDTO updateSignApproveState(PlanSignManager planSignManager, HttpServletRequest request, Authentication auth);

	ResultDTO updateSign(Sign sign, HttpServletRequest request, Authentication auth);

	ResultDTO findApproverList(PlanSignManager planSignManager, HttpServletRequest request, Authentication auth);

	ResultDTO deleteSign(Sign sign, HttpServletRequest request, Authentication auth);

	ResultDTO findPlanSignManager(PlanSignManager planSignManager, HttpServletRequest request, Authentication auth);

	ResultDTO updatePlanSign(PlanSignManager planSignManager, HttpServletRequest request, Authentication auth);

}
