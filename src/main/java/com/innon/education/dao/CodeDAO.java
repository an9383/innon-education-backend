package com.innon.education.dao;

import com.innon.education.common.repository.dto.CodeDTO;
import com.innon.education.common.repository.model.Code;

import java.util.List;

public interface CodeDAO {
    List<CodeDTO> findCodeList(Code code);

    int saveCode(Code code);

    int updateCode(Code code);

    int deleteCode(Code code);

    CodeDTO findByCodeName(String code_name);

    /////////////////////////////////////////////////////////

    int editById(CodeDTO codeDto);

    int delete(int no);

    List<CodeDTO> findUpdateOrDeleteCodeList(CodeDTO codeDto);
}
