package com.zch.book.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zch.api.dto.book.AddCommentForm;
import com.zch.api.dto.book.ImageTextForm;
import com.zch.api.vo.book.ImageTextAndCategoryVO;
import com.zch.api.vo.book.ImageTextSingleVO;
import com.zch.api.vo.book.ImageTextVO;
import com.zch.api.vo.book.comment.CommentVO;
import com.zch.book.domain.po.ImageText;

/**
 * @author Poison02
 * @date 2024/2/21
 */
public interface IImageTextService extends IService<ImageText> {

    /**
     * 条件分页查询图文列表
     * @param pageNum
     * @param pageSize
     * @param sort
     * @param order
     * @param keywords
     * @param categoryId
     * @return
     */
    ImageTextAndCategoryVO getImageTextPageByCondition(Integer pageNum, Integer pageSize, String sort, String order,
                                                       String keywords,
                                                       Integer categoryId);

    /**
     * 根据id获取图文信息
     * @param id
     * @return
     */
    ImageTextVO getImageTextById(Integer id);

    /**
     * 根据id删除图文
     * @param id
     * @return
     */
    Boolean deleteImageTextById(Integer id);

    /**
     * 根据id更新图文
     * @param id
     * @param form
     * @return
     */
    Boolean updateImageTextById(Integer id, ImageTextForm form);

    Boolean insertImageText(ImageTextForm form);

    //=================================================================================
    /**
     * 前台 获取图文列表
     * @param pageNum
     * @param pageSize
     * @param scene
     * @param categoryId
     * @return
     */
    ImageTextAndCategoryVO getImageTextList(Integer pageNum, Integer pageSize, String scene, Integer categoryId);

    /**
     * 获取图文明细
     * @param id
     * @return
     */
    ImageTextSingleVO getImageTextDetailById(Integer id);

    /**
     * 获取 图文评论列表
     * @param relationId
     * @param pageNum
     * @param pageSize
     * @param commentId
     * @return
     */
    Page<CommentVO> getImageTextCommentList(Integer relationId, Integer pageNum, Integer pageSize, Integer commentId);

    /**
     * 发表评论
     * @param id
     * @param form
     * @return
     */
    Integer addComment(Integer id, AddCommentForm form);

}
