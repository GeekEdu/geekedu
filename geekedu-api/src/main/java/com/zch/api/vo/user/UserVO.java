package com.zch.api.vo.user;

import com.zch.common.mvc.entity.BaseVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Poison02
 * @date 2024/2/28
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserVO extends BaseVO {

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
     * 盐值
     */
    private String salt;

    /**
     * 密码
     */
    private String password;

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
     * 角色id
     */
    private Integer roleId;

    /**
     * 积分
     */
    private Long points;

    /**
     * vip类型 月度 年度 永久
     */
    private Integer vipId;

    /**
     * vip到期时间 剩余天数
     */
    private LocalDateTime vipExpireTime;

    /**
     * 邀请人数量
     */
    private Integer inviteCount;

    /**
     * 邀请余额
     */
    private BigDecimal inviteAmount;

    /**
     * 是否被冻结 0-否 1-是
     */
    private Boolean isFrozen;

    private LocalDateTime createdTime;

    /**
     * 标签
     */
    private Integer tagId;

    private List<TagVO> tag;

    private VipVO vip;

}
