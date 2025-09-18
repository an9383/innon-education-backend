package com.innon.education.educurrent.service.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.innon.education.common.util.DataLib;
import com.innon.education.code.controller.dto.ResultDTO;
import com.innon.education.educurrent.dao.EduCurrentDAO;
import com.innon.education.educurrent.repository.dto.EduCurrentCntDTO;
import com.innon.education.educurrent.repository.dto.EduCurrentListDTO;
import com.innon.education.educurrent.repository.dto.EduCurrentUserDTO;
import com.innon.education.educurrent.repository.model.EduCurrent;
import com.innon.education.educurrent.service.EduCurrentService;
import com.innon.education.management.plan.repository.dto.ManagementPlanDTO;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class EduCurrentServiceImpl implements EduCurrentService {

    private ResultDTO resultDTO;

    @Autowired
    EduCurrentDAO eduCurrentDAO;

    @Autowired
    MessageSource messageSource;

    @Override
    public ResultDTO findEduCurrentByPlanId(EduCurrent eduCurrent) {
        resultDTO = new ResultDTO();
        EduCurrentCntDTO eduCurrentCntDTO = new EduCurrentCntDTO();
        int planId = eduCurrent.getPlan_id();
        int eduTotal = 0;

        int eduResult = eduCurrentDAO.findEduResultByPlanId(planId);
        if(eduResult > 0) {
            eduTotal = eduResult;
            eduCurrentCntDTO.setResult(eduResult);
            eduCurrentCntDTO.setEvaluation("-");
            eduCurrentCntDTO.setProgress("-");
            eduCurrentCntDTO.setPlan("-");
        } else {
            int eduEval = eduCurrentDAO.findEduEvaluationByPlanId(planId);
            int eduPrgs = eduCurrentDAO.findEduProgressByPlanId(planId);
            int eduPlan = eduCurrentDAO.findEduPlanByPlanId(planId);
            eduTotal = eduResult + eduEval + eduPrgs + eduPlan;

            eduCurrentCntDTO.setResult("-");
            eduCurrentCntDTO.setEvaluation(eduEval);
            eduCurrentCntDTO.setProgress(eduPrgs);
            eduCurrentCntDTO.setPlan(eduPlan);
        }

        if(eduTotal > 0) {
            resultDTO.setState(true);
            resultDTO.setResult(eduCurrentCntDTO);
        } else {
            resultDTO.setState(false);
        }

        return resultDTO;
    }

    @Override
    public ResultDTO findAllEduCurrent(EduCurrent eduCurrent) {
        resultDTO = new ResultDTO();
        EduCurrentCntDTO eduCurrentCntDTO = new EduCurrentCntDTO();
        Map<String, Object> searchParams = new HashMap<>();
        if(eduCurrent != null) {
            searchParams.put("search_start_date", eduCurrent.getSearch_start_date());
            searchParams.put("search_end_date", eduCurrent.getSearch_end_date());
        }
        int eduPlan = eduCurrentDAO.findAllEduPlan(searchParams);
        int eduEval = eduCurrentDAO.findAllEduProgress(searchParams);
        int eduPrgs = 0;
        int eduResult = 0;
        int eduTotal = eduPlan + eduEval + eduPrgs + eduResult;

        if(eduTotal > 0) {
            eduCurrentCntDTO.setPlan(eduPlan);
            eduCurrentCntDTO.setEvaluation(eduEval);
            eduCurrentCntDTO.setProgress(eduPrgs);
            eduCurrentCntDTO.setResult(eduResult);

            resultDTO.setState(true);
            resultDTO.setResult(eduCurrentCntDTO);
        } else {
            resultDTO.setState(false);
        }

        return resultDTO;
    }

    @Override
    public ResultDTO findEduCurrentByUserId(EduCurrent eduCurrent) {
        resultDTO = new ResultDTO();
        String qmsUserId = eduCurrent.getQms_user_id();

        EduCurrentCntDTO eduCurrentCntDTO = new EduCurrentCntDTO();
        EduCurrentListDTO eduCurrentListDTO = new EduCurrentListDTO();

        eduCurrentListDTO.setPlanList(eduCurrentDAO.findEduPlanByUserId(qmsUserId));
        eduCurrentListDTO.setProgressList(eduCurrentDAO.findEduProgressByUserId(qmsUserId));
        eduCurrentListDTO.setEvaluationList(eduCurrentDAO.findEduEvaluationByUserId(qmsUserId));
        eduCurrentListDTO.setResultList(eduCurrentDAO.findEduResultByUserId(qmsUserId));

        int eduTotal = eduCurrentListDTO.getPlanList().size() +
                eduCurrentListDTO.getProgressList().size() +
                eduCurrentListDTO.getEvaluationList().size() +
                eduCurrentListDTO.getResultList().size();

        if(eduTotal > 0) {
            eduCurrentCntDTO.setPlan(eduCurrentListDTO.getPlanList().size());
            eduCurrentCntDTO.setProgress(eduCurrentListDTO.getProgressList().size());
            eduCurrentCntDTO.setEvaluation(eduCurrentListDTO.getEvaluationList().size());
            eduCurrentCntDTO.setResult(eduCurrentListDTO.getResultList().size());
            eduCurrentCntDTO.setTotal(eduTotal);

            eduCurrentListDTO.setCurrentCnt(eduCurrentCntDTO);

            resultDTO.setState(true);
            resultDTO.setResult(eduCurrentListDTO);
        } else {
            resultDTO.setState(false);
        }

        return resultDTO;
    }

    @Override
    public ResultDTO findEduCurrentUserList(EduCurrent eduCurrent) {
        resultDTO = new ResultDTO();
        String searchQmsUserId = eduCurrent != null ? eduCurrent.getQms_user_id() : null;
        List<EduCurrentUserDTO> eduCurrentUserList = eduCurrentDAO.findEduCurrentUserList(searchQmsUserId);

        if(!DataLib.isEmpty(eduCurrentUserList)) {
            for(EduCurrentUserDTO eduCurrentUser : eduCurrentUserList) {
                EduCurrentCntDTO eduCurrentCntDTO = new EduCurrentCntDTO();

                List<ManagementPlanDTO> planList = eduCurrentDAO.findEduPlanByUserId(eduCurrentUser.getQms_user_id());
                List<ManagementPlanDTO> progressList = eduCurrentDAO.findEduProgressByUserId(eduCurrentUser.getQms_user_id());
                List<ManagementPlanDTO> evaluationList = eduCurrentDAO.findEduEvaluationByUserId(eduCurrentUser.getQms_user_id());
                List<ManagementPlanDTO> resultList = eduCurrentDAO.findEduResultByUserId(eduCurrentUser.getQms_user_id());

                int eduTotal = planList.size() +
                        progressList.size() +
                        evaluationList.size() +
                        resultList.size();

                eduCurrentCntDTO.setPlan(planList.size());
                eduCurrentCntDTO.setProgress(progressList.size());
                eduCurrentCntDTO.setEvaluation(evaluationList.size());
                eduCurrentCntDTO.setResult(resultList.size());
                eduCurrentCntDTO.setTotal(eduTotal);

                eduCurrentUser.setEduCurrentCntDTO(eduCurrentCntDTO);
            }

            resultDTO.setState(true);
            resultDTO.setResult(eduCurrentUserList);
        } else {
            resultDTO.setState(false);
        }

        return resultDTO;
    }

    @Override
    public ResultDTO findEduCurrentPlanList(EduCurrent eduCurrent, HttpServletRequest request, Authentication auth) {
        resultDTO  = new ResultDTO();
        List<ManagementPlanDTO> planList = eduCurrentDAO.findEduPlanByUserId(eduCurrent.getQms_user_id());

        if(!DataLib.isEmpty(planList)) {
            resultDTO.setState(true);
            resultDTO.setCode(200);
            resultDTO.setMessage(messageSource.getMessage("api.message.200", new String[]{"진행현황"}, Locale.KOREA));
            resultDTO.setResult(planList);
        } else {
            resultDTO.setState(false);
            resultDTO.setCode(400);
            resultDTO.setMessage(messageSource.getMessage("api.message.400", new String[]{"진행현황"}, Locale.KOREA));
            resultDTO.setResult(null);
        }

        return resultDTO;
    }

    @Override
    public ResultDTO findEduCurrentProgressList(EduCurrent eduCurrent, HttpServletRequest request, Authentication auth) {
        resultDTO  = new ResultDTO();
        List<ManagementPlanDTO> progressList = eduCurrentDAO.findEduProgressByUserId(eduCurrent.getQms_user_id());

        if(!DataLib.isEmpty(progressList)) {
            resultDTO.setState(true);
            resultDTO.setCode(200);
            resultDTO.setMessage(messageSource.getMessage("api.message.200", new String[]{"진행현황"}, Locale.KOREA));
            resultDTO.setResult(progressList);
        } else {
            resultDTO.setState(false);
            resultDTO.setCode(400);
            resultDTO.setMessage(messageSource.getMessage("api.message.400", new String[]{"진행현황"}, Locale.KOREA));
            resultDTO.setResult(null);
        }

        return resultDTO;
    }

    @Override
    public ResultDTO findEduCurrentEvaluationList(EduCurrent eduCurrent, HttpServletRequest request, Authentication auth) {
        resultDTO  = new ResultDTO();
        List<ManagementPlanDTO> evaluationList = eduCurrentDAO.findEduEvaluationByUserId(eduCurrent.getQms_user_id());
        Iterator<ManagementPlanDTO> iterator = evaluationList.iterator();
        while(iterator.hasNext()) {
            ManagementPlanDTO eval  = iterator.next();
            if(
                    eval.getUser_status().equals("edus21003") &&
                    eval.getRelation_num().equals(String.valueOf(eval.getAnswer_cnt())) &&
                    eval.getPass_yn() != 'Y'
            ) {
                iterator.remove();
            }
        }

        if(!DataLib.isEmpty(evaluationList)) {
            resultDTO.setState(true);
            resultDTO.setCode(200);
            resultDTO.setMessage(messageSource.getMessage("api.message.200", new String[]{"진행현황"}, Locale.KOREA));
            resultDTO.setResult(evaluationList);
        } else {
            resultDTO.setState(false);
            resultDTO.setCode(400);
            resultDTO.setMessage(messageSource.getMessage("api.message.400", new String[]{"진행현황"}, Locale.KOREA));
            resultDTO.setResult(null);
        }

        return resultDTO;
    }

    @Override
    public ResultDTO findEduCurrentResultList(EduCurrent eduCurrent, HttpServletRequest request, Authentication auth) {
        resultDTO  = new ResultDTO();
        List<ManagementPlanDTO> resultList = eduCurrentDAO.findEduResultByUserId(eduCurrent.getQms_user_id());

        if(!DataLib.isEmpty(resultList)) {
            resultDTO.setState(true);
            resultDTO.setCode(200);
            resultDTO.setMessage(messageSource.getMessage("api.message.200", new String[]{"진행현황"}, Locale.KOREA));
            resultDTO.setResult(resultList);
        } else {
            resultDTO.setState(false);
            resultDTO.setCode(400);
            resultDTO.setMessage(messageSource.getMessage("api.message.400", new String[]{"진행현황"}, Locale.KOREA));
            resultDTO.setResult(null);
        }

        return resultDTO;
    }
}
