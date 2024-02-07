package com.zch.oss.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zch.api.vo.resources.FileUploadVO;
import com.zch.api.vo.resources.FileVO;
import com.zch.api.vo.resources.ImageListVO;
import com.zch.oss.domain.po.File;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Poison02
 * @date 2024/1/4
 */
public interface IFileService extends IService<File> {

    /**
     * 获得图片列表
     * @param pageNum
     * @param pageSize
     * @return
     */
    ImageListVO getImagesList(Integer pageNum, Integer pageSize);

    /**
     * 上传文件
     * @param file 文件
     * @return
     */
    FileUploadVO uploadFile(MultipartFile file, Integer from);

    /**
     * 获取文件信息
     * @param id 文件id
     * @return
     */
    FileVO getFileInfo(Long id);

    /**
     * 删除文件信息
     * @param id 文件id
     * @return
     */
    Boolean deleteFileInfo(Long id);

}
