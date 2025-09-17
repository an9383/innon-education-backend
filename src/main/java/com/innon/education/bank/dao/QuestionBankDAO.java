package com.innon.education.bank.dao;

import com.innon.education.bank.repository.dto.QuestionBankDTO;
import com.innon.education.bank.repository.model.QbQItem;
import com.innon.education.bank.repository.model.QbQuestion;
import com.innon.education.bank.repository.model.QuestionBank;

import java.util.List;

public interface QuestionBankDAO {
    int saveQuestionBank(QuestionBank questionBank);
    int updateQuestionBank(QuestionBank questionBank);
    int deleteQuestionBank(QuestionBank questionBank);
    List<QuestionBankDTO> findQuestionBankList(QuestionBank questionBank);

    int saveQbQuestion(QbQuestion question);
    int deleteQbQuestion(QbQuestion question);
    List<QbQuestion> findQbQuestionList(QbQuestion question);

    int saveQbQItem(QbQItem qbQItem);
    int updateQbQItem(QbQItem qbQItem);
    int deleteQbQItemList(int qb_q_id);
    List<QbQItem> findQbQItemList(int qb_q_id);
}
