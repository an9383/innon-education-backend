package com.innon.education.admin.doccode.dao;

import com.innon.education.admin.doccode.repository.DocCode;
import com.innon.education.admin.doccode.repository.DocCodeDTO;

import java.util.List;

public interface DocCodeDAO {
    int saveDocCode(DocCode docCode);
    List<DocCodeDTO> findDocCodeList(DocCode docCode);
    int deleteDocCode(DocCode docCode);
    int updateDocCode(DocCode docCode);
    String findDocNumByDocCode(DocCode docCode);

    DocCodeDTO findDocCode(String level1);
}
