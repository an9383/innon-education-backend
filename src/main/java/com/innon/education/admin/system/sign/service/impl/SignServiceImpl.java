package com.innon.education.admin.system.sign.service.impl;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.innon.education.admin.system.sign.dao.SignDAO;
import com.innon.education.admin.system.sign.repository.PlanSign;
import com.innon.education.admin.system.sign.repository.PlanSignManager;
import com.innon.education.admin.system.sign.repository.Sign;
import com.innon.education.admin.system.sign.repository.SignUser;
import com.innon.education.admin.system.sign.service.SignService;
import com.innon.education.annual.plan.dao.AnnualPlanDAO;
import com.innon.education.annual.plan.repository.dto.AnnualPlanDTO;
import com.innon.education.annual.plan.repository.entity.AnnualPlanEntity;
import com.innon.education.annual.plan.repository.entity.AnnualPlanSearchEntity;
import com.innon.education.annual.plan.repository.model.AnnualPlan;
import com.innon.education.auth.dto.request.LoginRequest;
import com.innon.education.auth.entity.User;
import com.innon.education.auth.repository.UserRepository;
import com.innon.education.common.dao.CommonDAO;
import com.innon.education.common.repository.dto.CodeDTO;
import com.innon.education.common.repository.entity.LogEntity;
import com.innon.education.common.repository.model.Dept;
import com.innon.education.common.repository.model.EmailMessage;
import com.innon.education.common.service.CommonService;
import com.innon.education.common.service.EmailService;
import com.innon.education.common.util.DataLib;
import com.innon.education.controller.dto.ResultDTO;
import com.innon.education.dao.CodeDAO;
import com.innon.education.jwt.dto.CustomUserDetails;
import com.innon.education.library.document.dao.DocumentDAO;
import com.innon.education.library.document.repasitory.dto.DocumentDTO;
import com.innon.education.library.document.repasitory.dto.PlanDocument;
import com.innon.education.library.document.repasitory.entity.DocumentApplyEntity;
import com.innon.education.library.document.repasitory.model.Document;
import com.innon.education.library.document.repasitory.model.DocumentMemo;
import com.innon.education.library.factory.dao.FactoryDAO;
import com.innon.education.management.plan.dao.ManagementPlanDAO;
import com.innon.education.management.plan.repository.dto.ManagementPlanDTO;
import com.innon.education.management.plan.repository.entity.HelpUrlEntity;
import com.innon.education.management.plan.repository.entity.ManagementPlanEntity;
import com.innon.education.management.plan.repository.entity.ManagementPlanUserEntity;
import com.innon.education.management.plan.repository.entity.QuestionInfoDetailEntity;
import com.innon.education.management.plan.repository.entity.QuestionInfoEntity;
import com.innon.education.management.plan.repository.model.ManagementPlan;
import com.innon.education.management.plan.repository.model.PlanQms;
import com.innon.education.management.result.dao.ManagementResultDAO;
import com.innon.education.management.result.repository.dto.ManagementResultDTO;
import com.innon.education.management.result.repository.dto.ReEducationDTO;
import com.innon.education.management.result.repository.entity.ReEducationEntity;
import com.innon.education.management.result.repository.model.ManagementResult;
import com.innon.education.qualified.current.dao.CurrentDAO;
import com.innon.education.qualified.current.repository.dto.JobSkillDTO;
import com.innon.education.qualified.current.repository.entity.JobSkillEntity;
import com.innon.education.qualified.current.repository.model.JobRevision;
import com.innon.education.qualified.current.repository.model.JobSkill;

import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;

@Service
public class SignServiceImpl implements SignService {
	private ResultDTO resultDTO;
	@Autowired
	SignDAO signDAO;
	@Autowired
	MessageSource messageSource;
	@Autowired
	CommonService commonService;
	@Autowired
	ManagementPlanDAO managementPlanDAO;
	@Autowired
	CodeDAO codeDAO;
	@Autowired
	FactoryDAO factoryDAO;
	@Autowired
	DocumentDAO documentDAO;
	@Autowired
	CommonDAO commonDAO;
	@Autowired
	EmailService emailService;

	@Autowired
	UserRepository userRepository;

	@Autowired
	AnnualPlanDAO annualPlanDAO;
	@Autowired
	CurrentDAO currentDAO;

	@Autowired
	ManagementResultDAO managementResultDAO;

	private final String G_TYPE_EDU_CODE			 = "comc11001"; //교육
	private final String G_TYPE_DOC_CODE			 = "comc11002"; //문서

	private final String G_APPROVE_REJECT_CODE		 = "coms11006";	//반려 코드
	private final String G_APPROVE_EXIT_CODE		 = "coms11001";	//결재종료
	private final String G_APPROVE_REQUEST_CODE		 = "coms11004";	//요청
	private final String G_APPROVE_WAIT_CODE		 = "coms11007";	//대기
	private final String G_APPROVE_CONFIRM_CODE		 = "coms11005";	//승인

	private final String G_PLAN_EDU_ANNUAL_DEPT_CODE = "edus11005"; //연간부서교육
	private final String G_PLAN_EDU_ANNUAL_INFO_CODE = "edus11001"; //연간공통교육

	private final String G_SIGN_PLAN_CODE			 = "comi1s3";	//교육계획
	private final String G_SIGN_ANNUAL_INFO_CODE	 = "comi1s1";	//연간교육계획
	private final String G_SIGN_JOB_SKILL_CODE		 = "comi1s7";	//직무기술항목
	private final String G_SIGN_JOB_CODE			 = "comi1s6";	//직무요구항목
	private final String G_SIGN_ANNUAL_DEPT_CODE	 = "comi1s8";	//연간부서계획
	private final String G_SIGN_RESULT_CODE			 = "comi1s5";	//교육보고

	private final String G_SIGN_TYPE_ANNUAL_EXAMINE_CODE = "comi1s11005"; //연간계획검토

	private final String G_PROGRESS_START_CODE		 = "comi1s41001";	//교육진행배부
	private final String G_PROGRESS_APPROVE_CODE	 = "comi1s11002";	//계획배부작성

	@Override
	public ResultDTO findSignList(Sign sign, HttpServletRequest request, Authentication authentication) {
		resultDTO = new ResultDTO();
		User user = (User) authentication.getPrincipal();
		boolean chkUserRole = authentication.getAuthorities().stream().anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
		if (!chkUserRole) {
			sign.setGroupList(commonDAO.findGroupInfoByDeptCd(user.getDept_cd()));
		}
		resultDTO.setResult(signDAO.findSignList(sign));
		resultDTO.setCode(200);
		return resultDTO;
	}

