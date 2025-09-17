package com.innon.education.library.document.dao.impl;

import com.innon.education.library.document.dao.DocumentDAO;
import com.innon.education.library.document.repasitory.dto.ApproveMyworkDTO;
import com.innon.education.library.document.repasitory.dto.DocumentDTO;
import com.innon.education.library.document.repasitory.entity.*;
import com.innon.education.library.document.repasitory.model.Document;
import com.innon.education.library.document.repasitory.model.DocumentMemo;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.MyBatisSystemException;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@Slf4j
public class DocumentDAOImpl implements DocumentDAO {

    @Autowired
    SqlSessionTemplate sqlSession;

    @Override
    public List<DocumentDTO> findDocumentList(DocumentSearchEntity entity) {
        try {
            return sqlSession.selectList("com.innon.education.library.mapper.document-mapper.findDocumentList", entity);
        } catch(MyBatisSystemException e) {
            log.error(e.getCause().toString());
            throw new MyBatisSystemException(e);
        }
    }

    @Override
    public int saveDocument(DocumentEntity entity) {
        try {
            return sqlSession.insert("com.innon.education.library.mapper.document-mapper.saveDocument", entity);
        } catch(MyBatisSystemException e) {
            log.error(e.getCause().toString());
            throw new MyBatisSystemException(e.getCause());
        }
    }

    @Override
    public int saveDocumentApply(DocumentApplyEntity entity) {
        try {
            return sqlSession.selectOne("com.innon.education.library.mapper.document-mapper.saveDocumentApply", entity);
        } catch(MyBatisSystemException e) {
            log.error(e.getCause().toString());
            throw new MyBatisSystemException(e.getCause());
        }
    }

    @Override
    public int deleteTrashDocument() {
        try {
            return sqlSession.delete("com.innon.education.library.mapper.document-mapper.deleteTrashDocument");
        } catch(MyBatisSystemException e) {
            log.error(e.getCause().toString());
            throw new MyBatisSystemException(e.getCause());
        }
    }

    @Override
    public int saveDocumentLoan(DocumentLoanEntity entity) {
        try {
            return sqlSession.insert("com.innon.education.library.mapper.document-mapper.saveDocumentLoan", entity);
        } catch(MyBatisSystemException e) {
            log.error(e.getCause().toString());
            throw new MyBatisSystemException(e.getCause());
        }
    }

    @Override
    public int saveDocumentLoanApprove(DocumentLoanApproveEntity entity) {
        try {
            return sqlSession.insert("com.innon.education.library.mapper.document-mapper.saveDocumentLoanApprove", entity);
        } catch(MyBatisSystemException e) {
            log.error(e.getCause().toString());
            throw new MyBatisSystemException(e.getCause());
        }
    }

    @Override
    public List<ApproveMyworkDTO> findApproveMyworkList(int role_id) {
        try {
            return sqlSession.selectList("com.innon.education.library.mapper.document-mapper.findApproveMyworkList", role_id);
        } catch(MyBatisSystemException e) {
            log.error(e.getCause().toString());
            throw new MyBatisSystemException(e.getCause());
        }
    }

    @Override
    public List<DocumentDTO> findDocumentLoanList(DocumentSearchEntity entity) {
        try {
            return sqlSession.selectList("com.innon.education.library.mapper.document-mapper.findDocumentLoanList", entity);
        } catch(MyBatisSystemException e) {
            log.error(e.getCause().toString());
            throw new MyBatisSystemException(e.getCause());
        }
    }

    @Override
    public int findDocumentLoanListCnt(DocumentSearchEntity entity) {
        try {
            return sqlSession.selectOne("com.innon.education.library.mapper.document-mapper.findDocumentLoanListCnt", entity);
        } catch(MyBatisSystemException e) {
            log.error(e.getCause().toString());
            throw new MyBatisSystemException(e.getCause());
        }
    }

    @Override
    public DocumentDTO findDocument(Document document) {
        try {
            return sqlSession.selectOne("com.innon.education.library.mapper.document-mapper.findDocumentList", document);
        } catch(MyBatisSystemException e) {
            log.error(e.getCause().toString());
            throw new MyBatisSystemException(e.getCause());
        }
    }
    @Override
    public DocumentDTO findDocumentNo(Document document) {
        try {
            DocumentSearchEntity param = new DocumentSearchEntity();
            param.setDocument_num(document.getDocument_num());
            return sqlSession.selectOne("com.innon.education.library.mapper.document-mapper.findDocumentNo", param);
        } catch(MyBatisSystemException e) {
            log.error(e.getCause().toString());
            throw new MyBatisSystemException(e.getCause());
        }
    }

    @Override
    public int updateDocument(DocumentEntity entity) {
        try {
            return sqlSession.update("com.innon.education.library.mapper.document-mapper.updateDocument", entity);
        } catch(MyBatisSystemException e) {
            log.error(e.getCause().toString());
            throw new MyBatisSystemException(e.getCause());
        }
    }

    @Override
    public int updateDocumentConfirmDate(int id) {
        try {
            return sqlSession.update("com.innon.education.library.mapper.document-mapper.updateDocumentConfirmDate", id);
        } catch(MyBatisSystemException e) {
            log.error(e.getCause().toString());
            throw new MyBatisSystemException(e.getCause());
        }
    }

    @Override
    public int saveDocumentMemo(DocumentMemo memo) {
        try {
            return sqlSession.insert("com.innon.education.library.mapper.document-mapper.saveDocumentMemo", memo);
        } catch(MyBatisSystemException e) {
            log.error(e.getCause().toString());
            throw new MyBatisSystemException(e.getCause());
        }
    }

    @Override
    public List<DocumentMemo> findDocumentMemoList(DocumentMemo memo) {
        try {
            return sqlSession.selectList("com.innon.education.library.mapper.document-mapper.findDocumentMemoList", memo);
        } catch(MyBatisSystemException e) {
            log.error(e.getCause().toString());
            throw new MyBatisSystemException(e.getCause());
        }
    }

    @Override
    public DocumentDTO getDocumentLoan(DocumentSearchEntity documentSearchEntity) {
        return sqlSession.selectOne("com.innon.education.library.mapper.document-mapper.findDocumentList", documentSearchEntity);

    }
}
