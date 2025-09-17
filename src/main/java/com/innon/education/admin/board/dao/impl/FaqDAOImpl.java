package com.innon.education.admin.board.dao.impl;

import com.innon.education.admin.board.dao.FaqDAO;
import com.innon.education.admin.board.repository.dto.FaqDTO;
import com.innon.education.admin.board.repository.model.Faq;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.MyBatisSystemException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class FaqDAOImpl implements FaqDAO {

    @Autowired
    private SqlSession sqlSession;

    @Override
    public int saveFaq(Faq faq) {
        try {
            return sqlSession.insert("com.innon.education.admin.mapper.faq-mapper.saveFaq", faq);
        } catch(MyBatisSystemException e) {
            throw new MyBatisSystemException(e.getCause());
        }
    }

    @Override
    public List<FaqDTO> findFaqList(Faq faq) {
        try {
            return sqlSession.selectList("com.innon.education.admin.mapper.faq-mapper.findFaqList", faq);
        } catch(MyBatisSystemException e) {
            throw new MyBatisSystemException(e.getCause());
        }
    }

    @Override
    public int updateFaq(Faq faq) {
        try {
            return sqlSession.update("com.innon.education.admin.mapper.faq-mapper.updateFaq", faq);
        } catch(MyBatisSystemException e) {
            System.out.println(e.getCause());
            throw new MyBatisSystemException(e.getCause());
        }
    }

    @Override
    public int deleteFaq(Faq faq) {
        try {
            return sqlSession.update("com.innon.education.admin.mapper.faq-mapper.deleteFaq", faq);
        } catch(MyBatisSystemException e) {
            throw new MyBatisSystemException(e.getCause());
        }
    }
}