	@Override
	public ResultDTO saveSign(Sign sign, HttpServletRequest request, Authentication authentication) {
		resultDTO = new ResultDTO();
		User user = (User) authentication.getPrincipal();

		sign.setSys_reg_user_id(authentication.getName());
		sign.setDept_cd(user.getDept_cd());

		int resultId = signDAO.saveSign(sign);
		if (resultId > 0) {
			int logId = commonService.saveLog(LogEntity.builder().table_id(sign.getId()).page_nm("sign").url_addr(request.getRequestURI()).state("insert").reg_user_id(authentication.getName()).build());
			try {
				/*Object obj=sign;
				for (Field field : obj.getClass().getDeclaredFields()){
					field.setAccessible(true);
					Object value=field.get(obj);
					resultDTO = commonService.saveLogDetail(LogChild
							.builder()
							.log_id(logId)
							.field(field.getName())
							.new_value(value.toString())
							.reg_user_id(authentication.getName())
							.build());
				}*/
			}
			catch (Exception e) {
				e.printStackTrace();
			}

			sign.getSignUser().forEach(signUser -> {
				signUser.setSign_id(sign.getId());
				signUser.setSys_upd_user_id(authentication.getName());
				int reUserId = signDAO.saveSignUser(signUser);
			});

			resultDTO.setState(true);
			resultDTO.setCode(201);
			resultDTO.setMessage(messageSource.getMessage("api.message.201", new String[] { "결재" }, Locale.KOREA));
			resultDTO.setResult(sign.getId());
		}
		else {
			resultDTO.setState(false);
			resultDTO.setCode(400);
			resultDTO.setMessage(messageSource.getMessage("api.message.400", new String[] { "결재" }, Locale.KOREA));
			resultDTO.setResult(null);
		}

		return resultDTO;
	}

	@Override
	public ResultDTO findSignUserList(Sign sign, HttpServletRequest request, Authentication auth) {
		resultDTO = new ResultDTO();
		List<SignUser> signUserList = signDAO.findSignUserList(sign.getId());

		if (!DataLib.isEmpty(signUserList)) {
			//			int logId = commonService.saveLog(LogEntity.builder()
			//					.table_id(sign.getId())
			//					.page_nm("sign")
			//					.url_addr(request.getRequestURI())
			//					.state("view")
			//					.reg_user_id(auth.getName())
			//					.build());

			resultDTO.setState(true);
			resultDTO.setCode(200);
			resultDTO.setMessage(messageSource.getMessage("api.message.200", new String[] { "결재권자" }, Locale.KOREA));
			resultDTO.setResult(signUserList);
		}
		else {
			resultDTO.setState(false);
			resultDTO.setCode(400);
			resultDTO.setMessage(messageSource.getMessage("api.message.400", new String[] { "결재권자" }, Locale.KOREA));
			resultDTO.setResult(null);
		}

		return resultDTO;
	}

	// 이수철 : 미사용
	/*
	@Override
	public ResultDTO updateSignApproveState(PlanSignManager planSignManager, HttpServletRequest request, Authentication auth) {
		resultDTO = new ResultDTO();
		int signOrderNum = planSignManager.getOrder_num();
	
		PlanSignManager searchEntity = planSignManager;
		int topOrderNum = signOrderNum + 1;
		searchEntity.setOrder_num(topOrderNum);
		PlanSignManager topApproverUser = signDAO.findTopApprover(planSignManager);
	
		int beforeOrderNum = signOrderNum - 1;
		searchEntity.setOrder_num(beforeOrderNum);
		PlanSignManager beforeApproverUser = signDAO.findTopApprover(planSignManager);
	
		if (beforeApproverUser != null) {
			if (beforeApproverUser.getState().equals("REQUEST")) {
				resultDTO.setState(false);
				resultDTO.setCode(405);
				resultDTO.setMessage(beforeOrderNum + "차 결재자가 미결재한 상태입니다.");
				resultDTO.setResult(null);
			}
			else if (beforeApproverUser.getState().equals("APPROVED")) {
				if (topApproverUser != null) {
					System.out.println(beforeOrderNum + "차 결재자 승인 완료");
					int update = signDAO.updateSignApproveState(planSignManager);
					resultDTO.setState(true);
					resultDTO.setCode(202);
					resultDTO.setMessage(messageSource.getMessage("api.message.202", new String[] { "결재승인", "" }, Locale.KOREA));
					resultDTO.setResult(update);
				}
				else {
					System.out.println("최종 결재권자");
					int update = signDAO.updateSignApproveState(planSignManager);
					resultDTO.setState(true);
					resultDTO.setCode(202);
					resultDTO.setMessage(messageSource.getMessage("api.message.202", new String[] { "결재승인", "" }, Locale.KOREA));
					resultDTO.setResult(update);
					if (planSignManager.getState().equals("APPROVED")) {
						if (planSignManager.getFlag().equals("edu")) {
							CodeDTO stateCode = codeDAO.findByCodeName(planSignManager.getCode_name());
							ManagementPlanEntity planEntity = new ManagementPlanEntity();
							planEntity.setState(String.valueOf(stateCode.getId()));
							planEntity.setId(planSignManager.getPlan_id());
							managementPlanDAO.updateEducationPlan(planEntity);
						}
						else if (planSignManager.getFlag().equals("doc")) {
							DocumentApplyEntity docApplyEntity = new DocumentApplyEntity();
							docApplyEntity.setDocument_id(planSignManager.getPlan_id());
							docApplyEntity.setStatus(planSignManager.getCode_name());
							factoryDAO.updateDocumentLoan(docApplyEntity);
							documentDAO.updateDocumentConfirmDate(planSignManager.getPlan_id());
						}
					}
				}
			}
			else {
				resultDTO.setState(false);
				resultDTO.setCode(405);
				resultDTO.setMessage(beforeOrderNum + "차 결재자가 반려/기각한 결재입니다.");
				resultDTO.setResult(null);
			}
		}
		else {
			if (beforeOrderNum == 0) {
				System.out.println("최초 결재자");
				int update = signDAO.updateSignApproveState(planSignManager);
				resultDTO.setState(true);
				resultDTO.setCode(202);
				resultDTO.setMessage(messageSource.getMessage("api.message.202", new String[] { "결재승인", "수정" }, Locale.KOREA));
				resultDTO.setResult(update);
				if (planSignManager.getState().equals("APPROVED") && topApproverUser == null) {
					if (planSignManager.getFlag().equals("edu")) {
						CodeDTO stateCode = codeDAO.findByCodeName("EDUPLANSTATEPLANAPPROVE");
						ManagementPlanEntity planEntity = new ManagementPlanEntity();
						planEntity.setState(String.valueOf(stateCode.getId()));
						planEntity.setId(planSignManager.getPlan_id());
						managementPlanDAO.updateEducationPlan(planEntity);
					}
					else if (planSignManager.getFlag().equals("doc")) {
						DocumentApplyEntity docApplyEntity = new DocumentApplyEntity();
						docApplyEntity.setDocument_id(planSignManager.getPlan_id());
						docApplyEntity.setStatus(planSignManager.getCode_name());
						factoryDAO.updateDocumentLoan(docApplyEntity);
					}
				}
			}
		}
	
		return resultDTO;
	}
	*/
	
	@Override
	public ResultDTO updateSign(Sign sign, HttpServletRequest request, Authentication auth) {
		resultDTO = new ResultDTO();
		sign.setSys_reg_user_id(auth.getName());
		int update = signDAO.updateSign(sign);
		if (update > 0) {
			SignUser delSignUser = new SignUser();
			delSignUser.setSign_id(sign.getId());
			delSignUser.setSys_upd_user_id(auth.getName());
			signDAO.deleteSignUser(delSignUser);

			if (sign.getSignUser() != null) {
				sign.getSignUser().forEach(signUser -> {
					signUser.setSys_upd_user_id(auth.getName());
					signUser.setSign_id(sign.getId());
					signDAO.saveSignUser(signUser);
				});
			}

			resultDTO.setState(true);
			resultDTO.setCode(202);
			resultDTO.setMessage(messageSource.getMessage("api.message.202", new String[] { "전자결재", "수정" }, Locale.KOREA));
			resultDTO.setResult(sign.getId());
		}
		else {
			resultDTO.setState(false);
			resultDTO.setCode(400);
			resultDTO.setMessage(messageSource.getMessage("api.message.400", new String[] { "전자결재 수정" }, Locale.KOREA));
			resultDTO.setResult(null);
		}

		return resultDTO;
	}

