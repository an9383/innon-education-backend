package com.innon.education.library.factory.dao.impl;

import com.innon.education.library.document.repasitory.entity.DocumentApplyEntity;
import com.innon.education.library.factory.dao.FactoryDAO;
import com.innon.education.library.factory.repository.dto.FactoryDTO;
import com.innon.education.library.factory.repository.entity.FactoryEntity;
import com.innon.education.library.factory.repository.entity.FactorySearchEntity;
import org.mybatis.spring.MyBatisSystemException;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class FactoryDAOImpl implements FactoryDAO {

    @Autowired
    SqlSessionTemplate sqlSession;

    @Override
    public List<FactoryDTO> findDisplayFactoryList(FactorySearchEntity entity) {
        try {
            return sqlSession.selectList("com.innon.education.library.mapper.factory-mapper.findDisplayFactoryList", entity);
        } catch(MyBatisSystemException e) {
            System.out.println(e.getCause().getMessage());
            throw new MyBatisSystemException(e.getCause());
        }
    }

    @Override
    public int saveLocationDocument(FactoryEntity entity) {
        try {
            return sqlSession.insert("com.innon.education.library.mapper.factory-mapper.saveLocationDocument", entity);
        } catch(MyBatisSystemException e) {
            System.out.println(e.getCause().getMessage());
            throw new MyBatisSystemException(e.getCause());
        }
    }

    @Override
    public int updateDocumentLoan(DocumentApplyEntity entity) {
        try {
            return sqlSession.update("com.innon.education.library.mapper.factory-mapper.updateDocumentLoan", entity);
        } catch(MyBatisSystemException e) {
            System.out.println(e.getCause().getMessage());
            throw new MyBatisSystemException(e.getCause());
        }
    }

    @Override
    public int deleteTrashLocationDocument() {
        try {
            return sqlSession.delete("com.innon.education.library.mapper.factory-mapper.deleteTrashLocationDocument");
        } catch(MyBatisSystemException e) {
            System.out.println(e.getCause().getMessage());
            throw new MyBatisSystemException(e.getCause());
        }
    }

    @Override
    public int updateLocationDocument(FactoryEntity entity) {
        try {
            return sqlSession.update("com.innon.education.library.mapper.factory-mapper.updateLocationDocument", entity);
        } catch(MyBatisSystemException e) {
            System.out.println(e.getCause().getMessage());
            throw new MyBatisSystemException(e.getCause());
        }
    }

    @Override
    public int findLocationDocumentCnt(FactoryEntity entity) {
        return sqlSession.selectOne("com.innon.education.library.mapper.factory-mapper.findLocationDocumentCnt", entity);
    }

    @Override
    public int findDocumentLoanCntByDocId(DocumentApplyEntity entity) {
        return sqlSession.selectOne("com.innon.education.library.mapper.factory-mapper.findDocumentLoanCntByDocId", entity);
    }

    @Override
    public int deleteLocationDocument(DocumentApplyEntity documentApplyEntity) {
        try {
            return sqlSession.delete("com.innon.education.library.mapper.factory-mapper.deleteLocationDocument");
        } catch(MyBatisSystemException e) {
            System.out.println(e.getCause().getMessage());
            throw new MyBatisSystemException(e.getCause());
        }

    }
}
