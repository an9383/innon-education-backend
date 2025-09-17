package com.innon.education.library.document.service;

import com.innon.education.controller.dto.ResultDTO;
import com.innon.education.library.document.repasitory.model.Document;
import com.innon.education.library.document.repasitory.model.DocumentMemo;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Map;

public interface DocumentService {
    ResultDTO findDocumentList(Document document, HttpServletRequest request, Authentication auth);
    ResultDTO saveDocument(Document document, HttpServletRequest request, Authentication auth);
    ResultDTO updateDocument(Document document, HttpServletRequest request, Authentication auth);
    ResultDTO saveDocumentApply(List<Document> documentList, Authentication auth);

    ResultDTO readExcel();
    ResultDTO saveLibraryList(List<Map<String, Object>> insertList);
    ResultDTO saveLibrary(List<Map<String, Object>> libraryList, HttpServletRequest request, Authentication auth);

    ResultDTO deleteTrashDocument();
    ResultDTO deleteAllTrashDocument();

    ResultDTO findApproveMyworkList(Document document, Authentication auth);

    ResultDTO findDocumentLoanList(Document document, Authentication auth);

    ResultDTO findDocument(Document document, Authentication auth);

    ResultDTO saveDocumentForm(DocumentMemo memo, HttpServletRequest request, Authentication auth);

    ResultDTO findDocumentMemoList(DocumentMemo memo, HttpServletRequest request, Authentication auth);

    ResultDTO updateDocumentLoan(Document document, HttpServletRequest request, Authentication auth);

    ResultDTO findDocumentNo(Document document, Authentication auth);
}