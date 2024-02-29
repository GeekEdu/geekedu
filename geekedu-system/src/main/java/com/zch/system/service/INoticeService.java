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

    List<NoticeVO> getNoticeList();

    NoticeVO getLatestNotice();

    NoticeVO getNoticeById(Integer id);

}
