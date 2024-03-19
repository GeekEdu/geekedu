package com.zch.user.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zch.api.dto.user.*;
import com.zch.api.vo.order.OrderVO;
import com.zch.api.vo.user.*;
import com.zch.user.domain.po.User;
import org.springframework.web.multipart.MultipartFile;

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
     * 前台 手机号-密码登录
     * @param form
     * @return
     */
    LoginVO passwordLogin(PwdLoginForm form);

    /**
     * 前台 手机号-短信验证码登录
     * @param form
     * @return
     */
    LoginVO codeLogin(CodeLoginForm form);

    /**
     * 获取登录用户的明细
     * @return
     */
    UserVO getLoginUserDetail();

    /**
     * 修改头像
     * @param file
     * @return
     */
    Boolean updateUserAvatar(MultipartFile file);

    /**
     * 前台 签到 返回积分
     * @return
     */
    Integer signIn();

    /**
     * 是否签到
     * @return
     */
    Boolean isSign();

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
    VipAndTagVO getVipAndTagList();

    /**
     * 根据用户id获取用户详情
     * @param id
     * @return
     */
    UserVO getUserById(Long id);

    /**
     * TODO
     * 获取某个用户的订单数据
     * @param id
     * @param pageNum
     * @param pageSize
     * @return
     */
    Page<OrderVO> getMemberOrderList(Long id, Integer pageNum, Integer pageSize);

    /**
     * VIP相关操作
     * @return
     */
    List<VipVO> getVipList();

    /**
     * VIP相关操作
     * @param id
     * @return
     */
    VipVO getVipById(Integer id);

    /**
     * VIP相关操作
     * @param form
     * @return
     */
    Boolean addVip(VipForm form);

    /**
     * VIP相关操作
     * @param id
     * @return
     */
    Boolean deleteVip(Integer id);

    /**
     * VIP相关操作
     * @param id
     * @param form
     * @return
     */
    Boolean updateVip(Integer id, VipForm form);

    /**
     * 后台 返回教师列表
     * @return
     */
    List<UserSimpleVO> getTeacherList();

}
