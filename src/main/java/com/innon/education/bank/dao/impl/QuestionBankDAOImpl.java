package com.innon.education.bank.dao.impl;

import com.innon.education.bank.dao.QuestionBankDAO;
import com.innon.education.bank.repository.dto.QuestionBankDTO;
import com.innon.education.bank.repository.model.QbQItem;
import com.innon.education.bank.repository.model.QbQuestion;
import com.innon.education.bank.repository.model.QuestionBank;
import org.mybatis.spring.MyBatisSystemException;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class QuestionBankDAOImpl implements QuestionBankDAO {

    @Autowired
    SqlSessionTemplate sqlSession;

    @Override
    public int saveQuestionBank(QuestionBank questionBank) {
        try {
            return sqlSession.insert("com.innon.education.bank.mapper.question-mapper.saveQuestionBank", questionBank);
        } catch (MyBatisSystemException e) {
            System.out.println(e.getCause());
            throw new MyBatisSystemException(e.getCause());
        }
    }

    @Override
    public int updateQuestionBank(QuestionBank questionBank) {
        try {
            return sqlSession.update("com.innon.education.bank.mapper.question-mapper.updateQuestionBank", questionBank);
        } catch (MyBatisSystemException e) {
            System.out.println(e.getCause());
            throw new MyBatisSystemException(e.getCause());
        }
    }

    @Override
    public int deleteQuestionBank(QuestionBank questionBank) {
        try {
            return sqlSession.update("com.innon.education.bank.mapper.question-mapper.deleteQuestionBank", questionBank);
        } catch (MyBatisSystemException e) {
            System.out.println(e.getCause());
            throw new MyBatisSystemException(e.getCause());
        }
    }

    @Override
    public List<QuestionBankDTO> findQuestionBankList(QuestionBank questionBank) {
        try {
            return sqlSession.selectList("com.innon.education.bank.mapper.question-mapper.findQuestionBankList", questionBank);
        } catch (MyBatisSystemException e) {
            System.out.println(e.getCause());
            throw new MyBatisSystemException(e.getCause());
        }
    }

    @Override
    public int saveQbQuestion(QbQuestion question) {
        try {
            return sqlSession.insert("com.innon.education.bank.mapper.question-mapper.saveQbQuestion", question);
        } catch (MyBatisSystemException e) {
            System.out.println(e.getCause());
            throw new MyBatisSystemException(e.getCause());
        }
    }

    @Override
    public int deleteQbQuestion(QbQuestion question) {
        try {
            return sqlSession.update("com.innon.education.bank.mapper.question-mapper.deleteQbQuestion", question);
        } catch (MyBatisSystemException e) {
            System.out.println(e.getCause());
            throw new MyBatisSystemException(e.getCause());
        }
    }

    @Override
    public List<QbQuestion> findQbQuestionList(QbQuestion question) {
        try {
            return sqlSession.selectList("com.innon.education.bank.mapper.question-mapper.findQbQuestionList", question);
        } catch (MyBatisSystemException e) {
            System.out.println(e.getCause());
            throw new MyBatisSystemException(e.getCause());
        }
    }

    @Override
    public int saveQbQItem(QbQItem qbQItem) {
        try {
            return sqlSession.insert("com.innon.education.bank.mapper.question-mapper.saveQbQItem", qbQItem);
        } catch (MyBatisSystemException e) {
            System.out.println(e.getCause());
            throw new MyBatisSystemException(e.getCause());
        }
    }

    @Override
    public int updateQbQItem(QbQItem qbQItem) {
        try {
            return sqlSession.update("com.innon.education.bank.mapper.question-mapper.updateQbQItem", qbQItem);
        } catch (MyBatisSystemException e) {
            System.out.println(e.getCause());
            throw new MyBatisSystemException(e.getCause());
        }
    }

    @Override
    public int deleteQbQItemList(int qb_q_id) {
        try {
            return sqlSession.delete("com.innon.education.bank.mapper.question-mapper.deleteQbQItemList", qb_q_id);
        } catch (MyBatisSystemException e) {
            System.out.println(e.getCause());
            throw new MyBatisSystemException(e.getCause());
        }
    }

    @Override
    public List<QbQItem> findQbQItemList(int qb_q_id) {
        try {
            return sqlSession.selectList("com.innon.education.bank.mapper.question-mapper.findQbQItemList", qb_q_id);
        } catch (MyBatisSystemException e) {
            System.out.println(e.getCause());
            throw new MyBatisSystemException(e.getCause());
        }
    }
}
