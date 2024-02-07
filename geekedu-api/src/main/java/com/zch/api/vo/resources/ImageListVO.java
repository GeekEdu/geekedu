package com.zch.api.vo.resources;

import com.zch.common.mvc.entity.BaseVO;
import com.zch.common.mvc.result.PageResult;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Poison02
 * @date 2024/2/7
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ImageListVO extends BaseVO {

    /**
     * 图片列表
     */
    private PageResult.Data<ImageVO> data = new PageResult.Data<>();

    /**
     * 上传图片来源
     */
    private List<FileFromVO> from = new ArrayList<>(0);

}
