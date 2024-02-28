package com.zch.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zch.api.vo.user.VipVO;
import com.zch.user.domain.po.VipInfo;

import java.util.List;

/**
 * @author Poison02
 * @date 2024/2/28
 */
public interface IVipInfoService extends IService<VipInfo> {

    /**
     * 返回vip信息列表
     * @return
     */
    List<VipVO> getVipList();

    /**
     * 根据id查找vip
     * @param id
     * @return
     */
    VipVO getVipById(Integer id);

}
