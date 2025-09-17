package com.innon.education.library.factory.dao;

import com.innon.education.library.document.repasitory.entity.DocumentApplyEntity;
import com.innon.education.library.factory.repository.dto.FactoryDTO;
import com.innon.education.library.factory.repository.entity.FactoryEntity;
import com.innon.education.library.factory.repository.entity.FactorySearchEntity;

import java.util.List;

public interface FactoryDAO {
    List<FactoryDTO> findDisplayFactoryList(FactorySearchEntity entity);
    int saveLocationDocument(FactoryEntity entity);
    int updateDocumentLoan(DocumentApplyEntity entity);

    int deleteTrashLocationDocument();
    int updateLocationDocument(FactoryEntity entity);

    int findLocationDocumentCnt(FactoryEntity entity);
    int findDocumentLoanCntByDocId(DocumentApplyEntity entity);

    int deleteLocationDocument(DocumentApplyEntity documentApplyEntity);
}
