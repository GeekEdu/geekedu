package com.zch.api.vo.user;

import com.zch.common.mvc.entity.BaseVO;
import com.zch.common.mvc.result.PageResult;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

/**
 * 学员列表VO
 * @author Poison02
 * @date 2024/2/17
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class MemberFullVO extends BaseVO {

    /**
     * 学员列表数据
     */
    private PageResult.Data<UserVO> data = new PageResult.Data<>();

    /**
     * 会员列表
     */
    private List<VipVO> vip = new ArrayList<>(0);

    /**
     * 学员标签列表
     */
    private List<TagVO> tags = new ArrayList<>(0);

    /**
     * 学员备注信息
     */
    private List<String> userRemarks = new ArrayList<>(0);

}
