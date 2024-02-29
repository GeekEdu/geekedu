package com.zch.api.vo.user;

import com.zch.common.mvc.entity.BaseVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Poison02
 * @date 2024/2/29
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class VipAndTagVO extends BaseVO {

    private List<VipVO> vip = new ArrayList<>(0);

    private List<TagVO> tag = new ArrayList<>(0);

}
