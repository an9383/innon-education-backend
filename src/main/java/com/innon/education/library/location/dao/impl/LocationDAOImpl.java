package com.innon.education.library.location.dao.impl;

import com.innon.education.library.location.dao.LocationDAO;
import com.innon.education.library.location.repository.dto.LocationDTO;
import com.innon.education.library.location.repository.entity.LocationEntity;
import com.innon.education.library.location.repository.model.Location;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.MyBatisSystemException;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@Slf4j
public class LocationDAOImpl implements LocationDAO {

    @Autowired
    SqlSessionTemplate sqlSession;

    @Override
    public int saveLocationCode(Location location) {
        try {
            return sqlSession.insert("com.innon.education.library.location.mapper.location-mapper.saveLocationCode", location);
        } catch (MyBatisSystemException e) {
            log.error(e.getCause().getMessage());
            throw new MyBatisSystemException(e.getCause());
        }
    }

    @Override
    public Map<String, Object> saveExcelLocationCode(LocationEntity entity) {
        try {
            return sqlSession.selectOne("com.innon.education.library.location.mapper.location-mapper.saveExcelLocationCode", entity);
        } catch(MyBatisSystemException e) {
            log.error(e.getCause().getMessage());
            throw new MyBatisSystemException(e.getCause());
        }
    }

    @Override
    public List<LocationDTO> findLocationList(Location location) {
        try {
            return sqlSession.selectList("com.innon.education.library.location.mapper.location-mapper.findLocationList", location);
        } catch(MyBatisSystemException e) {
            log.error(e.getCause().getMessage());
            throw new MyBatisSystemException(e.getCause());
        }
    }

    @Override
    public String findLocationCodeById(int id) {
        try {
            return sqlSession.selectOne("com.innon.education.library.location.mapper.location-mapper.findLocationCodeById", id);
        } catch(MyBatisSystemException e) {
            log.error(e.getCause().getMessage());
            throw new MyBatisSystemException(e.getCause());
        }
    }

    @Override
    public LocationDTO findLocationCodeByCode(String codeName) {
        try {
            return sqlSession.selectOne("com.innon.education.library.location.mapper.location-mapper.findLocationCodeByCode", codeName);
        } catch(MyBatisSystemException e) {
            log.error(e.getCause().getMessage());
            throw new MyBatisSystemException(e.getCause());
        }
    }

    @Override
    public int updateLocationCodeList(LocationEntity entity) {
        try {
            return sqlSession.update("com.innon.education.library.location.mapper.location-mapper.updateLocationCodeList", entity);
        } catch(MyBatisSystemException e) {
            log.error(e.getCause().getMessage());
            throw new MyBatisSystemException(e.getCause());
        }
    }

    @Override
    public int deleteLocationCodeList(LocationEntity entity) {
        try {
            return sqlSession.update("com.innon.education.library.location.mapper.location-mapper.deleteLocationCodeList", entity);
        } catch(MyBatisSystemException e) {
            log.error(e.getCause().getMessage());
            throw new MyBatisSystemException(e.getCause());
        }
    }

    @Override
    public int deleteTrashLocation() {
        try {
            return sqlSession.delete("com.innon.education.library.location.mapper.location-mapper.deleteTrashLocation");
        } catch(MyBatisSystemException e) {
            log.error(e.getCause().getMessage());
            throw new MyBatisSystemException(e.getCause());
        }
    }

    @Override
    public int updateLocationCode(Location location) {
        try {
            return sqlSession.update("com.innon.education.library.location.mapper.location-mapper.updateLocationCode", location);
        } catch(MyBatisSystemException e) {
            log.error(e.getCause().getMessage());
            throw new MyBatisSystemException(e.getCause());
        }
    }

    @Override
    public int deleteLocationCode(Location location) {
        try {
            return sqlSession.update("com.innon.education.library.location.mapper.location-mapper.deleteLocationCode", location);
        } catch(MyBatisSystemException e) {
            log.error(e.getCause().getMessage());
            throw new MyBatisSystemException(e.getCause());
        }
    }

    @Override
    public String locationFullPath(String locationCode) {
        try {
            return sqlSession.selectOne("com.innon.education.library.location.mapper.location-mapper.locationFullPath", locationCode);
        } catch(MyBatisSystemException e) {
            log.error(e.getCause().getMessage());
            throw new MyBatisSystemException(e.getCause());
        }
    }
}
