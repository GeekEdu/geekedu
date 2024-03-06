package com.zch.oss.domain.vo;

import com.zch.common.mvc.entity.BaseVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author Poison02
 * @date 2024/3/6
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class DogeCloudPlayVO extends BaseVO {

    private List<String> playUrl;

    private List<String> size;

    /**
     * 清晰度 高清 720p
     */
    private List<String> name;

    /**
     * 备用播放地址
     */
    private List<String> backupUrl;

}
