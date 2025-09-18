package com.innon.education.admin.board.service;

import com.innon.education.admin.board.repository.model.Faq;
import com.innon.education.code.controller.dto.ResultDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;

public interface FaqService {
    ResultDTO saveFaq(Faq faq, HttpServletRequest request, Authentication auth);
    ResultDTO findFaqList(Faq faq, HttpServletRequest request, Authentication auth);
    ResultDTO updateFaq(Faq faq, HttpServletRequest request, Authentication auth);
    ResultDTO deleteFaq(Faq faq, HttpServletRequest request, Authentication auth);
}
