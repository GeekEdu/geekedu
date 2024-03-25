package com.zch.oss.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zch.api.vo.resources.AttachVO;
import com.zch.oss.domain.po.Attach;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author Poison02
 * @date 2024/3/25
 */
public interface IAttachService extends IService<Attach> {

    /**
     * 附件列表
     * @param courseId
     * @return
     */
    List<AttachVO> queryAttachList(Integer courseId);

    /**
     * 上传课程附件
     * @param file
     * @param name
     * @param courseId
     * @return
     */
    Boolean uploadAttach(MultipartFile file, String name, Integer courseId);

    /**
     * 下载附件
     * @param id
     * @param token
     * @param res
     */
    void downloadAttach(Integer id, String token, HttpServletResponse res);

    /**
     * 删除附件
     * @param id
     * @return
     */
    Boolean deleteAttach(Integer id);

}
