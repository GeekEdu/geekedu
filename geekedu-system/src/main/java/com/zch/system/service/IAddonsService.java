package com.zch.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zch.system.domain.po.Addons;
import com.zch.system.domain.vo.AddonsVO;

import java.util.List;

/**
 * @author Poison02
 * @date 2024/1/16
 */
public interface IAddonsService extends IService<Addons> {

    /**
     * 获取权限
     * @return
     */
    List<AddonsVO> getAddons();

}
