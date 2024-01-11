package com.zch.user.domain.po;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zch.common.domain.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * user
 * @author Poison02
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("user")
public class User extends BaseEntity implements Serializable {


    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId("id")
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
    private Boolean type;

    /**
     * 帐号状态;0-正常，1-禁用
     */
    private Boolean status;

    /**
     * 姓名
     */
    private String name;

    /**
     * 性别，0-男，1-女
     */
    private Boolean gender;

    /**
     * 头像链接
     */
    private String avatar;

    /**
     * 邮箱
     */
    private String email;

    /**
     * qq号
     */
    private String qq;

    /**
     * 微信号
     */
    private String wx;

    /**
     * 生日
     */
    private Date birthday;

    /**
     * 地址
     */
    private String address;

    /**
     * ip地址
     */
    private String ipAddress;

    /**
     * 客户端
     */
    private String client;

    /**
     * 个人简介
     */
    private String intro;

    /**
     * 购买课程数量
     */
    private Short courseAmount;

    /**
     * 角色id
     */
    private Boolean roleId;

    /**
     * 是否删除;0-未删除，1-删除
     */
    private Boolean isDelete;

}
