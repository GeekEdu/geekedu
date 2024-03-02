package com.zch.api.vo.ask;

import com.zch.api.vo.user.UserSimpleVO;
import com.zch.common.mvc.entity.BaseVO;
import com.zch.common.mvc.result.PageResult;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Poison02
 * @date 2024/3/2
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CommentsFullVO extends BaseVO {

    private PageResult.Data<CommentsSimpleVO> data = new PageResult.Data<>();

    private Map<Long, UserSimpleVO> users = new HashMap<>(0);

}
