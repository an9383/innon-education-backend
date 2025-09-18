package com.innon.education.code.mapper;

import com.innon.education.common.repository.dto.CodeDTO;
import com.innon.education.common.repository.model.Code;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.StatementType;

import java.util.List;

@Mapper
public interface CodeMapper {
    @Select("select id, discription, code_name, order_num, main_code,short_name, parent_code, use_yn, depth_level   from tb_code")
    public List<CodeDTO> findAll(Code code);

    @Update("update tb_code set name = #{name}, parent_id = #{parent_id}, child_id = #{child_id} where id = #{no}")
    int editById(CodeDTO codeDto);

    @Update("update tb_code set del = true where no = #{no}")
    int delete(int no);

    @SelectKey(statementType = StatementType.PREPARED, statement = "select last_insert_id() as no", keyProperty = "no", before = false, resultType = int.class)
    @Insert("insert into tb_code (discription, code_name, order_num, main_code,short_name, parent_code, use_yn, depth_level) " +
                            "values (#{discription},#{code_name},#{order_num},#{main_code},#{short_name},#{parent_code},#{use_yn},#{depth_level})")
    int save(CodeDTO codeDto);
}
