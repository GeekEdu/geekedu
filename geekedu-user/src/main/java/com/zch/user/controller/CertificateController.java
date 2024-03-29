package com.zch.user.controller;

import com.zch.api.dto.user.certificate.CertificateForm;
import com.zch.api.vo.user.certificate.CertificateVO;
import com.zch.common.mvc.result.PageResult;
import com.zch.common.mvc.result.Response;
import com.zch.user.service.ICertificateService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * @author Poison02
 * @date 2024/3/24
 */
@RestController
@RequestMapping("/api/certificate")
@RequiredArgsConstructor
public class CertificateController {

    private final ICertificateService certificateService;

    /**
     * 分页返回证书列表
     * @param pageNum
     * @param pageSize
     * @param keywords
     * @return
     */
    @GetMapping("/list")
    public PageResult<CertificateVO> getCertificatePage(@RequestParam("pageNum") Integer pageNum,
                                                        @RequestParam("pageSize") Integer pageSize,
                                                        @RequestParam(value = "keywords", required = false) String keywords) {
        return PageResult.success(certificateService.getCertificateList(pageNum, pageSize, keywords));
    }

    /**
     * 获取证书详情
     * @param id
     * @return
     */
    @GetMapping("/{id}/detail")
    public Response<CertificateVO> getCertificateDetail(@PathVariable("id") Integer id) {
        return Response.success(certificateService.getCertificateById(id));
    }

    /**
     * 新增证书
     * @param form
     * @return
     */
    @PostMapping("/add")
    public Response<Boolean> addCertificate(@RequestBody CertificateForm form) {
        return Response.success(certificateService.addCertificate(form));
    }

    /**
     * 编辑证书
     * @param id
     * @param form
     * @return
     */
    @PostMapping("/{id}/update")
    public Response<Boolean> updateCertificate(@PathVariable("id") Integer id,
                                               @RequestBody CertificateForm form) {
        return Response.success(certificateService.updateCertificate(id, form));
    }

    /**
     * 删除证书
     * @param id
     * @return
     */
    @PostMapping("/{id}/delete")
    public Response<Boolean> deleteCertificate(@PathVariable("id") Integer id) {
        return Response.success(certificateService.deleteCertificate(id));
    }

    /**
     * 授予某个用户证书 TODO
     * @param id
     * @return
     */
    @PostMapping("/{id}/confer")
    public Response<Boolean> conferCertificate(@PathVariable("id") Integer id, @RequestParam("userId") Long userId) {
        return Response.success(certificateService.conferCertificate(id, userId));
    }

    //=======================================================================
    // 前台 返回 我的证书列表
    @GetMapping("/member/list")
    public PageResult<CertificateVO> getMyCertificatePage(@RequestParam("pageNum") Integer pageNum,
                                                          @RequestParam("pageSize") Integer pageSize) {
        return PageResult.success(certificateService.queryMemberCertificatePage(pageNum, pageSize));
    }

}
