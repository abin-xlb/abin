package abin.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 地址簿
 */
@Data
public class AddressBook implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String areaCode;


    //用户id
    private Long userId;


    //收货人
//    private String consignee;
    private String name;


    //手机号
//    private String phone;
    private String tel;


    //性别 0 女 1 男
    private String sex;


    //省级区划编号
    private String provinceCode;


    //省级名称
//    private String provinceName;
    private String province;



    //市级区划编号
    private String cityCode;


    //市级名称
//    private String cityName;
    private String city;


    //区级区划编号
    private String districtCode;


    //区级名称
//    private String districtName;
    private String county;


    //详细地址
//    private String detail;
    private String addressDetail;


    //标签
    private String label;

    //是否默认 0 否 1是
    private Integer isDefault;

    //创建时间
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;


    //更新时间
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;


    //创建人
    @TableField(fill = FieldFill.INSERT)
    private Long createUser;


    //修改人
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;


    //是否删除
    private Integer isDeleted;
}
