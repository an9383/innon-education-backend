package com.innon.education.annual.plan.service.impl;

import com.innon.education.admin.system.sign.dao.SignDAO;
import com.innon.education.admin.system.sign.repository.PlanSign;
import com.innon.education.admin.system.sign.repository.PlanSignDetail;
import com.innon.education.admin.system.sign.repository.PlanSignManager;
import com.innon.education.admin.system.sign.service.SignService;
import com.innon.education.annual.plan.dao.AnnualPlanDAO;
import com.innon.education.annual.plan.repository.dto.AnnualPlanDTO;
import com.innon.education.annual.plan.repository.dto.TbAnnualPlanDTO;
import com.innon.education.annual.plan.repository.entity.AnnualPlanEntity;
import com.innon.education.annual.plan.repository.entity.AnnualPlanJoinTable;
import com.innon.education.annual.plan.repository.entity.AnnualPlanMigrateEntity;
import com.innon.education.annual.plan.repository.entity.AnnualPlanSearchEntity;
import com.innon.education.annual.plan.repository.model.AnnualPlan;
import com.innon.education.annual.plan.service.AnnualPlanService;
import com.innon.education.auth.dto.request.LoginRequest;
import com.innon.education.auth.entity.User;
import com.innon.education.auth.repository.UserRepository;
import com.innon.education.common.repository.entity.LogChild;
import com.innon.education.common.repository.entity.LogEntity;
import com.innon.education.common.repository.model.EmailMessage;
import com.innon.education.common.service.CommonService;
import com.innon.education.common.service.EmailService;
import com.innon.education.dao.CodeDAO;
import com.innon.education.controller.dto.ResultDTO;
import com.innon.education.jwt.dto.CustomUserDetails;
import com.innon.education.library.document.dao.DocumentDAO;
import com.innon.education.library.document.repasitory.model.DocumentMemo;
import com.innon.education.management.plan.dao.ManagementPlanDAO;
import com.innon.education.management.plan.repository.entity.ManagementPlanEntity;
import com.innon.education.management.plan.repository.model.ManagementPlan;
import com.innon.education.management.plan.service.ManagementPlanService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Slf4j
@Service
public class AnnualPlanServiceImpl implements AnnualPlanService {
    private ResultDTO resultDTO;

