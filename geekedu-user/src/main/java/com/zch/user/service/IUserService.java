package com.zch.user.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zch.api.dto.user.*;
import com.zch.api.vo.book.record.StudyRecordVO;
import com.zch.api.vo.order.OrderVO;
import com.zch.api.vo.trade.order.OrderFullVO;
import com.zch.api.vo.user.*;
import com.zch.common.mvc.result.PageResult;
import com.zch.user.domain.po.User;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;


/**
 * @author Poison02
 * @date 2024/1/11
 */
public interface IUserService extends IService<User> {

    /**
     * 获取手机验证码
     * @param phone
     * @return
     */
    String getPhoneCode(String phone);

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
     * 获取用户订单列表
     * @param pageNum
     * @param pageSize
     * @return
     */
    PageResult<OrderFullVO> getOrderPage(Integer pageNum, Integer pageSize);

    /**
     * 前台 签到 返回积分
     * @return
     */
    Integer signIn();

    /**
     * 更新用户积分
     * @param userId
     * @param point
     */
    void updateUserPoint(Long userId, Long point);

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
     * 返回总学员数量
     * @return
     */
    Long getMemberCount();

    /**
     * 今日注册用户数
     * @return
     */
    Long todayRegisterCount();

    /**
     * 统计用户注册数
     * @return
     */
    Map<LocalDate, Long> statRegisterCount();

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

    /**
     * 是否是VIP
     * @param id
     * @return
     */
    Boolean queryIsVip(Long id);

    /**
     * 获取收藏图文列表
     * @return
     */
    List<StudyRecordVO> queryCollectList(String type);

    //===================================================
    // 小程序端

    /**
     * 小程序端登录
     * @param form
     * @return
     */
    WxLoginVO wxLogin(LoginForm form);

    /**
     * 小程序端注册
     * @param form
     * @return
     */
    WxLoginVO wxRegister(WxRegForm form);

    /**
     * 小程序端退出
     * @return
     */
    Boolean wxLogout();
}
