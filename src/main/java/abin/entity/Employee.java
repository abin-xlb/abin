package abin.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 员工实体
 */
@Data
public class Employee implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    //账号名
    private String username;
    //名字
    private String name;

    private String password;

    private String phone;

    private String sex;
    //身份证
    private String idNumber;
    //状态
    private Integer status;
    //插入时填充字段
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    //插入和更新时填充字段
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    //插入时填充字段
    @TableField(fill = FieldFill.INSERT)
    private Long createUser;
    //插入和更新时填充字段
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;
}
