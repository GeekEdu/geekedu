package com.zch.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zch.user.domain.dto.UserCertDTO;
import com.zch.user.domain.po.UserCertificate;
import org.apache.ibatis.annotations.Param;

/**
 * @author Poison02
 * @date 2024/3/29
 */
public interface UserCertificateMapper extends BaseMapper<UserCertificate> {

    /**
     * 查找证书授予用户列表
     * @param page
     * @param cId
     * @return
     */
    IPage<UserCertDTO> selectCertificatesBycId(Page<UserCertificate> page, @Param("cId") Integer cId);

}
