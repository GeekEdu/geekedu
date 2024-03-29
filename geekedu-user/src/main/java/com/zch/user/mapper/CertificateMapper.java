package com.zch.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zch.user.domain.dto.CertificateDTO;
import com.zch.user.domain.po.Certificate;
import org.apache.ibatis.annotations.Param;

/**
 * @author Poison02
 * @date 2024/3/24
 */
public interface CertificateMapper extends BaseMapper<Certificate> {

    IPage<CertificateDTO> selectCertificatesByUserId(Page<Certificate> page, @Param("userId") Long userId);

}
