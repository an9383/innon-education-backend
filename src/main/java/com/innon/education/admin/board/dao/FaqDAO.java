package com.innon.education.admin.board.dao;

import com.innon.education.admin.board.repository.dto.FaqDTO;
import com.innon.education.admin.board.repository.model.Faq;

import java.util.List;

public interface FaqDAO {
    int saveFaq(Faq faq);
    List<FaqDTO> findFaqList(Faq faq);
    int updateFaq(Faq faq);
    int deleteFaq(Faq faq);
}
