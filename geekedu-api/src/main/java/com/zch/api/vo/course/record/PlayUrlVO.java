package com.zch.api.vo.course.record;

import com.zch.common.mvc.entity.BaseVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Poison02
 * @date 2024/3/5
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PlayUrlVO extends BaseVO {

    /**
     * 播放地址
     */
    private List<String> url = new ArrayList<>(0);

}
