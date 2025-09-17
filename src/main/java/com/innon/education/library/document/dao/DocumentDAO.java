package com.innon.education.library.document.dao;

import com.innon.education.library.document.repasitory.dto.ApproveMyworkDTO;
import com.innon.education.library.document.repasitory.dto.DocumentDTO;
import com.innon.education.library.document.repasitory.entity.*;
import com.innon.education.library.document.repasitory.model.Document;
import com.innon.education.library.document.repasitory.model.DocumentMemo;

import java.util.List;
import java.util.Map;

public interface DocumentDAO {
    List<DocumentDTO> findDocumentList(DocumentSearchEntity entity);
    int saveDocument(DocumentEntity entity);
    int saveDocumentApply(DocumentApplyEntity entity);

    int deleteTrashDocument();
    int saveDocumentLoan(DocumentLoanEntity entity);
    int saveDocumentLoanApprove(DocumentLoanApproveEntity entity);

    List<ApproveMyworkDTO> findApproveMyworkList(int role_id);

    List<DocumentDTO> findDocumentLoanList(DocumentSearchEntity entity);
    int findDocumentLoanListCnt(DocumentSearchEntity entity);

    DocumentDTO findDocument(Document document);

    int updateDocument(DocumentEntity entity);
    int updateDocumentConfirmDate(int id);

    int saveDocumentMemo(DocumentMemo memo);

    List<DocumentMemo> findDocumentMemoList(DocumentMemo memo);

    DocumentDTO getDocumentLoan(DocumentSearchEntity entity);

    DocumentDTO findDocumentNo(Document document);
}
