package com.innon.education.management.result.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.innon.education.admin.system.sign.dao.SignDAO;
import com.innon.education.admin.system.sign.repository.PlanSign;
import com.innon.education.admin.system.sign.repository.PlanSignManager;
import com.innon.education.admin.system.sign.repository.SignDTO;
import com.innon.education.admin.system.sign.service.SignService;
import com.innon.education.auth.entity.User;
import com.innon.education.common.repository.entity.LogEntity;
import com.innon.education.common.service.CommonService;
import com.innon.education.common.util.DataLib;
import com.innon.education.code.controller.dto.ResultDTO;
import com.innon.education.library.document.dao.DocumentDAO;
import com.innon.education.library.document.repasitory.model.DocumentMemo;
import com.innon.education.management.plan.dao.ManagementPlanDAO;
import com.innon.education.management.plan.repository.dto.ManagementPlanDTO;
import com.innon.education.management.plan.repository.entity.ManagementPlanEntity;
import com.innon.education.management.plan.repository.model.ManagementPlan;
import com.innon.education.management.progress.dao.ManagementProgressDAO;
import com.innon.education.management.result.dao.ManagementResultDAO;
import com.innon.education.management.result.repository.dto.ManagementResultDTO;
import com.innon.education.management.result.repository.entity.ManagementResultEntity;
import com.innon.education.management.result.repository.entity.ReEducationEntity;
import com.innon.education.management.result.repository.model.ManagementResult;
import com.innon.education.management.result.repository.model.ReEducation;
import com.innon.education.management.result.service.ManagementResultService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ManagementResultServiceImpl implements ManagementResultService {

	private ResultDTO resultDTO;

	@Autowired
	ManagementPlanDAO planDAO;

	@Autowired
	ManagementResultDAO resultDAO;

	@Autowired
	MessageSource messageSource;

	@Autowired
	CommonService commonService;

	@Autowired
	SignDAO signDAO;

	@Autowired
	ManagementProgressDAO progressDAO;

	@Autowired
	DocumentDAO documentDAO;

	@Autowired
	SignService signService;

	@Override
	public ResultDTO saveEducationResult(ManagementResult result, HttpServletRequest request, Authentication auth) {
		resultDTO = new ResultDTO();

		SignDTO sign = signDAO.findSignById(result.getSignUserList().get(1).getSign_id());
		result.setProgress_type(sign.getSignform());
		// int resultId = planDAO.savePlan(plan);
		//        if(!plan.getStatus().equals("edus11004")) {
		//            if(resultId > 0) {
		//                PlanSign planSign = new PlanSign();
		//                planSign.setId(plan.getSign_id());
		//                planSign.setPlan_id(plan.getId());
		//
		//                planSign.setStatus(plan.getSignUserList().get(1).getUser_id());
		//                planSign.setSys_reg_user_id(auth.getName());
		//                planSign.setDept_cd(user.getDept_cd());
		//                planSign.setSys_upd_reg_date(new Date());
		//
		//                int planSignId = signDAO.savePlanSign(planSign);
		//
		//                int order_num = 1;
		//                for(PlanSignManager planSignManager : plan.getSignUserList()) {
		//                    int singId = planSignManager.getSign_id() == 0 ? plan.getSign_id() : planSignManager.getSign_id();
		//                    planSignManager.setSign_id(singId);
		//                    planSignManager.setPlan_id(plan.getId());
		//                    planSignManager.setSign_type(planSignManager.getSign_type());
		//                    planSignManager.setOrder_num(order_num);
		//                    planSignManager.setSign_category("comc11001");
		//                    planSignManager.setGroup_id(plan.getGroup_id());
		//                    planSignManager.setSys_reg_user_id(auth.getName());
		//                    planSignManager.setDept_cd(user.getDept_cd());
		//                    planSignManager.setPlan_sign_id(planSign.getId());//2024-12-11 첫상신시 승인일자를 추가
		//                    if(order_num == 1) {
		//
		//                        planSignManager.setState("coms11005");
		//                        planSignManager.setUser_id(auth.getName());
		//                    } else if(order_num == 2) {
		//                        planSignManager.setSys_upd_reg_date(null);
		//                        planSignManager.setState("coms11004");
		//                        planSignManager.setComment("");
		//                    } else {
		//
		//                        planSignManager.setSys_upd_reg_date(null);
		//                        planSignManager.setState("coms11007");
		//                        planSignManager.setComment("");
		//                    }
		//                    signDAO.savePlanSignManager(planSignManager);
		//
		//                    if(order_num == 1) {
		//                        PlanSignDetail signDetail = new PlanSignDetail();
		//                        signDetail.setPlan_sign_id(planSign.getId());
		//                        signDetail.setSign_manager_id(planSignManager.getId());
		//                        signDetail.setState("coms11005");
		//                        signDetail.setUser_id(auth.getName());
		//                        signDetail.setSys_reg_user_id(auth.getName());
		//                        signDetail.setComment(planSignManager.getComment());
		//                        signDAO.savePlanSignDetail(signDetail);
		//                    }
		//                    order_num++;
		//                }
		//            }

		try {
			// 교육결과 정보 등록
			ManagementResultEntity resultEntity = new ManagementResultEntity(result);
			int resultCnt = resultDAO.saveEducationResult(resultEntity);
			User user = (User) auth.getPrincipal();
			if (resultCnt > 0) {
				ManagementPlan param = new ManagementPlan();
				param.setId(result.getPlan_id());
				//교육계획 조회
				ManagementPlanDTO managementPlan = planDAO.educationPlanDetail(param);

				param.setUse_flag(result.getUse_flag());
				param.setProgress_type(result.getSignUserList().get(0).getSign_type());
				//교육계획 수정 : progress_type = 'comi1s51001'(교육결과작성) , use_flag = 'W'
				planDAO.updatePlan(param);

				PlanSign planSign = new PlanSign();
				planSign.setId(result.getSignUserList().get(1).getSign_id());
				planSign.setPlan_id(result.getPlan_id());

				planSign.setStatus(result.getSignUserList().get(1).getUser_id());// 이수철???
				planSign.setSys_reg_user_id(auth.getName());
				planSign.setDept_cd(user.getDept_cd());
				planSign.setSys_upd_reg_date(new Date());
				//결재(tb_plan_sign) 입력
				signDAO.savePlanSign(planSign);

				int order_num = 1;
				for (PlanSignManager planSignManager : result.getSignUserList()) {
					int singId = planSignManager.getSign_id() == 0 ? result.getSign_id() : planSignManager.getSign_id();
					planSignManager.setSign_id(singId);
					planSignManager.setPlan_id(resultEntity.getPlan_id());
					planSignManager.setSign_type(planSignManager.getSign_type());
					planSignManager.setOrder_num(order_num);
					planSignManager.setSign_category("comc11001");
					planSignManager.setGroup_id(managementPlan.getGroup_id());
					planSignManager.setSys_reg_user_id(auth.getName());
					planSignManager.setDept_cd(user.getDept_cd());
					planSignManager.setPlan_sign_id(planSign.getId());//2024-12-11 첫상신시 승인일자를 추가
					if (order_num == 1) {
						planSignManager.setState("coms11005"); // 승인
						planSignManager.setUser_id(auth.getName());
					}
					else if (order_num == 2) {
						planSignManager.setSys_upd_reg_date(null);
						planSignManager.setState("coms11004"); // 요청
						planSignManager.setComment("");
					}
					else {
						planSignManager.setSys_upd_reg_date(null);
						planSignManager.setState("coms11007"); // 대기
						planSignManager.setComment("");
					}
					//tb_plan_sign_manager
					signDAO.savePlanSignManager(planSignManager);
					/*
					if (order_num == 1) {
						PlanSignDetail planSignDetail = new PlanSignDetail();
						planSignDetail.setPlan_sign_id(planSign.getId());
						planSignDetail.setSign_manager_id(planSignManager.getId());
						planSignDetail.setState("coms11005");
						planSignDetail.setUser_id(auth.getName());
						planSignDetail.setSys_reg_user_id(auth.getName());
						planSignDetail.setComment(planSignManager.getComment());
						signDAO.savePlanSignDetail(planSignDetail);
					}
					*/
					order_num++;
				}
			}

			DocumentMemo documentMemo = new DocumentMemo();
			documentMemo.setDocument_id(result.getPlan_id());
			documentMemo.setMemo(result.getMemo());
			documentMemo.setType("edu");
			documentMemo.setSys_reg_user_id(auth.getName());
			documentDAO.saveDocumentMemo(documentMemo);//제출시 비고 입력

			commonService.saveLog(LogEntity.builder().type("edu").table_id(documentMemo.getId()).page_nm("result").url_addr(request.getRequestURI()).state("insert").reg_user_id(auth.getName()).build());
			//이수철 : reEducationList 어떨때 값이 들어 오는지 체크 필요 (재교율일때 같음)
			List<ReEducation> reEducationList = result.getReEducation();
			if (!ObjectUtils.isEmpty(reEducationList)) {
				for (ReEducation reEducation : reEducationList) {
					ReEducationEntity reEducationEntity = new ReEducationEntity();
					reEducationEntity.setPlan_id(result.getPlan_id());
					reEducationEntity.setRe_user_id(reEducation.getQms_user_id());
					resultDAO.saveReduceStudent(reEducationEntity);
				}

			}
			//            ReEducation reEducation = result.getReEducation();
			//            List<ManagementPlanDTO> planDtoList = setManagementPlanDTO(result.getPlan_id());
			//
			//            if(planDtoList != null && planDtoList.size() > 0) {
			//                for(ManagementPlanDTO planDto : planDtoList) {
			//                    ManagementPlan plan = new ManagementPlan();
			//                    plan.setId(planDto.getId());
			//                    int planId = plan.getId();
			//                    if(reEducation != null) {
			//                        plan.setPlan_start_date(reEducation.getRe_edu_start_date());
			//                        plan.setPlan_end_date(reEducation.getRe_edu_end_date());
			//                        plan.setRe_edu_cnt(planDto.getRe_edu_cnt() + 1);
			//                        plan.setParent_id(planDto.getId());
			//                        if(reEducation.getRe_edu_yn().equals("Y")) {
			//                            // 교육계획 정보 등록
			//                            planDAO.savePlan(plan);
			//
			//                            // 피교육자 정보 등록
			//                            if(reEducation.getPlanUserList() != null && reEducation.getPlanUserList().size() > 0) {
			//                                for(String plan_user_id : reEducation.getPlanUserList()) {
			//                                    ManagementPlanUserEntity planUserEntity = new ManagementPlanUserEntity();
			//
			//                                    planUserEntity.setPlan_id(planId);
			//                                    planUserEntity.setQms_user_id(plan_user_id);
			//
			//                                    int resultId = planDAO.savePlanUser(planUserEntity);
			//                                }
			//                            }
			//
			//                            // 참고 URL 정보 등록
			//                            if(planDto.getHelpUrlList() != null && planDto.getHelpUrlList().size() > 0) {
			//                                for(Map<String, Object> urlMap : planDto.getHelpUrlList()) {
			//                                    HelpUrlEntity urlEntity = new HelpUrlEntity(planId, urlMap);
			//                                    planDAO.savePlanContent(urlEntity);
			//                                }
			//                            }
			//
			//                            // 문제정보 등록
			//                            if(planDto.getQuestionInfo() != null) {
			//                                for(Map<String, Object> question : planDto.getQuestionInfo()) {
			//                                    QuestionInfoEntity questionInfoEntity = new QuestionInfoEntity(planId, question);
			//                                    planDAO.saveQuestionInfo(questionInfoEntity);
			//                                }
			//                            }
			//                        }
			//
			//                        // 재교육 정보 등록
			//                        ReEducationEntity reEduEntity = new ReEducationEntity(planId, reEducation);
			//                        resultDAO.saveReEducationInfo(reEduEntity);
			//                    }
			//                }
			//            }
			//
			//            if(result.getEdu_type().equals("edus11002")) { // QMS인 경우
			//                PlanQmsEntity planQmsEntity = new PlanQmsEntity();
			//                planQmsEntity.setPlan_id(result.getPlan_id());
			//                List<PlanQms> planQmsList = resultDAO.findPlanQmsList(planQmsEntity);
			//                ManagementPlanDTO plan = progressDAO.findEducationPlanById(result.getPlan_id());
			//                if(planQmsList != null && planQmsList.size() > 0) {
			//                    for(PlanQms planQms : planQmsList) {
			//                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			//                        if(plan.getPlan_end_date() != null && !plan.getPlan_end_date().isEmpty()) {
			//                            planQms.setPlan_end_date(format.parse(plan.getPlan_end_date()));
			//                        }
			//                        if(plan.getEdu_user_id() != null && !plan.getEdu_user_id().isEmpty()) {
			//                            planQms.setEdu_user_id(plan.getEdu_user_id());
			//                        }
			//                        if(plan.getWork_num() != null && !plan.getWork_num().isEmpty()) {
			//                            planQms.setWork_num(plan.getWork_num());
			//                        }
			//                        // IF_QMS_RES 등록
			//                        resultDAO.saveQmsRes(planQms);
			//                    }
			//                }
			//            }

			resultDTO.setState(true);
			resultDTO.setCode(201);
			resultDTO.setMessage(messageSource.getMessage("api.message.201", new String[] { "교육결과" }, Locale.KOREA));
			resultDTO.setResult(String.valueOf(result.getId()));
		}
		catch (Exception e) {
			log.info(e.getCause().toString());
			e.printStackTrace();
			resultDTO.setState(false);
			resultDTO.setCode(400);
			resultDTO.setMessage(messageSource.getMessage("api.message.400", new String[] { "교육결과" }, Locale.KOREA));
			resultDTO.setResult(null);
		}

		return resultDTO;
	}

	@Override
	public ResultDTO findResultPlanUserList(ManagementResult result) {
		resultDTO = new ResultDTO();
		int planId = 0;
		if (result != null) {
			planId = result.getPlan_id();
		}
		List<ManagementResultDTO> resultList = resultDAO.findResultPlanUserList(planId);

		if (!DataLib.isEmpty(resultList)) {
			resultDTO.setState(true);
			resultDTO.setResult(resultList);
		}
		else {
			resultDTO.setState(false);
		}

		return resultDTO;
	}

	@Override
	public ResultDTO managementResultDetail(ManagementResult result) {
		resultDTO = new ResultDTO();

		ManagementResultDTO detail = resultDAO.managementResultDetail(result);

		if (detail != null) {
			resultDTO.setState(true);
			resultDTO.setResult(detail);
		}
		else {
			resultDTO.setState(false);
		}

		return resultDTO;
	}

	@Override
	public ResultDTO updateEducationResult(ManagementResult result, HttpServletRequest request, Authentication auth) {
		resultDTO = new ResultDTO();

		SignDTO sign = signDAO.findSignById(result.getSignUserList().get(1).getSign_id());
		result.setProgress_type(sign.getSignform());

		try {

			// 교육결과 정보 등록
			ManagementResultEntity resultEntity = new ManagementResultEntity(result);
			int resultCnt = resultDAO.updateEducationResult(resultEntity);
			User user = (User) auth.getPrincipal();
			if (resultCnt > 0) {
				ManagementPlan param = new ManagementPlan();
				param.setId(result.getPlan_id());
				ManagementPlanDTO managementPlan = planDAO.educationPlanDetail(param);

				param.setUse_flag(result.getUse_flag());
				param.setProgress_type(result.getSignUserList().get(0).getSign_type());
				planDAO.updatePlan(param);
				String signForm = "comi1s5";
				PlanSign paramSign = new PlanSign();
				paramSign.setSignform(signForm);
				paramSign.setPlan_id(result.getPlan_id());
				PlanSign resultPlanSign = signDAO.findPlanSign(paramSign);

				PlanSignManager planSignManager = new PlanSignManager();
				planSignManager.setPlan_id(result.getPlan_id());
				planSignManager.setSignform(signForm);
				planSignManager.setState(result.getSignUserList().get(0).getState());
				planSignManager.setUser(result.getUser());
				planSignManager.setComment(result.getSignUserList().get(0).getComment());
				planSignManager.setOrder_num(1);
				planSignManager.setId(resultPlanSign.getId());
				signService.updatePlanSign(planSignManager, request, auth);
			}

			DocumentMemo memo = new DocumentMemo();
			memo.setDocument_id(result.getPlan_id());
			memo.setMemo(result.getMemo());
			memo.setType("edu");
			memo.setSys_reg_user_id(auth.getName());
			documentDAO.saveDocumentMemo(memo);

			int logId = commonService.saveLog(LogEntity.builder().type("edu").table_id(memo.getId()).page_nm("result").url_addr(request.getRequestURI()).state("update").reg_user_id(auth.getName()).build());

			resultDTO.setState(true);
			resultDTO.setCode(201);
			resultDTO.setMessage(messageSource.getMessage("api.message.202", new String[] { "교육결과" }, Locale.KOREA));
			resultDTO.setMessage(String.valueOf(result.getId()));
		}
		catch (Exception e) {
			log.info(e.getCause().toString());
			e.printStackTrace();
			resultDTO.setState(false);
			resultDTO.setCode(400);
			resultDTO.setMessage(messageSource.getMessage("api.message.400", new String[] { "교육결과" }, Locale.KOREA));
			resultDTO.setResult(null);
		}

		return resultDTO;
	}

	private List<ManagementPlanDTO> setManagementPlanDTO(int plan_id) {
		ManagementPlanEntity planEntity = new ManagementPlanEntity(plan_id);
		// 교육계획 조회
		List<ManagementPlanDTO> planResultList = planDAO.educationPlanList(planEntity);

		for (ManagementPlanDTO planResult : planResultList) {
			// 참고 URL 목록 조회
			List<Map<String, Object>> helpUrlList = planDAO.findManagementHelpUrl(plan_id);
			if (!DataLib.isEmpty(helpUrlList)) {
				planResult.setHelpUrlList(helpUrlList);
			}

			// 문제정보 조회
			List<Map<String, Object>> questionInfo = planDAO.findQuestionInfo(plan_id);
			planResult.setQuestionInfo(questionInfo);
		}
		/*// 교육계획 조회
		List<ManagementPlanDTO> planResult = planDAO.findEducationPlan(planEntity);
		
		// 참고 URL 목록 조회
		List<Map<String, Object>> helpUrlSet = planDAO.findManagementHelpUrl(plan_id);
		planResult.setHelpUrlSet(helpUrlSet);
		
		// 문제정보 조회
		Map<String, Object> questionInfo = planDAO.findQuestionInfo(plan_id);
		Object questionInfoId = questionInfo.get("id");
		planResult.setQuestionInfo(questionInfo);
		
		// 문제상세정보 목록 조회
		List<Map<String, Object>> questionDetail = planDAO.findQuestionDetail(questionInfoId);
		planResult.setQuestionDetail(questionDetail);*/

		return planResultList;
	}
}
