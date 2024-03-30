package com.zch.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zch.user.domain.po.UserCertificate;
import com.zch.user.mapper.UserCertificateMapper;
import com.zch.user.service.IUserCertificateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Poison02
 * @date 2024/3/29
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class UserCertificateServiceImpl extends ServiceImpl<UserCertificateMapper, UserCertificate> implements IUserCertificateService {
    @Override
    public void deleteBatchByUserId(List<Long> ids) {
        for (Long id : ids) {
            remove(new LambdaQueryWrapper<UserCertificate>()
                    .eq(UserCertificate::getUserId, id));
        }
    }
}
