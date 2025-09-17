package com.innon.education.admin.manage.service.impl;

import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.innon.education.admin.manage.dao.ManagerDAO;
import com.innon.education.admin.manage.repository.Manager;
import com.innon.education.admin.manage.repository.ManagerDTO;
import com.innon.education.admin.manage.repository.ManagerUser;
import com.innon.education.admin.manage.service.ManagerService;
import com.innon.education.auth.entity.User;
import com.innon.education.common.repository.entity.LogEntity;
import com.innon.education.common.service.CommonService;
import com.innon.education.common.util.DataLib;
import com.innon.education.controller.dto.ResultDTO;
import com.innon.education.dao.CodeDAO;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class ManagerServiceImpl implements ManagerService {
    private ResultDTO resultDTO;
    @Autowired
    ManagerDAO managerDAO;
    @Autowired
    MessageSource messageSource;
    @Autowired
    CommonService commonService;
    @Autowired
    CodeDAO codeDAO;

    @Override
    public ResultDTO findManagerList(Manager manager, HttpServletRequest request, Authentication authentication) {
        resultDTO = new ResultDTO();
        resultDTO.setResult(managerDAO.findManageList(manager));
        resultDTO.setCode(200);
        // resultDTO.setMessage(messageSource.getMessage());
        return resultDTO;
    }

    @Override
    public ResultDTO saveManager(Manager manager, HttpServletRequest request, Authentication authentication) {
        resultDTO = new ResultDTO();
        User user = (User) authentication.getPrincipal();

        manager.setSys_reg_user_id(authentication.getName());
        manager.setDept_cd(user.getDept_cd());
        manager.getManagerUsers().forEach(managerUser -> {
            managerUser.setSys_reg_user_id(authentication.getName());
        });
        int resultId = managerDAO.saveManage(manager);
        if (resultId > 0) {
            //resultDTO.setMessage("위치 코드 등록이 완료되었습니다.");


            int logId = commonService.saveLog(LogEntity.builder()
                    .table_id(manager.getId())
                    .page_nm("sign")
                    .url_addr(request.getRequestURI())
                    .state("insert")
                    .reg_user_id(authentication.getName())
                    .build());
            try {

//                    Object obj=sign;
//                    for (Field field : obj.getClass().getDeclaredFields()){
//                        field.setAccessible(true);
//                        Object value=field.get(obj);
//                        resultDTO = commonService.saveLogDetail(LogChild
//                                .builder()
//                                .log_id(logId)
//                                .field(field.getName())
//                                .new_value(value.toString())
//                                .reg_user_id(authentication.getName())
//                                .build());
//                    }

            } catch (Exception e) {
                e.printStackTrace();
            }

            manager.getManagerUsers().forEach(managerUser -> {
                managerUser.setManager_id(manager.getId());
                managerUser.setSys_reg_user_id(authentication.getName());
                int reUserId = managerDAO.saveManagerUser(managerUser);
            });


            resultDTO.setState(true);
            resultDTO.setCode(201);
            resultDTO.setMessage(messageSource.getMessage("api.message.201", new String[]{"담당자"}, Locale.KOREA));
            //todo 정주원 save 완료시 resultdata 에 id 리턴
            resultDTO.setResult(manager.getId());
        } else {
            resultDTO.setState(false);
            resultDTO.setCode(400);
            resultDTO.setMessage(messageSource.getMessage("api.message.400", new String[]{"담당자"}, Locale.KOREA));
            resultDTO.setResult(null);
        }
        return resultDTO;
    }

    @Override
    public ResultDTO findManagerUserList(Manager manager, HttpServletRequest request, Authentication auth) {
        resultDTO = new ResultDTO();

        List<ManagerUser> signUserList = managerDAO.findManagerUserList(manager);
        if(!DataLib.isEmpty(signUserList)) {

//            int logId = commonService.saveLog(LogEntity.builder()
//                    .table_id(manager.getId())
//                    .page_nm("sign")
//                    .url_addr(request.getRequestURI())
//                    .state("view")
//                    .reg_user_id(auth.getName())
//                    .build());


            resultDTO.setState(true);
            resultDTO.setCode(200);
            resultDTO.setMessage(messageSource.getMessage("api.message.200", new String[]{"결재권자"}, Locale.KOREA));
            resultDTO.setResult(signUserList);
        } else {
            resultDTO.setState(false);
            resultDTO.setCode(400);
            resultDTO.setMessage(messageSource.getMessage("api.message.400", new String[]{"결재권자"}, Locale.KOREA));
            resultDTO.setResult(null);
        }

        return resultDTO;
    }

    @Override
    public ResultDTO updateManager(Manager manager, HttpServletRequest request, Authentication auth) {
        resultDTO = new ResultDTO();
        manager.setSys_reg_user_id(auth.getName());
        int update = managerDAO.updateManagerUpdate(manager);
        if(update > 0) {
            ManagerUser delUser = new ManagerUser();
            delUser.setManager_id(manager.getId());
            delUser.setSys_reg_user_id(auth.getName());
            managerDAO.deleteManagerUser(delUser);

            if(manager.getManagerUsers() != null) {
                manager.getManagerUsers().forEach(managerUser -> {
                    managerUser.setSys_reg_user_id(auth.getName());
                    managerUser.setManager_id(manager.getId());
                    int saveUserId = managerDAO.saveManagerUser(managerUser);
                    System.out.println(saveUserId);
                });
            }

            resultDTO.setState(true);
            resultDTO.setCode(202);
            resultDTO.setMessage(messageSource.getMessage("api.message.202", new String[]{"담당자 ","수정"}, Locale.KOREA));
            resultDTO.setResult(manager.getId());
        } else {
            resultDTO.setState(false);
            resultDTO.setCode(400);
            resultDTO.setMessage(messageSource.getMessage("api.message.400", new String[]{"담당자", "수정"}, Locale.KOREA));
            resultDTO.setResult(null);
        }

        return resultDTO;
    }

    @Override
    public ResultDTO findManagerDept(Manager manager, HttpServletRequest request, Authentication auth) {
        resultDTO = new ResultDTO();

        List<ManagerDTO> managerList = managerDAO.findManageList(manager);
        managerList.forEach(managerDTO -> {
            manager.setId(managerDTO.getId());
            managerDTO.setChildren(managerDAO.findManagerUserList(manager));
       });
//
//        List<ManagerUser> signUserList = managerDAO.findManagerUserList(manager.getId());
//        Map<Object, List<String>> colum = signUserList.stream().map(managerUser -> managerUser.getTitle()).collect(Collectors.groupingBy(s -> s));
//        Map<String, List<ManagerUser>> managerList = signUserList.stream().collect(Collectors.groupingBy(ManagerUser::getTitle));
//        managerList.forEach((s, managerUsers) -> {
//            System.out.println(s);
//            System.out.println(managerUsers);
//        });
        if(!DataLib.isEmpty(managerList)) {

//            int logId = commonService.saveLog(LogEntity.builder()
//                    .table_id(manager.getId())
//                    .page_nm("sign")
//                    .url_addr(request.getRequestURI())
//                    .state("view")
//                    .reg_user_id(auth.getName())
//                    .build());


            resultDTO.setState(true);
            resultDTO.setCode(200);
            resultDTO.setMessage(messageSource.getMessage("api.message.200", new String[]{"결재권자"}, Locale.KOREA));
            resultDTO.setResult(managerList);
        } else {
            resultDTO.setState(false);
            resultDTO.setCode(400);
            resultDTO.setMessage(messageSource.getMessage("api.message.400", new String[]{"결재권자"}, Locale.KOREA));
            resultDTO.setResult(null);
        }

        return resultDTO;
    }
}
