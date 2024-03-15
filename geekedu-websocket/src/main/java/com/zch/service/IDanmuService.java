package com.zch.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zch.api.vo.course.live.LiveDurationVO;
import com.zch.domain.Danmu;

/**
 * @author Poison02
 * @date 2024/3/15
 */
public interface IDanmuService extends IService<Danmu> {

    /**
     * 保存弹幕信息
     * @param duration
     */
    void saveDanmuInfo(Integer courseId, Integer videoId, LiveDurationVO duration);

}
