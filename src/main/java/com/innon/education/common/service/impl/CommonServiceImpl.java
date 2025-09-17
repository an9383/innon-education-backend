package com.innon.education.common.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.innon.education.admin.group.dao.GroupDAO;
import com.innon.education.admin.group.repository.Group;
import com.innon.education.auth.dto.request.LoginRequest;
import com.innon.education.auth.entity.RoleUser;
import com.innon.education.auth.entity.User;
import com.innon.education.auth.repository.UserRepository;
import com.innon.education.common.dao.CommonDAO;
import com.innon.education.common.dao.UserRoleDAO;
import com.innon.education.common.repository.dto.EduDeptDTO;
import com.innon.education.common.repository.dto.EduInsaDTO;
import com.innon.education.common.repository.dto.LogDTO;
import com.innon.education.common.repository.dto.MenuDTO;
import com.innon.education.common.repository.entity.DeptEntity;
import com.innon.education.common.repository.entity.InsaEntity;
import com.innon.education.common.repository.entity.LogChild;
import com.innon.education.common.repository.entity.LogEntity;
import com.innon.education.common.repository.model.Dept;
import com.innon.education.common.repository.model.Email;
import com.innon.education.common.repository.model.EmailMessage;
import com.innon.education.common.repository.model.Insa;
import com.innon.education.common.service.CommonService;
import com.innon.education.common.service.EmailService;
import com.innon.education.common.util.DataLib;
import com.innon.education.controller.dto.ResultDTO;
import com.innon.education.jwt.dto.CustomUserDetails;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class CommonServiceImpl implements CommonService {

    private ResultDTO resultDTO;

    @Autowired
    CommonDAO commonDAO;

    @Autowired
    MessageSource messageSource;

    @Autowired
    UserRoleDAO userRoleDAO;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    GroupDAO groupDAO;

    @Autowired
    EmailService emailService;

    @Override
    public ResultDTO findEduDeptList(Dept dept, Authentication auth) {
        resultDTO = new ResultDTO();
        DeptEntity deptEntity = new DeptEntity(dept);
        User user = (User) auth.getPrincipal();
        List<EduDeptDTO> resultList = commonDAO.findEduDeptList(deptEntity);

        if(!DataLib.isEmpty(resultList)) {
            resultDTO.setState(true);
            resultDTO.setResult(resultList);
        } else {
            resultDTO.setState(false);
        }

        return resultDTO;
    }




    @Override
    public ResultDTO findEduInsaList(Insa insa) {
        resultDTO = new ResultDTO();
        List<EduInsaDTO> resultList = new ArrayList<>();
        int listSize = 0;

        if(insa.getDept_cds() != null) {
            DeptEntity deptEntity = new DeptEntity(insa);
            /*List<EduDeptDTO> childEduDeptCdList = commonDAO.findChildEduDeptCdList(deptEntity);

            InsaEntity insaEntity = new InsaEntity(childEduDeptCdList);
            listSize = commonDAO.findEduInsaListCnt(insaEntity);
            insaEntity.setPage_no(insa.getPage_no());
            insaEntity.setPage_size(insa.getPage_size());
            resultList = commonDAO.findEduInsaList(insaEntity);*/
        } else {

//TODO 주원 UPDATE 필요 parameter insa(x)  ['' , '' ] 또는 "'',''"스트링으로
            InsaEntity insaEntity = new InsaEntity(insa);
            listSize = commonDAO.findEduInsaListCnt(insaEntity);
//            insaEntity.setPage_no(insa.getPage_no());
//            insaEntity.setPage_size(insa.getPage_size());
            resultList = commonDAO.findEduInsaList(insaEntity);

        }

        if(!DataLib.isEmpty(resultList)) {
            resultDTO.setState(true);
            resultDTO.setResult(resultList);
            resultDTO.setMessage("total Size : " + listSize);
        } else {
            resultDTO.setState(false);
        }

        return resultDTO;
    }

    @Override
    public ResultDTO findFactoryCdByUserId(String user_id) {
        resultDTO = new ResultDTO();
        String dept_cd = commonDAO.findFactoryCdByUserId(user_id);
        if(!DataLib.isEmpty(dept_cd)) {
            resultDTO.setState(true);
            resultDTO.setResult(dept_cd);
        } else {
            resultDTO.setState(false);
        }

        return resultDTO;
    }

    @Override
    public ResultDTO findEduDeptUserList(Dept dept, Authentication auth) {
        resultDTO = new ResultDTO();
        User user = (User)auth.getPrincipal();
        boolean chkUserRole = auth.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));

        List<EduDeptDTO> resultList = new ArrayList<>();
        
        if("user_nm".equals(dept.getSearch_type())) {
            InsaEntity insaEntity = new InsaEntity();
            insaEntity.setSearch_txt(dept.getSearch_txt());
            insaEntity.setSearch_type("user_nm");
            insaEntity.setGroup_id(dept.getGroup_id());
            insaEntity.setUse_flag(dept.getUse_flag());
            insaEntity.setDel_flag(dept.getDel_flag());
            insaEntity.setEdu_user_id(dept.getEdu_user_id());
            List<EduInsaDTO> insaList = commonDAO.findEduInsaList(insaEntity);
            
            List<String> deptCds = insaList.stream()
                .map(EduInsaDTO::getDept_cd)
                .distinct()
                .toList();
            
            if(deptCds.size() > 0) {
                DeptEntity deptEntity = new DeptEntity();
                if(!chkUserRole) {
                    deptEntity.setDept_cd(user.getDept_cd());
                }
                deptEntity.setDept_cds(deptCds);
                deptEntity.setUse_flag(dept.getUse_flag());
                deptEntity.setDel_flag(dept.getDel_flag());
                resultList = commonDAO.findEduDeptPopupList(deptEntity);
    
                resultList.forEach(eduDeptDTO -> {
                    List<EduInsaDTO> deptInsaList = insaList.stream()
                    .filter(item -> eduDeptDTO.getDept_cd().equals(item.getDept_cd()))
                    .toList();
    
                    eduDeptDTO.setChildren(deptInsaList);
                });
            }
        } else { // else if("dept_nm".equals(dept.getSearch_type()))
            DeptEntity deptEntity = new DeptEntity();
            if(!chkUserRole) {
                deptEntity.setDept_cd(user.getDept_cd());
            }
            deptEntity.setSearch_txt(dept.getSearch_txt());
            deptEntity.setGroup_id(dept.getGroup_id());
            deptEntity.setUse_flag(dept.getUse_flag());
            deptEntity.setDel_flag(dept.getDel_flag());
            deptEntity.setSearch_type("dept_nm");
            deptEntity.setEdu_user_id(dept.getEdu_user_id());
            resultList = commonDAO.findEduDeptPopupList(deptEntity);

            resultList.forEach(eduDeptDTO -> {
                InsaEntity insaEntity = new InsaEntity();
                insaEntity.setDept_cd(eduDeptDTO.getDept_cd());
                insaEntity.setGroup_id(deptEntity.getGroup_id());
                insaEntity.setEdu_user_id(deptEntity.getEdu_user_id());
                List<EduInsaDTO> deptInsaList = commonDAO.findEduInsaList(insaEntity);

                eduDeptDTO.setChildren(deptInsaList);
            });
        }

        resultDTO.setState(!DataLib.isEmpty(resultList));
        resultDTO.setResult(resultList);

        return resultDTO;
    }

    @Override
    public int saveLog(LogEntity logEntity) {
        int resultId = commonDAO.saveLog(logEntity);
        int logId = 0;
        if(resultId > 0) {
            logId = logEntity.getId();
        }

        return logId;
    }

    @Override
    public ResultDTO saveLogDetail(LogChild logChild) {
        resultDTO = new ResultDTO();
        int state = commonDAO.saveLogDetail(logChild);
        if(state == 1) {
            resultDTO.setState(true);
            resultDTO.setMessage("로그등록이 완료되었습니다.");
        } else {
            resultDTO.setState(false);
            resultDTO.setMessage("로그등록이 실패하였습니다.");
        }

        return resultDTO;
    }

    @Override
    public ResultDTO findLogList(LogEntity logEntity) {
        resultDTO = new ResultDTO();
        List<LogDTO> resultList = commonDAO.findLogList(logEntity);
        if(!DataLib.isEmpty(resultList)) {
            resultDTO.setState(true);
            resultDTO.setResult(resultList);
        } else {
            resultDTO.setState(false);
        }

        return resultDTO;
    }

    @Override
    public ResultDTO updateInsa(Insa insa, HttpServletRequest request, Authentication auth) {
        resultDTO = new ResultDTO();
        String regex = "^(?=.*[0-9])(?=.*[a-zA-Z]).{4,}$";
        boolean pwMatches = Pattern.compile(regex).matcher(insa.getPassword()).find();

        LoginRequest paramUser = new LoginRequest();
        paramUser.setUser_id(insa.getUser_id());
        User dbUser = userRepository.findUser(paramUser);


        if(insa.getPassword().equals(insa.getUser_id())) {
            resultDTO.setState(false);
            resultDTO.setCode(400);
            resultDTO.setMessage("비밀번호는 아이디와 동일할 수 없습니다.");
            resultDTO.setResult(null);
        } else if(insa.getPassword().length() < 4) {
            resultDTO.setState(false);
            resultDTO.setCode(400);
            resultDTO.setMessage("비밀번호는 4자리 이상으로 구성되어야 합니다.");
            resultDTO.setResult(null);
        } else if(!pwMatches) {
            resultDTO.setState(false);
            resultDTO.setCode(400);
            resultDTO.setMessage("비밀번호는 숫자와 영문자를 포함하여 구성되어야 합니다.");
            resultDTO.setResult(null);
        } else if(passwordEncoder.matches(insa.getPassword(), dbUser.getPassword())){
            resultDTO.setState(false);
            resultDTO.setCode(400);
            resultDTO.setMessage("이전 비밀번호와 같은 비밀번호는 사용 할 수 없습니다.");
            resultDTO.setResult(null);
        } else {
            insa.setPassword(passwordEncoder.encode(insa.getPassword()));
            insa.setPw_reset_date(new Date());
            int state = commonDAO.updateInsa(insa);

            if(state == 1) {
                RoleUser saveUserRole = new RoleUser();
                saveUserRole.setUser_id(insa.getUser_id());
                saveUserRole.setRole_id(7);
                userRoleDAO.saveRoleUser(saveUserRole);
                resultDTO.setState(true);
                resultDTO.setCode(200);
                resultDTO.setMessage("비밀번호 변경이 성공하였습니다.");
                resultDTO.setResult(null);
            } else {
                resultDTO.setState(false);
                resultDTO.setCode(400);
                resultDTO.setMessage(messageSource.getMessage("api.message.400", new String[]{"사용자 정보 변경"}, Locale.KOREA));
                resultDTO.setResult(null);
            }
        }
        return resultDTO;
    }

    @Override
    public ResultDTO findCheckUserAuth(LoginRequest user, HttpServletRequest request, Authentication auth) {
        resultDTO = new ResultDTO();
        if(!auth.getName().equals(user.getUser_id())) {
            resultDTO.setState(false);
            resultDTO.setCode(405);
            resultDTO.setMessage("사용자 정보가 일치하지 않습니다.");
            resultDTO.setResult(null);
        } else {
            CustomUserDetails userInfo = userRepository.findUser(user);
            boolean matches = passwordEncoder.matches(user.getPassword(), userInfo.getPassword());
            if(userInfo == null) {
                resultDTO.setState(false);
                resultDTO.setCode(405);
                resultDTO.setMessage("존재하지 않는 계정입니다.");
                resultDTO.setResult(null);
            }else if(!matches) {


                resultDTO.setState(false);
                resultDTO.setCode(405);
                resultDTO.setMessage("비밀번호가 일치하지 않습니다.");
                resultDTO.setResult(null);
            }else if(user.getPassword() == null){

                resultDTO.setState(false);
                resultDTO.setCode(405);
                resultDTO.setMessage("비밀번호 재설정은 로그아웃 이후 비밀번호 찾기를 통해 진행해 주세요.");
                resultDTO.setResult(null);

            } else {
                resultDTO.setState(true);
                resultDTO.setCode(200);
            }
        }

        return resultDTO;
    }

    @Override
    public ResultDTO findFileCheckUserAuth(String user, HttpServletRequest request, Authentication auth) {
        resultDTO = new ResultDTO();
        if(auth.getName().equals(user)) {
            resultDTO.setState(false);
            resultDTO.setCode(405);
            resultDTO.setMessage("사용자 정보가 일치하지 않습니다.");
            resultDTO.setResult(null);
        } else {
            resultDTO.setState(true);
            resultDTO.setCode(200);
        }
        return resultDTO;
    }


    @Override
    public ResultDTO getEduInsaList(Insa insa, HttpServletRequest request, Authentication auth) {
        resultDTO = new ResultDTO();
        User user = (User) auth.getPrincipal();

        InsaEntity insaEntity = new InsaEntity();
        insaEntity.setSearch_txt(insa.getSearch_txt());
        insaEntity.setSearch_type(insa.getSearch_type());
        insaEntity.setUse_flag(insa.getUse_flag());
        insaEntity.setDel_flag(insa.getDel_flag());
        if(insa.getPage() != null) {
            insaEntity.setPage(insa.getPage());
        }

        if(insa.getSearch_txt() == null) {
            insaEntity.setSearch_txt(user.getDept_nm());
        }
        if(insa.getDept_cd() != null && !insa.getDept_cd().equals("")) {
            insaEntity.setDept_cd(insa.getDept_cd());
        }
        List<EduInsaDTO> resultList = commonDAO.findEduInsaList(insaEntity);
        if(insa.getPage() != null) {
            resultDTO.setPage(insa.getPage());
            resultDTO.getPage().setTotalCnt(resultList);
        }

        if(!DataLib.isEmpty(resultList)) {
            resultDTO.setState(true);
            resultDTO.setCode(200);
            resultDTO.setMessage(messageSource.getMessage("api.message.200", new String[]{"사용자"}, Locale.KOREA));
            resultDTO.setResult(resultList);
        } else {
            resultDTO.setState(false);
            resultDTO.setCode(400);
            resultDTO.setMessage(messageSource.getMessage("api.message.400", new String[]{"사용자 조회"}, Locale.KOREA));
            resultDTO.setResult(null);
        }

        return resultDTO;
    }

    @Override
    public ResultDTO getEduPopupInsaList(Insa insa, HttpServletRequest request, Authentication auth) {
        resultDTO = new ResultDTO();
        User user = (User) auth.getPrincipal();
        boolean chkUserRole = auth.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
        System.out.println("chkUserRole" + chkUserRole);
        if(!chkUserRole) {
            insa.setDept_cd(user.getDept_cd());
        }

        List<EduInsaDTO> resultList = commonDAO.findEduPopupInsaList(insa);

        if(!DataLib.isEmpty(resultList)) {
            resultDTO.setState(true);
            resultDTO.setCode(200);
            resultDTO.setMessage(messageSource.getMessage("api.message.200", new String[]{"사용자"}, Locale.KOREA));
            resultDTO.setResult(resultList);
        } else {
            resultDTO.setState(false);
            resultDTO.setCode(400);
            resultDTO.setMessage(messageSource.getMessage("api.message.400", new String[]{"사용자 조회"}, Locale.KOREA));
            resultDTO.setResult(null);
        }

        return resultDTO;
    }

    @Override
    public ResultDTO updateDept(List<Dept> deptList, HttpServletRequest request, Authentication auth) {
        resultDTO = new ResultDTO();
        try {
            for(Dept dept : deptList) {
                try {
                    int state = commonDAO.updateDept(dept);
                    if(state == 1) {
                        Insa insa = new Insa();
                        insa.setDept_cd(dept.getDept_cd());
                        insa.setUse_flag(dept.getUse_flag());
                        try {
                            commonDAO.updateInsa(insa);
                        } catch(Exception e) {
                            resultDTO.setState(false);
                            resultDTO.setCode(400);
                            resultDTO.setMessage(messageSource.getMessage("api.message.400", new String[]{"부서별 사용자 활성화"}, Locale.KOREA));
                            resultDTO.setResult(null);
                        }
                    }
                } catch(Exception e) {
                    resultDTO.setState(false);
                    resultDTO.setCode(400);
                    resultDTO.setMessage(messageSource.getMessage("api.message.400", new String[]{"부서별 사용자 활성화"}, Locale.KOREA));
                    resultDTO.setResult(null);
                }
            }

            resultDTO.setState(true);
            resultDTO.setCode(200);
            resultDTO.setMessage(messageSource.getMessage("api.message.202", new String[]{"부서별 사용자", "활성화"}, Locale.KOREA));
            resultDTO.setResult(null);
        } catch(Exception e) {
            resultDTO.setState(false);
            resultDTO.setCode(400);
            resultDTO.setMessage(messageSource.getMessage("api.message.400", new String[]{"부서별 사용자 활성화"}, Locale.KOREA));
            resultDTO.setResult(null);
        }

        return resultDTO;
    }

    @Override
    public ResultDTO findGroupInfoByDeptCd(HttpServletRequest request, Authentication auth) {
        resultDTO = new ResultDTO();
        User user = (User) auth.getPrincipal();
        boolean chkUserRole = auth.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
        String dept_cd = null;
        if(!chkUserRole) {
            dept_cd = user.getDept_cd();
        }
        List<Group> groupList = commonDAO.findGroupInfoByDeptCd(dept_cd);
        if(!DataLib.isEmpty(groupList)) {
            resultDTO.setState(true);
            resultDTO.setCode(200);
            resultDTO.setMessage(messageSource.getMessage("api.message.200", new String[]{"그룹"}, Locale.KOREA));
            resultDTO.setResult(groupList);
        } else {
            resultDTO.setState(false);
            resultDTO.setCode(400);
            resultDTO.setMessage(messageSource.getMessage("api.message.400", new String[]{"그룹관리"}, Locale.KOREA));
            resultDTO.setResult(null);
        }

        return resultDTO;
    }

    @Override
    public ResultDTO findTopDeptByDeptCd(HttpServletRequest request, Authentication auth) {
        resultDTO = new ResultDTO();
        User user = (User) auth.getPrincipal();

        List<Dept> topDeptList = commonDAO.findTopDeptByDeptCd(user.getDept_cd());
        if(!DataLib.isEmpty(topDeptList)) {
            resultDTO.setState(true);
            resultDTO.setCode(200);
            resultDTO.setMessage(messageSource.getMessage("api.message.200", new String[]{"상위부서"}, Locale.KOREA));
            resultDTO.setResult(topDeptList);
        } else {
            resultDTO.setState(false);
            resultDTO.setCode(400);
            resultDTO.setMessage(messageSource.getMessage("api.message.400", new String[]{"상위부서"}, Locale.KOREA));
            resultDTO.setResult(null);
        }

        return resultDTO;
    }

    @Override
    public ResultDTO sendOtp(LoginRequest login, HttpServletRequest request) {
        resultDTO = new ResultDTO();
        CustomUserDetails user = userRepository.findUser(login);
        if(user != null) {
            String otp = createCode();
            Email email = new Email();
            email.setReceiver(login.getEmail());
            email.setSender("inno.n_sys@inno-n.com");
            Email otpInfo = commonDAO.findEmailOtp(email);

            if(otpInfo != null) {
                while(otp.equals(otpInfo.getOtp())) {
                    otp = createCode();
                }
            }

            String test = request.getRequestURL().toString().replace(request.getRequestURI(), "");
            String loginUrl = "https://dip.inno-n.com/";
            if(login.getPath() != null ){
                loginUrl +=login.getPath();
            }
            String subjectPath = (!DataLib.isEmpty(login.getPath())) ? login.getPath() : "ADMIN";

            EmailMessage message = EmailMessage.builder()
                    .to(login.getEmail())
                    .url(loginUrl)
                    .subject("["+subjectPath+"]"+"인증번호 발송")
                    .message("<td colspan=\"2\" style=\"text-align:center;font-size: 25px;\">"+otp+"</td>")
                    .build();
            try {
                emailService.sendMail(message, "email");
                email.setSuccess_code("success");
                email.setOtp(otp);

                resultDTO.setState(true);
                resultDTO.setCode(200);
                resultDTO.setMessage("인증번호가 발송 되었습니다.");
                resultDTO.setResult(null);
            } catch (Exception e) {
                email.setSuccess_code("fail");
                resultDTO.setState(false);
                resultDTO.setCode(400);
                resultDTO.setMessage("메일 발송이 실패하였습니다.");
                resultDTO.setResult(null);
            }

            commonDAO.saveEmailOtp(email);
        } else {
            resultDTO.setState(false);
            resultDTO.setCode(400);
            resultDTO.setMessage("정보가 일치하지 않습니다.");
            resultDTO.setResult(null);
        }

        return resultDTO;
    }

    @Override
    public ResultDTO certiCheck(LoginRequest login) {
        resultDTO = new ResultDTO();
        CustomUserDetails user = userRepository.findUser(login);

        if(user != null) {
            Email email = new Email();
            email.setReceiver(login.getEmail());
            Email otpInfo = commonDAO.findEmailOtp(email);

            if(otpInfo != null) {
                long diffSec = (new Date().getTime() - otpInfo.getSend_date().getTime()) / 1000;
                if(diffSec > 180) {
                    resultDTO.setState(false);
                    resultDTO.setCode(401);
                    resultDTO.setMessage("인증 시간이 초과하였습니다.");
                    resultDTO.setResult(null);
                } else {
                    if(!login.getOtp().equals(otpInfo.getOtp())) {
                        resultDTO.setState(false);
                        resultDTO.setCode(400);
                        resultDTO.setMessage("정보가 일치하지 않습니다.");
                        resultDTO.setResult(null);
                    } else {
                        resultDTO.setState(true);
                        resultDTO.setCode(200);
                        resultDTO.setMessage("인증이 성공하였습니다.");
                        resultDTO.setResult(null);
                    }
                }
            } else {
                resultDTO.setState(false);
                resultDTO.setCode(400);
                resultDTO.setMessage("정보가 일치하지 않습니다.");
                resultDTO.setResult(null);
            }
        } else {
            resultDTO.setState(false);
            resultDTO.setCode(400);
            resultDTO.setMessage("정보가 일치하지 않습니다.");
            resultDTO.setResult(null);
        }

        return resultDTO;
    }

    @Override
    public ResultDTO changePassword(LoginRequest login) {
        resultDTO = new ResultDTO();
        CustomUserDetails user = userRepository.findUser(login);
        char use_flag = '0';
        Email email = new Email();
        email.setReceiver(login.getEmail());
        email.setOtp(login.getOtp());
        if(user != null) {
            Email otpInfo = commonDAO.findEmailOtp(email);

            if(otpInfo != null) {
                long diffSec = (new Date().getTime() - otpInfo.getSend_date().getTime()) / 1000;
                if(diffSec > 180) {
                    resultDTO.setState(false);
                    resultDTO.setCode(401);
                    resultDTO.setMessage("인증 시간이 초과하였습니다.");
                    resultDTO.setResult(null);
                } else {
                    if (!login.getOtp().equals(otpInfo.getOtp())) {
                        resultDTO.setState(false);
                        resultDTO.setCode(400);
                        resultDTO.setMessage("정보가 일치하지 않습니다.");
                        resultDTO.setResult(null);
                    } else {
                        String regex = "^(?=.*[0-9])(?=.*[a-zA-Z]).{4,}$";
                        boolean pwMatches = Pattern.compile(regex).matcher(login.getPassword()).find();
                        if(login.getPassword().equals(login.getUser_id())) {
                            resultDTO.setState(false);
                            resultDTO.setCode(400);
                            resultDTO.setMessage("비밀번호는 아이디와 동일할 수 없습니다.");
                            resultDTO.setResult(null);
                        } else if(login.getPassword().length() < 4) {
                            resultDTO.setState(false);
                            resultDTO.setCode(400);
                            resultDTO.setMessage("비밀번호는 4자리 이상으로 구성되어야 합니다.");
                            resultDTO.setResult(null);
                        } else if(!pwMatches) {
                            resultDTO.setState(false);
                            resultDTO.setCode(400);
                            resultDTO.setMessage("비밀번호는 숫자와 영문자를 포함하여 구성되어야 합니다.");
                            resultDTO.setResult(null);
                        } else if(passwordEncoder.matches(login.getPassword(), user.getPassword())){
                            resultDTO.setState(false);
                            resultDTO.setCode(400);
                            resultDTO.setMessage("이전 비밀번호와 같은 비밀번호는 사용 할 수 없습니다.");
                            resultDTO.setResult(null);

                        } else {
                            use_flag = '1';

                            Insa insa = new Insa();
                            insa.setPw_reset_flag("Y");
                            insa.setPw_wrong_cnt(0);
                            insa.setPassword(passwordEncoder.encode(login.getPassword()));
                            insa.setPw_reset_date(new Date());
                            insa.setUser_id(login.getUser_id());
                            commonDAO.updateInsa(insa);

                            resultDTO.setState(true);
                            resultDTO.setCode(200);
                            resultDTO.setMessage("비밀번호 변경이 성공하였습니다.");
                            resultDTO.setResult(null);
                        }
                    }
                }
            } else {
                resultDTO.setState(false);
                resultDTO.setCode(400);
                resultDTO.setMessage("정보가 일치하지 않습니다.");
                resultDTO.setResult(null);
            }
        } else {
            resultDTO.setState(false);
            resultDTO.setCode(400);
            resultDTO.setMessage("정보가 일치하지 않습니다.");
            resultDTO.setResult(null);
        }
        email.setUse_flag(use_flag);
        commonDAO.updateEmailOtpFlag(email);

        return resultDTO;
    }

    @Override
    public ResultDTO findRoleMenuList(MenuDTO menuDto, HttpServletRequest request, Authentication auth) {
        resultDTO = new ResultDTO();
        User user = (User) auth.getPrincipal();
        /*boolean chkUserRole = auth.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
        String dept_cd = null;
        if(!chkUserRole) {
            dept_cd = user.getDept_cd();
        }*/
        List<MenuDTO> menuList = commonDAO.findRoleMenuList(menuDto);
        if(!DataLib.isEmpty(menuList)) {
            resultDTO.setState(true);
            resultDTO.setCode(200);
            resultDTO.setMessage(messageSource.getMessage("api.message.200", new String[]{"메뉴권한"}, Locale.KOREA));
            resultDTO.setResult(menuList);
        } else {
            resultDTO.setState(false);
            resultDTO.setCode(400);
            resultDTO.setMessage(messageSource.getMessage("api.message.400", new String[]{"메뉴권한"}, Locale.KOREA));
            resultDTO.setResult(null);
        }

        return resultDTO;
    }

    @Override
    public ResultDTO saveMenuAuth(Map<String, Object> menuDto, HttpServletRequest request, Authentication auth) {
        resultDTO = new ResultDTO();
        User user = (User) auth.getPrincipal();
        String role_id = null;
        List<MenuDTO> menuList = new ArrayList<>();

        for(Object obj : menuDto.keySet()) {
            if(menuDto.get(obj) instanceof Integer) {
                role_id = String.valueOf(menuDto.get(obj));
            }
            if(menuDto.get(obj) instanceof List<?>) {
                List<Map<String, Object>> tempList = (List<Map<String, Object>>) menuDto.get(obj);

                for(Map<String, Object> map : tempList) {
                    MenuDTO dto = new MenuDTO();
                    dto.setRole_menu_id(map.get("role_menu_id").toString());
                    dto.setSys_req_user_id(user.getUser_id());
                    menuList.add(dto);
                }
            }
        }

        int resultId = 0;
        try {
            if (role_id != null) {
                commonDAO.deleteRoleMenuList(role_id);
                for (MenuDTO dto : menuList) {
                    dto.setRole_id(role_id);
                    resultId += commonDAO.saveRoleMenu(dto);
                }
            }

            resultDTO.setState(true);
            resultDTO.setCode(201);
            resultDTO.setMessage(messageSource.getMessage("api.message.201", new String[]{"메뉴권한"}, Locale.KOREA));
            resultDTO.setResult(resultId);
        } catch (Exception e) {
            resultDTO.setState(false);
            resultDTO.setCode(400);
            resultDTO.setMessage(messageSource.getMessage("api.message.400", new String[]{"메뉴권한"}, Locale.KOREA));
            resultDTO.setResult(null);
        }

        return resultDTO;
    }

    public String createCode() {
        Random random = new Random();
        StringBuffer key = new StringBuffer();

        for (int i = 0; i < 8; i++) {
            int index = random.nextInt(4);

            switch (index) {
                case 0: key.append((char) ((int) random.nextInt(26) + 97)); break;
                case 1: key.append((char) ((int) random.nextInt(26) + 65)); break;
                default: key.append(random.nextInt(9));
            }
        }
        return key.toString();
    }
}