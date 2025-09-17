package com.innon.education.admin.doccode.dao.impl;

import com.innon.education.admin.doccode.dao.DocCodeDAO;
import com.innon.education.admin.doccode.repository.DocCode;
import com.innon.education.admin.doccode.repository.DocCodeDTO;
import org.mybatis.spring.MyBatisSystemException;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class DocCodeDAOImpl implements DocCodeDAO {

    @Autowired
    SqlSessionTemplate sqlSession;

    @Override
    public int saveDocCode(DocCode docCode) {
        try {
            return sqlSession.insert("com.innon.education.admin.mapper.doccode-mapper.saveDocCode", docCode);
        } catch(MyBatisSystemException e) {
            System.out.println(e.getCause().getMessage());
            throw new MyBatisSystemException(e.getCause());
        }
    }

    @Override
    public List<DocCodeDTO> findDocCodeList(DocCode docCode) {
        try {
            return sqlSession.selectList("com.innon.education.admin.mapper.doccode-mapper.findDocCodeList", docCode);
        } catch(MyBatisSystemException e) {
            System.out.println(e.getCause().getMessage());
            throw new MyBatisSystemException(e.getCause());
        }
    }

    @Override
    public int deleteDocCode(DocCode docCode) {
        try {
            return sqlSession.update("com.innon.education.admin.mapper.doccode-mapper.deleteDocCode", docCode);
        } catch(MyBatisSystemException e) {
            System.out.println(e.getCause().getMessage());
            throw new MyBatisSystemException(e.getCause());
        }
    }

    @Override
    public int updateDocCode(DocCode docCode) {
        try {
            return sqlSession.update("com.innon.education.admin.mapper.doccode-mapper.updateDocCode", docCode);
        } catch(MyBatisSystemException e) {
            System.out.println(e.getCause().getMessage());
            throw new MyBatisSystemException(e.getCause());
        }
    }

    @Override
    public String findDocNumByDocCode(DocCode docCode) {
        try {
            return sqlSession.selectOne("com.innon.education.admin.mapper.doccode-mapper.findDocNumByDocCode", docCode);
        } catch(MyBatisSystemException e) {
            System.out.println(e.getCause().getMessage());
            throw new MyBatisSystemException(e.getCause());
        }
    }

    @Override
    public DocCodeDTO findDocCode(String level1) {
        try {
            return sqlSession.selectOne("com.innon.education.admin.mapper.doccode-mapper.findDocCode", level1);
        } catch(MyBatisSystemException e) {
            System.out.println(e.getCause().getMessage());
            throw new MyBatisSystemException(e.getCause());
        }
    }
}
