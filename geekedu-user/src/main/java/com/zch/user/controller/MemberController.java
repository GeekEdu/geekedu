package com.zch.user.controller;

import com.zch.api.dto.user.VipForm;
import com.zch.api.vo.book.record.StudyRecordVO;
import com.zch.api.vo.order.OrderVO;
import com.zch.api.vo.user.*;
import com.zch.common.mvc.result.PageResult;
import com.zch.common.mvc.result.Response;
import com.zch.user.service.IUserService;
import com.zch.user.service.IVipInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * @author Poison02
 * @date 2024/2/28
 */
@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor
public class MemberController {

    private final IUserService userService;

    private final IVipInfoService vipInfoService;

    /**
     * 返回总学员数
     * @return
     */
    @GetMapping("/count")
    public Response<Long> getMemberCount() {
        return Response.success(userService.getMemberCount());
    }

    /**
     * 今日注册用户数
     * @return
     */
    @GetMapping("/register/count")
    public Response<Long> todayRegisterCount() {
        return Response.success(userService.todayRegisterCount());
    }

    /**
     * 统计注册用户数
     * @return
     */
    @GetMapping("/stat/regisCount")
    public Response<Map<LocalDate, Long>> statRegisterCount() {
        return Response.success(userService.statRegisterCount());
    }

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
    @GetMapping("/list")
    public Response<MemberFullVO> getMemberList(@RequestParam("pageNum") Integer pageNum,
                                                @RequestParam("pageSize") Integer pageSize,
                                                @RequestParam("sort") String sort,
                                                @RequestParam("order") String order,
                                                @RequestParam(value = "keywords", required = false) String keywords,
                                                @RequestParam(value = "vipId", required = false) Integer vipId,
                                                @RequestParam(value = "tagId", required = false) Integer tagId,
                                                @RequestParam(value = "createdTime", required = false) List<String> createdTime) {
        return Response.success(userService.getMemberPage(pageNum, pageSize, sort, order, keywords, vipId, tagId, createdTime));
    }

    /**
     * 获取vip列表和用户标签
     * @return
     */
    @GetMapping("/vipAndTag/list")
    public Response<VipAndTagVO> getVipAndTagList() {
        return Response.success(userService.getVipAndTagList());
    }

    /**
     * 根据id获取学员明细
     * @param id
     * @return
     */
    @GetMapping("/detail/{id}")
    public Response<UserVO> getMemberById(@PathVariable("id") Long id) {
        return Response.success(userService.getUserById(id));
    }

    /**
     * TODO
     * 获取某个学员详情的订单数据
     * @param id
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping("/detail/{id}/order/list")
    public PageResult<OrderVO> getMemberOrderList(@PathVariable("id") Long id,
                                                  @RequestParam("pageNum") Integer pageNum,
                                                  @RequestParam("pageSize") Integer pageSize) {
        return PageResult.success(userService.getMemberOrderList(id, pageNum, pageSize));
    }

    /**
     * vip列表
     * @return
     */
    @GetMapping("/vip/list")
    public Response<List<VipVO>> getVipList() {
        return Response.success(userService.getVipList());
    }

    /**
     * vip明细
     * @param id
     * @return
     */
    @GetMapping("/vip/{id}")
    public Response<VipVO> getVipById(@PathVariable("id") Integer id) {
        return Response.success(userService.getVipById(id));
    }

    /**
     * 新增vip
     * @return
     */
    @PostMapping("/vip/add")
    public Response<Boolean> addVip(@RequestBody VipForm form) {
        return Response.success(userService.addVip(form));
    }

    /**
     * 删除vip
     * @param id
     * @return
     */
    @PostMapping("/vip/delete/{id}")
    public Response<Boolean> deleteVip(@PathVariable("id") Integer id) {
        return Response.success(userService.deleteVip(id));
    }

    /**
     * 更新vip
     * @param id
     * @return
     */
    @PostMapping("/vip/update/{id}")
    public Response<Boolean> updateVip(@PathVariable("id") Integer id, @RequestBody VipForm form) {
        return Response.success(userService.updateVip(id, form));

    }

    /**
     * 后台 获取教师列表
     * @return
     */
    @GetMapping("/teacher/list")
    public Response<List<UserSimpleVO>> getTeacherList() {
        return Response.success(userService.getTeacherList());
    }

    /**
     * 获取vip价格
     * @param id
     * @return
     */
    @GetMapping("/vip/price")
    public Response<BigDecimal> getVipPriceById(@RequestParam("id") Integer id) {
        return Response.success(vipInfoService.getVipPrice(id));
    }

    /**
     * 判断是否是vip
     * @param id
     * @return
     */
    @GetMapping("/{id}/isVip")
    public Response<Boolean> queryIsVip(@PathVariable("id") Long id) {
        return Response.success(userService.queryIsVip(id));
    }

    /**
     * 获取收藏列表
     * @return
     */
    @GetMapping("/topic/collect/list")
    public Response<List<StudyRecordVO>> queryTopicCollectList(@RequestParam("type") String type) {
        return Response.success(userService.queryCollectList(type));
    }

}
