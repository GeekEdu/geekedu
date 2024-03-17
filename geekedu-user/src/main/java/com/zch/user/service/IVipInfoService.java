package com.zch.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zch.api.dto.user.VipForm;
import com.zch.api.vo.user.VipVO;
import com.zch.user.domain.po.VipInfo;

import java.math.BigDecimal;
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

    /**
     * VIP相关操作
     * @param form
     * @return
     */
    Boolean addVip(VipForm form);

    /**
     * VIP相关操作
     * @param id
     * @return
     */
    Boolean deleteVip(Integer id);

    /**
     * VIP相关操作
     * @param id
     * @param form
     * @return
     */
    Boolean updateVip(Integer id, VipForm form);

    BigDecimal getVipPrice(Integer id);

}
