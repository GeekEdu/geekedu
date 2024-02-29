package com.zch.user.controller;

import com.zch.api.vo.order.OrderVO;
import com.zch.api.vo.user.MemberFullVO;
import com.zch.api.vo.user.UserVO;
import com.zch.api.vo.user.VipAndTagVO;
import com.zch.common.mvc.result.PageResult;
import com.zch.common.mvc.result.Response;
import com.zch.user.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Poison02
 * @date 2024/2/28
 */
@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor
public class MemberController {

    private final IUserService userService;

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
    public Response<VipAndTagVO> getVipList() {
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

}