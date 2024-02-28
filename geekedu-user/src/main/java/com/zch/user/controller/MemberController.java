package com.zch.user.controller;

import com.zch.api.vo.user.MemberFullVO;
import com.zch.api.vo.user.UserVO;
import com.zch.api.vo.user.VipVO;
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
     * 获取vip列表
     * @return
     */
    @GetMapping("/vip/list")
    public Response<List<VipVO>> getVipList() {
        return Response.success(userService.getVipList());
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

}
