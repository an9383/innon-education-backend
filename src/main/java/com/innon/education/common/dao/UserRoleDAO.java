package com.innon.education.common.dao;

import com.innon.education.auth.entity.Role;
import com.innon.education.auth.entity.RoleDTO;
import com.innon.education.auth.entity.RoleUser;

import java.util.List;

public interface UserRoleDAO {
    List<Role> findUserRoleList(Role role);

    Role finduserRole(Role role);

    int saveRole(Role role);

    int saveRoleUser(RoleUser saveUserRole);

    List<Role> roleManageList(Role role);

    List<RoleDTO> roleUserList(Role role);

    int updateRoles(Role role);

    int deleteRoleUser(int role_id);
}
