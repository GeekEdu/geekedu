package com.zch.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zch.api.dto.user.ChangePwdForm;
import com.zch.api.dto.user.LoginForm;
import com.zch.api.vo.user.*;
import com.zch.user.domain.po.User;

import java.util.List;


/**
 * @author Poison02
 * @date 2024/1/11
 */
public interface IUserService extends IService<User> {

    /**
     * 生成验证码
     *
     * @return
     */
    CaptchaVO getCaptcha();

    /**
     * 登录验证
     *
     * @param form
     * @return
     */
    LoginVO login(LoginForm form);

    /**
     * 修改密码
     *
     * @param form
     * @return
     */
    Boolean changePwd(ChangePwdForm form);

    boolean addUser(User user);

    /**
     * 获取用户所有权限
     *
     * @return
     */
    UserRoleVO getUsers();

    /**
     * 通过 userId 查询用户
     *
     * @param userId
     * @return
     */
    UserSimpleVO getUserById(String userId);

    /**
     * 后台返回学员列表
     * @param pageNum
     * @param pageSize
     * @param sort
     * @param order
     * @param keywords
     * @param vipId
     * @param tagId
     * @param createdTime
     * @return
     */
    MemberFullVO getMemberPage(Integer pageNum,
                               Integer pageSize,
                               String sort,
                               String order,
                               String keywords,
                               Integer vipId,
                               Integer tagId,
                               List<String> createdTime);

    /**
     * 获取VIP列表
     * @return
     */
    List<VipVO> getVipList();

    UserVO getUserById(Long id);

}