	@Override
	public ResultDTO findApproverList(PlanSignManager planSignManager, HttpServletRequest request, Authentication auth) {
		resultDTO = new ResultDTO();
		List<PlanSignManager> approverList = signDAO.findApproverList(planSignManager);

		if (!DataLib.isEmpty(approverList)) {
			resultDTO.setState(true);
			resultDTO.setCode(200);
			resultDTO.setMessage(messageSource.getMessage("api.message.200", new String[] { "결재자" }, Locale.KOREA));
			resultDTO.setResult(approverList);
		}
		else {
			resultDTO.setState(false);
			resultDTO.setCode(400);
			resultDTO.setMessage(messageSource.getMessage("api.message.400", new String[] { "결재자" }, Locale.KOREA));
			resultDTO.setResult(null);
		}

		return resultDTO;
	}

	@Override
	public ResultDTO deleteSign(Sign sign, HttpServletRequest request, Authentication auth) {
		resultDTO = new ResultDTO();
		sign.setSys_reg_user_id(auth.getName());
		int update = signDAO.deleteSign(sign);

		if (update > 0) {
			SignUser delSignUser = new SignUser();
			delSignUser.setSign_id(sign.getId());
			delSignUser.setSys_upd_user_id(auth.getName());
			signDAO.deleteSignUser(delSignUser);

			resultDTO.setState(true);
			resultDTO.setCode(202);
			resultDTO.setMessage(messageSource.getMessage("api.message.202", new String[] { "전자결재", "삭제" }, Locale.KOREA));
			resultDTO.setResult(sign.getId());
		}
		else {
			resultDTO.setState(false);
			resultDTO.setCode(400);
			resultDTO.setMessage(messageSource.getMessage("api.message.400", new String[] { "전자결재 삭제" }, Locale.KOREA));
			resultDTO.setResult(null);
		}

		return resultDTO;
	}

	@Override
	public ResultDTO findPlanSignManager(PlanSignManager planSignManager, HttpServletRequest request, Authentication auth) {
		resultDTO = new ResultDTO();
		User user = (User) auth.getPrincipal();
		try {
			List<Dept> deptList = commonDAO.findTopDeptByDeptCd(user.getDept_cd());
			if (!DataLib.isEmpty(deptList)) {
				planSignManager.setDeptList(deptList);
				List<PlanSignManager> planSignManagerLIst = signDAO.findPlanSignManager(planSignManager);
				if (!DataLib.isEmpty(planSignManagerLIst)) {
					//					int logId = commonService.saveLog(LogEntity.builder()
					//							.table_id(planSignManager.getPlan_id())
					//							.page_nm("managerSignUser")
					//							.url_addr(request.getRequestURI())
					//							.state("view")
					//							.reg_user_id(auth.getName())
					//							.build());

					resultDTO.setState(true);
					resultDTO.setCode(200);
					resultDTO.setMessage(messageSource.getMessage("api.message.200", new String[] { "결재 승인자" }, Locale.KOREA));
					resultDTO.setResult(planSignManagerLIst);
				}
			}
		}
		catch (Exception e) {
			resultDTO.setState(false);
			resultDTO.setCode(400);
			resultDTO.setMessage(messageSource.getMessage("api.message.400", new String[] { "결재 승인자 조회" }, Locale.KOREA));
			resultDTO.setResult(null);
		}

		return resultDTO;
	}

