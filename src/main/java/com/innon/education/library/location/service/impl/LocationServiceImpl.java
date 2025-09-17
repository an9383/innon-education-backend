package com.innon.education.library.location.service.impl;

import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.innon.education.auth.entity.User;
import com.innon.education.common.dao.CommonDAO;
import com.innon.education.common.repository.entity.LogEntity;
import com.innon.education.common.service.CommonService;
import com.innon.education.common.util.DataLib;
import com.innon.education.controller.dto.ResultDTO;
import com.innon.education.library.location.dao.LocationDAO;
import com.innon.education.library.location.repository.dto.LocationDTO;
import com.innon.education.library.location.repository.entity.LocationEntity;
import com.innon.education.library.location.repository.model.Location;
import com.innon.education.library.location.service.LocationService;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class LocationServiceImpl implements LocationService {

    private ResultDTO resultDTO;

    @Autowired
    LocationDAO locationDAO;

    @Autowired
    CommonDAO commonDAO;

    @Autowired
    MessageSource messageSource;

    @Autowired
    CommonService commonService;

    @Override
    public ResultDTO saveLocation(Location location, HttpServletRequest request, Authentication auth) {
        resultDTO = new ResultDTO();
        User user = (User) auth.getPrincipal();

        // 로그인유저 plantCd 조회해서 등록 필요
        /*LocationEntity locationEntity = new LocationEntity(
                auth.getName(), location.getParent_code(),
                location.getLocation_name(), location.getLocation_code(),
                location.getOrder_num(), user.getPlant_cd(),
                location.getShort_name(), location.getUse_yn(),
                location.getDepth_level(), location.getDelete_at(),
                ""
        );*/

        location.setSys_reg_user_id(auth.getName());
        int resultId = locationDAO.saveLocationCode(location);
        if(resultId > 0) {
            int logId = commonService.saveLog(LogEntity.builder()
                            .table_id(location.getId())
                            .page_nm("location")
                            .url_addr(request.getRequestURI())
                            .state("insert")
                            .reg_user_id(auth.getName())
                            .build());

            resultDTO.setState(true);
            resultDTO.setCode(201);
            resultDTO.setMessage(messageSource.getMessage("api.message.201", new String[]{"위치코드"}, Locale.KOREA));
            resultDTO.setResult(location.getId());
        } else {
            resultDTO.setState(false);
            resultDTO.setCode(400);
            resultDTO.setMessage(messageSource.getMessage("api.message.400", new String[]{"위치코드"}, Locale.KOREA));
            resultDTO.setResult(null);
        }

        return resultDTO;
    }

    @Override
    public ResultDTO findLocationList(Location location, HttpServletRequest request, Authentication auth) {
        resultDTO = new ResultDTO();
        User user = (User) auth.getPrincipal();
        boolean chkUserRole = auth.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
        if(!chkUserRole) {
            location.setGroupList(commonDAO.findGroupInfoByDeptCd(user.getDept_cd()));
        }
        List<LocationDTO> resultList = locationDAO.findLocationList(location);

        if(!DataLib.isEmpty(resultList)) {
            resultDTO.setState(true);
            resultDTO.setResult(resultList);
        } else {
            resultDTO.setState(false);
        }

        return resultDTO;
    }

    @Override
    public ResultDTO updateLocationCodeList(Location location) {
        resultDTO = new ResultDTO();
        String locationCode = locationDAO.findLocationCodeById(location.getId());
        LocationEntity entity = new LocationEntity(locationCode, location.getReplace_location_code());
        int updateNum = locationDAO.updateLocationCodeList(entity);

        if(updateNum > 0) {
            resultDTO.setState(true);
            resultDTO.setCode(202);
            resultDTO.setMessage(messageSource.getMessage("api.message.202", new String[]{"위치 코드", "수정"}, Locale.KOREA));
            resultDTO.setResult(updateNum);
        } else {
            resultDTO.setState(false);
            resultDTO.setCode(400);
            resultDTO.setMessage(messageSource.getMessage("api.message.400", new String[]{"위치 코드"}, Locale.KOREA));
            resultDTO.setResult(updateNum);
        }

        return resultDTO;
    }

    @Override
    public ResultDTO deleteLocationCodeList(Location location) {
        resultDTO = new ResultDTO();
        String locationCode = locationDAO.findLocationCodeById(location.getId());
        LocationEntity entity = new LocationEntity(locationCode, location.getReplace_location_code());
        int deleteNum = locationDAO.deleteLocationCodeList(entity);

        if(deleteNum > 0) {
            resultDTO.setState(true);
            resultDTO.setCode(202);
            resultDTO.setMessage(messageSource.getMessage("api.message.202", new String[]{"위치 코드", "삭제"}, Locale.KOREA));
            resultDTO.setResult(deleteNum);
        } else {
            resultDTO.setState(false);
            resultDTO.setCode(400);
            resultDTO.setMessage(messageSource.getMessage("api.message.400", new String[]{"위치 코드"}, Locale.KOREA));
            resultDTO.setResult(deleteNum);
        }

        return resultDTO;
    }

    @Override
    public ResultDTO deleteTrashLocation() {
        resultDTO = new ResultDTO();
        int deleteNum = locationDAO.deleteTrashLocation();
        if(deleteNum > 0) {
            resultDTO.setMessage("개발과정 TB_LOCATION 데이터 삭제 완료");
        } else {
            resultDTO.setMessage("TB_LOCATION 데이터 삭제 실패");
        }

        return resultDTO;
    }

    @Override
    public ResultDTO updateLocationCode(Location location, HttpServletRequest request, Authentication auth) {
        resultDTO  = new ResultDTO();

        try {
            location.setSys_reg_user_id(auth.getName());
            int resultId = locationDAO.updateLocationCode(location);
            if(resultId > 0) {
                int logId = commonService.saveLog(LogEntity.builder()
                        .table_id(location.getId())
                        .page_nm("location")
                        .url_addr(request.getRequestURI())
                        .state("update")
                        .reg_user_id(auth.getName())
                        .build());
            }

            resultDTO.setState(true);
            resultDTO.setCode(202);
            resultDTO.setMessage(messageSource.getMessage("api.message.202", new String[]{"위치코드", "수정"}, Locale.KOREA));
            resultDTO.setResult(location.getId());
        } catch (Exception e) {
            resultDTO.setState(false);
            resultDTO.setCode(400);
            resultDTO.setMessage(messageSource.getMessage("api.message.400", new String[]{"위치코드 수정"}, Locale.KOREA));
            resultDTO.setResult(null);
        }

        return resultDTO;
    }

    @Override
    public ResultDTO deleteLocationCode(Location location, HttpServletRequest request, Authentication auth) {
        resultDTO  = new ResultDTO();

        try {
            location.setSys_reg_user_id(auth.getName());
            int resultId = locationDAO.deleteLocationCode(location);
            if(resultId > 0) {
                int logId = commonService.saveLog(LogEntity.builder()
                        .table_id(location.getId())
                        .page_nm("location")
                        .url_addr(request.getRequestURI())
                        .state("delete")
                        .reg_user_id(auth.getName())
                        .build());

                deleteChildren(location.getChildren(), auth.getName());
            }

            resultDTO.setState(true);
            resultDTO.setCode(202);
            resultDTO.setMessage(messageSource.getMessage("api.message.202", new String[]{"위치코드", "삭제"}, Locale.KOREA));
            resultDTO.setResult(location.getId());
        } catch (Exception e) {
            resultDTO.setState(false);
            resultDTO.setCode(400);
            resultDTO.setMessage(messageSource.getMessage("api.message.400", new String[]{"위치코드 삭제"}, Locale.KOREA));
            resultDTO.setResult(null);
        }

        return resultDTO;
    }

    private void deleteChildren(List<Location> children, String user_id) {
        if(children == null) {
            return;
        }

        for(Location child : children) {
            child.setSys_reg_user_id(user_id);
            int resultId = locationDAO.deleteLocationCode(child);
            if(child.getChildren() != null) {
                deleteChildren(child.getChildren(), user_id);
            }
        }
    }
}
