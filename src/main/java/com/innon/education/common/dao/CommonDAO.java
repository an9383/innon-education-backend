package com.innon.education.common.dao;

import com.innon.education.admin.group.repository.Group;
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
import com.innon.education.common.repository.model.Insa;
import com.innon.education.common.repository.model.Login;

import java.util.List;

public interface CommonDAO {
    List<EduDeptDTO> findEduDeptList(DeptEntity deptEntity);
    List<EduDeptDTO> findChildEduDeptCdList(DeptEntity deptEntity);
    List<EduInsaDTO> findEduInsaList(InsaEntity insaEntity);
    List<EduInsaDTO> findEduPopupInsaList(Insa insa);
    int findEduInsaListCnt(InsaEntity insaEntity);
    String findFactoryCdByUserId(String user_id);

    List<EduInsaDTO> findEduInsaListCustom(String o);

    String findAllDepth();

    int saveLog(LogEntity logEntity);
    int saveLogDetail(LogChild logChild);

    List<LogDTO> findLogList(LogEntity logEntity);
    List<LogChild> logChildList(LogEntity logEntity);
    int updateInsa(Insa insa);

    int updateDept(Dept dept);

    List<EduDeptDTO> findEduDeptPopupList(DeptEntity deptEntity);

    List<Group> findGroupInfoByDeptCd(String dept_cd);

    List<Dept> findTopDeptByDeptCd(String dept_cd);

    int saveLoginInfo(Login login);

    Email findEmailOtp(Email email);

    int saveEmailOtp(Email email);

    int updateEmailOtpFlag(Email email);

    List<MenuDTO> findRoleMenuList(MenuDTO menuDto);

    int deleteRoleMenuList(String roleId);

    int saveRoleMenu(MenuDTO menuDto);
}
