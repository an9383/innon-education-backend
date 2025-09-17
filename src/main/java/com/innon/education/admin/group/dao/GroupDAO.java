package com.innon.education.admin.group.dao;

import com.innon.education.admin.group.repository.Group;
import com.innon.education.admin.group.repository.GroupDTO;
import com.innon.education.admin.group.repository.GroupDept;

import java.util.List;

public interface GroupDAO {
    int saveGroup(Group group);

    int saveGroupDept(GroupDept groupDept);

    List<GroupDTO> findGroupList(Group group);

    List<GroupDept> findGroupDeptList(Group group);

    int updateGroupUpdate(Group group);

    int updateGroupDept(GroupDept groupDept);
}
