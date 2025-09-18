package com.innon.education.management.plan.service.impl;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.innon.education.management.plan.repository.dto.EducationPlanUserInfoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.innon.education.admin.system.sign.dao.SignDAO;
import com.innon.education.admin.system.sign.repository.PlanSign;
import com.innon.education.admin.system.sign.repository.PlanSignManager;
import com.innon.education.admin.system.sign.repository.SignDTO;
import com.innon.education.admin.system.sign.service.SignService;
import com.innon.education.auth.dto.request.LoginRequest;
import com.innon.education.auth.entity.User;
import com.innon.education.auth.repository.UserRepository;
import com.innon.education.common.repository.entity.LogChild;
import com.innon.education.common.repository.entity.LogEntity;
import com.innon.education.common.repository.model.EmailMessage;
import com.innon.education.common.service.CommonService;
import com.innon.education.common.service.EmailService;
import com.innon.education.common.util.DataLib;
import com.innon.education.code.controller.dto.ResultDTO;
import com.innon.education.code.controller.dao.CodeDAO;
import com.innon.education.jwt.dto.CustomUserDetails;
import com.innon.education.library.document.dao.DocumentDAO;
import com.innon.education.library.document.repasitory.dto.PlanDocument;
import com.innon.education.library.document.repasitory.model.DocumentMemo;
import com.innon.education.management.plan.dao.ManagementPlanDAO;
import com.innon.education.management.plan.repository.dto.ManagementPlanDTO;
import com.innon.education.management.plan.repository.dto.UserEduCurrentDTO;
import com.innon.education.management.plan.repository.entity.HelpUrlEntity;
import com.innon.education.management.plan.repository.entity.ManagementPlanEntity;
import com.innon.education.management.plan.repository.entity.ManagementPlanUserEntity;
import com.innon.education.management.plan.repository.entity.QuestionInfoDetailEntity;
import com.innon.education.management.plan.repository.entity.QuestionInfoEntity;
import com.innon.education.management.plan.repository.model.EducationPlanContent;
import com.innon.education.management.plan.repository.model.EducationPlanLab;
import com.innon.education.management.plan.repository.model.ManagementPlan;
import com.innon.education.management.plan.repository.model.PlanQms;
import com.innon.education.management.plan.repository.model.QuestionInfo;
import com.innon.education.management.plan.repository.model.UserEduCurrent;
import com.innon.education.management.plan.service.ManagementPlanService;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class ManagementPlanServiceImpl implements ManagementPlanService {

	private ResultDTO resultDTO;

	@Autowired
	ManagementPlanDAO planDAO;

	@Autowired
	CodeDAO codeDAO;

	@Autowired
	MessageSource messageSource;

	@Autowired
	CommonService commonService;

	@Autowired
	SignDAO signDAO;

	@Autowired
	DocumentDAO documentDAO;
	@Autowired
	SignService signService;

	@Autowired
	UserRepository userRepository;
	@Autowired
	EmailService emailService;


	@Override
	public ResultDTO managementPlanList(ManagementPlan plan) {
		ResultDTO resultDTO = new ResultDTO();
		ManagementPlanEntity planEntity = new ManagementPlanEntity(plan);
		if (plan.getPage() != null) {
			planEntity.setPage(plan.getPage());
		}
		List<ManagementPlanDTO> resultList = planDAO.educationPlanList(planEntity);
		if (plan.getPage() != null) {
			resultDTO.setPage(plan.getPage());
			resultDTO.getPage().setTotalCnt(resultList);
		}
		if (!DataLib.isEmpty(resultList)) {
			//for (ManagementPlanDTO result : resultList) {
			  //  int planId = result.getId();

				// 교육대상 값 설정
				//todo warning 정주원
//				List<ManagementPlanUserDTO> planUserList = planDAO.findManagementPlanUser(planId);
//				String qms_user_nms = "";
//				if (planUserList != null && planUserList.size() > 0) {
//					result.setPlanUserList(planUserList);
//					result.setPlan_user_cnt(planUserList.size() + "명");
//					for (ManagementPlanUserDTO planUser : planUserList) {
//						qms_user_nms += planUser.getQms_user_nm() + ",";
//					}
//					qms_user_nms = qms_user_nms.substring(0, qms_user_nms.length() - 1);
//					result.setQms_user_nms(qms_user_nms);
//				}

				// 참고 URL 값 설정
//				List<Map<String, Object>> helpUrlList = planDAO.findManagementHelpUrl(planId);
//				if (helpUrlList != null && helpUrlList.size() > 0) {
//					result.setHelpUrlList(helpUrlList);
//				}
//
//				// 문제정보 값 설정
//				List<Map<String, Object>> questionInfo = planDAO.findQuestionInfo(planId);
//				result.setQuestionInfo(questionInfo);
			//}

			resultDTO.setState(true);
			resultDTO.setResult(resultList);
		} else {
			resultDTO.setState(false);
		}

		return resultDTO;
	}

	@Override
	public ResultDTO updateEducationPlanStatus(List<ManagementPlan> planList) {
		resultDTO = new ResultDTO();
		int updateNum = 0;
		if (!DataLib.isEmpty(planList)) {
			for (ManagementPlan plan : planList) {
				ManagementPlanEntity planEntity = new ManagementPlanEntity(plan);
				updateNum += planDAO.updateEducationPlanStatus(planEntity);
			}

			if (updateNum > 0) {
				resultDTO.setMessage(updateNum + "개의 계획 상태가 수정 되었습니다.");
			} else {
				resultDTO.setMessage("계획 상태의 수정이 실패하였습니다.");
			}
		} else {
			resultDTO.setMessage("수정될 교육계획 정보가 없습니다.");
		}

		return resultDTO;
	}

	@Transactional
	@Override
	public ResultDTO savePlan(ManagementPlan plan, HttpServletRequest request, Authentication auth) {
		resultDTO = new ResultDTO();
		if(plan.getUse_flag() != 'W') {
			resultDTO.setCode(200);
		} else {
			resultDTO = commonService.findCheckUserAuth(plan.getUser(), request, auth);
		}


		if (resultDTO.getCode() == 200) {
			User user = (User) auth.getPrincipal();
			plan.setDept_cd(user.getDept_cd());
			plan.setDept_nm(user.getDept_nm());

			//		if(plan.getEdu_user_id() == null || plan.getEdu_user_id().equals("")) {
			//			plan.setEdu_user_id(auth.getName());
			//		}

			try {
				System.out.println("\n edu_type =" + plan.getEdu_type());//교육유형 : 일탈...
				System.out.println("\n progress_type =" + plan.getProgress_type());
				plan.setProgress_type("comi1s31001");// 교육계획작성
				plan.setSys_reg_user_id(auth.getName());
				int resultId = planDAO.savePlan(plan);
				if (plan.getUse_flag() == 'W') {
					//getSignUserList 에 선택한 결재선의 planSignManager 모두 들어 옴, 첫번째(작성)는 sign_id=0 올 온다.
					SignDTO sign = signDAO.findSignById(plan.getSignUserList().get(1).getSign_id());

					if (resultId > 0) {
						PlanSign planSign = new PlanSign();
						planSign.setId(plan.getSign_id());
						planSign.setPlan_id(plan.getId());

						planSign.setStatus(plan.getSignUserList().get(1).getUser_id());
						planSign.setSys_reg_user_id(auth.getName());
						planSign.setDept_cd(user.getDept_cd());
						planSign.setSys_upd_reg_date(new Date());
						signDAO.savePlanSign(planSign);

						int order_num = 1;
						for (PlanSignManager signUser : plan.getSignUserList()) {
							int singId = signUser.getSign_id() == 0 ? plan.getSign_id() : signUser.getSign_id();
							signUser.setSign_id(singId);
							signUser.setPlan_id(plan.getId());
							signUser.setSign_type(signUser.getSign_type());
							signUser.setOrder_num(order_num);
							signUser.setSign_category("comc11001");//교육
							signUser.setGroup_id(plan.getGroup_id());
							signUser.setSys_reg_user_id(auth.getName());
							signUser.setDept_cd(user.getDept_cd());
							signUser.setPlan_sign_id(planSign.getId());//2024-12-11 첫상신시 승인일자를 추가
							if (order_num == 1) {
								signUser.setState("coms11005");//승인
								signUser.setUser_id(auth.getName());
							}
							else if (order_num == 2) {
								signUser.setSys_upd_reg_date(null);
								signUser.setState("coms11004");//요청
								signUser.setSignform("comi1s3");//교육계획
								signUser.setComment("");

								String returnUrl = "https://dip.inno-n.com/lms/mypage";

								LoginRequest paramUser = new LoginRequest();
								paramUser.setUser_id(signUser.getUser_id());
								CustomUserDetails userInfo = userRepository.findUser(paramUser);
								String emailContent = emailService.messageSignInfo(signUser, userInfo, "ing");

								EmailMessage message = EmailMessage.builder()
										.to(userInfo.getEmail())
										.subject(emailService.createSubject(signUser, userInfo,"ing"))
										.message(emailContent).url(returnUrl)
										.build();
								emailService.sendMail(message, "email");
							}
							else {
								signUser.setSys_upd_reg_date(null);
								signUser.setState("coms11007");//대기
								signUser.setComment("");
							}
							signDAO.savePlanSignManager(signUser);
							/*
							if (order_num == 1) {
								PlanSignDetail signDetail = new PlanSignDetail();
								signDetail.setPlan_sign_id(planSign.getId());
								signDetail.setSign_manager_id(signUser.getId());
								signDetail.setState("coms11005");
								signDetail.setUser_id(auth.getName());
								signDetail.setSys_reg_user_id(auth.getName());
								signDetail.setComment(signUser.getComment());
								signDAO.savePlanSignDetail(signDetail);
							}
							*/
							order_num++;
						}
					}
				}
				DocumentMemo memo = new DocumentMemo();
				memo.setDocument_id(plan.getId());
				memo.setMemo(plan.getMemo());
				memo.setType("edu");
				memo.setSys_reg_user_id(auth.getName());
				documentDAO.saveDocumentMemo(memo);

				int logId = commonService.saveLog(LogEntity.builder()
						.type("edu")
						.table_id(memo.getId())
						.page_nm("education")
						.url_addr(request.getRequestURI())
						.state("insert")
						.reg_user_id(auth.getName())
						.build());

				if (logId > 0) {
					try {
						ManagementPlanEntity logEntity = new ManagementPlanEntity(plan);
						Object obj = logEntity;
						for (Field field : obj.getClass().getDeclaredFields()) {
							field.setAccessible(true);
							Object value = field.get(obj);
							if (value != null ) {
								commonService.saveLogDetail(LogChild
										.builder()
										.log_id(logId)
										.field(field.getName())
										.new_value(value.toString())
										.reg_user_id(auth.getName())
										.build());
							}
						}
					} catch (Exception e) {
						System.out.println(e.getCause().toString());
						e.printStackTrace();
					}
				}
				resultDTO.setState(true);
				resultDTO.setCode(201);
				resultDTO.setMessage(messageSource.getMessage("api.message.201", new String[]{"교육계획"}, Locale.KOREA));
				resultDTO.setResult(memo.getId());

			} catch (Exception e) {
				System.out.println(e.getCause().toString());
				resultDTO.setState(false);
				resultDTO.setCode(400);
				resultDTO.setMessage(messageSource.getMessage("api.message.400", new String[]{"교육계획"}, Locale.KOREA));
				resultDTO.setResult(null);
			}
		}
		return resultDTO;
	}

	@Override
	public ResultDTO savePlanUser(ManagementPlan plan, int plan_id, HttpServletRequest request, Authentication auth) {
		resultDTO = new ResultDTO();
		User user = (User) auth.getPrincipal();

		try {

			int logId = commonService.saveLog(LogEntity.builder()
					.table_id(plan_id)
					.type("edu")
					.page_nm("edu_user")
					.url_addr(request.getRequestURI())
					.state("insert")
					.reg_user_id(auth.getName())
					.build());
			for (ManagementPlanUserEntity userEntity : plan.getPlanUserList()) {
				userEntity.setStatus("edus11003");
				userEntity.setPlan_id(plan.getId());
				//userEntity.setPlant_cd(user.getPlant_cd());
				if (userEntity.getUser_id() == null || userEntity.getUser_id().equals("") || auth.getName().equals(userEntity.getUser_id())) {
					continue;
				}
				int resultId = planDAO.savePlanUser(userEntity);
				if (resultId > 0) {

					commonService.saveLogDetail(LogChild
							.builder()
							.log_id(logId)
							.field("user_id")
							.new_value(userEntity.getUser_id())
							.reg_user_id(auth.getName())
							.build());
				}
			}
		} catch (Exception e) {
			System.out.println(e.getCause());
		}

		return resultDTO;
	}

	@Override
	public ResultDTO savePlanContent(ManagementPlan plan, int plan_id, HttpServletRequest request, Authentication auth) {
		resultDTO = new ResultDTO();
		int saveNum = 0;
		int logId = commonService.saveLog(LogEntity.builder()
				.table_id(plan_id)
				.type("edu")
				.page_nm("edu_content")
				.url_addr(request.getRequestURI())
				.state("insert")
				.reg_user_id(auth.getName())
				.build());
		for (Map<String, Object> urlMap : plan.getHelpUrlList()) {
			HelpUrlEntity urlEntity = new HelpUrlEntity(plan.getId(), urlMap);
			int resultId = planDAO.savePlanContent(urlEntity);
			if (resultId > 0) {
				saveNum++;

				try {
					for (String key : urlMap.keySet()) {
						Object value = urlMap.get(key);
						if (value != null && value != "") {
							commonService.saveLogDetail(LogChild
									.builder()
									.log_id(logId)
									.field(key)
									.new_value(value.toString())
									.reg_user_id(auth.getName())
									.build());
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				resultDTO.setState(false);
				resultDTO.setCode(400);
				resultDTO.setMessage(messageSource.getMessage("api.message.400", new String[]{"교육내용"}, Locale.KOREA));
				resultDTO.setResult(null);
			}
		}

		if (saveNum > 0) {
			resultDTO.setState(true);
			resultDTO.setCode(201);
			resultDTO.setMessage(messageSource.getMessage("api.message.201", new String[]{"교육내용"}, Locale.KOREA));
			resultDTO.setResult(saveNum);
		}

		return resultDTO;
	}

	@Override
	public ResultDTO saveQuestionInfo(ManagementPlan plan, int plan_id, HttpServletRequest request, Authentication auth) {
		resultDTO = new ResultDTO();

		int logId = commonService.saveLog(LogEntity.builder()
				.table_id(plan_id)
				.type("edu")
				.page_nm("edu_question")
				.url_addr(request.getRequestURI())
				.state("insert")
				.reg_user_id(auth.getName())
				.build());

		for (Map<String, Object> question : plan.getQuestionInfo()) {
			QuestionInfoEntity questionInfoEntity = new QuestionInfoEntity(plan.getId(), question);
			questionInfoEntity.setGroup_id(plan.getGroup_id());
			int resultId = planDAO.saveQuestionInfo(questionInfoEntity);

			if (resultId > 0) {

				//List<Map<String, Object>> questionInfoDTO = planDAO.findQuestionInfo(plan.getId());

				commonService.saveLogDetail(LogChild
						.builder()
						.log_id(logId)
						.field("edu_question")
						.new_value(String.valueOf(questionInfoEntity.getId()))
						.reg_user_id(auth.getName())
						.build());
			}
			if (questionInfoEntity.getId() > 0) {
				resultDTO.setState(true);
				resultDTO.setResult(questionInfoEntity.getId());
				resultDTO.setMessage("문제 정보 등록이 완료 되었습니다. ");
			} else {
				resultDTO.setState(false);
				resultDTO.setMessage("");
			}
		}

		return resultDTO;
	}

	@Override
	public ResultDTO saveQuestionInfoDetail(ManagementPlan plan, int question_id) {
		resultDTO = new ResultDTO();
		int saveNum = 0;

		for (Map<String, Object> detailMap : plan.getQuestionDetail()) {
			QuestionInfoDetailEntity detailEntity = new QuestionInfoDetailEntity(question_id, detailMap);
			saveNum += planDAO.saveQuestionInfoDetail(detailEntity);
		}

		if (saveNum > 0) {
			resultDTO.setState(true);
			resultDTO.setMessage(saveNum + "건의 문제 상세정보 등록이 완료 되었습니다. ");
		} else {
			resultDTO.setState(false);
			resultDTO.setMessage("");
		}

		return resultDTO;
	}

	@Override
	public ResultDTO requestQmsPlan(PlanQms plan) {
//		resultDTO = new ResultDTO();
//
//		ManagementPlan savePlan = new ManagementPlan();
//		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
//		savePlan.setTitle(plan.getTitle());
//		savePlan.setPlan_end_date(format.format(plan.getPlan_end_date()));
//		savePlan.setPlan_start_date(format.format(new Date()));
//		savePlan.setWork_num(plan.getWork_num());
//		planDAO.savePlan(savePlan);
//
//		PlanQmsEntity qmsEntity = new PlanQmsEntity(plan);
////		planDAO.saveWorkManagement(workEntity);
//		planDAO.saveQmsRes(qmsEntity);
//
//		// API 호출 함수 임시 주석 처리
////		callApiConnection();

		return resultDTO;
	}

	@Override
	public ResultDTO saveEducationPlan(List<EducationPlanLab> planList) {
		resultDTO = new ResultDTO();
		for (EducationPlanLab plan : planList) {
			ManagementPlanEntity planEntity = new ManagementPlanEntity(plan);
			// 교육계획정보 저장
			// 생성된 교육계획 아이디 변수 저장
			int plan_id = 0;
			// 부서코드를 기준으로 하위 조직 구성원 조회
			// 조회된 구성원만큼 피교육자로 등록
			ManagementPlanUserEntity planUserEntity = new ManagementPlanUserEntity();
			planUserEntity.setPlan_id(plan_id);
			planUserEntity.setQms_user_id("plan_user_id");

			// education_plan_help(참고 URL 테이블)에 교육내용 저장
			// 적격성정보, 단위 저장 테이블 확인 필요
		}

		return resultDTO;
	}

	@Override
	public ResultDTO findUserEduCurrentList(UserEduCurrent userEduCurrent, HttpServletRequest request, Authentication auth) {
		resultDTO = new ResultDTO();
		List<UserEduCurrentDTO> resultList = planDAO.findUserEduCurrentList(userEduCurrent);

		if (!DataLib.isEmpty(resultList)) {
			resultDTO.setState(true);
			resultDTO.setCode(200);
			resultDTO.setMessage(messageSource.getMessage("api.message.200", new String[]{"사용자 현황"}, Locale.KOREA));
			resultDTO.setResult(resultList);
		} else {
			resultDTO.setState(false);
			resultDTO.setCode(400);
			resultDTO.setMessage(messageSource.getMessage("api.message.400", new String[]{"사용자 현황"}, Locale.KOREA));
			resultDTO.setResult(null);
		}

		return resultDTO;
	}

	@Override
	public ResultDTO savePlanDocument(ManagementPlan plan, int planId, HttpServletRequest request, Authentication auth) {
		resultDTO = new ResultDTO();

		DocumentMemo memo = new DocumentMemo();
		memo.setDocument_id(plan.getId());
		memo.setMemo(plan.getMemo());
		memo.setType("edu");
		memo.setSys_reg_user_id(auth.getName());
		documentDAO.saveDocumentMemo(memo);

		int logId = commonService.saveLog(LogEntity.builder()
				.type("edu")
				.table_id(memo.getId())
				.page_nm("edu_document")
				.url_addr(request.getRequestURI())
				.state("insert")
				.reg_user_id(auth.getName())
				.build());

		for (PlanDocument paramDocument : plan.getDocumentList()) {
			paramDocument.setGroup_id(plan.getGroup_id());
			paramDocument.setPlan_id(plan.getId());
			planDAO.savePlanDocument(paramDocument);

			if (paramDocument.getId() > 0) {
				resultDTO.setState(true);
				resultDTO.setResult(paramDocument.getId());
				resultDTO.setMessage("교육 문서 정보 등록이 완료 되었습니다. ");
			} else {
				resultDTO.setState(false);
				resultDTO.setMessage("");
			}
		}
		return resultDTO;
	}
	@Transactional
	@Override
	public ResultDTO updatePlan(ManagementPlan plan, HttpServletRequest request, Authentication auth) {
		resultDTO = new ResultDTO();
		resultDTO = commonService.findCheckUserAuth(plan.getUser(), request, auth);
		if (resultDTO.getCode() == 200) {
			User user = (User) auth.getPrincipal();
			try {
				plan.setProgress_type("comi1s31001");// 교육계획작성
				int resultId = planDAO.updatePlan(plan);
				if (plan.getUse_flag() == 'W') {
					SignDTO sign = signDAO.findSignById(plan.getSignUserList().get(1).getSign_id());

					if (resultId > 0) {

						PlanSign paramSign = new PlanSign();
						paramSign.setSignform("comi1s3");
						paramSign.setPlan_id(plan.getId());
						PlanSign resultPlanSign = signDAO.findPlanSign(paramSign);

						if(resultPlanSign != null){
							PlanSignManager planSignManager = new PlanSignManager();
							planSignManager.setPlan_id(plan.getId());
							planSignManager.setSignform("comi1s3");
							planSignManager.setState(plan.getSignUserList().get(0).getState());
							planSignManager.setUser(plan.getUser());
							planSignManager.setComment(plan.getSignUserList().get(0).getComment());
							planSignManager.setOrder_num(1);
							planSignManager.setId(resultPlanSign.getId());
							signService.updatePlanSign(planSignManager, request, auth);

						}else{
							PlanSign planSign = new PlanSign();
							planSign.setId(plan.getSign_id());
							planSign.setPlan_id(plan.getId());

							planSign.setStatus(plan.getSignUserList().get(1).getUser_id());
							planSign.setSys_reg_user_id(auth.getName());
							planSign.setDept_cd(user.getDept_cd());
							planSign.setSys_upd_reg_date(new Date());
							signDAO.savePlanSign(planSign);

							int order_num = 1;
							for (PlanSignManager psm : plan.getSignUserList()) {
								int singId = psm.getSign_id() == 0 ? plan.getSign_id() : psm.getSign_id();
								psm.setSign_id(singId);
								psm.setPlan_id(plan.getId());
								psm.setSign_type(psm.getSign_type());
								psm.setOrder_num(order_num);
								psm.setSign_category("comc11001");
								psm.setGroup_id(plan.getGroup_id());
								psm.setSys_reg_user_id(auth.getName());
								psm.setDept_cd(user.getDept_cd());
								psm.setPlan_sign_id(planSign.getId());//2024-12-11 첫상신시 승인일자를 추가
								if (order_num == 1) {
									psm.setState("coms11005");
									psm.setUser_id(auth.getName());
								} else if (order_num == 2) {
									psm.setSys_upd_reg_date(null);
									psm.setState("coms11004");
									psm.setComment("");
								} else {
									psm.setSys_upd_reg_date(null);
									psm.setState("coms11007");
									psm.setComment("");
								}
								signDAO.savePlanSignManager(psm);
								/*
								if (order_num == 1) {
									PlanSignDetail planSignDetail = new PlanSignDetail();
									planSignDetail.setPlan_sign_id(planSign.getId());
									planSignDetail.setSign_manager_id(psm.getId());
									planSignDetail.setState("coms11005");
									planSignDetail.setUser_id(auth.getName());
									planSignDetail.setSys_reg_user_id(auth.getName());
									planSignDetail.setComment(psm.getComment());
									signDAO.savePlanSignDetail(planSignDetail);
								}
								*/
								order_num++;
							}

						}

					}
				}
				// 문서 비고 등록
				DocumentMemo memo = new DocumentMemo();
				memo.setDocument_id(plan.getId());
				memo.setMemo(plan.getMemo());
				memo.setSys_reg_user_id(auth.getName());
				memo.setType("edu");
				documentDAO.saveDocumentMemo(memo);

				int logId = commonService.saveLog(LogEntity.builder()
						.type("edu")
						.table_id(memo.getId())
						.page_nm("education")
						.url_addr(request.getRequestURI())
						.state("update")
						.reg_user_id(auth.getName())
						.build());

				if (logId > 0) {
//					ManagementPlanEntity logEntity = new ManagementPlanEntity(plan);
//					ManagementPlanEntity be_logEntity = new ManagementPlanEntity(plan.getBefore_education());
//					Object obj = logEntity;
//					Object obj2 = be_logEntity;
//					//변경 정보 저장
//
//					for (Field field : obj.getClass().getDeclaredFields()) {
//						field.setAccessible(true);
//						Object value = field.get(obj);
//						Object value2 = field.get(obj2);
//						if (value != null && !value.equals(value2)) {
//							commonService.saveLogDetail(LogChild
//									.builder()
//									.log_id(logId)
//									.field(field.getName())
//									.new_value(value.toString())
//									.before_value(value2.toString())
//									.reg_user_id(auth.getName())
//									.build());
//						}
//					}
//
//					// 상속받은 변경 정보 저장
//					for (Field field : obj.getClass().getSuperclass().getDeclaredFields()) {
//						field.setAccessible(true);
//						Object value = field.get(obj);
//						Object value2 = field.get(obj2);
//						if (value != null && !value.equals(value2)) {
//							commonService.saveLogDetail(LogChild
//									.builder()
//									.log_id(logId)
//									.field(field.getName())
//									.new_value(value.toString())
//									.before_value(value2.toString())
//									.reg_user_id(auth.getName())
//									.build());
//						}
//					}
					resultDTO.setState(true);
					resultDTO.setCode(202);
					resultDTO.setMessage(messageSource.getMessage("api.message.202", new String[]{"교육 계획", "수정"}, Locale.KOREA));
					resultDTO.setResult(plan.getId());

				}
			} catch (Exception e) {
				System.out.println(e.getCause().toString());
				resultDTO.setState(false);
				resultDTO.setCode(400);
				resultDTO.setMessage(messageSource.getMessage("api.message.400", new String[]{"교육 계획"}, Locale.KOREA));
				resultDTO.setResult(null);
			}

		}

		return resultDTO;
	}

	@Override
	public int updatePlanUser(ManagementPlanUserEntity plan) {
		int resultCnt = planDAO.updatePlanUser(plan);
		return resultCnt;
	}

	@Override
	public int updatePlanContent(EducationPlanContent plan) {
		int resultCnt = planDAO.updatePlanContent(plan);
		return resultCnt;
	}

	@Override
	public int updateQuestionInfo(QuestionInfo plan) {
		int resultCnt = planDAO.updateQuestionInfo(plan);
		return resultCnt;
	}

	@Override
	public int updatePlanDocument(PlanDocument plan) {
		int resultCnt = planDAO.updatePlanDocument(plan);
		return resultCnt;
	}

	@Override
	public ResultDTO managementPlanDetail(ManagementPlan plan) {
		ResultDTO resultDTO = new ResultDTO();

		try{
			ManagementPlanDTO resultPlan = planDAO.educationPlanDetail(plan);

			if(plan.getType() != null && plan.getType().equals("rating")) {
				plan.setPlan_id(plan.getId());
				EducationPlanUserInfoDTO educationPlanUserDTO = planDAO.educationPlanUserInfo(plan);
				resultPlan.setBtn_visible(educationPlanUserDTO.getBtn_visible());
			}

			String[] splitPath = resultPlan.getFilepath() != null ? resultPlan.getFilepath().split("\\\\") : null;
			String fileName = splitPath != null ? splitPath[splitPath.length-1] : "";
			resultPlan.setFilename(fileName);
			resultDTO.setResult(resultPlan);
			resultDTO.setCode(200);
			resultDTO.setMessage(messageSource.getMessage("api.message.202", new String[]{"교육 계획", "조회"}, Locale.KOREA));

		} catch (Exception e) {
			System.out.println(e.getCause().toString());
			resultDTO.setState(false);
			resultDTO.setCode(400);
			resultDTO.setMessage(messageSource.getMessage("api.message.400", new String[]{"교육 계획"}, Locale.KOREA));
			resultDTO.setResult(null);
		}
		
		return resultDTO;
	}

	@Override
	public ResultDTO deleteTempEducation(ManagementPlan plan, HttpServletRequest request, Authentication auth) {
		ResultDTO resultDTO = new ResultDTO();

		try {
			plan.setSys_reg_user_id(auth.getName());
			int deleteNum = planDAO.deleteTempEducation(plan);
			if (deleteNum > 0) {
				resultDTO.setState(true);
				resultDTO.setCode(202);
				resultDTO.setMessage(messageSource.getMessage("api.message.202", new String[]{"임시저장", "삭제"}, Locale.KOREA));
				resultDTO.setResult(plan.getId());
			}
		} catch(Exception e) {
			System.out.println(e.getCause().toString());
			resultDTO.setState(false);
			resultDTO.setCode(400);
			resultDTO.setMessage(messageSource.getMessage("api.message.400", new String[]{"임시저장 삭제"}, Locale.KOREA));
		}

		return resultDTO;
	}

	@Transactional
	@Override
	public ResultDTO uploadEducationPlanFile (MultipartFile uploadFiles, HttpServletRequest request, Authentication auth) {
		User user = (User) auth.getPrincipal();
		String userId = user.getUser_id();

		resultDTO = new ResultDTO();
		resultDTO = commonService.findFileCheckUserAuth(userId, request, auth);
		if (resultDTO.getCode() == 200) {

		try {
			Path uploadPath = Paths.get("./upload");
			if (!Files.exists(uploadPath)) {
				Files.createDirectories(uploadPath);
			}
			Path filePath = uploadPath.resolve(uploadFiles.getOriginalFilename());

			// 파일 저장
			Files.copy(uploadFiles.getInputStream(), filePath);
			resultDTO.setState(true);
			resultDTO.setCode(202);
			resultDTO.setMessage(messageSource.getMessage("api.message.202", new String[]{"파일 업로드 성공:", uploadFiles.getOriginalFilename()}, Locale.KOREA));

			} catch (IOException e) {
				System.out.println(e.getCause().toString());
				resultDTO.setState(false);
				resultDTO.setCode(400);
				resultDTO.setMessage(messageSource.getMessage("api.message.400", new String[]{"파일 업로드 실패"}, Locale.KOREA));
				resultDTO.setResult(null);
			}
		}
	return resultDTO;
	};

	private void callApiConnection() {
		try {
			String urlStr = "http://localhost:8080/api/education/management/plan/test";
			HttpClient client = HttpClient.newHttpClient();

			Map<String, Object> param = new HashMap<>();
			param.put("title", "제목111");
			param.put("plan_end_date", "2024-10-11");
			param.put("req_user_id", "kd.hong");
			param.put("process_user_id", "hong.kd");
			param.put("work_num", "CRR-TEST");
			param.put("work_seq", 1234);

			// Map 데이터를 수동으로 JSON 문자열로 변환
			StringBuilder jsonInputString = new StringBuilder();
			jsonInputString.append("{");
			for (Map.Entry<String, Object> entry : param.entrySet()) {
				jsonInputString.append("\"")
						.append(entry.getKey())
						.append("\":");

				// 값이 숫자 또는 boolean일 경우 그대로 사용, 문자열일 경우 따옴표로 묶음
				if (entry.getValue() instanceof String) {
					jsonInputString.append("\"")
							.append(entry.getValue().toString())
							.append("\"");
				} else {
					jsonInputString.append(entry.getValue().toString());
				}

				jsonInputString.append(",");
			}
			// 마지막 쉼표 제거 후 닫기
			jsonInputString.setLength(jsonInputString.length() - 1);  // 마지막 쉼표 제거
			jsonInputString.append("}");

			// HttpRequest 생성 (POST, JSON 데이터 포함)
			HttpRequest request = HttpRequest.newBuilder()
					.uri(URI.create(urlStr))
					.header("Content-Type", "application/json")
					.POST(HttpRequest.BodyPublishers.ofString(jsonInputString.toString()))
					.build();

			// 요청 보내고 응답 받기
			HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

			// 응답 출력
			System.out.println("Response Code: " + response.statusCode());
			System.out.println(response.body());
		} catch (Exception e) {
			System.out.println(e.getCause());
		}
	}
}
