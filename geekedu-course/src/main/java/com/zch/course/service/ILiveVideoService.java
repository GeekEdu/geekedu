package com.zch.course.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zch.api.dto.course.live.LiveVideoForm;
import com.zch.api.vo.course.live.LiveVideoVO;
import com.zch.course.domain.po.LiveVideo;

import java.util.List;

/**
 * @author Poison02
 * @date 2024/3/12
 */
public interface ILiveVideoService extends IService<LiveVideo> {

    /**
     * 分页查找视频列表
     * @param pageNum
     * @param pageSize
     * @param courseId
     * @return
     */
    Page<LiveVideoVO> getVideoList(Integer pageNum, Integer pageSize, Integer courseId);

    /**
     * 视频明细
     * @param id
     * @return
     */
    LiveVideoVO getVideoDetail(Integer id);

    /**
     * 更新视频
     * @param id
     * @param form
     * @return
     */
    Boolean updateVideo(Integer id, LiveVideoForm form);

    /**
     * 删除视频
     * @param id
     * @return
     */
    Boolean deleteVideo(Integer id);

    /**
     * 新增视频
     * @param form
     * @return
     */
    Boolean addVideo(LiveVideoForm form);

    /**
     * 查询视频列表
     * @param courseId
     * @return
     */
    List<LiveVideoVO> getVideoList(Integer courseId);

}
