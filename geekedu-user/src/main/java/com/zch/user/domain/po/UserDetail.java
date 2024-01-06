package com.zch.user.domain.po;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * user_detail
 * @author
 */
@Data
public class UserDetail implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 主键
     */
    private Long id;

    /**
     * 名字
     */
    private String name;

    /**
     * 用户类型;0-超级管理员，1-教师，2-学员
     */
    private Short type;

    /**
     * 性别;0-男性，1-女性
     */
    private Short gender;

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
     * 省
     */
    private String province;

    /**
     * 市
     */
    private String city;

    /**
     * 区
     */
    private String district;

    /**
     * 个人简介
     */
    private String intro;

    /**
     * 购买课程数量;只有学员才有
     */
    private Short courseAmount;

    /**
     * 角色id
     */
    private Long roleId;

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
