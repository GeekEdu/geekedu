package com.zch.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zch.api.dto.user.certificate.CertificateForm;
import com.zch.api.vo.user.certificate.CertificateVO;
import com.zch.common.core.utils.BeanUtils;
import com.zch.common.core.utils.CollUtils;
import com.zch.common.core.utils.ObjectUtils;
import com.zch.common.redis.utils.StringUtils;
import com.zch.user.domain.dto.CertificateDTO;
import com.zch.user.domain.po.Certificate;
import com.zch.user.domain.po.UserCertificate;
import com.zch.user.mapper.CertificateMapper;
import com.zch.user.service.ICertificateService;
import com.zch.user.service.IUserCertificateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * @author Poison02
 * @date 2024/3/24
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class CertificateServiceImpl extends ServiceImpl<CertificateMapper, Certificate> implements ICertificateService {

    private final IUserCertificateService userCertificateService;

    private final CertificateMapper certificateMapper;

    @Override
    public Page<CertificateVO> getCertificateList(Integer pageNum, Integer pageSize, String keywords) {
        Page<CertificateVO> vo = new Page<>();
        long count = count();
        if (count == 0) {
            vo.setTotal(0);
            vo.setRecords(new ArrayList<>(0));
            return vo;
        }
        LambdaQueryWrapper<Certificate> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(keywords)) {
            wrapper.like(Certificate::getName, keywords);
        }
        Page<Certificate> page = page(new Page<>(pageNum, pageSize), wrapper);
        if (ObjectUtils.isNull(page) || ObjectUtils.isNull(page.getRecords()) || CollUtils.isEmpty(page.getRecords())) {
            vo.setTotal(0);
            vo.setRecords(new ArrayList<>(0));
            return vo;
        }
        vo.setRecords(page.getRecords().stream().map(item -> {
            CertificateVO vo1 = new CertificateVO();
            BeanUtils.copyProperties(item, vo1);
            return vo1;
        }).collect(Collectors.toList()));
        vo.setTotal(count);
        return vo;
    }

    @Override
    public CertificateVO getCertificateById(Integer id) {
        Certificate certificate = getById(id);
        if (ObjectUtils.isNull(certificate)) {
            return new CertificateVO();
        }
        CertificateVO vo = new CertificateVO();
        BeanUtils.copyProperties(certificate, vo);
        return vo;
    }

    @Override
    public Boolean addCertificate(CertificateForm form) {
        Certificate certificate = new Certificate();
        certificate.setName(form.getName());
        certificate.setParams(form.getParams());
        certificate.setTemplateImage(form.getTemplateImage());
        return save(certificate);
    }

    @Override
    public Boolean updateCertificate(Integer id, CertificateForm form) {
        Certificate certificate = getById(id);
        if (ObjectUtils.isNull(certificate)) {
            return false;
        }
        certificate.setName(form.getName());
        certificate.setParams(form.getParams());
        certificate.setTemplateImage(form.getTemplateImage());
        return updateById(certificate);
    }

    @Override
    public Boolean deleteCertificate(Integer id) {
        return removeById(id);
    }

    @Override
    public Boolean conferCertificate(Integer id, Long userId) {
        // 保存用户证书关联表
        // 将证书中的模版信息需要进行更换，更换为相关的用户信息
        // 将该用户的证书上传到minio，后续可在前台进行下载
        return null;
    }

    @Override
    public Page<CertificateVO> queryMemberCertificatePage(Integer pageNum, Integer pageSize) {
        // Long userId = UserContext.getLoginId();
        Long userId = 1745747394693820416L;
        Page<CertificateVO> vo = new Page<>();
        long count = userCertificateService.count(new LambdaQueryWrapper<UserCertificate>()
                .eq(UserCertificate::getUserId, userId));
        if (count == 0) {
            vo.setRecords(new ArrayList<>(0));
            vo.setTotal(0);
            return vo;
        }
        Page<Certificate> page = new Page<>(pageNum, pageSize);
        IPage<CertificateDTO> iPage = certificateMapper.selectCertificatesByUserId(page, userId);
        if (ObjectUtils.isNull(iPage) || ObjectUtils.isNull(iPage.getRecords()) || CollUtils.isEmpty(iPage.getRecords())) {
            vo.setRecords(new ArrayList<>(0));
            vo.setTotal(0);
            return vo;
        }
        vo.setRecords(iPage.getRecords().stream().map(item -> {
            CertificateVO vo1 = new CertificateVO();
            BeanUtils.copyProperties(item, vo1);
            return vo1;
        }).collect(Collectors.toList()));
        vo.setTotal(count);
        return vo;
    }
}
