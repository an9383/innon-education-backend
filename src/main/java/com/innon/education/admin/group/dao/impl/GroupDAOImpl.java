package com.innon.education.admin.group.dao.impl;

import com.innon.education.admin.group.dao.GroupDAO;
import com.innon.education.admin.group.repository.Group;
import com.innon.education.admin.group.repository.GroupDTO;
import com.innon.education.admin.group.repository.GroupDept;
import org.mybatis.spring.MyBatisSystemException;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class GroupDAOImpl implements GroupDAO {
    @Autowired
    SqlSessionTemplate sqlSession;

    @Override
    public int saveGroup(Group group) {
        return sqlSession.insert("com.innon.education.admin.mapper.group-mapper.saveGroup", group);
    }

    @Override
    public int saveGroupDept(GroupDept groupDept) {
        return sqlSession.insert("com.innon.education.admin.mapper.group-mapper.saveGroupDept", groupDept);
    }

    @Override
    public List<GroupDTO> findGroupList(Group group) {
        return sqlSession.selectList("com.innon.education.admin.mapper.group-mapper.findGroupList", group);
    }

    @Override
    public List<GroupDept> findGroupDeptList(Group group) {
        return sqlSession.selectList("com.innon.education.admin.mapper.group-mapper.findGroupDeptList", group);
    }

    @Override
    public int updateGroupUpdate(Group group) {
        try {
            return sqlSession.update("com.innon.education.admin.mapper.group-mapper.updateGroupUpdate", group);
        } catch(MyBatisSystemException e) {
            System.out.println(e.getCause().getMessage());
            throw new MyBatisSystemException(e.getCause());
        }
    }

    @Override
    public int updateGroupDept(GroupDept groupDept) {
        try {
            return sqlSession.update("com.innon.education.admin.mapper.group-mapper.updateGroupDept", groupDept);
        } catch(MyBatisSystemException e) {
            System.out.println(e.getCause().getMessage());
            throw new MyBatisSystemException(e.getCause());
        }
    }
}
