package com.zch.system.domain.vo;

import com.zch.common.mvc.entity.BaseVO;
import com.zch.system.domain.po.Notice;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author Poison02
 * @date 2024/1/24
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class NoticeVO extends BaseVO {

    private List<Notice> data;

    private Long total;

}
