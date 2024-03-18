package com.zch.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zch.api.vo.system.notice.NoticeVO;
import com.zch.system.domain.po.Notice;

import java.util.List;

/**
 * @author Poison02
 * @date 2024/2/29
 */
public interface INoticeService extends IService<Notice> {

    /**
     * 获取公告列表
     * @return
     */
    List<NoticeVO> getNoticeList();

    /**
     * 获取最新公告
     * @return
     */
    NoticeVO getLatestNotice();

    /**
     * 获取公告详情
     * @param id
     * @return
     */
    NoticeVO getNoticeById(Integer id);

}
