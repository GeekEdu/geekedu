package com.zch.user.domain.po;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * user
 * @author Poison02
 */
@Data
public class User implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 主键
     */
    private Long id;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 电话号码
     */
    private String phone;

    /**
     * 密码
     */
    private String password;

    /**
     * 用户类型;0-超级管理员，1-讲师，2-学员
     */
    private Short type;

    /**
     * 帐号状态;0-正常，1-禁用
     */
    private Short status;

    /**
     * 创建人
     */
    private String createdBy;

    /**
     * 创建时间
     */
    private Date createdTime;

    /**
     * 更新人
     */
    private String updatedBy;

    /**
     * 更新时间
     */
    private Date updatedTime;

    /**
     * 是否删除;0-未删除，1-删除
     */
    private Short isDelete;

}
