package com.innon.education.new_education.dao;

import com.innon.education.new_education.dto.CurrentSignDTO;
import com.innon.education.new_education.dto.New_SearchDTO;
import com.innon.education.new_education.dto.New_SearchDTO;

import java.util.List;

public interface New_SignDAO {


    List<CurrentSignDTO> currentSignList(New_SearchDTO searchDTO) throws Exception ;

    List<CurrentSignDTO> planSignDetailList(New_SearchDTO searchDTO);
}
