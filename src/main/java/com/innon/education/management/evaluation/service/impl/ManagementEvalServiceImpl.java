package com.innon.education.management.evaluation.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import com.innon.education.management.plan.repository.dto.EducationPlanUserInfoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.innon.education.common.util.DataLib;
import com.innon.education.controller.dto.ResultDTO;
import com.innon.education.management.evaluation.dao.ManagementEvalDAO;
import com.innon.education.management.evaluation.repository.dto.EduQuestionDTO;
import com.innon.education.management.evaluation.repository.dto.ManagementEvalDTO;
import com.innon.education.management.evaluation.repository.dto.QuestionInfoDTO;
import com.innon.education.management.evaluation.repository.dto.QuestionItemDTO;
import com.innon.education.management.evaluation.repository.entity.ManagementEvalEntity;
import com.innon.education.management.evaluation.repository.model.ManagementEval;
import com.innon.education.management.evaluation.service.ManagementEvalService;
import com.innon.education.management.plan.dao.ManagementPlanDAO;
import com.innon.education.management.plan.repository.dto.ManagementPlanDTO;
import com.innon.education.management.plan.repository.entity.ManagementPlanUserEntity;
import com.innon.education.management.plan.repository.model.ManagementPlan;
import com.innon.education.management.progress.dao.ManagementProgressDAO;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class ManagementEvalServiceImpl implements ManagementEvalService {

	private ResultDTO resultDTO;

	@Autowired
	ManagementEvalDAO managementEvalDAO;
	@Autowired
	MessageSource messageSource;
	@Autowired
	ManagementProgressDAO progressDAO;
	@Autowired
	ManagementPlanDAO planDAO;

	@Override
	public ResultDTO savePuqAnswer(List<ManagementEval> evalList, HttpServletRequest request, Authentication auth) {
		resultDTO = new ResultDTO();
		int saveNum = 0;
		String status = "";
		int code = 0;
		String answerCnt = "";
		boolean btnVisible = false;
		if (!DataLib.isEmpty(evalList)) {
			int gradeYCnt = 0;
			for (ManagementEval eval : evalList) {
				ManagementEvalEntity entity = new ManagementEvalEntity(eval);
				entity.setEpu_id(auth.getName());
				answerCnt = String.valueOf(entity.getAnswer_cnt());
				saveNum += managementEvalDAO.savePuqAnswer(entity);
				if (eval.getStatus().equals("TEMP")) {
					status = "임시저장";
					code = 202;
				}
				else {
					status = "제출";
					code = 201;
				}

				if (eval.getGrade_yn() == 'Y') {
					gradeYCnt++;
				}
			}

			double score = (double) gradeYCnt / evalList.size();
			double gradeScore = Math.round(score * 10) / 10.0;

			if (saveNum > 0) {

				ManagementPlan searchPlan = new ManagementPlan();
				searchPlan.setId(evalList.get(0).getPlan_id());
				ManagementPlanDTO resultPlan = planDAO.educationPlanDetail(searchPlan);
				double passingRate = (double) resultPlan.getPassing_rate() / 100;

				ManagementPlanUserEntity plan = new ManagementPlanUserEntity();
				plan.setQms_user_id(auth.getName());
				plan.setPlan_id(evalList.get(0).getPlan_id());

				ManagementPlan planInfo = new ManagementPlan();
				planInfo.setPlan_id(evalList.get(0).getPlan_id());
				planInfo.setEdu_user_id(auth.getName());
				EducationPlanUserInfoDTO educationPlanUserDTO = planDAO.educationPlanUserInfo(planInfo);

				if (passingRate > gradeScore) {
					if(educationPlanUserDTO.getStatus() == null || !educationPlanUserDTO.getStatus().equals("edus21004")) {
						plan.setStatus("edus21003"); // 불합격
					}
				}
				else if (passingRate <= gradeScore) {
					plan.setStatus("edus21004"); // 합격
					plan.setPass_yn('Y');
				}
				if(resultPlan.getRelation_num().equals(answerCnt)) {		// 답안제출 횟수가 재시험 횟수와 같을 경우
					if(plan.getStatus().equals("edus21004")) { // 합격일 경우
						System.out.println("정답제출 버튼 제거 안함"); // btnVisible = false;
						plan.setBtn_visible('Y');
					} else if (plan.getStatus().equals("edus21003")) { // 불합격일 경우
						System.out.println("정답제출 버튼 제거"); // btnVisible = true;
						plan.setBtn_visible('N');
					}
				} else {	// 답안제출 횟수가 재시험 횟수와 같지 않을 경우
					if(gradeScore == 1.0) { // 100점인 경우
						System.out.println("정답제출 버튼 제거"); // btnVisible = true;
						plan.setBtn_visible('N');
					}
				}
				planDAO.updatePlanUser(plan);

				resultDTO.setState(true);
				resultDTO.setCode(code);
				resultDTO.setMessage(messageSource.getMessage("api.message.202", new String[] { "답안", status }, Locale.KOREA));
				resultDTO.setResult(plan.getBtn_visible());
			}
			else {
				resultDTO.setState(false);
				resultDTO.setCode(400);
				resultDTO.setMessage(messageSource.getMessage("api.message.400", new String[] { "답안" }, Locale.KOREA));
				resultDTO.setResult(saveNum);
			}
		}
		else {
			resultDTO.setMessage("등록할 수강생 답변이 존재하지 않습니다.");
		}

		return resultDTO;
	}

	@Override
	public ResultDTO updatePuqAnswer(List<ManagementEval> evalList) {
		resultDTO = new ResultDTO();
		int updateNum = 0;
		if (!DataLib.isEmpty(evalList)) {
			for (ManagementEval eval : evalList) {
				ManagementEvalEntity entity = new ManagementEvalEntity(eval);
				updateNum += managementEvalDAO.updatePuqAnswer(entity);
			}

			if (updateNum > 0) {
				resultDTO.setMessage(updateNum + "건의 수강생 정답유무 정보가 수정 되었습니다.");
			}
			else {
				resultDTO.setMessage("수강생 정답유무 정보 수정이 실패 하였습니다.");
			}
		}
		else {
			resultDTO.setMessage("수정할 수강생 정보가 존재하지 않습니다.");
		}

		return resultDTO;
	}

	@Override
	public ResultDTO findQuestionInfo(ManagementEval eval, HttpServletRequest request, Authentication auth) {
		resultDTO = new ResultDTO();
		EduQuestionDTO eduQuestionDTO = new EduQuestionDTO();

		ManagementPlanDTO plan = progressDAO.findEducationPlanById(eval.getPlan_id());

		if (plan != null) {
			eduQuestionDTO.setTitle(plan.getTitle());
			eduQuestionDTO.setPlan_start_date(plan.getPlan_start_date());
			eduQuestionDTO.setPlan_end_date(plan.getPlan_end_date());
			eduQuestionDTO.setWork_num(plan.getWork_num());
		}

		if (eval.getView() != null && eval.getView().equals("eduPlan")) {
			List<QuestionInfoDTO> questionInfoList = managementEvalDAO.findEduQuestionInfo(eval);
			eduQuestionDTO.setQuestions(questionInfoList);
		}
		else {
			eval.setQms_user_id(auth.getName());
			String userPass = managementEvalDAO.findUserPassYn(eval);
			eduQuestionDTO.setPass_yn(userPass);
			List<QuestionInfoDTO> randomQuestionInfoList = new ArrayList<>();

			//tb_pu_q_answer : 수강생 답안
			List<QuestionInfoDTO> answerList = managementEvalDAO.findTempQuestionList(eval);
			if (DataLib.isEmpty(answerList)) {
				List<QuestionInfoDTO> questionInfoList = managementEvalDAO.findQuestionInfo(eval);//질문
				
				// 필수 질문
				List<QuestionInfoDTO> essentialList = new ArrayList<>(questionInfoList.stream().filter(item -> item.getEssential() == 'Y').toList());

				if (plan != null && essentialList.size() < plan.getQuestion_num()) {
					//필수가 아닌 질문
					List<QuestionInfoDTO> notEssentialList = questionInfoList.stream().filter(item -> item.getEssential() != 'Y').collect(Collectors.toList());
					if (!DataLib.isEmpty(notEssentialList)) {
						Collections.shuffle(notEssentialList);
						if(questionInfoList.size() < plan.getQuestion_num()) {
							notEssentialList = notEssentialList.subList(0, questionInfoList.size() - essentialList.size());
						}else {
							notEssentialList = notEssentialList.subList(0, plan.getQuestion_num() - essentialList.size());
						}
						essentialList.addAll(notEssentialList);
					}
				}
				answerList = essentialList;
				// 리스트 섞기
				Collections.shuffle(answerList);
				// 출제문항수(getQuestion_num()) 보다 실제 문제수가 작은 것을 처리하기 위함 : 이수철
				randomQuestionInfoList = answerList.subList(0, Math.min(plan.getQuestion_num(), answerList.size()));

				for (QuestionInfoDTO questionInfo : randomQuestionInfoList) {
					ManagementEval saveEval = new ManagementEval();
					saveEval.setId(questionInfo.getId());
					saveEval.setQuestion_info_id(questionInfo.getQuestion_info_id());
					saveEval.setPlan_id(questionInfo.getPlan_id());
					saveEval.setQms_user_id(questionInfo.getQms_user_id());
					saveEval.setUser_answer(questionInfo.getUser_answer());
					saveEval.setStatus(questionInfo.getStatus());
					saveEval.setAnswer_cnt(questionInfo.getAnswer_cnt());
					saveEval.setQb_q_id(questionInfo.getQb_q_id());

					ManagementEvalEntity entity = new ManagementEvalEntity(saveEval);
					entity.setEpu_id(auth.getName());
					managementEvalDAO.savePuqAnswer(entity);
				}
			}
			else {
				randomQuestionInfoList = answerList;
			}

			for (QuestionInfoDTO questionInfoDTO : randomQuestionInfoList) {
				List<QuestionItemDTO> questionItems = managementEvalDAO.findQuestionItem(questionInfoDTO.getQb_q_id());
				if (questionItems != null) {
					questionInfoDTO.setQuestion_items(questionItems);
				}
			}
			eduQuestionDTO.setQuestions(randomQuestionInfoList);
		}

		try {
			resultDTO.setState(true);
			resultDTO.setCode(200);
			resultDTO.setMessage(messageSource.getMessage("api.message.200", new String[] { "문제목록" }, Locale.KOREA));
			resultDTO.setResult(eduQuestionDTO);
		}
		catch (Exception e) {
			resultDTO.setState(false);
			resultDTO.setCode(404);
			resultDTO.setMessage(messageSource.getMessage("api.message.404", new String[] { "문제목록의" }, Locale.KOREA));
			resultDTO.setResult(null);
		}

		return resultDTO;
	}

	@Override
	public ResultDTO findWrongAnswerList(ManagementEval eval, HttpServletRequest request, Authentication auth) {
		resultDTO = new ResultDTO();
		eval.setQms_user_id(auth.getName());
		List<ManagementEvalDTO> resultList = managementEvalDAO.findWrongAnswerList(eval);

		if (!DataLib.isEmpty(resultList)) {
			resultDTO.setState(true);
			resultDTO.setCode(200);
			resultDTO.setMessage(messageSource.getMessage("api.message.200", new String[] { "정답여부" }, Locale.KOREA));
			resultDTO.setResult(resultList);
		}
		else {
			resultDTO.setState(false);
			resultDTO.setCode(400);
			resultDTO.setMessage(messageSource.getMessage("api.message.400", new String[] { "정답여부" }, Locale.KOREA));
			resultDTO.setResult(null);
		}

		return resultDTO;
	}
}
