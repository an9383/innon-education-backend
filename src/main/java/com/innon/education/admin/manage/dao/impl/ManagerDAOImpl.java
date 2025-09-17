package com.innon.education.admin.manage.dao.impl;

import com.innon.education.admin.manage.dao.ManagerDAO;
import com.innon.education.admin.manage.repository.Manager;
import com.innon.education.admin.manage.repository.ManagerDTO;
import com.innon.education.admin.manage.repository.ManagerUser;
import org.mybatis.spring.MyBatisSystemException;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ManagerDAOImpl implements ManagerDAO {
    @Autowired
    SqlSessionTemplate sqlSession;

    @Override
    public int saveManage(Manager manager) {
        return sqlSession.insert("com.innon.education.admin.mapper.manager-mapper.saveManager", manager);
    }

    @Override
    public int saveManagerUser(ManagerUser managerUser) {
        return sqlSession.insert("com.innon.education.admin.mapper.manager-mapper.saveManagerUser", managerUser);
    }

    @Override
    public List<ManagerDTO> findManageList(Manager manager) {
        return sqlSession.selectList("com.innon.education.admin.mapper.manager-mapper.findManagerList", manager);
    }

    @Override
    public List<ManagerUser> findManagerUserList(Manager manager) {
        return sqlSession.selectList("com.innon.education.admin.mapper.manager-mapper.findManagerUserList", manager);

    }
    @Override
    public List<Object> findAuthMenu(Manager manager) {
        return sqlSession.selectList("com.innon.education.admin.mapper.manager-mapper.findAuthMenu", manager);

    }
    @Override
    public int updateManagerUpdate(Manager manager) {
        try {
            return sqlSession.update("com.innon.education.admin.mapper.manager-mapper.updateManager", manager);
        } catch(MyBatisSystemException e) {
            System.out.println(e.getCause().getMessage());
            throw new MyBatisSystemException(e.getCause());
        }
    }

    @Override
    public int updateManagerUser(ManagerUser managerUser) {
        try {
            return sqlSession.selectOne("com.innon.education.admin.mapper.manager-mapper.updateManagerUser", managerUser);
        } catch(MyBatisSystemException e) {
            System.out.println(e.getCause().getMessage());
            throw new MyBatisSystemException(e.getCause());
        }
    }

    @Override
    public int deleteManagerUser(ManagerUser managerUser) {
        try {
            return sqlSession.delete("com.innon.education.admin.mapper.manager-mapper.deleteManagerUser", managerUser);
        } catch(MyBatisSystemException e) {
            System.out.println(e.getCause().getMessage());
            throw new MyBatisSystemException(e.getCause());
        }
    }
}
