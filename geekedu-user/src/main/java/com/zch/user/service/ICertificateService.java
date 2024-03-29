package com.zch.user.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zch.api.dto.user.certificate.CertificateForm;
import com.zch.api.vo.user.certificate.CertificateVO;
import com.zch.user.domain.po.Certificate;

/**
 * @author Poison02
 * @date 2024/3/24
 */
public interface ICertificateService extends IService<Certificate> {

    /**
     * 分页查询证书列表
     * @param pageNum
     * @param pageSize
     * @param keywords
     * @return
     */
    Page<CertificateVO> getCertificateList(Integer pageNum, Integer pageSize, String keywords);

    /**
     * 根据id查询证书
     * @param id
     * @return
     */
    CertificateVO getCertificateById(Integer id);

    /**
     * 新增证书
     * @param form
     * @return
     */
    Boolean addCertificate(CertificateForm form);

    /**
     * 编辑证书
     * @param id
     * @param form
     * @return
     */
    Boolean updateCertificate(Integer id, CertificateForm form);

    /**
     * 删除证书
     * @param id
     * @return
     */
    Boolean deleteCertificate(Integer id);

    /**
     * 授予证书
     * @param id
     * @param userId
     * @return
     */
    Boolean conferCertificate(Integer id, Long userId);

    /**
     * 查找我的证书列表
     * @param pageNum
     * @param pageSize
     * @return
     */
    Page<CertificateVO> queryMemberCertificatePage(Integer pageNum, Integer pageSize);

}
