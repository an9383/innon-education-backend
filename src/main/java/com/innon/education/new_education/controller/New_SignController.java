package com.innon.education.new_education.controller;

import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.innon.education.auth.entity.User;
import com.innon.education.common.dao.CommonDAO;
import com.innon.education.code.controller.dto.ResultDTO;
import com.innon.education.new_education.dto.CurrentSignDTO;
import com.innon.education.new_education.dto.New_SearchDTO;
import com.innon.education.new_education.service.NewSignService;

import jakarta.servlet.http.HttpServletRequest;

@CrossOrigin
@RestController
@RequestMapping("/api/new_plan")
public class New_SignController {

	@Autowired
	NewSignService newSignService;
	@Autowired
	MessageSource messageSource;

	ResultDTO resultDTO;
	@Autowired
	CommonDAO commonDAO;

	@PostMapping("/current/sign/list")
	public ResponseEntity<ResultDTO> findEduCurrentPlanList(@RequestBody New_SearchDTO searchDTO, HttpServletRequest request, Authentication auth) {
		resultDTO = new ResultDTO();
		try {

			User user = (User) auth.getPrincipal();
			searchDTO.setDept_cd(user.getDept_cd());
			if (searchDTO.getGroup_id() == 0) {
				searchDTO.setGroupList(commonDAO.findGroupInfoByDeptCd(user.getDept_cd()));
			}
			//            List<Dept> deptList = commonDAO.findTopDeptByDeptCd(user.getDept_cd());
			//            searchDTO.setDeptList(deptList);

			//전자결재 목록
			List<CurrentSignDTO> currentSignList = newSignService.currentSignList(searchDTO);
			resultDTO.setState(true);
			resultDTO.setCode(200);
			resultDTO.setMessage(messageSource.getMessage("api.message.200", new String[] { "결재 현황" }, Locale.KOREA));
			resultDTO.setResult(currentSignList);
		}
		catch (Exception e) {
			System.out.println(e.getCause().toString());
			resultDTO.setState(false);
			resultDTO.setCode(400);
			resultDTO.setMessage(messageSource.getMessage("api.message.400", new String[] { "결재 현황" }, Locale.KOREA));
			resultDTO.setResult(null);

		}

		return ResponseEntity.ok(resultDTO);
	}

	// 이수철 : 미사용
	@PostMapping("/sign/detail")
 	public ResponseEntity<ResultDTO> planSignDetailList(@RequestBody New_SearchDTO searchDTO, HttpServletRequest request, Authentication auth) {
		resultDTO = new ResultDTO();
		try {

			User user = (User) auth.getPrincipal();
			searchDTO.setDept_cd(user.getDept_cd());
			if (searchDTO.getGroup_id() == 0) {
				searchDTO.setGroupList(commonDAO.findGroupInfoByDeptCd(user.getDept_cd()));
			}
			//            List<Dept> deptList = commonDAO.findTopDeptByDeptCd(user.getDept_cd());
			//            searchDTO.setDeptList(deptList);
			List<CurrentSignDTO> currentSignList = newSignService.planSignDetailList(searchDTO);
			resultDTO.setState(true);
			resultDTO.setCode(200);
			resultDTO.setMessage(messageSource.getMessage("api.message.200", new String[] { "결재 이력" }, Locale.KOREA));
			resultDTO.setResult(currentSignList);
		}
		catch (Exception e) {
			System.out.println(e.getCause().toString());
			resultDTO.setState(false);
			resultDTO.setCode(400);
			resultDTO.setMessage(messageSource.getMessage("api.message.400", new String[] { "결재 이력" }, Locale.KOREA));
			resultDTO.setResult(null);

		}

		return ResponseEntity.ok(resultDTO);
	}

}
