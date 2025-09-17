package com.innon.education.qualified.technology.dao.impl;

import com.innon.education.qualified.technology.dao.TechnologyDAO;
import com.innon.education.qualified.technology.repository.dto.TechnologyDTO;
import com.innon.education.qualified.technology.repository.entity.TechnologyEntity;
import com.innon.education.qualified.technology.repository.entity.TechnologySearchEntity;
import org.mybatis.spring.MyBatisSystemException;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TechnologyDAOImpl implements TechnologyDAO {

    @Autowired
    SqlSessionTemplate sqlSession;

    @Override
    public int saveQualifiedTechnology(TechnologyEntity entity) {
        try {
            return sqlSession.insert("com.innon.education.qualified.mapper.technology-mapper.saveQualifiedTechnology", entity);
        } catch(MyBatisSystemException e) {
            throw new MyBatisSystemException(e.getCause());
        }
    }

    @Override
    public List<TechnologyDTO> findQualifiedTechnologyList(TechnologySearchEntity entity) {
        try {
            return sqlSession.selectList("com.innon.education.qualified.mapper.technology-mapper.findQualifiedTechnologyList", entity);
        } catch(MyBatisSystemException e) {
            throw new MyBatisSystemException(e.getCause());
        }
    }
}
