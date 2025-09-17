package com.innon.education.common.dao.impl;

import com.innon.education.auth.entity.Role;
import com.innon.education.auth.entity.RoleDTO;
import com.innon.education.auth.entity.RoleUser;
import com.innon.education.common.dao.UserRoleDAO;
import org.mybatis.spring.MyBatisSystemException;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserRoleDAOImpl implements UserRoleDAO {

    @Autowired
    SqlSessionTemplate sqlSession;
    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Override
    public List<Role> findUserRoleList(Role role) {;
        try {
            return sqlSession.selectList("com.innon.education.auth.mapper.RoleMapper.findRoleList");
        } catch(MyBatisSystemException e) {
            logger.info(String.valueOf(e.getCause()));
            throw new MyBatisSystemException(e.getCause());
        }
    }

    @Override
    public Role finduserRole(Role role) {
        try {
            return sqlSession.selectOne("com.innon.education.auth.mapper.RoleMapper.findRoleCheck",role);
        } catch(MyBatisSystemException e) {
            logger.info(String.valueOf(e.getCause()));
            throw new MyBatisSystemException(e.getCause());
        }
    }

    @Override
    public int saveRole(Role role) {
        try {
            return sqlSession.insert("com.innon.education.auth.mapper.RoleMapper.saveRole",role);
        } catch(MyBatisSystemException e) {
            logger.info(String.valueOf(e.getCause()));
            throw new MyBatisSystemException(e.getCause());
        }
    }

    @Override
    public int saveRoleUser(RoleUser saveUserRole) {
        try {
            return sqlSession.insert("com.innon.education.auth.mapper.RoleMapper.saveRoleUser",saveUserRole);
        } catch(MyBatisSystemException e) {
            logger.info(String.valueOf(e.getCause()));
            throw new MyBatisSystemException(e.getCause());
        }
    }

    @Override
    public List<Role> roleManageList(Role role) {
        try {
            return sqlSession.selectList("com.innon.education.auth.mapper.RoleMapper.roleManageList");
        } catch(MyBatisSystemException e) {
            logger.info(String.valueOf(e.getCause()));
            throw new MyBatisSystemException(e.getCause());
        }
    }

    @Override
    public List<RoleDTO> roleUserList(Role role) {
        try {
            return sqlSession.selectList("com.innon.education.auth.mapper.RoleMapper.roleUserList",role);
        } catch(MyBatisSystemException e) {
            logger.info(String.valueOf(e.getCause()));
            throw new MyBatisSystemException(e.getCause());
        }
    }

    @Override
    public int updateRoles(Role role) {
        try {
            return sqlSession.update("com.innon.education.auth.mapper.RoleMapper.updateRoles", role);
        } catch(MyBatisSystemException e) {
            logger.info(String.valueOf(e.getCause()));
            throw new MyBatisSystemException(e.getCause());
        }
    }

    @Override
    public int deleteRoleUser(int role_id) {
        try {
            return sqlSession.update("com.innon.education.auth.mapper.RoleMapper.deleteRoleUser", role_id);
        } catch(MyBatisSystemException e) {
            logger.info(String.valueOf(e.getCause()));
            throw new MyBatisSystemException(e.getCause());
        }
    }
}