	@Transactional
	@Override
	public ResultDTO updatePlanSign(PlanSignManager planSignManager, HttpServletRequest request, Authentication auth) {
		resultDTO = new ResultDTO();
		resultDTO = commonService.findCheckUserAuth(planSignManager.getUser(), request, auth);
		String returnUrl = "doc".equals(planSignManager.getFlag()) ? "https://dip.inno-n.com/dms/mypage" : "https://dip.inno-n.com/lms/mypage";
		if (resultDTO.getCode() == 200) {
			String state = "";
			String emailContent = "";
			try {
				//signform
				//기 입력 된 결재정보 중 가장 최근의 plan_sign_id 를 signform(교육계획/교육보고...) 기준으로 가져온다. (tb_plan_sign_manager)
				List<PlanSignManager> planSignManagerList = signDAO.findApproverList(planSignManager);
				if (G_APPROVE_REJECT_CODE.equals(planSignManager.getState())) {
					System.out.println("결재상태 초기화");
					state = "반려";

					// 최종상태값 변경 : tb_plan_sign
					PlanSign planSign = new PlanSign();
					planSign.setPlan_id(planSignManager.getPlan_id());
					planSign.setSign_category(planSignManager.getSign_category());// 교육/문서
					planSign.setStatus(planSignManager.getState());
					planSign.setSys_upd_user_id(auth.getName()); // 이수철 : 이전에 왜 막았나?
					signDAO.updatePlanSign(planSign);

					// 결재가 문서일 경우 문서 상태값 변경을 위한 로직 추가
					if (G_TYPE_DOC_CODE.equals(planSignManager.getSign_category())) {
						DocumentApplyEntity docApplyEntity = new DocumentApplyEntity();
						docApplyEntity.setDocument_id(planSignManager.getPlan_id());
						if (planSignManager.getSignform().equals("comi2s1")) { // 대출신청 >>  입고
							docApplyEntity.setStatus("docs11003");
						}
						else if (planSignManager.getSignform().equals("comi2s2")) { // 폐기신청 >> 만료
							docApplyEntity.setStatus("docs11005");
						}
						else if (planSignManager.getSignform().equals("comi2s3")) { // 연장신청 >> 대출
							docApplyEntity.setStatus("docs11001");
							docApplyEntity.setIsSetCurrentEndDate(-1);// current_end_date_req = null 처리
						}
						factoryDAO.updateDocumentLoan(docApplyEntity);
						updateRejectPlanSignManager(planSignManager, planSignManagerList, auth);
					}
					//연간교육계획 또는 연간부서계획
					else if (G_SIGN_ANNUAL_INFO_CODE.equals(planSignManager.getSignform()) || G_SIGN_ANNUAL_DEPT_CODE.equals(planSignManager.getSignform())) {
						if (planSignManager.getDeptPlanList().size() > 0) { //연간계획검토 복수 반려 일시 edu_type edus11001

							boolean infoAnnualFlag = planSignManager.getDeptPlanList().stream().anyMatch(annualPlan -> G_PLAN_EDU_ANNUAL_INFO_CODE.equals(annualPlan.getEdu_type()));

							for (AnnualPlan rejectAnnual : planSignManager.getDeptPlanList()) {
								//annualPlan.setId(annualPlan.getId());
								rejectAnnual.setProgress_type(G_APPROVE_REJECT_CODE);
								if (infoAnnualFlag) {							 //공통 계획이 포함되어있을경우 부서결재를 막아준다.
									rejectAnnual.setUse_flag('D');				//동작 못하게 N :수정가능, R :반려 수정 가능 전체 반려시 동작 못하게 코드 D:
								}
								else {
									rejectAnnual.setUse_flag('R');
								}

								AnnualPlanEntity updateAnnual = new AnnualPlanEntity(rejectAnnual);
								if (G_PLAN_EDU_ANNUAL_INFO_CODE.equals(rejectAnnual.getEdu_type())) {   //공통계획
									continue;
								}
								// tb_annual_education : 연간교육
								int resultCnt = annualPlanDAO.updateAnnualPlan(updateAnnual);

								if (resultCnt > 0) {
									PlanSignManager deptSignUser = new PlanSignManager();
									deptSignUser.setSign_category(G_TYPE_EDU_CODE);
									deptSignUser.setSignform(G_SIGN_ANNUAL_DEPT_CODE);
									deptSignUser.setPlan_id(updateAnnual.getId());

									List<PlanSignManager> dept_planSignList = signDAO.findApproverList(deptSignUser);

									for (PlanSignManager psm : dept_planSignList) {
										String signUserState = G_APPROVE_WAIT_CODE;

										// update planSignManager
										psm.setUser_id(psm.getOrder_num() == 1 ? psm.getSys_reg_user_id() : null);
										psm.setComment("");
										psm.setState(signUserState);
										//psm.setPlan_sign_id(planSignManager.getPlan_sign_id());
										signDAO.updatePlanSignManager(psm);

										//										if (psm.getOrder_num() == planSignManager.getOrder_num()) {
										//											// insert planSignDetail
										//											PlanSignDetail signDetail = new PlanSignDetail();
										//											signDetail.setPlan_sign_id(psm.getPlan_sign_id());
										//											signDetail.setComment(planSignManager.getComment());
										//											signDetail.setSign_manager_id(psm.getId());
										//											signDetail.setState(planSignManager.getState());
										//											signDetail.setUser_id(auth.getName());
										//											signDetail.setSys_reg_user_id(auth.getName());
										//											signDetail.setPlan_sign_id(psm.getPlan_sign_id());
										//											signDAO.savePlanSignDetail(signDetail);
										//										}
									}
								}
							}

						}
						else { // 단수의 반려일시
							AnnualPlan paramAnnual = new AnnualPlan();
							paramAnnual.setId(planSignManager.getPlan_id());
							paramAnnual.setProgress_type(G_APPROVE_REJECT_CODE);
							paramAnnual.setUse_flag('R');
							AnnualPlanEntity updateAnnual = new AnnualPlanEntity(paramAnnual);
							annualPlanDAO.updateAnnualPlan(updateAnnual);
							updateRejectPlanSignManager(planSignManager, planSignManagerList, auth);
						}
					}
					else if (G_SIGN_PLAN_CODE.equals(planSignManager.getSignform())) { //교육계획
						ManagementPlanEntity planEntity = new ManagementPlanEntity();
						planEntity.setProgress_type(G_APPROVE_REJECT_CODE);
						planEntity.setUse_flag('R');
						planEntity.setId(planSignManager.getPlan_id());
						managementPlanDAO.updateEducationPlan(planEntity);
						updateRejectPlanSignManager(planSignManager, planSignManagerList, auth);
					}
					else if (G_SIGN_JOB_CODE.equals(planSignManager.getSignform())) { //직무요구항목 (직무요구서)
						JobRevision updateJob = new JobRevision();
						updateJob.setUse_flag('R');
						updateJob.setId(planSignManager.getPlan_id());
						currentDAO.updateJobRevision(updateJob);
						updateRejectPlanSignManager(planSignManager, planSignManagerList, auth);
					}
					else if (G_SIGN_RESULT_CODE.equals(planSignManager.getSignform())) { //교육보고
						ManagementPlanEntity planEntity = new ManagementPlanEntity();
						//반려시 교육진행완료 또는 반려로 교육계획을 업데이트 할지 체크 필요 : 이수철
						//S,E는 결과버튼이 산다.
						//N : 임시저장 , W : wait 승인대기(나무색 배경색) , R : 반려 , S,C : 승인완료
						//C : cancel (결재종료) , S : start , E : end ???
						planEntity.setProgress_type(G_APPROVE_REJECT_CODE);//반려
						//planEntity.setProgress_type("comi1s41002");//교육진행완료
						planEntity.setUse_flag('R');
						planEntity.setId(planSignManager.getPlan_id());
						managementPlanDAO.updateEducationPlan(planEntity);
						/*
						planEntity.setProgress_type(planSignManagerList.get(0).getSign_type());
						planEntity.setUse_flag('A');
						planEntity.setId(planSignManager.getPlan_id());
						managementPlanDAO.updateEducationPlan(planEntity);
						*/
						updateRejectPlanSignManager(planSignManager, planSignManagerList, auth);
					}
					else if (G_SIGN_JOB_SKILL_CODE.equals(planSignManager.getSignform())) { //직무기술항목
						JobSkill updateSkill = new JobSkill();
						updateSkill.setUse_flag('R');
						updateSkill.setId(planSignManager.getPlan_id());
						JobSkillEntity updateSkillEntity = new JobSkillEntity(updateSkill);
						currentDAO.updateJobSkill(updateSkillEntity);
						updateRejectPlanSignManager(planSignManager, planSignManagerList, auth);
					}

					//					for (PlanSignManager psm : planSignList) {
					//						String signUserState = psm.getOrder_num() == 1 ? g_approve_request_code : g_approve_wait_code;
					//
					//						// update planSignManager
					//						psm.setUser_id(psm.getOrder_num() == 1 ?psm.getSys_reg_user_id():null);
					//						psm.setComment("");
					//						psm.setState(signUserState);
					//						psm.setPlan_sign_id(planSignManager.getPlan_sign_id());
					//						signDAO.updatePlanSignManager(psm);
					//
					//						if (psm.getOrder_num() == planSignManager.getOrder_num()) {
					//							// insert planSignDetail
					//							PlanSignDetail signDetail = new PlanSignDetail();
					//							signDetail.setPlan_sign_id(psm.getPlan_sign_id());
					//							signDetail.setComment(planSignManager.getComment());
					//							signDetail.setSign_manager_id(psm.getId());
					//							signDetail.setState(planSignManager.getState());
					//							signDetail.setUser_id(auth.getName());
					//							signDetail.setSys_reg_user_id(auth.getName());
					//							signDetail.setPlan_sign_id(psm.getPlan_sign_id());
					//							signDAO.savePlanSignDetail(signDetail);
					//						}
					//					}
					
					// 이수철 : 공통모듈로 변경
					emailService.sendMail(planSignManager.getSys_reg_user_id(), planSignManager, "last", returnUrl);

				}
				else if (G_APPROVE_EXIT_CODE.equals(planSignManager.getState())) {
					//결재종료
					//결재선의 마지막 결재 권한자만 할 수 있다.
					//문서에는 종료가 없다.
					state = "종료";
					//결재종료 : 교육
					if (G_TYPE_EDU_CODE.equals(planSignManager.getSign_category())) {
						//연간교육계획
						if (G_SIGN_ANNUAL_INFO_CODE.equals(planSignManager.getSignform())) {
							//연간계획 최종 승인시에는 하위 계획들 전체 임시저장으로
							AnnualPlan paramAnnual = new AnnualPlan();
							paramAnnual.setId(planSignManager.getPlan_id());
							paramAnnual.setDelete_at('Y');
							paramAnnual.setUse_flag('C');
							paramAnnual.setProgress_type(G_APPROVE_EXIT_CODE);
							AnnualPlanEntity updateAnnual = new AnnualPlanEntity(paramAnnual);

							annualPlanDAO.updateAnnualPlan(updateAnnual);

							ManagementPlanEntity planEntity = new ManagementPlanEntity();
							planEntity.setParent_id(planSignManager.getPlan_id());

							List<ManagementPlanDTO> resultList = managementPlanDAO.educationPlanList(planEntity);
							for (ManagementPlanDTO managementPlanDTO : resultList) {
								planEntity.setProgress_type(G_APPROVE_EXIT_CODE);
								planEntity.setUse_flag('C'); //취소
								planEntity.setId(managementPlanDTO.getId());
								managementPlanDAO.updateEducationPlan(planEntity);
							}

						}
						//결재종료 : 교육계획
						else if (G_SIGN_PLAN_CODE.equals(planSignManager.getSignform())) {
							ManagementPlanEntity planEntity = new ManagementPlanEntity();
							planEntity.setProgress_type(G_APPROVE_EXIT_CODE);
							planEntity.setUse_flag('C'); //C취소
							planEntity.setDelete_at('Y');
							planEntity.setId(planSignManager.getPlan_id());
							managementPlanDAO.updateEducationPlan(planEntity);
						}
						//결재종료 : 직무요구항목
						else if (G_SIGN_JOB_CODE.equals(planSignManager.getSignform())) {
							JobRevision updateJob = new JobRevision();
							updateJob.setUse_flag('C');
							updateJob.setId(planSignManager.getPlan_id());
							updateJob.setDelete_at('Y');
							int resultCnt = currentDAO.updateJobRevision(updateJob);
						}
						//결재종료 : 직무기술항목
						else if (G_SIGN_JOB_SKILL_CODE.equals(planSignManager.getSignform())) {
							JobSkill updateSkill = new JobSkill();
							updateSkill.setUse_flag('C');
							updateSkill.setId(planSignManager.getPlan_id());
							JobSkillEntity updateSkillEntity = new JobSkillEntity(updateSkill);
							currentDAO.updateJobSkill(updateSkillEntity);
						}
						
						System.out.println("최종결재자");
						PlanSignManager planSignManagerLast = planSignManagerList.get(planSignManagerList.size() - 1);
						planSignManagerLast.setUser_id(auth.getName());
						planSignManagerLast.setState(planSignManager.getState());
						planSignManagerLast.setComment(planSignManager.getComment());
						planSignManagerLast.setSys_reg_user_id(auth.getName());
						planSignManagerLast.setPlan_sign_id(planSignManager.getPlan_sign_id());
						signDAO.updatePlanSignManager(planSignManagerLast);
						
						/*
						PlanSignDetail signDetail = new PlanSignDetail();
						signDetail.setPlan_sign_id(psm.getPlan_sign_id());
						signDetail.setComment(planSignManager.getComment());
						signDetail.setSign_manager_id(psm.getId());
						signDetail.setState(planSignManager.getState());
						signDetail.setUser_id(auth.getName());
						signDetail.setSys_reg_user_id(auth.getName());
						signDAO.savePlanSignDetail(signDetail);
						*/
						
						// 이수철 : emailContent 를 안구하는거 구하도록 변경 함 
						emailService.sendMail(planSignManagerLast.getSys_reg_user_id(), planSignManagerLast, "last", returnUrl);
					}
					else {
						throw new Exception("문서관리에는 결재종료가 없습니다!");
					}

				}
				else {
					state = "승인";
					if (!DataLib.isEmpty(planSignManagerList)) {
						int maxOrderNum = planSignManagerList.get(planSignManagerList.size() - 1).getOrder_num();
						if (planSignManager.getOrder_num() == maxOrderNum) {
							System.out.println("최종결재자");

							// 최종결재자 정보 (맨 뒤)
							PlanSignManager planSignManagerLast = planSignManagerList.get(planSignManagerList.size() - 1);
							planSignManagerLast.setUser_id(auth.getName());
							planSignManagerLast.setState(planSignManager.getState());		//승인(coms11005)
							planSignManagerLast.setComment(planSignManager.getComment());	//결재 비고
							planSignManagerLast.setSys_reg_user_id(auth.getName());
							planSignManagerLast.setPlan_sign_id(planSignManager.getPlan_sign_id());	//유형별결재ID
							signDAO.updatePlanSignManager(planSignManagerLast);

							/*
							PlanSignDetail signDetail = new PlanSignDetail();
							signDetail.setPlan_sign_id(planSignManagerLast.getPlan_sign_id());
							signDetail.setComment(planSignManager.getComment());
							signDetail.setSign_manager_id(planSignManagerLast.getId());
							signDetail.setState(planSignManager.getState());
							signDetail.setUser_id(auth.getName());
							signDetail.setSys_reg_user_id(auth.getName());
							signDAO.savePlanSignDetail(signDetail);
							*/

							//tb_plan_sign : 최종결재상태 업데이트
							PlanSign planSign = new PlanSign();
							planSign.setPlan_id(planSignManager.getPlan_id());
							planSign.setSign_category(planSignManager.getSignform());
							planSign.setStatus(planSignManagerLast.getSign_type()); //최종결재상태
							planSign.setSys_upd_user_id(auth.getName());
							signDAO.updatePlanSign(planSign);

							emailService.sendMail(planSignManagerLast.getSys_reg_user_id(), planSignManagerLast, "last", returnUrl);

							//최종결재자 + 승인 : 문서
							if (planSignManager.getSign_category().equals(G_TYPE_DOC_CODE)) {
								// 문서상태 업데이트 대출신청인경우 대출로 폐기신청인경우 폐기로
								// 25.04.02 대출 신청일 경우 대출이 아닌 대출 승인 상태로 변경
								DocumentApplyEntity docApplyEntity = new DocumentApplyEntity();
								docApplyEntity.setDocument_id(planSignManager.getPlan_id());
								if (planSignManager.getSignform().equals("comi2s1")) { // 대출신청
									docApplyEntity.setStatus("docs11009"); //대출승인
									docApplyEntity.setUpdate_type("sign");
								}
								else if (planSignManager.getSignform().equals("comi2s2")) { // 폐기신청
									docApplyEntity.setStatus("docs11002");//폐기
								}
								else if (planSignManager.getSignform().equals("comi2s3")) { // 연장신청
									docApplyEntity.setStatus("docs11001");//대출
									docApplyEntity.setIsSetCurrentEndDate(1);// current_end_date = current_end_date_req 처리
								}
								factoryDAO.updateDocumentLoan(docApplyEntity);
							}
							//최종결재자 + 승인 : 연간교육계획 : 최종 승인시에는 하위 계획들 전체 임시저장으로
							else if (G_SIGN_ANNUAL_INFO_CODE.equals(planSignManager.getSignform())) {
								ManagementPlanEntity planEntity = new ManagementPlanEntity();
								planEntity.setParent_id(planSignManager.getPlan_id());
								AnnualPlan paramAnnual = new AnnualPlan();
								paramAnnual.setId(planSignManager.getPlan_id());
								//AnnualPlanDTO annualPlan = annualPlanDAO.findAnnualPlan(paramAnnual);
								paramAnnual.setProgress_type(planSignManagerLast.getSign_type());
								paramAnnual.setUse_flag('S');
								AnnualPlanEntity updateAnnual = new AnnualPlanEntity(paramAnnual);
								int resultCnt = annualPlanDAO.updateAnnualPlan(updateAnnual);

								List<ManagementPlanDTO> resultList = managementPlanDAO.educationPlanList(planEntity);

								for (ManagementPlanDTO managementPlanDTO : resultList) {
									planEntity.setProgress_type(G_PROGRESS_START_CODE);
									planEntity.setUse_flag('N'); //s는 시작 상태 .
									planEntity.setId(managementPlanDTO.getId());
									managementPlanDAO.updateEducationPlan(planEntity);
								}
							}
							//최종결재자 + 승인 : 교육계획
							else if (G_SIGN_PLAN_CODE.equals(planSignManager.getSignform())) {
								ManagementPlanEntity planEntity = new ManagementPlanEntity();
								planEntity.setProgress_type(G_PROGRESS_START_CODE); //교육진행배부
								planEntity.setUse_flag('S'); //s는 시작 상태 .
								planEntity.setId(planSignManager.getPlan_id());
								//교육계획 테이블 교육진행배부 상태로 업데이트 (tb_education_plan)
								managementPlanDAO.updateEducationPlan(planEntity);
								ManagementPlan managementPlan = new ManagementPlan();
								managementPlan.setId(planSignManager.getPlan_id());
								ManagementPlanDTO resultPlan = managementPlanDAO.educationPlanDetail(managementPlan);
								if ("edus11002".equals(resultPlan.getStatus())) { // edus11002=QMS 교육업무
									PlanQms planQms = new PlanQms();
									planQms.setPlan_id(planSignManager.getPlan_id());
									managementResultDAO.saveQmsRes(planQms);
								}
							}
							//최종결재자 + 승인 : 교육보고
							else if (G_SIGN_RESULT_CODE.equals(planSignManager.getSignform())) {
								ManagementPlanEntity planEntity = new ManagementPlanEntity();
								planEntity.setProgress_type(planSignManager.getSign_type());
								planEntity.setUse_flag('E'); //교육 결과보고 완료.
								planEntity.setId(planSignManager.getPlan_id());
								managementPlanDAO.updateEducationPlan(planEntity);

								ManagementResult paramResult = new ManagementResult();
								paramResult.setPlan_id(planSignManager.getPlan_id());
								ManagementResultDTO reDTO = managementResultDAO.managementResultDetail(paramResult);
								if ('Y' == reDTO.getRe_edu_yn()) {
									ManagementPlan paramPlan = new ManagementPlan();
									paramPlan.setId(planSignManager.getPlan_id());
									ManagementPlanDTO resultDTO = managementPlanDAO.educationPlanDetail(paramPlan);
									resultDTO.setId(0);
									resultDTO.setProgress_type(G_PROGRESS_START_CODE);
									resultDTO.setUse_flag('N'); //임시저장
									resultDTO.setRe_edu_cnt(resultDTO.getRe_edu_cnt() + 1);
									resultDTO.setPlan_start_date(reDTO.getRe_edu_start_date());
									resultDTO.setPlan_end_date(reDTO.getRe_edu_end_date());
									resultDTO.setParent_id(reDTO.getId());
									ManagementPlan savePlan = new ManagementPlan(resultDTO);
									managementPlanDAO.savePlan(savePlan);	//교육 복제
									List<ReEducationDTO> manageResultList = managementResultDAO.reduceStudentList(planSignManager.getPlan_id());
									for (ReEducationDTO reEducationDTO : manageResultList) {
										LoginRequest setUser = new LoginRequest();
										setUser.setUser_id(reEducationDTO.getRe_user_id());
										CustomUserDetails res_user = userRepository.findUser(setUser);
										ReEducationEntity paramReduEntity = new ReEducationEntity();
										ManagementPlanUserEntity managementPlanUserEntity = new ManagementPlanUserEntity();
										managementPlanUserEntity.setPlan_id(savePlan.getId());
										managementPlanUserEntity.setQms_user_id(paramReduEntity.getRe_user_id());
										managementPlanUserEntity.setDept_cd(res_user.getDept_cd());
										managementPlanUserEntity.setDept_nm(res_user.getDept_nm());

										managementPlanDAO.savePlanUser(managementPlanUserEntity);
									}

									//교육 문서
									List<DocumentDTO> planDoc = managementPlanDAO.findManagementPlanDocument(planSignManager.getPlan_id());

									for (DocumentDTO documentDTO : planDoc) {
										PlanDocument setDocument = new PlanDocument();
										setDocument.setD_id(documentDTO.getId());
										setDocument.setPlan_id(savePlan.getId());
										managementPlanDAO.savePlanDocument(setDocument);
									}
									//교육 내용
									List<Map<String, Object>> setHelp = managementPlanDAO.findManagementHelpUrl(planSignManager.getPlan_id());
									for (Map<String, Object> stringObjectMap : setHelp) {
										HelpUrlEntity setUrl = new HelpUrlEntity(savePlan.getId(), stringObjectMap);
										managementPlanDAO.savePlanContent(setUrl);
									}
									//교육 문제
									List<Map<String, Object>> questionList = managementPlanDAO.findQuestionInfo(planSignManager.getPlan_id());

									for (Map<String, Object> stringObjectMap : questionList) {
										int beforeId = (int) stringObjectMap.get("id");

										QuestionInfoEntity questionInfoEntity = new QuestionInfoEntity(savePlan.getId(), stringObjectMap);
										managementPlanDAO.saveQuestionInfo(questionInfoEntity);

										List<Map<String, Object>> qDetail = managementPlanDAO.findQuestionDetail(beforeId);

										for (Map<String, Object> qDet : qDetail) {
											QuestionInfoDetailEntity setQDetail = new QuestionInfoDetailEntity(questionInfoEntity.getId(), qDet);
											managementPlanDAO.saveQuestionInfoDetail(setQDetail);
										}
									}
								}

							}
							//최종결재자 + 승인 : 직무요구항목
							else if (G_SIGN_JOB_CODE.equals(planSignManager.getSignform())) {
								JobRevision paramJobRevision = new JobRevision();
								paramJobRevision.setId(planSignManager.getPlan_id());
								JobRevision result = currentDAO.findJobRevision(paramJobRevision);
								JobRevision checkJob = new JobRevision();
								checkJob.setUse_flag('S');
								checkJob.setQualified_id(result.getQualified_id());
								checkJob.setGroup_id(planSignManager.getGroup_id());
								List<JobRevision> jobRevisionList = currentDAO.findJobRevisionList(checkJob);

								for (JobRevision jobRevision : jobRevisionList) {

									JobRevision cancelJob = new JobRevision();
									cancelJob.setUse_flag('C');
									cancelJob.setId(jobRevision.getId());
									currentDAO.updateJobRevision(cancelJob);
								}

								JobRevision updateJob = new JobRevision();
								updateJob.setUse_flag('S');
								updateJob.setId(planSignManager.getPlan_id());
								int resultCnt = currentDAO.updateJobRevision(updateJob);
							}
							//최종결재자 + 승인 : 직무기술항목
							else if (G_SIGN_JOB_SKILL_CODE.equals(planSignManager.getSignform())) {
								PlanSign paramSign = new PlanSign();
								paramSign.setSignform(planSignManager.getSignform());
								paramSign.setPlan_id(planSignManager.getPlan_id());
								PlanSign resultPlanSign = signDAO.findPlanSign(paramSign);
								JobSkill paramJobSkill = new JobSkill();
								paramJobSkill.setSkill_user_id(resultPlanSign.getSys_reg_user_id());
								paramJobSkill.setUse_flag('S');
								List<JobSkillDTO> jobSkillList = currentDAO.userJobSkillList(paramJobSkill);
								JobSkill updateSkill = new JobSkill();
								if (jobSkillList.size() > 0) { // 기존 버전 있으면 +1 이전 버전은  C 캔슬처리
									updateSkill.setVersion(jobSkillList.get(0).getVersion() + 1);
									jobSkillList.get(0).setUse_flag('C');
									JobSkillEntity updateSkillEntity = new JobSkillEntity(jobSkillList.get(0));
									currentDAO.updateJobSkill(updateSkillEntity);
								}
								else {
									updateSkill.setVersion(1);
								}
								updateSkill.setUse_flag('S');
								updateSkill.setId(planSignManager.getPlan_id());
								JobSkillEntity updateSkillEntity = new JobSkillEntity(updateSkill);
								int resultCnt = currentDAO.updateJobSkill(updateSkillEntity);
							}
							//최종결재자 + 승인 : 연간부서계획
							else if (G_SIGN_ANNUAL_DEPT_CODE.equals(planSignManager.getSignform())) {
								AnnualPlan updDept = new AnnualPlan();
								updDept.setId(planSignManager.getPlan_id());
								updDept.setProgress_type(planSignManager.getSign_type());
								updDept.setUse_flag('S');
								AnnualPlanEntity updAnnual = new AnnualPlanEntity(updDept);
								int updCnt = annualPlanDAO.updateAnnualPlan(updAnnual);

								AnnualPlanSearchEntity searchAnnualPlan = new AnnualPlanSearchEntity();
								searchAnnualPlan.setId(planSignManager.getPlan_id());
								AnnualPlanDTO annualPlan = annualPlanDAO.findAnnualPlan(searchAnnualPlan);
								AnnualPlanSearchEntity searchAnnual = new AnnualPlanSearchEntity();
								searchAnnual.setId(annualPlan.getId());
								searchAnnual.setGroup_id(annualPlan.getGroup_id());
								searchAnnual.setEdu_year(annualPlan.getEdu_year());
								searchAnnual.setUse_flag('Z');						  //계획 수행이 아닌것들
								searchAnnual.setEdu_type(G_PLAN_EDU_ANNUAL_DEPT_CODE);
								searchAnnual.setId(0);
								//하위 연간계획이 전부 승인됬을시 연간 계획 다음 스텝으로 이동
								List<AnnualPlanDTO> annualPlanDTOList = annualPlanDAO.findAnnualPlanList(searchAnnual);

								if (annualPlanDTOList.size() == 0) { //마지막 단계일떄
									searchAnnual = new AnnualPlanSearchEntity();
									searchAnnual.setId(annualPlan.getId());
									searchAnnual.setGroup_id(annualPlan.getGroup_id());
									searchAnnual.setEdu_year(annualPlan.getEdu_year());
									searchAnnual.setEdu_type(G_PLAN_EDU_ANNUAL_INFO_CODE);
									searchAnnual.setId(0);//공통 아이디 조회
									AnnualPlanDTO infoAnnual = annualPlanDAO.findAnnualPlan(searchAnnual);

									PlanSign setPlanSign = new PlanSign();
									setPlanSign.setPlan_id(infoAnnual.getId());
									setPlanSign.setSignform(G_SIGN_ANNUAL_INFO_CODE);
									PlanSign res_plan_sign = signDAO.findPlanSign(setPlanSign);

									PlanSignManager psm = new PlanSignManager();
									psm.setPlan_id(infoAnnual.getId());
									// psm.setSign_type(g_sign_type_annual_examine_code);
									psm.setState(G_APPROVE_REQUEST_CODE);
									psm.setPlan_sign_id(res_plan_sign.getId());
									psm.setEdu_state(G_SIGN_TYPE_ANNUAL_EXAMINE_CODE);	  //edu_state 값으로 signtype 지정
									signDAO.updatePlanSignManager(psm);

									updDept = new AnnualPlan();
									updDept.setId(infoAnnual.getId());
									AnnualPlanEntity setAnnual = new AnnualPlanEntity(updDept);
									setAnnual.setProgress_type(G_SIGN_TYPE_ANNUAL_EXAMINE_CODE);
									updCnt = annualPlanDAO.updateAnnualPlan(setAnnual);
								}
							}
						}
						else { // 최종결재자가 아니면 : 앞조건 = if (planSignManager.getOrder_num() == maxOrderNum)
							System.out.println("내 결재 상태와 다음 결재자 상태 업데이트");
							if (planSignManager.getOrder_num() == 1) {
								// document_loan update
								if (G_TYPE_DOC_CODE.equals(planSignManager.getSign_category())) {
									DocumentApplyEntity docApplyEntity = new DocumentApplyEntity();
									docApplyEntity.setReq_user_id(auth.getName());
									docApplyEntity.setDocument_id(planSignManager.getPlan_id());
									docApplyEntity.setStatus(planSignManager.getStatus());
									docApplyEntity.setCurrent_start_date(planSignManager.getCurrent_start_date());
									docApplyEntity.setCurrent_end_date(planSignManager.getCurrent_end_date());
									docApplyEntity.setSys_reg_user_id(auth.getName());
									factoryDAO.updateDocumentLoan(docApplyEntity);

									// document_memo save
									DocumentMemo memo = new DocumentMemo();
									memo.setMemo(planSignManager.getMemo());
									memo.setSys_reg_user_id(auth.getName());
									memo.setDocument_id(planSignManager.getPlan_id());
									memo.setType("doc");
									documentDAO.saveDocumentMemo(memo);
								}
							}
							for (PlanSignManager psm : planSignManagerList) {
								//로그인 결재자 순번과 결재자목록 순번이 같으면
								if (psm.getOrder_num() == planSignManager.getOrder_num()) {
									psm.setUser_id(auth.getName());
									psm.setState(G_APPROVE_CONFIRM_CODE);
									psm.setComment(planSignManager.getComment());
									psm.setSys_upd_user_id(auth.getName());
									psm.setPlan_sign_id(planSignManager.getPlan_sign_id());
									signDAO.updatePlanSignManager(psm);

									//결재단계의 상태 값으로 변경해줌 현재는 교육만.
									if (G_TYPE_EDU_CODE.equals(planSignManager.getSign_category())) {	//교육
										if (G_SIGN_PLAN_CODE.equals(planSignManager.getSignform())) {	   //교육계획
											ManagementPlanEntity paramPlan = new ManagementPlanEntity();
											paramPlan.setId(planSignManager.getPlan_id());
											paramPlan.setProgress_type(planSignManager.getSign_type());
											managementPlanDAO.updateEducationPlan(paramPlan);
										}
										else if (G_SIGN_ANNUAL_INFO_CODE.equals(planSignManager.getSignform())) { //연간교육

											if (G_PROGRESS_APPROVE_CODE.equals(psm.getSign_type())) {
												AnnualPlanSearchEntity searchAnnualPlan = new AnnualPlanSearchEntity();
												searchAnnualPlan.setId(planSignManager.getPlan_id());
												AnnualPlanDTO annualPlan = annualPlanDAO.findAnnualPlan(searchAnnualPlan);
												AnnualPlanSearchEntity searchAnnual = new AnnualPlanSearchEntity();
												searchAnnual.setId(annualPlan.getId());
												searchAnnual.setGroup_id(annualPlan.getGroup_id());
												searchAnnual.setEdu_year(annualPlan.getEdu_year());
												searchAnnual.setEdu_type(G_PLAN_EDU_ANNUAL_DEPT_CODE);
												searchAnnual.setId(0);
												List<AnnualPlanDTO> annualPlanDTOList = annualPlanDAO.findAnnualPlanList(searchAnnual);

												AnnualPlan updDept = new AnnualPlan();
												updDept.setId(planSignManager.getPlan_id());
												updDept.setProgress_type(planSignManager.getSign_type());
												updDept.setUse_flag('S');
												AnnualPlanEntity updAnnual = new AnnualPlanEntity(updDept);
												int updCnt = annualPlanDAO.updateAnnualPlan(updAnnual);

												for (AnnualPlanDTO annualPlanDTO : annualPlanDTOList) { //연간 부서 계획 결재 단계로
													if ('S' == annualPlanDTO.getUse_flag()) {		//승인된 건들은 패스
														continue;
													}

													updDept = new AnnualPlan();
													updDept.setId(annualPlanDTO.getId());
													updDept.setProgress_type(planSignManager.getSign_type());
													if ('D' == annualPlanDTO.getUse_flag()) {										//3단계이후 반려된 건일 경우 D이기떄문에 반려로 변경
														updDept.setUse_flag('R');
													}
													else {
														updDept.setUse_flag('N');														//처음 시작일경우
													}

													updAnnual = new AnnualPlanEntity(updDept);
													updCnt = annualPlanDAO.updateAnnualPlan(updAnnual);

													if (updCnt > 0) { //update 성공시
														if ('R' == updAnnual.getUse_flag()) {

															PlanSignManager deptSignUser = new PlanSignManager();
															deptSignUser.setSign_category(G_TYPE_EDU_CODE);
															deptSignUser.setSignform(G_SIGN_ANNUAL_DEPT_CODE);
															deptSignUser.setPlan_id(updAnnual.getId());

															List<PlanSignManager> dept_planSignList = signDAO.findApproverList(deptSignUser);

															// update planSignManager
															PlanSignManager reject_user = dept_planSignList.get(0);	// 공통계획이 연간교육 검토단계로 갈시에 반려된 부서 계획은 요청단계로 변경
															reject_user.setState(G_APPROVE_REQUEST_CODE);
															signDAO.updatePlanSignManager(reject_user);

														}
													}
												}
											}
										}
									}
									/*
									PlanSignDetail signDetail = new PlanSignDetail();
									signDetail.setPlan_sign_id(psm.getPlan_sign_id());
									signDetail.setComment(planSignManager.getComment());
									signDetail.setSign_manager_id(psm.getId());
									signDetail.setState(G_APPROVE_CONFIRM_CODE);
									signDetail.setUser_id(auth.getName());
									signDetail.setSys_reg_user_id(auth.getName());
									signDetail.setPlan_sign_id(psm.getPlan_sign_id());
									signDAO.savePlanSignDetail(signDetail);
									*/
									if (G_TYPE_DOC_CODE.equals(planSignManager.getSign_category())) {
										ManagementPlanEntity planEntity = new ManagementPlanEntity();
										planEntity.setProgress_type(planSignManager.getSignform());
										planEntity.setId(planSignManager.getPlan_id());
										managementPlanDAO.updateEducationPlan(planEntity);
									}

								}
								else if (psm.getOrder_num() == planSignManager.getOrder_num() + 1) {
									// 로그인 결재자 의 다음 사람이면
									
									//연간교육계획 && 계획배부작성
									if (G_SIGN_ANNUAL_INFO_CODE.equals(planSignManager.getSignform()) && G_PROGRESS_APPROVE_CODE.equals(planSignManager.getSign_type())) {
										continue;
									}

									psm.setStatus(planSignManager.getSign_type());
									psm.setState(G_APPROVE_REQUEST_CODE);
									psm.setSys_upd_user_id(auth.getName());
									psm.setComment("");
									psm.setSignform(planSignManager.getSignform());
									psm.setPlan_sign_id(planSignManager.getPlan_sign_id());
									signDAO.updatePlanSignManager(psm);

									// 이메일 보내기
									if (!StringUtils.isEmpty(psm.getUser_id())) {
										LoginRequest paramUser = new LoginRequest();
										paramUser.setUser_id(psm.getUser_id());
										CustomUserDetails userInfo = userRepository.findUser(paramUser);
										CodeDTO re_code = codeDAO.findByCodeName(psm.getSignform());
	
										if ("doc".equals(planSignManager.getFlag())) {
											// 문서상태 업데이트 대출신청인경우 대출로 폐기신청인경우 폐기로
											Document paramDocument = new Document();
											paramDocument.setId(psm.getPlan_id());
											DocumentDTO re_docuemnt = documentDAO.findDocument(paramDocument);
											emailContent = emailService.messageInfo(planSignManager.getState(), userInfo, re_docuemnt.getTitle(), re_code.getCode_name());
										}
										else {
											emailContent = emailService.messageSignInfo(psm, userInfo, "ing");
										}
	
										EmailMessage message = EmailMessage.builder()
											.to(userInfo.getEmail())
											.subject(emailService.createSubject(psm, userInfo, "ing"))
											.message(emailContent)
											.url(returnUrl)
											.build();
										emailService.sendMail(message, "email");
									}
								}
							}
							// 이수철 : 최종결재자가 아니면 tb_plan_sign.status 에 NULL로 업데이트 한다.
							PlanSign planSign = new PlanSign();
							planSign.setPlan_id(planSignManager.getPlan_id());
							planSign.setSign_category(planSignManager.getSign_category());
							planSign.setSys_upd_user_id(auth.getName());
							signDAO.updatePlanSign(planSign);
						}
					}
				}
				resultDTO.setState(true);
				resultDTO.setCode(202);
				resultDTO.setMessage(messageSource.getMessage("api.message.202", new String[] { "결재", state }, Locale.KOREA));
				resultDTO.setResult(planSignManager.getPlan_id());
			}
			catch (Exception e) {
				e.printStackTrace();
				System.out.println(e.getCause().toString());
				resultDTO.setState(false);
				resultDTO.setCode(400);
				resultDTO.setMessage(messageSource.getMessage("api.message.400", new String[] { "결재" + state }, Locale.KOREA));
				resultDTO.setResult(null);
			}
			return resultDTO;
		}
		return resultDTO;
	}
	
	@Transactional
	private int updateRejectPlanSignManager(PlanSignManager planSignManager,  List<PlanSignManager> planSignManagerList, Authentication auth) throws Exception {
		System.out.println("내결재상태 = 반려 업데이트");
		if (planSignManager.getOrder_num() == 1) {
			throw new Exception("최초 결재 요청자는 반려 시키지 못합니다!");
		}
		for (PlanSignManager psm : planSignManagerList) {
			//결재자 반려로 처리한다. 
			if (psm.getPlan_sign_id() == planSignManager.getPlan_sign_id() && psm.getOrder_num() == planSignManager.getOrder_num()) {
				psm.setUser_id(auth.getName());
				psm.setState(G_APPROVE_REJECT_CODE);//반려
				psm.setComment(planSignManager.getComment());
				psm.setSys_upd_user_id(auth.getName());
				psm.setPlan_sign_id(planSignManager.getPlan_sign_id());
				return signDAO.updatePlanSignManager(psm);
			}
		}
		return 0;
	}
}
