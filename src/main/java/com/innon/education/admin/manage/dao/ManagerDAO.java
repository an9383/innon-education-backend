package com.innon.education.admin.manage.dao;

import com.innon.education.admin.manage.repository.Manager;
import com.innon.education.admin.manage.repository.ManagerDTO;
import com.innon.education.admin.manage.repository.ManagerUser;

import java.util.List;
import java.util.Map;

public interface ManagerDAO {
    int saveManage(Manager manager);

    int saveManagerUser(ManagerUser managerUser);

    List<ManagerDTO> findManageList(Manager manager);

    List<ManagerUser> findManagerUserList(Manager manager);

    List<Object> findAuthMenu(Manager manager);

    int updateManagerUpdate(Manager manager);

    int updateManagerUser(ManagerUser signUser);

    int deleteManagerUser(ManagerUser managerUser);
}
