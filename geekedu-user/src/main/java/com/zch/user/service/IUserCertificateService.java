package com.zch.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zch.user.domain.po.UserCertificate;

import java.util.List;

/**
 * @author Poison02
 * @date 2024/3/29
 */
public interface IUserCertificateService extends IService<UserCertificate> {

    /**
     * 通过用户id删除
     * @param ids
     * @return
     */
    void deleteBatchByUserId(List<Long> ids);

}
