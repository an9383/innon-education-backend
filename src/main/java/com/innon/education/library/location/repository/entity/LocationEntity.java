package com.innon.education.library.location.repository.entity;

import com.innon.education.code.controller.dto.CommonDTO;
import com.innon.education.library.location.repository.model.Location;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Data
public class LocationEntity extends CommonDTO {
    private String reg_user_id;
    private String parent_code;
    private String location_name;
    private String location_code;
    private int order_num;
    private String plant_cd;
    private String short_name;
    private char use_flag;
    private int depth_level;
    private Date sys_reg_date;
    private char delete_at;
    private String update_user_id;
    private Date update_date;

    private int id;
    private String replace_location_code;

    private String discription;

    public LocationEntity (
            String reg_user_id,
            String parent_code,
            String location_name,
            String location_code,
            int order_num,
            String plant_cd,
            String short_name,
            char use_flag,
            int depth_level,
            char delete_at,
            String update_user_id
    ) {
        this.reg_user_id = reg_user_id;
        this.parent_code = parent_code;
        this.location_name = location_name;
        this.location_code = location_code;
        this.order_num = order_num;
        this.plant_cd = plant_cd;
        this.short_name = short_name;
        this.use_flag = use_flag;
        this.depth_level = depth_level;
        this.delete_at = delete_at;
        this.update_user_id = update_user_id;
    }

    public LocationEntity (
            String location_code,
            String replace_location_code
    ) {
        this.location_code = location_code;
        this.replace_location_code = replace_location_code;
    }

    public LocationEntity (Location location) {
        this.reg_user_id = location.getSys_reg_user_id();
        this.setSearch_txt(location.getSearch_txt());
        this.setSearch_type(location.getSearch_type());
        this.setGroup_id(location.getGroup_id());
    }
}