    @Autowired
    AnnualPlanDAO annualPlanDAO;
    @Autowired
    CodeDAO codeDAO;
    @Autowired
    MessageSource messageSource;
    @Autowired
    CommonService commonService;
    @Autowired
    ManagementPlanService managementPlanService;
    @Autowired
    ManagementPlanDAO planDAO;
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
    public ResultDTO saveAnnualPlan(AnnualPlan annualPlan, Authentication auth, HttpServletRequest request) {
        resultDTO = new ResultDTO();
        User user = (User) auth.getPrincipal();
        // 교육계획 등록
        resultDTO = commonService.findCheckUserAuth(annualPlan.getUser(), request, auth);

        if (resultDTO.getCode() == 200) {
            annualPlan.setSys_reg_user_id(user.getUsername());
//            annualPlan.setProgress_type(annualPlan.getSignUserList().get(0).getSign_type());
            int resultId = 0;
            AnnualPlanSearchEntity annualPlanSearch = new AnnualPlanSearchEntity();
            annualPlanSearch.setGroup_id(annualPlan.getGroup_id());
            annualPlanSearch.setEdu_year(annualPlan.getEdu_year());
            annualPlanSearch.setUse_flag('I');
            List<AnnualPlanDTO> anualPlanList = annualPlanDAO.findAnnualPlanList(annualPlanSearch);

            if (anualPlanList.size() > 0) {
                resultDTO.setState(true);
                resultDTO.setCode(200);
                resultDTO.setMessage(annualPlan.getEdu_year() + "년은 이미 등록된 계획이 있습니다.");
                resultDTO.setResult(null);
            } else {
                AnnualPlanEntity saveAnnualPlan = new AnnualPlanEntity(annualPlan);
                saveAnnualPlan.setVersion("1");
                resultId = annualPlanDAO.saveAnnualPlan(saveAnnualPlan);
                DocumentMemo memo = new DocumentMemo();
                memo.setDocument_id(saveAnnualPlan.getId());
                memo.setMemo(annualPlan.getMemo());
                memo.setType("annual");
                memo.setSys_reg_user_id(auth.getName());
                documentDAO.saveDocumentMemo(memo);

                int logId = commonService.saveLog(LogEntity.builder()
                        .type("annual")
                        .table_id(memo.getId())
                        .page_nm("info")
                        .url_addr(request.getRequestURI())
                        .state("insert")
                        .reg_user_id(auth.getName())
                        .build());
                if (logId > 0) {
                    try {

                        Object obj = saveAnnualPlan;
                        for (Field field : obj.getClass().getDeclaredFields()) {
                            field.setAccessible(true);
                            Object value = field.get(obj);
                            if (value != null
                                    && (!"0".equals(value.toString())) && (!"".equals(value.toString().trim()))) {
                                commonService.saveLogDetail(LogChild
                                        .builder()
                                        .log_id(logId)
                                        .field(field.getName())
                                        .new_value(value.toString().trim())
                                        .reg_user_id(auth.getName())
                                        .build());
                            }
                        }
                    } catch (Exception e) {
                        log.info(e.getCause().toString());
                        e.printStackTrace();
                    }
                }
                try {
                    if (resultId > 0) {
//                        PlanSign planSign = new PlanSign();
//                        planSign.setId(annualPlan.getSign_id());
//                        planSign.setPlan_id(saveAnnualPlan.getId());
//                        planSign.setTitle(saveAnnualPlan.getEdu_type()+"-"+planSign.getTitle());
//                        planSign.setStatus(annualPlan.getSignUserList().get(1).getUser_id());
//                        planSign.setSys_reg_user_id(annualPlan.getSys_reg_user_id());
//                        planSign.setDept_cd(user.getDept_cd());
//                        planSign.setSys_upd_reg_date(new Date());
//
//                        int planSignId = signDAO.savePlanSign(planSign);
//
//                        int order_num = 1;
//                        for (PlanSignUser psm : annualPlan.getSignUserList()) {
//                            int singId = psm.getSign_id() == 0 ? annualPlan.getSign_id() : psm.getSign_id();
//                            psm.setSign_id(singId);
//                            psm.setPlan_id(saveAnnualPlan.getId());
//                            psm.setSign_type(psm.getSign_type());
//                            psm.setOrder_num(order_num);
//                            psm.setSign_category("comc11001");
//                            psm.setGroup_id(annualPlan.getGroup_id());
//                            psm.setSys_reg_user_id(auth.getName());
//                            psm.setDept_cd(user.getDept_cd());
//                            psm.setPlan_sign_id(planSign.getId());//2024-12-11 첫상신시 승인일자를 추가
//                            if (order_num == 1) {
//                                psm.setState("coms11005");
//                                psm.setUser_id(auth.getName());
//                            } else if (order_num == 2) {
//                                psm.setSys_upd_reg_date(null);
//                                psm.setState("coms11004");
//                                psm.setComment("");
//
//                                String returnUrl = "https://dip.inno-n.com/lms/mypage";
//
//                                LoginRequest paramUser = new LoginRequest();
//                                paramUser.setUser_id(psm.getUser_id());
//                                CustomUserDetails userInfo = userRepository.findUser(paramUser);
//                                psm.setSignform("comi1s1");
//                                String emailContent = emailService.messageSignInfo(psm, userInfo, "ing");
//
//                                EmailMessage message = EmailMessage.builder()
//                                        .to(userInfo.getEmail())
//                                        .subject(emailService.createSubject(psm, userInfo,"ing"))
//                                        .message(emailContent).url(returnUrl)
//                                        .build();
//                                emailService.sendMail(message, "email");
//
//                            } else {
//                                psm.setSys_upd_reg_date(null);
//                                psm.setState("coms11007");
//                                psm.setComment("");
//                            }
//                            signDAO.savePlanSignUser(psm);
//
//                            if (order_num == 1) {
//                                PlanSignDetail signDetail = new PlanSignDetail();
//                                signDetail.setPlan_sign_id(planSign.getId());
//                                signDetail.setSign_manager_id(psm.getId());
//                                signDetail.setState("coms11005");
//                                signDetail.setUser_id(auth.getName());
//                                signDetail.setSys_reg_user_id(auth.getName());
//                                signDetail.setComment(psm.getComment());
//                                signDAO.savePlanSignDetail(signDetail);
//                            }
//                            order_num++;
//                        }

//                        for (ManagementPlan managementPlan : annualPlan.getManagementPlanList()) {
//                            managementPlan.setParent_id(saveAnnualPlan.getId());
//                            managementPlan.setGroup_id(annualPlan.getGroup_id());
//                            managementPlan.setSys_reg_user_id(auth.getName());
//                            //managementPlan.setProgress_type(annualPlan.getProceed_type());
//                            managementPlan.setStatus(annualPlan.getEdu_type());
//                            managementPlan.setUse_flag(annualPlan.getUse_flag());
//                            int returnPlan = planDAO.savePlan(managementPlan);
//                            if (returnPlan > 0) {
//
//                                DocumentMemo p_memo = new DocumentMemo();
//                                p_memo.setDocument_id(managementPlan.getId());
//                                p_memo.setMemo(annualPlan.getMemo());
//                                p_memo.setType("edu");
//                                p_memo.setSys_reg_user_id(auth.getName());
//                                documentDAO.saveDocumentMemo(p_memo);
//
//                                int p_logId = commonService.saveLog(LogEntity.builder()
//                                        .type("edu")
//                                        .table_id(p_memo.getId())
//                                        .page_nm("education")
//                                        .url_addr(request.getRequestURI())
//                                        .state("insert")
//                                        .reg_user_id(auth.getName())
//                                        .build());
//
//                                if (logId > 0) {
//                                    try {
//                                        ManagementPlanEntity logEntity = new ManagementPlanEntity(managementPlan);
//                                        Object obj = logEntity;
//                                        for (Field field : obj.getClass().getDeclaredFields()) {
//                                            field.setAccessible(true);
//                                            Object value = field.get(obj);
//                                            if (value != null
//                                                    && (!"0".equals(value.toString()))&&(!"".equals(value.toString().trim()))) {
//                                                commonService.saveLogDetail(LogChild
//                                                        .builder()
//                                                        .log_id(p_logId)
//                                                        .field(field.getName())
//                                                        .new_value(value.toString().trim())
//                                                        .reg_user_id(auth.getName())
//                                                        .build());
//                                            }
//                                        }
//                                    } catch (Exception e) {
//                                        log.info(e.getCause().toString());
//                                        e.printStackTrace();
//                                    }
//                                }
//
//
//                                AnnualPlanJoinTable annualPlanJoinTable = new AnnualPlanJoinTable();
//                                annualPlanJoinTable.setAnnual_edu_id(saveAnnualPlan.getId());
//                                annualPlanJoinTable.setPlan_id(managementPlan.getId());
//                                annualPlanJoinTable.setSys_reg_user_id(user.getUsername());
//                                annualPlanDAO.saveAnnualPlanJoin(annualPlanJoinTable);
//                            }
//                        }
                        for (AnnualPlan deptPlan : annualPlan.getDeptPlanList()) {
                            deptPlan.setGroup_id(annualPlan.getGroup_id());
                            deptPlan.setDept_cd(deptPlan.getDept_cd());
                            deptPlan.setUse_flag(annualPlan.getUse_flag());
                            deptPlan.setEdu_year(annualPlan.getEdu_year());
                            deptPlan.setGroup_id(annualPlan.getGroup_id());
                            deptPlan.setTitle("[" + deptPlan.getDept_nm() + "] 부서 연간 계획");
                            deptPlan.setEdu_type("edus11005");
                            AnnualPlanEntity saveDeptAnnual = new AnnualPlanEntity(deptPlan);
                            saveDeptAnnual.setVersion("1");
                            int returnPlan = annualPlanDAO.saveAnnualPlan(saveDeptAnnual);

                            DocumentMemo d_memo = new DocumentMemo();
                            d_memo.setDocument_id(saveDeptAnnual.getId());
                            d_memo.setMemo(annualPlan.getMemo());
                            d_memo.setType("annual");
                            d_memo.setSys_reg_user_id(auth.getName());
                            documentDAO.saveDocumentMemo(d_memo);

                            int d_logId = commonService.saveLog(LogEntity.builder()
                                    .type("annual")
                                    .table_id(d_memo.getId())
                                    .page_nm("dept")
                                    .url_addr(request.getRequestURI())
                                    .state("insert")
                                    .reg_user_id(auth.getName())
                                    .build());
                            if (d_logId > 0) {
                                try {

                                    Object obj = saveDeptAnnual;
                                    for (Field field : obj.getClass().getDeclaredFields()) {
                                        field.setAccessible(true);
                                        Object value = field.get(obj);
                                        if (value != null
                                                && (!"0".equals(value.toString())) && (!"".equals(value.toString().trim()))) {
                                            commonService.saveLogDetail(LogChild
                                                    .builder()
                                                    .log_id(d_logId)
                                                    .field(field.getName())
                                                    .new_value(value.toString().trim())
                                                    .reg_user_id(auth.getName())
                                                    .build());
                                        }
                                    }
                                } catch (Exception e) {
                                    log.info(e.getCause().toString());
                                    e.printStackTrace();
                                }
                            }

//                                if (returnPlan > 0) {
//                                    AnnualPlanJoinTable annualPlanJoinTable = new AnnualPlanJoinTable();
//                                    annualPlanJoinTable.setAnnual_edu_id(saveAnnualPlan.getId());
//                                    annualPlanJoinTable.setPlan_id(deptPlan.getId());
//                                    annualPlanJoinTable.setSys_reg_user_id(user.getUsername());
//                                    annualPlanDAO.saveAnnualPlanJoin(annualPlanJoinTable);
//                                }
                        }


                    } else {
                        resultDTO.setState(false);
                        resultDTO.setCode(400);
                        resultDTO.setMessage(messageSource.getMessage("api.message.400", new String[]{"연간교육"}, Locale.KOREA));
                        resultDTO.setResult(null);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    log.info(e.getCause().toString());
                    resultDTO.setState(false);
                    resultDTO.setCode(400);
                    resultDTO.setMessage(messageSource.getMessage("api.message.400", new String[]{"연간공통교육"}, Locale.KOREA));
                    resultDTO.setResult(null);
                }
                resultDTO.setState(true);
                resultDTO.setCode(201);
                resultDTO.setMessage(messageSource.getMessage("api.message.201", new String[]{"연간공통교육"}, Locale.KOREA));
                resultDTO.setResult(null);
            }
        } else {
            resultDTO.setState(false);
            resultDTO.setCode(400);
        }


        return resultDTO;
    }

    @Transactional
    @Override
    public ResultDTO updateAnnualPlan(AnnualPlan annualPlan, Authentication auth, HttpServletRequest request) {
        resultDTO = new ResultDTO();
        User user = (User) auth.getPrincipal();
        // 교육계획 등록
        resultDTO = commonService.findCheckUserAuth(annualPlan.getUser(), request, auth);

        if (resultDTO.getCode() == 200) {
            annualPlan.setSys_reg_user_id(user.getUsername());
            annualPlan.setProgress_type(annualPlan.getSignUserList().get(0).getSign_type());
            int resultId = 0;
            annualPlan.setUse_flag('W');
            AnnualPlanEntity updateAnnual = new AnnualPlanEntity(annualPlan);
            resultId = annualPlanDAO.updateAnnualPlan(updateAnnual);

            DocumentMemo memo = new DocumentMemo();
            memo.setDocument_id(updateAnnual.getId());
            memo.setMemo(annualPlan.getMemo());
            memo.setType("annual");
            memo.setSys_reg_user_id(auth.getName());
            documentDAO.saveDocumentMemo(memo);

            int logId = commonService.saveLog(LogEntity.builder()
                    .type("annual")
                    .table_id(memo.getId())
                    .page_nm("edus11001".equals(annualPlan.getEdu_type()) ? "info" : "dept")
                    .url_addr(request.getRequestURI())
                    .state("update")
                    .reg_user_id(auth.getName())
                    .build());

            if (logId > 0) {
                try {
                    AnnualPlanEntity beforeEntity = new AnnualPlanEntity(annualPlan.getBe_annual_plan());
                    Object obj = updateAnnual;
                    Object obj2 = beforeEntity;
                    for (Field field : obj.getClass().getDeclaredFields()) {
                        field.setAccessible(true);
                        Object value = field.get(obj);
                        Object value2 = field.get(obj2);
                        if (value != null
                                && (value != null && !"0".equals(value.toString())) && (!"".equals(value.toString().trim()))
                                && (value2 != null && !"0".equals(value2.toString())) && (!"".equals(value2.toString().trim()))
                                && !value.equals(value2)
                        ) {
                            commonService.saveLogDetail(LogChild
                                    .builder()
                                    .log_id(logId)
                                    .field(field.getName())
                                    .new_value(value.toString().trim())
                                    .before_value(value2.toString().trim())
                                    .reg_user_id(auth.getName())
                                    .build());
                        }
                    }
                } catch (Exception e) {
                    log.info(e.getCause().toString());
                    e.printStackTrace();
                }
            }
            try {
                if (resultId > 0) {
                    String signForm = "edus11001".equals(updateAnnual.getEdu_type()) ? "comi1s1" : "comi1s8";
                    PlanSign paramSign = new PlanSign();
                    paramSign.setSignform(signForm);
                    paramSign.setPlan_id(updateAnnual.getId());
                    PlanSign resultPlanSign = signDAO.findPlanSign(paramSign);
                    if (resultPlanSign != null) {
                        PlanSignManager planSignManager = new PlanSignManager();
                        planSignManager.setPlan_id(updateAnnual.getId());
                        planSignManager.setSignform(signForm);
                        planSignManager.setState(annualPlan.getSignUserList().get(0).getState());
                        planSignManager.setUser(annualPlan.getUser());
                        planSignManager.setComment(annualPlan.getMemo());
                        planSignManager.setOrder_num(1);
                        planSignManager.setId(resultPlanSign.getId());
                        signService.updatePlanSign(planSignManager, request, auth);
                    } else {


                        PlanSign planSign = new PlanSign();
                        planSign.setId(annualPlan.getSign_id());
                        planSign.setPlan_id(updateAnnual.getId());

                        planSign.setStatus(annualPlan.getSignUserList().get(1).getUser_id());
                        planSign.setSys_reg_user_id(annualPlan.getSys_reg_user_id());
                        planSign.setDept_cd(user.getDept_cd());
                        planSign.setSys_upd_reg_date(new Date());

                        int planSignId = signDAO.savePlanSign(planSign);

                        int order_num = 1;
                        for (PlanSignManager planSignManager : annualPlan.getSignUserList()) {
                            int singId = planSignManager.getSign_id() == 0 ? annualPlan.getSign_id() : planSignManager.getSign_id();
                            planSignManager.setSign_id(singId);
                            planSignManager.setPlan_id(updateAnnual.getId());
                            planSignManager.setSign_type(planSignManager.getSign_type());
                            planSignManager.setOrder_num(order_num);
                            planSignManager.setSign_category("comc11001");
                            planSignManager.setGroup_id(annualPlan.getGroup_id());
                            planSignManager.setSys_reg_user_id(auth.getName());
                            planSignManager.setDept_cd(user.getDept_cd());
                            planSignManager.setPlan_sign_id(planSign.getId());//2024-12-11 첫상신시 승인일자를 추가
                            if (order_num == 1) {
                                planSignManager.setState("coms11005");
                                planSignManager.setUser_id(auth.getName());
                            } else if (order_num == 2) {
                                planSignManager.setSys_upd_reg_date(null);
                                planSignManager.setState("coms11004");
                                planSignManager.setComment("");

                                String returnUrl = "https://dip.inno-n.com/lms/mypage";
                                planSignManager.setSignform(planSign.getSignform());
                                emailService.sendMail(planSignManager.getUser_id(), planSignManager, "ing", returnUrl);

                            } else {
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
                    }
                    /*기존 데이터 삭제  N => 아무 동작 없음, W = > 결재 진행중 , S = > 결재 완료 , E 완료 */
                    AnnualPlanSearchEntity annualPlanSearch = new AnnualPlanSearchEntity();
                    annualPlanSearch.setId(updateAnnual.getId());
                    annualPlanSearch.setDelete_at('N');
                    List<AnnualPlanDTO> annualPlanDTOList = annualPlanDAO.findAnnualPlanList(annualPlanSearch);

                    for (AnnualPlanDTO annualPlanDTO : annualPlanDTOList) { //전체 삭제다시 등록
                        ManagementPlan updateEducation = new ManagementPlan();
                        updateEducation.setId(annualPlanDTO.getPlan_id());
                        updateEducation.setDelete_at('Y');
                        updateEducation.setUse_flag('C');
                        planDAO.updatePlan(updateEducation);
                    }
                    AnnualPlanJoinTable delPlan = new AnnualPlanJoinTable();
                    delPlan.setAnnual_edu_id(updateAnnual.getId());
                    delPlan.setDelete_at('Y');

                    int delResult = annualPlanDAO.updateAnnualPlanJoin(delPlan);


                    /*기존 삭제조치 end    */

                    for (ManagementPlan managementPlan : annualPlan.getManagementPlanList()) {
                        managementPlan.setParent_id(updateAnnual.getId());
                        managementPlan.setGroup_id(annualPlan.getGroup_id());
                        managementPlan.setSys_reg_user_id(auth.getName());
                        //managementPlan.setProgress_type(annualPlan.getProceed_type());
                        managementPlan.setStatus(annualPlan.getEdu_type());
                        managementPlan.setUse_flag(annualPlan.getUse_flag());
                        int returnPlan = planDAO.savePlan(managementPlan);

                        DocumentMemo p_memo = new DocumentMemo();
                        p_memo.setDocument_id(managementPlan.getId());
                        p_memo.setMemo(annualPlan.getMemo());
                        p_memo.setType("edu");
                        p_memo.setSys_reg_user_id(auth.getName());
                        documentDAO.saveDocumentMemo(p_memo);

                        int p_logId = commonService.saveLog(LogEntity.builder()
                                .type("edu")
                                .table_id(p_memo.getId())
                                .page_nm("education")
                                .url_addr(request.getRequestURI())
                                .state("insert")
                                .reg_user_id(auth.getName())
                                .build());

                        if (logId > 0) {
                            try {
                                ManagementPlanEntity logEntity = new ManagementPlanEntity(managementPlan);
                                Object obj = logEntity;
                                for (Field field : obj.getClass().getDeclaredFields()) {
                                    field.setAccessible(true);
                                    Object value = field.get(obj);
                                    if (value != null
                                            && (!"0".equals(value.toString())) && (!"".equals(value.toString().trim()))) {
                                        commonService.saveLogDetail(LogChild
                                                .builder()
                                                .log_id(p_logId)
                                                .field(field.getName())
                                                .new_value(value.toString().trim())
                                                .reg_user_id(auth.getName())
                                                .build());
                                    }
                                }
                            } catch (Exception e) {
                                log.info(e.getCause().toString());
                                e.printStackTrace();
                            }
                        }

                        if (returnPlan > 0) {
                            AnnualPlanJoinTable annualPlanJoinTable = new AnnualPlanJoinTable();
                            annualPlanJoinTable.setAnnual_edu_id(updateAnnual.getId());
                            annualPlanJoinTable.setPlan_id(managementPlan.getId());
                            annualPlanJoinTable.setSys_reg_user_id(user.getUsername());
                            annualPlanDAO.saveAnnualPlanJoin(annualPlanJoinTable);
                        }
                    }
                    // for (AnnualPlan deptPlan : annualPlan.getDeptPlanList()) {

//                        deptPlan.setGroup_id(annualPlan.getGroup_id());
//                        deptPlan.setDept_cd(deptPlan.getDept_cd());
//                        deptPlan.setUse_flag(annualPlan.getUse_flag());
//                        deptPlan.setTitle("[" + deptPlan.getDept_nm() + "]교육 임시저장");
//                        deptPlan.setEdu_type("edus11005");
//                        AnnualPlanEntity saveDeptAnnual = new AnnualPlanEntity(deptPlan);
//                        int returnPlan = annualPlanDAO.saveAnnualPlan(saveDeptAnnual);
//                        if (returnPlan > 0) {
//                            AnnualPlanJoinTable annualPlanJoinTable = new AnnualPlanJoinTable();
//                            annualPlanJoinTable.setAnnual_edu_id(updateAnnual.getId());
//                            annualPlanJoinTable.setPlan_id(deptPlan.getId());
//                            annualPlanJoinTable.setSys_reg_user_id(user.getUsername());
//                            annualPlanDAO.saveAnnualPlanJoin(annualPlanJoinTable);
//                        }
                    //   }


                } else {
                    resultDTO.setState(false);
                    resultDTO.setCode(400);
                    resultDTO.setMessage(messageSource.getMessage("api.message.400", new String[]{"연간교육", "수정"}, Locale.KOREA));
                    resultDTO.setResult(null);
                }
            } catch (Exception e) {
                e.printStackTrace();
                log.info(e.getCause().toString());
                resultDTO.setState(false);
                resultDTO.setCode(400);
                resultDTO.setMessage(messageSource.getMessage("api.message.400", new String[]{"연간교육", "수정"}, Locale.KOREA));
                resultDTO.setResult(null);
            }
            resultDTO.setState(true);
            resultDTO.setCode(202);
            resultDTO.setMessage(messageSource.getMessage("api.message.202", new String[]{"연간교육", "수정"}, Locale.KOREA));
            resultDTO.setResult(null);
        } else {
            resultDTO.setState(false);
            resultDTO.setCode(400);
        }
        return resultDTO;
    }

    @Override
    public ResultDTO findAnnualPlanView(AnnualPlanSearchEntity plan) {
        ResultDTO resultDTO = new ResultDTO();

        try {
            AnnualPlanDTO resultPlan = annualPlanDAO.findAnnualPlan(plan);
            resultDTO.setResult(resultPlan);
            resultDTO.setCode(200);
            resultDTO.setMessage(messageSource.getMessage("api.message.202", new String[]{"연간 교육", "조회"}, Locale.KOREA));

        } catch (Exception e) {
            log.info(e.getCause().toString());
            resultDTO.setState(false);
            resultDTO.setCode(400);
            resultDTO.setMessage(messageSource.getMessage("api.message.400", new String[]{"연간교육"}, Locale.KOREA));
            resultDTO.setResult(null);
        }

        return resultDTO;
    }

    @Override
    public ResultDTO annualEducationPlanList(AnnualPlanSearchEntity plan) {
        resultDTO = new ResultDTO();

        List<AnnualPlanDTO> resultList = annualPlanDAO.annualEducationPlanList(plan);

//        for (AnnualPlanDTO annualPlanDTO : resultList) {
//            ManagementPlanEntity paramEntity = new ManagementPlanEntity();
//            paramEntity.setParent_id(annualPlanDTO.getId());
//            annualPlanDTO.setChildren(planDAO.educationPlanList(paramEntity));
//        }


        if (!ObjectUtils.isEmpty(resultList)) {

            resultDTO.setCode(200);
            resultDTO.setMessage(messageSource.getMessage("api.message.202", new String[]{"연간 교육", "조회"}, Locale.KOREA));
            resultDTO.setResult(resultList);
        } else {
            resultDTO.setState(false);
            resultDTO.setState(false);
            resultDTO.setCode(400);
            resultDTO.setMessage(messageSource.getMessage("api.message.400", new String[]{"연간교육"}, Locale.KOREA));
            resultDTO.setResult(null);
        }

        return resultDTO;
    }

//    @Override
    public ResultDTO revisionAnnualPlan_old(AnnualPlan annualPlan, Authentication auth, HttpServletRequest request) {
        resultDTO = new ResultDTO();
        User user = (User) auth.getPrincipal();
        // 교육계획 등록
        resultDTO = commonService.findCheckUserAuth(annualPlan.getUser(), request, auth);

        if (resultDTO.getCode() == 200) {

            AnnualPlanSearchEntity annualPlanSearch = new AnnualPlanSearchEntity();
            annualPlanSearch.setGroup_id(annualPlan.getGroup_id());
            annualPlanSearch.setEdu_year(annualPlan.getEdu_year());
            annualPlanSearch.setUse_flag('V');
            List<AnnualPlanDTO> anualPlanList = annualPlanDAO.findAnnualPlanList(annualPlanSearch);

            if (anualPlanList.size() > 0) {
                resultDTO.setState(true);
                resultDTO.setCode(200);
                resultDTO.setMessage(annualPlan.getEdu_year() + "년은 이미 신청된 개정 내역이 있습니다.");
                resultDTO.setResult(null);
            } else {


                annualPlan.setSys_reg_user_id(user.getUsername());
                annualPlan.setProgress_type(annualPlan.getSignUserList().get(0).getSign_type());
                int resultId = 0;

                AnnualPlanEntity saveAnnualPlan = new AnnualPlanEntity(annualPlan);
                resultId = annualPlanDAO.saveAnnualPlan(saveAnnualPlan);


                DocumentMemo memo = new DocumentMemo();
                memo.setDocument_id(saveAnnualPlan.getId());
                memo.setMemo(annualPlan.getMemo());
                memo.setType("annual");
                memo.setSys_reg_user_id(auth.getName());
                documentDAO.saveDocumentMemo(memo);

                int logId = commonService.saveLog(LogEntity.builder()
                        .type("annual")
                        .table_id(memo.getId())
                        .page_nm("annual")
                        .url_addr(request.getRequestURI())
                        .state("revision")
                        .reg_user_id(auth.getName())
                        .build());

                try {
                    if (resultId > 0) {

                        PlanSign planSign = new PlanSign();
                        planSign.setId(annualPlan.getSign_id());
                        planSign.setPlan_id(saveAnnualPlan.getId());

                        planSign.setStatus(annualPlan.getSignUserList().get(1).getUser_id());
                        planSign.setSys_reg_user_id(annualPlan.getSys_reg_user_id());
                        planSign.setDept_cd(user.getDept_cd());
                        planSign.setSys_upd_reg_date(new Date());

                        int planSignId = signDAO.savePlanSign(planSign);

                        int order_num = 1;
                        for (PlanSignManager psm : annualPlan.getSignUserList()) {
                            int singId = psm.getSign_id() == 0 ? annualPlan.getSign_id() : psm.getSign_id();
                            psm.setSign_id(singId);
                            psm.setPlan_id(saveAnnualPlan.getId());
                            psm.setSign_type(psm.getSign_type());
                            psm.setOrder_num(order_num);
                            psm.setSign_category("comc11001");
                            psm.setGroup_id(annualPlan.getGroup_id());
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
							    PlanSignDetail signDetail = new PlanSignDetail();
							    signDetail.setPlan_sign_id(planSign.getId());
							    signDetail.setSign_manager_id(psm.getId());
							    signDetail.setState("coms11005");
							    signDetail.setUser_id(auth.getName());
							    signDetail.setSys_reg_user_id(auth.getName());
							    signDetail.setComment(psm.getComment());
							    signDAO.savePlanSignDetail(signDetail);
							}
							*/
                            order_num++;
                        }

                        /*기존 데이터 삭제  N => 아무 동작 없음, W = > 결재 진행중 , S = > 결재 완료 , E 완료 */
                        annualPlanSearch = new AnnualPlanSearchEntity();
                        annualPlanSearch.setId(saveAnnualPlan.getId());
                        List<AnnualPlanDTO> annualPlanDTOList = annualPlanDAO.findAnnualPlanList(annualPlanSearch);

                        for (AnnualPlanDTO annualPlanDTO : annualPlanDTOList) { //전체 삭제다시 등록
                            ManagementPlan updateEducation = new ManagementPlan();
                            updateEducation.setId(annualPlanDTO.getPlan_id());
                            updateEducation.setDelete_at('Y');
                            updateEducation.setUse_flag('C');
                            planDAO.updatePlan(updateEducation);
                        }
                        AnnualPlanJoinTable delPlan = new AnnualPlanJoinTable();
                        delPlan.setAnnual_edu_id(saveAnnualPlan.getId());
                        delPlan.setDelete_at('Y');
                        int delResult = annualPlanDAO.updateAnnualPlanJoin(delPlan);


                        /*기존 삭제조치 end    */


                        for (ManagementPlan managementPlan : annualPlan.getManagementPlanList()) {
                            managementPlan.setParent_id(saveAnnualPlan.getId());
                            managementPlan.setGroup_id(annualPlan.getGroup_id());
                            managementPlan.setSys_reg_user_id(auth.getName());
                            //managementPlan.setProgress_type(annualPlan.getProceed_type());
                            managementPlan.setStatus(annualPlan.getEdu_type());
                            managementPlan.setUse_flag(annualPlan.getUse_flag());
                            managementPlan.setProgress_type("");
                            int returnPlan = planDAO.savePlan(managementPlan);
                            if (returnPlan > 0) {
                                AnnualPlanJoinTable annualPlanJoinTable = new AnnualPlanJoinTable();
                                annualPlanJoinTable.setAnnual_edu_id(saveAnnualPlan.getId());
                                annualPlanJoinTable.setPlan_id(managementPlan.getId());
                                annualPlanJoinTable.setSys_reg_user_id(user.getUsername());
                                annualPlanDAO.saveAnnualPlanJoin(annualPlanJoinTable);
                            }
                        }
                        for (AnnualPlan deptPlan : annualPlan.getDeptPlanList()) {

                            deptPlan.setGroup_id(annualPlan.getGroup_id());
                            deptPlan.setDept_cd(deptPlan.getDept_cd());
                            deptPlan.setUse_flag(annualPlan.getUse_flag());
                            deptPlan.setTitle("[" + deptPlan.getDept_nm() + "]교육 임시저장");
                            deptPlan.setEdu_type("edus11005");
                            deptPlan.setProgress_type("");
                            AnnualPlanEntity saveAnnul = new AnnualPlanEntity(deptPlan);
                            int returnPlan = annualPlanDAO.saveAnnualPlan(saveAnnul);
//                            if (returnPlan > 0) {
//                                AnnualPlanJoinTable annualPlanJoinTable = new AnnualPlanJoinTable();
//                                annualPlanJoinTable.setAnnual_edu_id(updateAnnual.getId());
//                                annualPlanJoinTable.setPlan_id(deptPlan.getId());
//                                annualPlanJoinTable.setSys_reg_user_id(user.getUsername());
//                                annualPlanDAO.saveAnnualPlanJoin(annualPlanJoinTable);
//                            }
                        }

                    } else {
                        resultDTO.setState(false);
                        resultDTO.setCode(400);
                        resultDTO.setMessage(messageSource.getMessage("api.message.400", new String[]{"연간교육"}, Locale.KOREA));
                        resultDTO.setResult(null);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    log.info(e.getCause().toString());
                    resultDTO.setState(false);
                    resultDTO.setCode(400);
                    resultDTO.setMessage(messageSource.getMessage("api.message.400", new String[]{"연간교육"}, Locale.KOREA));
                    resultDTO.setResult(null);
                }
                resultDTO.setState(true);
                resultDTO.setCode(202);
                resultDTO.setMessage(messageSource.getMessage("api.message.202", new String[]{"연간교육"}, Locale.KOREA));
                resultDTO.setResult(null);

            }


        } else {
            resultDTO.setState(false);
            resultDTO.setCode(400);
        }
        return resultDTO;
    }

    @Override
    public ResultDTO annualList(AnnualPlanSearchEntity plan) {
        resultDTO = new ResultDTO();

        List<AnnualPlanDTO> resultList = annualPlanDAO.annualList(plan);

//        for (AnnualPlanDTO annualPlanDTO : resultList) {
//            ManagementPlanEntity paramEntity = new ManagementPlanEntity();
//            paramEntity.setParent_id(annualPlanDTO.getId());
//            annualPlanDTO.setChildren(planDAO.educationPlanList(paramEntity));
//        }


        if (!ObjectUtils.isEmpty(resultList)) {
            resultDTO.setState(true);
            resultDTO.setResult(resultList);
            resultDTO.setCode(200);
        } else {
            resultDTO.setState(false);
            resultDTO.setCode(400);
        }

        return resultDTO;
    }

    @Override
    public ResultDTO examineAnnualPlan(AnnualPlan annualPlan, Authentication auth, HttpServletRequest request) {
        resultDTO = new ResultDTO();

        //  SignDTO sign =signDAO.findSignById(annualPlan.getSign_id());
        //   result.setProgress_type(sign.getSignform());

        try {

            // 교육결과 정보 등록
//            ManagementResultEntity resultEntity = new ManagementResultEntity(annualPlan);
//            int resultCnt = resultDAO.updateEducationResult(resultEntity);
//            User user = (User) auth.getPrincipal();
//            if (resultCnt > 0) {
//                ManagementPlan param = new ManagementPlan();
//                param.setId(result.getPlan_id());
//                ManagementPlanDTO managementPlan = planDAO.educationPlanDetail(param);
//
//                param.setUse_flag(result.getUse_flag());
//                param.setProgress_type(result.getSignUserList().get(0).getSign_type());
//                planDAO.updatePlan(param);
//                String signForm = "comi1s5";
//                PlanSign paramSign = new PlanSign();
//                paramSign.setSignform(signForm);
//                paramSign.setPlan_id(result.getPlan_id());

//
            annualPlan.getPlanSignUser().setUser(annualPlan.getUser());
            annualPlan.getPlanSignUser().setComment(annualPlan.getMemo());
            signService.updatePlanSign(annualPlan.getPlanSignUser(), request, auth);
//            }
//
            DocumentMemo memo = new DocumentMemo();
            memo.setDocument_id(annualPlan.getId());
            memo.setMemo(annualPlan.getMemo());
            memo.setType("annual");
            memo.setSys_reg_user_id(auth.getName());
            documentDAO.saveDocumentMemo(memo);

            int logId = commonService.saveLog(LogEntity.builder()
                    .type("annual")
                    .table_id(memo.getId())
                    .page_nm("sign")
                    .url_addr(request.getRequestURI())
                    .state("update")
                    .reg_user_id(auth.getName())
                    .build());

            resultDTO.setState(true);
            resultDTO.setCode(202);
            resultDTO.setMessage(messageSource.getMessage("api.message.202", new String[]{"연간계획 승인"}, Locale.KOREA));

        } catch (Exception e) {
            log.info(e.getCause().toString());
            e.printStackTrace();
            resultDTO.setState(false);
            resultDTO.setCode(400);
            resultDTO.setMessage(messageSource.getMessage("api.message.400", new String[]{"연간계획 승인"}, Locale.KOREA));
            resultDTO.setResult(null);
        }

        return resultDTO;
    }


    @Override
    public ResultDTO saveAnnualPlanJoin(AnnualPlanJoinTable annualPlanJoinTable) {
        return null;
    }

    @Override
    public ResultDTO updateAnnualPlanJoin(AnnualPlanJoinTable annualPlanJoinTable) {
        return null;
    }


    @Override
    public ResultDTO annualPlanList(AnnualPlanSearchEntity plan) {
        resultDTO = new ResultDTO();
        int listSize = annualPlanDAO.findAnnualPlanListCnt(plan);
        if(listSize>0) {
            int version = annualPlanDAO.findAnnualEducationMaxVersion(plan);
            plan.setVersion(version);
        } else {
            plan.setVersion(1);
        }


        List<AnnualPlanDTO> resultList = annualPlanDAO.findAnnualPlanList(plan);

//        for (AnnualPlanDTO annualPlanDTO : resultList) {
//            ManagementPlanEntity paramEntity = new ManagementPlanEntity();
//            paramEntity.setParent_id(annualPlanDTO.getId());
//            annualPlanDTO.setChildren(planDAO.educationPlanList(paramEntity));
//        }


        if (!ObjectUtils.isEmpty(resultList)) {
            resultDTO.setState(true);
            resultDTO.setResult(resultList);
            resultDTO.setMessage("total Size : " + listSize);
        } else {
            resultDTO.setState(false);
        }

        return resultDTO;
    }


    @Override
    public ResultDTO migrateAnnualPlan(AnnualPlanSearchEntity plan) {
        resultDTO = new ResultDTO();
        AnnualPlanDTO migDto = annualPlanDAO.findAnnualPlan(plan);
        AnnualPlanMigrateEntity migEntity = new AnnualPlanMigrateEntity(migDto);
        int saveNum = annualPlanDAO.migrateAnnualPlan(migEntity);
        if (saveNum > 0) {
            resultDTO.setMessage("이관 작업이 성공하였습니다.");
        } else {
            resultDTO.setMessage("이관 작업이 실패하였습니다.");
        }

        return resultDTO;
    }

    @Transactional
    @Override
    public ResultDTO saveDeptAnnualPlan(AnnualPlan annualPlan, Authentication auth, HttpServletRequest request) {
        resultDTO = new ResultDTO();

        User user = (User) auth.getPrincipal();

        resultDTO = commonService.findCheckUserAuth(annualPlan.getUser(), request, auth);

        if (resultDTO.getCode() == 200) {
            for (ManagementPlan managementPlan : annualPlan.getManagementPlanList()) {
                managementPlan.setParent_id(annualPlan.getId());
                managementPlan.setGroup_id(annualPlan.getGroup_id());
                managementPlan.setSys_reg_user_id(auth.getName());
                //managementPlan.setProgress_type(annualPlan.getProceed_type());
                managementPlan.setStatus(annualPlan.getEdu_type());
                managementPlan.setUse_flag(annualPlan.getUse_flag());
                managementPlan.setEdu_type(managementPlan.getPlan_edu_type());
                int returnPlan = 0;
                if(managementPlan.getId() != 0) {
                    ManagementPlanEntity managementPlanEntity = new ManagementPlanEntity(managementPlan);
                    managementPlanEntity.setId(managementPlan.getPlan_id());
                    returnPlan = planDAO.updateEducationPlan(managementPlanEntity);
                } else {
                    returnPlan = planDAO.savePlan(managementPlan);

                    AnnualPlanJoinTable annualPlanJoinTable = new AnnualPlanJoinTable();
                    annualPlanJoinTable.setAnnual_edu_id(annualPlan.getId());
                    annualPlanJoinTable.setPlan_id(managementPlan.getId());
                    annualPlanJoinTable.setSys_reg_user_id(user.getUsername());
                    annualPlanDAO.saveAnnualPlanJoin(annualPlanJoinTable);
                }

                if (returnPlan > 0) {

                    DocumentMemo p_memo = new DocumentMemo();
                    p_memo.setDocument_id(managementPlan.getId());
                    p_memo.setMemo(annualPlan.getMemo());
                    p_memo.setType("edu");
                    p_memo.setSys_reg_user_id(auth.getName());
                    documentDAO.saveDocumentMemo(p_memo);

                    int p_logId = commonService.saveLog(LogEntity.builder()
                            .type("edu")
                            .table_id(p_memo.getId())
                            .page_nm("education")
                            .url_addr(request.getRequestURI())
                            .state("insert")
                            .reg_user_id(auth.getName())
                            .build());

                    if (p_logId > 0) {
                        try {
                            ManagementPlanEntity logEntity = new ManagementPlanEntity(managementPlan);
                            Object obj = logEntity;
                            for (Field field : obj.getClass().getDeclaredFields()) {
                                field.setAccessible(true);
                                Object value = field.get(obj);
                                if (value != null
                                        && (!"0".equals(value.toString())) && (!"".equals(value.toString().trim()))) {
                                    commonService.saveLogDetail(LogChild
                                            .builder()
                                            .log_id(p_logId)
                                            .field(field.getName())
                                            .new_value(value.toString().trim())
                                            .reg_user_id(auth.getName())
                                            .build());
                                }
                            }
                        } catch (Exception e) {
                            log.info(e.getCause().toString());
                            e.printStackTrace();
                        }
                    }
                }
            }
//            annualPlanDAO.saveAnnualSaveYn(annualPlan.getId());

            resultDTO.setState(true);
            resultDTO.setCode(202);
            resultDTO.setMessage(messageSource.getMessage("api.message.202", new String[]{"완료되었습니다"}, Locale.KOREA));
            resultDTO.setResult(null);
        }

        return resultDTO;
    }

    @Override
    public ResultDTO saveAnnualSign(AnnualPlan annualPlan, Authentication auth, HttpServletRequest request) {

        resultDTO = new ResultDTO();
        User user = (User) auth.getPrincipal();

        resultDTO = commonService.findCheckUserAuth(annualPlan.getUser(), request, auth);
        if (resultDTO.getCode() == 200) {

            AnnualPlanEntity saveAnnualPlan = new AnnualPlanEntity(annualPlan);
            annualPlanDAO.saveAnnualSign(saveAnnualPlan);

            PlanSign planSign = new PlanSign();
            planSign.setId(annualPlan.getSign_id());
            planSign.setPlan_id(annualPlan.getId());
            planSign.setTitle(annualPlan.getEdu_type() + "-" + planSign.getTitle());
            planSign.setStatus(annualPlan.getSignUserList().get(1).getUser_id());
            planSign.setSys_reg_user_id(annualPlan.getSys_reg_user_id());
            planSign.setDept_cd(user.getDept_cd());
            planSign.setSys_upd_reg_date(new Date());

            int planSignId = signDAO.savePlanSign(planSign);
            int order_num = 1;
            for (PlanSignManager planSignManager : annualPlan.getSignUserList()) {
                int singId = planSignManager.getSign_id() == 0 ? annualPlan.getSign_id() : planSignManager.getSign_id();
                planSignManager.setSign_id(singId);
                planSignManager.setPlan_id(annualPlan.getId());
                planSignManager.setSign_type(planSignManager.getSign_type());
                planSignManager.setOrder_num(order_num);
                planSignManager.setSign_category("comc11001");
                planSignManager.setGroup_id(annualPlan.getGroup_id());
                planSignManager.setSys_reg_user_id(auth.getName());
                planSignManager.setDept_cd(user.getDept_cd());
                planSignManager.setPlan_sign_id(planSign.getId());//2024-12-11 첫상신시 승인일자를 추가
                if (order_num == 1) {
                    planSignManager.setState("coms11005");
                    planSignManager.setUser_id(auth.getName());
                } else if (order_num == 2) {
                    planSignManager.setSys_upd_reg_date(null);
                    planSignManager.setState("coms11004");
                    planSignManager.setComment("");

                    String returnUrl = "https://dip.inno-n.com/lms/mypage";
                    planSignManager.setSignform("comi1s1");
                    emailService.sendMail(planSignManager.getUser_id(), planSignManager, "ing", returnUrl);

                } else {
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
            resultDTO.setCode(202);
            resultDTO.setMessage(messageSource.getMessage("api.message.202", new String[]{"상신 완료"}, Locale.KOREA));
            resultDTO.setResult(null);
        } else {
            resultDTO.setState(false);
            resultDTO.setCode(400);
        }


        return resultDTO;
    }

    @Transactional
    @Override
    public ResultDTO revisionAnnualPlan(AnnualPlan annualPlan, Authentication auth, HttpServletRequest request) {
        resultDTO = new ResultDTO();
        User user = (User) auth.getPrincipal();

        resultDTO = commonService.findCheckUserAuth(annualPlan.getUser(), request, auth);

        if (resultDTO.getCode() == 200) {
            annualPlan.setSys_reg_user_id(user.getUsername());

            AnnualPlanEntity searchAnnualEntity = new AnnualPlanEntity(annualPlan);
            List<AnnualPlanDTO> annualEduDTOS = annualPlanDAO.findBeforeVersionAnnualEdu(searchAnnualEntity);
            List<TbAnnualPlanDTO> annualPlanDTOS = annualPlanDAO.findBeforeVersionAnnualPlan(searchAnnualEntity);
            List<ManagementPlan> eduPlanDTOS = annualPlanDAO.findBeforeVersionEducationPlan(searchAnnualEntity);

            for(AnnualPlanDTO annualEdu : annualEduDTOS) {
                // 기존에 있던 버전의 tb_annual_education 을 새로 insert
                AnnualPlanEntity saveAnnualEdu = new AnnualPlanEntity(annualEdu);
                // 버전 증가

                saveAnnualEdu.setSys_reg_user_id(user.getUsername());
                saveAnnualEdu.setVersion(String.valueOf(annualEdu.getVersion()+1));
                annualPlanDAO.saveAnnualPlan(saveAnnualEdu);
                int annualEduId = saveAnnualEdu.getId();

                DocumentMemo memo = new DocumentMemo();
                memo.setDocument_id(annualEduId);
                memo.setMemo(annualPlan.getMemo());
                memo.setType("annual");
                memo.setSys_reg_user_id(auth.getName());
                documentDAO.saveDocumentMemo(memo);

                int logId = commonService.saveLog(LogEntity.builder()
                        .type("annual")
                        .table_id(memo.getId())
                        .page_nm("annual")
                        .url_addr(request.getRequestURI())
                        .state("revision")
                        .reg_user_id(auth.getName())
                        .build());

                for(ManagementPlan edu: eduPlanDTOS) {
                    if(edu.getParent_id() == annualEdu.getOld_id()) {
                        edu.setParent_id(saveAnnualEdu.getId());
                        edu.setSys_reg_user_id(user.getUsername());
                        planDAO.savePlan(edu);

                        for(TbAnnualPlanDTO plan : annualPlanDTOS) {
                            if(plan.getPlan_id() == edu.getOld_id()) {
                                plan.setPlan_id(edu.getId());
                            }
                        }

                    }
                }

                for(TbAnnualPlanDTO plan : annualPlanDTOS) {
                    if(plan.getAnnual_edu_id() == annualEdu.getOld_id()) {
                        plan.setAnnual_edu_id(saveAnnualEdu.getId());
                    }
                }
            }
            for(TbAnnualPlanDTO plan : annualPlanDTOS) {
                AnnualPlanJoinTable annualPlanJoinTable = new AnnualPlanJoinTable();
                annualPlanJoinTable.setAnnual_edu_id(plan.getAnnual_edu_id());
                annualPlanJoinTable.setPlan_id(plan.getPlan_id());
                annualPlanJoinTable.setSys_reg_user_id(user.getUsername());
                annualPlanDAO.saveAnnualPlanJoin(annualPlanJoinTable);
            }

                resultDTO.setState(true);
                resultDTO.setCode(202);
                resultDTO.setMessage(messageSource.getMessage("api.message.202", new String[]{"연간교육"}, Locale.KOREA));
                resultDTO.setResult(null);
        } else {
            resultDTO.setState(false);
            resultDTO.setCode(400);
        }
        return resultDTO;
    }

    @Override
    public ResultDTO editableDept(AnnualPlan annualPlan, Authentication auth, HttpServletRequest request) {
        resultDTO = new ResultDTO();
        User user = (User) auth.getPrincipal();

        resultDTO = commonService.findCheckUserAuth(annualPlan.getUser(), request, auth);

        if (resultDTO.getCode() == 200) {
            AnnualPlanEntity saveAnnualPlan = new AnnualPlanEntity(annualPlan);
            annualPlanDAO.saveAnnualSaveYn(saveAnnualPlan);

            resultDTO.setState(true);
            resultDTO.setCode(202);
            resultDTO.setMessage(messageSource.getMessage("api.message.202", new String[]{"저장"}, Locale.KOREA));
            resultDTO.setResult(null);
        } else {
            resultDTO.setState(false);
            resultDTO.setCode(400);
        }
        return resultDTO;
    }
}
