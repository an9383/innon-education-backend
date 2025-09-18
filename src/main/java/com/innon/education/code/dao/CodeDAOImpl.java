package com.innon.education.code.controller.dao;

import com.innon.education.common.repository.dto.CodeDTO;
import com.innon.education.common.repository.model.Code;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.MyBatisSystemException;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Slf4j
public class CodeDAOImpl implements CodeDAO{

    @Autowired
    SqlSessionTemplate sqlSession;

    @Override
    public List<CodeDTO> findCodeList(Code code) {
        try {
            return sqlSession.selectList("com.innon.education.common.mapper.code-mapper.findCodeList", code);
        } catch(MyBatisSystemException e) {
            log.error(e.getCause().getMessage());
            throw new MyBatisSystemException(e);
        }
    }

    @Override
    public int saveCode(Code code) {
        try {
            return sqlSession.insert("com.innon.education.common.mapper.code-mapper.saveCode", code);
        } catch(MyBatisSystemException e) {
            log.error(e.getCause().getMessage());
            throw new MyBatisSystemException(e);
        }
    }

    @Override
    public int updateCode(Code code) {
        try {
            return sqlSession.update("com.innon.education.common.mapper.code-mapper.updateCode", code);
        } catch(MyBatisSystemException e) {
            log.error(e.getCause().getMessage());
            throw new MyBatisSystemException(e);
        }
    }

    @Override
    public int deleteCode(Code code) {
        try {
            return sqlSession.update("com.innon.education.common.mapper.code-mapper.deleteCode", code);
        } catch(MyBatisSystemException e) {
            log.error(e.getCause().getMessage());
            throw new MyBatisSystemException(e);
        }
    }

    @Override
    public CodeDTO findByCodeName(String code_name) {
        try {
            return sqlSession.selectOne("com.innon.education.common.mapper.code-mapper.findByCodeName", code_name);
        } catch (MyBatisSystemException e) {
            log.error(e.getCause().getMessage());
            throw new MyBatisSystemException(e.getCause());
        }
    }

    @Override
    public int editById(CodeDTO codeDto) {
        return 0;
    }

    @Override
    public int delete(int no) {
        return 0;
    }

    @Override
    public List<CodeDTO> findUpdateOrDeleteCodeList(CodeDTO codeDto) {
        return List.of();
    }

    /*@Override
    public int editById(CodeDTO codeDto) {

        try {
            return sqlSession.insert("com.innon.education.common.mapper.code-mapper.findCodeDeptList", codeDto);
        } catch (MyBatisSystemException e) {
            throw new MyBatisSystemException(e.getCause());
        }
    }

    @Override
    public int delete(int no) {
        try {
            return sqlSession.insert("com.innon.education.common.mapper.code-mapper.findCodeDeptList", no);
        } catch (MyBatisSystemException e) {
            throw new MyBatisSystemException(e.getCause());
        }
    }

    @Override
    public ResultDTO save(CodeDTO codeDto) {
        ResultDTO resultDTO = new ResultDTO();
        try {
            Map<String, Object> returnMap = sqlSession.selectOne("com.innon.education.common.mapper.code-mapper.save", codeDto);
            String status = String.valueOf(returnMap.get("status"));
            int returnId = Integer.parseInt(String.valueOf(returnMap.get("id")));

            if(status.equals("INSERT")) {
                resultDTO.setState(true);
                resultDTO.setCode(201);
                resultDTO.setMessage(messageSource.getMessage("api.message.201", new String[]{"코드"}, Locale.KOREA));
                resultDTO.setResult(returnId);
            } else if(status.equals("SELECT")) {
                resultDTO.setState(false);
                resultDTO.setCode(409);
                resultDTO.setMessage(messageSource.getMessage("api.message.409", new String[]{"코드"}, Locale.KOREA));
                resultDTO.setResult(returnId);
            } else {
                resultDTO.setState(false);
                resultDTO.setCode(400);
                resultDTO.setMessage(messageSource.getMessage("api.message.400", new String[]{"코드"}, Locale.KOREA));
                resultDTO.setResult(returnId);
            }

            return resultDTO;
        } catch (MyBatisSystemException e) {
            throw new MyBatisSystemException(e.getCause());
        }
    }

    @Override
    public CodeDTO findByCodeName(String code_name) {
        try {
            return sqlSession.selectOne("com.innon.education.common.mapper.code-mapper.findByCodeName", code_name);
        } catch (MyBatisSystemException e) {
            System.out.println(e.getCause());
            throw new MyBatisSystemException(e.getCause());
        }
    }

    @Override
    public int updateCommonCode(CodeDTO codeDto) {
        try {
            return sqlSession.update("com.innon.education.common.mapper.code-mapper.updateCommonCode", codeDto);
        } catch (MyBatisSystemException e) {
            System.out.println(e.getCause());
            throw new MyBatisSystemException(e.getCause());
        }
    }

    @Override
    public int deleteCommonCode(CodeDTO codeDto) {
        try {
            return sqlSession.update("com.innon.education.common.mapper.code-mapper.deleteCommonCode", codeDto);
        } catch (MyBatisSystemException e) {
            System.out.println(e.getCause());
            throw new MyBatisSystemException(e.getCause());
        }
    }

    @Override
    public List<CodeDTO> findUpdateOrDeleteCodeList(CodeDTO codeDto) {
        try {
            return sqlSession.selectList("com.innon.education.common.mapper.code-mapper.findUpdateOrDeleteCodeList", codeDto);
        } catch (MyBatisSystemException e) {
            System.out.println(e.getCause());
            throw new MyBatisSystemException(e.getCause());
        }
    }*/
}
