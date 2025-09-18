package com.innon.education.qualified.current.service.impl;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.innon.education.admin.group.repository.Group;
import com.innon.education.admin.system.sign.dao.SignDAO;
import com.innon.education.admin.system.sign.repository.PlanSign;
import com.innon.education.admin.system.sign.repository.PlanSignManager;
import com.innon.education.admin.system.sign.service.SignService;
import com.innon.education.auth.entity.User;
import com.innon.education.auth.repository.UserRepository;
import com.innon.education.common.dao.CommonDAO;
import com.innon.education.common.repository.entity.LogEntity;
import com.innon.education.common.service.CommonService;
import com.innon.education.common.service.EmailService;
import com.innon.education.common.util.DataLib;
import com.innon.education.code.controller.dto.ResultDTO;
import com.innon.education.library.document.dao.DocumentDAO;
import com.innon.education.library.document.repasitory.model.DocumentMemo;
import com.innon.education.management.plan.repository.dto.ManagementPlanUserDTO;
import com.innon.education.qualified.current.dao.CurrentDAO;
import com.innon.education.qualified.current.repository.dto.JobRequestDTO;
import com.innon.education.qualified.current.repository.dto.JobSkillDTO;
import com.innon.education.qualified.current.repository.entity.JobSkillEntity;
import com.innon.education.qualified.current.repository.entity.JobSkillItemEntity;
import com.innon.education.qualified.current.repository.model.JobRequest;
import com.innon.education.qualified.current.repository.model.JobRevision;
import com.innon.education.qualified.current.repository.model.JobSkill;
import com.innon.education.qualified.current.repository.model.RevisionContent;
import com.innon.education.qualified.current.repository.model.RevisionDocument;
import com.innon.education.qualified.current.service.CurrentService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CurrentServiceImpl implements CurrentService {

	private ResultDTO resultDTO;

	@Autowired
	CurrentDAO currentDAO;
	@Autowired
	MessageSource messageSource;
	@Autowired
	CommonService commonService;
	@Autowired
	CommonDAO commonDAO;
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

	@Transactional
	@Override
	public ResultDTO saveJobRequest(JobRequest jobRequest, HttpServletRequest request, Authentication auth) {
		resultDTO = new ResultDTO();
		resultDTO = commonService.findCheckUserAuth(jobRequest.getUser(), request, auth);
		char status = jobRequest.getStatus();

		if (status == 'T') { // T : 임시저장 , S : 저장
			resultDTO.setCode(200);
		}

		if (resultDTO.getCode() == 200) {

			JobRevision checkRevision = new JobRevision();
			checkRevision.setGroup_id(jobRequest.getGroup_id());
			checkRevision.setJob_type_id(jobRequest.getJob_type_id());
			if (jobRequest.getRevision() != null) {
				checkRevision.setQualified_id(jobRequest.getRevision().getQualified_id());
			}
			checkRevision.setUse_flag('W');
			List<JobRevision> jobRequestList = currentDAO.findJobRevisionList(checkRevision);
			JobRequestDTO statusDTO = new JobRequestDTO();
			if (jobRequest.getId() > 0) {
				statusDTO = currentDAO.findJobQualifiedStatusById(jobRequest);
			}

			boolean test = statusDTO.getQualified_status() == 0;

			if (jobRequestList.size() > 0 && (statusDTO.getQualified_status() == 0 || statusDTO.getQualified_status() == 'S')) {
				resultDTO.setState(true);
				resultDTO.setCode(200);
				resultDTO.setMessage("이미 등록된 제정 신청 이력이 있습니다.");
				resultDTO.setResult(null);
			}
			else {

				User user = (User) auth.getPrincipal();
				jobRequest.setSys_reg_user_id(auth.getName());


				try {
					jobRequest.setUse_flag('W');
					currentDAO.saveJobRequest(jobRequest); //tb_job_qualified Merge
					//                    if (jobRequest.getId() <= 0) {
					//                        jobRequest.setUse_flag('W');
					//                        resultId = currentDAO.saveJobRequest(jobRequest);
					//                    } else {
					//                    }

					DocumentMemo memo = new DocumentMemo();
					if (jobRequest.getStatus() != 'T') {
						memo.setDocument_id(jobRequest.getId());
						memo.setMemo(jobRequest.getSignUserList().get(0).getComment());
						memo.setType("job");
						memo.setSys_reg_user_id(auth.getName());
						documentDAO.saveDocumentMemo(memo);

						commonService.saveLog(LogEntity.builder().type("job").table_id(memo.getId()).page_nm("job").url_addr(request.getRequestURI()).state("insert").reg_user_id(auth.getName()).build());
					}

					jobRequest.getRevision().setSys_reg_user_id(auth.getName());
					jobRequest.getRevision().setQualified_id(jobRequest.getId());
					jobRequest.getRevision().setGroup_id(jobRequest.getGroup_id());
					jobRequest.getRevision().setUse_flag('W');
					currentDAO.saveJobRevision(jobRequest.getRevision()); //tb_job_revision Merge

					if (jobRequest.getStatus() != 'T') {
						PlanSign planSign = new PlanSign();
						planSign.setId(jobRequest.getSign_id());
						planSign.setPlan_id(jobRequest.getRevision().getId());
						planSign.setStatus(jobRequest.getSignUserList().get(1).getSign_type());
						planSign.setSys_reg_user_id(auth.getName());
						planSign.setDept_cd(user.getDept_cd());
						planSign.setSys_upd_reg_date(new Date());
						signDAO.savePlanSign(planSign);

						int order_num = 1;

						for (PlanSignManager planSignManager : jobRequest.getSignUserList()) {
							int singId = planSignManager.getSign_id() == 0 ? jobRequest.getSign_id() : planSignManager.getSign_id();
							planSignManager.setSign_id(singId);
							planSignManager.setPlan_id(jobRequest.getRevision().getId());
							planSignManager.setSign_type(planSignManager.getSign_type());
							planSignManager.setOrder_num(order_num);
							planSignManager.setSign_category("comc11001");
							planSignManager.setGroup_id(jobRequest.getGroup_id());
							planSignManager.setSys_reg_user_id(auth.getName());
							planSignManager.setDept_cd(user.getDept_cd());
							planSignManager.setPlan_sign_id(planSign.getId());//2024-12-11 첫상신시 승인일자를 추가
							if (order_num == 1) {

								planSignManager.setState("coms11005");//승인
								planSignManager.setUser_id(auth.getName());
							}
							else if (order_num == 2) {
								planSignManager.setSys_upd_reg_date(null);
								planSignManager.setState("coms11004");//요청
								planSignManager.setComment("");

								String returnUrl = "https://dip.inno-n.com/lms/mypage";
								planSignManager.setSignform("comi1s6");
								emailService.sendMail(planSignManager.getUser_id(), planSignManager, "ing", returnUrl);
							}
							else {

								planSignManager.setSys_upd_reg_date(null);
								planSignManager.setState("coms11007");//대기
								planSignManager.setComment("");
							}
							signDAO.savePlanSignManager(planSignManager);
							/*
							if (order_num == 1) {
								PlanSignDetail signDetail = new PlanSignDetail();
								signDetail.setPlan_sign_id(planSign.getId());
								signDetail.setSign_manager_id(planSignManager.getId());
								signDetail.setState("coms11005");
								signDetail.setUser_id(auth.getName());
								signDetail.setSys_reg_user_id(auth.getName());
								signDetail.setComment(planSignManager.getComment());
								signDAO.savePlanSignDetail(signDetail);
							}
							*/
							order_num++;
						}
					}

					RevisionContent delContent = new RevisionContent();
					delContent.setDelete_at('Y');
					delContent.setRevision_id(jobRequest.getRevision().getId());
					currentDAO.updateRevisionContent(delContent);// 직무요구 교육내용(tb_revision_content) 삭제 : delete_at = 'Y'
					for (RevisionContent content : jobRequest.getRevision_contents()) {
						content.setSys_reg_user_id(auth.getName());
						content.setQualified_id(jobRequest.getId());
						content.setRevision_id(jobRequest.getRevision().getId());
						content.setGroup_id(jobRequest.getGroup_id());
						int contentId = currentDAO.saveRevisionContent(content);//Merge
					}

					RevisionDocument delDoc = new RevisionDocument();
					delDoc.setDelete_at('Y');
					delDoc.setRevision_id(jobRequest.getRevision().getId());
					currentDAO.updateRevisionDocument(delDoc);//직무요구문서(tb_revision_document) 삭제 : delete_at = 'Y' 
					for (RevisionDocument document : jobRequest.getRevision_documents()) {
						document.setSys_reg_user_id(auth.getName());
						document.setRevision_id(jobRequest.getRevision().getId());
						document.setGroup_id(jobRequest.getGroup_id());
						int documentId = currentDAO.saveRevisionDocument(document);
					}

					resultDTO.setState(true);
					resultDTO.setCode(201);
					resultDTO.setMessage(messageSource.getMessage("api.message.201", new String[] { "직무요구서" }, Locale.KOREA));
					resultDTO.setResult(jobRequest.getId());
				}
				catch (Exception e) {
					e.printStackTrace();
					resultDTO.setState(false);
					resultDTO.setCode(400);
					resultDTO.setMessage(messageSource.getMessage("api.message.400", new String[] { "직무요구서" }, Locale.KOREA));
					resultDTO.setResult(null);
				}
			}
		}
		return resultDTO;
	}

	@Transactional
	@Override
	public ResultDTO updateJobRequst(JobRequest jobRequest, HttpServletRequest request, Authentication auth) {
		resultDTO = new ResultDTO();

		resultDTO = commonService.findCheckUserAuth(jobRequest.getUser(), request, auth);

		if (resultDTO.getCode() == 200) {

			User user = (User) auth.getPrincipal();
			jobRequest.setSys_reg_user_id(auth.getName());

			int resultId = 0;
			try {

				jobRequest.getRevision().setUse_flag('W');
				resultId = currentDAO.updateJobRevision(jobRequest.getRevision());
				int updateId = jobRequest.getRevision().getId();
				DocumentMemo memo = new DocumentMemo();
				memo.setDocument_id(updateId);
				memo.setMemo(jobRequest.getSignUserList().get(0).getComment());
				memo.setType("job");
				memo.setSys_reg_user_id(auth.getName());
				documentDAO.saveDocumentMemo(memo);

				int logId = commonService.saveLog(LogEntity.builder().type("job").table_id(memo.getId()).page_nm("job").url_addr(request.getRequestURI()).state("insert").reg_user_id(auth.getName()).build());

				jobRequest.getRevision().setSys_reg_user_id(auth.getName());
				jobRequest.getRevision().setId(updateId);
				jobRequest.getRevision().setGroup_id(jobRequest.getGroup_id());
				jobRequest.getRevision().setUse_flag('W');
				int revisionId = currentDAO.updateJobRevision(jobRequest.getRevision());
				if (revisionId > 0) {

					PlanSign paramSign = new PlanSign();
					paramSign.setSignform("comi1s6");
					paramSign.setPlan_id(updateId);
					PlanSign resultPlanSign = signDAO.findPlanSign(paramSign);
					PlanSignManager planSignManager = new PlanSignManager();
					planSignManager.setPlan_id(updateId);
					planSignManager.setSignform("comi1s6");
					planSignManager.setState(jobRequest.getSignUserList().get(0).getState());
					planSignManager.setUser(jobRequest.getUser());
					planSignManager.setComment(jobRequest.getSignUserList().get(0).getComment());
					planSignManager.setOrder_num(1);
					planSignManager.setId(resultPlanSign.getId());
					signService.updatePlanSign(planSignManager, request, auth);

					RevisionContent delContent = new RevisionContent();
					delContent.setDelete_at('Y');
					delContent.setRevision_id(updateId);
					currentDAO.updateRevisionContent(delContent);

					RevisionDocument delDocument = new RevisionDocument();
					delDocument.setDelete_at('Y');
					delDocument.setRevision_id(updateId);
					currentDAO.updateRevisionDocument(delDocument);

					for (RevisionContent content : jobRequest.getRevision_contents()) {
						content.setSys_reg_user_id(auth.getName());
						content.setQualified_id(jobRequest.getId());
						content.setRevision_id(updateId);
						content.setGroup_id(jobRequest.getGroup_id());
						int contentId = currentDAO.saveRevisionContent(content);
					}

					for (RevisionDocument document : jobRequest.getRevision_documents()) {
						document.setSys_reg_user_id(auth.getName());
						document.setRevision_id(updateId);
						document.setGroup_id(jobRequest.getGroup_id());
						int documentId = currentDAO.saveRevisionDocument(document);
					}
				}

				resultDTO.setState(true);
				resultDTO.setCode(201);
				resultDTO.setMessage(messageSource.getMessage("api.message.201", new String[] { "직무요구서" }, Locale.KOREA));
				resultDTO.setResult(jobRequest.getId());
			}
			catch (Exception e) {
				e.printStackTrace();
				resultDTO.setState(false);
				resultDTO.setCode(400);
				resultDTO.setMessage(messageSource.getMessage("api.message.400", new String[] { "직무요구서" }, Locale.KOREA));
				resultDTO.setResult(null);
			}

		}
		return resultDTO;
	}

	@Override
	public ResultDTO findJobSkillList(ManagementPlanUserDTO planUserDTO, HttpServletRequest request, Authentication auth) {
		resultDTO = new ResultDTO();
		ManagementPlanUserDTO managementPlanUserDTO = new ManagementPlanUserDTO();
		managementPlanUserDTO.setQms_user_id(auth.getName());
		List<JobRevision> revisionList = currentDAO.findJobSkillList(managementPlanUserDTO);
		if (!DataLib.isEmpty(revisionList)) {
			//            int logId = commonService.saveLog(LogEntity.builder()
			//                    .table_id(jobRequest.getId())
			//                    .page_nm("jobRevision")
			//                    .url_addr(request.getRequestURI())
			//                    .state("view")
			//                    .reg_user_id(auth.getName())
			//                    .build());

			resultDTO.setState(true);
			resultDTO.setCode(200);
			resultDTO.setMessage(messageSource.getMessage("api.message.200", new String[] { "직무버전" }, Locale.KOREA));
			resultDTO.setResult(revisionList);
		}
		else {
			resultDTO.setState(false);
			resultDTO.setCode(400);
			resultDTO.setMessage(messageSource.getMessage("api.message.400", new String[] { "직무버전" }, Locale.KOREA));
			resultDTO.setResult(null);
		}

		return resultDTO;
	}

	@Transactional
	@Override
	public ResultDTO updateJobSkill(JobSkill jobSkill, HttpServletRequest request, Authentication auth) {
		resultDTO = new ResultDTO();
		resultDTO = commonService.findCheckUserAuth(jobSkill.getUser(), request, auth);

		if (resultDTO.getCode() == 200) {
			User user = (User) auth.getPrincipal();
			jobSkill.setSys_reg_user_id(auth.getName());

			int resultId = 0;
			try {
				jobSkill.setSys_reg_user_id(auth.getName());
				JobSkillEntity paramJob = new JobSkillEntity(jobSkill);
				paramJob.setSkill_user_id(auth.getName());
				paramJob.setUse_flag('W');
				resultId = currentDAO.updateJobSkill(paramJob);

				if (resultId > 0) {
					JobSkillItemEntity delItem = new JobSkillItemEntity();
					delItem.setJob_skill_id(paramJob.getId());
					delItem.setDelete_at('Y');

					currentDAO.updateJobSKillItem(delItem);

					DocumentMemo memo = new DocumentMemo();
					memo.setDocument_id(jobSkill.getId());
					memo.setMemo(jobSkill.getSignUserList().get(0).getComment());
					memo.setType("skill");
					memo.setSys_reg_user_id(auth.getName());
					documentDAO.saveDocumentMemo(memo);

					int logId = commonService.saveLog(LogEntity.builder().type("skill").table_id(memo.getId()).page_nm("detail").url_addr(request.getRequestURI()).state("insert").reg_user_id(auth.getName()).build());
					for (JobSkillItemEntity jobSkillItemEntity : jobSkill.getJobSkillItemList()) {
						jobSkillItemEntity.setSys_reg_user_id(auth.getName());
						jobSkillItemEntity.setJob_skill_id(jobSkill.getId());
						SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

						Calendar cal = Calendar.getInstance();
						Date date = jobSkillItemEntity.getPlan_end_date();
						cal.setTime(date);
						cal.add(Calendar.YEAR, jobSkillItemEntity.getQualified_yn());		//년 더하기

						jobSkillItemEntity.setSkill_expired_date(cal.getTime());
						currentDAO.saveJobSkillItem(jobSkillItemEntity);
					}
					PlanSign paramSign = new PlanSign();
					paramSign.setSignform("comi1s7");
					paramSign.setPlan_id(jobSkill.getId());
					PlanSign resultPlanSign = signDAO.findPlanSign(paramSign);

					if (resultPlanSign != null) {
						PlanSignManager planSignManager = new PlanSignManager();
						planSignManager.setPlan_id(jobSkill.getId());
						planSignManager.setSignform("comi1s7");
						planSignManager.setState(jobSkill.getSignUserList().get(0).getState());
						planSignManager.setUser(jobSkill.getUser());
						planSignManager.setComment(jobSkill.getSignUserList().get(0).getComment());
						planSignManager.setOrder_num(1);
						planSignManager.setId(resultPlanSign.getId());
						signService.updatePlanSign(planSignManager, request, auth);

					}
					resultDTO.setState(true);
					resultDTO.setCode(202);
					resultDTO.setMessage(messageSource.getMessage("api.message.202", new String[] { "직무기술서", "수정" }, Locale.KOREA));
					resultDTO.setResult(jobSkill.getId());
				}

			}
			catch (Exception e) {
				e.printStackTrace();
				log.info(e.getCause().toString());
				resultDTO.setState(false);
				resultDTO.setCode(400);
				resultDTO.setMessage(messageSource.getMessage("api.message.400", new String[] { "직무기술서" }, Locale.KOREA));
				resultDTO.setResult(null);
				return resultDTO;
			}

		}

		return resultDTO;
	}

	@Transactional
	@Override
	public ResultDTO saveJobSkill(JobSkill jobSkill, HttpServletRequest request, Authentication auth) {
		resultDTO = new ResultDTO();
		resultDTO = commonService.findCheckUserAuth(jobSkill.getUser(), request, auth);

		if (resultDTO.getCode() == 200) {
			User user = (User) auth.getPrincipal();
			jobSkill.setSys_reg_user_id(auth.getName());

			int resultId = 0;
			try {
				jobSkill.setSys_reg_user_id(auth.getName());
				JobSkillEntity paramJob = new JobSkillEntity(jobSkill);
				paramJob.setSkill_user_id(auth.getName());
				paramJob.setUse_flag('W');
				resultId = currentDAO.saveJobSkill(paramJob);//여기서 오류남

				DocumentMemo memo = new DocumentMemo();
				memo.setDocument_id(paramJob.getId());
				memo.setMemo(jobSkill.getSignUserList().get(0).getComment());
				memo.setType("skill");
				memo.setSys_reg_user_id(auth.getName());
				documentDAO.saveDocumentMemo(memo);

				int logId = commonService.saveLog(LogEntity.builder().type("skill").table_id(memo.getId()).page_nm("detail").url_addr(request.getRequestURI()).state("insert").reg_user_id(auth.getName()).build());
				for (JobSkillItemEntity jobSkillItemEntity : jobSkill.getJobSkillItemList()) {
					jobSkillItemEntity.setSys_reg_user_id(auth.getName());
					jobSkillItemEntity.setJob_skill_id(paramJob.getId());
					jobSkillItemEntity.setJv_id(paramJob.getVersion());
					jobSkillItemEntity.setMandate_user_id(paramJob.getSkill_user_id());
					SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

					Calendar cal = Calendar.getInstance();
					Date date = jobSkillItemEntity.getPlan_end_date();
					cal.setTime(date);
					cal.add(Calendar.YEAR, jobSkillItemEntity.getQualified_yn());		//년 더하기

					jobSkillItemEntity.setSkill_expired_date(cal.getTime());
					currentDAO.saveJobSkillItem(jobSkillItemEntity);

				}

				if (resultId > 0) {
					PlanSign planSign = new PlanSign();
					planSign.setId(jobSkill.getSign_id());
					planSign.setPlan_id(paramJob.getId());
					planSign.setStatus(jobSkill.getSignUserList().get(1).getSign_type());
					planSign.setSys_reg_user_id(auth.getName());
					planSign.setDept_cd(user.getDept_cd());
					planSign.setSys_upd_reg_date(new Date());
					signDAO.savePlanSign(planSign);

					int order_num = 1;
					for (PlanSignManager planSignManager : jobSkill.getSignUserList()) {
						int singId = planSignManager.getSign_id() == 0 ? jobSkill.getSign_id() : planSignManager.getSign_id();
						planSignManager.setSign_id(singId);
						planSignManager.setPlan_id(paramJob.getId());
						planSignManager.setSign_type(planSignManager.getSign_type());
						planSignManager.setOrder_num(order_num);
						planSignManager.setSign_category("comc11001");
						planSignManager.setGroup_id(jobSkill.getGroup_id());
						planSignManager.setSys_reg_user_id(auth.getName());
						planSignManager.setDept_cd(user.getDept_cd());
						planSignManager.setPlan_sign_id(planSign.getId());//2024-12-11 첫상신시 승인일자를 추가
						if (order_num == 1) {
							planSignManager.setState("coms11005");
							planSignManager.setUser_id(auth.getName());
						}
						else if (order_num == 2) {
							planSignManager.setSys_upd_reg_date(null);
							planSignManager.setState("coms11004");
							planSignManager.setComment("");

							String returnUrl = "https://dip.inno-n.com/lms/mypage";
							planSignManager.setSignform("comi1s7");
							emailService.sendMail(planSignManager.getUser_id(), planSignManager, "ing", returnUrl);
						}
						else {

							planSignManager.setSys_upd_reg_date(null);
							planSignManager.setState("coms11007");
							planSignManager.setComment("");
						}
						signDAO.savePlanSignManager(planSignManager);
						/*
						if (order_num == 1) {
							PlanSignDetail signDetail = new PlanSignDetail();
							signDetail.setPlan_sign_id(planSign.getId());
							signDetail.setSign_manager_id(planSignManager.getId());
							signDetail.setState("coms11005");
							signDetail.setUser_id(auth.getName());
							signDetail.setSys_reg_user_id(auth.getName());
							signDetail.setComment(planSignManager.getComment());
							signDAO.savePlanSignDetail(signDetail);
						}
						*/
						order_num++;
					}
					resultDTO.setState(true);
					resultDTO.setCode(201);
					resultDTO.setMessage(messageSource.getMessage("api.message.201", new String[] { "직무기술서" }, Locale.KOREA));
					resultDTO.setResult(paramJob.getId());
				}

			}
			catch (Exception e) {
				e.printStackTrace();
				log.info(e.getCause().toString());
				resultDTO.setState(false);
				resultDTO.setCode(400);
				resultDTO.setMessage(messageSource.getMessage("api.message.400", new String[] { "직무기술서" }, Locale.KOREA));
				resultDTO.setResult(null);
				return resultDTO;
			}

		}

		return resultDTO;
	}

	@Override
	public ResultDTO userJobSkillList(JobSkill jobSkill, HttpServletRequest request, Authentication auth) {

		resultDTO = new ResultDTO();
		if ("my".equals(jobSkill.getSearch_type())) {
			jobSkill.setSkill_user_id(auth.getName());
		}
		List<JobSkillDTO> revisionList = currentDAO.userJobSkillList(jobSkill);
		if (!DataLib.isEmpty(revisionList)) {
			//            int logId = commonService.saveLog(LogEntity.builder()
			//                    .table_id(jobRequest.getId())
			//                    .page_nm("jobRevision")
			//                    .url_addr(request.getRequestURI())
			//                    .state("view")
			//                    .reg_user_id(auth.getName())
			//                    .build());

			resultDTO.setState(true);
			resultDTO.setCode(200);
			resultDTO.setMessage(messageSource.getMessage("api.message.200", new String[] { "직무 인증현황" }, Locale.KOREA));
			resultDTO.setResult(revisionList);
		}
		else {
			resultDTO.setState(false);
			resultDTO.setCode(400);
			resultDTO.setMessage(messageSource.getMessage("api.message.400", new String[] { "직무 인증현황" }, Locale.KOREA));
			resultDTO.setResult(null);
		}

		return resultDTO;
	}

	@Override
	public ResultDTO JobSkillUserItemList(JobSkill jobSkill, HttpServletRequest request, Authentication auth) {
		resultDTO = new ResultDTO();
		User user = (User) auth.getPrincipal();
		List<JobSkillDTO> revisionList = currentDAO.JobSkillUserItemList(jobSkill);
		if (!DataLib.isEmpty(revisionList)) {
			//            int logId = commonService.saveLog(LogEntity.builder()
			//                    .table_id(jobRequest.getId())
			//                    .page_nm("jobRevision")
			//                    .url_addr(request.getRequestURI())
			//                    .state("view")
			//                    .reg_user_id(auth.getName())
			//                    .build());

			resultDTO.setState(true);
			resultDTO.setCode(200);
			resultDTO.setMessage(messageSource.getMessage("api.message.200", new String[] { "직무 인증현황" }, Locale.KOREA));
			resultDTO.setResult(revisionList);
		}
		else {
			resultDTO.setState(false);
			resultDTO.setCode(400);
			resultDTO.setMessage(messageSource.getMessage("api.message.400", new String[] { "직무 인증현황" }, Locale.KOREA));
			resultDTO.setResult(null);
		}

		return resultDTO;
	}

    @Override
    public ResultDTO deleteTempJobRequest(JobRequest jobRequest, HttpServletRequest request, Authentication auth) {
        resultDTO = new ResultDTO();
        try {
            jobRequest.setSys_upd_user_id(auth.getName());
            int deleteNum = currentDAO.deleteTempJobRequest(jobRequest);
            if (deleteNum > 0) {
                resultDTO.setState(true);
                resultDTO.setCode(202);
                resultDTO.setMessage(messageSource.getMessage("api.message.202", new String[]{"직무교육 임시저장", "삭제"}, Locale.KOREA));
                resultDTO.setResult(jobRequest.getId());
            }
        } catch (Exception e) {
            System.out.println(e.getCause().toString());
            resultDTO.setState(false);
            resultDTO.setCode(400);
            resultDTO.setMessage(messageSource.getMessage("api.message.400", new String[]{"직무교육 임시저장 삭제"}, Locale.KOREA));
        }

        return resultDTO;
    }

    @Override
    public ResultDTO findJobRequestList(JobRequest jobRequest, HttpServletRequest request, Authentication auth) {
        resultDTO = new ResultDTO();
        User user = (User) auth.getPrincipal();

        // todo 정주원 안준식 , 백단 프론트중 한곳에서 전체 해당하는 본인 그룹이 필요하니 협의 필요
        if (jobRequest.getGroup_id() == 0) {
            List<Group> groupList = commonDAO.findGroupInfoByDeptCd(user.getDept_cd());
            if (!DataLib.isEmpty(groupList)) {
                jobRequest.setGroupList(groupList);
            }
        }
        //todo 정주원 group_id가 없을경우 choose otherwise에 전체 그룹 필요 ..
        List<JobRequestDTO> resultList = currentDAO.findJobRequestList(jobRequest);
        if (!DataLib.isEmpty(resultList)) {
            //            int logId = commonService.saveLog(LogEntity.builder()
            //                    .table_id(jobRequest.getId())
            //                    .page_nm("jobRequest")
            //                    .url_addr(request.getRequestURI())
            //                    .state("view")
            //                    .reg_user_id(auth.getName())
            //                    .build());

            resultList.forEach(jobRequestDTO -> {
                JobRevision paramJob = new JobRevision();
                paramJob.setQualified_id(jobRequestDTO.getId());

                List<JobRevision> revisionList = currentDAO.findJobRevisionList(paramJob);
                jobRequestDTO.setChildren(revisionList);
                if (!DataLib.isEmpty(revisionList)) {
                    jobRequestDTO.setDept_nm(revisionList.get(0).getDept_nm());
                    jobRequestDTO.setVersion(revisionList.get(0).getVersion());
                }
            });

            resultDTO.setState(true);
            resultDTO.setCode(200);
            resultDTO.setMessage(messageSource.getMessage("api.message.200", new String[] { "직무요구서" }, Locale.KOREA));
            resultDTO.setResult(resultList);
        }
        else {
            resultDTO.setState(false);
            resultDTO.setCode(400);
            resultDTO.setMessage(messageSource.getMessage("api.message.400", new String[] { "직무요구서" }, Locale.KOREA));
            resultDTO.setResult(null);
        }

        return resultDTO;
    }

    @Override
    public ResultDTO findMyJobRequestList(JobRequest jobRequest, HttpServletRequest request, Authentication auth) {
        resultDTO = new ResultDTO();
        User user = (User) auth.getPrincipal();

        //todo 정주원 group_id가 없을경우 choose otherwise에 전체 그룹 필요 ..
        List<JobRequestDTO> resultList = currentDAO.findJobRequestList(jobRequest);
        if (!DataLib.isEmpty(resultList)) {
            //            int logId = commonService.saveLog(LogEntity.builder()
            //                    .table_id(jobRequest.getId())
            //                    .page_nm("jobRequest")
            //                    .url_addr(request.getRequestURI())
            //                    .state("view")
            //                    .reg_user_id(auth.getName())
            //                    .build());

            resultList.forEach(jobRequestDTO -> {
                JobRevision paramJob = new JobRevision();
                paramJob.setQualified_id(jobRequestDTO.getId());

                List<JobRevision> revisionList = currentDAO.findJobRevisionList(paramJob);
                jobRequestDTO.setChildren(revisionList);
                if (!DataLib.isEmpty(revisionList)) {
                    jobRequestDTO.setDept_nm(revisionList.get(0).getDept_nm());
                    jobRequestDTO.setVersion(revisionList.get(0).getVersion());
                }
            });

            resultDTO.setState(true);
            resultDTO.setCode(200);
            resultDTO.setMessage(messageSource.getMessage("api.message.200", new String[] { "직무요구서" }, Locale.KOREA));
            resultDTO.setResult(resultList);
        }
        else {
            resultDTO.setState(false);
            resultDTO.setCode(400);
            resultDTO.setMessage(messageSource.getMessage("api.message.400", new String[] { "직무요구서" }, Locale.KOREA));
            resultDTO.setResult(null);
        }

        return resultDTO;
    }

	@Override
	public ResultDTO findJobRevisionList(JobRequest jobRequest, HttpServletRequest request, Authentication auth) {
		resultDTO = new ResultDTO();
		User user = (User) auth.getPrincipal();
		JobRevision jobRevision = new JobRevision();
		if (jobRequest.getQualified_id() != 0) {
			//            jobRevision.setId(jobRequest.getId());
			jobRevision.setQualified_id(jobRequest.getQualified_id());
		}
		else {
			jobRevision.setQualified_id(jobRequest.getId());
		}
		List<JobRevision> revisionList = currentDAO.findJobRevisionList(jobRevision);
		if (!DataLib.isEmpty(revisionList)) {

			resultDTO.setState(true);
			resultDTO.setCode(200);
			resultDTO.setMessage(messageSource.getMessage("api.message.200", new String[] { "직무버전" }, Locale.KOREA));
			resultDTO.setResult(revisionList);
		}
		else {
			resultDTO.setState(false);
			resultDTO.setCode(400);
			resultDTO.setMessage(messageSource.getMessage("api.message.400", new String[] { "직무버전" }, Locale.KOREA));
			resultDTO.setResult(null);
		}

		return resultDTO;
	}

	@Override
	public ResultDTO findRevisionContentList(JobRevision jobRevision, HttpServletRequest request, Authentication auth) {
		resultDTO = new ResultDTO();
		User user = (User) auth.getPrincipal();
		List<RevisionContent> resultList = currentDAO.findRevisionContentList(jobRevision.getId());
		if (!DataLib.isEmpty(resultList)) {
			//            int logId = commonService.saveLog(LogEntity.builder()
			//                    .table_id(jobRevision.getId())
			//                    .page_nm("revisionContent")
			//                    .url_addr(request.getRequestURI())
			//                    .state("view")
			//                    .reg_user_id(auth.getName())
			//                    .build());

			resultDTO.setState(true);
			resultDTO.setCode(200);
			resultDTO.setMessage(messageSource.getMessage("api.message.200", new String[] { "교육내용" }, Locale.KOREA));
			resultDTO.setResult(resultList);
		}
		else {
			resultDTO.setState(false);
			resultDTO.setCode(400);
			resultDTO.setMessage(messageSource.getMessage("api.message.400", new String[] { "교육내용" }, Locale.KOREA));
			resultDTO.setResult(null);
		}

		return resultDTO;
	}

	@Override
	public ResultDTO findRevisionDocumentList(JobRevision jobRevision, HttpServletRequest request, Authentication auth) {
		resultDTO = new ResultDTO();
		List<RevisionDocument> resultList = currentDAO.findRevisionDocumentList(jobRevision.getId());

		if (!DataLib.isEmpty(resultList)) {
			//            int logId = commonService.saveLog(LogEntity.builder()
			//                    .table_id(jobRevision.getId())
			//                    .page_nm("revisionDocument")
			//                    .url_addr(request.getRequestURI())
			//                    .state("view")
			//                    .reg_user_id(auth.getName())
			//                    .build());

			resultDTO.setState(true);
			resultDTO.setCode(200);
			resultDTO.setMessage(messageSource.getMessage("api.message.200", new String[] { "교육문서" }, Locale.KOREA));
			resultDTO.setResult(resultList);
		}
		else {
			resultDTO.setState(false);
			resultDTO.setCode(400);
			resultDTO.setMessage(messageSource.getMessage("api.message.400", new String[] { "교육문서" }, Locale.KOREA));
			resultDTO.setResult(null);
		}

		return resultDTO;
	}
}
